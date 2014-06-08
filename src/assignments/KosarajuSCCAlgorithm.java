package assignments;

import graph.GraphDirected;
import graph.Vertex;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * This algorithm has a bug in it has I haven't found yet.
 * @author samer
 *
 */
public class KosarajuSCCAlgorithm {
	
	private static final int _NOT_VISITED = -2;
	private static final int _FIRST_VISIT = -1;
	private static final int _SECOND_VISIT = 0;
	
	private GraphDirected dg;
	private Map<Integer, Integer> vertexFinishingTime;
	private Map<Integer, Integer> vertexLeader;
	
	public KosarajuSCCAlgorithm(GraphDirected dag) {
		this.dg = dag;
		vertexFinishingTime = new HashMap<Integer, Integer>(dag.getNumOfVertices());
		for (Integer vid : dag.getVerticesIDs()) {
			vertexFinishingTime.put(vid, _NOT_VISITED);
		}
		vertexLeader = new HashMap<Integer, Integer>(dag.getNumOfVertices());
		for (Integer vid : dag.getVerticesIDs()) {
			vertexLeader.put(vid, _NOT_VISITED);
		}
	}
	
	public Map<Integer, Integer> getVerticesFinishingTimes() {
		return this.vertexFinishingTime;
	}
	
	public Map<Integer, Integer> getVerticesLeaders() {
		return this.vertexLeader;
	}
	
	public Map<Integer, Set<Integer>> extractSCCWithLeaders() {
		System.out
				.println("Computing the vertices finishing time in the reverse DAG");
		computeRevDGVerticesFinishingTime();
		
		System.out
				.println("Sorting the vertices finishing times in descending order");
		extractSCCFromOriginalDG();
		
		System.out
				.println("Aggregating each SCC vertices by common leader vertex");
		Map<Integer, Set<Integer>> sccs = new HashMap<Integer, Set<Integer>>();
		for (Entry<Integer, Integer> e : vertexLeader.entrySet()) {
			int leader = e.getValue();
			Set<Integer> scc;
			if (sccs.containsKey(leader)) {
				scc = sccs.get(leader);
			} else {
				scc = new HashSet<Integer>();
				sccs.put(leader, scc);
			}
			scc.add(e.getKey());
		}
		
		return sccs;
	}
	
	/**
	 * This function computes the finishing time of every vertex in the reversed directed-graph.
	 */
	private void computeRevDGVerticesFinishingTime() {
		GraphDirected revDG = dg.getReversed();
		int ft = 0;
		List<Integer> verticesIDs = revDG.getVerticesIDs();
//		Collections.sort(verticesIDs);
//		Collections.reverse(verticesIDs);
		for (Integer vid : verticesIDs) {
			if (vertexFinishingTime.get(vid) == _NOT_VISITED) {
				
				// invoke DSF starting from current vertex
				Stack<Integer> stack = new Stack<Integer>();
				stack.push(vid);
				vertexFinishingTime.put(vid, _FIRST_VISIT);
				while (!stack.isEmpty()) {
					Integer currVID = stack.peek();
					if (vertexFinishingTime.get(currVID) == _FIRST_VISIT) {
						vertexFinishingTime.put(currVID, _SECOND_VISIT);
						Vertex currV = revDG.getVertexByID(currVID);
						for (Integer currVAdjID : currV.getDistinctAdjVertices()) {
							if (vertexFinishingTime.get(currVAdjID) == _NOT_VISITED) {
								stack.push(currVAdjID);
								vertexFinishingTime.put(currVAdjID, _FIRST_VISIT);
							}
						}
					} else if (vertexFinishingTime.get(currVID) == _SECOND_VISIT) {
						vertexFinishingTime.put(currVID, ++ft);
						stack.pop();
					}
				}								
			}
		}		
	}
	
	private void extractSCCFromOriginalDG() {
		List<Entry<Integer, Integer>> vertFTList = new ArrayList<Entry<Integer, Integer>>(
				vertexFinishingTime.entrySet());
		Collections.sort(vertFTList, new Comparator<Entry<Integer, Integer>>() {
			
			@Override
			public int compare(Entry<Integer, Integer> o1,
					Entry<Integer, Integer> o2) {
				return o2.getValue() - o1.getValue();
			}
		});	
		
		for (Entry<Integer, Integer> e : vertFTList) {
			Integer vid = e.getKey();
			if (vertexLeader.get(vid) == _NOT_VISITED) {

				// invoke DSF starting from current vertex
				Stack<Integer> stack = new Stack<Integer>();
				stack.push(vid);
				vertexLeader.put(vid, _FIRST_VISIT);
				while (!stack.isEmpty()) {
					Integer currVID = stack.peek();
					if (vertexLeader.get(currVID) == _FIRST_VISIT) {
						vertexLeader.put(currVID, _SECOND_VISIT);
						Vertex currV = dg.getVertexByID(currVID);
						for (Integer currVAdjID : currV
								.getDistinctAdjVertices()) {
							if (vertexLeader.get(currVAdjID) == _NOT_VISITED) {
								stack.push(currVAdjID);
								vertexLeader.put(currVAdjID, _FIRST_VISIT);
							}
						}
					} else if (vertexLeader.get(currVID) == _SECOND_VISIT) {
						vertexLeader.put(currVID, vid);
						stack.pop();
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		int N = 5;
		int n = 4;
		String filePath = "./data/SCC/main-input.txt";
		System.out.println("Reading graph from file");
		GraphDirected g = readGraphFromFile(filePath, n);
		KosarajuSCCAlgorithm algo = new KosarajuSCCAlgorithm(g);
		Map<Integer, Set<Integer>> sccs = algo.extractSCCWithLeaders();
		Map<Integer, Integer> ft = algo.getVerticesFinishingTimes();
		Map<Integer, Integer> ls = algo.getVerticesLeaders();
		ArrayList<Integer> sccsSizes = new ArrayList<Integer>(sccs.size());
		for (Entry<Integer, Set<Integer>> e : sccs.entrySet()) {
			sccsSizes.add(e.getValue().size());
		}
		Collections.sort(sccsSizes);
		Collections.reverse(sccsSizes);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			if (i < sccsSizes.size())
				sb.append(sccsSizes.get(i)).append(' ');
			else
				sb.append("0 ");
		}
		System.out.println(sb);
		
		System.out.println(ft);
		System.out.println(ls);
		
//		int node = 8543;
//		System.out.println("FT of node: " + node + " = " + ft.get(node));
//		System.out.println("L of node: " + node + " = " + ls.get(node));
//		FT of node: 8543 = 623067
//		L of node: 8543 = 726608
		
//		FT of node: 8543 = 874865
//		L of node: 8543 = 8540
//		434821,968,459,313,211,205,197,180,178,177
	}
	
	public static GraphDirected readGraphFromFile(String filePath, int numOfVertices) {
		try {
			Scanner scanner = new Scanner(new BufferedInputStream(
					new FileInputStream(new File(filePath))));

			GraphDirected graph = new GraphDirected(numOfVertices);
			while (scanner.hasNextInt()) {
				graph.addConnection(scanner.nextInt(), scanner.nextInt());
			}
			scanner.close();
			return graph;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
