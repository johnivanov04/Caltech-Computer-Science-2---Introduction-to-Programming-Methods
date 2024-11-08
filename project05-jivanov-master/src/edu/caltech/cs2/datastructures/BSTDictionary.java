package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;

import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class BSTDictionary<K extends Comparable<? super K>, V>
        implements IDictionary<K, V> {

    private BSTNode<K, V> root;
    private int size;

    /**
     * Class representing an individual node in the Binary Search Tree
     */
    private static class BSTNode<K, V> {
        public final K key;
        public V value;

        public BSTNode<K, V> left;
        public BSTNode<K, V> right;

        /**
         * Constructor initializes this node's key, value, and children
         */
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }

        public BSTNode(BSTNode<K, V> o) {
            this.key = o.key;
            this.value = o.value;
            this.left = o.left;
            this.right = o.right;
        }

        public boolean isLeaf() {
            return this.left == null && this.right == null;
        }

        public boolean hasBothChildren() {
            return this.left != null && this.right != null;
        }
    }

    /**
     * Initializes an empty Binary Search Tree
     */
    public BSTDictionary() {
        this.root = null;
        this.size = 0;
    }


    @Override
    public V get(K key) {
        if (this.root == null) {
            return null;
        }
        BSTNode<K, V> node = this.root;
        while (!node.key.equals(key)){
            int checker = node.key.compareTo(key);
            if (checker < 0){
                node = node.right;
            }
            if (checker > 0){
                node = node.left;
            }
            if (node == null){
                return null;
            }
        }
        return node.value;
    }

    @Override
    public V remove(K key) {
        if (!this.containsKey(key) || this.get(key) == null){
            return null;
        }
        this.size --;
        V returnV = this.get(key);
        this.root = this.remove_helper(this.root, key);
        return returnV;
    }

    private BSTNode<K, V> remove_helper(BSTNode<K, V> node, K key){
        if (node == null){
            return null;
        }
        if (node.key.equals(key)){
            if (node.hasBothChildren()){
                BSTNode<K, V> node1;
                BSTNode<K, V> node2 = node.left;
                while (node2.right != null){
                    node2 = node2.right;
                }
                remove_helper(node, node2.key);
                node1 = new BSTNode<>(node2.key, node2.value);
                node1.right = node.right;
                node1.left = node.left;
                return node1;
            }
            else {
                if (node.isLeaf()){
                    return null;
                }
                else if (node.left == null){
                    return node.right;
                }
                else if (node.right == null){
                    return node.left;
                }
            }
        }
        if (node.key.compareTo(key)> 0){
            node.left = remove_helper(node.left, key);
        }
        else {
            node.right = remove_helper(node.right, key);
        }
        return node;

    }

    @Override
    public V put(K key, V value) {
        if (this.root == null){
            this.size++;
            this.root = new BSTNode<>(key, value);
            return null;
        }
        BSTNode<K, V> node = this.root;
        V returnV = this.get(key);
        while (!node.key.equals(key)){
            int checker = node.key.compareTo(key);
            if (checker < 0){
                if (node.right == null){
                    node.right = new BSTNode<>(key, value);
                }
                node = node.right;
            }
            if (checker > 0){
                if (node.left == null) {
                    node.left = new BSTNode<>(key, value);
                }
                node = node.left;
            }

        }
        if (returnV == null){
            this.size ++;
        }
        node.value = value;
        return returnV;

    }

    @Override
    public boolean containsKey(K key) {
        return this.keys().contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    /**
     * @return number of key/value pairs in the BST
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> keys = new LinkedDeque<>();
        return this.keys_helper(keys, this.root);
    }
    private ICollection<K> keys_helper(ICollection<K> keys, BSTNode<K, V> node){
        if (node == null){
            return keys;
        }
        keys.add(node.key);
        keys = keys_helper(keys, node.right);
        keys = keys_helper(keys, node.left);
        return keys;
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> vals = new LinkedDeque<>();
        return this.values_helper(vals, this.root);
    }

    private ICollection<V> values_helper(ICollection<V> vals, BSTNode<K, V> node){
        if (node == null){
            return vals;
        }
        vals.add(node.value);
        vals = values_helper(vals, node.right);
        vals = values_helper(vals, node.left);
        return vals;
    }

    /**
     * Implementation of an iterator over the BST
     */

    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }

    @Override
    public String toString() {
        if (this.root == null) {
            return "{}";
        }

        StringBuilder contents = new StringBuilder();

        IQueue<BSTNode<K, V>> nodes = new ArrayDeque<>();
        BSTNode<K, V> current = this.root;
        while (current != null) {
            contents.append(current.key + ": " + current.value + ", ");

            if (current.left != null) {
                nodes.enqueue(current.left);
            }
            if (current.right != null) {
                nodes.enqueue(current.right);
            }

            current = nodes.dequeue();
        }

        return "{" + contents.toString().substring(0, contents.length() - 2) + "}";
    }
}
