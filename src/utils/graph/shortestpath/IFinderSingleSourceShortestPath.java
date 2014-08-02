package utils.graph.shortestpath;

import java.util.List;

public interface IFinderSingleSourceShortestPath {
	
	public static final double _INF_PATH_COST = Double.MAX_VALUE;

	/**
	 * Computes the shortest paths from the given source to all the destination
	 * vertices in the graph.
	 * 
	 * @param s
	 *            ID of the vertex to be used a source to all SPs.
	 * @throws IllegalArgumentException
	 *             if a negative cost cycle was detected in the given graph.
	 */
	public void computeShortestPathsFrom(int s) throws IllegalArgumentException;

	/**
	 * Get the cost of the shortest path to the destination t from the given
	 * source.
	 * 
	 * @param t
	 *            destination vertex to which the SP cost will be returned.
	 * @return cost of the SP to the given destination vertex.
	 */
	public double getShortestPathCostTo(int t);

	/**
	 * Construct a path of hops through vertices from the sources vertex to the
	 * destination vertex t.
	 * 
	 * @param t
	 *            destination vertex to which the SP from the source will be
	 *            constructed, if any.
	 * @return order of the vertices to be traversed from the source to the
	 *         destination t. If no such path exists then the returned list will
	 *         contain only the destination t.
	 */
	public List<Integer> getShortestPathTo(int t);
}
