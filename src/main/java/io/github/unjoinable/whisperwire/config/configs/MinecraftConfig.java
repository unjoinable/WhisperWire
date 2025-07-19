package io.github.unjoinable.whisperwire.config.configs;

/**
 * Configuration for Minecraft integration.
 *
 * @param enableChatBridge Whether the chat bridge is enabled.
 * @param chatFormat Format for Minecraft chat messages.
 * @param discordToMcFormat Format for Discord messages shown in Minecraft.
 * @param showJoinLeave Whether to show join/leave messages in chat.
 */
public record MinecraftConfig(
        boolean enableChatBridge,
        String chatFormat,
        String discordToMcFormat,
        boolean showJoinLeave) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean enableChatBridge;
        private String chatFormat;
        private String discordToMcFormat;
        private boolean showJoinLeave;

        public Builder enableChatBridge(boolean enableChatBridge) {
            this.enableChatBridge = enableChatBridge;
            return this;
        }

        public Builder chatFormat(String chatFormat) {
            this.chatFormat = chatFormat;
            return this;
        }

        public Builder discordToMcFormat(String discordToMcFormat) {
            this.discordToMcFormat = discordToMcFormat;
            return this;
        }

        public Builder showJoinLeave(boolean showJoinLeave) {
            this.showJoinLeave = showJoinLeave;
            return this;
        }

        public MinecraftConfig build() {
            return new MinecraftConfig(enableChatBridge, chatFormat, discordToMcFormat, showJoinLeave);
        }
    }
}