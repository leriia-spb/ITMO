package queue;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(head, n): forall i=head..head+n: a'[i] = a[i]
public class ArrayQueueModule {
    private static int size = 0;
    private static int head = 0;
    private static Object[] elements = new Object[5];

    // Pre: element != null
    // Post: n' = n + 1 && head' = head
    //       a'[n'] = element &&
    //       immutable(head, n')
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(size + 1);
        elements[shift(head, size)] = element;
        size++;
    }

    // Pre: true
    // Post: n' = n && head' = head &&  immutable(head, n)
    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
            Object[] tail = Arrays.copyOf(elements, head);
            elements = Arrays.copyOfRange(elements, head, 2 * capacity + head);
            System.arraycopy(tail, 0, elements, capacity - head - 1, tail.length);
            head = 0;
        }
    }

    // Pre: n > 0
    // Post: R = a[head] && head' = head + 1 && n' = n - 1 &&  immutable(head', n')

    public static Object dequeue() {
        assert size > 0;
        size--;
        Object value = elements[head];
        head = shift(head, 1);
        return value;
    }

    // Pre: n > 0
    // Post: R = a[head] && head' = head && n' = n && immutable(head, n)

    public static Object element() {
        assert size > 0;
        return elements[head];
    }

    // Pre: true
    // Post: R = n && n' = n && head' = head && immutable(head, n)
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: R = (n == 0) && n' = n && head' = head && immutable(head, n)
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: n = 0 && head = 0 && immutable(head, n)
    public static void clear() {
        Object[] elements = new Object[5];
        head = 0;
        size = 0;
    }

    // Pre: n > 0
    // Post: R = a[head + n] && n' = n - 1 && head' = head && immutable(head, n')
    public static Object remove() {
        assert size > 0;
        return elements[shift(head,  --size)];
    }

    // Pre: n > 0
    // Post: R = a[head + n] && n' = n && head' = head && immutable(head, n')
    public static Object peek() {
        assert size > 0;
        return elements[shift(head, size - 1)];
    }

    // Pre: element != null
    // Post: n' = n + 1 && head' = head - 1 && immutable(head, n')
    public static void push(Object element) {
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
    public static int lastIndexIf(Predicate<Object> predicate) {
        int rez = find(predicate, shift(head, size - 1), -1);
        if (rez == -1){
            return -1;
        }
        return size - find(predicate, shift(head, size - 1), -1) - 1;
    }

    // Pre: predicate
    // Post: n' = n && head' = head && immutable(head, n') &&
    // (R == -1 && for i = head ... head+n predicate(a[i]) = false) ^
    // (R = i && predicate(a[i]) = true && for j = head... i - 1 predicate(a[j]) = false)
    public static int indexIf(Predicate<Object> predicate) {
        return find(predicate, head, 1);
    }
    // Pre: predicate && 0 <= index < elements.length && delta - int
    // Post: n' = n && head' = head && immutable(head, n') &&
    // (R == -1 && for i = head ... head+n predicate(a[i]) = false) ^
    // (R = i && predicate(a[i]) = true && for j = i + 1... head + n predicate(a[j]) = false)
    private static int find(Predicate<Object> predicate, int index, int delta){
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
    private static int shift(int num, int delta){
        return (num + delta + elements.length) % elements.length;
    }
}

