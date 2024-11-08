package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IGraph;
import edu.caltech.cs2.interfaces.ISet;

public class Graph<V, E> implements IGraph<V, E> {
    private IDictionary<V, IDictionary<V, E>> graphD;

    public Graph(){
        graphD = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    @Override
    public boolean addVertex(V vertex) {
        if (this.graphD.containsKey(vertex)){
            return false;
        }
        this.graphD.put(vertex, new ChainingHashDictionary<>(MoveToFrontDictionary::new));
        return true;
    }

    @Override
    public boolean addEdge(V src, V dest, E e) {
        if (!this.graphD.containsKey(src) || !this.graphD.containsKey(dest)){
            throw new IllegalArgumentException("Invalid Vertices");
        }
        IDictionary<V, E> dataE = this.graphD.get(src);
        boolean bool = !dataE.containsKey(dest);
        dataE.put(dest, e);
        return bool;
    }

    @Override
    public boolean addUndirectedEdge(V n1, V n2, E e) {
        boolean bool1 = this.addEdge(n1, n2, e);
        boolean bool2 = this.addEdge(n2, n1, e);
        return bool1 && bool2;
    }

    @Override
    public boolean removeEdge(V src, V dest) {

        IDictionary<V,E> dataE = this.graphD.get(src);
        if (dataE.containsKey(dest)) {
            dataE.remove(dest);
            return true;
        }
        return false;
    }

    @Override
    public ISet<V> vertices() {
        return new ChainingHashSet<>(this.graphD.keys());
    }

    @Override
    public E adjacent(V i, V j) {
        IDictionary<V, E> dataE = this.graphD.get(i);

        return dataE.get(j);
    }

    @Override
    public ISet<V> neighbors(V vertex) {

        return new ChainingHashSet<>(this.graphD.get(vertex).keys());
    }
}