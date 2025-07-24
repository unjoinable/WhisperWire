package io.github.unjoinable.whisperwire.config;

import io.github.unjoinable.whisperwire.config.configs.DiscordConfig;
import io.github.unjoinable.whisperwire.config.configs.LoggingConfig;
import io.github.unjoinable.whisperwire.config.configs.MinecraftConfig;
import io.github.unjoinable.whisperwire.config.configs.WebhookConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomlj.Toml;
import org.tomlj.TomlParseError;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Responsible for loading, parsing, and validating the TOML configuration file.
 * Constructs configuration objects required for runtime based on the parsed data.
 */
@SuppressWarnings("DataFlowIssue") // Already validate the fields
public class ConfigReader {
    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);
    private static final Path CONFIG_PATH = Paths.get("config", "config.toml");

    /**
     * List of top-level required keys that must be present in the configuration.
     * Nested keys like [discord.channels_id] are validated separately.
     */
    private static final List<String> REQUIRED_KEYS = Arrays.asList(
            "discord.token",
            "discord.guild_id",
            "minecraft.enable_chat_bridge",
            "minecraft.chat_format",
            "minecraft.discord_to_mc_format",
            "minecraft.show_join_leave",
            "webhook.formatting.username_format",
            "webhook.formatting.avatar_url",
            "logging.log_to_file",
            "logging.log_file_path"
    );

    private final Path configPath;

    /**
     * Constructs a ConfigReader using the default config path: "config/config.toml".
     */
    public ConfigReader() {
        this.configPath = CONFIG_PATH;
    }

    /**
     * Constructs a ConfigReader with a custom path (used for testing or external config setups).
     *
     * @param configPath Path to the TOML configuration file.
     */
    public ConfigReader(Path configPath) {
        this.configPath = configPath;
    }

    /**
     * Loads, validates, and parses the TOML configuration file into a {@link RuntimeContext}.
     *
     * @return a fully populated {@link RuntimeContext} containing all application configurations.
     * @throws IllegalStateException if the config file is missing, invalid, or contains missing keys.
     */
    public RuntimeContext load() {
        log.info("Loading configuration from {}", configPath.toAbsolutePath());

        try {
            TomlParseResult result = Toml.parse(configPath);

            if (result.hasErrors()) {
                String errors = result.errors().stream()
                        .map(TomlParseError::toString)
                        .collect(Collectors.joining(", "));
                log.error("Failed to parse TOML configuration: {}", errors);
                throw new IllegalStateException("Failed to parse TOML configuration: " + errors);
            }

            validateKeys(result);
            log.info("All required configuration keys found.");

            RuntimeContext context = new RuntimeContext(
                    parseDiscordConfig(result),
                    parseLoggingConfig(result),
                    parseMinecraftConfig(result),
                    parseWebhookConfig(result)
            );

            log.info("Configuration loaded successfully.");
            return context;

        } catch (IOException e) {
            throw new IllegalStateException("Failed to read configuration file at: " + configPath.toAbsolutePath(), e);
        }
    }

    /**
     * Validates presence of required keys and the [discord.channels_id] section.
     *
     * @param result Parsed TOML result.
     * @throws IllegalStateException if any required key or section is missing.
     */
    private void validateKeys(TomlParseResult result) {
        for (String key : REQUIRED_KEYS) {
            if (!result.contains(key)) {
                log.error("Missing required configuration key: {}", key);
                throw new IllegalStateException(String.format(
                        "Missing required configuration key: '%s' in %s",
                        key, configPath.toAbsolutePath()
                ));
            }
        }

        TomlTable discord = result.getTable("discord");
        TomlTable channels = discord != null ? discord.getTable("channels_id") : null;

        if (channels == null || channels.isEmpty()) {
            log.error("Missing or empty [discord.channels_id] section.");
            throw new IllegalStateException("Missing or empty [discord.channels_id] section in config file.");
        }
    }

    /**
     * Parses the [discord] and [discord.channels_id] sections into a {@link DiscordConfig}.
     */
    private DiscordConfig parseDiscordConfig(TomlParseResult result) {
        log.debug("Parsing Discord configuration...");
        TomlTable discord = result.getTable("discord");
        TomlTable channels = discord.getTable("channels_id");

        DiscordConfig.Builder builder = DiscordConfig.builder()
                .token(discord.getString("token"))
                .guildId(discord.getString("guild_id"));

        for (String name : channels.keySet()) {
            String id = channels.getString(name);
            if (id != null) {
                log.debug("Mapping Discord channel '{}': {}", name, id);
                builder.channel(name, id);
            }
        }

        return builder.build();
    }

    /**
     * Parses the [minecraft] section into a {@link MinecraftConfig}.
     */
    private MinecraftConfig parseMinecraftConfig(TomlParseResult result) {
        log.debug("Parsing Minecraft configuration...");
        TomlTable mc = result.getTable("minecraft");

        return new MinecraftConfig(
                mc.getBoolean("enable_chat_bridge"),
                mc.getString("chat_format"),
                mc.getString("discord_to_mc_format"),
                mc.getBoolean("show_join_leave")
        );
    }

    /**
     * Parses the [webhook.formatting] section into a {@link WebhookConfig}.
     */
    private WebhookConfig parseWebhookConfig(TomlParseResult result) {
        log.debug("Parsing Webhook configuration...");
        TomlTable formatting = result.getTable("webhook").getTable("formatting");

        return new WebhookConfig(
                formatting.getString("username_format"),
                formatting.getString("avatar_url")
        );
    }

    /**
     * Parses the [logging] section into a {@link LoggingConfig}.
     */
    private LoggingConfig parseLoggingConfig(TomlParseResult result) {
        log.debug("Parsing Logging configuration...");
        TomlTable logging = result.getTable("logging");

        return new LoggingConfig(
                logging.getBoolean("log_to_file"),
                logging.getString("log_file_path")
        );
    }
}