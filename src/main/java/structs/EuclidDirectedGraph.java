package main.java.structs;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

/**
 * @author Oleg Shatin
 *         11-501
 */
@XmlRootElement
public class EuclidDirectedGraph implements Iterable<BoundsGraphVertex>,Serializable {

    public EuclidDirectedGraph(){

    }


    /* A map from nodes in the graph to sets of outgoing edges.  Each
         * set of edges is represented by a map from edges to doubles.
         */

    public Map<BoundsGraphVertex, Map<BoundsGraphVertex, Double>> getMap() {
        return mGraph;
    }

    public void setMap(Map<BoundsGraphVertex, Map<BoundsGraphVertex, Double>> mGraph) {
        this.mGraph = mGraph;
    }
    @XmlElement
    protected Map<BoundsGraphVertex, Map<BoundsGraphVertex, Double>> mGraph = new HashMap<BoundsGraphVertex, Map<BoundsGraphVertex, Double>>();

    /**
     * Adds a new node to the graph.  If the node already exists, this
     * function is a no-op.
     *
     * @param node The node to add.
     * @return Whether or not the node was added.
     */
    public boolean addNode(BoundsGraphVertex node) {
        /* If the node already exists, don't do anything. */
        if (mGraph.containsKey(node))
            return false;

        /* Otherwise, add the node with an empty set of outgoing edges. */
        mGraph.put(node, new HashMap<BoundsGraphVertex, Double>());
        return true;
    }

    /**
     * Given a start node, destination, and length, adds an arc from the
     * start node to the destination of the length.  If an arc already
     * existed, the length is updated to the specified value.  If either
     * endpoint does not exist in the graph, throws a NoSuchElementException.
     *
     * @param start  The start node.
     * @param dest   The destination node.
     * @param length The length of the edge.
     * @throws NoSuchElementException If either the start or destination nodes
     *                                do not exist.
     */
    public void addEdge(BoundsGraphVertex start, BoundsGraphVertex dest, double length) {
        /* Confirm both endpoints exist. */
        if (!mGraph.containsKey(start) || !mGraph.containsKey(dest))
            throw new NoSuchElementException("Both nodes must be in the graph.");

        /* Add the edge. */
        mGraph.get(start).put(dest, length);
    }



    /**
     * Removes the edge from start to dest from the graph.  If the edge does
     * not exist, this operation is a no-op.  If either endpoint does not
     * exist, this throws a NoSuchElementException.
     *
     * @param start The start node.
     * @param dest  BoundsGraphVertexhe destination node.
     * @throws NoSuchElementException If either node is not in the graph.
     */
    public void removeEdge(BoundsGraphVertex start, BoundsGraphVertex dest) {
        /* Confirm both endpoints exist. */
        if (!mGraph.containsKey(start) || !mGraph.containsKey(dest))
            throw new NoSuchElementException("Both nodes must be in the graph.");

        mGraph.get(start).remove(dest);
    }

    /**
     * Given a node in the graph, returns an immutable view of the edges
     * leaving that node, as a map from endpoints to costs.
     *
     * @param node The node whose edges should be queried.
     * @return An immutable view of the edges leaving that node.
     * @throws NoSuchElementException If the node does not exist.
     */
    public Map<BoundsGraphVertex, Double> edgesFrom(BoundsGraphVertex node) {
        /* Check that the node exists. */
        Map<BoundsGraphVertex, Double> arcs = mGraph.get(node);
        if (arcs == null)
            throw new NoSuchElementException("Source node does not exist.");

        return Collections.unmodifiableMap(arcs);
    }

    /**
     * Returns an iterator that can traverse the nodes in the graph.
     *
     * @return An iterator that traverses the nodes in the graph.
     */
    public Iterator<BoundsGraphVertex> iterator() {
        return mGraph.keySet().iterator();
    }
    public void addEuclidEdge(BoundsGraphVertex start, BoundsGraphVertex dest){
        addEdge(start, dest, BoundsGraphVertex.distance(start, dest));
    }



}
