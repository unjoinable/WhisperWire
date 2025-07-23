package io.github.unjoinable.whisperwire.core.endpoint;

import io.github.unjoinable.whisperwire.core.message.Message;

/**
 * Abstract base class for implementing {@link Endpoint}s.
 *
 * <p>This class provides common functionality for endpoints that send messages,
 * including storage of a unique identifier and active state tracking.
 *
 * <p>The {@code id} serves both as the unique identifier and as the logical
 * source name used for filtering, routing, or identification purposes.
 *
 * <p>Subclasses must implement {@link #sendMessage(Message)} to define how messages
 * are actually delivered to the destination.
 */
public abstract class AbstractEndpoint implements Endpoint {
    protected final String id;
    protected volatile boolean active = true;

    /**
     * Constructs a new {@code AbstractEndpoint} with the given identifier.
     *
     * @param id The unique and descriptive identifier for this endpoint (e.g., "discord-main", "game-chat").
     */
    protected AbstractEndpoint(String id) {
        this.id = id;
    }

    /**
     * Returns the unique identifier of this endpoint.
     *
     * <p>This ID is also used as the source identifier for message routing and filtering.
     *
     * @return The identifier string for this endpoint.
     */
    @Override
    public String id() {
        return id;
    }

    /**
     * Indicates whether this endpoint is currently active and able to receive messages.
     *
     * <p>This flag can be used to temporarily disable the endpoint without unregistering it.
     *
     * @return {@code true} if the endpoint is active; {@code false} if inactive.
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether this endpoint is active.
     *
     * @param active {@code true} to activate the endpoint; {@code false} to deactivate it.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}