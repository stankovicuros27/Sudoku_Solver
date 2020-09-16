package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sudoku.Board;
import sudoku.Move;
import sudoku.MoveOperation;

public class BoardUtil {
	private static final List<Single> allSingles = initializeAllSingles();
	private static final List<Pair> allPairs = initializeAllPairs();
	private static final List<Triple> allTriples = initializeAllTriples();
	private static final List<Quad> allQuads = initializeAllQuads();
	private static final List<Group> allGroups = initializeAllGroups();
	private static final int values[] = generateValues();
	private static final List<Field> allFields = initializeAllFields();
	private static final List<Row> allRows = initializeAllRows();
	private static final List<Column> allColumns = initializeAllColumns();
	private static final List<Box> allBoxes = initializeAllBoxes();
	private static final List<Section> allSections = initializeAllSections();

	public static void addClueMoves(List<Move> moves, int row, int column, int value) {
		for (int cluerow = 0; cluerow < Board.DIMENSION; cluerow++) {
			if (cluerow == row)
				continue;
			moves.add(new Move(cluerow, column, value, MoveOperation.CLUE, "Clue row"));
		}
		for (int cluecolumn = 0; cluecolumn < Board.DIMENSION; cluecolumn++) {
			if (cluecolumn == column)
				continue;
			moves.add(new Move(row, cluecolumn, value, MoveOperation.CLUE, "Clue line"));
		}
		for (int x = Board.SIZE * (row / Board.SIZE); x < Board.SIZE * (row / Board.SIZE + 1); x++) {
			for (int y = Board.SIZE * (column / Board.SIZE); y < Board.SIZE * (column / Board.SIZE + 1); y++) {
				if (x == row && y == column)
					continue;
				moves.add(new Move(x, y, value, MoveOperation.CLUE, ""));
			}
		}
	}

	private static List<Box> initializeAllBoxes() {
		List<Box> boxes = new ArrayList<>();
		for (int boxY = 0; boxY < Board.SIZE; boxY++) {
			for (int boxX = 0; boxX < Board.SIZE; boxX++) {
				boxes.add(new Box(boxY, boxX));
			}
		}
		return boxes;
	}

	public static List<Box> allBoxes() {
		return allBoxes;
	}

	private static List<Column> initializeAllColumns() {
		List<Column> columns = new ArrayList<>();
		for (int columnIndex = 0; columnIndex < Board.DIMENSION; columnIndex++) {
			columns.add(new Column(columnIndex));
		}
		return columns;
	}

	public static List<Column> allColumns() {
		return allColumns;
	}

	private static List<Row> initializeAllRows() {
		List<Row> rows = new ArrayList<>();
		for (int rowIndex = 0; rowIndex < Board.DIMENSION; rowIndex++) {
			rows.add(new Row(rowIndex));
		}
		return rows;
	}

	public static List<Row> allRows() {
		return allRows;
	}

	private static List<Section> initializeAllSections() {
		List<Section> sections = new ArrayList<>();
		sections.addAll(allRows);
		sections.addAll(allColumns);
		sections.addAll(allBoxes);
		return sections;
	}

	public static List<Section> allSections() {
		return allSections;
	}

	private static List<Field> initializeAllFields() {
		List<Field> fields = new ArrayList<>();
		for (int row = 0; row < Board.DIMENSION; row++) {
			for (int column = 0; column < Board.DIMENSION; column++) {
				fields.add(new Field(row, column));
			}
		}
		return fields;
	}

	public static List<Field> allFields() {
		return allFields;
	}

	private static int[] generateValues() {
		int values[] = new int[Board.DIMENSION];
		for (int i = 0; i < Board.DIMENSION; i++) {
			values[i] = i;
		}
		return values;
	}

	public static int[] allValues() {
		return values;
	}

	public static boolean fieldHasUniqueCandidateInSection(Board board, Field validField, Section section, int value) {
		for (Field field : section.fields()) {
			if (!field.equals(validField) && board.isPossible(field.getRow(), field.getColumn(), value))
				return false;
		}
		return true;
	}

	public static boolean fieldInsideBox(Field field, Box box) {
		int boxY = field.getRow() / Board.SIZE;
		int boxX = field.getColumn() / Board.SIZE;
		return boxX == box.getBoxX() && boxY == box.getBoxY();
	}

	public static List<Row> rowsHavingCandidateInsideBox(Board board, Box box, int value) {
		List<Row> rowsWithCandidate = new ArrayList<>();
		for (Row row : BoardUtil.rowsInsideBox(box)) {
			for (Field field : row.fields()) {
				if (fieldInsideBox(field, box) && !board.isSolved(field.getRow(), field.getColumn())
						&& board.isPossible(field.getRow(), field.getColumn(), value)) {
					rowsWithCandidate.add(row);
				}
			}
		}
		return rowsWithCandidate;
	}

	public static List<Column> columnsHavingCandidateInsideBox(Board board, Box box, int value) {
		List<Column> columnsWithCandidate = new ArrayList<>();
		for (Column column : BoardUtil.columnsInsideBox(box)) {
			for (Field field : column.fields()) {
				if (fieldInsideBox(field, box) && !board.isSolved(field.getRow(), field.getColumn())
						&& board.isPossible(field.getRow(), field.getColumn(), value)) {
					columnsWithCandidate.add(column);
				}
			}
		}
		return columnsWithCandidate;
	}

	public static ArrayList<Integer> candidateRowsInsideBox(Board board, int row, int column, int value) {
		List<Integer> rowList = new ArrayList<>();
		int boxY = row / Board.SIZE;
		int boxX = column / Board.SIZE;
		for (int rowIterator = boxY * Board.SIZE; rowIterator < (boxY + 1) * Board.SIZE; rowIterator++) {
			for (int columnIterator = boxX * Board.SIZE; columnIterator < (boxX + 1) * Board.SIZE; columnIterator++) {
				if (board.isPossible(rowIterator, columnIterator, value)) {
					if (!rowList.contains(rowIterator))
						rowList.add(rowIterator);
				}
			}
		}
		return (ArrayList<Integer>) rowList;
	}

	public static ArrayList<Integer> candidateColumnsInsideBox(Board board, int row, int column, int value) {
		List<Integer> columnList = new ArrayList<>();
		int boxY = row / Board.SIZE;
		int boxX = column / Board.SIZE;
		for (int rowIterator = boxY * Board.SIZE; rowIterator < (boxY + 1) * Board.SIZE; rowIterator++) {
			for (int columnIterator = boxX * Board.SIZE; columnIterator < (boxX + 1) * Board.SIZE; columnIterator++) {
				if (board.isPossible(rowIterator, columnIterator, value)) {
					if (!columnList.contains(columnIterator))
						columnList.add(columnIterator);
				}
			}
		}
		return (ArrayList<Integer>) columnList;
	}

	public static ArrayList<Integer> getBoxesInsideColumnHavingValue(Board board, int column, int value) {
		ArrayList<Integer> boxes = new ArrayList<>();
		for (int rowIterator = 0; rowIterator < Board.DIMENSION; rowIterator++) {
			if (!board.isSolved(rowIterator, column) && board.isPossible(rowIterator, column, value)) {
				if (!boxes.contains(rowIterator / Board.SIZE))
					boxes.add(rowIterator / Board.SIZE);
			}
		}
		return boxes;
	}

	public static ArrayList<Integer> getBoxesInsideRowHavingValue(Board board, int row, int value) {
		ArrayList<Integer> boxes = new ArrayList<>();
		for (int columnIterator = 0; columnIterator < Board.DIMENSION; columnIterator++) {
			if (!board.isSolved(row, columnIterator) && board.isPossible(row, columnIterator, value)) {
				if (!boxes.contains(columnIterator / Board.SIZE))
					boxes.add(columnIterator / Board.SIZE);
			}
		}
		return boxes;
	}

	private static List<Single> initializeAllSingles() {
		List<Single> allSingles = new ArrayList<>();
		for (int i = 0; i < Board.DIMENSION; i++) {
			allSingles.add(new Single(i));
		}
		return allSingles;
	}

	public static List<Single> allSingles() {
		return allSingles;
	}

	private static List<Pair> initializeAllPairs() {
		List<Pair> allPairs = new ArrayList<>();
		for (int i = 0; i < Board.DIMENSION; i++) {
			for (int j = 0; j < Board.DIMENSION; j++) {
				if (i != j) {
					Pair pair = new Pair(i, j);
					if (!allPairs.contains(pair)) {
						allPairs.add(pair);
					}
				}
			}
		}
		return allPairs;
	}

	public static List<Pair> allPairs() {
		return allPairs;
	}

	private static List<Triple> initializeAllTriples() {
		List<Triple> allTriples = new ArrayList<>();
		for (int i = 0; i < Board.DIMENSION; i++) {
			for (int j = 0; j < Board.DIMENSION; j++) {
				for (int k = 0; k < Board.DIMENSION; k++) {
					if (i != j && i != k && j != k) {
						Triple triple = new Triple(i, j, k);
						if (!allTriples.contains(triple))
							allTriples.add(triple);
					}
				}
			}
		}
		return allTriples;
	}

	public static List<Triple> allTriples() {
		return allTriples;
	}

	private static List<Quad> initializeAllQuads() {
		List<Quad> allQuads = new ArrayList<>();
		for (int i = 0; i < Board.DIMENSION; i++) {
			for (int j = 0; j < Board.DIMENSION; j++) {
				for (int k = 0; k < Board.DIMENSION; k++) {
					for (int l = 0; l < Board.DIMENSION; l++) {
						if (i != j && i != k && i != l && j != k && j != l && k != l) {
							Quad quad = new Quad(i, j, k, l);
							if (!allQuads.contains(quad)) {
								allQuads.add(quad);
							}
						}
					}
				}
			}
		}
		return allQuads;
	}

	public static List<Quad> allQuads() {
		return allQuads;
	}

	private static List<Group> initializeAllGroups() {
		List<Group> allGroups = new ArrayList<>();
		allGroups.addAll(allSingles);
		allGroups.addAll(allPairs);
		allGroups.addAll(allTriples);
		allGroups.addAll(allQuads);
		return allGroups;
	}

	public static List<Group> allGroups() {
		return allGroups;
	}

	public static List<Row> rowsInsideBox(Box box) {
		List<Row> rows = new ArrayList<>();
		for (int row = box.getBoxY() * Board.SIZE; row < (box.getBoxY() + 1) * Board.SIZE; row++) {
			rows.add(new Row(row));
		}
		return rows;
	}

	public static List<Column> columnsInsideBox(Box box) {
		List<Column> columns = new ArrayList<>();
		for (int column = box.getBoxX() * Board.SIZE; column < (box.getBoxX() + 1) * Board.SIZE; column++) {
			columns.add(new Column(column));
		}
		return columns;
	}

	public static List<Field> fieldsInsideRow(int row) {
		List<Field> fields = new ArrayList<>();
		for (int column = 0; column < Board.DIMENSION; column++) {
			fields.add(new Field(row, column));
		}
		return fields;
	}

	public static List<Field> fieldsInsideColumn(int column) {
		List<Field> fields = new ArrayList<>();
		for (int row = 0; row < Board.DIMENSION; row++) {
			fields.add(new Field(row, column));
		}
		return fields;
	}

	public static List<Field> fieldsWithAllGroupValuesInsideSection(Board board, Group group,
			List<Field> fieldsInSection) {
		List<Field> fieldsWithValues = new ArrayList<>();
		for (Field field : fieldsInSection) {
			if (board.isSolved(field.getRow(), field.getColumn()))
				continue;
			boolean toInsert = true;
			for (int groupValue : group.getValues()) {
				if (!board.isPossible(field.getRow(), field.getColumn(), groupValue)) {
					toInsert = false;
					break;
				}
			}
			if (toInsert)
				fieldsWithValues.add(field);
		}
		return fieldsWithValues;
	}

	public static List<Field> fieldsWithOnlyGroupValuesInsideSection(Board board, Group group,
			List<Field> fieldsInSection) {
		List<Field> fieldWithOnlyValues = new ArrayList<>();
		for (Field field : fieldsInSection) {
			if (board.isSolved(field.getRow(), field.getColumn()))
				continue;
			boolean toInsert = true;
			boolean hasValue = false;
			for (int value : BoardUtil.allValues()) {
				if (board.isPossible(field.getRow(), field.getColumn(), value)) {
					if (group.contains(value)) {
						hasValue = true;
					} else {
						toInsert = false;
						break;
					}
				}
			}
			if (toInsert && hasValue)
				fieldWithOnlyValues.add(field);
		}
		return fieldWithOnlyValues;
	}

	public static List<Field> fieldsWithAnyGroupValuesInsideSection(Board board, Group group,
			List<Field> fieldsInSection) {
		List<Field> fieldsWithAnyValues = new ArrayList<>();
		Map<Integer, Boolean> map = new HashMap<>();
		for (int value : group.getValues())
			map.put(value, false);
		for (Field field : fieldsInSection) {
			if (board.isSolved(field.getRow(), field.getColumn()))
				continue;
			for (int value : group.getValues()) {
				if (board.isPossible(field.getRow(), field.getColumn(), value)) {
					map.put(value, true);
					if (!fieldsWithAnyValues.contains(field)) {
						fieldsWithAnyValues.add(field);
					}
				}
			}
		}
		if (map.containsValue(false))
			fieldsWithAnyValues.clear();
		return fieldsWithAnyValues;
	}

	public static Box getBoxByField(Field field) {
		int boxY = field.getRow() / Board.SIZE;
		int boxX = field.getColumn() / Board.SIZE;
		return new Box(boxY, boxX);
	}

	public static List<Box> boxesHavingCandidateInsideRow(Board board, Row row, int value) {
		List<Box> boxesWithValue = new ArrayList<>();
		for (Field field : row.fields()) {
			if (!board.isSolved(field.getRow(), field.getColumn())
					&& board.isPossible(field.getRow(), field.getColumn(), value)) {
				Box box = BoardUtil.getBoxByField(field);
				if (!boxesWithValue.contains(box))
					boxesWithValue.add(box);
			}
		}
		return boxesWithValue;
	}

	public static List<Box> boxesHavingCandidateInsideColumn(Board board, Column column, int value) {
		List<Box> boxesWithValue = new ArrayList<>();
		for (Field field : column.fields()) {
			if (!board.isSolved(field.getRow(), field.getColumn())
					&& board.isPossible(field.getRow(), field.getColumn(), value)) {
				Box box = BoardUtil.getBoxByField(field);
				if (!boxesWithValue.contains(box))
					boxesWithValue.add(box);
			}
		}
		return boxesWithValue;
	}

	public static List<Box> boxesInRow(Row row) {
		List<Box> boxesInRow = new ArrayList<>();
		int boxY = row.getRowIndex() / Board.SIZE;
		for (int boxX = 0; boxX < Board.SIZE; boxX++) {
			boxesInRow.add(new Box(boxY, boxX));
		}
		return boxesInRow;
	}

	public static List<Box> boxesInColumn(Column column) {
		List<Box> boxesInColumn = new ArrayList<>();
		int boxX = column.getColumnIndex() / Board.SIZE;
		for (int boxY = 0; boxY < Board.SIZE; boxY++) {
			boxesInColumn.add(new Box(boxY, boxX));
		}
		return boxesInColumn;
	}

	public static List<Move> removeValueFromSection(Board board, Section section, List<Field> validFields, int value) {
		for (Field field : section.fields()) {
			if (!validFields.contains(field) && !board.isSolved(field.getRow(), field.getColumn())
					&& board.isPossible(field.getRow(), field.getColumn(), value)) {
				List<Move> moves = new ArrayList<>();
				moves.add(new Move(field.getRow(), field.getColumn(), value, MoveOperation.CONCLUSION,
						"Remove Candidate (" + (field.getRow()) + ", " + (field.getColumn()) + ") : " + (value + 1)));
				moves.add(new Move(field.getRow(), field.getColumn(), value, MoveOperation.DISABLE,
						"Remove Candidate (" + (field.getRow()) + ", " + (field.getColumn()) + ") : " + (value + 1)));
				if (SolverUros.isSolutionStep(moves))
					return moves;
			}
		}
		return SolverUros.EMPTY_MOVES_LIST;
	}

	public static Field intersectionRowColumn(Row row, Column column) {
		return new Field(row.getRowIndex(), column.getColumnIndex());
	}

	public static boolean fieldsInSameBox(Board board, int row1, int column1, int row2, int column2) {
		return (row1 / Board.SIZE == row2 / Board.SIZE && column1 / Board.SIZE == column2 / Board.SIZE);
	}
}
