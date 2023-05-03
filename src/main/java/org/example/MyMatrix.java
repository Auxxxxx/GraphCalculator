package org.example;

import Jama.LUDecomposition;
import Jama.Matrix;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class MyMatrix {
    private final Matrix lhs;
    private final Matrix rhs;

    public MyMatrix(double[][] lhs, double[] rhs) {
        this.lhs = new Matrix(lhs);
        this.rhs = new Matrix(rhs, rhs.length);
    }

    public double[] solve() {
        double[][] solutions = lhs.solve(rhs).transpose().getArray();
        return solutions[0];
    }

    private double[] toSingleDimension(double[][] arr) {
        return Arrays.stream(arr)
                .flatMapToDouble(a -> DoubleStream.of(a[0]))
                .toArray();
    }

}
