package nl.han.ica.datastructures;

import java.util.NoSuchElementException;

public class HANQueue<T> implements IHANQueue<T> {

    // front of the queue is index 0 of the list
    private final IHANLinkedList<T> list = new HANLinkedList<>();

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean isEmpty() {
        return list.getSize() == 0;
    }

    @Override
    public void enqueue(T value) {
        list.insert(list.getSize(), value);
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T first = list.getFirst();
        list.removeFirst();
        return first;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return list.getFirst();
    }

    @Override
    public int getSize() {
        return list.getSize();
    }
}
