package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    private Supplier<IDictionary<K, V>> chain;
    private int[] primes = {1, 2, 3 ,5 ,7 ,11, 31, 73, 151, 353, 809, 1657, 3359, 7001, 14503, 30011, 65003, 135007,
            275003, 550007};
    private Object[] buckets; //What will be inside the hashmap (when there are collisions this is what holds multiple values
    private int size = 0;

    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) { //The supplier is what tells us what Data structure to use
        this.chain = chain;
        this.buckets = new Object[this.primes[0]]; // the buckets are going to be initially 11
    }

    private int primeNumber(){
        int ret = 0;
        int test = this.buckets.length;
        for (int i = 0; i < this.primes.length; i++){
            if (this.primes[i]-test == 0){
                return i;
            }
        }
        return ret;
    }

    public String toString(){
        if (this.size == 0){
            return "[]";
        }
        String ret = "[";
        for (K key : this.keys()){
            ret += "<" + key + ">, ";
        }

        return ret + "]";
    }

    /**
     * @param key
     * @return value corresponding to key
     */
    @Override
    public V get(K key) {
        int hash = key.hashCode();
        if (hash < 0){
            hash *= -1;
        }

        //int primeTest = this.primeNumber();
        int hashOut = hash % this.primes[0];
        IDictionary<K,V> getHelp = (IDictionary<K, V>) this.buckets[hashOut];
        if (getHelp == null){
            return null;
        }
        return getHelp.get(key);
    }

    @Override
    public V remove(K key) {
        int hash = key.hashCode();
        if (hash < 0){
            hash *= -1;
        }
        int hashOut = hash % this.primes[0];
        IDictionary<K, V> removeHelp = (IDictionary<K, V>) this.buckets[hashOut];

        if (removeHelp == null){
            return null;
        }
        if (!removeHelp.containsKey(key)){
            return null;
        }

        V valueRemove = removeHelp.get(key);
        removeHelp.remove(key);

        this.size--;
        return valueRemove;
    }

    private boolean loadFactor(){
        //int check = this.keys().size();
        double loadFactor = this.size/ (double) this.primes[0];
        return loadFactor > 1;
    }

    private void largerBuckets(){
        //this.primeNumber ++;
        int[] newPrimes = new int[this.primes.length-1];
        for (int i = 1; i < this.primes.length; i++){
            newPrimes[i-1] = this.primes[i];
        }
        this.primes = newPrimes;

        Object[] newBuckets = new Object[this.primes[0]];
        IDictionary<K, V> loop;
        IDictionary<K, V> newDict;
        for (Object i : this.buckets){
            loop = (IDictionary<K, V>) i;
            if (loop != null){
                for (K j : loop.keys()){
                    int makePos = j.hashCode();
                    if (makePos < 0){
                        makePos *= -1;
                    }

                    int hash = makePos % this.primes[0];
                    if (newBuckets[hash] == null){
                        newBuckets[hash] = this.chain.get();
                    }
                    newDict = (IDictionary<K, V>) newBuckets[hash];
                    newDict.put(j, loop.get(j));
                }
            }
        }
        this.buckets = newBuckets;
    }

    @Override
    public V put(K key, V value) {
        if (this.loadFactor()){
            this.largerBuckets();
        }

        //int hash = key.hashCode();
        int hash = key.hashCode();
        if (hash < 0){
            hash *= -1;
        }
        int hashOut = hash % this.primes[0];


        if (this.buckets[hashOut] == null){
            this.buckets[hashOut] = this.chain.get();
        }
        IDictionary<K, V> putHelp = (IDictionary<K, V>) this.buckets[hashOut];

        V retVal = putHelp.get(key);
        if (retVal == null){
            this.size ++;
        }
        putHelp.put(key, value);

        return retVal;
    }

    @Override
    public boolean containsKey(K key) {
        return this.keys().contains(key);
    }

    /**
     * @param value
     * @return true if the HashDictionary contains a key-value pair with
     * this value, and false otherwise
     */
    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    /**
     * @return number of key-value pairs in the HashDictionary
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> keys = new LinkedDeque<>();
        IDictionary<K, V> dict;
        for (Object i : this.buckets){
            dict = (IDictionary<K, V>) i;

            if (dict != null){
                for (K j : dict.keys()){
                    keys.add(j);
                }
            }
        }
        return keys;
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> values = new LinkedDeque<>();
        IDictionary<K, V> dict;
        for (Object i : this.buckets){
            dict = (IDictionary<K, V>) i;
            if (dict != null){
                for (V j : dict.values()){
                    values.add(j);
                }
            }
        }
        return values;
    }

    /**
     * @return An iterator for all entries in the HashDictionary
     */
    @Override
    public Iterator<K> iterator() {
        return this.keys().iterator();
    }
}

