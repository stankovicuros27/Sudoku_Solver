package sudoku;

import java.io.Serializable;
import java.util.List;

public interface Solver extends Serializable {
	List<Move> getMove();
}
