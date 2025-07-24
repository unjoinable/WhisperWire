package io.github.unjoinable.whisperwire.config.configs;

/**
 * Configuration for Discord webhooks.
 *
 * @param usernameFormat Format string for webhook username.
 * @param avatarUrl URL to use for the webhook avatar.
 */
public record WebhookConfig(String usernameFormat, String avatarUrl) {}
