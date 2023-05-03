package org.example;

import Jama.Matrix;
import org.junit.jupiter.api.Test;

class MyGraphTest {

    @Test
    void test5VerticeGraphGetLhsArray() {
        int p1 = 60;
        int verticeAmount = 5;
        MyGraph graph = new MyGraph(5);
        graph.addEdge("v1", "v2", 2);
        graph.addEdge("v2", "v3", 3);
        graph.addEdge("v2", "v4", 2);
        graph.addEdge("v3", "v4", 1);
        graph.addEdge("v4", "v5", 2);
        graph.setVertexWeight("v1", p1);
        graph.setVertexWeight("v5", 0);
        boolean onlyDigits = graph.solve();
    }
}