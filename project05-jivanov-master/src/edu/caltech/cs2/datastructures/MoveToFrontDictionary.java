package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private int size;
    private DictNode<K, V> head;
    private static class DictNode<K, V> {
        public K key;
        public V value;
        public DictNode<K, V> next;

        public DictNode(K key, V value, DictNode<K, V> node) {
            this.key = key;
            this.value = value;
            this.next = node;
        }
    }

    public MoveToFrontDictionary() {
        this.size = 0;
        this.head = null;
    }
    public String toString(){
        return "hello";
    }

    @Override
    public V remove(K key) {
        if (this.size() == 0 || this.head == null){
            return null;
        }
        V returnV;
        if (this.head.key.equals(key)){
            returnV = this.head.value;
            this.head = this.head.next;

        }
        else{
            returnV = remove_helper(key, this.head);
            if (returnV == null){
                return null;
            }
        }
        this.size --;
        return returnV;
    }

    private V remove_helper(K key, DictNode<K, V> node){
        if (node.next == null){
            return null;
        }
        if (node.next.key.equals(key)){
            V returnV = node.next.value;
            node.next = node.next.next;
            return returnV;
        }
        else {
            return remove_helper(key, node.next);
        }
    }

    @Override
    public V put(K key, V value) {
        V returnV;
        if (this.containsKey(key)){
            returnV = this.get(key);
            this.head.value = value;
            return returnV;
        }
        else {
            if (this.size() == 0 || this.head == null){
                this.head = new DictNode<>(key, value, null);
            }
            else {
                DictNode<K, V> lastHead = this.head;
                this.head = new DictNode<>(key, value, lastHead);
            }
            this.size ++;
            return null;
        }
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> keys = new LinkedDeque<>();
        if (this.size() == 0){
            return keys;
        }
        return keys_helper(keys, this.head);
    }

    private ICollection<K> keys_helper(ICollection<K> keys, DictNode<K, V> node){
        if (node == null){
            return keys;
        }
        keys.add(node.key);
        return keys_helper(keys, node.next);
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> vals = new LinkedDeque<>();
        if (this.size() == 0){
            return vals;
        }
        return values_helper(vals, this.head);
    }

    private ICollection<V> values_helper(ICollection<V> vals, DictNode<K, V> node){
        if (node == null){
            return vals;
        }
        vals.add(node.value);
        return values_helper(vals, node.next);
    }

    public V get(K key) {
        if (this.size == 0){
            return null;
        }
        if (this.head.key.equals(key)){
            return head.value;
        }
        DictNode<K, V> previous = this.head;
        DictNode<K, V> node = this.head.next;
        for (int i = 0; i < this.size(); i++){
            if (node != null){
                if (node.key.equals(key)){
                    previous.next = node.next;
                    node.next = this.head;
                    this.head = node;
                    return this.head.value;
                }
                previous = node;
                node = node.next;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new DictIterator();
    }

    private class DictIterator implements Iterator<K>{
        private DictNode<K, V> dictNode = MoveToFrontDictionary.this.head;


        public boolean hasNext(){
            return this.dictNode != null;
        }

        public K next(){
            if (!this.hasNext()){
                return null;
            }
            K returnK = this.dictNode.key;
            if (this.dictNode.next == null){
                this.dictNode = null;
            }
            else{
                this.dictNode = this.dictNode.next;
            }
            return returnK;
        }
    }
}
