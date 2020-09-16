package sudoku;

import java.util.List;

public class SolutionStep {
   List<Move> moves;
   
   SolutionStep (List<Move> moves) {
      this.moves = moves;
   }
}
