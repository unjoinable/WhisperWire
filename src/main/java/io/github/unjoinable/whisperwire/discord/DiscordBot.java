package io.github.unjoinable.whisperwire.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

/**
 * Manages the lifecycle of a Discord bot using JDA.
 *
 * <p>This class handles startup, graceful shutdown, and access to the underlying
 * {@link JDA} instance and target {@link Guild}.
 */
public class DiscordBot {
    private static final Logger log = LoggerFactory.getLogger(DiscordBot.class);

    private final BotLoadingContext context;
    private @Nullable JDA jda;
    private @Nullable Guild guild;

    /**
     * Constructs a new {@code DiscordBot} using the provided context.
     *
     * @param context the {@link BotLoadingContext} providing token and guild ID
     * @throws NullPointerException if the context is {@code null}
     */
    public DiscordBot(BotLoadingContext context) {
        this.context = Objects.requireNonNull(context, "context must not be null");
    }

    /**
     * Starts the Discord bot and initializes the underlying {@link JDA} instance.
     *
     * <p>This method blocks until the bot is fully connected and ready. If startup fails,
     * the {@code JDA} and {@code Guild} references to remain {@code null}.
     */
    public void start() {
        try {
            this.jda = JDABuilder
                    .createDefault(context.botToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                    .build()
                    .awaitReady();

            this.guild = jda.getGuildById(context.guildId());
            if (this.guild == null) {
                log.warn("Guild with ID '{}' not found", context.guildId());
            } else {
                log.info("Successfully connected to guild: {}", guild.getName());
            }
        } catch (InterruptedException | InvalidTokenException e) {
            Thread.currentThread().interrupt();
            log.error("Error starting Discord bot", e);
        }
    }

    /**
     * Shuts down the Discord bot if it was previously started.
     */
    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
            log.info("Attempting to shut down Discord bot.");
        }
    }

    /**
     * Returns the {@link JDA} instance if initialized.
     *
     * @return the {@link JDA}, or {@code null} if not started or failed to start
     */
    public @Nullable JDA getJda() {
        return jda;
    }

    /**
     * Returns the connected {@link Guild}, if available.
     *
     * @return the {@link Guild}, or {@code null} if not found or bot not started
     */
    public @Nullable Guild getGuild() {
        return guild;
    }

    /**
     * Attempts to retrieve a {@link TextChannel} by its ID from the connected guild.
     *
     * @param channelId the ID of the Discord text channel
     * @return an {@link Optional} containing the {@link TextChannel} if found, or empty if not available
     */
    public Optional<TextChannel> textChannelById(String channelId) {
        if (guild == null) return Optional.empty();
        return Optional.ofNullable(guild.getTextChannelById(channelId));
    }
}