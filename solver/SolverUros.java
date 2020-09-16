package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sudoku.Board;
import sudoku.Move;
import sudoku.Solver;

public class SolverUros implements Solver {
	private static final long serialVersionUID = 1L;

	public static final List<Move> EMPTY_MOVES_LIST = new ArrayList<>();
	private Board board;
	private List<PartialSolver> solvers = Arrays.asList(new NakedSingle(), new HiddenSingle(), new NakedPair(),
			new CandidateLocker(), new NakedTriple(), new NakedQuad(), new HiddenPair(), new HiddenTriple(),
			new HiddenQuad(), new XWing(), new SwordFish());

	public SolverUros(Board board) {
		this.board = board;
	}

	@Override
	public List<Move> getMove() {
		for (PartialSolver solver : solvers) {
			List<Move> moves = solver.solve(board);
			if (isSolutionStep(moves))
				return moves;
		}
		return EMPTY_MOVES_LIST;
	}

	static boolean isSolutionStep(List<Move> moves) {
		return moves != null && !moves.isEmpty();
	}
}
