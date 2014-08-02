package utils.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Vertex {

	private int vertexID;
	private int vertexOutDegree;
	private HashMap<Edge, Integer> edges;

	public Vertex(int id) {
		vertexID = id;
		vertexOutDegree = 0;
		edges = new HashMap<Edge, Integer>();
	}

	public int getVertexID() {
		return vertexID;
	}

	public int getOutDegree() {
		return this.vertexOutDegree;
	}

	public void addEdge(Edge e) {
		if (e.isVertexOnEdge(vertexID)) {
			int parallel = 0;
			if (edges.containsKey(e))
				parallel = edges.get(e);
			edges.put(e, parallel + 1);
			vertexOutDegree++;
		} else
			throw new IllegalArgumentException(
					"Cannot add edge to vertex that is equal to neither of its endpoints.");
	}

	public void updateEdgeWeight(WeightedEdge e, double newWeight) {
		Integer parallel;
		if ((parallel = edges.remove(e)) != null) {
			e.weight = newWeight;
			edges.put(e, parallel);
		} else {
			throw new IllegalArgumentException("Unknown vertex edge "
					+ e.toString());
		}
	}

	public List<Edge> getAllAdjEdges() {
		List<Edge> allEdges = new ArrayList<Edge>(vertexOutDegree);
		for (Edge e : edges.keySet()) {
			int parallel = edges.get(e);
			for (int i = 0; i < parallel; i++) {
				allEdges.add(e);
			}
		}
		return allEdges;
	}

	public Set<Edge> getDistinctAdjEdges() {
		return edges.keySet();
	}

	public Set<Integer> getDistinctAdjVertices() {
		Set<Integer> vertices = new HashSet<Integer>(edges.size());
		for (Edge e : edges.keySet()) {
			vertices.add(e.getOtherVertex(vertexID));
		}

		return vertices;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + vertexID + "): [ ");
		for (Edge e : getDistinctAdjEdges()) {
			sb.append("(" + e + ", " + edges.get(e) + ") - ");
		}
		sb.append("]");
		return sb.toString();
	}

	public boolean isAdjacentToVertex(int v) {
		return getDistinctAdjVertices().contains(v);
	}

	public void removeEdgeIfExists(Edge e) {
		if (edges.containsKey(e))
			edges.remove(e);
	}
}
