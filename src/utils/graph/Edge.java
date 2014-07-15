package utils.graph;

/**
 * Simple implementation of graph edge. It can work for both directed and
 * undirected graphs.
 * 
 * @author Samer Meggaly
 *
 */
public class Edge {

	/**
	 * Indicates whether the edge is part of a directed graph or not. If this
	 * property is <code>true</code>, then edge property {@link #uID} represents
	 * the "from" (Tail) vertex of the edge and the edge property {@link #vID}
	 * represents the "to" (Head) vertex of the edge.
	 */
	public boolean directed;

	/**
	 * The <i>unique</i> identifying ID of the vertex.
	 */
	public int uID;

	/**
	 * The <i>unique</i> identifying ID of the vertex.
	 */
	public int vID;

	/**
	 * Creates a new edge which will be undirected.
	 * 
	 * @param u
	 *            First vertex of the edge.
	 * @param v
	 *            Second vertex of the edge.
	 */
	public Edge(int u, int v) {
		this(u, v, false);
	}

	public Edge(int u, int v, boolean directed) {
		this.directed = directed;
		uID = u;
		vID = v;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		
		if (!(o instanceof Edge))
			return false;
		
		if (this == o)
			return true;
		
		Edge other = (Edge) o;
		return Math.min(this.uID, this.vID) == Math.min(other.uID, other.vID)
				&& Math.max(this.uID, this.vID) == Math.max(other.uID,
						other.vID);
	}
	
	@Override
	public int hashCode() {
		// quick recipe from Effective Java eBook.
		int result = 17;
		if (directed) {
			result = 31 * result + uID;
			result = 31 * result + vID;
		} else {
			result = 31 * result + (uID ^ vID);
		}
		return result;
	}

	@Override
	public String toString() {
		if (directed)
			return "(" + uID + "," + vID + ")";
		else
			return "{" + uID + "," + vID + "}";
	}

	/**
	 * Given a one of the vertices of the edge, return the other one.
	 * 
	 * @param uv
	 *            Known vertex on the edge.
	 * @return The other vertex on the edge.
	 * @throws IllegalArgumentException
	 *             If the given vertex does not belong to the edge.
	 */
	public int getOtherVertex(int uv) {
		if (uID == uv)
			return vID;
		if (vID == uv)
			return uID;
		throw new IllegalArgumentException("Given ID: <" + uv
				+ "> does not equal any of the vertices of the edge: <" + this
				+ ">");
	}
	
	public boolean isVertexOnEdge(int vertex) {
		return vertex == uID || vertex == vID;
	}

}
