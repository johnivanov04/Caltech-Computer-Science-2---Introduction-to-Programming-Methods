package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
  private Node<E> head;
  private Node<E> foot;
  private int size;

  private static class Node<E>{
    public final E data;
    public Node<E> next;
    public Node<E> prev;

    public Node(E data){
      this(data, null, null);
    }
    public Node(E data, Node<E> next, Node<E> prev){
      this.data = data;
      this.next = next;
      this.prev = prev;
    }
  }

  public LinkedDeque(){
    this.head = null;
    this.foot = null;
    this.size = 0;
  }

  @Override
  public String toString(){
    if (this.size == 0){
      return "[]";
    }
    String retStr = "[";
    Node<E> curr = this.head;
    while (curr != null && curr.next != null){
      retStr += curr.data + ", ";
      curr = curr.next;
    }
    retStr += curr.data;
    return retStr + "]";
  }

  @Override
  public void addFront(E e) {
    if (this.size == 0 || this.head == null){
      this.head = new Node<E>(e);
      this.foot = this.head;
    }
    else{
      Node<E> newN = new Node<E>(e, this.head, null);
      this.head.prev = newN;
      this.head = newN;
    }
    this.size++;
  }

  @Override
  public void addBack(E e) {
    if (this.size == 0 || this.head == null){
      this.head = new Node<E>(e);
      this.foot = this.head;
    }
    else {
      Node<E> newN = new Node<E>(e, null, this.foot);
      this.foot.next = newN;
      this.foot = newN;
    }
    this.size++;
  }

  @Override
  public E removeFront() {
    if (this.size == 0 || this.head == null) {
      return null;
    }
    E first = this.head.data;
    if (this.head == this.foot || this.size == 1) {
      this.head = null;
      this.foot = null;
    }
    else {
      this.head = this.head.next;
      this.head.prev = null;
    }
    this.size--;
    return first;
  }
  @Override
  public E removeBack() {
    if (this.size == 0 || this.head == null) {
      return null;
    }
    E last = this.foot.data;
    if (this.head == this.foot || this.size == 1){
      this.foot = null;
      this.head = null;
    }
    else {
      this.foot = this.foot.prev;
      this.foot.next = null;
    }
    this.size --;
    return last;
  }

  @Override
  public boolean enqueue(E e) {
    this.addFront(e);
    return true;
  }

  @Override
  public E dequeue() {
    return this.removeBack();
  }

  @Override
  public boolean push(E e) {
    this.addBack(e);
    return true;
  }

  @Override
  public E pop() {
    return this.removeBack();
  }

  @Override
  public E peekFront() {
    if (this.size == 0 || this.head == null){
      return null;
    }
    return this.head.data;
  }

  @Override
  public E peekBack() {
    return this.peek();
  }

  @Override
  public E peek() {
    if (this.size == 0 || this.head == null || this.foot == null){
      return null;
    }
    return this.foot.data;
  }

  private class LinkedDequeIterator implements Iterator<E> {

    private Node<E> currentNode = LinkedDeque.this.head;
    private int curr_idx = 0;

    @Override
    public boolean hasNext() {
      return curr_idx < LinkedDeque.this.size;
    }

    @Override
    public E next() {
      if (hasNext()) {
        E e = this.currentNode.data;
        this.currentNode = this.currentNode.next;
        curr_idx++;
        return e;
      }
      return null;
    }
  }
  @Override
  public Iterator<E> iterator() {
    return new LinkedDequeIterator();
  }

  @Override
  public int size() {
    return this.size;
  }
}
