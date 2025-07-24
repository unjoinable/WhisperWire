package io.github.unjoinable.whisperwire.core.node.impls.minecraft;

import io.github.unjoinable.whisperwire.core.message.Message;
import io.github.unjoinable.whisperwire.core.node.AbstractDuplexNode;

import java.util.concurrent.CompletableFuture;

public class MinestomDuplexNode extends AbstractDuplexNode {

    public MinestomDuplexNode(String id) {
        super(id);
    }

    @Override
    public CompletableFuture<Void> sendMessage(Message message) {
        return CompletableFuture.completedFuture(null);
    }
}
