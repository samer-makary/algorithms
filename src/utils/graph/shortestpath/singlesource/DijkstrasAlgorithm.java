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
import utils.heap.Heap;
import utils.heap.HeapEntry;
import utils.heap.MinHeap;

public class DijkstrasAlgorithm implements IFinderSingleSourceShortestPath {

	private final GraphDirected dgraph;
	private Map<Integer, HeapEntry<DijkstraVertex>> optPaths;

	public DijkstrasAlgorithm(GraphDirected dg) {
		if (dg == null)
			throw new IllegalArgumentException("Graph cannot be null");
		this.dgraph = dg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * utils.graph.shortestpath.singlesource.IFinderSingleSourceShortestPath
	 * #computeShortestPathsFrom(int)
	 */
	@Override
	public void computeShortestPathsFrom(int s) throws IllegalArgumentException {
		// initialize shortest paths to all vertex starting from the given
		// source vertex s
		Heap<DijkstraVertex> minHeap = initForSource(s);

		while (!minHeap.isEmpty()) {
			DijkstraVertex currDV = minHeap.poll();
			Vertex currV = dgraph.getVertexByID(currDV.vID);
			optPaths.get(currDV.vID).getData().optimal = true;

			for (Edge e : currV.getAllAdjEdges()) {
				WeightedEdge we = (WeightedEdge) e;
				HeapEntry<DijkstraVertex> currDVAdjacentEntry = optPaths.get(we
						.getOtherVertex(currDV.vID));
				DijkstraVertex currDVAdjacent = currDVAdjacentEntry.getData();
				if (!currDVAdjacent.optimal) {
					double newCost = we.weight + currDV.pathCost;

					if (newCost < currDVAdjacent.pathCost) {
						DijkstraVertex newCurrDVAdjacent = new DijkstraVertex();
						newCurrDVAdjacent.vID = currDVAdjacent.vID;
						newCurrDVAdjacent.optimal = currDVAdjacent.optimal;
						newCurrDVAdjacent.pathPredecessor = currV.getVertexID();
						newCurrDVAdjacent.pathCost = newCost;
						HeapEntry<DijkstraVertex> newCurrDVAdjacentEntry = minHeap
								.updateEntryDataValue(currDVAdjacentEntry,
										newCurrDVAdjacent);
						optPaths.put(newCurrDVAdjacent.vID,
								newCurrDVAdjacentEntry);
					}
				}
			}
		}
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
			return optPaths.get(t).getData().pathCost;
		} else {
			throw new IllegalArgumentException("Unknown vertex (t) id " + t);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * utils.graph.shortestpath.singlesource.IFinderSingleSourceShortestPath
	 * #getShortestPathTo(int)
	 */
	@Override
	public List<Integer> getShortestPathTo(int t) {
		if (optPaths == null)
			throw new IllegalStateException("No paths computed yet");

		if (!optPaths.containsKey(t)) {
			throw new IllegalArgumentException("Unknown vertex (t) id " + t);
		} else {
			DijkstraVertex vpp = optPaths.get(t).getData();
			List<Integer> pathToT = new ArrayList<Integer>();
			if (vpp.pathPredecessor != -1) {
				pathToT = getShortestPathTo(vpp.pathPredecessor);
			}
			pathToT.add(t);
			return pathToT;
		}
	}

	private Heap<DijkstraVertex> initForSource(int s) {
		Heap<DijkstraVertex> heap = new MinHeap<DijkstraVertex>(
				dgraph.getNumOfVertices());
		optPaths = new HashMap<Integer, HeapEntry<DijkstraVertex>>(
				dgraph.getNumOfVertices());

		for (int vID : dgraph.getVerticesIDs()) {
			DijkstraVertex dv = new DijkstraVertex();
			dv.vID = vID;
			if (vID == s) {
				dv.pathCost = 0;
			}
			optPaths.put(dv.vID, heap.add(dv));
		}

		return heap;
	}

	/**
	 * An class which holds the information needed for a given vertex,
	 * represented by its ID, to indicate the cost of the shortest-path found
	 * from source vertex to this node and also to indicate the predecessor of
	 * that vertex on that shortest-path, if any was found. This class
	 * represents the output of {@link DijkstrasAlgorithm}.
	 * 
	 * @author samer
	 * 
	 */
	private class DijkstraVertex implements Comparable<DijkstraVertex> {

		public int vID;
		public int pathPredecessor = -1;
		public double pathCost = _INF_PATH_COST;
		public boolean optimal = false;

		@Override
		public int compareTo(DijkstraVertex o) {
			return Double.compare(pathCost, o.pathCost);
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;

			if (o instanceof DijkstraVertex) {
				if (this != o) {
					DijkstraVertex dvObj = (DijkstraVertex) o;
					return this == dvObj || this.vID == dvObj.vID;
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "[vID=" + vID + ", pathPredecessor=" + pathPredecessor
					+ ", pathCost=" + pathCost + ", optimal=" + optimal + "]";
		}

	}

}
