package org.example;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class MyGraph {
    private final List<MyVertex> vertice;
    private final Graph<MyVertex, DefaultWeightedEdge> graph;

    public MyGraph(int vertexAmount) {
        vertice = new ArrayList<>();
        graph = buildGraph();
        MyVertex.resetCounter();
        for (int i = 0; i < vertexAmount; i++) {
            MyVertex vertex = new MyVertex();
            graph.addVertex(vertex);
            vertice.add(vertex);
        }
    }

    // initialization

    public void setVerticeWeights(List<Integer> weights) {
        Iterator<Integer> iterator = weights.iterator();
        for (MyVertex vertex : vertice) {
            if (vertex.isCalculated()) {
                int weight = iterator.next();
                vertex.setWeight(weight);
            }
        }
    }

    public void addEdge(String sourceIndex, String targetIndex, double weight) {
        Graphs.addEdge(graph, MyVertex.of(sourceIndex), MyVertex.of(targetIndex), weight);
    }

    public void setVertexWeight(String index, int weight) {
        MyVertex vertex = getVertex(index);
        vertex.setWeight(weight);
        vertex.setCalculated(true);
    }

    private MyVertex getVertex(String index) {
        return vertice.stream()
                .filter(v -> v.getIndex().equals(index))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private DefaultWeightedEdge getEdge(String sourceIndex, String targetIndex) {
        return graph.getEdge(MyVertex.of(sourceIndex), MyVertex.of(targetIndex));
    }

    public double getEdgeWeight(DefaultWeightedEdge edge) {
        return graph.getEdgeWeight(edge);
    }


    private Graph<MyVertex, DefaultWeightedEdge> buildGraph() {
        return GraphTypeBuilder
                .<MyVertex, DefaultWeightedEdge>undirected().allowingMultipleEdges(false)
                .allowingSelfLoops(false).edgeClass(DefaultWeightedEdge.class).weighted(true).buildGraph();
    }

    //calculation

    public boolean solve() {
        MyMatrix matrix = new MyMatrix(getLhsArray(), getRhsArray());
        double[] solutions = matrix.solve();

        List<MyVertex> notCalculated = getNotCalculated();
        for (int vertexIndex = 0; vertexIndex < notCalculated.size(); vertexIndex++) {
            MyVertex vertex = notCalculated.get(vertexIndex);
            vertex.setWeight(solutions[vertexIndex]);
            vertex.setCalculated(true);
        }

        return Arrays.stream(solutions).allMatch(this::isDigit);
    }

    private double[][] getLhsArray() {
        List<MyVertex> notCalculated = getNotCalculated();
        int len = notCalculated.size();
        double[][] lhsArray = new double[len][len];
        if (len == 0) {
            throw new RuntimeException("Граф уже заполнен");
        }

        for (int sourceIndex = 0; sourceIndex < notCalculated.size(); sourceIndex++) {
            MyVertex source = notCalculated.get(sourceIndex);

            double sourceCoef = 0;
            for (MyVertex target : getNeighbors(source.getIndex())) {
                DefaultWeightedEdge edge = getEdge(source.getIndex(), target.getIndex());
                sourceCoef += getEdgeWeight(edge);
            }
            lhsArray[sourceIndex][sourceIndex] = sourceCoef;

            for (int targetIndex = 0; targetIndex < notCalculated.size(); targetIndex++) {
                MyVertex target = notCalculated.get(targetIndex);
                if (source == target) {
                    continue;
                }

                double targetCoef = 0;
                DefaultWeightedEdge edge = getEdge(source.getIndex(), target.getIndex());
                if (edge != null) {
                    targetCoef = -getEdgeWeight(edge);
                }
                lhsArray[sourceIndex][targetIndex] = targetCoef;
            }
        }
        return lhsArray;
    }

    private double[] getRhsArray() {
        List<MyVertex> notCalculated = getNotCalculated();
        int len = notCalculated.size();
        double[] rhsArray = new double[len];

        for (int sourceIndex = 0; sourceIndex < notCalculated.size(); sourceIndex++) {
            MyVertex source = notCalculated.get(sourceIndex);

            double sourceCoef = 0;
            for (MyVertex target : getNeighbors(source.getIndex())) {
                if (target.isCalculated()) {
                    DefaultWeightedEdge edge = getEdge(source.getIndex(), target.getIndex());
                    sourceCoef += getEdgeWeight(edge) * target.getWeight();
                }
            }
            rhsArray[sourceIndex] = sourceCoef;
        }
        return rhsArray;
    }

    private List<MyVertex> getNotCalculated() {
        return vertice.stream()
                .filter(v -> !v.isCalculated())
                .collect(Collectors.toList());
    }

    private boolean isDigit(double number) {
        return isDigit(number, 10);
    }

    private boolean isDigit(double number, int precision) {
        long rounded = Math.round(number);
        double offset = Math.pow(0.1, precision);
        return rounded - offset < number && number < rounded + offset;
    }

    private boolean checkGraph() {
        for (MyVertex vertex : vertice) {
            if (vertex.isCalculated() && !checkVertex(vertex)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkVertex(MyVertex vertex) {
        int neighborWeights = 0;
        int edgeWeights = 0;
        for (MyVertex neighbor : getNeighbors(vertex.getIndex())) {
            int edgeWeight = (int) getEdgeWeight(graph.getEdge(vertex, neighbor));
            neighborWeights += edgeWeight * neighbor.getWeight();
            edgeWeights += edgeWeight;
        }
        return edgeWeights != 0 &&
                neighborWeights % edgeWeights == 0 &&
                vertex.getWeight() == neighborWeights / edgeWeights;
    }

    public List<MyVertex> getNeighbors(String index) {
        return Graphs.neighborSetOf(graph, MyVertex.of(index))
                .stream()
                .map(MyVertex::getIndex)
                .map(this::getVertex)
                .collect(Collectors.toList());
    }

    public Graph<MyVertex, DefaultWeightedEdge> getGraph() {
        return graph;
    }
}
