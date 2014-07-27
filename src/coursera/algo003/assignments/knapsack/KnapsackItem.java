package coursera.algo003.assignments.knapsack;

public class KnapsackItem {

	int id;
	int value;
	int weight;
	
	public KnapsackItem(int id, int v, int w) {
		this.id = id;
		this.value = v;
		this.weight = w;
	}

	public int getId() {
		return id;
	}

	public int getValue() {
		return value;
	}

	public int getWeight() {
		return weight;
	}
	
	@Override
	public String toString() {
		return "Item [id=" + id + ", value=" + value + ", weight=" + weight
				+ "]";
	}
}
