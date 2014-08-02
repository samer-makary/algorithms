package utils.graph.shortestpath.allpairs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import utils.graph.Edge;
import utils.graph.GraphDirected;
import utils.graph.Vertex;
import utils.graph.WeightedEdge;
import utils.graph.shortestpath.IFinderAllPairsShortestPath;
import utils.graph.shortestpath.IFinderSingleSourceShortestPath;
import utils.graph.shortestpath.singlesource.BellmanFordAlgorithm;
import utils.graph.shortestpath.singlesource.DijkstrasAlgorithm;

public class JohnsonsAlgorithm implements IFinderAllPairsShortestPath {

	private final GraphDirected dg;

	public JohnsonsAlgorithm(GraphDirected dg) {
		if (dg == null)
			throw new IllegalArgumentException("Graph cannot be null");
		this.dg = dg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.graph.shortestpath.IFinderAllPairsShortestPath#
	 * getShortestPathsCostsFromAllSources()
	 */
	@Override
	public Map<Integer, Map<Integer, Double>> getShortestPathsCostsFromAllSources() {

		// O(n) add new dummy source vertex to the graph
		int newDummySource = extendGraphWithDummyVertex();

		// O(m * n) compute SPs cost from the new dummy vertex to all the
		// original vertices in the given graph using BellmanFord algorithm
		Map<Integer, Double> verticesShiftWeight = getVerticesBellmanFordWeights(newDummySource);
		if (verticesShiftWeight == null) {
			return null;
		}

		// O(m) shift edges costs by the difference of the weights of the head
		// and the tail of the edge itself as follows:
		// for edge e, cost(e) = cost(e) + (weight(TAIL(e)) - weight(HEAD(e)))
		shiftEdgesCostsByDiffOfVerticesWeights(verticesShiftWeight);

		// O(n * m * log n) compute the SPs for every given vertex as a source to
		// other graph vertices
		Map<Integer, Map<Integer, Double>> costs = runDijkstraOnAllPairs(newDummySource);

		// O(n * n) un-shift all shortest paths costs by the amount computed
		// from the difference in the weights of path source and destination
		// vertices
		unshiftEdgesCostsByDiffOfVerticesWeights(verticesShiftWeight, costs);

		return costs;
	}

	private void unshiftEdgesCostsByDiffOfVerticesWeights(
			Map<Integer, Double> verticesShiftWeight,
			Map<Integer, Map<Integer, Double>> costs) {

		for (Integer tid : costs.keySet()) {
			double tw = verticesShiftWeight.get(tid);
			Map<Integer, Double> tCosts = costs.get(tid);
			for (Integer hid : tCosts.keySet()) {
				double hw = verticesShiftWeight.get(hid);
				tCosts.put(hid, tCosts.get(hid) - (tw - hw));
			}
		}
	}

	private Map<Integer, Map<Integer, Double>> runDijkstraOnAllPairs(
			int newDummySource) {
		Map<Integer, Map<Integer, Double>> shiftedCosts = new HashMap<Integer, Map<Integer, Double>>(
				dg.getNumOfVertices());
		IFinderSingleSourceShortestPath dAlgo = new DijkstrasAlgorithm(dg);
		List<Integer> vertices = dg.getVerticesIDs();
		vertices.remove((Object) newDummySource);
		for (Integer sid : vertices) {
			dAlgo.computeShortestPathsFrom(sid);
			Map<Integer, Double> fromSCosts = new HashMap<Integer, Double>(
					dg.getNumOfVertices());
			for (Integer tid : vertices) {
				double cost = dAlgo.getShortestPathCostTo(tid);
				fromSCosts.put(tid, cost);
			}
			shiftedCosts.put(sid, fromSCosts);
		}

		return shiftedCosts;
	}

	private void shiftEdgesCostsByDiffOfVerticesWeights(
			Map<Integer, Double> verticesShiftWeight) {
		for (Integer tid : dg.getVerticesIDs()) {
			Vertex t = dg.getVertexByID(tid);
			double tw = verticesShiftWeight.get(tid);
			for (Edge e : new HashSet<Edge>(t.getDistinctAdjEdges())) {
				WeightedEdge we = (WeightedEdge) e;
				Integer hid = we.getOtherVertex(tid);
				double hw = verticesShiftWeight.get(hid);
				t.updateEdgeWeight(we, we.weight + (tw - hw));
			}
		}
	}

	private Map<Integer, Double> getVerticesBellmanFordWeights(
			int newDummySource) {
		IFinderSingleSourceShortestPath bmAlgo = new BellmanFordAlgorithm(dg);
		try {
			bmAlgo.computeShortestPathsFrom(newDummySource);
		} catch (IllegalArgumentException e) {
			return null;
		}

		Map<Integer, Double> spCost = new HashMap<Integer, Double>(
				dg.getNumOfVertices());
		for (Integer vid : dg.getVerticesIDs()) {
			double cost = bmAlgo.getShortestPathCostTo(vid);
			spCost.put(vid, cost);
		}
		return spCost;
	}

	private int extendGraphWithDummyVertex() {
		List<Integer> verticesIDs = dg.getVerticesIDs();
		int maxID = Integer.MIN_VALUE;
		for (int vid : verticesIDs)
			maxID = Math.max(maxID, vid);
		int newVertexID = maxID + 1;

		for (int vid : verticesIDs)
			dg.addConnection(newVertexID, vid, 0);

		return newVertexID;
	}

}
