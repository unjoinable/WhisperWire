package io.github.unjoinable.whisperwire.core.message;

import java.util.function.Predicate;

/**
 * Represents a filter that determines whether a {@link Message} should be accepted
 * based on custom criteria.
 *
 * <p>This interface extends {@link Predicate}&lt;Message&gt;, enabling functional-style
 * filtering and composition using logical operations such as {@code and()}, {@code or()},
 * and {@code negate()}.
 *
 * <p>Filters can be composed and reused across components that process messages,
 * such as moderation systems, message routers, or logging utilities.
 */
public interface MessageFilter extends Predicate<Message> {

    /**
     * A filter that accepts all messages unconditionally.
     */
    MessageFilter ACCEPT_ALL = _ -> true;

    /**
     * Creates a filter that matches messages from a specific source string.
     *
     * @param sourceId The source identifier to match (e.g. "discord-123").
     * @return A {@code MessageFilter} that accepts only messages with the given source ID.
     */
    static MessageFilter bySource(String sourceId) {
        return message -> sourceId.equalsIgnoreCase(message.source());
    }

    /**
     * Creates a filter that matches messages based on their username.
     *
     * @param usernamePredicate A predicate to test the username.
     * @return A {@code MessageFilter} that accepts messages whose username matches the given predicate.
     */
    static MessageFilter byUsername(Predicate<String> usernamePredicate) {
        return message -> usernamePredicate.test(message.username());
    }

    /**
     * Creates a filter that matches messages based on their raw content.
     *
     * @param contentPredicate A predicate to test the raw message content.
     * @return A {@code MessageFilter} that accepts messages whose content matches the given predicate.
     */
    static MessageFilter byContent(Predicate<String> contentPredicate) {
        return message -> contentPredicate.test(message.rawMessage());
    }

    /**
     * Combines this filter with another filter using logical AND.
     *
     * <p>The resulting filter accepts a message only if both this filter and the
     * {@code other} filter accept it.
     *
     * @param other The other filter to combine with.
     * @return A new {@code MessageFilter} that performs logical AND between the two filters.
     */
    @Override
    default MessageFilter and(Predicate<? super Message> other) {
        return message -> this.test(message) && other.test(message);
    }

    /**
     * Combines this filter with another filter using logical OR.
     *
     * <p>The resulting filter accepts a message if either this filter or the
     * {@code other} filter accepts it.
     *
     * @param other The other filter to combine with.
     * @return A new {@code MessageFilter} that performs logical OR between the two filters.
     */
    @Override
    default MessageFilter or(Predicate<? super Message> other) {
        return message -> this.test(message) || other.test(message);
    }

    /**
     * Returns a filter that negates this filter.
     *
     * <p>The resulting filter accepts a message only if this filter rejects it.
     *
     * @return A new {@code MessageFilter} that negates this filter.
     */
    @Override
    default MessageFilter negate() {
        return message -> !this.test(message);
    }
}