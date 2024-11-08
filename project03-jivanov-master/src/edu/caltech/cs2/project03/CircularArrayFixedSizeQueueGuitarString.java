package edu.caltech.cs2.project03;

import edu.caltech.cs2.datastructures.CircularArrayFixedSizeQueue;
import edu.caltech.cs2.interfaces.IFixedSizeQueue;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Random;


public class CircularArrayFixedSizeQueueGuitarString {
    private static Random RANDOM;
    private IFixedSizeQueue<Double> guitar;
    private static final double SAMPLING_RATE = 44100.0;
    private static final double DECAY_FACTOR = 0.996;
    public CircularArrayFixedSizeQueueGuitarString(double frequency) {
        int capacity = (int) (SAMPLING_RATE / frequency) + 1;
        this.guitar = new CircularArrayFixedSizeQueue<>(capacity);
        for (int i = 0; i < capacity; i ++){
            this.guitar.enqueue(0.0);
        }
    }

    public int length() {
        return this.guitar.size();
    }

    public void pluck() {
        RANDOM = new Random();
        for (int i = 0; i < this.length(); i++){
            this.guitar.dequeue();
            double random = 0.5 - RANDOM.nextDouble();
            this.guitar.enqueue(random);
        }
    }

    public void tic() {
        double first = this.guitar.dequeue();
        double newF = this.guitar.peek();
        double newValue = (first + newF) / 2 * DECAY_FACTOR;
        this.guitar.enqueue(newValue);
    }

    public double sample() {
        return this.guitar.peek();
    }
}
