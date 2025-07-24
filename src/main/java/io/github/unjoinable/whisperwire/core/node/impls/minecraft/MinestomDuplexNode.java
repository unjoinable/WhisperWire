package io.github.unjoinable.whisperwire.core.node.impls.minecraft;

import io.github.unjoinable.whisperwire.core.message.Message;
import io.github.unjoinable.whisperwire.core.node.AbstractDuplexNode;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;

/**
 * A {@link io.github.unjoinable.whisperwire.core.node.DuplexNode} implementation
 * for Minestom that broadcasts external messages (e.g., from Discord)
 * to all connected in-game players.
 */
public class MinestomDuplexNode extends AbstractDuplexNode {
    private static final Component DISCORD_PREFIX = text("[Discord] ", BLUE);
    private final Supplier<Collection<Player>> playerSupplier;

    /**
     * Constructs a new {@code MinestomDuplexNode}.
     *
     * @param playerSupplier a supplier that provides the current online players
     */
    public MinestomDuplexNode(Supplier<Collection<Player>> playerSupplier) {
        super("minestom-" + UUID.randomUUID().toString().substring(0, 8));
        this.playerSupplier = Objects.requireNonNull(playerSupplier);
    }

    /**
     * Asynchronously broadcasts a message to all online players.
     *
     * @param message the message to send; must not be {@code null}
     * @return a {@link CompletableFuture} that completes after the message is broadcast
     */
    @Override
    public CompletableFuture<Void> sendMessage(Message message) {
        Objects.requireNonNull(message, "message must not be null");

        return CompletableFuture.runAsync(() -> {
            Component formatted = DISCORD_PREFIX.append(
                    text(message.username() + ": " + message.rawMessage())
            );
            playerSupplier.get().forEach(player -> player.sendMessage(formatted));
        });
    }
}