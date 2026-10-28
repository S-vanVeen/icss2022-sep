package nl.han.ica.datastructures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HANQueueTest {

    @Test
    void newQueueIsEmpty() {
        HANQueue<String> queue = new HANQueue<>();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getSize());
    }

    @Test
    void dequeueIsFirstInFirstOut() {
        HANQueue<Integer> queue = new HANQueue<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, queue.getSize());
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    void peekDoesNotRemove() {
        HANQueue<String> queue = new HANQueue<>();
        queue.enqueue("a");
        queue.enqueue("b");
        assertEquals("a", queue.peek());
        assertEquals(2, queue.getSize());
    }

    @Test
    void clearEmptiesQueue() {
        HANQueue<String> queue = new HANQueue<>();
        queue.enqueue("a");
        queue.clear();
        assertTrue(queue.isEmpty());
    }

    @Test
    void dequeueAndPeekOnEmptyThrow() {
        HANQueue<String> queue = new HANQueue<>();
        assertThrows(NoSuchElementException.class, queue::dequeue);
        assertThrows(NoSuchElementException.class, queue::peek);
    }
}
