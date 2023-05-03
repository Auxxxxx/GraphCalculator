package org.example;

import java.util.Objects;

public class MyVertex {
    private static final String indexPrefix = "v";
    private static Integer indexCounter;

    private final String index;
    private double weight;
    private boolean calculated;

    public MyVertex() {
        indexCounter++;
        this.index = indexPrefix + indexCounter;
        this.calculated = false;
    }

    public static void resetCounter() {
        indexCounter = 0;
    }

    private MyVertex(String index) {
        this.index = index;
    }

    public static MyVertex of(String index) {
        return new MyVertex(index);
    }

    public String getIndex() {
        return index;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public boolean isCalculated() {
        return calculated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyVertex myVertex = (MyVertex) o;
        return Objects.equals(index, myVertex.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }

    @Override
    public String toString() {
        return String.format("%s=%.10f", index, weight);
    }
}