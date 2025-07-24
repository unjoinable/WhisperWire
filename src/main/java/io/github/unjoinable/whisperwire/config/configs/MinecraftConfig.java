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
        boolean showJoinLeave) {}