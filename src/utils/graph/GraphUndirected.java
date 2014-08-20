package utils.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class GraphUndirected {

	private static int maxVertexID = 0;
	private final Random rand;
	protected HashMap<Integer, Vertex> vertices;

	public GraphUndirected() {
		this(0);
	}

	public GraphUndirected(int numOfVertices) {
		this.vertices = new HashMap<Integer, Vertex>(numOfVertices);
		this.rand = new Random(System.currentTimeMillis() << 32);
	}

	public List<Integer> getVerticesIDs() {
		return new ArrayList<Integer>(vertices.keySet());
	}

	public int getNumOfVertices() {
		return this.vertices.size();
	}

	public Vertex getVertexByID(int id) {
		return vertices.get(id);
	}

	public int getNumOfEdges() {
		int counter = 0;
		for (Vertex v : vertices.values()) {
			counter += v.getAllAdjEdges().size();
		}

		return counter / 2;
	}

	public void addConnection(int u, int v) {
		maxVertexID = Math.max(maxVertexID, Math.max(u, v));

		Vertex uVert;
		if (vertices.containsKey(u)) {
			uVert = vertices.get(u);
		} else {
			uVert = new Vertex(u);
			vertices.put(u, uVert);
		}
		uVert.addEdge(new Edge(u, v));

		// add the v vertex in case it had out-degree of 0
		if (!vertices.containsKey(v))
			vertices.put(v, new Vertex(v));
	}

	public void addConnection(int u, int v, double w) {
		maxVertexID = Math.max(maxVertexID, Math.max(u, v));

		Vertex uVert;
		if (vertices.containsKey(u)) {
			uVert = vertices.get(u);
		} else {
			uVert = new Vertex(u);
			vertices.put(u, uVert);
		}
		uVert.addEdge(new WeightedEdge(u, v, w));

		// add the v vertex in case it had out-degree of 0
		if (!vertices.containsKey(v))
			vertices.put(v, new Vertex(v));
	}

	public Edge getRandomEdge() {
		List<Edge> allGraphEdges = new ArrayList<Edge>();
		for (Vertex v : vertices.values()) {
			allGraphEdges.addAll(v.getAllAdjEdges());
		}
		return allGraphEdges.get(rand.nextInt(allGraphEdges.size()));
	}

	public void contract(Edge e) {
		if (vertices.size() <= 1)
			throw new RuntimeException("Graph contains less than 2 vertices!");

		if (e == null)
			throw new IllegalArgumentException(
					"Contraction edge cannot be NULL");

		if (!vertices.containsKey(e.uID) || !vertices.containsKey(e.vID))
			throw new IllegalArgumentException("Both vertices of edge " + e
					+ " must belong to the graph");

		int newVertIdx = ++maxVertexID;
		Vertex uberVertex = new Vertex(newVertIdx);
		vertices.put(newVertIdx, uberVertex);
		Vertex uVert = vertices.remove(e.uID);
		Vertex vVert = vertices.remove(e.vID);
		uVert.removeEdgeIfExists(e);
		vVert.removeEdgeIfExists(e);
		replaceVertices(uberVertex, uVert);
		replaceVertices(uberVertex, vVert);
	}

	private void replaceVertices(Vertex newVertex, Vertex oldVertex) {
		List<Edge> oldVertEdges = oldVertex.getAllAdjEdges();
		int nID = newVertex.getVertexID();
		int oID = oldVertex.getVertexID();

		for (Edge e : oldVertEdges) {
			oldVertex.removeEdgeIfExists(e);
			Vertex adjToOld = vertices.get(e.getOtherVertex(oID));
			adjToOld.removeEdgeIfExists(e);
			adjToOld.addEdge(new Edge(adjToOld.getVertexID(), nID));
			newVertex.addEdge(new Edge(nID, adjToOld.getVertexID()));
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of Vertices: " + vertices.size() + "\n");
		for (Vertex v : vertices.values()) {
			sb.append("    ");
			sb.append(v);
			sb.append("\n");
		}

		return sb.toString();
	}

	public List<Set<Integer>> getConnectedComponents() {
		Set<Integer> visited = new HashSet<Integer>(vertices.size());
		Set<Integer> vIDs = vertices.keySet();
		List<Set<Integer>> sccs = new ArrayList<Set<Integer>>();
		for (Integer vID : vIDs) {
			if (!visited.contains(vID)) {
				// simulate DFS starting from vID node
				Set<Integer> scc = DFS(vID);
				int sizeBeforeNewSCC = visited.size();
				visited.addAll(scc);
				if (visited.size() != scc.size() + sizeBeforeNewSCC)
					throw new IllegalStateException(
							"Duplicate vertices found in 2 different SCC");
				sccs.add(scc);
			}
		}

		return sccs;
	}

	public Set<Integer> DFS(int vID) {
		if (!vertices.containsKey(vID))
			throw new IllegalArgumentException(
					"DFS must start from a vertex that belongs to the graph");

		Set<Integer> sccVerts = new HashSet<Integer>();
		sccVerts.add(vID);
		Stack<Integer> stack = new Stack<Integer>();
		stack.add(vID);

		while (!stack.isEmpty()) {
			int currVertexID = stack.pop();
			Vertex currVertex = vertices.get(currVertexID);
			for (Integer adjVertexID : currVertex.getDistinctAdjVertices()) {
				if (!sccVerts.contains(adjVertexID)) {
					sccVerts.add(adjVertexID);
					stack.add(adjVertexID);
				}
			}
		}
		
		return sccVerts;
	}
}
