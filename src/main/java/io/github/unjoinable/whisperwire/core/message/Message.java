package io.github.unjoinable.whisperwire.core.message;

import java.time.Instant;

/**
 * Immutable record representing a message passed between communication endpoints.
 *
 * <p>Each message includes a string-based source identifier, the sender's username,
 * the raw message content, and a timestamp representing when the message was sent or received.
 *
 * <p>This class provides factory methods for convenient construction, supporting
 * flexible message processing scenarios such as logging, replay, or filtering.
 *
 * @param source      A string identifier of the message origin (e.g. "discord-123", "minestom-global").
 * @param username    The username associated with the message sender.
 * @param rawMessage  The unprocessed or raw content of the message.
 * @param timestamp   The exact {@link Instant} the message occurred.
 */
public record Message(
        String source,
        String username,
        String rawMessage,
        Instant timestamp) {

    /**
     * Creates a new message with the current system time as its timestamp.
     *
     * @param source     The string identifier of the message origin.
     * @param username   The username of the sender.
     * @param rawMessage The raw content of the message.
     * @return A new {@code Message} instance with the current timestamp.
     */
    public static Message of(String source, String username, String rawMessage) {
        return new Message(source, username, rawMessage, Instant.now());
    }

    /**
     * Creates a new message with a specific timestamp.
     *
     * @param source     The string identifier of the message origin.
     * @param username   The username of the sender.
     * @param rawMessage The raw content of the message.
     * @param timestamp  The exact {@link Instant} the message occurred.
     * @return A new {@code Message} instance with the specified timestamp.
     */
    public static Message at(String source, String username, String rawMessage, Instant timestamp) {
        return new Message(source, username, rawMessage, timestamp);
    }
}