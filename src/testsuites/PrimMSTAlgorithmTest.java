package testsuites;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import utils.graph.GraphUndirected;
import coursera.algo003.lectures.MSTUndirected;
import coursera.algo003.lectures.PrimAlgorithmForMST;

public class PrimMSTAlgorithmTest {

	private static final double EPSILON = 0.001;

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

	@Test
	public void testCase1_N() {
		String filePath = "./data/MST/test-case-1.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._HEAP_WITH_O_N_UPDATE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(-684, mst.getTreeWeight(), EPSILON);
	}

	@Test
	public void testCaseTiny_N() {
		String filePath = "./data/MST/tinyEWG.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._HEAP_WITH_O_N_UPDATE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(1.81000, mst.getTreeWeight(), EPSILON);
	}

	@Test
	public void testCaseMedium_N() {
		String filePath = "./data/MST/mediumEWG.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._HEAP_WITH_O_N_UPDATE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(10.46351, mst.getTreeWeight(), EPSILON);
	}

	@Test
	public void testCaseEdges_N() {
		String filePath = "./data/MST/edges.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._HEAP_WITH_O_N_UPDATE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(-3612829.0, mst.getTreeWeight(), EPSILON);
	}

	@Test
	public void testCase1_LogN() {
		String filePath = "./data/MST/test-case-1.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._HEAP_WITH_O_LOGN_UPDATE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(-684, mst.getTreeWeight(), EPSILON);
	}

	@Test
	public void testCaseTiny_LogN() {
		String filePath = "./data/MST/tinyEWG.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._HEAP_WITH_O_LOGN_UPDATE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(1.81000, mst.getTreeWeight(), EPSILON);
	}

	@Test
	public void testCaseMedium_LogN() {
		String filePath = "./data/MST/mediumEWG.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._HEAP_WITH_O_LOGN_UPDATE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(10.46351, mst.getTreeWeight(), EPSILON);
	}
	
	@Test
	public void testCaseEdges_LogN() {
		String filePath = "./data/MST/edges.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._HEAP_WITH_O_LOGN_UPDATE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(-3612829.0, mst.getTreeWeight(), EPSILON);
	}
	
	@Test
	public void testCase1_Naive() {
		String filePath = "./data/MST/test-case-1.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._NAIVE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(-684, mst.getTreeWeight(), EPSILON);
	}

	@Test
	public void testCaseTiny_Naive() {
		String filePath = "./data/MST/tinyEWG.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._NAIVE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(1.81000, mst.getTreeWeight(), EPSILON);
	}

	@Test
	public void testCaseMedium_Naive() {
		String filePath = "./data/MST/mediumEWG.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._NAIVE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(10.46351, mst.getTreeWeight(), EPSILON);
	}
	
	@Test
	public void testCaseEdges_Naive() {
		String filePath = "./data/MST/edges.txt";
		GraphUndirected g = readGraph(filePath);
		PrimAlgorithmForMST.heapType = PrimAlgorithmForMST._NAIVE;
		PrimAlgorithmForMST prim = new PrimAlgorithmForMST(g);
		MSTUndirected mst = prim.getMST();
		assertEquals(-3612829.0, mst.getTreeWeight(), EPSILON);
	}

}
