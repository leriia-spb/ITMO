package queue;

import java.util.List;
import java.util.function.Function;
import java.util.function.BinaryOperator;

public abstract class AbstractQueue implements Queue {
    protected int size;
    public Object reduce(Object init, BinaryOperator<Object> op){
        Object res = init;
        for(int i = 0; i < size; i++){
            Object current = dequeue();
            res = op.apply(res, current);
            enqueue(current);
        }
        return res;
    }
    public Queue flatMap(Function<Object, List<Object>> function){
        Queue res = create();
        for(int i = 0; i < size; i++){
            Object current = dequeue();
            List<Object> func = function.apply(current);
            for(Object obj: func){
                res.enqueue(obj);
            }
            enqueue(current);
        }
        return res;
    }
    public void enqueue(Object element){
        assert element != null;

        pushImpl(element);
        size++;
    }
    public Object element() {
        assert size > 0;
        return peekImpl();
    }
    public Object dequeue() {
        assert size > 0;

        Object result = element();
        size--;
        popImpl();
        return result;
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        deleteAll();
    }
    protected abstract void deleteAll();

    protected abstract void pushImpl(Object element);


    protected abstract Object peekImpl();

    protected abstract void popImpl();
    protected abstract AbstractQueue create();
}
