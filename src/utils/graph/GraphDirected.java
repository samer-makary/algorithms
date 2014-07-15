package utils.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class GraphDirected {

	private HashMap<Integer, Vertex> vertices;

	public GraphDirected() {
		this(0);
	}

	public GraphDirected(int numOfVertices) {
		this.vertices = new HashMap<Integer, Vertex>(numOfVertices);
	}

	public int getNumOfVertices() {
		return this.vertices.size();
	}

	public List<Integer> getVerticesIDs() {
		return new ArrayList<Integer>(vertices.keySet());
	}

	public Vertex getVertexByID(int id) {
		return vertices.get(id);
	}

	public int getNumOfEdges() {
		int counter = 0;
		for (Vertex v : vertices.values()) {
			counter += v.getAllAdjEdges().size();
		}

		return counter;
	}

	public void addConnection(int fromVertex, int toVertex) {

		Vertex uVert;
		if (vertices.containsKey(fromVertex)) {
			uVert = vertices.get(fromVertex);
		} else {
			uVert = new Vertex(fromVertex);
			vertices.put(fromVertex, uVert);
		}
		uVert.addEdge(new Edge(fromVertex, toVertex, true));

		if (!vertices.containsKey(toVertex)) {
			vertices.put(toVertex, new Vertex(toVertex));
		}
	}

	public void addConnection(int fromVertex, int toVertex, double weight) {

		Vertex uVert;
		if (vertices.containsKey(fromVertex)) {
			uVert = vertices.get(fromVertex);
		} else {
			uVert = new Vertex(fromVertex);
			vertices.put(fromVertex, uVert);
		}
		uVert.addEdge(new WeightedEdge(fromVertex, toVertex, weight, true));

		if (!vertices.containsKey(toVertex)) {
			vertices.put(toVertex, new Vertex(toVertex));
		}
	}

	public GraphDirected getReversed() {
		GraphDirected gRev = new GraphDirected(getNumOfVertices());
		for (Entry<Integer, Vertex> e : vertices.entrySet()) {
			Vertex u = e.getValue();
			for (Edge edg : u.getAllAdjEdges()) {
				gRev.addConnection(edg.getOtherVertex(u.getVertexID()),
						u.getVertexID());
			}
		}

		return gRev;
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
