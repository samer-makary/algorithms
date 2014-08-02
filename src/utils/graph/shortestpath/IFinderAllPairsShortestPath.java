package utils.graph.shortestpath;

import java.util.Map;

public interface IFinderAllPairsShortestPath {

	/**
	 * Computes the costs of all pairs shortest paths.
	 * 
	 * @return a map from every vertex ID as a source to a map containing the
	 *         all vertices IDs as destinations and double value representing
	 *         the cost of the SP to them.
	 */
	public Map<Integer, Map<Integer, Double>> getShortestPathsCostsFromAllSources();

}
