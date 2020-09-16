package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;

public class Row implements Section {
	private int rowIndex;
	private List<Field> fields;

	public Row(int rowIndex) {
		super();
		this.rowIndex = rowIndex;
		fields = initializeFields();
	}

	private List<Field> initializeFields() {
		List<Field> fields = new ArrayList<>();
		for (int column = 0; column < Board.DIMENSION; column++) {
			fields.add(new Field(rowIndex, column));
		}
		return fields;
	}

	// provera za ubacivanje djubreta preko setera...

	
	
	@Override
	public List<Field> fields() {
		return fields;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	@Override
	public String toString() {
		return "Row [rowIndex=" + rowIndex + "]";
	}
}
