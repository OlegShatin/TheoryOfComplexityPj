/**
 * @author Oleg Shatin
 * 11-501
 */

import structs.BoundsGraphVertex;
import structs.DirectedGraph;
import structs.EuclidDirectedGraph;
import structs.FibonacciHeap;

import java.util.*;

public class Algorhytms {
    public static <T> Map<T, Double> shortestEuclidDijkstraFibonacciPath(DirectedGraph<T> graph, T source,
                                                                         T sink, List<T> resultStorage) {
        /* Create a Fibonacci heap storing the distances of unvisited nodes
         * from the source node.
         */
        FibonacciHeap<T> pq = new FibonacciHeap<T>();

        /* The Fibonacci heap uses an internal representation that hands back
         * Entry objects for every stored element.  This map associates each
         * node in the graph with its corresponding Entry.
         */
        Map<T, FibonacciHeap.Entry<T>> entries = new HashMap<T, FibonacciHeap.Entry<T>>();

        /* Maintain a map from nodes to their distances.  Whenever we expand a
         * node for the first time, we'll put it in here.
         */
        Map<T, Double> result = new HashMap<T, Double>();

        /* Add each node to the Fibonacci heap at distance +infinity since
         * initially all nodes are unreachable.
         */
        for (T node : graph)
            entries.put(node, pq.enqueue(node, Double.POSITIVE_INFINITY));

        /* Update the source so that it's at distance 0.0 from itself; after
         * all, we can get there with a path of length zero!
         */
        pq.decreaseKey(entries.get(source), 0.0);

        /* Keep processing the queue until no nodes remain. */
        while (!pq.isEmpty()) {
            /* Grab the current node.  The algorithm guarantees that we now
             * have the shortest distance to it.
             */
            FibonacciHeap.Entry<T> curr = pq.dequeueMin();

            /* Store this in the result table. */
            result.put(curr.getValue(), curr.getPriority());

            /* Update the priorities of all of its edges. */
            for (Map.Entry<T, Double> arc : graph.edgesFrom(curr.getValue()).entrySet()) {
                /* If we already know the shortest path from the source to
                 * this node, don't add the edge.
                 */
                if (result.containsKey(arc.getKey())) continue;

                /* Compute the cost of the path from the source to this node,
                 * which is the cost of this node plus the cost of this edge.
                 */
                double pathCost = curr.getPriority() + arc.getValue();

                /* If the length of the best-known path from the source to
                 * this node is longer than this potential path cost, update
                 * the cost of the shortest path.
                 */
                FibonacciHeap.Entry<T> dest = entries.get(arc.getKey());
                if (pathCost < dest.getPriority())
                    pq.decreaseKey(dest, pathCost);
            }
        }

        /* Finally, report the distances we've found. */
        return result;
    }

    public static Double shortestEuclidDijkstraFibonacciPath(EuclidDirectedGraph graph, BoundsGraphVertex source,
                                                             BoundsGraphVertex sink, List<BoundsGraphVertex> resultStorage) {
        /* Create a Fibonacci heap storing the distances of unvisited nodes
         * from the source node.
         */

        FibonacciHeap<BoundsGraphVertex> pq = new FibonacciHeap<BoundsGraphVertex>();

        /* The Fibonacci heap uses an internal representation that hands back
         * Entry objects for every stored element.  This map associates each
         * node in the graph with its corresponding Entry.
         */
        Map<BoundsGraphVertex, FibonacciHeap.Entry<BoundsGraphVertex>> entries = new HashMap<BoundsGraphVertex, FibonacciHeap.Entry<BoundsGraphVertex>>();

        /* Maintain a map from nodes to their distances.  Whenever we expand a
         * node for the first time, we'll put it in here.
         */
        Map<BoundsGraphVertex, Double> result = new HashMap<BoundsGraphVertex, Double>();

        /* Add each node to the Fibonacci heap at distance between node and sink
         */
        for (BoundsGraphVertex node : graph)
            entries.put(node, pq.enqueue(node, BoundsGraphVertex.distance(node, sink)));

        /* Update the source so that it's at distance 0.0 from itself; after
         * all, we can get there with a path of length zero!
         */
        pq.decreaseKey(entries.get(source), 0.0);

        /* Keep processing the queue until no nodes remain. */
        while (!pq.isEmpty()) {
            /* Grab the current node.  The algorithm guarantees that we now
             * have the shortest distance to it.
             */
            FibonacciHeap.Entry<BoundsGraphVertex> curr = pq.dequeueMin();

            /* Store this in the result table. */
            result.put(curr.getValue(), curr.getPriority());

            /* Update the priorities of all of its edges. */
            for (Map.Entry<BoundsGraphVertex, Double> arc : graph.edgesFrom(curr.getValue()).entrySet()) {
                /* If we already know the shortest path from the source to
                 * this node, don't add the edge.
                 */
                if (result.containsKey(arc.getKey())) continue;

                /* Compute the cost of the path from the source to this node,
                 * which is the cost of this node plus the cost of this edge.
                 */
                double pathCost = curr.getPriority() + arc.getValue();

                /* If the length of the best-known path from the source to
                 * this node is longer than this potential path cost, update
                 * the cost of the shortest path.
                 */
                FibonacciHeap.Entry<BoundsGraphVertex> dest = entries.get(arc.getKey());
                if (pathCost < dest.getPriority())
                    pq.decreaseKey(dest, pathCost);
            }
        }

        /* Finally, report the distances we've found. */
        return 0.0;
    }



}
