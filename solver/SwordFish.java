package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;
import sudoku.Move;

public class SwordFish implements PartialSolver {

	@Override
	public List<Move> solve(Board board) {
		return solveSwordFish(board);
	}

	private List<Move> solveSwordFish(Board board) {
		for (Row row1 : BoardUtil.allRows()) {
			for (Row row2 : BoardUtil.allRows()) {
				for (Row row3 : BoardUtil.allRows()) {
					for (Column column1 : BoardUtil.allColumns()) {
						for (Column column2 : BoardUtil.allColumns()) {
							for (Column column3 : BoardUtil.allColumns()) {
								if (!row1.equals(row2) && !row1.equals(row3) && !row2.equals(row3)
										&& !column1.equals(column2) && !column1.equals(column3)
										&& !column2.equals(column3)) {
									List<Field> intersectionFields = new ArrayList<>();
									intersectionFields.add(BoardUtil.intersectionRowColumn(row1, column1));
									intersectionFields.add(BoardUtil.intersectionRowColumn(row1, column2));
									intersectionFields.add(BoardUtil.intersectionRowColumn(row1, column3));
									intersectionFields.add(BoardUtil.intersectionRowColumn(row2, column1));
									intersectionFields.add(BoardUtil.intersectionRowColumn(row2, column2));
									intersectionFields.add(BoardUtil.intersectionRowColumn(row2, column3));
									intersectionFields.add(BoardUtil.intersectionRowColumn(row3, column1));
									intersectionFields.add(BoardUtil.intersectionRowColumn(row3, column2));
									intersectionFields.add(BoardUtil.intersectionRowColumn(row3, column3));
									List<Row> rows = new ArrayList<>();
									rows.add(row1);
									rows.add(row2);
									rows.add(row3);
									List<Column> columns = new ArrayList<>();
									columns.add(column1);
									columns.add(column2);
									columns.add(column3);
									List<Integer> commonCandidates = commonCandidates(board, intersectionFields, rows,
											columns);

									for (int value : commonCandidates) {
										if (uniqueCandidatesInSections(board, value, row1, row2, row3,
												intersectionFields)) {
											List<Move> moves = new ArrayList<>();
											moves.addAll(BoardUtil.removeValueFromSection(board, column1,
													intersectionFields, value));
											moves.addAll(BoardUtil.removeValueFromSection(board, column2,
													intersectionFields, value));
											moves.addAll(BoardUtil.removeValueFromSection(board, column3,
													intersectionFields, value));
											if (SolverUros.isSolutionStep(moves))
												return moves;
										}
										if (uniqueCandidatesInSections(board, value, column1, column2, column3,
												intersectionFields)) {
											List<Move> moves = new ArrayList<>();
											moves.addAll(BoardUtil.removeValueFromSection(board, row1,
													intersectionFields, value));
											moves.addAll(BoardUtil.removeValueFromSection(board, row2,
													intersectionFields, value));
											moves.addAll(BoardUtil.removeValueFromSection(board, row3,
													intersectionFields, value));
											if (SolverUros.isSolutionStep(moves))
												return moves;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return SolverUros.EMPTY_MOVES_LIST;
	}

	private List<Integer> commonCandidates(Board board, List<Field> fields, List<Row> rows, List<Column> columns) {
		List<Integer> commonCandidates = new ArrayList<>();

		for (int value : BoardUtil.allValues()) {
			if (isCommonCandidate(board, fields, rows, columns, value)) {
				commonCandidates.add(value);
			}
		}
		
		return commonCandidates;
	}

	private boolean isCommonCandidate(Board board, List<Field> fields, List<Row> rows, List<Column> columns,
			int value) {

		for (Row row : rows) {
			int cnt = 0;
			for (Field field : fields) {
				if (!board.isSolved(field.getRow(), field.getColumn())
						&& board.isPossible(field.getRow(), field.getColumn(), value)
						&& row.getRowIndex() == field.getRow()) {
					cnt++;
				}
			}
			if (cnt < 2) return false;
		}
		
		for (Column column : columns) {
			int cnt = 0;
			for (Field field : fields) {
				if (!board.isSolved(field.getRow(), field.getColumn())
						&& board.isPossible(field.getRow(), field.getColumn(), value)
						&& column.getColumnIndex() == field.getColumn()) {
					cnt++;
				}
			}
			if (cnt < 2) return false;
		}

		return true;
	}

	private boolean uniqueCandidatesInSections(Board board, int value, Section section1, Section section2,
			Section section3, List<Field> validFields) {
		for (Field field : section1.fields()) {
			if (validFields.contains(field))
				continue;
			if (!board.isSolved(field.getRow(), field.getColumn())
					&& board.isPossible(field.getRow(), field.getColumn(), value)) {
				return false;
			}
		}
		for (Field field : section2.fields()) {
			if (validFields.contains(field))
				continue;
			if (!board.isSolved(field.getRow(), field.getColumn())
					&& board.isPossible(field.getRow(), field.getColumn(), value)) {
				return false;
			}
		}
		for (Field field : section3.fields()) {
			if (validFields.contains(field))
				continue;
			if (!board.isSolved(field.getRow(), field.getColumn())
					&& board.isPossible(field.getRow(), field.getColumn(), value)) {
				return false;
			}
		}
		return true;
	}
}
