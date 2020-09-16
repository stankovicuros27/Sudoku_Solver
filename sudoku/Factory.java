package sudoku;

import solver.SolverUros;

public interface Factory {
	public static Solver getSolver(Board board) {
		return new SolverUros(board);
	}
}
