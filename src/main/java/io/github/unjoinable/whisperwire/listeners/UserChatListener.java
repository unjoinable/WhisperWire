package io.github.unjoinable.whisperwire.listeners;

import io.github.unjoinable.whisperwire.config.configs.DiscordConfig;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener that logs messages from a specific guild and channel,
 * ignoring messages from bots, system users, and other channels.
 *
 * <p>This class caches the guild and channel IDs from {@link DiscordConfig}
 * for quick filtering during message events.</p>
 */
public class UserChatListener extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(UserChatListener.class);

    private DiscordConfig config;
    private String cachedGuildId;
    private String cachedChannelId;

    /**
     * Constructs a new {@code UserChatListener} with the provided configuration.
     *
     * @param config the Discord configuration object
     */
    public UserChatListener(DiscordConfig config) {
        this.config = config;
        updateCachedValues();
    }

    /**
     * Called automatically by JDA when a message is received.
     * Filters out messages that:
     * <ul>
     *   <li>Are not from the configured guild</li>
     *   <li>Are not from the configured channel</li>
     *   <li>Are sent by bots or system users</li>
     * </ul>
     * Logs the content of valid messages.
     *
     * @param event the {@link MessageReceivedEvent} triggered by JDA
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!isFromTargetGuild(event) || !isFromTargetChannel(event)) return;

        User user = event.getAuthor();
        if (user.isBot() || user.isSystem()) return;
    }

    /**
     * Updates the internal configuration and refreshes cached values.
     *
     * @param config the new configuration to use
     */
    public void updateConfig(DiscordConfig config) {
        this.config = config;
        updateCachedValues();
    }

    /**
     * Refreshes the cached guild and channel IDs from the current configuration.
     */
    private void updateCachedValues() {
        this.cachedGuildId = config.guildId();
        this.cachedChannelId = config.chatChannelId();
    }

    /**
     * Checks whether the event originates from the configured guild.
     *
     * @param event the message event
     * @return {@code true} if from the target guild; {@code false} otherwise
     */
    private boolean isFromTargetGuild(MessageReceivedEvent event) {
        return event.isFromGuild() && event.getGuild().getId().equals(cachedGuildId);
    }

    /**
     * Checks whether the event originates from the configured channel.
     *
     * @param event the message event
     * @return {@code true} if from the target channel; {@code false} otherwise
     */
    private boolean isFromTargetChannel(MessageReceivedEvent event) {
        return event.getChannel().getId().equals(cachedChannelId);
    }
}