package coursera.algo003.assignments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utils.graph.GraphDirected;
import utils.graph.shortestpath.IFinderAllPairsShortestPath;
import utils.graph.shortestpath.allpairs.BellmanFordBasedAlgorithm;
import utils.graph.shortestpath.allpairs.JohnsonsAlgorithm;

public class AllPairsShortestPath {

	private static final int _BELLMAN_FORD_ALGORITHM = 0;
	private static final int _JOHNSONS_ALGORITHM = 1;
	private static final int _ALGORITHM = _BELLMAN_FORD_ALGORITHM;

	public static void main(String[] args) throws IOException {
		for (int i : new int[] { 1, 2, 3 }) {
			String filePath = String.format("./data/APSP/g%d.txt", i);
			System.out.println("Processing graph file @ " + filePath);
			runBellmanFordAPSP(filePath);
			System.out.println();
		}
	}

	private static void runBellmanFordAPSP(String graphFile) throws IOException {
		long start = System.currentTimeMillis();
		GraphDirected dg = readGraphFromFile(graphFile);
		System.out.println("Graph loaded from file in "
				+ (System.currentTimeMillis() - start) + " millisec(s)");
		IFinderAllPairsShortestPath apspAlgorithm;
		switch (_ALGORITHM) {
		case _BELLMAN_FORD_ALGORITHM:
			apspAlgorithm = new BellmanFordBasedAlgorithm(dg);
			break;
		case _JOHNSONS_ALGORITHM:
			apspAlgorithm = new JohnsonsAlgorithm(dg);
			;
			break;

		default:
			throw new IllegalArgumentException("Unkown algorithm number "
					+ _ALGORITHM);
		}
		start = System.currentTimeMillis();
		Map<Integer, Map<Integer, Double>> allPairsCosts = apspAlgorithm
				.getShortestPathsCostsFromAllSources();
		if (allPairsCosts == null) {
			System.out.println("Graph contains negative cost cycles within "
					+ ((System.currentTimeMillis() - start) / (60 * 1000))
					+ " min(s)");
		} else {
			System.out
					.println("APSP using Bellman-Ford Algorithms finished in "
							+ ((System.currentTimeMillis() - start) / (60 * 1000))
							+ " min(s)");
			double allPairsMinCost = min(getDistinctValues(allPairsCosts));
			System.out.println("All-pairs min cost " + allPairsMinCost);
		}
	}

	private static Set<Double> getDistinctValues(
			Map<Integer, Map<Integer, Double>> allValues) {
		Set<Double> distinct = new HashSet<Double>();
		for (Map<Integer, Double> values : allValues.values()) {
			distinct.addAll(values.values());
		}

		return distinct;
	}

	private static double min(Set<Double> values) {
		double min = Double.MAX_VALUE;
		for (Double v : values) {
			if (Double.compare(v, min) < 0)
				min = v;
		}
		return min;
	}

	private static GraphDirected readGraphFromFile(String filePath)
			throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				new File(filePath)));
		String[] lineParts = br.readLine().split("\\s+");
		int n = Integer.parseInt(lineParts[0]);
		int m = Integer.parseInt(lineParts[1]);

		GraphDirected dg = new GraphDirected(n);
		for (int i = 0; i < m; i++) {
			lineParts = br.readLine().split("\\s+");
			int head = Integer.parseInt(lineParts[0]);
			int tail = Integer.parseInt(lineParts[1]);
			double weight = Double.parseDouble(lineParts[2]);
			dg.addConnection(head, tail, weight);
		}

		br.close();
		return dg;
	}
}
