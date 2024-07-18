package queue;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..n: a[i] != null
// Let: immutable(head, n): forall i=head..head+n: a'[i] = a[i]
public interface Queue extends Copiable {

    // Pre: function != null
    // Post: R = new Queue: immutable(0, n) &&
    // forall i=0...n: a'[i] = function(a[head + i])
    // n' = n && head' = head
    // immutable(head, n')
    Queue flatMap(Function<Object, List<Object>> function);

    // Pre: element != null
    // Post: n' = n + 1 && head' = head
    // a'[n'] = element &&
    // immutable(head, n')
    void enqueue(Queue this, Object element);

    // Pre: n > 0
    // Post: R = a[head] && head' = head + 1 && n' = n - 1 &&  immutable(head', n')

    Object dequeue(Queue this);

    // Pre: n > 0
    // Post: R = a[head] && head' = head && n' = n && immutable(head, n)

    Object element(Queue this);

    // Pre: true
    // Post: n = 0 && head = 0 && immutable(head, n)
    void clear(Queue this);

    // Pre: true
    // Post: R = n && n' = n && immutable(n)
    int size();

    // Pre: true
    // Post: R = (n = 0) && n' = n && immutable(n)
    boolean isEmpty();

    // Pre: true
    // Post R.n = n && forall i=1..n: R.a[i] = a[i] && n'== n && immutable(n)
    Queue makeCopy();

    Object reduce(Object init, BinaryOperator<Object> op);
}