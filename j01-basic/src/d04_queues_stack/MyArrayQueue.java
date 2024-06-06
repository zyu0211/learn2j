package d04_queues_stack;

public class MyArrayQueue<E> {
    private final CycleArray<E> arr;

    public MyArrayQueue() {
        arr = new CycleArray<>();
    }

    public void push(E t) {
        arr.addLast(t);
    }

    public E pop() {
        E e = arr.getFirst();
        arr.removeFirst();
        return e;
    }

    public E peek() {
        return arr.getFirst();
    }

    public int size() {
        return arr.size();
    }
}
