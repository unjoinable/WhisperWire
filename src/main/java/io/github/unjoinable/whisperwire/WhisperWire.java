package io.github.unjoinable.whisperwire;

import io.github.unjoinable.whisperwire.config.ConfigReader;
import io.github.unjoinable.whisperwire.config.RuntimeContext;
import io.github.unjoinable.whisperwire.discord.DiscordBot;
import io.github.unjoinable.whisperwire.discord.BotLoadingContext;

/**
 * Entry point for initializing and starting the WhisperWire Discord bot.
 * <p>
 * Designed to be used as a library component.
 */
public final class WhisperWire {
    private final RuntimeContext context;
    private final DiscordBot bot;

    /**
     * Constructs a new instance of WhisperWire.
     * Loads configuration and prepares the bot.
     */
    public WhisperWire() {
        this.context = new ConfigReader().load();
        var discordConfig = context.discordConfig();
        this.bot = new DiscordBot(new BotLoadingContext(discordConfig.token(), discordConfig.guildId()));
    }

    /**
     * Starts the WhisperWire bot.
     */
    public void start() {
        bot.start();
    }

    /**
     * Retrieves the current runtime context.
     *
     * @return the loaded {@link RuntimeContext}
     */
    public RuntimeContext context() {
        return context;
    }

    /**
     * Retrieves the underlying Discord bot instance.
     *
     * @return the {@link DiscordBot} instance
     */
    public DiscordBot bot() {
        return bot;
    }
}