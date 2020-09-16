package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;

public class Column implements Section {
	private int columnIndex;
	private List<Field> fields;

	public Column(int columnIndex) {
		super();
		this.columnIndex = columnIndex;
		fields = initializeFields();
	}
	
	private List<Field> initializeFields() {
		List<Field> fields = new ArrayList<>();
		for (int row = 0; row < Board.DIMENSION; row++) {
			fields.add(new Field(row, columnIndex));
		}
		return fields;
	}
	
	//provera za ubacivanje djubreta preko setera...
	
	public int getColumnIndex() {
		return columnIndex;
	}

	@Override
	public List<Field> fields() {
		return fields;
	}
}
