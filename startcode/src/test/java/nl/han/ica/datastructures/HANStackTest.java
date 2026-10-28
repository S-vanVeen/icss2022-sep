package nl.han.ica.datastructures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HANStackTest {

    @Test
    void pushThenPeekReturnsTopWithoutRemoving() {
        HANStack<String> stack = new HANStack<>();
        stack.push("Hello");
        assertEquals("Hello", stack.peek());
        assertEquals("Hello", stack.peek());
    }

    @Test
    void popReturnsItemsInReverseOrder() {
        HANStack<Integer> stack = new HANStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    void popOnEmptyStackThrows() {
        HANStack<String> stack = new HANStack<>();
        assertThrows(NoSuchElementException.class, stack::pop);
    }

    @Test
    void peekOnEmptyStackThrows() {
        HANStack<String> stack = new HANStack<>();
        stack.push("a");
        stack.pop();
        assertThrows(NoSuchElementException.class, stack::peek);
    }
}
