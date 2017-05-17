package structs;
/**
 * @author Oleg Shatin
 *         11-501
 */
public class EuclidDirectedGraph extends DirectedGraph<BoundsGraphVertex> {

    public void addEuclidEdge(BoundsGraphVertex start, BoundsGraphVertex dest){
        super.addEdge(start, dest, BoundsGraphVertex.distance(start, dest));
    }


}
