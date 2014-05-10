package testsuites;

import static org.junit.Assert.*;

import org.junit.Test;

import assignments.CountingInversions;

public class CountingInversionsTest {

	@Test(expected=IllegalArgumentException.class)
	public void testcase1() {
		CountingInversions.countInversions(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testcase2() {
		CountingInversions.countInversions(new int[] {});
	}
	
	@Test
	public void testcase3() {
		assertTrue(0 == CountingInversions.countInversions(new int[] {1}));
	}
	
	@Test
	public void testcase4() {
		assertTrue(1 == CountingInversions.countInversions(new int[] {2, 1}));
	}
	
	@Test
	public void testcase5() {
		assertTrue(2 == CountingInversions.countInversions(new int[] {2, 3, 1}));
	}
	
	@Test
	public void testcase6() {
		assertTrue(3 == CountingInversions.countInversions(new int[] {3, 2, 1}));
	}
	
	@Test
	public void testcase7() {
		assertTrue(15 == CountingInversions.countInversions(new int[] {6, 5, 4, 3, 2, 1}));
	}
	
	@Test
	public void testcase8() {
		assertTrue(0 == CountingInversions.countInversions(new int[] {1, 2, 3, 4, 5, 6}));
	}
	
	@Test
	public void testcase9() {
		int size = 100000;
		int[] large = new int[size];
		for (int i = 0; i < size; i++)
			large[i] = (int) (Math.random() * size);
		int inversions = 0;
		for (int i = 0; i < size; i++)
			for (int j = i + 1; j < size; j++)
				if (large[i] > large[j])
					inversions++;
		
		assertTrue(inversions == CountingInversions.countInversions(large));
	}
	
	/*
	// The following test case takes a looot of time
	@Test
	public void testcase10() {
		int size = 1000000;
		int[] large = new int[size];
		for (int i = 0; i < size; i++)
			large[i] = (int) (Math.random() * size);
		int inversions = 0;
		for (int i = 0; i < size; i++)
			for (int j = i + 1; j < size; j++)
				if (large[i] > large[j])
					inversions++;
		
		assertTrue(inversions == CountingInversions.countInversions(large));
	}
	*/
}
