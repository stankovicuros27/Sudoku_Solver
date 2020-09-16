package solver;

import java.util.List;

import sudoku.Board;
import sudoku.Move;

public class NakedPair extends NakedGroup {
	@Override
	public List<Move> solve(Board board) {
		return solveNakedGroupAllSections(board, BoardUtil.allPairs(), BoardUtil.allSections());
	}
}
