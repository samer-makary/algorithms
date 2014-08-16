package coursera.algo003.assignments.twosat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.graph.GraphDirected;
import coursera.algo003.assignments.twosat.TwoSATProblem.Clause;
import coursera.algo005.assignments.KosarajuSCCAlgorithm;

public class TwoSATDigraphSolver extends TwoSATSolver {

	private Map<Integer, Set<Integer>> sccs;
	private Map<Integer, Integer> varToVert;
	private GraphDirected digraph;

	public TwoSATDigraphSolver(TwoSATProblem problem) {
		super(problem);
		init();
	}

	private void init() {
		int numVerts = problem.getNumVars() << 1;
		varToVert = new HashMap<Integer, Integer>(numVerts);
		int counter = 0;
		for (Integer var : problem.getVariables()) {
			varToVert.put(var, counter);
			varToVert.put(-var, counter + 1);
			counter += 2;
		}
		digraph = new GraphDirected(numVerts);
		for (Clause c : problem.getClauses()) {
			int p = c.getVar1();
			int q = c.getVar2();
			digraph.addConnection(varToVert.get(-p), varToVert.get(q));
			digraph.addConnection(varToVert.get(-q), varToVert.get(p));
		}
	}

	public GraphDirected get2SATDigraph() {
		return this.digraph;
	}

	@Override
	public void solve() {
		KosarajuSCCAlgorithm kosaraju = new KosarajuSCCAlgorithm(digraph);
		sccs = kosaraju.extractSCCWithLeaders();
	}
	
	@Override
	public boolean isSatisfiable() {
		boolean sat = true;
		for (Set<Integer> scc : sccs.values()) {
			boolean valid = isSCCValid(scc);
			if (!valid) {
				sat = false;
				break;
			}
		}

		return sat;
	}
	
	@Override
	public Map<Integer, Boolean> getSolution() {
		throw new UnsupportedOperationException();
	}

	/**
	 * A SCC is valid unless it contains the vertices of both a variable and its
	 * negation. Hence, we can evaluate every SCC by sorting its vertices and
	 * check whether or not there exists 2 consecutive vertices a,b where "a" is
	 * even and "b" is equal to (a+1).
	 * 
	 * Note: this method is only valid as long as
	 * {@link TwoSATDigraphSolver#init()} function sets every variable to an
	 * even vertex ID and the negation of the variable to (1+the same even
	 * vertex ID).
	 * 
	 * @param scc
	 *            the Strongly-Connected-Component that will be validated.
	 * @return <code>true</code> if, for all variables, the SCC does <b>not</b>
	 *         contain both vertices of a variables and its negation,
	 *         <code>false</code> otherwise.
	 */
	private boolean isSCCValid(Set<Integer> scc) {
		List<Integer> verts = new ArrayList<Integer>(scc);
		Collections.sort(verts);
		for (int i = 0; i < verts.size() - 1; i++) {
			if (verts.get(i) % 2 == 0) {
				if (verts.get(i) + 1 == verts.get(i + 1))
					return false;
			}
		}
		return true;
	}

}
