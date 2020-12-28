package com.company;

import java.util.*;

public class Graph<V> {
    HashMap<V, Vertex> vertexMap = new HashMap<>();
    ArrayList<Edge> edgeList = new ArrayList<>();
    HashMap<Vertex, ArrayList<Edge>> adjacencyList = new HashMap<>();

    public Graph() {
        // Creates an empty graph
    }

    public void addVertex(V value) {
        Vertex v = new Vertex(value);
        vertexMap.put(value, v);
        adjacencyList.put(v, new ArrayList<>());
    }

    public void addVertex(V[] values) {
        for (V value: values) {
            Vertex v = new Vertex(value);
            vertexMap.put(value, v);
            adjacencyList.put(v, new ArrayList<>());
        }
    }

    public void addEdge(V start, V finish) {
        Edge edge1 = new Edge(vertexMap.get(start), vertexMap.get(finish));
        Edge edge2 = new Edge(vertexMap.get(finish), vertexMap.get(start));
        adjacencyList.get(vertexMap.get(start)).add(edge1);
        adjacencyList.get(vertexMap.get(finish)).add(edge2);
        edgeList.add(edge1);
        edgeList.add(edge2);
    }

    public void addEdge(V start, V finish, double weight) {
        Edge edge1 = new Edge(vertexMap.get(start), vertexMap.get(finish), weight);
        Edge edge2 = new Edge(vertexMap.get(finish), vertexMap.get(start), weight);
        adjacencyList.get(vertexMap.get(start)).add(edge1);
        adjacencyList.get(vertexMap.get(finish)).add(edge2);
        edgeList.add(edge1);
        edgeList.add(edge2);
    }

    public void addEdge(V[][] edges) {
        for (V[] edge : edges) {
            if (edge.length == 2) {
                addEdge(edge[0], edge[1]);
            }
        }
    }

    public void addDirectedEdge(V start, V finish) {
        Edge edge1 = new Edge(vertexMap.get(start), vertexMap.get(finish));
        adjacencyList.get(vertexMap.get(start)).add(edge1);
        edgeList.add(edge1);
    }

    public void addDirectedEdge(V start, V finish, double weight) {
        Edge edge1 = new Edge(vertexMap.get(start), vertexMap.get(finish), weight);
        adjacencyList.get(vertexMap.get(start)).add(edge1);
        edgeList.add(edge1);
    }

    public HashMap<V, Vertex> getVertices() {
        return vertexMap;
    }

    public ArrayList<Edge> getEdges() {
        return edgeList;
    }

    public int totalWeight() {
        int totalWeight = 0;
        for (Edge e: edgeList) {
            totalWeight += e.weight;
        }
        return totalWeight;
    }

    public ArrayList<Vertex> depth_first(V startIndex) {
        ArrayList<Vertex> returnList = new ArrayList<>();
        Stack<Vertex> nodeStack = new Stack<>();
        Vertex currentVertex;

        returnList.add(vertexMap.get(startIndex));
        nodeStack.push(vertexMap.get(startIndex));
        vertexMap.get(startIndex).wasVisited = true;

        while (!nodeStack.isEmpty()) {
            currentVertex = nodeStack.peek();
            if (adjacencyList.get(currentVertex).size() != 0) {
                boolean edgeFound = false;
                for (Edge neighborsEdge: adjacencyList.get(currentVertex))
                {
                    if (!neighborsEdge.finish.wasVisited) {
                        returnList.add(neighborsEdge.finish);
                        nodeStack.push(neighborsEdge.finish);
                        neighborsEdge.finish.wasVisited = true;
                        edgeFound = true;
                        break; // Find other solution
                    }
                }
                if (!edgeFound)
                    nodeStack.pop();
            }
        }
        vertexMap.forEach((v, vertex) -> vertex.wasVisited = false);

        return returnList;
        }

    public ArrayList<Vertex> breath_first(V startIndex) {
        ArrayList<Vertex> returnList = new ArrayList<>();
        ArrayDeque<Vertex> nodeQueue = new ArrayDeque<>();
        Vertex currentVertex;

        returnList.add(vertexMap.get(startIndex));
        nodeQueue.add(vertexMap.get(startIndex));
        vertexMap.get(startIndex).wasVisited = true;

        while (!nodeQueue.isEmpty()) {
            currentVertex = nodeQueue.removeFirst();
            for (Edge e: adjacencyList.get(currentVertex)) {
                if (!e.finish.wasVisited) {
                    returnList.add(e.finish);
                    nodeQueue.add(e.finish);
                    e.finish.wasVisited = true;
                }
            }
        }

        vertexMap.forEach((v, vertex) -> vertex.wasVisited = false);

        return returnList;
    }

    public Graph<V> minimum_spanning_tree() {
        V startIndex = vertexMap.keySet().stream().findFirst().get();
        Graph<V> returnGraph = new Graph<>();
        Vertex currentVertex = vertexMap.get(startIndex);
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
        returnGraph.addVertex(currentVertex.element);
        currentVertex.isInTree = true;
        vertexMap.forEach((v, vertex) -> {returnGraph.addVertex(v);});

        boolean done = false;
        while (!done) {
            if (adjacencyList.get(currentVertex).size() > 0)
                for (Edge e: adjacencyList.get(currentVertex)) {
                    if (!e.finish.isInTree) {
                        priorityQueue.add(e);
                    }
                }

            priorityQueue.removeIf(e -> e.finish.isInTree);

            if (priorityQueue.peek() != null) {
                Edge currentEdge = priorityQueue.poll();
                returnGraph.addEdge(currentEdge.start.element, currentEdge.finish.element, currentEdge.weight);
                currentVertex = currentEdge.finish;
                currentVertex.isInTree = true;
                done = mst_complete();
            }
        }

        vertexMap.forEach((v, vertex) -> vertex.isInTree = false);

        return returnGraph;
    }

    private boolean mst_complete() {
        for (Vertex v: vertexMap.values()) {
            if (!v.isInTree)
                return false;
        }
        return true;
    }

    public MinimumPath minimumDistance(V startIndex, V finishIndex) {
        ArrayDeque<Vertex> vertexDeque = new ArrayDeque<>();
        Vertex currentVertex = vertexMap.get(startIndex);
        HashMap<Vertex, Distance> minimumDistanceMap = new HashMap<>();
        minimumDistanceMap.put(currentVertex, new Distance(0, null));
        currentVertex.wasVisited = true;

        boolean done = false;
        while (!done) {
            for (Edge e: adjacencyList.get(currentVertex)) {
                if (!e.finish.wasVisited) {
                    minimumDistanceMap.put(e.finish, new Distance(minimumDistanceMap.get(currentVertex).shortestDistance + e.weight, e));
                    e.finish.wasVisited = true;
                    vertexDeque.addLast(e.finish);
                }
                else if (!e.finish.isInTree && minimumDistanceMap.get(currentVertex).shortestDistance + e.weight < minimumDistanceMap.get(e.finish).shortestDistance) {
                    minimumDistanceMap.put(e.finish, new Distance(minimumDistanceMap.get(currentVertex).shortestDistance + e.weight, e));
                }
            }
            if (!vertexDeque.isEmpty()) {
                currentVertex = vertexDeque.removeFirst();
                currentVertex.isInTree = true;
            }
            else {
                done = true;
            }
        }
        for (Vertex v : vertexMap.values()) {
            v.wasVisited = false;
            v.isInTree = false;
        }

        return new MinimumPath(minimumDistanceMap, startIndex, finishIndex);
    }

    class MinimumPath {
        double shortestPathDistance;
        ArrayList<Edge> path = new ArrayList<>();

        MinimumPath (HashMap<Vertex, Distance> processedMinDistanceMap, V startVertex, V finishVertex) {
            shortestPathDistance = processedMinDistanceMap.get(vertexMap.get(finishVertex)).shortestDistance;
            Edge currentEdge = processedMinDistanceMap.get(vertexMap.get(finishVertex)).previousEdge;
            Stack<Edge> stackx = new Stack<>();
            while (currentEdge != null) {
                stackx.add(currentEdge);
                currentEdge = processedMinDistanceMap.get(currentEdge.start).previousEdge;
            }
            while (!stackx.isEmpty()) {
                path.add(stackx.pop());
            }
        }
    }

    class Distance {
        double shortestDistance;
        Edge previousEdge;

        Distance(double distance, Edge previousEdge) {
            this.shortestDistance = distance;
            this.previousEdge = previousEdge;
        }
    }

    class Edge implements Comparable<Edge> {
        Vertex start;
        Vertex finish;
        double weight = 1;

        Edge(Vertex start, Vertex finish) {
            this.start = start;
            this.finish = finish;
        }

        Edge(Vertex start, Vertex finish, double weight) {
            this.start = start;
            this.finish = finish;
            this.weight = weight;
        }

        public int compareTo(Edge compareEdge) {
            return (int)(this.weight - compareEdge.weight);
        }

        @Override
        public String toString() {
            return this.start + "->" + this.finish;
        }
    }

    class Vertex {
        V element;
        boolean wasVisited = false;
        boolean isInTree = false;

        Vertex(V element){
            this.element = element;
        }

        @Override
        public String toString() {
            return element.toString();
        }
    }
}
