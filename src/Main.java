import structs.BoundsGraphVertex;
import structs.EuclidDirectedGraph;


import java.util.HashMap;
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
        BoundsGraphVertex ver1 = new BoundsGraphVertex(10, 10);
        BoundsGraphVertex ver2 = new BoundsGraphVertex(10, 10);
        graph1.addNode(ver1);
        graph1.addNode(ver2);
        graph1.addEuclidEdge(ver1, ver2);
        //created graph #2
        EuclidDirectedGraph graph2 = new EuclidDirectedGraph();
        try {
            //save into file from graph #1
            XMLSerializer.write(graph1, "input.xml");
            //read from file into graph #2
            graph2 = (EuclidDirectedGraph) XMLSerializer.read("output.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //check all vetexes from graph #1 was saved
        for (BoundsGraphVertex vertex :graph2) {
            System.out.println(vertex.getX() + " " + vertex.getY());
        }



    }
}
