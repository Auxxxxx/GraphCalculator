package org.example;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static final int precision = 10;

    static MyGraph graph;
    static int p1;

    public static void main(String[] args) {
        while (true) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                initP1(reader);
                initGraph(reader);
                initEdges(reader);
                initVertice(reader);

                boolean digitsOnly = graph.solve();
                printGraph(digitsOnly, reader);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Что-то пошло не так!!!");
            }
        }
    }

    private static void initP1(BufferedReader reader) throws IOException {
        System.out.print("Введите p-1: ");
        p1 = Integer.parseInt(reader.readLine());
    }

    private static void initGraph(BufferedReader reader) throws IOException {
        System.out.print("Введите количество вершин: ");
        int vertexAmount = Integer.parseInt(reader.readLine());
        graph = new MyGraph(vertexAmount);
        System.out.println();
    }

    private static void provideInitEdgePrompt() {
        System.out.println("Введите данные рёбер \n" +
                "Формат ввода ребра: вершина1 вершина2 вес \n" +
                "Пример: v2 v5 3 \n" +
                "Для завершения введите Стоп");
    }

    private static void initEdges(BufferedReader reader) throws IOException {
        provideInitEdgePrompt();
        while (true) {
            String input = reader.readLine();
            if (input == null || input.isBlank() || input.equals("Стоп")) {
                break;
            }
            processEdgeData(input.split(" "));
        }
    }

    private static void processEdgeData(String[] edgeData) {
        String sourceIndex = edgeData[0];
        String targetIndex = edgeData[1];
        double weight = Double.parseDouble(edgeData[2]);
        graph.addEdge(sourceIndex, targetIndex, weight);
    }

    private static void initVertice(BufferedReader reader) throws IOException {
        String[] indexes;
        System.out.println("Введите через запятую индексы вершин со значением p-1. Пример: v1,v5,v6");
        indexes = reader.readLine().split(",");
        for (String index : indexes) {
            if (!index.isBlank()) {
                graph.setVertexWeight(index, p1);
            }
        }
        System.out.println("Введите через запятую индексы вершин со значением 0. Пример: v4,v7");
        indexes = reader.readLine().split(",");
        for (String index : indexes) {
            if (!index.isBlank()) {
                graph.setVertexWeight(index, 0);
            }
        }
    }

    private static void printGraph(boolean digitsOnly, BufferedReader reader) throws IOException {
        if (!digitsOnly) {
            System.out.println("Невозможно заполнить граф целыми числами. Вывести на экран? (да/нет)");
            if (!reader.readLine().equals("да")) {
                return;
            }
        }
        DOTExporter<MyVertex, DefaultWeightedEdge> exporter = buildExporter(digitsOnly);
        Writer writer = new StringWriter();
        exporter.exportGraph(graph.getGraph(), writer);
        System.out.println(writer);
    }

    private static DOTExporter<MyVertex, DefaultWeightedEdge> buildExporter(boolean digitsOnly) {
        DOTExporter<MyVertex, DefaultWeightedEdge> exporter = new DOTExporter<>(MyVertex::toString);
        exporter.setVertexIdProvider(MyVertex::getIndex);
        exporter.setVertexAttributeProvider(v -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            if (digitsOnly) {
                String label = String.format("%s=%d", v.getIndex(), (int) v.getWeight());
                map.put("label", DefaultAttribute.createAttribute(label));
            } else {
                map.put("label", DefaultAttribute.createAttribute(v.toString()));
            }
            return map;
        });
        exporter.setEdgeAttributeProvider(e -> {
            Map<String, Attribute> map = new LinkedHashMap<>();
            map.put("label", DefaultAttribute.createAttribute((int) graph.getEdgeWeight(e)));
            return map;
        });
        return exporter;
    }
}