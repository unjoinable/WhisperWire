package io.github.unjoinable.whisperwire.core.endpoint.impls;

import io.github.unjoinable.whisperwire.core.endpoint.AbstractEndpoint;
import io.github.unjoinable.whisperwire.core.message.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.CompletableFuture;

public final class DiscordEndpoint extends AbstractEndpoint {
    private final TextChannel channel;

    public DiscordEndpoint(TextChannel channel) {
        super("discord-" + channel.getId());
        this.channel = channel;
    }

    @Override
    public CompletableFuture<Void> sendMessage(Message message) {
        if (message.rawMessage().isBlank()) {
            return CompletableFuture.completedFuture(null);
        }

        String formatted = "**[" + message.username() + "]** " + message.rawMessage();
        return channel.sendMessage(formatted).submit().thenAccept(_ -> {});
    }
}