package coursera.algo003.assignments.twosat;

import java.util.Map;

public abstract class TwoSATSolver {

	protected TwoSATProblem problem;
	
	public TwoSATSolver(TwoSATProblem problem) {
		if (problem == null) {
			throw new IllegalArgumentException("Problem cannot be null");
		}
		this.problem = problem;
	}
	
	public abstract void solve();
	
	public abstract boolean isSatisfiable();
	
	public abstract Map<Integer, Boolean> getSolution();

}
