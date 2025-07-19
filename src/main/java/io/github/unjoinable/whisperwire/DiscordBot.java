package io.github.unjoinable.whisperwire;

import io.github.unjoinable.whisperwire.config.configs.DiscordConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordBot {
    private static final Logger log = LoggerFactory.getLogger(DiscordBot.class);
    private final DiscordConfig config;
    private @Nullable JDA jda;

    public DiscordBot(DiscordConfig config) {
        this.config = config;
    }

    /**
     * Starts the Discord bot.
     */
    public void start() {
        try {
            this.jda = JDABuilder
                    .createDefault(this.config.token())
                    .build()
                    .awaitReady();
        } catch (InterruptedException | InvalidTokenException e) {
            log.error("Erroring starting discord bot", e);
        }
    }

    /**
     * Shuts down the bot if it is running.
     */
    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
            log.info("Attempting to shutdown discord bot.");
        }
    }
}
