package solver;

import java.util.List;

import sudoku.Board;
import sudoku.Move;

public class HiddenTriple extends HiddenGroup {
	@Override
	public List<Move> solve(Board board) {
		return solveHiddenGroupAllSections(board, BoardUtil.allTriples(), BoardUtil.allSections());
	}
}
