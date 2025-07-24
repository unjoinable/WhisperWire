package io.github.unjoinable.whisperwire.core.node;

import io.github.unjoinable.whisperwire.core.message.Message;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages active {@link DuplexLink}s between {@link DuplexNode}s and facilitates message relaying.
 */
public class LinkManager {
    private final Set<DuplexLink> links = ConcurrentHashMap.newKeySet();

    /**
     * Establishes a bidirectional link between two nodes.
     *
     * @param a the first node
     * @param b the second node
     * @return {@code true} if a new link was established; {@code false} if already linked or invalid
     * @throws NullPointerException if either node is {@code null}
     */
    public boolean link(DuplexNode a, DuplexNode b) {
        Objects.requireNonNull(a, "node a must not be null");
        Objects.requireNonNull(b, "node b must not be null");

        if (a.id().equals(b.id()) || isLinked(a, b)) return false;

        return links.add(new DuplexLink(a, b));
    }

    /**
     * Removes the link between two nodes, if it exists.
     *
     * @param a one node
     * @param b the other node
     * @return {@code true} if a link was removed
     * @throws NullPointerException if either node is {@code null}
     */
    public boolean unlink(DuplexNode a, DuplexNode b) {
        Objects.requireNonNull(a, "node a must not be null");
        Objects.requireNonNull(b, "node b must not be null");

        return links.removeIf(link -> link.connects(a, b));
    }

    /**
     * Checks whether two nodes are linked.
     *
     * @param a one node
     * @param b the other node
     * @return {@code true} if a link exists between the nodes
     * @throws NullPointerException if either node is {@code null}
     */
    public boolean isLinked(DuplexNode a, DuplexNode b) {
        Objects.requireNonNull(a, "node a must not be null");
        Objects.requireNonNull(b, "node b must not be null");

        return links.stream().anyMatch(link -> link.connects(a, b));
    }

    /**
     * Broadcasts a message from the given source to all linked nodes.
     *
     * <p>Returns a {@link CompletableFuture} that completes when all send operations
     * to linked nodes have completed.
     *
     * @param source  the node sending the message
     * @param message the message to relay
     * @return a {@link CompletableFuture} representing the completion of all relayed messages
     * @throws NullPointerException if either argument is {@code null}
     */
    public CompletableFuture<Void> relay(DuplexNode source, Message message) {
        Objects.requireNonNull(source, "source node must not be null");
        Objects.requireNonNull(message, "message must not be null");

        var futures = links.stream()
                .filter(link -> link.contains(source))
                .map(link -> link.oppositeOf(source).sendMessage(message))
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures);
    }

    /**
     * Returns all currently active links.
     *
     * @return an immutable view of all current links
     */
    public Set<DuplexLink> activeLinks() {
        return Set.copyOf(links);
    }

    /**
     * Removes all links.
     */
    public void reset() {
        links.clear();
    }
}