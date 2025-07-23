package io.github.unjoinable.whisperwire.listener;

import io.github.unjoinable.whisperwire.config.configs.DiscordConfig;
import io.github.unjoinable.whisperwire.core.bridge.Bridge;
import io.github.unjoinable.whisperwire.core.endpoint.Endpoint;
import io.github.unjoinable.whisperwire.core.message.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GlobalMessageListener extends ListenerAdapter {
    private final Bridge bridge;
    private final Endpoint endpoint;
    private final DiscordConfig config;

    public GlobalMessageListener(Bridge bridge, Endpoint endpoint, DiscordConfig config) {
        this.bridge = bridge;
        this.endpoint = endpoint;
        this.config = config;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!isFromTargetGuild(event) || !isFromTargetChannel(event)) return;

        User user = event.getAuthor();
        if (user.isBot() || user.isSystem()) return;

        var username = user.getName();
        var rawMessage = event.getMessage().getContentRaw();
        var message = Message.of(endpoint.id(), username, rawMessage);

        this.bridge.routeMessage(message);
    }

    /**
     * Checks whether the event originates from the configured guild.
     *
     * @param event the message event
     * @return {@code true} if from the target guild; {@code false} otherwise
     */
    private boolean isFromTargetGuild(MessageReceivedEvent event) {
        return event.isFromGuild() && event.getGuild().getId().equals(config.guildId());
    }

    /**
     * Checks whether the event originates from the configured channel.
     *
     * @param event the message event
     * @return {@code true} if from the target channel; {@code false} otherwise
     */
    private boolean isFromTargetChannel(MessageReceivedEvent event) {
        return event.getChannel().getId().equals(config.chatChannelId());
    }
}
