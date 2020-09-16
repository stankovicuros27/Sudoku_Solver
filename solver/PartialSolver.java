package solver;

import java.util.List;

import sudoku.Board;
import sudoku.Move;

public interface PartialSolver {
   List<Move> solve(Board board);
}
