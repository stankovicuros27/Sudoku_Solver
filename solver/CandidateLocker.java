package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;
import sudoku.Move;
import sudoku.MoveOperation;

public class CandidateLocker implements PartialSolver {
	
	@Override
	public List<Move> solve(Board board) {
		List<Move> moves = new ArrayList<>();
		moves.addAll(lockCandidatesByBoxes(board));
		if (SolverUros.isSolutionStep(moves))
			return moves;
		moves.addAll(lockCandidatesInBoxByColumn(board));
		if (SolverUros.isSolutionStep(moves))
			return moves;
		moves.addAll(lockCandidatesInBoxByRow(board));
		return moves;
	}

	private List<Move> lockCandidatesByBoxes(Board board) {
		List<Move> moves = new ArrayList<>();
		for (int boxY = 0; boxY < Board.SIZE; boxY++) {
			for (int boxX = 0; boxX < Board.SIZE; boxX++) {
				for (int value = 0; value < Board.DIMENSION; value++) {
					List<Integer> candidateRowsInsideBox = BoardUtil.candidateRowsInsideBox(board, boxY * Board.SIZE,
							boxX * Board.SIZE, value);
					List<Integer> candidateColumnsInsideBox = BoardUtil.candidateColumnsInsideBox(board,
							boxY * Board.SIZE, boxX * Board.SIZE, value);

					if (candidateRowsInsideBox.size() == 1) {
						moves.addAll(lockCandidateInRowByBox(board, candidateRowsInsideBox.get(0), boxX * Board.SIZE,
								value));
						if (SolverUros.isSolutionStep(moves))
							return moves;
					}
					if (candidateColumnsInsideBox.size() == 1) {
						moves.addAll(lockCandidateInColumnByBox(board, boxY * Board.SIZE,
								candidateColumnsInsideBox.get(0), value));
						if (SolverUros.isSolutionStep(moves))
							return moves;
					}
				}
			}
		}
		return moves;
	}

	private List<Move> lockCandidateInRowByBox(Board board, int row, int boxColumn, int value) {
		List<Move> moves = new ArrayList<>();
		for (int columnIterator = 0; columnIterator < Board.DIMENSION; columnIterator++) {
			if (!BoardUtil.fieldsInSameBox(board, row, columnIterator, row, boxColumn)) {
				if (!board.isSolved(row, columnIterator) && board.isPossible(row, columnIterator, value)) {
					BoardUtil.addClueMoves(moves, row, columnIterator, value);
					moves.add(new Move(row, columnIterator, value, MoveOperation.CONCLUSION,
							"Lock Candidate (" + (row) + ", " + (columnIterator) + ") : " + (value + 1)));
					moves.add(new Move(row, columnIterator, value, MoveOperation.DISABLE,
							"Locked Candidate in Row by Box (" + (row) + ", " + (columnIterator) + ") : "
									+ (value + 1)));
					if (SolverUros.isSolutionStep(moves))
						return moves;
				}
			}
		}
		return moves;
	}

	private List<Move> lockCandidateInColumnByBox(Board board, int boxRow, int column, int value) {
		List<Move> moves = new ArrayList<>();
		for (int rowIterator = 0; rowIterator < Board.DIMENSION; rowIterator++) {
			if (!BoardUtil.fieldsInSameBox(board, rowIterator, column, boxRow, column)) {
				if (!board.isSolved(rowIterator, column) && board.isPossible(rowIterator, column, value)) {
					BoardUtil.addClueMoves(moves, rowIterator, column, value);
					moves.add(new Move(rowIterator, column, value, MoveOperation.CONCLUSION,
							"Lock Candidate (" + (rowIterator) + ", " + (column) + ") : " + (value + 1)));
					moves.add(new Move(rowIterator, column, value, MoveOperation.DISABLE,
							"Locked Candidate in Column by Box (" + (rowIterator) + ", " + (column) + ") : "
									+ (value + 1)));
					if (SolverUros.isSolutionStep(moves))
						return moves;
				}
			}
		}
		return moves;
	}

	private List<Move> lockCandidatesInBoxByColumn(Board board) {
		List<Move> moves = new ArrayList<>();
		for (int columnIterator = 0; columnIterator < Board.DIMENSION; columnIterator++) {
			for (int value = 0; value < Board.DIMENSION; value++) {
				List<Integer> boxes = BoardUtil.getBoxesInsideColumnHavingValue(board, columnIterator, value);
				if (boxes.size() == 1) {
					int boxY = boxes.get(0);
					moves.addAll(lockCandidateInBoxByColumn(board, boxY, columnIterator, value));
					if (SolverUros.isSolutionStep(moves))
						return moves;
				}
			}
		}
		return moves;
	}

	private List<Move> lockCandidateInBoxByColumn(Board board, int boxY, int column, int value) {
		int boxX = column / Board.SIZE;
		List<Move> moves = new ArrayList<>();
		for (int rowIterator = boxY * Board.SIZE; rowIterator < (boxY + 1) * Board.SIZE; rowIterator++) {
			for (int columnIterator = boxX * Board.SIZE; columnIterator < (boxX + 1) * Board.SIZE; columnIterator++) {
				if (columnIterator != column && !board.isSolved(rowIterator, columnIterator)
						&& board.isPossible(rowIterator, columnIterator, value)) {
					BoardUtil.addClueMoves(moves, rowIterator, columnIterator, value);
					moves.add(new Move(rowIterator, columnIterator, value, MoveOperation.CONCLUSION,
							"Lock Candidate (" + (rowIterator) + ", " + (columnIterator) + ") : " + (value + 1)));
					moves.add(new Move(rowIterator, columnIterator, value, MoveOperation.DISABLE,
							"Locked Candidate in Box by Column (" + (rowIterator) + ", " + (columnIterator) + ") : "
									+ (value + 1)));
					if (SolverUros.isSolutionStep(moves))
						return moves;
				}
			}
		}
		return moves;
	}

	private List<Move> lockCandidatesInBoxByRow(Board board) {
		List<Move> moves = new ArrayList<>();
		for (int rowIterator = 0; rowIterator < Board.DIMENSION; rowIterator++) {
			for (int value = 0; value < Board.DIMENSION; value++) {
				List<Integer> boxes = BoardUtil.getBoxesInsideRowHavingValue(board, rowIterator, value);
				if (boxes.size() == 1) {
					int boxX = boxes.get(0);
					moves.addAll(lockCandidateInBoxByRow(board, boxX, rowIterator, value));
					if (SolverUros.isSolutionStep(moves))
						return moves;
				}
			}
		}
		return moves;
	}

	private List<Move> lockCandidateInBoxByRow(Board board, int boxX, int row, int value) {
		int boxY = row / Board.SIZE;
		List<Move> moves = new ArrayList<>();
		for (int rowIterator = boxY * Board.SIZE; rowIterator < (boxY + 1) * Board.SIZE; rowIterator++) {
			for (int columnIterator = boxX * Board.SIZE; columnIterator < (boxX + 1) * Board.SIZE; columnIterator++) {
				if (rowIterator != row && !board.isSolved(rowIterator, columnIterator)
						&& board.isPossible(rowIterator, columnIterator, value)) {
					BoardUtil.addClueMoves(moves, rowIterator, columnIterator, value);
					moves.add(new Move(rowIterator, columnIterator, value, MoveOperation.CONCLUSION,
							"Lock Candidate (" + (rowIterator) + ", " + (columnIterator) + ") : " + (value + 1)));
					moves.add(new Move(rowIterator, columnIterator, value, MoveOperation.DISABLE,
							"Locked Candidate in Box by Row (" + (rowIterator) + ", " + (columnIterator) + ") : "
									+ (value + 1)));
					if (SolverUros.isSolutionStep(moves))
						return moves;
				}
			}
		}
		return moves;
	}
}
