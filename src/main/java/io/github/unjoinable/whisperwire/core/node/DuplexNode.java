package io.github.unjoinable.whisperwire.core.node;

import io.github.unjoinable.whisperwire.core.message.Message;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a bidirectional communication endpoint capable of sending and receiving messages.
 *
 * <p>A {@code DuplexNode} serves as a logical bridge between two systems or platforms,
 * providing a consistent interface for message exchange. Each node has a unique identifier
 * and supports asynchronous message delivery.
 */
public interface DuplexNode {

    /**
     * Sends a message to this node.
     *
     * <p>The implementation may perform the send operation either synchronously or asynchronously.
     * This method returns a {@link CompletableFuture} that completes when the message has been
     * successfully processed or a failure occurs during transmission.
     *
     * @param message the {@link Message} to send; must not be {@code null}
     * @return a {@link CompletableFuture} that completes when the message has been handled
     * @throws NullPointerException if {@code message} is {@code null}
     */
    CompletableFuture<Void> sendMessage(Message message);

    /**
     * Returns the unique identifier for this node.
     *
     * <p>The identifier should be stable and unique for the lifetime of the application instance.
     * It is used to distinguish this node from others in the system, regardless of type.
     *
     * @return a non-null, unique identifier string
     */
    String id();
}