package solver;

import java.util.List;

import sudoku.Board;
import sudoku.Move;

public class NakedTriple extends NakedGroup {
	@Override
	public List<Move> solve(Board board) {
		return solveNakedGroupAllSections(board, BoardUtil.allTriples(), BoardUtil.allSections());
	}
}
