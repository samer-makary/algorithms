package coursera.algo005.assignments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CountingInversions {

	public static long countInversions(int[] array) {
		if (array == null || array.length == 0)
			throw new IllegalArgumentException(
					"Invalid array input for counting number of inversions");

		return mergeAndCount(array);
	}

	private static long mergeAndCount(int[] array) {
		if (array.length == 1)
			return 0L;
		else {
			int mid = array.length / 2;

			// O(n) splitting of the list into two half-length lists
			int[] leftSubArray = Arrays.copyOf(array, mid);
			int[] rightSubArray = Arrays.copyOfRange(array, mid, array.length);

			// apply merge and count on both sub-arrays separately
			long leftInversions = mergeAndCount(leftSubArray);
			long rightInversions = mergeAndCount(rightSubArray);

			// count the number of inversion across both sub-arrays
			long splitInversions = mergeAndCountSplitInversion(array,
					leftSubArray, rightSubArray);

			return leftInversions + splitInversions + rightInversions;
		}
	}

	private static long mergeAndCountSplitInversion(int[] array, int[] left,
			int[] right) {
		long splitInversions = 0L;
		int mergePtr = 0;
		int leftPtr = 0;
		int rightPtr = 0;

		while (leftPtr < left.length && rightPtr < right.length) {
			if (left[leftPtr] > right[rightPtr]) {
				array[mergePtr++] = right[rightPtr++];
				splitInversions += (left.length - leftPtr);
			} else {
				array[mergePtr++] = left[leftPtr++];
			}
		}

		while (rightPtr < right.length) {
			array[mergePtr++] = right[rightPtr++];
		}

		while (leftPtr < left.length) {
			array[mergePtr++] = left[leftPtr++];
		}

		return splitInversions;
	}

	// The following part is to read and process the input file
	// given for the homework programming assignment
	public static void main(String[] args) {
		String filePath = "./data/IntegerArray.txt";
		int[] ints = readArrayFromFile(filePath);
		long invers = countInversions(ints);
		System.out.println("Number of Inversions: " + invers);
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
