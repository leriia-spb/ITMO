package queue;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(head, n): forall i=head..head+n: a'[i] = a[i]
public class ArrayQueueADT {
    private int size = 0;
    private int head = 0;
    private Object[] elements = new Object[5];

    // Pre: element != null
    // Post: n' = n + 1 && head' = head
    //       a'[n'] = element &&
    //       immutable(head, n')
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, queue.size + 1);
        queue.elements[shift(queue, queue.head, queue.size)] = element;
        queue.size++;
    }

    // Pre: true
    // Post: n' = n && head' = head &&  immutable(head, n)
    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length) {
            Object[] tail = Arrays.copyOf(queue.elements, queue.head);
            queue.elements = Arrays.copyOfRange(queue.elements, queue.head, 2 * capacity + queue.head);
            System.arraycopy(tail, 0, queue.elements, capacity - queue.head - 1, tail.length);
            queue.head = 0;
        }
    }

    // Pre: n > 0
    // Post: R = a[head] && head' = head + 1 && n' = n - 1 &&  immutable(head', n')

    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        queue.size--;
        Object value = queue.elements[queue.head];
        queue.head = shift(queue, queue.head, 1);
        return value;
    }

    // Pre: n > 0
    // Post: R = a[head] && head' = head && n' = n && immutable(head, n)

    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.head];
    }

    // Pre: true
    // Post: R = n && n' = n && head' = head && immutable(head, n)
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pre: true
    // Post: R = (n == 0) && n' = n && head' = head && immutable(head, n)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pre: true
    // Post: n = 0 && head = 0 && immutable(head, n)
    public static void clear(ArrayQueueADT queue) {
        Object[] elements = new Object[5];
        queue.head = 0;
        queue.size = 0;
    }

    // Pre: n > 0
    // Post: R = a[head + n] && n' = n - 1 && head' = head && immutable(head, n')
    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[shift(queue, queue.head, --queue.size)];
    }

    // Pre: n > 0
    // Post: R = a[head + n] && n' = n && head' = head && immutable(head, n')
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[shift(queue, queue.head, queue.size - 1)];
    }

    // Pre: element != null
    // Post: n' = n + 1 && head' = head - 1 && immutable(head, n')
    public static void push(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        ensureCapacity(queue, queue.size + 1);
        queue.head = shift(queue, queue.head, -1);
        queue.elements[queue.head] = element;
        queue.size++;
    }

    // Pre: predicate
    // Post: n' = n && head' = head && immutable(head, n') &&
    // (R == -1 && for i = head ... head+n predicate(a[i]) = false) ^
    // (R = i && predicate(a[i]) = true && for j = i + 1... head + n predicate(a[j]) = false)
    public static int lastIndexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        int rez = find(queue, predicate, shift(queue, queue.head, queue.size - 1), -1);
        if (rez == -1) {
            return -1;
        }
        return queue.size - find(queue, predicate, shift(queue, queue.head, queue.size - 1), -1) - 1;
    }

    // Pre: predicate
    // Post: n' = n && head' = head && immutable(head, n') &&
    // (R == -1 && for i = head ... head+n predicate(a[i]) = false) ^
    // (R = i && predicate(a[i]) = true && for j = head... i - 1 predicate(a[j]) = false)
    public static int indexIf(ArrayQueueADT queue, Predicate<Object> predicate) {
        return find(queue, predicate, queue.head, 1);
    }

    // Pre: predicate && 0 <= index < elements.length && delta - int
    // Post: n' = n && head' = head && immutable(head, n') &&
    // (R == -1 && for i = head ... head+n predicate(a[i]) = false) ^
    // (R = i && predicate(a[i]) = true && for j = i + 1... head + n predicate(a[j]) = false)
    private static int find(ArrayQueueADT queue, Predicate<Object> predicate, int index, int delta) {
        for (int i = 0; i < queue.size; i++) {
            if (predicate.test(queue.elements[index])) {
                return i;
            }
            index = shift(queue, index, delta);
        }
        return -1;
    }

    // Pre: num - int && delta - int
    // Post: R = (num + delta + elements.length) % elements.length
    private static int shift(ArrayQueueADT queue, int num, int delta) {
        return (num + delta + queue.elements.length) % queue.elements.length;
    }
}

