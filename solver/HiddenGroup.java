package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;
import sudoku.Move;
import sudoku.MoveOperation;

public abstract class HiddenGroup implements PartialSolver {
	protected List<Move> solveHiddenGroupFields(Board board, Group group, List<Field> fields) {
		for (Field field : fields) {
			if (board.isSolved(field.getRow(), field.getColumn())) continue;
			for (int value : BoardUtil.allValues()) {
				if (board.isPossible(field.getRow(), field.getColumn(), value) && !group.contains(value)) {
					List<Move> moves = new ArrayList<>();
					BoardUtil.addClueMoves(moves, field.getRow(), field.getColumn(), value);
					moves.add(new Move(field.getRow(), field.getColumn(), value, MoveOperation.CONCLUSION,
							"Hidden " + group + " (" + (field.getRow()) + ", " + (field.getColumn()) + ") : " + (value + 1)));
					moves.add(new Move(field.getRow(), field.getColumn(), value, MoveOperation.DISABLE,
							"Hidden " + group + " (" + (field.getRow()) + ", " + (field.getColumn()) + ") : " + (value + 1)));
					if (SolverUros.isSolutionStep(moves)) return moves;
				}
			}
		}
		return SolverUros.EMPTY_MOVES_LIST;
	}
	
	protected List<Move> solveHiddenGroupAllSections(Board board, List<? extends Group> groups, List<Section> sections) {
		for (Group group : groups) {
			for (Section section : sections) {
				List<Field> fieldsWithAnyPairValues = BoardUtil.fieldsWithAnyGroupValuesInsideSection(board, group, section.fields());
				if (fieldsWithAnyPairValues.size() == group.size()) {
					List<Move> moves = solveHiddenGroupFields(board, group, fieldsWithAnyPairValues);
					if (SolverUros.isSolutionStep(moves))
						return moves;
				}
			}
		}
		return SolverUros.EMPTY_MOVES_LIST;
	}
}
