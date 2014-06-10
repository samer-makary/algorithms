package coursera.algo005.assignments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class QuickSort {

	private static long comparisonCounter;

	public static final int FIRST_ELEMENT_PIVOT = 0;
	public static final int LAST_ELEMENT_PIVOT = 1;
	public static final int MEDIAN_OF_3_PIVOT = 2;
	public static int pivotingTech = FIRST_ELEMENT_PIVOT;

	public static long quickSort(int[] list) {
		comparisonCounter = 0L;
		sort(list, 0, list.length - 1);
		return comparisonCounter;
	}

	private static void sort(int[] list, int from, int to) {
		int pIdx = -1;
		switch (pivotingTech) {
		case FIRST_ELEMENT_PIVOT:
			pIdx = firstElementPivot(list, from, to);
			break;

		case LAST_ELEMENT_PIVOT:
			pIdx = lastElementPivot(list, from, to);
			break;

		case MEDIAN_OF_3_PIVOT:
			pIdx = medianElementPivot(list, from, to);
			break;

		default:
			throw new RuntimeException("Invalid value for pivoting technique "
					+ pivotingTech);
		}

		if (pIdx > from)
			sort(list, from, pIdx - 1);
		if (to > pIdx)
			sort(list, pIdx + 1, to);
	}

	private static int firstElementPivot(int[] list, int fromIdx, int toIdx) {
		if (fromIdx == toIdx)
			return fromIdx;

		comparisonCounter += toIdx - fromIdx;
		int pivot = list[fromIdx];
		int i = fromIdx + 1;
		int j = i;
		while (j <= toIdx) {
			if (list[j] < pivot) {
				int temp = list[i];
				list[i] = list[j];
				list[j] = temp;
				i++;
			}
			j++;
		}

		list[fromIdx] = list[i - 1];
		list[i - 1] = pivot;
		return i - 1;
	}

	private static int lastElementPivot(int[] list, int fromIdx, int toIdx) {
		if (fromIdx == toIdx)
			return fromIdx;

		// swap the last element with the first element before anything
		int p = list[toIdx];
		list[toIdx] = list[fromIdx];
		list[fromIdx] = p;

		comparisonCounter += toIdx - fromIdx;
		int pivot = list[fromIdx];
		int i = fromIdx + 1;
		int j = i;
		while (j <= toIdx) {
			if (list[j] < pivot) {
				int temp = list[i];
				list[i] = list[j];
				list[j] = temp;
				i++;
			}
			j++;
		}

		list[fromIdx] = list[i - 1];
		list[i - 1] = pivot;
		return i - 1;
	}

	private static int medianElementPivot(int[] list, int fromIdx, int toIdx) {
		if (fromIdx == toIdx)
			return fromIdx;

		int pivotIdx = getMedianOfThreeIdx(list, fromIdx, toIdx);
		// swap the median element with the first element before anything
		int p = list[pivotIdx];
		list[pivotIdx] = list[fromIdx];
		list[fromIdx] = p;

		comparisonCounter += toIdx - fromIdx;
		int pivot = list[fromIdx];
		int i = fromIdx + 1;
		int j = i;
		while (j <= toIdx) {
			if (list[j] < pivot) {
				int temp = list[i];
				list[i] = list[j];
				list[j] = temp;
				i++;
			}
			j++;
		}

		list[fromIdx] = list[i - 1];
		list[i - 1] = pivot;
		return i - 1;
	}

	private static int getMedianOfThreeIdx(int[] list, int fromIdx, int toIdx) {
		int middleIdx = (fromIdx + toIdx) >> 1;
		int[] three = new int[] { list[fromIdx], list[middleIdx], list[toIdx] };
		Arrays.sort(three);
		
		if (three[1] == list[fromIdx])
			return fromIdx;
		if (three[1] == list[toIdx])
			return toIdx;
		
		return middleIdx;
	}

	// The following part is to read and process the input file
	// given for the homework programming assignment
	public static void main(String[] args) {
		String filePath = "./data/QuickSort.txt";
		int[] ints = readArrayFromFile(filePath);
		long comparisons = -1;
		
		pivotingTech = FIRST_ELEMENT_PIVOT;
		comparisons = quickSort(ints.clone());
		System.out.println("Number of Comparisons for FIRST: " + comparisons);
		
		pivotingTech = LAST_ELEMENT_PIVOT;
		comparisons = quickSort(ints.clone());
		System.out.println("Number of Comparisons for LAST: " + comparisons);
		
		pivotingTech = MEDIAN_OF_3_PIVOT;
		comparisons = quickSort(ints.clone());
		System.out.println("Number of Comparisons for MEDIAN: " + comparisons);
		comparisons = quickSort(new int[] {0, 9, 8, 7, 6, 5, 4, 3, 2, 1});
		System.out.println("Number of Comparisons for MEDIAN: " + comparisons);
		
	}

	private static int[] readArrayFromFile(String filePath) {
		try {
			Scanner scanner = new Scanner(new BufferedInputStream(
					new FileInputStream(new File(filePath))));

			ArrayList<Integer> tempBuffer = new ArrayList<Integer>(100000);
			while (scanner.hasNextInt()) {
				tempBuffer.add(scanner.nextInt());
			}
			scanner.close();

			int[] array = new int[tempBuffer.size()];
			for (int i = 0; i < array.length; i++)
				array[i] = tempBuffer.get(i);

			return array;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
