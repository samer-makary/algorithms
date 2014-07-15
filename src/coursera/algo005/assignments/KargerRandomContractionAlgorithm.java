package coursera.algo005.assignments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import utils.graph.Edge;
import utils.graph.GraphUndirected;

public class KargerRandomContractionAlgorithm {

	public static void main(String[] args) {
		int n = 200;	// ans: 17, in like billions of run, I only met this answer once!!!
		String filePath = "./data/kargerMinCut.txt";
		GraphUndirected g = readGraphFromFile(filePath, n);
		System.out.println("Done reading graph from file");
		int minCutSize = getMinCutSize(g, n * n * n);
		System.out.println("MinCut size = " + minCutSize);		
	}
	
	private static int getMinCutSize(GraphUndirected g, int trials) {
		int minCut = Integer.MAX_VALUE;
		for (int i = 0; i < trials; i++) {
			while (g.getNumOfVertices() > 2) {
				Edge e = g.getRandomEdge();
				g.contract(e);
			}
			
			minCut = Math.min(minCut, g.getNumOfEdges() / 2);
		}
		return minCut;
	}
	
	private static GraphUndirected readGraphFromFile(String filePath, int numOfVertices) {
		try {
			Scanner scanner = new Scanner(new BufferedInputStream(
					new FileInputStream(new File(filePath))));

			GraphUndirected graph = new GraphUndirected(numOfVertices);
			while (scanner.hasNextLine()) {
				String[] lineParts = scanner.nextLine().split("\\s+");
				int u = Integer.parseInt(lineParts[0]);
				for (int i = 1; i < lineParts.length; i++) {
					graph.addConnection(u, Integer.parseInt(lineParts[i]));
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
