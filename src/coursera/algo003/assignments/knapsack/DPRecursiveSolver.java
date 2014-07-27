package coursera.algo003.assignments.knapsack;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Performance is so much worse than {@link DPTabularSolver}. Also, some bug
 * might exit as the solver fails some test cases, need more investigation.
 * 
 * @author Samer
 * 
 */
public class DPRecursiveSolver extends AbstractKnapsackSolver {

	private Map<SubProblem, KnapsackSolution> cachedMemo;
	private KnapsackSolution solu;

	public DPRecursiveSolver(KnapsackItem[] items, long knapsackSize) {
		super(items, knapsackSize);
		cachedMemo = new HashMap<SubProblem, KnapsackSolution>(((int) knapsackSize));
		this.solu = new KnapsackSolution(-1, null);
	}

	@Override
	public void solve() {
		try {
			this.solu = solveProblem(items.length, knapsackSize);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	private KnapsackSolution solveProblem(int i, long j) {
		if (i == 0 || j == 0)
			return new KnapsackSolution(0, new BitSet());

		KnapsackSolution subProblemSolu;
		SubProblem subProblem = new SubProblem(i, j);
		if (cachedMemo.containsKey(subProblem)) {
			subProblemSolu = cachedMemo.get(subProblem);
		} else {
			KnapsackItem item = items[i - 1];

			subProblemSolu = solveProblem(i - 1, j);
			if (item.weight <= j) {
				KnapsackSolution otherSolu = solveProblem(i - 1, j
						- item.weight);
				if (item.value + otherSolu.getTotalValue() > subProblemSolu
						.getTotalValue()) {
					otherSolu.addItemToSolution(item);
					subProblemSolu = otherSolu;
				}
			}

			cachedMemo.put(subProblem,
					(KnapsackSolution) subProblemSolu.clone());
		}

		return subProblemSolu;
	}

	@Override
	public KnapsackSolution getSolution() {
		return solu;
	}

	private class SubProblem {
		int iFirstItems;
		long jSackSize;

		public SubProblem(int iFirstItems, long jSackSize) {
			this.iFirstItems = iFirstItems;
			this.jSackSize = jSackSize;
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = result * 31 + iFirstItems;
			result = result * 31 + (int) (jSackSize ^ (jSackSize >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;
			if (o instanceof SubProblem) {
				SubProblem spo = (SubProblem) o;
				return iFirstItems == spo.iFirstItems
						&& jSackSize == spo.jSackSize;
			}
			return false;
		}
	}

}
