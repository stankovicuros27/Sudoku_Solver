package sudoku;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class CyclicSolver {
   private static Logger logger = new Logger(); 
   private static List<SolutionStep> solutionSteps = new ArrayList<>();
   private static Board board;
   
   private static class SSFilenameFilter implements FileFilter {
      @Override
      public boolean accept(File file) {
         String path = file.getAbsolutePath().toLowerCase();
         if ( path.endsWith("main.ss") ) return false;
         return path.endsWith(".ss");
      }
   }
   
   public static void main(String[] args) {
      board = new Board();
      int counter = 0;
      int success = 0;
      
      File currFolder = new File(".");
      File[] ssFile = currFolder.listFiles(new SSFilenameFilter());
      if ( ssFile==null ) {
    	  logger.log("No ss files.");
    	  return;
      }
      
      long start = System.currentTimeMillis();
      for ( File ss : ssFile ) {
         String answer;
         counter++;
         try {
            List<Move> moves = board.load(ss);
            solutionSteps.clear();
            solutionSteps.add(new SolutionStep(moves));
            playStep(0);
            
            boolean solved = board.solve(Factory.getSolver(board));
            answer = solved ? "Solved" : "UNSOLVED";
            if ( solved ) {
               if ( !board.isSolutionGood() ) {
                  logger.log("BAAAAAAAAAAAAAAAAAAD");
               }
               success++;
            }
         } catch (Exception e) {
            answer = "EXCEPTION";
         }
         logger.log(ss.getName() + " : " + answer);
      }
      long end = System.currentTimeMillis();
      logger.log(success + "/" + counter + " Vreme : " + ((end-start)/1000.0) + "s." );
   }
   
   static void playStep(int index) {
      List<Move> moves = solutionSteps.get(index).moves;

      for ( Move move : moves ) {
         switch (move.getOperation()) {
            case WRITE:
               board.set(move.getRow(), move.getColumn(), move.getValue());
               break;
            case DISABLE:
               board.disable(move.getRow(), move.getColumn(), move.getValue());
               break;
            case CLUE:
            case CONCLUSION:
               break;
         }
      }
   }
}
