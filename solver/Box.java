package solver;

import java.util.ArrayList;
import java.util.List;

import sudoku.Board;

public class Box implements Section {
	private int boxY, boxX;
	private List<Field> fields;

	
	public Box(int boxY, int boxX) {
		super();
		this.boxY = boxY;
		this.boxX = boxX;
		fields = initializeFields();
	}

	private List<Field> initializeFields() {
		List<Field> fields = new ArrayList<>();
		for (int row = boxY * Board.SIZE; row < (boxY + 1) * Board.SIZE; row++) {
			for (int column = boxX * Board.SIZE; column < (boxX + 1) * Board.SIZE; column++) {
				fields.add(new Field(row, column));
			}
		}
		return fields;
	}
	
	//provera za ubacivanje djubreta preko setera...

	@Override
	public List<Field> fields() {
		return fields;
	}

	public int getBoxY() {
		return boxY;
	}
	
	public int getBoxX() {
		return boxX;
	}
}
