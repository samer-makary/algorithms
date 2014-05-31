package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GraphUndirected {

	private static int maxVertexID = 0;
	private final Random rand;
	private HashMap<Integer, Vertex> vertices;
	
	public GraphUndirected() {
		this(0);
	}
	
	public GraphUndirected(int numOfVertices) {
		this.vertices = new HashMap<Integer, Vertex>(numOfVertices);
		this.rand = new Random(System.currentTimeMillis() << 32);
	}
	
	public int getNumOfVertices() {
		return this.vertices.size();
	}
	
	public int getNumOfEdges() {
		int counter = 0;
		for (Vertex v : vertices.values()) {
			counter += v.getAllAdjEdges().size();
		}
		
		return counter;
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
		
//		Vertex vVert;
//		if (vertices.containsKey(v)) {
//			vVert = vertices.get(v);
//		} else {
//			vVert = new Vertex(v);
//			vertices.put(v, vVert);
//		}
//		vVert.addEdge(new Edge(v, u));
			
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
			throw new IllegalArgumentException("Contraction edge cannot be NULL");
		
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
	
}
