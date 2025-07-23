package io.github.unjoinable.whisperwire;

import io.github.unjoinable.whisperwire.config.configs.DiscordConfig;
import io.github.unjoinable.whisperwire.listeners.UserChatListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the lifecycle of a Discord bot using JDA.
 *
 * <p>This class handles startup, graceful shutdown, and access to the underlying
 * {@link JDA} instance. It also registers event listeners needed for message forwarding
 * or processing.
 */
public class DiscordBot {
    private static final Logger log = LoggerFactory.getLogger(DiscordBot.class);

    private final DiscordConfig config;
    private @Nullable JDA jda;

    /**
     * Constructs a new {@code DiscordBot} using the provided configuration.
     *
     * @param config The {@link DiscordConfig} containing the bot token and related settings.
     */
    public DiscordBot(DiscordConfig config) {
        this.config = config;
    }

    /**
     * Starts the Discord bot and initializes the underlying {@link JDA} instance.
     *
     * <p>This method blocks until the bot is fully connected and ready. If the startup fails
     * due to an invalid token or thread interruption, the {@code JDA} instance will remain {@code null},
     * and an error will be logged.
     */
    public void start() {
        try {
            this.jda = JDABuilder
                    .createDefault(this.config.token(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                    .build()
                    .awaitReady();
            this.jda.addEventListener(new UserChatListener(config));
        } catch (InterruptedException | InvalidTokenException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            log.error("Error starting Discord bot", e);
        }
    }

    /**
     * Shuts down the Discord bot if it was previously started.
     *
     * <p>This method gracefully shuts down the {@link JDA} instance, releasing resources
     * and closing connections to Discord.
     */
    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
            log.info("Attempting to shut down Discord bot.");
        }
    }

    /**
     * Returns the {@link JDA} instance associated with this bot, if it was initialized successfully.
     *
     * <p>This may return {@code null} if the bot failed to start due to an invalid token,
     * interrupted thread, or other startup failure.
     *
     * @return The {@link JDA} instance, or {@code null} if the bot is not currently running.
     */
    public @Nullable JDA getJda() {
        return jda;
    }
}
