package testsuites;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import utils.graph.GraphDirected;
import utils.graph.shortestpath.IFinderSingleSourceShortestPath;
import utils.graph.shortestpath.singlesource.DijkstrasAlgorithm;

public class DijkstraSPNewAlgorithmTest {

	private static GraphDirected readGraphFromFile(String filePath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					filePath)));
			String[] lineParts;
			String line;
			GraphDirected dg = new GraphDirected();
			while ((line = br.readLine()) != null) {

				lineParts = line.split("\\s+");
				int tail = Integer.parseInt(lineParts[0]);
				for (int i = 1; i < lineParts.length; i++) {
					String[] edge = lineParts[i].split(",");
					int head = Integer.parseInt(edge[0]);
					double weight = Double.parseDouble(edge[1]);
					dg.addConnection(tail, head, weight);
				}
			}

			br.close();
			return dg;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void testCase1_1() {
		int s = 1;
		String filePath = "./data/DijkstraSP/test-case-1.txt";
		GraphDirected g = readGraphFromFile(filePath);
		IFinderSingleSourceShortestPath algo = new DijkstrasAlgorithm(g);
		algo.computeShortestPathsFrom(s);
		Map<Integer, Integer> act = new HashMap<Integer, Integer>();
		act.put(1, 0);
		act.put(2, 1);
		act.put(3, 2);
		act.put(4, 1);
		act.put(7, 7);
		act.put(6, 6);
		act.put(5, 4);
		for (Integer i : act.keySet()) {
			assertEquals("Failed for node " + i, new Double(act.get(i)),
					new Double(algo.getShortestPathCostTo(i)));
		}
	}

	@Test
	public void testCase1_6() {
		int s = 6;
		String filePath = "./data/DijkstraSP/test-case-1.txt";
		GraphDirected g = readGraphFromFile(filePath);
		IFinderSingleSourceShortestPath algo = new DijkstrasAlgorithm(g);
		algo.computeShortestPathsFrom(s);
		Map<Integer, Integer> act = new HashMap<Integer, Integer>();
		act.put(1, 6);
		act.put(2, 7);
		act.put(3, 5);
		act.put(4, 5);
		act.put(7, 1);
		act.put(6, 0);
		act.put(5, 2);
		for (Integer i : act.keySet()) {
			assertEquals("Failed for node " + i, new Double(act.get(i)),
					new Double(algo.getShortestPathCostTo(i)));
		}
	}

	@Test
	public void testCase2_1() {
		int s = 1;
		String filePath = "./data/DijkstraSP/test-case-2.txt";
		GraphDirected g = readGraphFromFile(filePath);
		IFinderSingleSourceShortestPath algo = new DijkstrasAlgorithm(g);
		algo.computeShortestPathsFrom(s);
		Map<Integer, Integer> act = new HashMap<Integer, Integer>();
		act.put(1, 0);
		act.put(2, 51);
		act.put(3, 8);
		act.put(4, 1);
		act.put(5, 45);
		act.put(6, 46);
		act.put(7, 42);
		act.put(8, 59);
		act.put(9, 39);
		act.put(10, 55);
		act.put(11, 30);
		act.put(12, 28);
		act.put(13, 42);
		act.put(14, 27);
		act.put(15, 49);
		act.put(16, 51);
		act.put(17, 44);
		act.put(18, 43);
		act.put(19, 68);
		act.put(20, 21);
		act.put(21, 22);
		act.put(22, 30);
		act.put(23, 45);
		act.put(24, 22);
		act.put(25, 31);
		act.put(26, 23);
		act.put(27, 47);
		act.put(28, 57);
		act.put(29, 23);
		act.put(30, 54);
		act.put(31, 57);
		act.put(32, 62);
		act.put(33, 31);
		act.put(34, 39);
		act.put(35, 54);
		act.put(36, 57);
		act.put(37, 49);
		act.put(38, 35);
		act.put(39, 16);
		act.put(40, 43);
		act.put(41, 67);
		act.put(42, 20);
		act.put(43, 76);
		act.put(44, 49);
		act.put(45, 59);
		act.put(46, 62);
		act.put(47, 50);
		act.put(48, 26);
		act.put(49, 58);
		act.put(50, 16);
		for (Integer i : act.keySet()) {
			assertEquals("Failed for node " + i, new Double(act.get(i)),
					new Double(algo.getShortestPathCostTo(i)));
		}
	}

	@Test
	public void testCase3_1() {
		int s = 1;
		String filePath = "./data/DijkstraSP/test-case-3.txt";
		GraphDirected g = readGraphFromFile(filePath);
		IFinderSingleSourceShortestPath algo = new DijkstrasAlgorithm(g);
		algo.computeShortestPathsFrom(s);
		Map<Integer, Integer> act = new HashMap<Integer, Integer>();
		act.put(1, 0);
		act.put(2, 8);
		act.put(3, 10);
		act.put(4, 12);
		act.put(5, 11);
		act.put(6, 15);
		act.put(7, 7);
		act.put(8, 21);
		act.put(9, 22);
		act.put(10, 18);
		act.put(11, 24);
		act.put(12, 20);
		act.put(13, 21);
		for (Integer i : act.keySet()) {
			assertEquals("Failed for node " + i, new Double(act.get(i)),
					new Double(algo.getShortestPathCostTo(i)));
		}
	}

	@Test
	public void testCase4_1() {
		int s = 1;
		String filePath = "./data/DijkstraSP/test-case-4.txt";
		GraphDirected g = readGraphFromFile(filePath);
		IFinderSingleSourceShortestPath algo = new DijkstrasAlgorithm(g);
		algo.computeShortestPathsFrom(s);
		Map<Integer, Integer> act = new HashMap<Integer, Integer>();
		act.put(1, 0);
		act.put(2, 7);
		act.put(3, 9);
		act.put(4, 20);
		act.put(5, 20);
		act.put(6, 11);
		for (Integer i : act.keySet()) {
			assertEquals("Failed for node " + i, new Double(act.get(i)),
					new Double(algo.getShortestPathCostTo(i)));
		}
	}

	@Test
	public void dijkstraData_29() {
		int s = 29;
		String filePath = "./data/DijkstraSP/dijkstraData.txt";
		GraphDirected g = readGraphFromFile(filePath);
		IFinderSingleSourceShortestPath algo = new DijkstrasAlgorithm(g);
		algo.computeShortestPathsFrom(s);
		Map<Integer, Integer> act = new HashMap<Integer, Integer>();
		act.put(1, 3540);
		act.put(2, 4420);
		act.put(3, 4538);
		act.put(4, 5445);
		act.put(5, 3317);
		act.put(6, 4903);
		act.put(7, 4048);
		act.put(8, 3769);
		act.put(9, 4032);
		act.put(10, 4206);
		act.put(11, 3649);
		act.put(12, 3310);
		act.put(13, 2784);
		act.put(14, 3944);
		act.put(15, 2451);
		act.put(16, 4361);
		act.put(17, 4249);
		act.put(18, 1790);
		act.put(19, 3154);
		act.put(20, 2947);
		for (Integer i : act.keySet()) {
			assertEquals("Failed for node " + i, new Double(act.get(i)),
					new Double(algo.getShortestPathCostTo(i)));
		}
	}

}
