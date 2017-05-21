package main.java; /**
 * @author Oleg Shatin
 * 11-501
 */

import main.java.structs.BoundsGraphVertex;
import main.java.structs.EuclidDirectedGraph;
import main.java.structs.FibonacciHeap;

import java.util.*;

public class Algorithms {
    public static Double shortestEuclidDijkstraFibonacciPathWithHeuristics(EuclidDirectedGraph graph,
                                                             BoundsGraphVertex source,
                                                             BoundsGraphVertex sink,
                                                             List<BoundsGraphVertex> resultStorage,
                                                             Double oldShortestCost) {
        /* Create a Fibonacci heap storing the distances of unvisited nodes
         * from the source node.
         */
        FibonacciHeap<BoundsGraphVertex> pq = new FibonacciHeap<BoundsGraphVertex>();

        /* Create a map - tree, which returns parent vertex by child.
         */
        HashMap<BoundsGraphVertex, BoundsGraphVertex> parentsStorage = new HashMap<>();

        /* The Fibonacci heap uses an internal representation that hands back
         * Entry objects for every stored element.  This map associates each
         * node in the graph with its corresponding Entry.
         */
        Map<BoundsGraphVertex, FibonacciHeap.Entry<BoundsGraphVertex>> entries
                = new HashMap<BoundsGraphVertex, FibonacciHeap.Entry<BoundsGraphVertex>>();

        /* Maintain a map from nodes to their distances.  Whenever we expand a
         * node for the first time, we'll put it in here.
         */
        Map<BoundsGraphVertex, Double> result = new HashMap<BoundsGraphVertex, Double>();

        /* Add each node to the Fibonacci heap at
         * priority == {current cost from source to vertex} + {distance from vertex to sink}
         * but infinity since initially all nodes are unreachable.
         */
        for (BoundsGraphVertex node : graph)
            entries.put(node, pq.enqueue(node, Double.POSITIVE_INFINITY));

        /* Update the source so that it's at distance 0.0 from itself + distance to sink; after
         * all, we can get there with a path of length zero!
         */
        pq.decreaseKey(entries.get(source), BoundsGraphVertex.distance(source, sink));

        /* Keep processing the queue until no nodes remain.
         * Priority == {current cost from source to vertex} + {distance from vertex to sink}
         * */
        while (!pq.isEmpty()) {
            /* Grab the current node.  The algorithm guarantees that we now
             * have the shortest distance to it.
             */
            FibonacciHeap.Entry<BoundsGraphVertex> curr = pq.dequeueMin();

            /* Stop searching if we already had shortest cost and now we're going too far. */
            if (curr.getPriority() > oldShortestCost) break;
            /* Store this in the result table. */
            result.put(curr.getValue(), curr.getPriority());
            //if current is sink (finish point) stop algo
            if (curr.getValue() == sink) break;


            /* Update the priorities of all of its edges. */
            for (Map.Entry<BoundsGraphVertex, Double> arc : graph.edgesFrom(curr.getValue()).entrySet()) {
                /* If we already know the shortest path from the source to
                 * this node, don't add the edge.
                 */
                if (result.containsKey(arc.getKey())) continue;

                /* Compute the cost of the path from the source to this node,
                 * (but priority containing current cost + distance from vertex to sink,
                 * important to get more relevant vertexes from pq: need to reduce distance from vertex to sink
                 * to get true current distance)
                 * which is the cost of this node plus the cost of this edge.
                 */
                double trueDistanceFromSourceToCurr = curr.getPriority() - BoundsGraphVertex.distance(curr.getValue(), sink);
                double pathCost = trueDistanceFromSourceToCurr + arc.getValue();

                /* If the length of the best-known path from the source to
                 * this node is longer than this potential path cost, update
                 * the cost of the shortest path (but priority containing current cost +
                 * distance from vertex to sink, important to get more relevant vertexes from pq)
                 * and add curr to parents storage like parent of arc.
                 */
                FibonacciHeap.Entry<BoundsGraphVertex> dest = entries.get(arc.getKey());
                double trueBetterKnownCostToCurr = dest.getPriority() - BoundsGraphVertex.distance(dest.getValue(), sink);
                if (pathCost < trueBetterKnownCostToCurr) {
                    pq.decreaseKey(dest, pathCost + BoundsGraphVertex.distance(dest.getValue(), sink));
                    parentsStorage.put(arc.getKey(), curr.getValue());
                }

            }
        }
        //stack for reversing path
        Stack<BoundsGraphVertex> stack = new Stack<>();
        BoundsGraphVertex currentPathPoint = sink;
        while (currentPathPoint != null){
            stack.push(currentPathPoint);
            currentPathPoint = parentsStorage.get(currentPathPoint);
        }
        //reversing path and saving into storage
        while (!stack.isEmpty()){
            resultStorage.add(stack.pop());
        }

        /* Finally, report the distance we've found. */
        return result.get(sink);
    }

    public static Double shortestEuclidDijkstraFibonacciPathToManySinks(EuclidDirectedGraph graph,
                                                                        BoundsGraphVertex source,
                                                                        List<BoundsGraphVertex> sinks,
                                                                        List<BoundsGraphVertex> resultStorage,
                                                                        Double oldShortestCost) {
        /* Create a Fibonacci heap storing the distances of unvisited nodes
         * from the source node.
         */
        FibonacciHeap<BoundsGraphVertex> pq = new FibonacciHeap<BoundsGraphVertex>();

        /* Create a map - tree, which returns parent vertex by child.
         */
        HashMap<BoundsGraphVertex, BoundsGraphVertex> parentsStorage = new HashMap<>();

        /* The Fibonacci heap uses an internal representation that hands back
         * Entry objects for every stored element.  This map associates each
         * node in the graph with its corresponding Entry.
         */
        Map<BoundsGraphVertex, FibonacciHeap.Entry<BoundsGraphVertex>> entries
                = new HashMap<BoundsGraphVertex, FibonacciHeap.Entry<BoundsGraphVertex>>();

        /* Maintain a map from nodes to their distances.  Whenever we expand a
         * node for the first time, we'll put it in here.
         */
        Map<BoundsGraphVertex, Double> result = new HashMap<BoundsGraphVertex, Double>();

        /* Add each node to the Fibonacci heap at distance +infinity since
         * initially all nodes are unreachable.
         */
        for (BoundsGraphVertex node : graph)
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
            FibonacciHeap.Entry<BoundsGraphVertex> curr = pq.dequeueMin();
            /* Stop searching if we already had shortest cost. */
            if (curr.getPriority() > oldShortestCost) break;
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
                double pathCost = curr.getPriority()  + arc.getValue();

                /* If the length of the best-known path from the source to
                 * this node is longer than this potential path cost, update
                 * the cost of the shortest path and add curr to parents storage like parent of arc.
                 */
                FibonacciHeap.Entry<BoundsGraphVertex> dest = entries.get(arc.getKey());
                if (pathCost < dest.getPriority()) {
                    pq.decreaseKey(dest, pathCost);
                    parentsStorage.put(arc.getKey(), curr.getValue());
                }

            }
        }
        /*
        * Attempt to find best sink and cost to it
        * */
        BoundsGraphVertex bestSink = null;
        Double minCostValue = Double.POSITIVE_INFINITY;
        for (BoundsGraphVertex sink : sinks){
            if (result.containsKey(sink)) {

                Double costValueToSink = result.get(sink);
                if (minCostValue > costValueToSink) {

                    minCostValue = costValueToSink;
                    bestSink = sink;

                }
            }
        }
        /*
        * If best sink founded need to save path to this sink into storage*/
        if (bestSink != null){

            //stack for reversing path
            Stack<BoundsGraphVertex> stack = new Stack<>();
            BoundsGraphVertex currentPathPoint = bestSink;
            while (currentPathPoint != null){
                stack.push(currentPathPoint);
                currentPathPoint = parentsStorage.get(currentPathPoint);
            }
            //reversing path and saving into storage
            while (!stack.isEmpty()){
                resultStorage.add(stack.pop());
            }

        }
        /* Finally, report the distance we've found or +INFINITY if there are no better paths */
        return result.get(bestSink);
    }
}
