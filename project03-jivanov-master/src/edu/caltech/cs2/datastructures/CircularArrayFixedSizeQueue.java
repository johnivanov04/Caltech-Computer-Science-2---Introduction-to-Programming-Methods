package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IFixedSizeQueue;

import java.util.Iterator;

public class CircularArrayFixedSizeQueue<E> implements IFixedSizeQueue<E> {
  private E[] backingList;
  private int front;
  private int size;
  public CircularArrayFixedSizeQueue(int capacity) {
    this.backingList = (E[]) new Object[capacity];
    this.front = capacity / 2;
    this.size = 0;
  }
  //make back method
  private int back(){
    int bck = this.front + this.size()-1;
    if (bck >= this.capacity()) {
      return bck - this.capacity();
    }
    return bck;
  }

  @Override
  public String toString(){
    if (this.size() == 0) {
      return "[]";
    }
    String s = "[";

    int tail = this.front + this.size() -1;
    if (tail >= this.capacity()){
      tail = tail - this.capacity();
    }
    int iterator = tail;
    for (int i = 0; i < this.size(); i ++){
      if (iterator == 0){
        iterator = 0;
      }
      else if (iterator < 0){
        iterator = this.capacity() -1;
      }
      s += this.backingList[iterator] + ", ";
      iterator --;
    }
    s = s.substring(0, s.length() -2);
    return s + "]";

  }

  @Override
  public boolean isFull() {
    return this.capacity() == this.size();
  }

  @Override
  public int capacity() {
    return this.backingList.length;
  }

  @Override
  public boolean enqueue(E e) {
    if (this.isFull()){
      return false;
    }
    if (this.front == 0){
      this.front = this.capacity() -1;
    }
    else{
      this.front = this.front - 1;
    }
    this.backingList[this.front] = e;
    this.size ++;
    return true;
  }

  @Override
  public E dequeue() {
    if (this.size() == 0){
      return null;
    }
    E back = this.backingList[this.back()];
    this.backingList[this.back()] = null;
    this.size --;
    return back;
  }

  @Override
  public E peek() {
    if (this.size() == 0){
      return null;
    }
    return this.backingList[this.back()];
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public void add(E e) {
    this.enqueue(e);
  }

  @Override
  public void clear() {
    for (int i = 0; i < this.size; i++){
      this.backingList[i] = null;
    }
    this.size = 0;
  }
  private class CircularArrayFixedSizeQueueIterator implements Iterator<E> {

    private int start;
    private int count;

    public CircularArrayFixedSizeQueueIterator() {
      this.start = CircularArrayFixedSizeQueue.this.back();
      this.count = 0;
    }

    public boolean hasNext() {
      return count < CircularArrayFixedSizeQueue.this.size();
    }

    public E next() {
      if (this.start < 0) {
        this.start = CircularArrayFixedSizeQueue.this.capacity() - 1;
      }
      E e = CircularArrayFixedSizeQueue.this.backingList[this.start];
      this.start--;
      this.count++;
      return e;
    }
  }
  @Override
  public Iterator<E> iterator() {
    return new CircularArrayFixedSizeQueueIterator();
  }
}

