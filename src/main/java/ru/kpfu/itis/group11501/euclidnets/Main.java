package ru.kpfu.itis.group11501.euclidnets;


import ru.kpfu.itis.group11501.euclidnets.structs.BoundsGraphVertex;
import ru.kpfu.itis.group11501.euclidnets.structs.EuclidDirectedGraph;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Oleg Shatin
 *         11-501
 */
public class Main {
    public static void main(String[] args) {

        //generate graph to input.xml
        generateAndSaveExampleGraph();


        /*
        * select graph for prepairing and handling by algorhytm
        * determine graph source from config.xml:
        * config case = ?
        * 1 - from matrix
        * 2 - from edges list
        * 3 - from xml
        * */
        //read configCase
        Integer configCase = 0;
        try {
            configCase = (Integer) XMLSerializer.read( "config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //determine case
        EuclidDirectedGraph graph = null;
        switch (configCase){
            case 1:
                graph = getGraphFromMatrix();
                break;
            case 2:
                graph = getGraphFromEdgesList();
                break;
            case 3:
                graph = getGraphFromXML();
                break;
            default:
                return;

        }
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

    private static EuclidDirectedGraph getGraphFromEdgesList() {
        EuclidDirectedGraph resultGraph = new EuclidDirectedGraph();
        /*
        * attempt to get graph from matrix
        * */
        try {
            Scanner scanner = new Scanner(new File("edges.txt"));

            if (scanner.hasNext()) {
                //create arrayList to get vertex by int
                ArrayList<BoundsGraphVertex> vArray = new ArrayList<>();
                //scan vertexes
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    //to guarantied skip space
                    if (line.contains("*") || line.equals("*"))
                        break;
                    //get bounds
                    double[] bounds = Arrays.stream(line.split(" "))
                            .mapToDouble(Double::parseDouble).toArray();
                    //create vertex
                    //create vertex and add it to array
                    BoundsGraphVertex vertex = new BoundsGraphVertex(bounds[0], bounds[1], vArray.size() + 1 + "");
                    vArray.add(vertex);
                    resultGraph.addNode(vertex);
                }
                //scan and add edges
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    int[] edge = Arrays.stream(line.split(" "))
                            .mapToInt(Integer::parseInt).toArray();
                    //get vertexes from array[0..n-1] with index shifting
                    resultGraph.addEuclidEdge(vArray.get(edge[0] - 1), vArray.get(edge[1] - 1));
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resultGraph;
    }

    private static EuclidDirectedGraph getGraphFromXML() {
        EuclidDirectedGraph graph = null;
        try {
            //read from file into graph #2
            graph = (EuclidDirectedGraph) XMLSerializer.read("input.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static void generateAndSaveExampleGraph() {
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
    }

    public static EuclidDirectedGraph getGraphFromMatrix() {
        EuclidDirectedGraph graphFromMatrix = new EuclidDirectedGraph();
        /*
        * attempt to get graph from matrix
        * */
        try {
            Scanner scanner = new Scanner(new File("matrix.txt"));

            if (scanner.hasNext()) {
                //get first line
                Boolean[] oneVertexLine = Arrays.stream(scanner.nextLine().split(" "))
                        .map(Boolean::parseBoolean).toArray(Boolean[]::new);
                //find vertexes count - n
                int n = oneVertexLine.length;
                //create arrayList to get vertex by int
                ArrayList<BoundsGraphVertex> vArray = new ArrayList<>(n);
                //create bool matrix
                Boolean[][] matrix = new Boolean[n][n];
                //generate n vertexes and add it to graph and arrayList
                for (int i = 0; i < n; i++) {
                    BoundsGraphVertex vertex = new BoundsGraphVertex();
                    vertex.setName("" + i + 1);
                    //index of this vertex in ArrayList matches i
                    vArray.add(vertex);
                    graphFromMatrix.addNode(vertex);
                }
                //add first line to mtx
                matrix[0] = oneVertexLine;

                // read other lines
                for (int i = 1; scanner.hasNext() && i < n; i++) {
                    oneVertexLine =  Arrays.stream(scanner.nextLine().split(" "))
                            .map(Boolean::parseBoolean).toArray(Boolean[]::new);
                    matrix[i] = oneVertexLine;
                }

                //reading bounds
                for (int i = 0; scanner.hasNext() && i < n; i++) {
                    String line = scanner.nextLine();
                    //to guarantied skip space
                    if (line.contains("*")) {
                        i--;
                        continue;
                    }
                    //get bounds
                    double[] bounds = Arrays.stream(line.split(" "))
                            .mapToDouble(Double::parseDouble).toArray();
                    //write bounds
                    vArray.get(i).setX(bounds[0]);
                    vArray.get(i).setY(bounds[1]);
                }

                //adding edges
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        //if we have edge then add it.
                        if (matrix[i][j])
                            graphFromMatrix.addEuclidEdge(vArray.get(i), vArray.get(j));
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return graphFromMatrix;
    }
}
