package main.java.ru.kpfu.itis.group11501.euclidnets;

import main.java.ru.kpfu.itis.group11501.euclidnets.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Amir Kadyrov
 * Date: 20.05.2017
 */
public class GraphGenerator {
    int numberOfSources = 0;
    int numberOfSinks = 0;

    public int getNumberOfSources() {
        return numberOfSources;
    }

    public int getNumberOfSinks() {
        return numberOfSinks;
    }

    public EuclidDirectedGraph generateGraph() {

        EuclidDirectedGraph graph = new EuclidDirectedGraph();

        System.out.println("Enter number of vertexes:");
        int n = (new Scanner(System.in)).nextInt();

        List<BoundsGraphVertex> vertexList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            BoundsGraphVertex vertex = new BoundsGraphVertex(random(-1000, 1000), random(-1000, 1000), String.valueOf(i));
            graph.addNode(vertex);
            vertexList.add(vertex);
        }

        //set the required number of sources and sinks

        if (vertexList.size() > 100){
            numberOfSources = random(1, vertexList.size() * 0.01);
            numberOfSinks = random(1, vertexList.size() * 0.01);
        }
        else {
            numberOfSources = 1;
            numberOfSinks = 1;
        }

        //set exact vertexes to sources or sinks
        ArrayList<BoundsGraphVertex> sources = new ArrayList<>();
        ArrayList<BoundsGraphVertex> sinks = new ArrayList<>();

        //setting sources
        for (int i = 0; i < numberOfSources; i++) {
            int id = random(0, vertexList.size() - 1);
            BoundsGraphVertex vertexCheckingHelper = vertexList.get(id);
            while (sources.contains(vertexCheckingHelper)){
                id = random(0, vertexList.size() - 1);
                vertexCheckingHelper = vertexList.get(id);
            }
            sources.add(vertexCheckingHelper);
        }

        //setting sinks; remember, that sinks can't contain sources
        for (int i = 0; i < numberOfSinks; i++) {
            int id = random(0, vertexList.size() - 1);
            BoundsGraphVertex vertexCheckingHelper = vertexList.get(id);
            while (sinks.contains(vertexCheckingHelper) || sources.contains(vertexCheckingHelper)){
                id = random(0, vertexList.size() - 1);
                vertexCheckingHelper = vertexList.get(id);
            }
            sinks.add(vertexCheckingHelper);
        }

        //creating edges
        for (int i = 0; i < n * 10; i++) {
            int sourceId = random(0, n-1);
            int sinkId = random(0, n-1);

            BoundsGraphVertex source = vertexList.get(sourceId);
            BoundsGraphVertex sink = vertexList.get(sinkId);

            //repeat randomizing until sink is not in the sources and source is not in the sinks
            while (sources.contains(sink) || sinks.contains(source)){
                sourceId = random(0, n-1);
                sinkId = random(0, n-1);

                source = vertexList.get(sourceId);
                sink = vertexList.get(sinkId);
            }

            graph.addEuclidEdge(source, sink);
        }

        return graph;
    }

    private static int random(int min, double max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
