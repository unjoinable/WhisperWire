package io.github.unjoinable.whisperwire.core.message;

/**
 * Represents a transformation applied to a {@link Message}, potentially modifying its contents
 * or metadata. This interface is intended for use in transformation pipelines where messages
 * may need to be reformatted, enriched, or cleaned.
 *
 * <p>Transformers must not filter or discard messages; for that, use {@link MessageFilter}.
 */
@FunctionalInterface
public interface MessageTransformer {

    /**
     * Transforms the given message.
     *
     * @param message The original message to transform.
     * @return The transformed message. Must not be {@code null}.
     */
    Message transform(Message message);

    /**
     * Identity transformer that returns messages unchanged.
     */
    MessageTransformer IDENTITY = message -> message;

    /**
     * Chains this transformer with another transformer.
     *
     * @param next The transformer to apply after this one.
     * @return A composed transformer that applies both in sequence.
     */
    default MessageTransformer andThen(MessageTransformer next) {
        return message -> next.transform(this.transform(message));
    }
}