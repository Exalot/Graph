package com.company;

public class Main {
    public static void main(String[] args) {
        mppDemo();
    }

    static void bfsDemo() {
        Graph<Character> graph = new Graph<>();

        graph.addVertex(new Character[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'});

        graph.addEdge('A', 'B');
        graph.addEdge('B', 'F');
        graph.addEdge('F', 'H');
        graph.addEdge('A', 'C');
        graph.addEdge('A', 'D');
        graph.addEdge('D', 'G');
        graph.addEdge('G', 'I');
        graph.addEdge('A', 'E');

        System.out.println(graph.breath_first('A'));
    }

    static void dfsDemo() {
        Graph<Character> graph = new Graph<>();

        graph.addVertex(new Character[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'});

        graph.addEdge('A', 'B');
        graph.addEdge('B', 'F');
        graph.addEdge('F', 'H');
        graph.addEdge('A', 'C');
        graph.addEdge('A', 'D');
        graph.addEdge('D', 'G');
        graph.addEdge('G', 'I');
        graph.addEdge('A', 'E');

        System.out.println(graph.breath_first('A'));
        System.out.println(graph.depth_first('A'));
    }

    static void mstDemo() {
        Graph<Character> graph = new Graph<>();

        graph.addVertex(new Character[] {'D', 'A', 'B', 'E', 'C', 'F'});

        graph.addEdge('D','A', 4);
        graph.addEdge('A','B', 6);
        graph.addEdge('B','D', 7);
        graph.addEdge('B','E', 7);
        graph.addEdge('B','C', 10);
        graph.addEdge('D','E', 12);
        graph.addEdge('D','C', 8);
        graph.addEdge('C','E', 5);
        graph.addEdge('C','F', 6);
        graph.addEdge('E','F', 7);

        System.out.println(graph.minimum_spanning_tree().edgeList);
    }

    static void mppDemo() {
        Graph<String> graph = new Graph<>();

        graph.addVertex(new String[] {"Ajo", "Bordo", "Danza", "Colina", "Erizo"});

        graph.addDirectedEdge("Ajo","Bordo", 50);
        graph.addDirectedEdge("Ajo","Danza", 80);
        graph.addDirectedEdge("Bordo","Danza", 90);
        graph.addDirectedEdge("Bordo", "Colina", 60);
        graph.addDirectedEdge("Danza", "Colina", 20);
        graph.addDirectedEdge("Colina", "Erizo", 40);
        graph.addDirectedEdge("Danza", "Erizo", 70);
        System.out.println(graph.minimumDistance("Ajo", "Colina").shortestPathDistance);
        System.out.println(graph.minimumDistance("Ajo", "Erizo").path);
    }
}
