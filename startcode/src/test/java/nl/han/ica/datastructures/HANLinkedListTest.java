package nl.han.ica.datastructures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HANLinkedListTest {

    @Test
    void addFirstPutsItemAtFront() {
        HANLinkedList<String> list = new HANLinkedList<>();
        list.addFirst("b");
        list.addFirst("a");
        assertEquals("a", list.getFirst());
        assertEquals("b", list.get(1));
        assertEquals(2, list.getSize());
    }

    @Test
    void insertInTheMiddleAndAtTheEnd() {
        HANLinkedList<Integer> list = new HANLinkedList<>();
        list.insert(0, 1);
        list.insert(1, 3);
        list.insert(1, 2);
        list.insert(3, 4);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
    }

    @Test
    void deleteRemovesItemAtPosition() {
        HANLinkedList<Integer> list = new HANLinkedList<>();
        list.insert(0, 1);
        list.insert(1, 2);
        list.insert(2, 3);
        list.delete(1);
        assertEquals(2, list.getSize());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }

    @Test
    void removeFirstAndClear() {
        HANLinkedList<Integer> list = new HANLinkedList<>();
        list.addFirst(2);
        list.addFirst(1);
        list.removeFirst();
        assertEquals(2, list.getFirst());
        list.clear();
        assertEquals(0, list.getSize());
    }

    @Test
    void badPositionsThrow() {
        HANLinkedList<Integer> list = new HANLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.delete(0));
    }
}
