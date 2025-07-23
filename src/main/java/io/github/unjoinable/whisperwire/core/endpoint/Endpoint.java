package io.github.unjoinable.whisperwire.core.endpoint;

import io.github.unjoinable.whisperwire.core.message.Message;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a communication endpoint capable of sending messages.
 * <p>
 * This interface abstracts destinations such as Discord channels, in-game player groups,
 * or other systems capable of receiving {@link Message} objects.
 * Implementations may represent text channels, user interfaces, bots, or server consoles.
 */
public interface Endpoint {

    /**
     * Sends a message to this endpoint.
     *
     * <p>This method may perform the send operation synchronously or asynchronously,
     * but must return a {@link CompletableFuture} that completes when the send operation
     * has either finished or failed.
     *
     * @param message The {@link Message} to send. Must not be {@code null}.
     * @return A {@link CompletableFuture} that completes when the message has been processed.
     */
    CompletableFuture<Void> sendMessage(Message message);

    /**
     * Returns a unique identifier for this endpoint.
     *
     * <p>This identifier should remain stable across the lifetime of the application
     * and should uniquely distinguish this endpoint from others of the same or different types.
     *
     * @return A unique, non-null identifier string.
     */
    String id();

    /**
     * Indicates whether this endpoint is currently active and available to receive messages.
     *
     * <p>Inactive endpoints may be temporarily unavailable due to disconnection, shutdown,
     * permissions, or other runtime conditions.
     *
     * @return {@code true} if this endpoint is active; {@code false} otherwise.
     */
    default boolean isActive() {
        return true;
    }
}