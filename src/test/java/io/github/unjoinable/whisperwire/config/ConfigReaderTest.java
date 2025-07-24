
package io.github.unjoinable.whisperwire.config;

import io.github.unjoinable.whisperwire.config.configs.DiscordConfig;
import io.github.unjoinable.whisperwire.config.configs.LoggingConfig;
import io.github.unjoinable.whisperwire.config.configs.MinecraftConfig;
import io.github.unjoinable.whisperwire.config.configs.WebhookConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ConfigReaderTest {
    @TempDir Path tempDir;

    @Test
    void testLoad_Success() throws IOException {
        Path configFile = tempDir.resolve("config.toml");
        String content = """
            # Discord Bot Settings
            [discord]
            token = "abc123"
            guild_id = "guild1"

            # Minecraft Bridge Settings
            [minecraft]
            enable_chat_bridge = true
            chat_format = "<{username}> {message}"
            discord_to_mc_format = "[D] {username}: {message}"
            show_join_leave = true

            # Webhook Settings (Optional)
            [webhook.formatting]
            username_format = "{username}"
            avatar_url = "https://example.com/{uuid}"

            # Logging Settings
            [logging]
            log_to_file = true
            log_file_path = "logs/test.log"
            """;
        Files.writeString(configFile, content);
        ConfigReader configReader = new ConfigReader(configFile);
        RuntimeContext context = configReader.load();

        assertNotNull(context);

        // Discord Config
        DiscordConfig discord = context.discordConfig();
        assertNotNull(discord);
        assertEquals("abc123", discord.token());
        assertEquals("guild1", discord.guildId());

        // Minecraft Config
        MinecraftConfig mc = context.minecraftConfig();
        assertNotNull(mc);
        assertTrue(mc.enableChatBridge());
        assertEquals("<{username}> {message}", mc.chatFormat());
        assertEquals("[D] {username}: {message}", mc.discordToMcFormat());
        assertTrue(mc.showJoinLeave());

        // Webhook Config
        WebhookConfig webhook = context.webhookConfig();
        assertNotNull(webhook);
        assertEquals("{username}", webhook.usernameFormat());
        assertEquals("https://example.com/{uuid}", webhook.avatarUrl());

        // Logging Config
        LoggingConfig logging = context.loggingConfig();
        assertNotNull(logging);
        assertTrue(logging.logToFile());
        assertEquals("logs/test.log", logging.logFilePath());
    }
}
