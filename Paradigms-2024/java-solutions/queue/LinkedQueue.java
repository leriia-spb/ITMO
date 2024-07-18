package queue;

public class LinkedQueue extends AbstractQueue implements Copiable {
    private Node head;
    private Node tail;


    protected void pushImpl(Object element) {
        if (size == 0) {
            head = new Node(element, null);
            tail = head;
        } else {
            Node newTail = new Node(element, head);
            tail.next = newTail;
            tail = newTail;
        }
    }

    protected void popImpl() {
        head = head.next;
    }

    protected Object peekImpl() {
        return head.value;
    }

    public LinkedQueue makeCopy() {
        final LinkedQueue copy = new LinkedQueue();
        copy.size = size;
        copy.head = head;
        return copy;
    }

    public void deleteAll() {
        head = new Node(0, null);
        size = 0;
    }

    public AbstractQueue create() {
        return new LinkedQueue();
    }

    private static class Node {
        private final Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }
}