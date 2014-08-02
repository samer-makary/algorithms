package utils.graph.shortestpath.allpairs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.graph.GraphDirected;
import utils.graph.shortestpath.IFinderAllPairsShortestPath;
import utils.graph.shortestpath.singlesource.BellmanFordAlgorithm;

public class BellmanFordBasedAlgorithm implements IFinderAllPairsShortestPath {

	private final List<Integer> verticesIDs;
	private final BellmanFordAlgorithm bellmanFord;

	public BellmanFordBasedAlgorithm(GraphDirected dg) {
		if (dg == null)
			throw new IllegalArgumentException("Graph cannot be null");
		bellmanFord = new BellmanFordAlgorithm(dg);
		verticesIDs = dg.getVerticesIDs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see utils.graph.shortestpath.IFinderAllPairsShortestPath#
	 * getShortestPathsCostsFromAllSources()
	 */
	@Override
	public Map<Integer, Map<Integer, Double>> getShortestPathsCostsFromAllSources() {
		Map<Integer, Map<Integer, Double>> res = new HashMap<Integer, Map<Integer, Double>>(
				verticesIDs.size());
		int count = 0;
		try {
			for (Integer vid : verticesIDs) {
				count++;
				bellmanFord.computeShortestPathsFrom(vid);
				Map<Integer, Double> spCosts = new HashMap<Integer, Double>(
						verticesIDs.size());
				for (Integer uid : verticesIDs) {
					spCosts.put(uid, bellmanFord.getShortestPathCostTo(uid));
				}
				res.put(vid, spCosts);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Detected negative cost cycle after processing "
					+ count + " vertices");
			return null;
		}

		return res;
	}

}
