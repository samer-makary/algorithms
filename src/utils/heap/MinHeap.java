package utils.heap;

import java.util.List;

public class MinHeap<T extends Comparable<T>> extends Heap<T> {

	public MinHeap() {
		super();
	}

	public MinHeap(List<T> c) {
		super(c);
	}

	public MinHeap(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	protected boolean heapPropertyHolds(int parentIdx, int childIdx) {
		if (!isValidHeapIdx(parentIdx) || !isValidHeapIdx(childIdx)) {
			throw new IndexOutOfBoundsException(String.format(
					"Cannot check heap property between "
							+ "parent at idx %d and child at idx %d "
							+ "while heap size is %d", parentIdx, childIdx,
					size()));
		}

		HeapEntry<T> parent = getEntryAt(parentIdx);
		HeapEntry<T> child = getEntryAt(childIdx);
		return parent.getData().compareTo(child.getData()) <= 0;
	}

	@Override
	public HeapEntry<T> updateEntryDataValue(HeapEntry<T> entry, T newData) {
		if (!isValidHeapIdx(entry.getIdx())) {
			throw new IllegalArgumentException(
					"This entry index does not belong to the heap. "
							+ "The entry could have been de-referenced from the heap. "
							+ "It has index of value " + entry.getIdx()
							+ " while heap size is " + size());
		}

		if (newData == null) {
			throw new IllegalArgumentException(
					"Cannot set the heap entry data object to null");
		}

		int comp = newData.compareTo(entry.getData());
		entry.setData(newData);
		if (comp < 0) {
			int i = entry.getIdx();
			while (isValidHeapIdx(i) && isValidHeapIdx(parentOf(i))
					&& !heapPropertyHolds(parentOf(i), i)) {
				swap(i, parentOf(i));
				i = parentOf(i);
			}
		} else {
			heapify(entry.getIdx());
		}

		return entry;
	}
}
