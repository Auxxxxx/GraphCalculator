package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyMatrixTest {

    @Test
    void solve() {
        double[][] lhs = new double[][]{
                {1.0, 0.0, 3.0},
                {0.0, 1.0, -2.0},
                {0.0, 0.0, 0.0}
        };
        double[] rhs = new double[] {4.0, 5.0, 0.0};
        double[] res = new MyMatrix(lhs, rhs).solve();
    }
}