package main.java;

import main.java.structs.BoundsGraphVertex;
import main.java.structs.EuclidDirectedGraph;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Shatin
 *         11-501
 */
public class Main {
    public static void main(String[] args) {
        //new graph graph #1
        EuclidDirectedGraph graph1 = new EuclidDirectedGraph();
        //vertexes added and created edge
        BoundsGraphVertex ver1 = new BoundsGraphVertex(10, 10, "source");
        BoundsGraphVertex ver2 = new BoundsGraphVertex(10, 30, "secondNode");
        BoundsGraphVertex ver3 = new BoundsGraphVertex(10, 40, "thirdNode");
        BoundsGraphVertex ver4 = new BoundsGraphVertex(10, 50, "sink");
        BoundsGraphVertex ver5 = new BoundsGraphVertex(10, 60, "fiveNode - loop from second");
        BoundsGraphVertex ver6 = new BoundsGraphVertex(10, 70, "sixNode - loop from five");
        BoundsGraphVertex ver7 = new BoundsGraphVertex(10, 80, "sevenNode - loop from six to third");

        graph1.addNode(ver1);
        graph1.addNode(ver2);
        graph1.addNode(ver3);
        graph1.addNode(ver4);
        graph1.addNode(ver5);
        graph1.addNode(ver6);
        graph1.addNode(ver7);
        //use .addEuclidEdge to compute edge's length automatically
        graph1.addEuclidEdge(ver1, ver2);
        graph1.addEuclidEdge(ver2, ver3);
        graph1.addEuclidEdge(ver3, ver4);
        graph1.addEuclidEdge(ver2, ver5);
        graph1.addEuclidEdge(ver5, ver6);
        graph1.addEuclidEdge(ver6, ver7);
        graph1.addEuclidEdge(ver7, ver3);
        //created graph #2
        EuclidDirectedGraph graph2 = new EuclidDirectedGraph();
        try {
            //save into file from graph #1
            XMLSerializer.write(graph1, "input.xml", false);
            //read from file into graph #2
            graph2 = (EuclidDirectedGraph) XMLSerializer.read("input.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //check all vetexes from graph #1 was saved
        for (BoundsGraphVertex vertex : graph2) {
            System.out.println(vertex.getX() + " " + vertex.getY());
        }

        //select graph for prepairing and handling by algorhytm
        EuclidDirectedGraph graph = graph1;
        //find all sources and sinks
        ArrayList<BoundsGraphVertex> sources = new ArrayList<>();
        ArrayList<BoundsGraphVertex> sinks = new ArrayList<>();
        //get all sources and sinks
        for (Map.Entry<BoundsGraphVertex, Map<BoundsGraphVertex, Double>> vertexEntry : graph.getMap().entrySet()) {
            //if there are no edges from vertex then its sink
            if (vertexEntry.getValue().isEmpty()) {
                sinks.add(vertexEntry.getKey());
            } else {
                //mark all vertexes which have incoming edges
                for (BoundsGraphVertex dest : vertexEntry.getValue().keySet()) {
                    dest.setMarked(true);
                }
            }
        }
        //all unmarked vertexes are sources
        for (BoundsGraphVertex vertex : graph) {
            if (!vertex.isMarked()) sources.add(vertex);
        }

        /*
        * First algorithm: for each source-sink pair get path using euclid heuristics
        * */
        List<BoundsGraphVertex> minPath = null;
        Double minLength = Double.MAX_VALUE;
        for (BoundsGraphVertex source :
                sources) {
            for (BoundsGraphVertex sink :
                    sinks) {
                //need use path storage list because algorhytm returns only double val of length.
                //path will be saved in this storage.
                List<BoundsGraphVertex> pathStorage = new ArrayList<>();
                //do algo
                Double length = Algorithms.shortestEuclidDijkstraFibonacciPathWithHeuristics(graph, source, sink, pathStorage, minLength);
                //check min
                if (minLength > length) {
                    minLength = length;
                    minPath = pathStorage;
                }

            }
        }

        /*
        * Second algorithm: for each source get better sink
        * */
        minPath = null;
        minLength = Double.MAX_VALUE;
        for (BoundsGraphVertex source :
                sources) {
            //need use path storage list because algorhytm returns only double val of length.
            //path will be saved in this storage.
            List<BoundsGraphVertex> pathStorage = new ArrayList<>();
            //do algo
            Double length = Algorithms.shortestEuclidDijkstraFibonacciPathToManySinks(graph, source, sinks, pathStorage, minLength);
            //check min
            if (minLength > length) {
                minLength = length;
                minPath = pathStorage;
            }
        }
        try {
            XMLSerializer.write(minPath, "output.xml", false);
            XMLSerializer.write(minLength, "output.xml", true);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
