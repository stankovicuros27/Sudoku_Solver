package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;
import sudoku.Move;

public class XWing implements PartialSolver {

	@Override
	public List<Move> solve(Board board) {
		return solveXWing(board);
	}

	private List<Move> solveXWing(Board board) {
		for (Row row1 : BoardUtil.allRows()) {
			for (Row row2 : BoardUtil.allRows()) {
				for (Column column1 : BoardUtil.allColumns()) {
					for (Column column2 : BoardUtil.allColumns()) {
						if (!row1.equals(row2) && !column1.equals(column2)) {
							List<Field> intersectionFields = new ArrayList<>();
							intersectionFields.add(BoardUtil.intersectionRowColumn(row1, column1));
							intersectionFields.add(BoardUtil.intersectionRowColumn(row1, column2));
							intersectionFields.add(BoardUtil.intersectionRowColumn(row2, column1));
							intersectionFields.add(BoardUtil.intersectionRowColumn(row2, column2));
							List<Integer> commonCandidates = commonCandidates(board, intersectionFields);
							for (int value : commonCandidates) {
								if (uniqueCandidatesInSections(board, value, row1, row2)) {
									List<Move> moves = new ArrayList<>();
									moves.addAll(BoardUtil.removeValueFromSection(board, column1, intersectionFields, value));
									moves.addAll(BoardUtil.removeValueFromSection(board, column2, intersectionFields, value));		
									if (SolverUros.isSolutionStep(moves))
										return moves;
								} 
								if (uniqueCandidatesInSections(board, value, column1, column2)) {
									List<Move> moves = new ArrayList<>();
									moves.addAll(BoardUtil.removeValueFromSection(board, row1, intersectionFields, value));
									moves.addAll(BoardUtil.removeValueFromSection(board, row2, intersectionFields, value));		
									if (SolverUros.isSolutionStep(moves))
										return moves;
								}
							}
						}
					}
				}
			}
		}
		return SolverUros.EMPTY_MOVES_LIST;
	}

	private List<Integer> commonCandidates(Board board, List<Field> fields) {
		List<Integer> commonCandidates = new ArrayList<>();
		for (int value : BoardUtil.allValues()) {
			boolean common = true;
			for (Field field : fields) {
				if (board.isSolved(field.getRow(), field.getColumn())
						|| !board.isPossible(field.getRow(), field.getColumn(), value)) {
					common = false;
					break;
				}
			}
			if (common) commonCandidates.add(value);
		}
		return commonCandidates;
	}
	
	private boolean uniqueCandidatesInSections(Board board, int value, Section section1, Section section2) {
		boolean ret = true;
		int cnt1 = 0, cnt2 = 0;
		for (Field field : section1.fields()) {
			if (!board.isSolved(field.getRow(), field.getColumn()) && board.isPossible(field.getRow(), field.getColumn(), value)) {
				if (++cnt1 > 2) {
					ret = false;
					break;
				}
			}
		}
		for (Field field : section2.fields()) {
			if (!board.isSolved(field.getRow(), field.getColumn()) && board.isPossible(field.getRow(), field.getColumn(), value)) {
				if (++cnt2 > 2) {
					ret = false;
					break;
				}
			}
		}
		return ret;
	}
}
