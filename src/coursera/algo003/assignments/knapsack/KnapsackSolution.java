package coursera.algo003.assignments.knapsack;

import java.util.BitSet;

public class KnapsackSolution {
	private long totalValue;
	private BitSet selected;

	public KnapsackSolution(long totalValue, BitSet selected) {
		this.totalValue = totalValue;
		this.selected = selected;
	}

	public long getTotalValue() {
		return totalValue;
	}

	/**
	 * Creates a binary string of zeros and ones indicating whether an item was
	 * selected into the knapsack or not given the ordering passed to the
	 * function. So for example if we have 4 items and the returned string from
	 * this function was 1011, then this means that the first, third, and fourth
	 * items in the given array were the only ones selected.
	 * 
	 * @param orderedItemsIDs
	 *            The given array of items IDs that specifies the order in which
	 *            the items will checked-and-added to the output binary string.
	 * @return the leftmost character in the string maps to the item with the ID
	 *         number at index 0 in the given ordering array, and the rightmost
	 *         character maps to the item with the ID number at index 0 in the
	 *         given ordering array. In the middle, all other items sorted by
	 *         their ID numbers ordering in the given array.
	 */
	public String getSelectedStringWithOrderedIDs(int[] orderedItemsIDs) {
		StringBuilder sb = new StringBuilder();
		for (int itemID : orderedItemsIDs)
			sb.append(((selected.get(itemID)) ? "1" : "0"));

		return sb.toString();
	}

	public boolean isItemSelected(int itemID) {
		return selected.get(itemID);
	}

	public void addItemToSolution(KnapsackItem i) {
		if (!selected.get(i.id)) {
			selected.set(i.id);
			totalValue += i.value;
		} else
			throw new IllegalStateException(i + ", totval: " + totalValue
					+ " and selected: " + selected);
	}

	@Override
	protected Object clone() {
		BitSet newSet = new BitSet(selected.length());
		newSet.and(selected);
		KnapsackSolution clonedSolu = new KnapsackSolution(totalValue, newSet);
		return clonedSolu;
	}

}
