package coursera.algo003.assignments;

import graph.GraphUndirected;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import coursera.algo003.lectures.MSTUndirected;
import coursera.algo003.lectures.PrimAlgorithmForMST;

public class PrimMST {

	public static void main(String[] args) {
		// edges.txt has |V| = 500 and |E| = 2184
		String filePath = "./data/MST/edges.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._HEAP_WITH_O_LOGN_UPDATE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		System.out.println("Tree cost for file: " + filePath + " = "
				+ mst.getTreeWeight());
		
		/*
		List<WeightedEdge> edges = new ArrayList<>();
		for (WeightedEdge e : mst.getTreeEdges()) {
			edges.add(e);
		}
		
		Collections.sort(edges, new Comparator<WeightedEdge>() {

			@Override
			public int compare(WeightedEdge o1, WeightedEdge o2) {
				int o11 = o1.uID;
				int o12 = o1.vID;
				int o1Min = Math.min(o11, o12);
				int o21 = o2.uID;
				int o22 = o2.vID;
				int o2Min = Math.min(o21, o22);
				if (o1Min == o2Min) {
					return Math.max(o11, o12) - Math.max(o21, o22); 
				} else {
					return o1Min - o2Min;
				}
			}
		});
		
		for (WeightedEdge e : edges)
			System.out.println(e);
		System.out.println(mst.getNumOfVertices());
//		List<Edge> edged434 = mst.getVertexByID(434).getAllAdjEdges();
//		for (Edge e : edged434)
//			System.out.println(e);
		*/
	}

	private static GraphUndirected readGraph(String filePath) {
		try {
			Scanner scanner = new Scanner(new BufferedInputStream(
					new FileInputStream(new File(filePath))));

			GraphUndirected graph = new GraphUndirected();
			while (scanner.hasNextLine()) {
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
