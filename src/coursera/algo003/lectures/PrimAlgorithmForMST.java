package coursera.algo003.lectures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import utils.graph.Edge;
import utils.graph.GraphUndirected;
import utils.graph.Vertex;
import utils.graph.WeightedEdge;
import utils.heap.Heap;
import utils.heap.HeapEntry;
import utils.heap.MinHeap;

/**
 * Implementation of Prim's MST algorithm using both java {@link PriorityQueue}
 * and using my own {@link Heap} data structure which enable key updates in
 * O(LogN) time complexity.
 * 
 * @author samer
 * 
 */
public class PrimAlgorithmForMST {

	public static final int _NAIVE = 0;
	public static final int _HEAP_WITH_O_N_UPDATE = 1;
	public static final int _HEAP_WITH_O_LOGN_UPDATE = 2;
	public static int heapType = _HEAP_WITH_O_LOGN_UPDATE;

	private GraphUndirected udg;

	public PrimAlgorithmForMST(GraphUndirected udg) {
		if (udg == null)
			throw new IllegalArgumentException("Graph instance cannot be null");
		this.udg = udg;
	}

	public MSTUndirected getMST() {
		// get the SCCs of the graph in case it was NOT connected
		List<Set<Integer>> graphSCCs = udg.getConnectedComponents();
		MSTUndirected msts = new MSTUndirected();

		// create MST for every SCC
		for (Set<Integer> scc : graphSCCs) {
			switch (heapType) {
			case _NAIVE:
				useNaive(msts, scc);
				break;

			case _HEAP_WITH_O_N_UPDATE:
				useO_N_Heap(msts, scc);
				break;

			case _HEAP_WITH_O_LOGN_UPDATE:
				useO_LogN_Heap(msts, scc);
				break;
			}
		}

		return msts;
	}

	private void useNaive(MSTUndirected msts, Set<Integer> scc) {
		Set<Integer> spanned = new HashSet<Integer>(scc.size());
		spanned.add(scc.iterator().next());
		while (spanned.size() < scc.size()) {
			double minCutEdgeWeight = Double.MAX_VALUE;
			Edge minCutEdge = null;
			for (Integer v : spanned) {
				List<Edge> edges = udg.getVertexByID(v).getAllAdjEdges();
				for (Edge e : edges) {
					WeightedEdge we = (WeightedEdge) e;
					if (!spanned.contains(we.getOtherVertex(v))) {
						if (Double.compare(we.weight, minCutEdgeWeight) < 0) {
							minCutEdgeWeight = we.weight;
							minCutEdge = we;
						}
					}
				}
			}
			// only one of the following is new to the set
			spanned.add(minCutEdge.uID);
			spanned.add(minCutEdge.vID);
			msts.addConnection(minCutEdge.uID, minCutEdge.vID, minCutEdgeWeight);
		}
	}

	private void useO_N_Heap(MSTUndirected msts, Set<Integer> scc) {
		Map<Integer, Double> vIDToKey = new HashMap<Integer, Double>(scc.size());
		Queue<PrimAlgorithmVertex> minHeap = new PriorityQueue<PrimAlgorithmVertex>(
				scc.size());

		// add all vertices to the min heap
		for (Integer vID : scc) {
			PrimAlgorithmVertex pav = new PrimAlgorithmVertex();
			pav.vertexID = vID;
			minHeap.add(pav);
			vIDToKey.put(vID, Double.MAX_VALUE);
		}

		while (!minHeap.isEmpty()) {
			PrimAlgorithmVertex currPrimVertex = minHeap.poll();
			Vertex currVertex = udg.getVertexByID(currPrimVertex.vertexID);

			// vertex is now spanned, remove it from unvisited map
			vIDToKey.remove(currPrimVertex.vertexID);

			// only case in which the following check will fail
			// is for the very first vertex only.
			if (currPrimVertex.vertexEdge != null) {
				msts.addConnection(currPrimVertex.vertexID,
						currPrimVertex.vertexEdge
								.getOtherVertex(currPrimVertex.vertexID),
						currPrimVertex.vertexEdge.weight);
			}

			for (Edge e : currVertex.getAllAdjEdges()) {
				WeightedEdge we = (WeightedEdge) e;
				int otherVert = we.getOtherVertex(currVertex.getVertexID());

				// if other vertex hasn't been visited before
				// or it's been visited but with higher edge-cost,
				// then remove it from Heap and insert it with new cost.
				if (vIDToKey.containsKey(otherVert)) {
					if (vIDToKey.get(otherVert) > we.weight) {
						vIDToKey.put(otherVert, we.weight);
						PrimAlgorithmVertex pav = new PrimAlgorithmVertex();
						pav.vertexID = e.getOtherVertex(currVertex
								.getVertexID());
						if (!minHeap.remove(pav))
							throw new IllegalStateException("A7A"
									+ minHeap.contains(pav));
						pav.vertexEdge = we;
						minHeap.add(pav);
					}
				}
			}
		}
	}

	private void useO_LogN_Heap(MSTUndirected msts, Set<Integer> scc) {
		Map<Integer, HeapEntry<PrimAlgorithmVertex>> vIDToKey = null;
		vIDToKey = new HashMap<Integer, HeapEntry<PrimAlgorithmVertex>>(
				scc.size());
		Heap<PrimAlgorithmVertex> minHeap = new MinHeap<PrimAlgorithmVertex>(
				scc.size());

		// add all vertices to the min heap
		for (Integer vID : scc) {
			PrimAlgorithmVertex pav = new PrimAlgorithmVertex();
			pav.vertexID = vID;
			vIDToKey.put(vID, minHeap.add(pav));
		}

		while (!minHeap.isEmpty()) {
			PrimAlgorithmVertex currPrimVertex = minHeap.poll();
			Vertex currVertex = udg.getVertexByID(currPrimVertex.vertexID);

			// remove the heap entry object of the vertex from the map
			// as it's been now spanned by the tree
			vIDToKey.remove(currPrimVertex.vertexID);

			// only case in which the following check will fail
			// is for the very first vertex only.
			if (currPrimVertex.vertexEdge != null) {
				msts.addConnection(currPrimVertex.vertexID,
						currPrimVertex.vertexEdge
								.getOtherVertex(currPrimVertex.vertexID),
						currPrimVertex.vertexEdge.weight);
			}

			for (Edge e : currVertex.getAllAdjEdges()) {
				WeightedEdge we = (WeightedEdge) e;
				int otherVert = we.getOtherVertex(currVertex.getVertexID());

				// if other vertex hasn't been visited before
				// or it's been visited but with higher edge-cost,
				// then its ref in the min-heap with new cost.
				HeapEntry<PrimAlgorithmVertex> vhe = vIDToKey.get(otherVert);
				if (vhe != null
						&& (vhe.getData().vertexEdge == null || vhe.getData().vertexEdge.weight > we.weight)) {
					PrimAlgorithmVertex pav = new PrimAlgorithmVertex();
					pav.vertexID = vhe.getData().vertexID;
					pav.vertexEdge = we;
					vhe = minHeap.updateEntryDataValue(vhe, pav);
					vIDToKey.put(otherVert, vhe);
				}
			}
		}
	}

	private class PrimAlgorithmVertex implements
			Comparable<PrimAlgorithmVertex> {
		int vertexID;
		WeightedEdge vertexEdge;

		@Override
		public int compareTo(PrimAlgorithmVertex o) {
			if (o.vertexEdge == null)
				return -1;
			else if (this.vertexEdge == null)
				return 1;
			return Double.compare(vertexEdge.weight, o.vertexEdge.weight);
		}

		@Override
		public boolean equals(Object other) {
			if (other == null)
				return false;
			else if (!(other instanceof PrimAlgorithmVertex))
				return false;
			else {
				PrimAlgorithmVertex o = (PrimAlgorithmVertex) other;
				return this.vertexID == o.vertexID;
			}
		}

		@Override
		public String toString() {
			return vertexID + "_"
					+ ((vertexEdge == null) ? "?" : vertexEdge.weight);
		}

	}
}
