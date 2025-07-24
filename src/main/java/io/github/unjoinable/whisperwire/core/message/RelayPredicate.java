package io.github.unjoinable.whisperwire.core.message;

import io.github.unjoinable.whisperwire.core.node.DuplexNode;

/**
 * A functional interface that determines whether a {@link Message} should be relayed.
 *
 * <p>{@code RelayPredicate} is used to apply filtering rules to message transmission
 * between {@link DuplexNode}s, either globally or on a per-link basis.
 *
 * <p>This interface is composable using logical operations such as {@code and},
 * {@code or}, and {@code negate}, enabling complex filtering logic to be built
 * from simple, reusable components.
 */
@FunctionalInterface
public interface RelayPredicate {

    /**
     * A predicate that always allows messages to be relayed.
     */
    RelayPredicate ALLOW_ALL = message -> true;

    /**
     * A predicate that always blocks messages from being relayed.
     */
    RelayPredicate DENY_ALL = message -> false;

    /**
     * A predicate that only allows messages with non-blank content.
     */
    RelayPredicate NOT_BLANK = message -> !message.rawMessage().isBlank();

    /**
     * Tests whether the specified message should be relayed.
     *
     * @param message the message to evaluate; must not be {@code null}
     * @return {@code true} if the message should be relayed; {@code false} otherwise
     */
    boolean test(Message message);

    /**
     * Returns a composed predicate that performs a short-circuiting logical AND
     * of this predicate and another.
     *
     * @param other the other predicate to combine with; must not be {@code null}
     * @return a composed predicate that returns {@code true} if both predicates return {@code true}
     * @throws NullPointerException if {@code other} is {@code null}
     */
    default RelayPredicate and(RelayPredicate other) {
        return message -> this.test(message) && other.test(message);
    }

    /**
     * Returns a composed predicate that performs a short-circuiting logical OR
     * of this predicate and another.
     *
     * @param other the other predicate to combine with; must not be {@code null}
     * @return a composed predicate that returns {@code true} if either predicate returns {@code true}
     * @throws NullPointerException if {@code other} is {@code null}
     */
    default RelayPredicate or(RelayPredicate other) {
        return message -> this.test(message) || other.test(message);
    }

    /**
     * Returns a predicate that represents the logical negation of this predicate.
     *
     * @return a predicate that returns {@code true} if this predicate returns {@code false}, and vice versa
     */
    default RelayPredicate negate() {
        return message -> !this.test(message);
    }
}