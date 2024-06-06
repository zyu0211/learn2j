package d04_queues_stack;

import java.util.LinkedList;

/**
 * 用链表作为底层数据结构实现栈
 */
public class MyLinkedStack<E> {
    private LinkedList<E> list = new LinkedList<>();

    // 向栈顶加入元素，时间复杂度 O(1)
    public void push(E e) {
        list.addLast(e);
    }

    // 从栈顶弹出元素，时间复杂度 O(1)
    public E pop() {
        return list.removeLast();
    }

    // 查看栈顶元素，时间复杂度 O(1)
    public E peek() {
        return list.getLast();
    }

    // 返回栈中的元素个数，时间复杂度 O(1)
    public int size() {
        return list.size();
    }
}

