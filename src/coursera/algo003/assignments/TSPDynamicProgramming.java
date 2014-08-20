package coursera.algo003.assignments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import utils.graph.Edge;
import utils.graph.GraphUndirected;
import utils.graph.Vertex;
import utils.graph.WeightedEdge;

public class TSPDynamicProgramming {

	private GraphUndirected graph;

	public TSPDynamicProgramming(GraphUndirected udg) {
		if (udg == null)
			throw new IllegalArgumentException("Graph cannot be null");
		this.graph = udg;
	}

	public double tourCost() {
		Map<DPSolu, Double> toursCost = new HashMap<DPSolu, Double>();
		List<Integer> vertices = graph.getVerticesIDs();
		int n = graph.getNumOfVertices();

		// setup initial vertex of the tour
		int tourFirstVert = vertices.remove(0);
		toursCost.put(new DPSolu((1 << tourFirstVert), tourFirstVert), 0.0);
//		BitSet tmp = new BitSet(32);
//		tmp.set(tourFirstVert);
//		toursCost.put(new DPSolu(tmp, tourFirstVert), 0.0);

		for (int m = 1; m <= n - 1; m++) {
			long bef = System.currentTimeMillis();
			List<BitSet> tourSets = ints2BitSets(chooseR(new HashSet<Integer>(
					vertices), m));
			System.out.println("Generated " + tourSets.size()
					+ " combination in " + (System.currentTimeMillis() - bef));
			Map<DPSolu, Double> newToursCost = new HashMap<DPSolu, Double>(
					tourSets.size());
			for (BitSet SWithOutStart : tourSets) {
				BitSet S = (BitSet) SWithOutStart.clone();
				S.set(tourFirstVert);
				// for (int j : listSetBits(SWithOutStart)) {
				for (int j = SWithOutStart.nextSetBit(0); j >= 0; j = SWithOutStart
						.nextSetBit(j + 1)) {

//					DPSolu jSSolu = new DPSolu(S, j);
					DPSolu jSSolu = new DPSolu(bitSet2int(S), j);
					S.clear(j);
					double min = (toursCost.containsKey(jSSolu) ? toursCost
							.get(jSSolu) : Double.MAX_VALUE);
					Set<Edge> jAdjVerts = graph.getVertexByID(j)
							.getDistinctAdjEdges();
					for (Edge e : jAdjVerts) {
						Integer jAdj = e.getOtherVertex(j);
						// if (((S >> jAdj) & 1) == 1) {
						if (S.get(jAdj)) {
							WeightedEdge we = (WeightedEdge) e;
//							DPSolu tmpSolu = new DPSolu(S, jAdj);
							DPSolu tmpSolu = new DPSolu(bitSet2int(S), jAdj);
							double newCost = (toursCost.containsKey(tmpSolu) ? toursCost
									.get(tmpSolu) + we.weight
									: Double.MAX_VALUE);
							if (Double.compare(newCost, min) < 0) {
								min = newCost;
								newToursCost.put(jSSolu, newCost);
							}
						}
					}
					// S |= (1 << j);
					S.set(j);
				}
			}
			toursCost = newToursCost;
			System.out.println("Finished tours of length " + m);
		}

		Map<Integer, Double> weightJToStart = new HashMap<Integer, Double>();
		for (Integer vid : vertices) {
			Vertex v = graph.getVertexByID(vid);
			for (Edge e : v.getDistinctAdjEdges()) {
				if (e.getOtherVertex(vid) == tourFirstVert) {
					WeightedEdge we = (WeightedEdge) e;
					weightJToStart.put(vid, we.weight);
				}
			}
		}

		double minTourCost = Double.MAX_VALUE;
		int allVerts = setBitByVerticesIDs(0, vertices);
		allVerts |= (1 << tourFirstVert);
//		BitSet allVerts = new BitSet();
//		allVerts.set(0, vertices.size());
		for (int vid : vertices) {
			DPSolu tmpSolu = new DPSolu(allVerts, vid);
			double newCost = ((toursCost.containsKey(tmpSolu) && weightJToStart
					.containsKey(vid)) ? toursCost.get(tmpSolu)
					+ weightJToStart.get(vid) : Double.MAX_VALUE);
			if (Double.compare(newCost, minTourCost) < 0)
				minTourCost = newCost;
		}

		return minTourCost;
	}

	private List<BitSet> ints2BitSets(List<Integer> chooseR) {
		List<BitSet> bitsets = new ArrayList<BitSet>(chooseR.size());
		for (int c : chooseR) {
			BitSet bs = new BitSet(32);
			for (int i = 0; c > 0; c >>>= 1, i++) {
				if ((c & 0x1) == 1)
					bs.set(i);
			}
			bitsets.add(bs);
		}

		return bitsets;
	}

	private int bitSet2int(BitSet bs) {
		int val = 0;
		for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
			val |= (1 << i);
		}
		return val;
	}

	private int clearBit(int s, int bitIdx) {
		if (((s >>> bitIdx) & 0x1) == 1)
			s ^= (1 << bitIdx);
		return s;
	}

	private List<Integer> listSetBits(int intBits) {
		int verts = intBits;
		List<Integer> setBits = new ArrayList<Integer>(32);
		for (int i = 0; verts > 0; verts >>>= 1, i++) {
			if ((verts & 0x1) == 1)
				setBits.add(i);
		}
		return setBits;
	}

	private int setBitByVerticesIDs(int j, Collection<Integer> ids) {
		for (int i : ids)
			j |= (1 << i);
		return j;
	}

	private List<Integer> chooseR(Set<Integer> list, int r) {
		List<Integer> choices = new ArrayList<Integer>();
		if (list.size() == r) {
			int choice = setBitByVerticesIDs(0, list);
			choices.add(choice);
		} else if (r > 0) {
			for (Integer i : new HashSet<Integer>(list)) {
				list.remove(i);
				if (r > 1) {
					List<Integer> iChoices = chooseR(
							new HashSet<Integer>(list), r - 1);
					for (Integer iChoice : iChoices) {
						iChoice |= (1 << i);
						choices.add(iChoice);
					}
				} else {
					int iChoice = 1 << i;
					choices.add(iChoice);
				}
			}
		}

		return choices;
	}

	private class DPSolu {
		int tourVertices;
		int tourEndVertex;

		public DPSolu(int tvs, int v) {
			this.tourVertices = tvs;
			this.tourEndVertex = v;
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = result * 31 + hashCode(tourVertices);
			result = result * 31 + tourEndVertex;
			return result;
		}

		private int hashCode(int verts) {
			int result = 1;
			for (int i = 0; verts > 0; verts >>>= 1) {
				if ((verts & (1 << i)) != 0)
					result = result * 31 + i;
			}
			return result;
		}

		@Override
		public boolean equals(Object o) {
			if (o != null) {
				if (o instanceof DPSolu) {
					DPSolu dps = (DPSolu) o;
					return dps.tourEndVertex == this.tourEndVertex
							&& dps.tourVertices == this.tourVertices;
				}
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "[tourVertices=" + tourVertices + ", tourEndVertex="
					+ tourEndVertex + "]";
		}

	}

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("Start...");
		long start = System.currentTimeMillis();
		GraphUndirected graph = readPointsIntoGraph("./data/tsp.txt");
		System.out.println("Done reading graph in "
				+ (System.currentTimeMillis() - start) / 1000 + " sec(s)");
		TSPDynamicProgramming solver = new TSPDynamicProgramming(graph);
		start = System.currentTimeMillis();
		double tspCost = solver.tourCost();
		System.out.println(String.format(
				"Found TSP tour with cost %f in %d min(s)", tspCost,
				((System.currentTimeMillis() - start) / (60 * 1000))));

		List<Integer> verts = graph.getVerticesIDs();
		Integer[] arr = new Integer[verts.size()];
		arr = verts.toArray(arr);
		double[][] distances = readPointsIntoDistMat("./data/tsp.txt");
		ArrayList<Integer[]> perms = permutations(arr);
		double min = Double.MAX_VALUE;
		for (Integer[] tour : perms) {
			double tourCost = 0;
			for (int i = 1; i < tour.length; i++)
				tourCost += distances[tour[i - 1]][tour[i]];
			tourCost += distances[tour[tour.length - 1]][tour[0]];
			if (Double.compare(tourCost, min) < 0)
				min = tourCost;
		}
		System.out.println("Opt tour cost " + min);
	}

	private static double[][] readPointsIntoDistMat(String fileName)
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		int N = scanner.nextInt();
		double[][] points = new double[N][2];
		for (int i = 0; i < N; i++) {
			points[i][0] = scanner.nextDouble();
			points[i][1] = scanner.nextDouble();
		}
		scanner.close();

		double[][] dists = new double[N][N];
		for (int p1Idx = 0; p1Idx < N; p1Idx++) {
			double[] p1 = points[p1Idx];
			dists[p1Idx][p1Idx] = 0.0;
			for (int p2Idx = p1Idx + 1; p2Idx < N; p2Idx++) {
				if (p1Idx != p2Idx) {
					double[] p2 = points[p2Idx];
					double distance = Math.sqrt(Math.pow(p1[0] - p2[0], 2)
							+ Math.pow(p1[1] - p2[1], 2));
					dists[p1Idx][p2Idx] = distance;
					dists[p2Idx][p1Idx] = distance;
				}
			}
		}
		return dists;
	}

	private static GraphUndirected readPointsIntoGraph(String fileName)
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		int N = scanner.nextInt();
		double[][] points = new double[N][2];
		for (int i = 0; i < N; i++) {
			points[i][0] = scanner.nextDouble();
			points[i][1] = scanner.nextDouble();
		}
		scanner.close();

		GraphUndirected graph = new GraphUndirected(N);
		for (int p1Idx = 0; p1Idx < N; p1Idx++) {
			double[] p1 = points[p1Idx];
			for (int p2Idx = 0; p2Idx < N; p2Idx++) {
				if (p1Idx != p2Idx) {
					double[] p2 = points[p2Idx];
					double weight = Math.sqrt(Math.pow(p1[0] - p2[0], 2)
							+ Math.pow(p1[1] - p2[1], 2));
					graph.addConnection(p1Idx, p2Idx, weight);
				}
			}
		}
		return graph;
	}

	private static <E> ArrayList<E[]> permutations(E[] arr) {
		final ArrayList<E[]> resultList = new ArrayList<E[]>();
		final int l = arr.length;
		if (l == 0)
			return resultList;
		if (l == 1) {
			resultList.add(arr);
			return resultList;
		}

		E[] subClone = Arrays.copyOf(arr, l - 1);
		System.arraycopy(arr, 1, subClone, 0, l - 1);

		for (int i = 0; i < l; ++i) {
			E e = arr[i];
			if (i > 0)
				subClone[i - 1] = arr[0];
			final ArrayList<E[]> subPermutations = permutations(subClone);
			for (E[] sc : subPermutations) {
				E[] clone = Arrays.copyOf(arr, l);
				clone[0] = e;
				System.arraycopy(sc, 0, clone, 1, l - 1);
				resultList.add(clone);
			}
			if (i > 0)
				subClone[i - 1] = e;
		}
		return resultList;
	}

}
