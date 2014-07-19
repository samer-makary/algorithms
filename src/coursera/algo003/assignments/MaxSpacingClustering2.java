package coursera.algo003.assignments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import utils.IntUnionFind;

public class MaxSpacingClustering2 {

	private static int verticesCount;
	private static int vertexBinaryLabelLength;
	private static Set<Integer> verticesLabels;

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Started...");
		long start;
		start = System.currentTimeMillis();
		readVerticesFromClustering_BigFile();
		System.out.println("Done reading vertices labels from file in "
				+ ((System.currentTimeMillis() - start) / 1000) + " sec(s)");

		start = System.currentTimeMillis();
		int K = maxNumberOfClusters();
		System.out.println("Max number of separate clusters = " + K
				+ ",\n  Result was computed in "
				+ ((System.currentTimeMillis() - start) / 1000) + " sec(s)");
	}

	/**
	 * Makes sure that if any two vertices have a Hamming distance <= 2, then
	 * they both must belong to the same cluster.
	 * 
	 * @return final number of clusters with Spacing equal to 3
	 */
	private static int maxNumberOfClusters() {
		IntUnionFind iuf = new IntUnionFind(verticesLabels);
		for (Integer v : verticesLabels) {
			Set<Integer> neighs = getNeighbors2Away(v);
			neighs.retainAll(verticesLabels);
			for (Integer vn : neighs) {
				iuf.union(v, vn);
			}
		}

		return iuf.countDistinctComponents();
	}

	private static Set<Integer> getNeighbors2Away(int v) {
		// n ... For 1 Hamming distances
		// (n choose 2) = n*(n-1)/2 ... For 2 Hamming distances
		Set<Integer> neighbors = new HashSet<Integer>(
				(vertexBinaryLabelLength >> 1) * (vertexBinaryLabelLength - 1));

		for (int b1 = 0; b1 < vertexBinaryLabelLength; b1++) {
			neighbors.add(v ^ (1 << b1));
			for (int b2 = b1 + 1; b2 < vertexBinaryLabelLength; b2++) {
				neighbors.add(v ^ ((1 << b1) | (1 << b2)));
			}
		}

		return neighbors;
	}

	private static void readVerticesFromClustering_BigFile()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new BufferedInputStream(
				new FileInputStream(new File("./data/clustering_big.txt"))));

		verticesCount = scanner.nextInt();
		vertexBinaryLabelLength = scanner.nextInt();
		if (vertexBinaryLabelLength > 31)
			throw new IllegalArgumentException(
					"Vertices binary labels cannot be more than 31 bits long");
		verticesLabels = new HashSet<Integer>(verticesCount);
		for (int v = 0; v < verticesCount; v++) {
			int label = 0;
			for (int bit = 0; bit < vertexBinaryLabelLength; bit++) {
				int aBit = scanner.nextInt();
				switch (aBit) {
				case 0:
				case 1:
					label <<= 1;
					label |= aBit;
					break;

				default:
					throw new IllegalArgumentException(
							"Vertices binary labels must be 0/1 not " + aBit);
				}
			}
			verticesLabels.add(label);
		}
		scanner.close();

	}

}
