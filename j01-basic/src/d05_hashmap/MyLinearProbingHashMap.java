package d05_hashmap;

import java.util.LinkedList;
import java.util.List;

public class MyLinearProbingHashMap {
    public static void main(String[] args) {
        // 方法一
        MyLinearProbingHashMap1<Integer, Integer> map1 = new MyLinearProbingHashMap1<>();
        map1.put(1, 1);
        map1.put(2, 2);
        map1.put(10, 10);
        map1.put(20, 20);
        map1.put(30, 30);
        map1.put(3, 3);
        System.out.println(map1.get(1)); // 1
        System.out.println(map1.get(2)); // 2
        System.out.println(map1.get(20)); // 20

        map1.put(1, 100);
        System.out.println(map1.get(1)); // 100

        map1.remove(20);
        System.out.println(map1.get(20)); // null
        System.out.println(map1.get(30)); // 30

        System.out.println("=================");

        //方法二
        MyLinearProbingHashMap2<Integer, Integer> map2 = new MyLinearProbingHashMap2<>();
        map2.put(1, 1);
        map2.put(2, 2);
        map2.put(10, 10);
        map2.put(20, 20);
        map2.put(30, 30);
        map2.put(3, 3);
        System.out.println(map2.get(1)); // 1
        System.out.println(map2.get(2)); // 2
        System.out.println(map2.get(20)); // 20

        map2.put(1, 100);
        System.out.println(map2.get(1)); // 100

        map2.remove(20);
        System.out.println(map2.get(20)); // null
        System.out.println(map2.get(30)); // 30
    }
}


/**
 * 线性探查方法一：rehash 方式实现的哈希表
 */
class MyLinearProbingHashMap1<K, V> {

    private static class KVNode<K, V> {
        K key;
        V val;

        KVNode(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    // 真正存储键值对的数组
    private KVNode<K, V>[] table;
    // HashMap 中的键值对个数
    private int size;
    // 默认的初始化容量
    private static final int INIT_CAP = 4;

    public MyLinearProbingHashMap1() {
        this(INIT_CAP);
    }

    public MyLinearProbingHashMap1(int initCapacity) {
        size = 0;
        table = (KVNode<K, V>[]) new KVNode[initCapacity];
    }

    /***** 增/改 *****/

    public void put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        // 我们把负载因子默认设为 0.75，超过则扩容
        if (size >= table.length * 0.75) {
            resize(table.length * 2);
        }

        int index = getKeyIndex(key);
        // key 已存在，修改对应的 val
        if (table[index] != null) {
            table[index].val = val;
            return;
        }

        // key 不存在，在空位插入
        table[index] = new KVNode<>(key, val);
        size++;
    }

    /***** 删 *****/

    // 删除 key 和对应的 val
    public void remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        // 缩容，当负载因子小于 0.125 时，缩容
        if (size <= table.length / 8) {
            resize(table.length / 4);
        }

        int index = getKeyIndex(key);

        if (table[index] == null) {
            // key 不存在，不需要 remove
            return;
        }

        // 开始 remove
        table[index] = null;
        size--;
        // 保持元素连续性，进行 rehash
        index = (index + 1) % table.length;
        for (; table[index] != null; index = (index + 1) % table.length) {
            KVNode<K, V> entry = table[index];
            table[index] = null;
            // 这里减一，因为 put 里面又会加一
            size--;
            put(entry.key, entry.val);

        }
    }

    /***** 查 *****/

    // 返回 key 对应的 val，如果 key 不存在，则返回 null
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        int index = getKeyIndex(key);
        if (table[index] == null) {
            return null;
        }
        return table[index].val;
    }

    // 返回所有 key（顺序不固定）
    public List<K> keys() {
        LinkedList<K> keys = new LinkedList<>();
        for (KVNode<K, V> entry : table) {
            if (entry != null) {
                keys.addLast(entry.key);
            }
        }
        return keys;
    }

    /***** 其他工具函数 *****/

    public int size() {
        return size;
    }

    // 哈希函数，将键映射到 table 的索引
    // [0, table.length - 1]
    private int hash(K key) {
        // int: 0000 0000 0000 ... 0000
        //    : 0111 1111 1111 ... 1111
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    // 对 key 进行线性探查，返回一个索引
    // 如果 key 不存在，返回的就是下一个为 null 的索引，可用于插入
    private int getKeyIndex(K key) {
        int index;
        for (index = hash(key); table[index] != null; index = (index + 1) % table.length) {
            if (table[index].key.equals(key))
                return index;
        }
        return index;
    }

    private void resize(int newCap) {
        MyLinearProbingHashMap1<K, V> newMap = new MyLinearProbingHashMap1<>(newCap);
        for (KVNode<K, V> entry : table) {
            if (entry != null) {
                newMap.put(entry.key, entry.val);
            }
        }
        this.table = newMap.table;
    }
}

/**
 * 线性探查方法二：特殊值标记版本
 */
class MyLinearProbingHashMap2<K, V> {

    private static class KVNode<K, V> {
        K key;
        V val;

        KVNode(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    // 被删除的 KVNode 的占位符
    private final KVNode<K, V> DUMMY = new KVNode<>(null, null);

    // 真正存储键值对的 table 数组
    private KVNode<K, V>[] table;
    // HashMap 中的键值对个数
    private int size;
    // 默认的初始化容量
    private static final int INIT_CAP = 4;

    public MyLinearProbingHashMap2() {
        this(INIT_CAP);
    }

    public MyLinearProbingHashMap2(int cap) {
        size = 0;
        table = (KVNode<K, V>[]) new KVNode[cap];
    }

    /***** 增/改 *****/

    // 添加 key -> val 键值对
    // 如果键 key 已存在，则将值修改为 val
    public void put(K key, V val) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        // 负载因子默认设为 0.75，超过则扩容
        if (size >= table.length * 0.75) {
            resize(table.length * 2);
        }

        int index = getKeyIndex(key);
        if (index != -1) {
            // key 已存在，修改对应的 val
            table[index].val = val;
            return;
        }

        // key 不存在
        KVNode<K, V> x = new KVNode<>(key, val);
        // 在 table 中找一个空位或者占位符，插入
        index = hash(key);
        while (table[index] != null && table[index] != DUMMY) {
            index = (index + 1) % table.length;
        }
        table[index] = x;
        size++;
    }

    /***** 删 *****/

    // 删除 key 和对应的 val，并返回 val
    // 若 key 不存在，则返回 null
    public void remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        // 缩容
        if (size < table.length / 8) {
            resize(table.length / 2);
        }

        int index = getKeyIndex(key);
        if (index == -1) {
            // key 不存在，不需要 remove
            return;
        }

        // 开始 remove
        // 直接用占位符表示删除
        table[index] = DUMMY;
        size--;
    }

    /***** 查 *****/

    // 返回 key 对应的 val
    // 如果 key 不存在，则返回 null
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        int index = getKeyIndex(key);
        if (index == -1) {
            return null;
        }

        return table[index].val;
    }

    public List<K> keys() {
        LinkedList<K> keys = new LinkedList<>();
        for (KVNode<K, V> entry : table) {
            if (entry != null) {
                keys.addLast(entry.key);
            }
        }
        return keys;
    }

    public int size() {
        return size;
    }

    // 对 key 进行线性探查，返回一个索引
    // 根据 keys[i] 是否为 null 判断是否找到对应的 key
    private int getKeyIndex(K key) {
        int step = 0;
        for (int i = hash(key); table[i] != null; i = (i + 1) % table.length) {
            KVNode<K, V> entry = table[i];
            // 遇到占位符直接跳过
            if (entry == DUMMY) {
                continue;
            }
            if (entry.key.equals(key)) {
                return i;
            }
            step++;
            // 防止死循环
            if (step == table.length) {
                // 这里可以触发一次 resize，把标记为删除的占位符清理掉
                resize(table.length);
                return -1;
            }
        }
        return -1;
    }

    // 哈希函数，将键映射到 table 的索引
    // [0, table.length - 1]
    private int hash(K key) {
        // int: 0000 0000 0000 ... 0000
        //    : 0111 1111 1111 ... 1111
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    private void resize(int cap) {
        MyLinearProbingHashMap2<K, V> newMap = new MyLinearProbingHashMap2<>(cap);
        for (KVNode<K, V> entry : table) {
            if (entry != null && entry != DUMMY) {
                newMap.put(entry.key, entry.val);
            }
        }
        this.table = newMap.table;
    }
}
