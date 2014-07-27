package coursera.algo003.assignments.knapsack;

import java.util.BitSet;

public class DPTabularSolver extends AbstractKnapsackSolver {

	private int[][] table;
	private KnapsackSolution solu;

	public DPTabularSolver(KnapsackItem[] items, long knapsackSize) {
		super(items, knapsackSize);
		if (knapsackSize > Integer.MAX_VALUE)
			throw new UnsupportedOperationException(
					"Knapsack size cannot exceed max integer value for tabular solver");
		this.table = new int[((int) knapsackSize) + 1][items.length + 1];
	}

	@Override
	public void solve() {

		// if no item is selected, optimal value will be zero
		for (int i = 0; i < table.length; i++)
			table[i][0] = 0;
		// if knapsack size is zero, optimal value will be zero
		for (int i = 0; i < table[0].length; i++)
			table[0][i] = 0;

		for (int itemIdx = 1; itemIdx < table[0].length; itemIdx++) {
			for (int sackSize = 1; sackSize < table.length; sackSize++) {
				KnapsackItem item = items[itemIdx - 1];
				if (item.weight <= sackSize) {
					table[sackSize][itemIdx] = Math.max(
							table[sackSize][itemIdx - 1], table[sackSize
									- item.weight][itemIdx - 1]
									+ item.value);
				} else {
					table[sackSize][itemIdx] = table[sackSize][itemIdx - 1];
				}
			}
		}

		return;
	}

	@Override
	public KnapsackSolution getSolution() {
		if (solu == null) {
			int maxRow = table.length - 1;
			int maxCol = table[0].length - 1;
			int sizeIdx = maxRow;
			int itemIdx = maxCol;

			BitSet selected = new BitSet(itemIdx);
			while (sizeIdx > 0 && itemIdx > 0) {
				if (table[sizeIdx][itemIdx] == table[sizeIdx][itemIdx - 1]) {
					selected.set(items[itemIdx - 1].id, false);
				} else {
					selected.set(items[itemIdx - 1].id, true);
					sizeIdx -= items[itemIdx - 1].weight;
				}
				itemIdx--;
			}

			while (itemIdx > 0) {
				selected.set(items[itemIdx - 1].id, false);
				itemIdx--;
			}

			solu = new KnapsackSolution(table[maxRow][maxCol], selected);
		}
		return solu;
	}

}
