package io.github.unjoinable.whisperwire.core.node;

import io.github.unjoinable.whisperwire.core.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DuplexLinkTest {

    private TestDuplexNode nodeA;
    private TestDuplexNode nodeB;
    private DuplexLink link;

    @BeforeEach
    void setup() {
        nodeA = new TestDuplexNode("node-" + UUID.randomUUID());
        nodeB = new TestDuplexNode("node-" + UUID.randomUUID());
        link = new DuplexLink(nodeA, nodeB);
    }

    @Test
    void testForwardFromNodeAtoNodeB() {
        Message msg = new Message("discord", "Alex", "Hey there!", Instant.now());

        link.forward(nodeA, msg).join();

        assertEquals(1, nodeB.getReceivedMessages().size());
        assertEquals(msg, nodeB.getReceivedMessages().getFirst());
    }

    @Test
    void testForwardFromNodeBtoNodeA() {
        Message msg = new Message("minecraft", "Steve", "Sup!", Instant.now());

        link.forward(nodeB, msg).join();

        assertEquals(1, nodeA.getReceivedMessages().size());
        assertEquals(msg, nodeA.getReceivedMessages().getFirst());
    }

    @Test
    void testInvalidNodeThrowsException() {
        TestDuplexNode outsider = new TestDuplexNode("outsider");
        Message msg = new Message("irc", "Intruder", "Oops", Instant.now());

        assertThrows(IllegalArgumentException.class, () -> link.forward(outsider, msg));
    }
}