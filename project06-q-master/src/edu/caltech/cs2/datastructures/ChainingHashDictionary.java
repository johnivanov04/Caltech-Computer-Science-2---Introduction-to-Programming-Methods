package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;
import java.util.function.Supplier;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V>
{
    private Supplier<IDictionary<K, V>> chain;
    private int size;
    private int capacity;
    private IDictionary<K, V>[] data;

    public static final int[] PRIMES = {11, 23, 41, 79, 83, 163, 167, 317, 691, 1201, 2969, 6559, 12983, 26821, 55381, 110233, 212869, 420331};

    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain)
    {
        this.chain = chain;
        this.size = 0;
        this.capacity = 0;

        data = new IDictionary[PRIMES[capacity]];

        for (int i = 0; i < data.length; i++)
        {
            data[i] = this.chain.get();
        }
    }

    public ChainingHashDictionary()
    {
        Supplier<IDictionary<K, V>> sup = () -> new MoveToFrontDictionary<>();

        this.chain = sup;
        this.size = 0;
        this.capacity = 0;

        data = new IDictionary[PRIMES[capacity]];

        for (int i = 0; i < data.length; i++)
        {
            data[i] = this.chain.get();
        }
    }

    /**
     * @param key
     * @return value corresponding to key
     */
    @Override
    public V get(K key)
    {
        IDictionary<K, V> bucket = this.data[Math.abs(key.hashCode()) % this.data.length];

        return bucket == null ? null : bucket.get(key);
    }

    @Override
    public V remove(K key)
    {
        IDictionary<K, V> bucket = this.data[Math.abs(key.hashCode()) % this.data.length];

        V removed = bucket.remove(key);

        if (removed != null) size--;

        return removed;
    }

    @Override
    public V put(K key, V value)
    {
        if ((double) this.size / this.data.length > 1.0) this.resize();

        int index = Math.abs(key.hashCode()) % this.data.length;

        V old = this.data[index].put(key, value);

        if (old == null) size++;

        return old;
    }

    private void resize()
    {
        IDictionary<K, V>[] temp;

        if (++capacity < PRIMES.length)
        {
            temp = new IDictionary[PRIMES[capacity]];

            for (int i = 0; i < temp.length; i++)
            {
                temp[i] = this.chain.get();
            }

            for (K key : this.keys())
            {
                int index = Math.abs(key.hashCode()) % this.data.length;
                int newIndex = Math.abs(key.hashCode()) % temp.length;
                temp[newIndex].put(key, this.data[index].get(key));
            }
        }
        else
        {
            temp = new IDictionary[this.data.length * 2];

            for (int i = 0; i < temp.length; i++)
            {
                temp[i] = this.chain.get();
            }

            for (K key : this.keys())
            {
                int index = Math.abs(key.hashCode()) % this.data.length;
                int newIndex = Math.abs(key.hashCode()) % temp.length;
                temp[newIndex].put(key, this.data[index].get(key));
            }
        }

        this.data = temp;
    }

    @Override
    public boolean containsKey(K key)
    {
        IDictionary<K, V> bucket = this.data[Math.abs(key.hashCode()) % this.data.length];

        return bucket.containsKey(key);
    }

    /**
     * @param value
     * @return true if the HashDictionary contains a key-value pair with
     * this value, and false otherwise
     */
    @Override
    public boolean containsValue(V value)
    {
        for (int i = 0; i < data.length; i++)
        {
            IDictionary<K, V> bucket = data[i];

            if (bucket.containsValue(value)) return true;
        }
        return false;
    }

    /**
     * @return number of key-value pairs in the HashDictionary
     */
    @Override
    public int size()
    {
        return this.size;
    }

    @Override
    public ICollection<K> keys()
    {
        ICollection<K> keys = new LinkedDeque<>();

        for (int i = 0; i < this.data.length; i++)
        {
            IDictionary<K, V> dictionary = this.data[i];

            for (K key : dictionary.keys()) keys.add(key);
        }

        return keys;
    }

    @Override
    public ICollection<V> values()
    {
        ICollection<V> values = new LinkedDeque<>();

        for (int i = 0; i < this.data.length; i++)
        {
            IDictionary<K, V> dictionary = this.data[i];

            for (V value : dictionary.values()) values.add(value);
        }

        return values;
    }

    /**
     * @return An iterator for all entries in the HashDictionary
     */
    @Override
    public Iterator<K> iterator()
    {
        return new ChainingHashDictionaryIterator();
    }

    @Override
    public String toString()
    {
        String result = "[";
        for (int i = 0; i < this.data.length; i++)
        {
            IDictionary<K, V> dictionary = this.data[i];

            if (dictionary != null)
            {
                for (K key : dictionary.keys())
                {
                    result += "(" + key + " " + dictionary.get(key) + ")";
                }
            }
        }

        return result + "]";
    }

    private class ChainingHashDictionaryIterator implements Iterator<K>
    {

        ICollection<K> keys = keys();
        Iterator<K> keyIterator = keys.iterator();

        @Override
        public boolean hasNext()
        {
            return keyIterator.hasNext();
        }

        @Override
        public K next()
        {
            return keyIterator.next();
        }
    }
}