package coursera.algo003.assignments.twosat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TwoSATProblem {

	private Set<Integer> variables;
	private List<Clause> clauses;

	public TwoSATProblem(int numVars, int numClas) {
		this.variables = new HashSet<Integer>(numVars);
		this.clauses = new ArrayList<TwoSATProblem.Clause>(numClas);
	}
	
	public int getNumVars() {
		return variables.size();
	}
	
	public int getNumClas() {
		return clauses.size();
	}
	
	public Set<Integer> getVariables() {
		return this.variables;
	}
	
	public List<Clause> getClauses() {
		return this.clauses;
	}

	public Clause addClause(int v1, int v2) {
		if (v1 == 0 || v2 == 0) {
			throw new IllegalArgumentException(
					"Non of the clause variables can be 0. "
							+ "Variables can only have non-zero values, but given "
							+ v1 + ", " + v2);
		}
		Clause c = new Clause(v1, v2);
		variables.add(Math.abs(v1));
		variables.add(Math.abs(v2));
		clauses.add(c);
		return c;
	}

	public boolean isSolution(Map<Integer, Boolean> assignment) {
		try {
			Clause mustBeNull = getUnsatisfiedClauseGiven(assignment);
			return mustBeNull == null;
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Clause getUnsatisfiedClauseGiven(Map<Integer, Boolean> assignment) {
		if (assignment.keySet() != variables) {
			throw new IllegalArgumentException(
					"Given assignment does not cover all variables");
		}

		Clause unsatClause = null;
		int randomCount = (int) (Math.random() * clauses.size());
		int countUnsat = 0;
		for (Clause c : clauses) {
			boolean satisfied = c.isSatisfied(assignment.get(c.var1),
					assignment.get(c.var2));
			if (!satisfied) {
				unsatClause = c;
				countUnsat++;
				if (randomCount == countUnsat)
					break;
			}
		}
		return unsatClause;
	}

	public final class Clause {
		private int var1, var2;

		public Clause(int var1, int var2) {
			this.var1 = var1;
			this.var2 = var2;
		}

		public int getVar1() {
			return var1;
		}

		public int getVar2() {
			return var2;
		}

		@Override
		public String toString() {
			return var1 + " + " + var2;
		}

		public boolean isSatisfied(boolean v1, boolean v2) {
			return (var1 > 0 ? v1 : !v1) || (var2 > 0 ? v2 : !v2);
		}

		@Override
		public int hashCode() {
			// since ordering is not important
			return var1 + var2;
		}

		@Override
		public boolean equals(Object o) {
			if (o != null && o instanceof Clause) {
				Clause c = (Clause) o;
				return this.var1 == c.var1 && this.var2 == c.var2;
			}
			return false;
		}

		@Override
		public Clause clone() {
			return new Clause(var1, var2);
		}
	}

}
