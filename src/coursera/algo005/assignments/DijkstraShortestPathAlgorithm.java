package coursera.algo005.assignments;

import graph.Edge;
import graph.GraphUndirected;
import graph.Vertex;
import graph.WeightedEdge;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class DijkstraShortestPathAlgorithm {

	public static final double _PATH_MAX_WEIGHT = 1000000.0;
	
	private GraphUndirected ugraph;
	
	public DijkstraShortestPathAlgorithm(GraphUndirected g) {
		if (g == null)
			throw new IllegalArgumentException("Graph g cannot be null");
		this.ugraph = g;		
	}
	
	/**
	 * Runs Dijkstra's Shortest-path algorithm on graph given a source vertex.
	 * 
	 * @param s
	 *            Source vertex for all shortest-paths returned by the
	 *            algorithm.
	 * @return A mapping from every vertex-id in the given graph to
	 *         {@link DijkstraVertex} object that carries the output of the
	 *         algorithm.
	 */
	public Map<Integer, DijkstraVertex> runOn(int s) {
		Queue<DijkstraVertex> queue = new PriorityQueue<DijkstraVertex>(
				ugraph.getNumOfVertices());
		Map<Integer, DijkstraVertex> res = init(s);
		
		try {
			DijkstraVertex sdv = new DijkstraVertex();
			sdv.vID = s;
			sdv.pathCost = 0;
			queue.add(sdv);
			
			while (!queue.isEmpty()) {
				DijkstraVertex currDV = queue.poll();			
				Vertex currV = ugraph.getVertexByID(currDV.vID);
				
				for (Edge e : currV.getAllAdjEdges()) {
					WeightedEdge we = (WeightedEdge) e;
					DijkstraVertex dv = new DijkstraVertex();
					dv.pathPredecessor = currV.getVertexID();
					dv.vID = we.getOtherVertex(dv.pathPredecessor);
					dv.pathCost = we.weight + res.get(dv.pathPredecessor).pathCost;
					
					if (dv.pathCost < res.get(dv.vID).pathCost) {
						// remove the previous cost to vertex vID
						queue.remove(dv);
						// add the new cost to vertex vID
						queue.add(dv);
						res.put(dv.vID, dv);
					}
				}
			}
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(
					"Graph edges needs to be of type "
							+ WeightedEdge.class.getSimpleName(), e);
		}
		
		return res;
	}

	private Map<Integer, DijkstraVertex> init(int s) {
		Map<Integer, DijkstraVertex> vdvMap = new HashMap<Integer, DijkstraVertex>(
				ugraph.getNumOfVertices());
		
		for (int vID : ugraph.getVerticesIDs()) {
			DijkstraVertex dv = new DijkstraVertex();
			dv.vID = vID;
			if (vID == s) {
				dv.pathCost = 0; 
			}
			vdvMap.put(dv.vID, dv);
		}
		
		return vdvMap;
	}

	/**
	 * An class which holds the information needed for a given vertex,
	 * represented by its ID, to indicate the cost of the shortest-path found
	 * from source vertex to this node and also to indicate the predecessor of
	 * that vertex on that shortest-path, if any was found.
	 * This class represents the output of {@link DijkstraShortestPathAlgorithm}.
	 * 
	 * @author samer
	 * 
	 */
	public class DijkstraVertex implements Comparable<DijkstraVertex> {

		public int vID;
		public int pathPredecessor = -1;
		public double pathCost = _PATH_MAX_WEIGHT;

		@Override
		public int compareTo(DijkstraVertex o) {
			return Double.compare(pathPredecessor, o.pathPredecessor);
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

	}

	public static void main(String[] args) {
		int s = 1;
		int n = 200;
		String filePath = "./data/DijkstraSP/dijkstraData.txt";
		System.out.println("Reading graph from file");
		GraphUndirected graph = readGraphFromFile(filePath, n);
//		System.out.println(graph);
		
		DijkstraShortestPathAlgorithm algo = new DijkstraShortestPathAlgorithm(graph);
		Map<Integer, DijkstraVertex> res = algo.runOn(s);
		
		int[] query = new int[] { 7, 37, 59, 82, 99, 115, 133, 165, 188, 197 };
		for (int q : query) {
			System.out.print((int) res.get(q).pathCost + ",");
		}		 
	}

	public static GraphUndirected readGraphFromFile(String filePath,
			int numOfVertices) {
		try {
			Scanner scanner = new Scanner(new BufferedInputStream(
					new FileInputStream(new File(filePath))));

			GraphUndirected graph = new GraphUndirected(numOfVertices);
			while (scanner.hasNextLine()) {
				String[] lineParts = scanner.nextLine().split("\\s+");
				int u = Integer.parseInt(lineParts[0]);
				for (int i = 1; i < lineParts.length; i++) {
					String[] toWeight = lineParts[i].split(",");
					graph.addConnection(u, Integer.parseInt(toWeight[0]),
							Double.parseDouble(toWeight[1]));
				}
			}
			scanner.close();
			return graph;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
