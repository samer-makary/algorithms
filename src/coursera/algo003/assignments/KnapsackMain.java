package coursera.algo003.assignments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import coursera.algo003.assignments.knapsack.AbstractKnapsackSolver;
import coursera.algo003.assignments.knapsack.BranchAndBoundSolver;
import coursera.algo003.assignments.knapsack.DPTabularSolver;
import coursera.algo003.assignments.knapsack.KnapsackItem;
import coursera.algo003.assignments.knapsack.KnapsackSolution;

public class KnapsackMain {

	public static void main(String[] args) throws IOException {
		System.out.println("Trying to solve small problem...");
		solveSmallProblem();
		System.out.println("Trying to solve big problem...");
		solveBigProblem();
		System.out.println("Done");
	}

	private static void solveBigProblem() throws IOException {
		// read input file
		BufferedReader br = new BufferedReader(new FileReader(
				"./data/knapsack_big.txt"));
		String[] line = br.readLine().split("\\s+");
		int N = Integer.parseInt(line[0]);
		long knapsackSize = Long.parseLong(line[1]);
		KnapsackItem[] items = new KnapsackItem[N];
		int[] itemsIDs = new int[N];
		for (int i = 0; i < N; i++) {
			line = br.readLine().split("\\s+");
			items[i] = new KnapsackItem(i, Integer.parseInt(line[0]),
					Integer.parseInt(line[1]));
			itemsIDs[i] = items[i].getId();
		}
		br.close();

		// solve the problem
		long start = System.currentTimeMillis();
		AbstractKnapsackSolver solver = new BranchAndBoundSolver(items, knapsackSize);
		solver.solve();
		KnapsackSolution solu = solver.getSolution();
		System.out.println("Optimal Value = " + solu.getTotalValue());
		long totalSize = 0;
		String selectedLineNumbers = "";
		for (KnapsackItem i : items)
			if (solu.isItemSelected(i.getId())) {
				selectedLineNumbers += (i.getId() + 2) + ",";
				totalSize += i.getWeight();
			}

		System.out.println("Selected size is " + totalSize
				+ " and items are at lines " + selectedLineNumbers);
		System.out.println("Selected binary string "
				+ solu.getSelectedStringWithOrderedIDs(itemsIDs));
		System.out.println("Computations took "
				+ ((System.currentTimeMillis() - start) / (60 * 1000))
				+ " min(s)");
	}

	private static void solveSmallProblem() throws IOException {
		// read input file
		BufferedReader br = new BufferedReader(new FileReader(
				"./data/knapsack1.txt"));
		String[] line = br.readLine().split("\\s+");
		int N = Integer.parseInt(line[0]);
		long knapsackSize = Long.parseLong(line[1]);
		KnapsackItem[] items = new KnapsackItem[N];
		int[] itemsIDs = new int[N];
		for (int i = 0; i < N; i++) {
			line = br.readLine().split("\\s+");
			items[i] = new KnapsackItem(i, Integer.parseInt(line[0]),
					Integer.parseInt(line[1]));
			itemsIDs[i] = items[i].getId();
		}
		br.close();

		// solve the problem
		long start = System.currentTimeMillis();
		AbstractKnapsackSolver solver = new DPTabularSolver(items, knapsackSize);
		solver.solve();
		KnapsackSolution solu = solver.getSolution();
		System.out.println("Optimal Value = " + solu.getTotalValue());
		long totalSize = 0;
		String selectedLineNumbers = "";
		for (KnapsackItem i : items)
			if (solu.isItemSelected(i.getId())) {
				selectedLineNumbers += (i.getId() + 2) + ",";
				totalSize += i.getWeight();
			}

		System.out.println("Selected size is " + totalSize
				+ " and items are at lines " + selectedLineNumbers);
		System.out.println("Selected binary string "
				+ solu.getSelectedStringWithOrderedIDs(itemsIDs));
		System.out.println("Computations took "
				+ ((System.currentTimeMillis() - start) / (60 * 1000))
				+ " min(s)");
	}
}
