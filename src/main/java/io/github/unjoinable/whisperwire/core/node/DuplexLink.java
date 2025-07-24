package io.github.unjoinable.whisperwire.core.node;

import io.github.unjoinable.whisperwire.core.message.Message;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Establishes a bidirectional message link between two {@link DuplexNode} instances.
 *
 * <p>This class forwards messages sent to one node to the other, effectively acting
 * as a bridge between the two endpoints. The connection is symmetric and supports
 * asynchronous message delivery.
 *
 * @param nodeA the first {@link DuplexNode}
 * @param nodeB the second {@link DuplexNode}
 */
public record DuplexLink(DuplexNode nodeA, DuplexNode nodeB) {

    /**
     * Constructs a {@code DuplexLink} between two nodes.
     *
     * @throws IllegalArgumentException if both nodes have the same ID
     * @throws NullPointerException if either node is {@code null}
     */
    public DuplexLink {
        Objects.requireNonNull(nodeA, "nodeA must not be null");
        Objects.requireNonNull(nodeB, "nodeB must not be null");

        if (Objects.equals(nodeA.id(), nodeB.id())) {
            throw new IllegalArgumentException("Cannot link nodes with identical IDs: " + nodeA.id());
        }
    }

    /**
     * Forwards a {@link Message} from the origin node to the opposite endpoint.
     *
     * <p>This method determines the direction based on the origin node's ID.
     *
     * @param fromNode the origin {@link DuplexNode}
     * @param message  the {@link Message} to forward
     * @return a {@link CompletableFuture} representing the completion of the send operation
     * @throws IllegalArgumentException if {@code fromNode} is not part of this link
     */
    public CompletableFuture<Void> forward(DuplexNode fromNode, Message message) {
        return switch (fromNode.id()) {
            case String idA when idA.equals(nodeA.id()) -> nodeB.sendMessage(message);
            case String idB when idB.equals(nodeB.id()) -> nodeA.sendMessage(message);
            default -> throw new IllegalArgumentException("Unknown node: " + fromNode.id());
        };
    }

    /**
     * Checks whether the given node is part of this link.
     *
     * @param node the {@link DuplexNode} to check
     * @return {@code true} if the node is linked, {@code false} otherwise
     */
    public boolean contains(DuplexNode node) {
        return node.id().equals(nodeA.id()) || node.id().equals(nodeB.id());
    }

    /**
     * Returns the opposite node in the link.
     *
     * @param node the node whose counterpart is to be found
     * @return the opposite {@link DuplexNode}
     * @throws IllegalArgumentException if the given node is not part of this link
     */
    public DuplexNode oppositeOf(DuplexNode node) {
        return switch (node.id()) {
            case String id when id.equals(nodeA.id()) -> nodeB;
            case String id when id.equals(nodeB.id()) -> nodeA;
            default -> throw new IllegalArgumentException("Node is not part of this link: " + node.id());
        };
    }
}