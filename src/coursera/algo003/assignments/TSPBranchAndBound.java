package coursera.algo003.assignments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import utils.IntUnionFind;
import utils.graph.Edge;
import utils.graph.GraphUndirected;
import utils.graph.Vertex;
import utils.graph.WeightedEdge;

public class TSPBranchAndBound {

	private GraphUndirected graph;
	private WeightedEdge[] edges;
	private Map<Integer, BitSet> vertexEdges;
	private BNBNode bestTourNode;

	public TSPBranchAndBound(GraphUndirected graph) {
		if (graph == null) {
			throw new IllegalArgumentException("Graph cannot be NULL");
		}

		this.graph = graph;
		initEdges();
		initVerticesEdges();
	}

	private void initEdges() {
		int numEdges = graph.getNumOfEdges();
		Set<WeightedEdge> distinctEdges = new HashSet<WeightedEdge>(
				2 * numEdges);
		for (Integer vid : graph.getVerticesIDs()) {
			Vertex v = graph.getVertexByID(vid);
			for (Edge e : v.getAllAdjEdges()) {
				distinctEdges.add((WeightedEdge) e);
			}
		}

		edges = new WeightedEdge[numEdges];
		int i = 0;
		for (WeightedEdge we : distinctEdges) {
			edges[i++] = we;
		}

		Arrays.sort(edges, new Comparator<WeightedEdge>() {

			@Override
			public int compare(WeightedEdge o1, WeightedEdge o2) {
				return Double.compare(o1.weight, o2.weight);
			}
		});
	}

	private void initVerticesEdges() {
		vertexEdges = new HashMap<Integer, BitSet>(graph.getNumOfVertices());
		for (Integer vid : graph.getVerticesIDs())
			vertexEdges.put(vid, new BitSet(graph.getNumOfEdges()));

		for (int i = 0; i < edges.length; i++) {
			vertexEdges.get(edges[i].uID).set(i);
			vertexEdges.get(edges[i].vID).set(i);
		}
	}

	public double solve() {
		BNBNode initNode = new BNBNode();
		Stack<BNBNode> stack = new Stack<BNBNode>();
		stack.add(initNode);
		bestTourNode = new BNBNode(-1, Double.MAX_VALUE, null, null);
		while (!stack.isEmpty()) {
			BNBNode currNode = stack.pop();
			if (currNode.isCompleteTour()
					&& Double.compare(currNode.tourCost, bestTourNode.tourCost) < 0) {
				bestTourNode = (BNBNode) currNode.clone();
			} else if (currNode.getNumEdgesIncluded() < graph
					.getNumOfVertices()
					&& currNode.lastCheckedEdge + 1 < edges.length) {
				int nextEdgeIdx = currNode.lastCheckedEdge + 1;
				if (currNode.canIncludeEdge(nextEdgeIdx)) {
					BNBNode withNextEdge = (BNBNode) currNode.clone();
					withNextEdge.includedEdges.set(nextEdgeIdx);
					withNextEdge.tourCost += edges[nextEdgeIdx].weight;
					withNextEdge.lastCheckedEdge++;
					if (withNextEdge.getCompleteTourCostLowerBound() < bestTourNode.tourCost)
						stack.push(withNextEdge);
				}
				BNBNode withoutNextEdge = (BNBNode) currNode.clone();
				withoutNextEdge.excludedEdges.set(nextEdgeIdx);
				withoutNextEdge.lastCheckedEdge++;
				if (withoutNextEdge.getCompleteTourCostLowerBound() < bestTourNode.tourCost)
					stack.push(withoutNextEdge);
			}
		}

		return bestTourNode.tourCost;
	}

	public List<WeightedEdge> getOptimalTourEdges() {
		List<WeightedEdge> tourEdges = new ArrayList<WeightedEdge>(edges.length);
		BitSet ies = bestTourNode.includedEdges;
		if (ies == null)
			return tourEdges;
		for (int eIdx = ies.nextSetBit(0); eIdx >= 0; eIdx = ies
				.nextSetBit(eIdx + 1)) {
			tourEdges.add(edges[eIdx]);
		}
		return tourEdges;
	}

	private class BNBNode {
		int lastCheckedEdge;
		BitSet includedEdges;
		BitSet excludedEdges;
		double tourCost;

		public BNBNode() {
			this(-1, 0.0, new BitSet(edges.length), new BitSet(edges.length));
		}

		public BNBNode(int lastCheckedEdge, double tourCost,
				BitSet includedEdges, BitSet excludedEdges) {
			this.lastCheckedEdge = lastCheckedEdge;
			this.includedEdges = includedEdges;
			this.excludedEdges = excludedEdges;
			this.tourCost = tourCost;
		}

		public int getNumEdgesIncluded() {
			return includedEdges.cardinality();
		}

		public boolean isCompleteTour() {
			if (includedEdges.cardinality() == graph.getNumOfVertices()) {
				Map<Integer, Integer> vertexVisits = getCountOfTourVisitsToVertex();
				boolean isNotComplete = false;
				for (Integer vid : graph.getVerticesIDs()) {
					isNotComplete = vertexVisits.get(vid) != 2;
					if (isNotComplete) {
						break;
					}
				}

				return !isNotComplete;
			}
			return false;
		}

		public boolean canIncludeEdge(int edgeIdx) {
			Map<Integer, Integer> vertexVisits = getCountOfTourVisitsToVertex();
			if (vertexVisits.get(edges[edgeIdx].uID) < 2
					&& vertexVisits.get(edges[edgeIdx].vID) < 2) {
				IntUnionFind set = new IntUnionFind(new HashSet<Integer>(
						graph.getVerticesIDs()));
				for (int eIdx = includedEdges.nextSetBit(0); eIdx >= 0; eIdx = includedEdges
						.nextSetBit(eIdx + 1)) {
					set.union(edges[eIdx].uID, edges[eIdx].vID);
				}

				includedEdges.set(edgeIdx);
				boolean isComplete = isCompleteTour();
				includedEdges.clear(edgeIdx);
				return isComplete
						|| set.find(edges[edgeIdx].uID) != set
								.find(edges[edgeIdx].vID);
			}

			return false;
		}

		public Map<Integer, Integer> getCountOfTourVisitsToVertex() {
			Map<Integer, Integer> visitsPerVertex = new HashMap<Integer, Integer>(
					graph.getNumOfVertices());
			for (Integer vid : graph.getVerticesIDs())
				visitsPerVertex.put(vid, 0);

			for (int eIdx = includedEdges.nextSetBit(0); eIdx >= 0; eIdx = includedEdges
					.nextSetBit(eIdx + 1)) {
				WeightedEdge we = edges[eIdx];
				visitsPerVertex.put(we.uID, visitsPerVertex.get(we.uID) + 1);
				visitsPerVertex.put(we.vID, visitsPerVertex.get(we.vID) + 1);
			}
			return visitsPerVertex;
		}

		public double getCompleteTourCostLowerBound() {
			int countTourEdges = 0;
			int eIdx = 0;
			double twiceCostLowerBound = 0.0;
			Map<Integer, Integer> vertexVisits = getCountOfTourVisitsToVertex();
			while (countTourEdges != (graph.getNumOfVertices() * 2)
					&& eIdx < edges.length) {
				if (includedEdges.get(eIdx)) {
					countTourEdges += 2;
					twiceCostLowerBound += 2 * edges[eIdx].weight;
				} else if (!excludedEdges.get(eIdx)) {
					if (vertexVisits.get(edges[eIdx].uID) < 2) {
						vertexVisits.put(edges[eIdx].uID,
								vertexVisits.get(edges[eIdx].uID) + 1);
						twiceCostLowerBound += edges[eIdx].weight;
						countTourEdges++;
					}
					if (vertexVisits.get(edges[eIdx].vID) < 2) {
						vertexVisits.put(edges[eIdx].vID,
								vertexVisits.get(edges[eIdx].vID) + 1);
						twiceCostLowerBound += edges[eIdx].weight;
						countTourEdges++;
					}
				}
				eIdx++;
			}

			if (countTourEdges != (graph.getNumOfVertices() * 2))
				// node constraints won't lead to complete TSP tour at all.
				return Double.MAX_VALUE;
			else
				return twiceCostLowerBound / 2;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + lastCheckedEdge;
			result = prime * result
					+ ((includedEdges == null) ? 0 : includedEdges.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BNBNode other = (BNBNode) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (lastCheckedEdge != other.lastCheckedEdge)
				return false;
			if (includedEdges == null) {
				if (other.includedEdges != null)
					return false;
			} else if (!includedEdges.equals(other.includedEdges))
				return false;
			return true;
		}

		private TSPBranchAndBound getOuterType() {
			return TSPBranchAndBound.this;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "BNBNode [lastCheckedEdge=" + lastCheckedEdge
					+ ", includedEdges=" + includedEdges + ", excludedEdges="
					+ excludedEdges + ", tourCost=" + tourCost + "]";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		protected Object clone() {
			return new BNBNode(lastCheckedEdge, tourCost,
					(BitSet) includedEdges.clone(),
					(BitSet) excludedEdges.clone());
		}

	}

	public static void main(String[] args) throws IOException {
		int[][] tinyGraph = new int[][] { new int[] { 0, 1, 3 },
				new int[] { 0, 2, 4 }, new int[] { 0, 3, 2 },
				new int[] { 0, 4, 7 }, new int[] { 1, 2, 4 },
				new int[] { 1, 3, 6 }, new int[] { 1, 4, 3 },
				new int[] { 2, 3, 5 }, new int[] { 2, 4, 8 },
				new int[] { 3, 4, 6 } };

		GraphUndirected graph = new GraphUndirected();
		for (int[] edge : tinyGraph) {
			graph.addConnection(edge[0], edge[1], edge[2]);
			graph.addConnection(edge[1], edge[0], edge[2]);
		}

		TSPBranchAndBound solver = new TSPBranchAndBound(graph);
		System.out.println(solver.solve());
		System.out.println(solver.getOptimalTourEdges());

		graph = readPointsIntoGraph("./data/tsp.txt");
		solver = new TSPBranchAndBound(graph);
		long start = System.currentTimeMillis();
		System.out.println(solver.solve());
		System.out.println(solver.getOptimalTourEdges());
		System.out.println("Finished in " + ((System.currentTimeMillis() - start) / (60 * 1000)));

	}

	private static GraphUndirected readPointsIntoGraph(String fileName)
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		int N = scanner.nextInt();
		double[][] points = new double[N][2];
		for (int i = 0; i < N; i++) {
			points[i][0] = scanner.nextDouble();
			points[i][1] = scanner.nextDouble();
		}
		scanner.close();

		GraphUndirected graph = new GraphUndirected(N);
		for (int p1Idx = 0; p1Idx < N; p1Idx++) {
			double[] p1 = points[p1Idx];
			for (int p2Idx = 0; p2Idx < N; p2Idx++) {
				if (p1Idx != p2Idx) {
					double[] p2 = points[p2Idx];
					double weight = Math.sqrt(Math.pow(p1[0] - p2[0], 2)
							+ Math.pow(p1[1] - p2[1], 2));
					graph.addConnection(p1Idx, p2Idx, weight);
				}
			}
		}
		return graph;
	}
}
