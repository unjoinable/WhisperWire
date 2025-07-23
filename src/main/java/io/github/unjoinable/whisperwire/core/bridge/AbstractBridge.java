package io.github.unjoinable.whisperwire.core.bridge;

import io.github.unjoinable.whisperwire.core.endpoint.Endpoint;
import io.github.unjoinable.whisperwire.core.message.Message;
import io.github.unjoinable.whisperwire.core.message.MessageFilter;
import io.github.unjoinable.whisperwire.core.message.MessageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract base implementation of the {@link Bridge} interface.
 * <p>
 * This class provides core functionality for managing {@link Endpoint}s, message {@link MessageFilter}s,
 * and {@link MessageTransformer}s, and implements the logic for asynchronously routing messages
 * between endpoints.
 * <p>
 * Subclasses may override behavior as needed, but can usually rely on the provided
 * filtering, transformation, and routing logic as-is.
 */
public abstract class AbstractBridge implements Bridge {
    protected final Map<String, Endpoint> endpoints;
    protected final List<MessageFilter> filters;
    protected final List<MessageTransformer> transformers;
    protected final AtomicBoolean running;

    /**
     * Creates a new {@code AbstractBridge} with initialized components.
     * Adds a default filter to ensure only messages with a known source ID are accepted.
     */
    protected AbstractBridge() {
        this.endpoints = new ConcurrentHashMap<>();
        this.filters = new CopyOnWriteArrayList<>();
        this.transformers = new CopyOnWriteArrayList<>();
        this.running = new AtomicBoolean(false);
    }

    /**
     * Registers a new endpoint. If an endpoint with the same ID exists, it is replaced.
     *
     * @param endpoint The endpoint to register.
     */
    @Override
    public void registerEndpoint(Endpoint endpoint) {
        endpoints.put(endpoint.id(), endpoint);
    }

    /**
     * Unregisters an endpoint by its ID.
     *
     * @param endpointId The ID of the endpoint to remove.
     */
    @Override
    public void unregisterEndpoint(String endpointId) {
        endpoints.remove(endpointId);
    }

    /**
     * Routes a message through filters and transformers, then dispatches to eligible endpoints.
     *
     * @param message The message to route.
     * @return A {@link CompletableFuture} that completes when all endpoint dispatches are done.
     */
    @Override
    public CompletableFuture<Void> routeMessage(Message message) {
        if (!running.get()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Bridge is not running"));
        }

        if (!passesFilters(message)) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture
                .supplyAsync(() -> applyTransformers(message))
                .thenCompose(this::dispatchToEndpoints);
    }

    /**
     * Adds a message filter to the bridge. Filters are evaluated in the order added.
     *
     * @param filter The filter to add.
     */
    @Override
    public void addFilter(MessageFilter filter) {
        filters.add(filter);
    }

    /**
     * Adds a message transformer to the bridge. Transformers are applied in order.
     *
     * @param transformer The transformer to add.
     */
    @Override
    public void addTransformer(MessageTransformer transformer) {
        transformers.add(transformer);
    }

    /**
     * Starts the bridge. Further calls without stopping will throw.
     */
    @Override
    public void start() {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("Bridge is already running");
        }
    }

    /**
     * Stops the bridge. Further calls without starting will throw.
     */
    @Override
    public void stop() {
        if (!running.compareAndSet(true, false)) {
            throw new IllegalStateException("Bridge is not currently running");
        }
    }

    /**
     * Checks if the bridge is currently running.
     *
     * @return {@code true} if running; {@code false} otherwise.
     */
    @Override
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Applies all registered filters to the given message.
     *
     * @param message The message to test.
     * @return {@code true} if the message passes all filters; otherwise {@code false}.
     */
    protected boolean passesFilters(Message message) {
        for (MessageFilter filter : filters) {
            if (!filter.test(message)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Applies all registered transformers to the message in sequence.
     *
     * @param original The original message.
     * @return The final transformed message.
     */
    protected Message applyTransformers(Message original) {
        Message current = original;
        for (MessageTransformer transformer : transformers) {
            current = transformer.transform(current);
        }
        return current;
    }

    /**
     * Dispatches a message to all eligible endpoints.
     *
     * @param message The message to send.
     * @return A {@link CompletableFuture} that completes when all sends are done.
     */
    protected CompletableFuture<Void> dispatchToEndpoints(Message message) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Endpoint endpoint : endpoints.values()) {
            if (shouldSkip(endpoint, message)) continue;
            futures.add(endpoint.sendMessage(message));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    /**
     * Determines whether an endpoint should be skipped for a given message.
     * <ul>
     *     <li>Skips if the endpoint has the same source as the message.</li>
     *     <li>Skips if the endpoint is inactive.</li>
     * </ul>
     *
     * @param endpoint The endpoint to check.
     * @param message  The message being routed.
     * @return {@code true} if the endpoint should be skipped; otherwise {@code false}.
     */
    protected boolean shouldSkip(Endpoint endpoint, Message message) {
        return endpoint.id().equalsIgnoreCase(message.source()) || !endpoint.isActive();
    }
}