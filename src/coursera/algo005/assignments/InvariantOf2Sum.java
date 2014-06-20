package coursera.algo005.assignments;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class InvariantOf2Sum {

	public static long countTargets(long[] list, long lb, long ub) {
		if (list == null || lb > ub)
			throw new IllegalArgumentException(
					"List cannot be null and the lb must be <= the ub");

		Set<Long> set = new HashSet<Long>(list.length);
		for (long i : list)
			set.add(i);

		long count = 0;
		Long[] distinctList = set.toArray(new Long[set.size()]);
		for (long t = lb; t <= ub; t++) {
			boolean found = false;
			int idx = 0;
			while (!found && idx < distinctList.length) {
				long x = distinctList[idx++];
				if (x != (t - x))
					found = set.contains(t - x);				
			}
			
			if (found)
				count++;
		}
		
		return count;
	}

	public static void main(String[] args) {
		String filePath = "./data/2_Sum.txt";
		long[] ints = readArrayFromFile(filePath, 1000000);
		System.out.println("Done reading the input file");
		long targets = countTargets(ints, -10000, 10000);
		System.out.println(targets);
	}

	private static long[] readArrayFromFile(String filePath, int n) {
		try {
			Scanner scanner = new Scanner(new BufferedInputStream(
					new FileInputStream(new File(filePath))));

			ArrayList<Long> tempBuffer = new ArrayList<Long>(n);
			while (scanner.hasNextLong()) {
				tempBuffer.add(scanner.nextLong());
			}
			scanner.close();

			long[] array = new long[tempBuffer.size()];
			for (int i = 0; i < array.length; i++)
				array[i] = tempBuffer.get(i);

			return array;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
}
