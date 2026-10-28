package nl.han.ica.datastructures;

import java.util.NoSuchElementException;

public class HANLinkedList<T> implements IHANLinkedList<T> {

    private static class Node<T> {
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    // header node doesn  hold a value, makes insert/delete at the front easier
    private final Node<T> header = new Node<>(null);
    private int size = 0;

    @Override
    public void addFirst(T value) {
        insert(0, value);
    }

    @Override
    public void clear() {
        header.next = null;
        size = 0;
    }

    @Override
    public void insert(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
        Node<T> previous = nodeBefore(index);
        Node<T> newNode = new Node<>(value);
        newNode.next = previous.next;
        previous.next = newNode;
        size++;
    }

    @Override
    public void delete(int pos) {
        checkPosition(pos);
        Node<T> previous = nodeBefore(pos);
        previous.next = previous.next.next;
        size--;
    }

    @Override
    public T get(int pos) {
        checkPosition(pos);
        return nodeBefore(pos).next.value;
    }

    @Override
    public void removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        }
        delete(0);
    }

    @Override
    public T getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("List is empty");
        }
        return get(0);
    }

    @Override
    public int getSize() {
        return size;
    }

    // walks from the header to the node just before position pos
    private Node<T> nodeBefore(int pos) {
        Node<T> current = header;
        for (int i = 0; i < pos; i++) {
            current = current.next;
        }
        return current;
    }

    private void checkPosition(int pos) {
        if (pos < 0 || pos >= size) {
            throw new IndexOutOfBoundsException("Position: " + pos + ", size: " + size);
        }
    }
}
