package io.github.unjoinable.whisperwire.core.node;

import io.github.unjoinable.whisperwire.core.message.Message;
import io.github.unjoinable.whisperwire.core.message.RelayPredicate;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Establishes a bidirectional message link between two {@link DuplexNode} instances,
 * with optional message filtering via a {@link RelayPredicate}.
 *
 * <p>This class forwards messages sent to one node to the other, optionally applying
 * a filter to determine whether the message should be relayed.
 *
 * @param nodeA     the first {@link DuplexNode}
 * @param nodeB     the second {@link DuplexNode}
 * @param predicate the {@link RelayPredicate} used to determine whether messages
 *                  should be relayed across this link
 */
public record DuplexLink(
        DuplexNode nodeA,
        DuplexNode nodeB,
        RelayPredicate predicate) {

    /**
     * Constructs a {@code DuplexLink} between two nodes with a default allow-all filter.
     *
     * @throws IllegalArgumentException if both nodes have the same ID
     * @throws NullPointerException if either node is {@code null}
     */
    public DuplexLink {
        Objects.requireNonNull(nodeA, "nodeA must not be null");
        Objects.requireNonNull(nodeB, "nodeB must not be null");
        Objects.requireNonNull(predicate, "predicate must not be null");

        if (Objects.equals(nodeA.id(), nodeB.id())) {
            throw new IllegalArgumentException("Cannot link nodes with identical IDs: " + nodeA.id());
        }
    }

    /**
     * Constructs a {@code DuplexLink} with default allow-all predicate.
     */
    public DuplexLink(DuplexNode nodeA, DuplexNode nodeB) {
        this(nodeA, nodeB, RelayPredicate.ALLOW_ALL);
    }

    /**
     * Forwards a {@link Message} from the origin node to the opposite endpoint,
     * if allowed by the {@link RelayPredicate}.
     *
     * @param fromNode the origin {@link DuplexNode}
     * @param message  the {@link Message} to forward
     * @return a {@link CompletableFuture} representing the send operation; completes immediately if blocked
     * @throws IllegalArgumentException if {@code fromNode} is not part of this link
     */
    public CompletableFuture<Void> forward(DuplexNode fromNode, Message message) {
        DuplexNode target = oppositeOf(fromNode);

        if (!predicate.test(message)) {
            return CompletableFuture.completedFuture(null); // Blocked by predicate
        }

        return target.sendMessage(message);
    }

    /**
     * Checks whether the given node is part of this link.
     */
    public boolean contains(DuplexNode node) {
        return node.id().equals(nodeA.id()) || node.id().equals(nodeB.id());
    }

    /**
     * Returns the opposite node in the link.
     */
    public DuplexNode oppositeOf(DuplexNode node) {
        return switch (node.id()) {
            case String id when id.equals(nodeA.id()) -> nodeB;
            case String id when id.equals(nodeB.id()) -> nodeA;
            default -> throw new IllegalArgumentException("Node is not part of this link: " + node.id());
        };
    }

    /**
     * Checks whether this link connects the two given nodes, regardless of order.
     */
    public boolean connects(DuplexNode a, DuplexNode b) {
        String id1 = a.id();
        String id2 = b.id();
        return (nodeA.id().equals(id1) && nodeB.id().equals(id2)) ||
                (nodeA.id().equals(id2) && nodeB.id().equals(id1));
    }
}