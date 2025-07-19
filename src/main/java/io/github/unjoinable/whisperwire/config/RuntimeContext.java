package io.github.unjoinable.whisperwire.config;

import io.github.unjoinable.whisperwire.config.configs.DiscordConfig;
import io.github.unjoinable.whisperwire.config.configs.LoggingConfig;
import io.github.unjoinable.whisperwire.config.configs.MinecraftConfig;
import io.github.unjoinable.whisperwire.config.configs.WebhookConfig;

public record RuntimeContext(
        DiscordConfig discordConfig,
        LoggingConfig loggingConfig,
        MinecraftConfig minecraftConfig,
        WebhookConfig webhookConfig) {}
