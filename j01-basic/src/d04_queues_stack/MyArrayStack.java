package d04_queues_stack;

import java.util.ArrayList;

/**
 * 用数组作为底层数据结构实现栈
 */
public class MyArrayStack<E> {
    private ArrayList<E> list = new ArrayList<>();

    // 向栈顶加入元素，时间复杂度 O(1)
    public void push(E e) {
        list.add(e);
    }

    // 从栈顶弹出元素，时间复杂度 O(1)
    public E pop() {
        return list.remove(list.size() - 1);
    }

    // 查看栈顶元素，时间复杂度 O(1)
    public E peek() {
        return list.get(list.size() - 1);
    }

    // 返回栈中的元素个数，时间复杂度 O(1)
    public int size() {
        return list.size();
    }
}

