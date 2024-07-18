package queue;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue implements Copiable {
    private int head = 0;
    private Object[] elements = new Object[5];

    public void pushImpl(Object element) {
        ensureCapacity(size + 1);
        elements[shift(head, size)] = element;
    }

    public Object peekImpl() {
        return elements[head];
    }

    private void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] tail = Arrays.copyOf(elements, head);
            elements = Arrays.copyOfRange(elements, head, 2 * capacity + head);
            System.arraycopy(tail, 0, elements, capacity - head - 1, tail.length);
            head = 0;
        }
    }

    public void popImpl() {
        head = shift(head, 1);
    }

    public AbstractQueue create() {
        return new ArrayQueue();
    }

    public void deleteAll() {
        elements = new Object[5];
        head = 0;
        size = 0;
    }

    public ArrayQueue makeCopy() {
        final ArrayQueue copy = new ArrayQueue();
        copy.size = size;
        copy.head = head;
        copy.elements = elements;
        return copy;
    }

    // Pre: n > 0
    // Post: R = a[head + n] && n' = n - 1 && head' = head && immutable(head, n')
    public Object remove() {
        assert size > 0;
        return elements[shift(head, --size)];
    }

    // Pre: n > 0
    // Post: R = a[head + n] && n' = n && head' = head && immutable(head, n')
    public Object peek() {
        assert size > 0;
        return elements[shift(head, size - 1)];
    }

    // Pre: element != null
    // Post: n' = n + 1 && head' = head - 1 && immutable(head, n')
    public void push(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        head = shift(head, -1);
        elements[head] = element;
        size++;
    }

    // Pre: predicate
    // Post: n' = n && head' = head && immutable(head, n') &&
    // (R == -1 && for i = head ... head+n predicate(a[i]) = false) ^
    // (R = i && predicate(a[i]) = true && for j = i + 1... head + n predicate(a[j]) = false)
    public int lastIndexIf(Predicate<Object> predicate) {
        int rez = find(predicate, shift(head, size - 1), -1);
        if (rez == -1) {
            return -1;
        }
        return size - find(predicate, shift(head, size - 1), -1) - 1;
    }

    // Pre: predicate
    // Post: n' = n && head' = head && immutable(head, n') &&
    // (R == -1 && for i = head ... head+n predicate(a[i]) = false) ^
    // (R = i && predicate(a[i]) = true && for j = head... i - 1 predicate(a[j]) = false)
    public int indexIf(Predicate<Object> predicate) {
        return find(predicate, head, 1);
    }

    // Pre: predicate && 0 <= index < elements.length && delta - int
    // Post: n' = n && head' = head && immutable(head, n') &&
    // (R == -1 && for i = head ... head+n predicate(a[i]) = false) ^
    // (R = i && predicate(a[i]) = true && for j = i + 1... head + n predicate(a[j]) = false)
    private int find(Predicate<Object> predicate, int index, int delta) {
        for (int i = 0; i < size; i++) {
            if (predicate.test(elements[index])) {
                return i;
            }
            index = shift(index, delta);
        }
        return -1;
    }

    // Pre: num - int && delta - int
    // Post: R = (num + delta + elements.length) % elements.length
    private int shift(int num, int delta) {
        return (num + delta + elements.length) % elements.length;
    }
}

