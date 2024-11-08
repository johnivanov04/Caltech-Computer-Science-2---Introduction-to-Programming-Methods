package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public class TrieMap<A, K extends Iterable<A>, V> implements ITrieMap<A, K, V> {
    private TrieNode<A, V> root;
    private Function<IDeque<A>, K> collector;
    private int size;

    public TrieMap(Function<IDeque<A>, K> collector) {
        this.root = null;
        this.collector = collector;
        this.size = 0;
    }
    

    @Override
    public boolean isPrefix(K key) {
        if (this.root == null){
            return false;
        }
        TrieNode<A, V> curr = this.root;
        for (A letter : key){
            if (curr.pointers.containsKey(letter)){
                curr = curr.pointers.get(letter);
            }
            else{
                return false;
            }
        }
        return true;
    }

    @Override
    public ICollection<V> getCompletions(K prefix) {
        IDeque<V> comp = new LinkedDeque<>();
        TrieNode<A, V> curr = this.root;
        if (this.isPrefix(prefix)){
            for (A letter : prefix){
                curr = curr.pointers.get(letter);
            }
            TrieMap<A, K, V> map = new TrieMap<>(this.collector);
            map.root = curr;
            for(V v : map.values()){
                comp.addBack(v);
            }
        }
        return comp;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public V get(K key) {
        if (this.size == 0){
            return null;
        }
        TrieNode<A, V> curr = this.root;
        for (A letter : key){
            if (curr.pointers.containsKey(letter)){
                curr = curr.pointers.get(letter);
            }
            else{
                return null;
            }
        }
        return curr.value;
    }

    @Override
    public V remove(K key) {
        if (!this.keys().contains(key)){
            return null;
        }
        V value = this.get(key);
        remove_helper(key.iterator(), this.root);
        this.size --;
        if (this.size() == 0){
            this.root = null;
        }
        return value;
    }
    private boolean remove_helper(Iterator<A> iterator, TrieNode<A, V> node) {
        if (iterator.hasNext()) {
            A letter = iterator.next();
            if (this.remove_helper(iterator, node.pointers.get(letter))) {
                node.pointers.remove(letter);
                if (node.value == null && node.pointers.size() == 0) {
                    return true;
                }
            }
            return false;
        }
        else {
            if (node.pointers.size() == 0) {
                return true;
            }
            else {
                node.value = null;
                return false;
            }
        }
    }

    @Override
    public V put(K key, V value) {
        TrieNode<A, V> curr;
        if (this.root == null){
            this.root = new TrieNode<>();
        }
        curr = this.root;
        for (A letter : key){
            if (!curr.pointers.containsKey(letter)){
                TrieNode<A, V> nde = new TrieNode<>();
                curr.pointers.put(letter, nde);
            }
            curr = curr.pointers.get(letter);
        }
        if (curr.value == null){
            this.size ++;
        }
        V value2 = curr.value;
        curr.value = value;
        return value2;
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (this.values().contains(value)){
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> keys = new LinkedDeque<>();
        IDeque<A> acc = new LinkedDeque<>();
        TrieNode<A, V> curr = this.root;
        keys_helper(keys, curr, acc);
        return keys;
    }
    private void keys_helper(ICollection<K> keys, TrieNode<A,V> node, IDeque<A> acc) {
        if (node == null) {
            return;
        }
        if (node.value != null) {
            K key = this.collector.apply(acc);
            keys.add(key);
        }
        for (A letter : node.pointers.keySet()) {
            acc.addBack(letter);
            TrieNode<A, V> next_node = node.pointers.get(letter);
            keys_helper(keys, next_node, acc);
            acc.removeBack();
        }
    }


    @Override
    public ICollection<V> values() {
        ICollection<V> vals = new LinkedDeque<>();
        values(this.root, vals);
        return vals;
    }

    private void values(TrieNode<A, V> node, ICollection<V> values) {
        if (node == null) {
            return;
        }
        if (node.value != null) {
            values.add(node.value);
        }
        for (A letter : node.pointers.keySet()) {
            TrieNode<A, V> next_node = node.pointers.get(letter);
            values(next_node, values);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }
    
    @Override
    public String toString() {
        return this.root.toString();
    }
    
    private static class TrieNode<A, V> {
        public final Map<A, TrieNode<A, V>> pointers;
        public V value;

        public TrieNode() {
            this(null);
        }

        public TrieNode(V value) {
            this.pointers = new HashMap<>();
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (this.value != null) {
                b.append("[" + this.value + "]-> {\n");
                this.toString(b, 1);
                b.append("}");
            }
            else {
                this.toString(b, 0);
            }
            return b.toString();
        }

        private String spaces(int i) {
            StringBuilder sp = new StringBuilder();
            for (int x = 0; x < i; x++) {
                sp.append(" ");
            }
            return sp.toString();
        }

        protected boolean toString(StringBuilder s, int indent) {
            boolean isSmall = this.pointers.entrySet().size() == 0;

            for (Map.Entry<A, TrieNode<A, V>> entry : this.pointers.entrySet()) {
                A idx = entry.getKey();
                TrieNode<A, V> node = entry.getValue();

                if (node == null) {
                    continue;
                }

                V value = node.value;
                s.append(spaces(indent) + idx + (value != null ? "[" + value + "]" : ""));
                s.append("-> {\n");
                boolean bc = node.toString(s, indent + 2);
                if (!bc) {
                    s.append(spaces(indent) + "},\n");
                }
                else if (s.charAt(s.length() - 5) == '-') {
                    s.delete(s.length() - 5, s.length());
                    s.append(",\n");
                }
            }
            if (!isSmall) {
                s.deleteCharAt(s.length() - 2);
            }
            return isSmall;
        }
    }
}
