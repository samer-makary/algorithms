package utils.graph.shortestpath.singlesource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.graph.Edge;
import utils.graph.GraphDirected;
import utils.graph.Vertex;
import utils.graph.WeightedEdge;
import utils.graph.shortestpath.IFinderSingleSourceShortestPath;

/**
 * This class implements the Bellman-Ford Shortest Path algorithm. Given a
 * directed graph, the algorithm will reverse the graph in the class constructor
 * to be able to access the incoming edges of every node easily.<br>
 * You can then easily compute the SPs starting from a given source to all the
 * other destinations in the aforementioned graph by calling
 * {@link #computeShortestPathsFrom(int)}.
 * 
 * @author Samer
 * 
 */
public class BellmanFordAlgorithm implements IFinderSingleSourceShortestPath {

	private final GraphDirected reversedGraph;
	private Map<Integer, VertexPathPredecessor> optPaths;

	public BellmanFordAlgorithm(GraphDirected dg) {
		if (dg == null)
			throw new IllegalArgumentException("Graph cannot be null");
		this.reversedGraph = dg.getReversed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.graph.shortestpath.IFinderSingleSourceShortestPath#
	 * computeShortestPathsFrom(int)
	 */
	@Override
	public void computeShortestPathsFrom(int s) throws IllegalArgumentException {
		initForSource(s);

		int N = reversedGraph.getNumOfVertices();

		// the following flag will be used to terminate the algorithm
		// prematurely if for any given loop i if no value for any vertex
		// changed, then no values will be changed anymore so there is no need
		// to continue.
		boolean changed = true;

		// next loop runs for N-1 iterations only because a path connecting all
		// N vertices can have at most N-1 edges and adding anymore edges will
		// cause a loop and create a cycle no-way.
		for (int i = 1; i < N - 1 && changed; i++) {
			changed = false;
			Map<Integer, VertexPathPredecessor> currIter = new HashMap<Integer, VertexPathPredecessor>(
					N);

			// now check for every vertex v if its adjacent incoming vertices u
			// has its SP cost decreased as this will in fact decrease the SP
			// cost to v through the use of vertex u.
			for (Integer vid : reversedGraph.getVerticesIDs()) {
				Vertex v = reversedGraph.getVertexByID(vid);
				VertexPathPredecessor minPred = optPaths.get(vid);

				for (Edge e : v.getAllAdjEdges()) {
					WeightedEdge we = (WeightedEdge) e;
					Integer uid = we.getOtherVertex(vid);
					double newCost = we.weight + optPaths.get(uid).pathCost;
					if (Double.compare(newCost, minPred.pathCost) < 0) {
						minPred = new VertexPathPredecessor(uid, newCost);
						changed = true;
					}
				}
				currIter.put(vid, minPred);
			}
			optPaths = currIter;
		}

		// check for any negative cost cycles
		if (hasNegativeCostCycles())
			throw new IllegalArgumentException(
					"Graph contains negative cost cycles");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.graph.shortestpath.IFinderSingleSourceShortestPath#
	 * getShortestPathCostTo(int)
	 */
	@Override
	public double getShortestPathCostTo(int t) {
		if (optPaths == null)
			throw new IllegalStateException("No paths computed yet");

		if (optPaths.containsKey(t)) {
			return optPaths.get(t).pathCost;
		} else {
			throw new IllegalArgumentException("Unknown vertex (t) id " + t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * utils.graph.shortestpath.IFinderSingleSourceShortestPath#getShortestPathTo
	 * (int)
	 */
	@Override
	public List<Integer> getShortestPathTo(int t) {
		if (optPaths == null)
			throw new IllegalStateException("No paths computed yet");

		if (!optPaths.containsKey(t)) {
			throw new IllegalArgumentException("Unknown vertex (t) id " + t);
		} else {
			VertexPathPredecessor vpp = optPaths.get(t);
			List<Integer> pathToT = new ArrayList<Integer>();
			if (vpp.predid != -1) {
				pathToT = getShortestPathTo(vpp.predid);
			}
			pathToT.add(t);
			return pathToT;
		}
	}

	private void initForSource(int s) {
		optPaths = new HashMap<Integer, VertexPathPredecessor>(
				reversedGraph.getNumOfVertices());

		for (Integer vid : reversedGraph.getVerticesIDs()) {
			VertexPathPredecessor vpp = new VertexPathPredecessor(-1,
					_INF_PATH_COST);
			if (vid == s)
				vpp.pathCost = 0;
			optPaths.put(vid, vpp);
		}
	}

	/**
	 * Checking for negative cost cycles is done by running the update SPs
	 * procedure one more time after the N-1 iterations. During this final run
	 * NO vertex value should be changed otherwise the graph must have had
	 * negative cost cycles.
	 * 
	 * @return <code>true</code> if a negative cost cycle was detected,
	 *         <code>false</code> otherwise.
	 */
	private boolean hasNegativeCostCycles() {
		int N = reversedGraph.getNumOfVertices();
		boolean changed = false;
		Map<Integer, VertexPathPredecessor> currIter = new HashMap<Integer, VertexPathPredecessor>(
				N);
		for (Integer vid : reversedGraph.getVerticesIDs()) {
			Vertex v = reversedGraph.getVertexByID(vid);
			VertexPathPredecessor minPred = optPaths.get(vid);

			for (Edge e : v.getAllAdjEdges()) {
				WeightedEdge we = (WeightedEdge) e;
				Integer uid = we.getOtherVertex(vid);
				double newCost = we.weight + optPaths.get(uid).pathCost;
				if (Double.compare(newCost, minPred.pathCost) < 0) {
					minPred = new VertexPathPredecessor(uid, newCost);
					changed = true;
				}
			}
			currIter.put(vid, minPred);
		}
		optPaths = currIter;

		return changed;
	}

	private class VertexPathPredecessor {
		int predid;
		double pathCost;

		public VertexPathPredecessor(int predid, double pathCost) {
			this.predid = predid;
			this.pathCost = pathCost;
		}
	}

	
}
