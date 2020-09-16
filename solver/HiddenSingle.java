package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;
import sudoku.Move;
import sudoku.MoveOperation;

public class HiddenSingle extends HiddenGroup {
	@Override
	public List<Move> solve(Board board) {
		for (Section section : BoardUtil.allSections()) {
			for (Field field : section.fields()) {
				if (board.isSolved(field.getRow(), field.getColumn()))
					continue;
				for (int value : BoardUtil.allValues()) {
					if (BoardUtil.fieldHasUniqueCandidateInSection(board, field, section, value)
							&& board.isPossible(field.getRow(), field.getColumn(), value)) {
						List<Move> moves = new ArrayList<>();
						int row = field.getRow();
						int column = field.getColumn();
						BoardUtil.addClueMoves(moves, row, column, value);
						moves.add(new Move(row, column, value, MoveOperation.CONCLUSION,
								"Hidden Single (" + (row) + ", " + (column) + ") : " + (value + 1)));
						moves.add(new Move(row, column, value, MoveOperation.WRITE,
								"Hidden Single (" + (row) + ", " + (column) + ") : " + (value + 1)));
						if (SolverUros.isSolutionStep(moves))
							return moves;
					}
				}
			}
		}
		return SolverUros.EMPTY_MOVES_LIST;
	}
}
