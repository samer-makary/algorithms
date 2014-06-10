package testsuites;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import coursera.algo005.lectures.StreamMedian;

public class StreamMedianTest {

	@Test
	public void test1() {
		StreamMedian sm = new StreamMedian();
		int[] inArr = new int[] { 4, 2, 3, 1 };
		int[] outArr = new int[inArr.length];
		int[] expArr = new int[inArr.length];
		for (int i = 0; i < inArr.length; i++) {
			outArr[i] = sm.append(inArr[i]);
			int[] tmp = Arrays.copyOfRange(inArr, 0, i + 1);
			Arrays.sort(tmp);
			expArr[i] = tmp[(tmp.length - 1) >> 1];
		}
		assertArrayEquals(expArr, outArr);
	}

	@Test
	public void test2() {
		StreamMedian sm = new StreamMedian();
		int[] inArr = new int[] { 4 };
		int[] outArr = new int[inArr.length];
		int[] expArr = new int[inArr.length];
		for (int i = 0; i < inArr.length; i++) {
			outArr[i] = sm.append(inArr[i]);
			int[] tmp = Arrays.copyOfRange(inArr, 0, i + 1);
			Arrays.sort(tmp);
			expArr[i] = tmp[(tmp.length - 1) >> 1];
		}
		assertArrayEquals(expArr, outArr);
	}
	
	@Test
	public void test3() {
		StreamMedian sm = new StreamMedian();
		int[] inArr = new int[] { 4, 1 };
		int[] outArr = new int[inArr.length];
		int[] expArr = new int[inArr.length];
		for (int i = 0; i < inArr.length; i++) {
			outArr[i] = sm.append(inArr[i]);
			int[] tmp = Arrays.copyOfRange(inArr, 0, i + 1);
			Arrays.sort(tmp);
			expArr[i] = tmp[(tmp.length - 1) >> 1];
		}
		assertArrayEquals(expArr, outArr);
	}
	
	@Test
	public void test4() {
		StreamMedian sm = new StreamMedian();
		int[] inArr = new int[] { 4, 1, 2 };
		int[] outArr = new int[inArr.length];
		int[] expArr = new int[inArr.length];
		for (int i = 0; i < inArr.length; i++) {
			outArr[i] = sm.append(inArr[i]);
			int[] tmp = Arrays.copyOfRange(inArr, 0, i + 1);
			Arrays.sort(tmp);
			expArr[i] = tmp[(tmp.length - 1) >> 1];
		}
		assertArrayEquals(expArr, outArr);
	}
	
	@Test
	public void test5() {
		StreamMedian sm = new StreamMedian();
		int[] inArr = new int[] { 1, 2, 3, 4, 5 };
		int[] outArr = new int[inArr.length];
		int[] expArr = new int[inArr.length];
		for (int i = 0; i < inArr.length; i++) {
			outArr[i] = sm.append(inArr[i]);
			int[] tmp = Arrays.copyOfRange(inArr, 0, i + 1);
			Arrays.sort(tmp);
			expArr[i] = tmp[(tmp.length - 1) >> 1];
		}
		assertArrayEquals(expArr, outArr);
	}
	
	@Test
	public void test6() {
		StreamMedian sm = new StreamMedian();
		int[] inArr = new int[] { 5, 4, 3, 2, 1 };
		int[] outArr = new int[inArr.length];
		int[] expArr = new int[inArr.length];
		for (int i = 0; i < inArr.length; i++) {
			outArr[i] = sm.append(inArr[i]);
			int[] tmp = Arrays.copyOfRange(inArr, 0, i + 1);
			Arrays.sort(tmp);
			expArr[i] = tmp[(tmp.length - 1) >> 1];
		}
		assertArrayEquals(expArr, outArr);
	}

	@Test
	public void test7() {
		StreamMedian sm = new StreamMedian();
		int[] inArr = new int[] { -5, 4, -3, 0, 2, 1 };
		int[] outArr = new int[inArr.length];
		int[] expArr = new int[inArr.length];
		for (int i = 0; i < inArr.length; i++) {
			outArr[i] = sm.append(inArr[i]);
			int[] tmp = Arrays.copyOfRange(inArr, 0, i + 1);
			Arrays.sort(tmp);
			expArr[i] = tmp[(tmp.length - 1) >> 1];
		}
		assertArrayEquals(expArr, outArr);
	}
	
	// Random testing is just for fun :D
	@Test
	public void testRandom50() {
		StreamMedian sm = new StreamMedian();
		int N = 50;
		int[] inArr = new int[N];
		for (int i = 0; i < N; i++) {
			inArr[i] = (int) (Math.random() * N) + 1;
		}
		
		int[] outArr = new int[inArr.length];
		int[] expArr = new int[inArr.length];
		for (int i = 0; i < inArr.length; i++) {
			outArr[i] = sm.append(inArr[i]);
			int[] tmp = Arrays.copyOfRange(inArr, 0, i + 1);
			Arrays.sort(tmp);
			expArr[i] = tmp[(tmp.length - 1) >> 1];
		}
		assertArrayEquals(expArr, outArr);
	}
	
}
