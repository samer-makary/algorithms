package coursera.algo003.assignments.knapsack;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class BranchAndBoundSolver extends AbstractKnapsackSolver {

	private KnapsackSolution solu;
	private final Map<Integer, Double> itemID2UnitValue = new HashMap<Integer, Double>(
			items.length);

	public BranchAndBoundSolver(KnapsackItem[] items, long knapsackSize) {
		super(items, knapsackSize);
		solu = new KnapsackSolution(-1, null);
	}

	@Override
	public void solve() {
		for (KnapsackItem i : items)
			itemID2UnitValue.put(i.id, ((double) i.value) / i.weight);

		// sort the items based on their unit-values
		Arrays.sort(items, new Comparator<KnapsackItem>() {
			// compare to sort in descending order
			@Override
			public int compare(KnapsackItem o1, KnapsackItem o2) {
				return -Double.compare(itemID2UnitValue.get(o1.getId()),
						itemID2UnitValue.get(o2.getId()));
			}
		});

		solu = solveDepthFirstBranchBound();
	}

	@Override
	public KnapsackSolution getSolution() {
		return solu;
	}

	private KnapsackSolution solveDepthFirstBranchBound() {

		// get the total number of items to be considered
		int itemsCount = items.length;
		// int initValueEst = sumValues(itemsList);
		int initValueEst = getRelaxationUpperBound(knapsackSize, items);

		int bestValue = -1;
		BitSet bestTaken = null;
		Stack<BranchBoundNode> stack = new Stack<BranchBoundNode>();
		stack.push(new BranchBoundNode(0, knapsackSize, initValueEst, new BitSet(),
				-1));
		while (!stack.isEmpty()) {
			BranchBoundNode node = stack.pop();
			int valueCur = node.valueCur;
			long capacityLeft = node.capacityLeft;
			int valueEst = node.valueEst;
			int lastItemConsidered = node.lastItemConsidered;
			BitSet takenItms = node.takenItms;
			if (capacityLeft >= 0 && (lastItemConsidered + 1) == itemsCount
					&& valueCur > bestValue) {
				bestValue = valueCur;
				bestTaken = takenItms;
			} else if (capacityLeft >= 0 && valueEst > bestValue) {
				lastItemConsidered++; // index of the item being considered now
				BitSet tempClone;
				// consider the case of leaving the item
				// int newValueEst = valueEst -
				// itemsList[lastItemConsidered].value;
				int newValueEst = valueCur
						+ getRelaxationUpperBound(capacityLeft,
								Arrays.copyOfRange(items,
										lastItemConsidered + 1, itemsCount));
				tempClone = (BitSet) takenItms.clone();
				tempClone.set(lastItemConsidered, false);
				stack.push(new BranchBoundNode(valueCur, capacityLeft,
						newValueEst, tempClone, lastItemConsidered));
				// consider the case of taking the item
				int newValue = valueCur + items[lastItemConsidered].value;
				long newCapacityLeft = capacityLeft
						- items[lastItemConsidered].weight;
				tempClone = (BitSet) takenItms.clone();
				tempClone.set(lastItemConsidered, true);
				stack.push(new BranchBoundNode(newValue, newCapacityLeft,
						valueEst, tempClone, lastItemConsidered));

			}
		}

		// we need to construct the selected items of solution
		// based on the given-initial ordering of the items (before sorting).
		// This is to mark the elements selected based on their IDs rather 
		// than their new index in the items array after sorting it.
		BitSet selected = new BitSet();
		for (int idx = 0; idx < items.length; idx++)
			selected.set(items[idx].id, bestTaken.get(idx));
		return new KnapsackSolution(bestValue, selected);
	}

	public static int sumValues(KnapsackItem[] itemsList) {
		int sum = 0;
		for (KnapsackItem itm : itemsList)
			sum += itm.value;
		return sum;
	}

	private int getRelaxationUpperBound(long capacity, KnapsackItem[] itemsList) {
		double totalValue = 0.0;
		int totalWeight = 0;
		int i = 0;
		while (i < itemsList.length
				&& totalWeight + itemsList[i].weight <= capacity) {
			totalWeight += itemsList[i].weight;
			totalValue += itemsList[i].value;
			i++;
		}

		if (i < itemsList.length)
			totalValue += (capacity - totalWeight)
					* itemID2UnitValue.get(itemsList[i].id);

		return (int) Math.round(totalValue);
	}

	class BranchBoundNode {
		int valueCur;
		long capacityLeft;
		int valueEst;
		BitSet takenItms;
		int lastItemConsidered;

		public BranchBoundNode(int valueCur, long capacityLeft, int valueEst,
				BitSet takenItms, int lastItemConsidered) {
			super();
			this.valueCur = valueCur;
			this.capacityLeft = capacityLeft;
			this.valueEst = valueEst;
			this.takenItms = takenItms;
			this.lastItemConsidered = lastItemConsidered;
		}

		@Override
		public String toString() {
			StringBuilder taken = new StringBuilder();
			taken.append("{ ");
			for (int i = 0; i <= lastItemConsidered; i++)
				taken.append((takenItms.get(i) ? "1 " : "0 "));
			taken.append("}");
			return "BranchBoundNode [idx=" + lastItemConsidered + ", valueCur="
					+ valueCur + ", capacityLeft=" + capacityLeft
					+ ", valueEst=" + valueEst + ", takenItms="
					+ taken.toString() + "]";
		}

	}

}
