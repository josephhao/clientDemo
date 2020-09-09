package bat.ke.qq.com;

public class HashMap<K, V> implements Map<K, V> {

    private Entry<K, V>[] table = null;
    int size = 0;

    public HashMap() {
        table = new Entry[16];
    }

    /****
     *  通过key进行hash %
     *  index下标 数组的下表 Entry
     *  是否为空  为空 直接存
     *  不为空 next用链表
     * @param k
     * @param v
     * @return
     */
    @Override
    public V put(K k, V v) {
        int index = hash(k);
        Entry<K, V> entry = table[index];
        if (null == entry) {//采用数组的存储 没有发生hash碰撞
            table[index] = new Entry<>(k, v, index, null);//王五
            size++;
        } else {   //采用链表的存储 hash碰撞
            table[index] = new Entry<>(k, v, index, entry);
        }
        return table[index].getValue();
    }

    private int hash(K k) {
        int index = k.hashCode() % 16;
        //return index>=0?index:-index;
        return Math.abs(index);
    }

    /****
     * key 进行hash index下标
     * 判断当前这个下标对应数组是否为 如果不为空 判断当前的key是否相等
     * 相等：找到了对应值
     * 不相等：继续找 next
     * @param k
     * @return
     */
    @Override
    public V get(K k) {
        if (size == 0) {
            return null;
        }
        int index = hash(k);
        Entry<K, V> entry = findValue(table[index], k);
        return entry==null?null:entry.getValue();
    }

    private Entry<K, V> findValue(Entry<K, V> entry, K k) {
        if (k.equals(entry.getKey()) || k == entry.getKey()) {
            return entry;
        } else {
            if (entry.next != null) {
                findValue(entry.next, k);
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    class Entry<K, V> implements Map.Entry<K, V> {
        K k;
        V v;
        int hash;
        Entry<K, V> next;

        public Entry(K k, V v, int hash, Entry<K, V> next) {
            this.k = k;
            this.v = v;
            this.hash = hash;
            this.next = next;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }
    }
}
