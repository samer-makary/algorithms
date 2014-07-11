package utils.heap;

public class HeapEntry<T extends Comparable<T>> {

	private int idx;
	private T data;

	HeapEntry() {
		this(null, -1);
	}

	HeapEntry(T data, int idx) {
		this.data = data;
		this.idx = idx;
	}

	void setIdx(int idx) {
		this.idx = idx;
	}

	int getIdx() {
		return idx;
	}

	void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	@Override
	public int hashCode() {
		int res = 17;
		res = res * 31 + idx;
		if (data != null)
			res = res * 31 + data.hashCode();
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof HeapEntry) {
			HeapEntry<?> heapEntryObj = (HeapEntry<?>) obj;
			if (this.idx == heapEntryObj.idx)
				return this.data.equals(heapEntryObj.data);
		}

		return false;
	}

	@Override
	public String toString() {
		return String.format("{%d: [%s]}", idx, data.toString());
	}

	@Override
	protected final Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(
				"You MUST NOT clone heap entry object."
						+ "This will de-reference it from the heap structure");
	}

}
