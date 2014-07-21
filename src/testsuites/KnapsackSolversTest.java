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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import coursera.algo003.assignments.knapsack.AbstractKnapsackSolver;
import coursera.algo003.assignments.knapsack.BranchAndBoundSolver;
import coursera.algo003.assignments.knapsack.DPTabularSolver;
import coursera.algo003.assignments.knapsack.KnapsackItem;
import coursera.algo003.assignments.knapsack.KnapsackSolution;

@RunWith(Parameterized.class)
public class KnapsackSolversTest {

	private static final int _MAX_TEST_CASE_NUMBER = 18;
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
		testCase = new KnapsackTestCase(testCaseNumber);
	}

	@Test
	public void testKnapsackDPTabularSolver() {
		AbstractKnapsackSolver solver = new DPTabularSolver(testCase.items,
				testCase.knapsackSize);
		solver.solve();
		KnapsackSolution solu = solver.getSolution();
		assertEquals("Optimal value mismatch for case #" + testCaseNumber,
				testCase.optSoluValue, solu.getTotalValue());
		assertEquals("Selected items mismatch for case #" + testCaseNumber,
				testCase.selectedItems,
				solu.getSelectedStringWithAscOrderOfIDs());
	}

	@Test
	public void testKnapsackBranchAndBoundSolver() {
		AbstractKnapsackSolver solver = new BranchAndBoundSolver(testCase.items,
				testCase.knapsackSize);
		solver.solve();
		KnapsackSolution solu = solver.getSolution();
		assertEquals("Optimal value mismatch for case #" + testCaseNumber,
				testCase.optSoluValue, solu.getTotalValue());
		assertEquals("Selected items mismatch for case #" + testCaseNumber,
				testCase.selectedItems,
				solu.getSelectedStringWithAscOrderOfIDs());
	}

	private class KnapsackTestCase {
		long knapsackSize;
		KnapsackItem[] items;
		long optSoluValue;
		String selectedItems;

		public KnapsackTestCase(int testCaseNumber) {
			File inFile = new File("./data/knapsack/test-case-("
					+ testCaseNumber + ")");
			File otFile = new File("./data/knapsack/test-case-("
					+ testCaseNumber + ").solu");
			loadProblem(inFile, otFile);
		}

		private void loadProblem(File inFile, File otFile) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(inFile));
				int N = br.read();
				knapsackSize = br.read();
				items = new KnapsackItem[N];
				for (int i = 0; i < N; i++) {
					items[i] = new KnapsackItem(i, br.read(), br.read());
				}
				br.close();

				br = new BufferedReader(new FileReader(otFile));
				optSoluValue = br.read();
				br.read(); // read dummy integer value in files
				StringBuilder sb = new StringBuilder(N);
				for (int i = 0; i < N; i++) {
					sb.append(br.read());
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
