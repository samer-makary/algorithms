package coursera.algo003.assignments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import coursera.algo003.assignments.twosat.TwoSATDigraphSolver;
import coursera.algo003.assignments.twosat.TwoSATProblem;

public class TwoSATMain {

	public static void main(String[] args) throws IOException {
		int caseNum = 1;
		for (; caseNum <= 6; caseNum++) {
			System.gc();
			File f = new File("./data/2SAT/2sat" + caseNum + ".txt");
			long start = System.currentTimeMillis();
			System.out.println("Started solving for case file " + f.getName());
			TwoSATDigraphSolver solver = new TwoSATDigraphSolver(
					readProblemFrom(f));
			System.out.println("Problem digraph was created in "
					+ (System.currentTimeMillis() - start) / 1000 + " sec(s)");
			start = System.currentTimeMillis();
			solver.solve();
			System.out.println("2SAT solver finished in "
					+ (System.currentTimeMillis() - start) / 1000 + " sec(s)");
			start = System.currentTimeMillis();
			boolean satisfiable = solver.isSatisfiable();
			System.out.println("Verification that the problem is"
					+ (satisfiable ? " [" : " [NOT ") + "Satisfiable] took "
					+ (System.currentTimeMillis() - start) + " millisec(s)");
			System.out.println();
		}
	}

	private static TwoSATProblem readProblemFrom(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		int NM = Integer.parseInt(br.readLine());
		TwoSATProblem problem = new TwoSATProblem(NM, NM);
		for (int i = 0; i < NM; i++) {
			String[] clauseVars = br.readLine().split("\\s+");
			problem.addClause(Integer.parseInt(clauseVars[0]),
					Integer.parseInt(clauseVars[1]));
		}
		br.close();
		return problem;
	}

}
