package coursera.algo003.assignments.knapsack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class KnapsackSolution {
	private long totalValue;
	private Map<Integer, Boolean> selected;

	public KnapsackSolution(long totalValue, Map<Integer, Boolean> selected) {
		this.totalValue = totalValue;
		this.selected = selected;
	}

	public long getTotalValue() {
		return totalValue;
	}

	/**
	 * Creates a binary string of zeros and ones indicating whether an item was
	 * selected into the knapsack or not. So for example if we have 4 items and
	 * the returned string from this function was 1011, then this means that the
	 * first, third, and fourth items were the only ones selected.
	 * 
	 * @return the leftmost character in the string maps to the item with the
	 *         least ID number, and the rightmost character maps to the item
	 *         with the largest ID number. In the middle, all other items sorted
	 *         ASC by their ID numbers also.
	 */
	public String getSelectedStringWithAscOrderOfIDs() {
		List<Integer> ids = new ArrayList<Integer>(selected.keySet());
		Collections.sort(ids);

		StringBuilder sb = new StringBuilder();
		for (Integer itemID : ids)
			sb.append(((selected.get(itemID)) ? "1" : "0"));

		return sb.toString();
	}
}
