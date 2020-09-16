package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;
import sudoku.Move;
import sudoku.MoveOperation;

public class NakedSingle extends NakedGroup {
	@Override
	public List<Move> solve(Board board) {
		for (Single single : BoardUtil.allSingles()) {
			for (Field field : BoardUtil.allFields()) {
				if (board.isSolved(field.getRow(), field.getColumn()))
					continue;
				int value = single.getValues()[0];
				if (board.isPossible(field.getRow(), field.getColumn(), value)
						&& board.getPossibilitiesCount(field.getRow(), field.getColumn()) == 1) {
					List<Move> moves = new ArrayList<>();
					int row = field.getRow();
					int column = field.getColumn();
					BoardUtil.addClueMoves(moves, row, column, value);
					moves.add(new Move(row, column, value, MoveOperation.CONCLUSION,
							"Naked Single (" + (row) + ", " + (column) + ") : " + (value + 1)));
					moves.add(new Move(row, column, value, MoveOperation.WRITE,
							"Naked Single (" + (row) + ", " + (column) + ") : " + (value + 1)));
					if (SolverUros.isSolutionStep(moves))
						return moves;
				}
			}
		}
		return SolverUros.EMPTY_MOVES_LIST;
	}
}
