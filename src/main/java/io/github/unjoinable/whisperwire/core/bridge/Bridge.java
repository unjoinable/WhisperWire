package io.github.unjoinable.whisperwire.core.bridge;

import io.github.unjoinable.whisperwire.core.message.Message;
import io.github.unjoinable.whisperwire.core.message.MessageFilter;
import io.github.unjoinable.whisperwire.core.message.MessageTransformer;
import io.github.unjoinable.whisperwire.core.endpoint.Endpoint;

import java.util.concurrent.CompletableFuture;

/**
 * Core interface representing a bridge that connects multiple {@link Endpoint}s
 * and facilitates message routing between them.
 *
 * <p>A {@code Bridge} acts as the central coordinator for incoming and outgoing
 * {@link Message} instances. It applies {@link MessageFilter}s and {@link MessageTransformer}s.
 *
 * <p>Implementations should ensure thread-safety and support for asynchronous message dispatching.
 */
public interface Bridge {

    /**
     * Registers a new communication {@link Endpoint} with this bridge.
     *
     * @param endpoint The endpoint to register. Must not be {@code null}.
     */
    void registerEndpoint(Endpoint endpoint);

    /**
     * Unregisters a previously registered endpoint by its unique identifier.
     *
     * @param endpointId The ID of the endpoint to remove.
     */
    void unregisterEndpoint(String endpointId);

    /**
     * Routes a message through the bridge.
     *
     * <p>The bridge applies filters and transformations in the order they were added.
     * If the message is accepted after filtering and transformation, it is forwarded
     * to all appropriate endpoints and receivers.
     *
     * @param message The incoming message to route. Must not be {@code null}.
     * @return A {@link CompletableFuture} that completes when routing has finished.
     */
    CompletableFuture<Void> routeMessage(Message message);

    /**
     * Adds a {@link MessageFilter} to the bridge.
     *
     * <p>Filters are applied in the order they are added. If any filter rejects a message,
     * that message will not be routed further.
     *
     * @param filter The filter to add. Must not be {@code null}.
     */
    void addFilter(MessageFilter filter);

    /**
     * Adds a {@link MessageTransformer} to the bridge.
     *
     * <p>Transformers are applied sequentially. If a transformer returns an empty result,
     * the message is dropped.
     *
     * @param transformer The transformer to add. Must not be {@code null}.
     */
    void addTransformer(MessageTransformer transformer);

    /**
     * Starts the bridge, enabling message routing.
     *
     * <p>This method should be called before routing any messages. It may establish connections
     * or perform initialization logic specific to endpoints.
     */
    void start();

    /**
     * Stops the bridge, disabling message routing and releasing resources.
     *
     * <p>After this method is called, no further messages should be routed
     * until {@link #start()} is called again.
     */
    void stop();

    /**
     * Checks whether the bridge is currently running.
     *
     * @return {@code true} if the bridge is running; {@code false} otherwise.
     */
    boolean isRunning();
}