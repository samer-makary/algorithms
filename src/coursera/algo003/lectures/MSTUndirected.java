package coursera.algo003.lectures;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import utils.graph.Edge;
import utils.graph.GraphUndirected;
import utils.graph.Vertex;
import utils.graph.WeightedEdge;

public class MSTUndirected extends GraphUndirected {

	public MSTUndirected() {
		super();
	}

	// check is NOT complete
	public boolean isSpanningTreeOfG(GraphUndirected G) {
		Set<Set<Integer>> treeSCCs = new HashSet<Set<Integer>>(
				this.getConnectedComponents());
		Set<Set<Integer>> graphSCCs = new HashSet<Set<Integer>>(
				G.getConnectedComponents());

		int numTreeVerts = getNumOfVertices();
		int numTreeEdges = getNumOfEdges();

		// need to re-visit this condition, check corner cases
		// TODO: need to add a check that the tree is acyclic
		return numTreeEdges < numTreeVerts && treeSCCs.equals(graphSCCs);
	}
	
	public Set<WeightedEdge> getTreeEdges() {
		Collection<Vertex> verts = vertices.values();
		// tree is supposed to have at most |V|-1 edges
		Set<WeightedEdge> edges = new HashSet<WeightedEdge>(verts.size()); 
		for (Vertex v : verts) {
			for (Edge e : v.getAllAdjEdges()) {
				edges.add((WeightedEdge) e);
			}
		}
		
		return edges;
	}
	
	public double getTreeWeight() {
		Set<WeightedEdge> edges = getTreeEdges();
		double weight = 0.0;
		for (WeightedEdge we : edges) {
			weight += we.weight;
		}
		
		return weight;
	}
}
