package io.github.unjoinable.whisperwire.config;

import io.github.unjoinable.whisperwire.config.configs.DiscordConfig;
import io.github.unjoinable.whisperwire.config.configs.LoggingConfig;
import io.github.unjoinable.whisperwire.config.configs.MinecraftConfig;
import io.github.unjoinable.whisperwire.config.configs.WebhookConfig;

/**
 * Represents the fully loaded and validated application configuration at runtime.
 *
 * <p>This class aggregates all subsystem-specific configuration objects such as:
 * <ul>
 *     <li>{@link DiscordConfig} – for Discord bot integration</li>
 *     <li>{@link LoggingConfig} – for logging behavior and log file settings</li>
 *     <li>{@link MinecraftConfig} – for Minecraft ↔ Discord bridge settings</li>
 *     <li>{@link WebhookConfig} – for formatting webhook messages sent to Discord</li>
 * </ul>
 *
 * <p>It is typically created by the {@link ConfigReader} after successfully reading
 * and validating the application's `config.toml` file.
 *
 * @param discordConfig   Configuration for the Discord bot.
 * @param loggingConfig   Configuration related to logging output and file handling.
 * @param minecraftConfig Configuration for the Minecraft integration and chat bridge.
 * @param webhookConfig   Configuration for how webhook messages are formatted.
 */
public record RuntimeContext(
        DiscordConfig discordConfig,
        LoggingConfig loggingConfig,
        MinecraftConfig minecraftConfig,
        WebhookConfig webhookConfig) {}
