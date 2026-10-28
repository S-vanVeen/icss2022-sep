package nl.han.ica.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HANStack<T> implements IHANStack<T> {

    // top of the stack is the last item in the list
    private final List<T> elements = new ArrayList<>();

    @Override
    public void push(T value) {
        elements.add(value);
    }

    @Override
    public T pop() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return elements.remove(elements.size() - 1);
    }

    @Override
    public T peek() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return elements.get(elements.size() - 1);
    }
}
