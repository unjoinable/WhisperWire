package io.github.unjoinable.whisperwire.core.node;

import io.github.unjoinable.whisperwire.core.message.Message;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for implementations of {@link DuplexNode}, providing common functionality
 * such as identifier management and utility methods.
 *
 * <p>Subclasses must implement the {@link #sendMessage(Message)} method to define
 * how messages are handled by the specific node type.
 */
public abstract class AbstractDuplexNode implements DuplexNode {
    private final String id;

    /**
     * Constructs a new {@code AbstractDuplexNode} with the given unique identifier.
     *
     * @param id the unique identifier for this node; must not be {@code null} or blank
     * @throws NullPointerException     if {@code id} is {@code null}
     * @throws IllegalArgumentException if {@code id} is blank
     */
    protected AbstractDuplexNode(String id) {
        Objects.requireNonNull(id, "id must not be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String id() {
        return id;
    }

    /**
     * Sends a message to this node.
     *
     * <p>Subclasses must implement this method to handle message processing logic.
     *
     * @param message the message to send
     * @return a {@link CompletableFuture} representing the outcome of the send operation
     */
    @Override
    public abstract CompletableFuture<Void> sendMessage(Message message);

    /**
     * Returns a string representation of this node.
     *
     * @return a string containing the class name and ID
     */
    @Override
    public String toString() {
        return "%s[id=%s]".formatted(getClass().getSimpleName(), id);
    }

    /**
     * Equality is based on node ID.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof DuplexNode other && Objects.equals(id, other.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}