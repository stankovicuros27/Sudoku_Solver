package sudoku;

/**
 * Potez, u resavanju sudoku problema
 */
public class Move {
	/** x-coordinata polja [0, Board.DIMENSION-1] */
	private int row;
	/** y-coordinata polja [0, Board.DIMENSION-1] */
	private int column;
	/** vrednost [0, Board.DIMENSION-1] */
	private int value;
	/** Prateci tekst (objasnjenje poteza) */
	private String text;
	/** Operacija koju potez sprovodi */
   private MoveOperation operation;

	public Move(int row, int column, int value, MoveOperation operation, String text) {
		this.row = row;
		this.column = column;
		this.value = value;
		this.operation = operation;
		this.text = text;
	}

	public int getRow() {
      return row;
   }

   public int getColumn() {
      return column;
   }

   public int getValue() {
      return value;
   }

   public String getText() {
      return text;
   }

   public MoveOperation getOperation() {
      return operation;
   }

   @Override
	public String toString() {
		return "(" + row + ", " + column + ") : " + operation + ":" + value + " : " + text;
	}
}
