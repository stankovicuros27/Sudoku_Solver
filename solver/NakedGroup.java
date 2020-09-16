package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;
import sudoku.Move;
import sudoku.MoveOperation;

public abstract class NakedGroup implements PartialSolver {
		
	protected List<Move> solveNakedGroupSection(Board board, Group group, Section section, List<Field> validFields) {
		for (Field field : section.fields()) {
			if (validFields.contains(field) || board.isSolved(field.getRow(), field.getColumn())) continue;
			for (int value : group.getValues()) {
				if (board.isPossible(field.getRow(), field.getColumn(), value)) {
					List<Move> moves = new ArrayList<>();
					BoardUtil.addClueMoves(moves, field.getRow(), field.getColumn(), value);
					moves.add(new Move(field.getRow(), field.getColumn(), value, MoveOperation.CONCLUSION,
							"Naked " + group + " (" + (field.getRow()) + ", " + (field.getColumn()) + ") : " + (value + 1)));
					moves.add(new Move(field.getRow(), field.getColumn(), value, MoveOperation.DISABLE,
							"Naked " + group + " (" + (field.getRow()) + ", " + (field.getColumn()) + ") : " + (value + 1)));
					if (SolverUros.isSolutionStep(moves)) return moves;
				}
			}
		}
		return SolverUros.EMPTY_MOVES_LIST;
	}
	
	protected List<Move> solveNakedGroupAllSections(Board board, List<? extends Group> groups, List<Section> sections) {
		for (Group group : groups) {
			for (Section section : sections) {
				List<Field> fieldsWithOnlyGroupValues = BoardUtil.fieldsWithOnlyGroupValuesInsideSection(board, group,
						section.fields());
				if (fieldsWithOnlyGroupValues.size() == group.size()) {
					List<Move> moves = solveNakedGroupSection(board, group, section, fieldsWithOnlyGroupValues);
					if (SolverUros.isSolutionStep(moves))
						return moves;
				}
			}
		}
		return SolverUros.EMPTY_MOVES_LIST;
	}
}
