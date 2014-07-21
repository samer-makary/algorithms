package coursera.algo003.assignments.knapsack;

public abstract class AbstractKnapsackSolver {

	protected KnapsackItem[] items;
	protected long knapsackSize;
	public AbstractKnapsackSolver(KnapsackItem[] items, long knapsackSize) {
		this.items = items;
		this.knapsackSize = knapsackSize;
	}
	
	public abstract void solve();
	
	public abstract KnapsackSolution getSolution();
	
}
