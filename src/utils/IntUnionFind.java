package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class IntUnionFind {

	private Map<Integer, Integer> intParent;
	private Map<Integer, Integer> parentRank;

	public IntUnionFind(Set<Integer> intSet) {
		intParent = new HashMap<Integer, Integer>(intSet.size());
		parentRank = new HashMap<Integer, Integer>(intSet.size());
		for (Integer i : intSet) {
			intParent.put(i, i);
			parentRank.put(i, 0);
		}
	}

	public int find(int elem) {
		if (intParent.containsKey(elem)) {
			int elemParent = intParent.get(elem);
			if (elemParent != elem) {
				elemParent = find(elemParent);
				intParent.put(elem, elemParent);
			}

			return elemParent;
		} else {
			throw new IllegalArgumentException(
					"Trying to find an element that wasn't inserted in the set before "
							+ elem);
		}
	}

	public boolean union(int elem1, int elem2) {
		int elem1Parent = find(elem1);
		int elem2Parent = find(elem2);
		if (elem1Parent != elem2Parent) {
			if (parentRank.get(elem1Parent) > parentRank.get(elem2Parent)) {
				intParent.put(elem2Parent, elem1Parent);
			} else if (parentRank.get(elem1Parent) < parentRank.get(elem2Parent)) {
				intParent.put(elem1Parent, elem2Parent);
			} else {
				// both are equal, so doesn't matter
				intParent.put(elem2Parent, elem1Parent);
				parentRank.put(elem1Parent, parentRank.get(elem1Parent) + 1);
			}
			
			return true;
		} else 
			return false;
	}
	
	public Set<Integer> getLeadersOfComponents() {
		Set<Integer> leaders = new HashSet<Integer>(intParent.size());
		for (Entry<Integer, Integer> e : intParent.entrySet()) {
			if (e.getKey() == e.getValue())
				leaders.add(e.getKey());
		}
		
		return leaders;
	}
	
	public int countDistinctComponents() {
		return getLeadersOfComponents().size();
	}

	public Map<Integer, Set<Integer>> getDistinctComponents() {
		Set<Integer> compLeader = getLeadersOfComponents();
		Map<Integer, Set<Integer>> res = new HashMap<Integer, Set<Integer>>(compLeader.size());
		for (Integer l : compLeader)
			res.put(l, new HashSet<Integer>());
		
		for (Integer elem : intParent.keySet()) {
			res.get(find(elem)).add(elem);
		}
		
		return res;
	}
}
