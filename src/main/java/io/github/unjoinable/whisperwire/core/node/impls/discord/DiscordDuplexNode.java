package io.github.unjoinable.whisperwire.core.node.impls.discord;

import io.github.unjoinable.whisperwire.core.message.Message;
import io.github.unjoinable.whisperwire.core.node.AbstractDuplexNode;
import io.github.unjoinable.whisperwire.core.node.DuplexNode;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A {@link DuplexNode} implementation
 * that sends messages to a Discord {@link TextChannel} using JDA.
 *
 * <p>This node formats messages in a simple Discord-friendly format and posts
 * them to the configured text channel.
 */
public class DiscordDuplexNode extends AbstractDuplexNode {
    private final TextChannel channel;

    /**
     * Constructs a new {@code DiscordDuplexNode} for a specific text channel.
     *
     * @param channel the Discord {@link TextChannel} to send messages to; must not be {@code null}
     */
    public DiscordDuplexNode(TextChannel channel) {
        super("discord-" + Objects.requireNonNull(channel, "channel must not be null").getId());
        this.channel = channel;
    }

    /**
     * Sends a message to the Discord channel.
     *
     * <p>If the message is blank, it is silently ignored.
     * Otherwise, the message is formatted as: {@code **[username]** message}
     *
     * @param message the message to send; must not be {@code null}
     * @return a {@link CompletableFuture} that completes when the message is sent
     */
    @Override
    public CompletableFuture<Void> sendMessage(Message message) {
        Objects.requireNonNull(message, "message must not be null");

        if (message.rawMessage().isBlank()) {
            return CompletableFuture.completedFuture(null);
        }

        String formatted = "**[" + message.username() + "]** " + message.rawMessage();

        return channel.sendMessage(formatted)
                .submit()
                .thenAccept(_ -> {}); // We don't care about the Message object here
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DiscordDuplexNode that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), channel);
    }
}