package testsuites;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import assignments.QuickSort;

public class QuickSortTest {

	private boolean testCompNum = false;

	@Before
	public void setUp() {
		QuickSort.pivotingTech = QuickSort.MEDIAN_OF_3_PIVOT;
		testCompNum = QuickSort.pivotingTech == QuickSort.FIRST_ELEMENT_PIVOT
				|| QuickSort.pivotingTech == QuickSort.LAST_ELEMENT_PIVOT;
	}

	@Test
	public void test1() {
		int[] list = new int[] { 1, 2, 3, 4 };
		long nc = QuickSort.quickSort(list);
		assertTrue(isSorted(list));
		if (testCompNum)
			assertEquals(6L, nc);
	}

	@Test
	public void test2() {
		int[] list = new int[] { 4, 3, 2, 1 };
		long nc = QuickSort.quickSort(list);
		assertTrue(isSorted(list));
		if (testCompNum)
			assertEquals(6L, nc);
	}

	@Test
	public void test3() {
		int[] list = new int[] { 1 };
		long nc = QuickSort.quickSort(list);
		assertTrue(isSorted(list));
		if (testCompNum)
			assertEquals(0L, nc);
	}

	@Test
	public void test4() {
		int[] list = new int[] { 2, 1 };
		QuickSort.quickSort(list);
		assertTrue(isSorted(list));
	}

	@Test
	public void test5() {
		int[] list = new int[] { 4, 5, 6, 3, 2, 1 };
		QuickSort.quickSort(list);
		assertTrue(isSorted(list));
	}

	private boolean isSorted(int[] toCheck) {
		boolean sorted = true;
		int i = 1;
		while (sorted && i < toCheck.length) {
			sorted = toCheck[i] >= toCheck[i - 1];
			i++;
		}
		return sorted;
	}

}
