package io.github.unjoinable.whisperwire.discord;

import java.util.Objects;

/**
 * Holds configuration context required for initializing a Discord integration.
 *
 * <p>This record encapsulates the essential connection parameters:
 * the bot's authentication token and the target guild ID.
 *
 * @param botToken the Discord bot token used for authentication; must not be {@code null}
 * @param guildId the ID of the target Discord guild (server); must not be {@code null}
 */
public record BotLoadingContext(
        String botToken,
        String guildId) {

    /**
     * Constructs a new {@code DiscordLoadingContext}.
     *
     * @param botToken the bot token; must not be {@code null}
     * @param guildId the Discord guild ID; must not be {@code null}
     * @throws NullPointerException if either argument is {@code null}
     */
    public BotLoadingContext {
        Objects.requireNonNull(botToken, "botToken must not be null");
        Objects.requireNonNull(guildId, "guildId must not be null");
    }
}