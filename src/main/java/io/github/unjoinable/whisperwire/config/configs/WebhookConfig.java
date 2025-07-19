package io.github.unjoinable.whisperwire.config.configs;

/**
 * Configuration for Discord webhooks.
 *
 * @param usernameFormat Format string for webhook username.
 * @param avatarUrl URL to use for the webhook avatar.
 */
public record WebhookConfig(
        String usernameFormat,
        String avatarUrl) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String usernameFormat;
        private String avatarUrl;

        public Builder usernameFormat(String usernameFormat) {
            this.usernameFormat = usernameFormat;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public WebhookConfig build() {
            return new WebhookConfig(usernameFormat, avatarUrl);
        }
    }
}
