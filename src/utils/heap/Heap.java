package utils.heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Heap data structure that supports update-key operation in logarithmic time.<br>
 * <b>CAUTION:</b> this data structure was not properly tested. Use it
 * carefully.
 * 
 * @author samer
 * 
 * @param <T>
 *            The type of the data objects that are the keys of the heap.
 */
public abstract class Heap<T extends Comparable<T>> implements Iterable<T> {

	private List<HeapEntry<T>> heap;
	private final HeapEntry<T> _DUMMY = new HeapEntry<T>();

	public Heap() {
		this(0);
	}

	public Heap(int initialCapacity) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException(
					"Initial capacity cannot be negative");
		heap = new ArrayList<HeapEntry<T>>(initialCapacity);
		heap.add(_DUMMY);
	}

	public Heap(List<T> c) {
		if (c == null)
			throw new IllegalArgumentException("List cannot be null");
		heap = new ArrayList<HeapEntry<T>>(c.size() + 1);
		heap.add(_DUMMY);
		int i = 1;
		for (T t : c) {
			if (t == null)
				throw new IllegalArgumentException(
						"List cannot hold null values");
			HeapEntry<T> he = new HeapEntry<T>(t, i);
			i++;
			heap.add(he);
		}
		buildHeap();
	}

	private final class HeapIterator implements Iterator<T> {

		private Iterator<HeapEntry<T>> itr;

		public HeapIterator(Heap<T> heap) {
			this.itr = heap.heap.iterator();
			this.itr.next(); // advance once to skip the
		}

		@Override
		public boolean hasNext() {
			return itr.hasNext();
		}

		@Override
		public T next() {
			return itr.next().getData();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"Cannot directly remove from the heap");
		}
	}

	private void buildHeap() {
		for (int i = size() >> 1; i > 0; i--) {
			heapify(i);
		}
	}

	protected final boolean isValidHeapIdx(int idx) {
		return idx > 0 && idx <= size();
	}

	protected final void swap(int i, int j) {
		if (isValidHeapIdx(i) && isValidHeapIdx(j)) {
			Collections.swap(heap, i, j);
			heap.get(i).setIdx(i);
			heap.get(j).setIdx(j);
		}
	}

	protected final void heapify(int p) {
		if (!isValidHeapIdx(p))
			throw new IndexOutOfBoundsException(
					"Cannot start heapify process from invalid index " + p);

		int l = getLeftIdx(p);
		int r = getRightIdx(p);
		int newP = p;

		if (isValidHeapIdx(l) && !heapPropertyHolds(p, l))
			newP = l; // l must swap with parent

		if (isValidHeapIdx(r) && !heapPropertyHolds(newP, r))
			newP = r; // r must swap with the new parent either p or l

		if (newP != p) {
			swap(newP, p);
			heapify(newP);
		}
	}

	protected final int getLeftIdx(int idx) {
		return idx << 1;
	}

	protected final int getRightIdx(int idx) {
		return getLeftIdx(idx) + 1;
	}

	protected final int parentOf(int idx) {
		return idx >> 1;
	}

	protected final HeapEntry<T> getEntryAt(int idx) {
		if (isValidHeapIdx(idx)) {
			return heap.get(idx);
		}
		return null;
	}

	protected abstract boolean heapPropertyHolds(int parentIdx, int childIdx);

	/**
	 * Use it to update the value of the data object that the heap-entry
	 * reference is holding.
	 * 
	 * @param entry
	 *            the entry whose data parameter will be update with the new
	 *            value. This is supposed to be the same object reference that
	 *            was returned via {@link #add(Comparable)} method of the heap
	 *            when the data was first inserted into the heap.
	 * @param newData
	 *            the new data value.
	 * @return the given entry node after updating its data value.
	 */
	public abstract HeapEntry<T> updateEntryDataValue(HeapEntry<T> entry,
			T newData);

	/**
	 * Returns an iterator of the heap which loop on the elements in no
	 * particular order.<br>
	 * <b>CAUTION:</b> the iterator does not support {@link Iterator#remove()}
	 * operation. Calling remove will throw
	 * {@link UnsupportedOperationException}.
	 */
	@Override
	public Iterator<T> iterator() {
		return new HeapIterator(this);
	}

	public int size() {
		return heap.size() - 1;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public Object[] toArray() {
		return Arrays.copyOfRange(heap.toArray(), 1, heap.size());
	}

	public T[] toArray(T[] a) {
		return Arrays.copyOfRange(heap.toArray(a), 1, heap.size());
	}

	/**
	 * Create a new heap entry, adds the new entry to the heap while maintaining
	 * the heap property, and then returns a reference to the newly created
	 * entry.<br>
	 * <b>Note that you should keep reference to the new entry saved if will
	 * update the value of the data objects later.<br>
	 * So in case the value of the data object in the entry changed, then you
	 * should invoke the method {@link #updateEntryDataValue(HeapEntry)} on your
	 * heap in-order for the change to reflect in the heap and maintain the
	 * heap-property</b>
	 * 
	 * @param o
	 *            new data object that will be added to the heap.
	 * @return the heap entry object that holds given data object and its
	 *         position in the heap.
	 */
	public HeapEntry<T> add(T o) {
		if (o == null)
			throw new IllegalArgumentException(
					"Cannot add null value to the heap");

		HeapEntry<T> newEntry = new HeapEntry<T>(o, size() + 1);
		heap.add(newEntry);
		int i = newEntry.getIdx();
		while (isValidHeapIdx(i) && isValidHeapIdx(parentOf(i))
				&& !heapPropertyHolds(parentOf(i), i)) {
			swap(i, parentOf(i));
			i = parentOf(i);
		}

		return newEntry;
	}

	public boolean contains(Object o) {
		return heap.contains(o);
	}

	/**
	 * Returns the top of the heap <b>without removing it</b>.
	 * 
	 * @return the top of the heap, or <code>null</code> if the heap was already
	 *         empty.
	 * 
	 * @see #poll()
	 */
	public T peek() {
		if (isEmpty())
			return null;
		return heap.get(1).getData();
	}

	/**
	 * Extracts the top of the heap, remove and then return it to the caller.
	 * 
	 * @return the removed top of the heap, or <code>null</code> if the heap was
	 *         already empty.
	 * 
	 * @see #peek()
	 */
	public T poll() {
		if (isEmpty())
			return null;
		swap(1, size());
		HeapEntry<T> remEntry = heap.remove(size());
		if (!isEmpty()) {
			heapify(1);
		}

		return remEntry.getData();
	}

	public boolean containsAll(Collection<?> c) {
		return heap.containsAll(c);
	}

	public void clear() {
		heap.clear();
		heap.add(_DUMMY);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[\n  Parent -> (left,  right)\n");
		for (int i = 1; i <= size() >> 1; i++) {
			sb.append("  " + heap.get(i) + " -> (");
			sb.append(getEntryAt(getLeftIdx(i)) + ", ");
			sb.append(getEntryAt(getRightIdx(i)) + ")\n");
		}
		sb.append("]");

		return sb.toString();
	}

}
