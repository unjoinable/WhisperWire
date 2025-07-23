package io.github.unjoinable.whisperwire.listener;

import io.github.unjoinable.whisperwire.core.bridge.Bridge;
import io.github.unjoinable.whisperwire.core.endpoint.Endpoint;
import io.github.unjoinable.whisperwire.core.message.Message;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class GlobalChatListener implements EventListener<PlayerChatEvent> {
    private final Bridge bridge;
    private final Endpoint endpoint;

    public GlobalChatListener(Bridge bridge, Endpoint endpoint) {
        this.bridge = bridge;
        this.endpoint = endpoint;
    }

    @Override
    public @NotNull Class<PlayerChatEvent> eventType() {
        return PlayerChatEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerChatEvent event) {
        var username = event.getPlayer().getUsername();
        var rawMessage = event.getRawMessage();
        var message = Message.of(endpoint.id(), username, rawMessage);
        this.bridge.routeMessage(message);

        return Result.SUCCESS;
    }
}
