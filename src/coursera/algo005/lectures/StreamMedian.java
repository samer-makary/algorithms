package coursera.algo005.lectures;

import java.util.PriorityQueue;
import java.util.Queue;

public class StreamMedian {

	private Queue<Integer> ltMedianMaxHeap;
	private Queue<Integer> gtMedianMinHeap;
	private int median;

	public StreamMedian() {
		reset();
	}

	public void reset() {
		ltMedianMaxHeap = new PriorityQueue<Integer>();
		gtMedianMinHeap = new PriorityQueue<Integer>();
	}

	public int getStreamSize() {
		return ltMedianMaxHeap.size() + gtMedianMinHeap.size();
	}

	public int append(int newElement) {
		if (ltMedianMaxHeap.isEmpty() && gtMedianMinHeap.isEmpty()) {
			median = newElement;
			ltMedianMaxHeap.add(-newElement);
		} else {
			// there is already some elements in the stream
			if (newElement <= median) {
				ltMedianMaxHeap.add(-newElement);
			} else {
				gtMedianMinHeap.add(newElement);
			}

			if (!areHeapsBalanced()) {
				restoreHeapsBalance();
			}

			extractMedian();
		}

		return median;
	}

	private void extractMedian() {
		if (ltMedianMaxHeap.size() >= gtMedianMinHeap.size()) {
			median = -1 * ltMedianMaxHeap.peek();
		} else {
			median = gtMedianMinHeap.peek();
		}

	}

	private void restoreHeapsBalance() {
		if (ltMedianMaxHeap.size() > gtMedianMinHeap.size()) {
			gtMedianMinHeap.add(-1 * ltMedianMaxHeap.poll());
		} else {
			ltMedianMaxHeap.add(-1 * gtMedianMinHeap.poll());
		}
	}

	private boolean areHeapsBalanced() {
		return Math.abs(ltMedianMaxHeap.size() - gtMedianMinHeap.size()) < 2;
	}

}
