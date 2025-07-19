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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigReader.class);
    private static final Path CONFIG_PATH = Paths.get("config", "config.toml");
    private static final List<String> REQUIRED_KEYS = Arrays.asList(
            "discord.token", "discord.guild_id", "discord.chat_channel_id",
            "minecraft.enable_chat_bridge", "minecraft.chat_format", "minecraft.discord_to_mc_format", "minecraft.show_join_leave",
            "webhook.formatting.username_format", "webhook.formatting.avatar_url",
            "logging.log_to_file", "logging.log_file_path"
    );

    private final Path configPath;

    /**
     * Default constructor — uses the hardcoded config path.
     */
    public ConfigReader() {
        this.configPath = CONFIG_PATH;
    }

    /**
     * Custom constructor for testing or custom config paths.
     *
     * @param configPath The path to the config file.
     */
    public ConfigReader(Path configPath) {
        this.configPath = configPath;
    }

    /**
     * Reads, parses, and validates the TOML configuration file.
     *
     * @return A fully populated {@link RuntimeContext} if the configuration is valid.
     * @throws IllegalStateException if the file is not found, contains parsing errors,
     *                               or is missing required keys.
     */
    @SuppressWarnings("DataFlowIssue") // Already validated the keys.
    public RuntimeContext load() {
        try {
            TomlParseResult result = Toml.parse(configPath);

            if (result.hasErrors()) {
                String errors = result.errors().stream()
                        .map(TomlParseError::toString)
                        .collect(Collectors.joining(", "));
                throw new IllegalStateException("Failed to parse TOML configuration: " + errors);
            }

            validateKeys(result);

            DiscordConfig discordConfig = new DiscordConfig.Builder()
                    .token(result.getString("discord.token"))
                    .guildId(result.getString("discord.guild_id"))
                    .chatChannelId(result.getString("discord.chat_channel_id"))
                    .eventsChannelId(result.getString("discord.events_channel_id"))
                    .build();

            MinecraftConfig minecraftConfig = new MinecraftConfig.Builder()
                    .enableChatBridge(result.getBoolean("minecraft.enable_chat_bridge"))
                    .chatFormat(result.getString("minecraft.chat_format"))
                    .discordToMcFormat(result.getString("minecraft.discord_to_mc_format"))
                    .showJoinLeave(result.getBoolean("minecraft.show_join_leave"))
                    .build();

            WebhookConfig webhookConfig = new WebhookConfig.Builder()
                    .usernameFormat(result.getString("webhook.formatting.username_format"))
                    .avatarUrl(result.getString("webhook.formatting.avatar_url"))
                    .build();

            LoggingConfig loggingConfig = new LoggingConfig.Builder()
                    .logToFile(result.getBoolean("logging.log_to_file"))
                    .logFilePath(result.getString("logging.log_file_path"))
                    .build();

            LOGGER.info("Configuration loaded successfully from {}", CONFIG_PATH.toAbsolutePath());
            return new RuntimeContext(discordConfig, loggingConfig, minecraftConfig, webhookConfig);

        } catch (IOException e) {
            throw new IllegalStateException("Failed to read configuration file at: " + CONFIG_PATH.toAbsolutePath(), e);
        }
    }

    /**
     * Validates that all required keys are present in the parsed TOML result.
     *
     * @param result The parsed TOML result to validate.
     * @throws IllegalStateException if a required key is missing.
     */
    private void validateKeys(TomlParseResult result) {
        for (String key : REQUIRED_KEYS) {
            if (!result.contains(key)) {
                throw new IllegalStateException(String.format("Missing required configuration key: '%s' in %s", key, CONFIG_PATH.toAbsolutePath()));
            }
        }
    }
}
