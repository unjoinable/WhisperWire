package io.github.unjoinable.whisperwire.core.endpoint.impls;

import io.github.unjoinable.whisperwire.core.endpoint.AbstractEndpoint;
import io.github.unjoinable.whisperwire.core.message.Message;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;

public final class MinestomEndpoint extends AbstractEndpoint {
    private static final Component PREFIX = text("[Discord] ", BLUE);

    private final Supplier<Collection<Player>> playerSupplier;

    /**
     * Constructs a MinestomEndpoint with a dynamic player supplier.
     *
     * @param playerSupplier A supplier that returns the current collection of players to send messages to.
     */
    public MinestomEndpoint(Supplier<Collection<Player>> playerSupplier) {
        super("minestom-global");
        this.playerSupplier = playerSupplier;
    }

    @Override
    public CompletableFuture<Void> sendMessage(Message message) {
        return CompletableFuture.runAsync(() -> {
            Component chatMessage = PREFIX.append(text(message.username() + ": " + message.rawMessage()));
            playerSupplier.get().forEach(player -> player.sendMessage(chatMessage));
        });
    }
}