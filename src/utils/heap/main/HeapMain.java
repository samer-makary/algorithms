package utils.heap.main;

import java.util.ArrayList;
import java.util.List;

import utils.heap.Heap;
import utils.heap.HeapEntry;
import utils.heap.MaxHeap;

class HeapMain {

	public static void main(String[] args) {
		int[] in = new int[] { 4, 1, 3, 2, 16, 9, 10, 14, 8, 7 };
		List<Integer> l = new ArrayList<>();
		for (int i : in)
			l.add(i);

		Heap<Integer> maxHeap = new MaxHeap<Integer>(l);
		HeapEntry<Integer> entry100 = maxHeap.add(100);
		System.out.println("Entry 100: " + entry100);
		maxHeap.add(200);
		System.out.println("Entry 100: " + entry100);
		maxHeap.poll();
		System.out.println("Entry 100: " + entry100);

		System.out.println(maxHeap);
		System.out.println("Heap size = " + maxHeap.size());

		System.out.println("Extract first 5 maxs: ");
		for (int i = 0; i < 5; i++)
			System.out.print(maxHeap.poll() + " ");
		System.out.println();
		System.out.println(maxHeap);
		int[] newin = new int[] { -1, -5, 12, 20, 30, 0 };
		for (int i : newin) {
			maxHeap.add(i);
			System.out.println("Max heap after inserting " + i);
			System.out.println(maxHeap);
			System.out.println("------------------------------");
		}
		System.out.println("Extract all heap");
		while (!maxHeap.isEmpty())
			System.out.print(maxHeap.poll() + " ");

	}
}
