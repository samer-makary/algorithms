package coursera.algo005.assignments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import coursera.algo005.lectures.StreamMedian;

public class StreamMedianAssignment {

	public static void main(String[] args) {
		String filePath = "./data/Median.txt";
		int[] ints = readArrayFromFile(filePath);
		StreamMedian sm = new StreamMedian();
		int result = 0;
		for (int i = 0; i < ints.length; i++) {
			int temp = sm.append(ints[i]);
			result = (result + (temp % 10000)) % 10000;
		}
		
		System.out.println(result % 10000);
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
