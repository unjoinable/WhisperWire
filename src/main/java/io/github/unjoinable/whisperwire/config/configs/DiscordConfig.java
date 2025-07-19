package io.github.unjoinable.whisperwire.config.configs;

/**
 * Configuration for Discord integration.
 *
 * @param token Discord bot token.
 * @param guildId Discord guild/server ID.
 * @param chatChannelId ID of the chat channel.
 * @param eventsChannelId ID of the events channel.
 */
public record DiscordConfig(
        String token,
        String guildId,
        String chatChannelId,
        String eventsChannelId) {

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
        private String token;
        private String guildId;
        private String chatChannelId;
        private String eventsChannelId;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder guildId(String guildId) {
            this.guildId = guildId;
            return this;
        }

        public Builder chatChannelId(String chatChannelId) {
            this.chatChannelId = chatChannelId;
            return this;
        }

        public Builder eventsChannelId(String eventsChannelId) {
            this.eventsChannelId = eventsChannelId;
            return this;
        }

        public DiscordConfig build() {
            return new DiscordConfig(token, guildId, chatChannelId, eventsChannelId);
        }
    }
}