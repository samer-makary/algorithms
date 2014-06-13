package graph;

public class WeightedEdge extends Edge {
	
	public double weight;

	public WeightedEdge(int u, int v, double w) {
		this(u, v, w, false);
	}
	
	public WeightedEdge(int u, int v, double w, boolean directed) {
		super(u, v, directed);
		this.weight = w;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o)
				&& Double.compare(weight, ((WeightedEdge) o).weight) == 0;
	}

	@Override
	public int hashCode() {
		long w = Double.doubleToRawLongBits(weight);
		int hashW = (int) (w ^ (w >>> 32));
		int result = 51;
		result = 31 * result + super.hashCode();
		result = 31 * result + hashW;
		return result;
	}

	@Override
	public String toString() {
		String stringW = String.format("%.2f", weight);
		if (directed)
			return "(" + uID + "," + vID + " = " + stringW + ")";
		else
			return "{" + uID + "," + vID + " = " + stringW + "}";
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new WeightedEdge(uID, vID, weight, directed);
	}
	
	

}
