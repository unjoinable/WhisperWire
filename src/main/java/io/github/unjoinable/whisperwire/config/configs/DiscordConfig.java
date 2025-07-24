package io.github.unjoinable.whisperwire.config.configs;

import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Configuration for Discord integration.
 *
 * @param token Discord bot token.
 * @param guildId Discord guild/server ID.
 * @param channels Map of channel names to their IDs.
 */
public record DiscordConfig(
        String token,
        String guildId,
        Map<String, String> channels) {

    /**
     * Creates a new builder for {@link DiscordConfig}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link DiscordConfig}.
     */
    public static class Builder {
        private @Nullable String token;
        private @Nullable String guildId;
        private final Map<String, String> channels = new HashMap<>();

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder guildId(String guildId) {
            this.guildId = guildId;
            return this;
        }

        public Builder channel(String name, String id) {
            this.channels.put(name, id);
            return this;
        }

        public DiscordConfig build() {
            Objects.requireNonNull(token, "Discord bot token must not be null");
            Objects.requireNonNull(guildId, "Discord guild ID must not be null");

            return new DiscordConfig(token, guildId, Map.copyOf(channels));
        }
    }
}