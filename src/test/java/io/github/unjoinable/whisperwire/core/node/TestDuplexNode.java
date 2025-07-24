package io.github.unjoinable.whisperwire.core.node;

import io.github.unjoinable.whisperwire.core.message.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A test implementation of {@link AbstractDuplexNode} for unit testing.
 * It stores received messages in-memory.
 */
public class TestDuplexNode extends AbstractDuplexNode {

    private final List<Message> receivedMessages = new ArrayList<>();

    public TestDuplexNode(String id) {
        super(id);
    }

    @Override
    public CompletableFuture<Void> sendMessage(@NotNull Message message) {
        receivedMessages.add(message);
        return CompletableFuture.completedFuture(null);
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void clearMessages() {
        receivedMessages.clear();
    }
}