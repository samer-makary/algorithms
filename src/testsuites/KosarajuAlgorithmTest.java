package testsuites;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import utils.graph.GraphDirected;
import coursera.algo005.assignments.KosarajuSCCAlgorithm;

public class KosarajuAlgorithmTest {

	private static int[] getSizeLargestNSCCs(int N,
			Map<Integer, Set<Integer>> sccs) {
		ArrayList<Integer> sccsSizes = new ArrayList<Integer>(sccs.size());
		for (Entry<Integer, Set<Integer>> e : sccs.entrySet()) {
			sccsSizes.add(e.getValue().size());
		}
		Collections.sort(sccsSizes);
		Collections.reverse(sccsSizes);
		int[] res = new int[N];
		for (int i = 0; i < N; i++) {
			if (i < sccsSizes.size())
				res[i] = sccsSizes.get(i);
			else
				res[i] = 0;
		}

		return res;
	}

	@Test
	public void testAssignmentSCC() {
		int N = 5;
		int n = 850000;
		String filePath = "./data/SCC/SCC.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 434821, 968, 459, 313, 211 }, res);
	}

	@Test
	public void testLecDG() {
		int N = 5;
		int n = 9;
		String filePath = "./data/SCC/lec-dg.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 3, 3, 3, 0, 0 }, res);
	}

	@Test
	public void testCase1() {
		int N = 5;
		int n = 8;
		String filePath = "./data/SCC/test-case-1.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 3, 3, 2, 0, 0 }, res);
	}

	@Test
	public void testCase2() {
		int N = 5;
		int n = 8;
		String filePath = "./data/SCC/test-case-2.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 3, 3, 1, 1, 0 }, res);
	}

	@Test
	public void testCase3() {
		int N = 5;
		int n = 8;
		String filePath = "./data/SCC/test-case-3.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 7, 1, 0, 0, 0 }, res);
	}

	@Test
	public void testCase4() {
		int N = 5;
		int n = 9;
		String filePath = "./data/SCC/test-case-4.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 6, 3, 2, 1, 0 }, res);
	}

	@Test
	public void testCase5() {
		int N = 10;
		int n = 50;
		String filePath = "./data/SCC/test-case-5.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 35, 7, 1, 1, 1, 1, 1, 1, 1, 1 }, res);
	}

	@Test
	public void testCase6() {
		int N = 5;
		int n = 15;
		String filePath = "./data/SCC/test-case-6.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 6, 1, 1, 0, 0 }, res);
	}

	@Test
	public void testCase7() {
		int N = 5;
		int n = 1437;
		String filePath = "./data/SCC/test-case-7.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 917, 313, 167, 37, 3 }, res);
	}

	@Test
	public void testCase8() {
		int N = 7;
		int n = 11;
		String filePath = "./data/SCC/test-case-8.txt";
		GraphDirected g = KosarajuSCCAlgorithm.readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		int[] res = getSizeLargestNSCCs(N, algo.extractSCCWithLeaders());
		assertArrayEquals(new int[] { 3, 2, 2, 2, 1, 1, 0 }, res);
	}

}
