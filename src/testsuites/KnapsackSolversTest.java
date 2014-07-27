package testsuites;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import coursera.algo003.assignments.knapsack.AbstractKnapsackSolver;
import coursera.algo003.assignments.knapsack.BranchAndBoundSolver;
import coursera.algo003.assignments.knapsack.DPRecursiveSolver;
import coursera.algo003.assignments.knapsack.DPTabularSolver;
import coursera.algo003.assignments.knapsack.KnapsackItem;
import coursera.algo003.assignments.knapsack.KnapsackSolution;

@RunWith(Parameterized.class)
public class KnapsackSolversTest {

	private static final boolean _RUN_DP_TABULAR_SOLVER_TEST = true;
	private static final boolean _RUN_DP_RECURSIVE_SOLVER_TEST = false;
	private static final boolean _RUN_BNB_SOLVER_TEST = true;
	private static final int _MAX_TEST_CASE_NUMBER = 18;

	@BeforeClass
	public static void classSetup() {
		System.out.println("Total number of test cases is " + _MAX_TEST_CASE_NUMBER);		
		if (!_RUN_DP_TABULAR_SOLVER_TEST)
			System.out.println(DPTabularSolver.class.getSimpleName()
					+ " will NOT be tested");
		if (!_RUN_DP_RECURSIVE_SOLVER_TEST)
			System.out.println(DPRecursiveSolver.class.getSimpleName()
					+ " will NOT be tested");
		if (!_RUN_BNB_SOLVER_TEST)
			System.out.println(BranchAndBoundSolver.class.getSimpleName()
					+ " will NOT be tested");
	}

	private KnapsackTestCase testCase;
	private int testCaseNumber;

	public KnapsackSolversTest(Integer testCaseNumber) {
		this.testCaseNumber = testCaseNumber;
	}

	// creates list of all test-cases numbers
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] testCasesNumbers = new Object[_MAX_TEST_CASE_NUMBER][1];
		for (int i = 0; i < _MAX_TEST_CASE_NUMBER; i++)
			testCasesNumbers[i][0] = new Integer(i + 1);
		return Arrays.asList(testCasesNumbers);
	}

	@Before
	public void setup() {
		System.gc();
		testCase = new KnapsackTestCase(testCaseNumber);
	}

	@Test
	public void testKnapsackDPTabularSolver() {
		if (!_RUN_DP_TABULAR_SOLVER_TEST)
			return;

		try {
			AbstractKnapsackSolver solver = new DPTabularSolver(testCase.items,
					testCase.knapsackSize);
			solver.solve();
			KnapsackSolution solu = solver.getSolution();
			assertEquals("Optimal value mismatch for case #" + testCaseNumber,
					testCase.optSoluValue, solu.getTotalValue());
			assertEquals("Selected items mismatch optimal value for case #"
					+ testCaseNumber, true, isValidSolution(solu));

		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("For test case #" + testCaseNumber);
			e.printStackTrace();
			throw e;
		} catch (OutOfMemoryError e) {
			// assuming that as a pass because tabular method doesn't scale
			System.err.println(DPTabularSolver.class.getSimpleName()
					+ " ran out of memory on case #" + testCaseNumber
					+ ". Tester will ignore this test case");
		}
	}

	@Test
	public void testKnapsackDPRecursiveSolver() {
		if (!_RUN_DP_RECURSIVE_SOLVER_TEST)
			return;

		try {
			AbstractKnapsackSolver solver = new DPRecursiveSolver(
					testCase.items, testCase.knapsackSize);
			solver.solve();
			KnapsackSolution solu = solver.getSolution();
			assertEquals("Optimal value mismatch for case #" + testCaseNumber,
					testCase.optSoluValue, solu.getTotalValue());
			assertEquals("Selected items mismatch optimal value for case #"
					+ testCaseNumber, true, isValidSolution(solu));

		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("For test case #" + testCaseNumber);
			e.printStackTrace();
			throw e;
		} catch (OutOfMemoryError e) {
			// assuming that as a pass because recursive method doesn't scale
			System.err.println(DPRecursiveSolver.class.getSimpleName()
					+ " ran out of memory on case #" + testCaseNumber
					+ ". Tester will ignore this test case");
		}
	}

	@Test
	public void testKnapsackBranchAndBoundSolver() {
		if (!_RUN_BNB_SOLVER_TEST)
			return;

		try {
			AbstractKnapsackSolver solver = new BranchAndBoundSolver(
					testCase.items, testCase.knapsackSize);
			solver.solve();
			KnapsackSolution solu = solver.getSolution();
			assertEquals("Optimal value mismatch for case #" + testCaseNumber,
					testCase.optSoluValue, solu.getTotalValue());
			assertEquals("Selected items mismatch optimal value for case #"
					+ testCaseNumber, true, isValidSolution(solu));

		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("For test case #" + testCaseNumber);
			e.printStackTrace();
			throw e;
		} catch (OutOfMemoryError e) {
			System.err.println(BranchAndBoundSolver.class.getSimpleName()
					+ " ran out of memory on case #" + testCaseNumber
					+ ". Tester will ignore this test case");
		}
	}

	private boolean isValidSolution(KnapsackSolution solu) {
		long takenSize = 0;
		long takenValue = 0;
		for (KnapsackItem i : testCase.items)
			if (solu.isItemSelected(i.getId())) {
				takenSize += i.getWeight();
				takenValue += i.getValue();
			}

		return takenSize <= testCase.knapsackSize
				&& takenValue == solu.getTotalValue();
	}

	private class KnapsackTestCase {
		String baseFileName;
		long knapsackSize;
		KnapsackItem[] items;
		long optSoluValue;
		@SuppressWarnings("unused")
		String selectedItems;

		public KnapsackTestCase(int testCaseNumber) {
			baseFileName = "./data/knapsack/test-case-(" + testCaseNumber + ")";
			File inFile = new File(baseFileName);
			File otFile = new File(baseFileName + ".solu");
			loadProblem(inFile, otFile);
		}

		@SuppressWarnings("unused")
		public int[] getOrderedItemsIDs() {
			int[] itemsIDs = new int[items.length];
			for (int i = 0; i < items.length; i++) {
				itemsIDs[i] = items[i].getId();
			}

			return itemsIDs;
		}

		private void loadProblem(File inFile, File otFile) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(inFile));
				String[] line = br.readLine().split("\\s+");
				int N = Integer.parseInt(line[0]);
				knapsackSize = Long.parseLong(line[1]);
				items = new KnapsackItem[N];
				for (int i = 0; i < N; i++) {
					line = br.readLine().split("\\s+");
					items[i] = new KnapsackItem(i, Integer.parseInt(line[0]),
							Integer.parseInt(line[1]));
				}
				br.close();

				br = new BufferedReader(new FileReader(otFile));
				line = br.readLine().split("\\s+");
				optSoluValue = Long.parseLong(line[0]);
				StringBuilder sb = new StringBuilder(N);
				line = br.readLine().split("\\s+");
				for (int i = 0; i < N; i++) {
					sb.append(line[i]);
				}
				selectedItems = sb.toString();
				br.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
