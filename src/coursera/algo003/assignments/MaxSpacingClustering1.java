package coursera.algo003.assignments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import utils.IntUnionFind;
import utils.graph.Edge;
import utils.graph.GraphUndirected;
import utils.graph.WeightedEdge;

public class MaxSpacingClustering1 {

	public static int maxSpacing1(GraphUndirected udg, int k) {
		if (k <= 1)
			throw new IllegalArgumentException("K must be > 1");

		List<Integer> vertices = udg.getVerticesIDs();
		IntUnionFind iuf = new IntUnionFind(new HashSet<Integer>(vertices));

		// create a list of edges sorted in ascending order of weight
		List<WeightedEdge> edges = new ArrayList<WeightedEdge>();
		for (Integer vid : vertices) {
			for (Edge e : udg.getVertexByID(vid).getAllAdjEdges())
				edges.add((WeightedEdge) e);
		}
		Collections.sort(edges, new Comparator<WeightedEdge>() {

			@Override
			public int compare(WeightedEdge arg0, WeightedEdge arg1) {
				return Double.compare(arg0.weight, arg1.weight);
			}
		});

		// create the clusters
		int eIdx = 0;
		while (iuf.countDistinctComponents() > k && eIdx < edges.size()) {
			WeightedEdge we = edges.get(eIdx);
			iuf.union(we.uID, we.vID);
			eIdx++;
		}

		System.out.println("Clusters are:");
		Map<Integer, Set<Integer>> clusters = iuf.getDistinctComponents();
		for (Integer i : clusters.keySet())
			System.out.println(i + " -> " + clusters.get(i));
		
		while (eIdx < edges.size()) {
			WeightedEdge we = edges.get(eIdx);
			if (iuf.find(we.uID) != iuf.find(we.vID))
				return (int) we.weight;
			else
				eIdx++;
		}

		throw new IllegalStateException("Failed to compute the spacing");
	}

	public static void main(String[] args) {
		int K = 4;
		GraphUndirected udg = readGraphFromClustering1File();
		System.out.println("Done reading graph from file");
		int spacing = maxSpacing1(udg, K);
		System.out.println("With " + K + "-clusters, spacing = " + spacing);
	}

	private static GraphUndirected readGraphFromClustering1File() {
		try {
			Scanner scanner = new Scanner(new BufferedInputStream(
					new FileInputStream(new File("./data/clustering1.txt"))));

			int n = Integer.parseInt(scanner.nextLine());
			GraphUndirected graph = new GraphUndirected(n);
			while (scanner.hasNextInt()) {
				String[] lineParts = scanner.nextLine().split("\\s+");
				int u = Integer.parseInt(lineParts[0]);
				int v = Integer.parseInt(lineParts[1]);
				double w = Double.parseDouble(lineParts[2]);
				graph.addConnection(u, v, w);
				graph.addConnection(v, u, w);
			}
			scanner.close();
			return graph;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

}
