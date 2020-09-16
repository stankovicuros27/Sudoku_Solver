package sudoku;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import sudoku.exceptions.InvalidIndexException;

/**
 * Model sudoku table
 */
public class Board implements Serializable {
   private static final long serialVersionUID = 1L;
   private Logger logger = new Logger(); 
   private static final String EOL = "\n";

   public static final int SIZE = 3;
   public static final int DIMENSION = SIZE*SIZE;
   public int getDimension() { return DIMENSION; }
   private Field[][] field;

   private enum FieldStatus {
      SOLVED,
      UNSOLVED;
   }

   /**
    * Sudoku polje
    */
   private class Field {
      int row;
      int column;
      int solution = -1;
      Color backgroundColor = null;
      FieldStatus status = FieldStatus.UNSOLVED;
      boolean[] possible = new boolean[DIMENSION];
      int cntPossibilities = DIMENSION;

      Field(int row, int column) {
         this.row = row;
         this.column = column;
         reset();
      }

      void reset() {
         solution = -1;
         status = FieldStatus.UNSOLVED;
         cntPossibilities = DIMENSION;
         IntStream.range(0, DIMENSION).forEach(i -> possible[i] = true);
      }

      boolean isSolved() {
         return status.equals(FieldStatus.SOLVED);
      }

      /**
       * U polje upisuje sadrzaj value
       * @param value
       */
      private void set(int value) {
         solution = value;
         status = FieldStatus.SOLVED;
         IntStream.range(0, DIMENSION).forEach(this::disable);
         enable(value);
      }

      private void setColor(Color color) {
         backgroundColor = color;
      }

      /**
       * U polju omogucuje upis sadrzaja value
       * @param value
       */
      private void enable(int value) {
         if ( !possible[value] ) {
            possible[value] = true;
            cntPossibilities++;
         }
      }

      /**
       * U polju onemogucuje upis sadrzaja value
       * @param value
       */
      private void disable(int value) {
         if ( possible[value] ) {
            possible[value] = false;
            cntPossibilities--;
         }
      }

      @Override
      public String toString() {
         return String.format("(%d, %d) : %s", row, column, isSolved() ? ""+(solution+1): "x ("+cntPossibilities+")");
      }
   }

   Board() {
      field = new Field[DIMENSION][DIMENSION];
      for ( int row=0 ; row<DIMENSION ; row++ ) {
         for ( int column=0 ; column<DIMENSION ; column++ ) {
            field[row][column] = new Field(row, column);
         }
      }
      reset();
      load(lastTaskPosition);
   }

   @SuppressWarnings("unused")
   private void printBoard() {
      for ( int row=0 ; row<Board.DIMENSION ; row++ ) {
         StringBuilder text = new StringBuilder("");
         for ( int column=0 ; column<Board.DIMENSION ; column++ ) {
            text.append(isSolved(row, column) ? (getSolution(row, column)+1) : ".");
         }         
         logger.log(text.toString());
      }
   }
   
   private boolean isSolutionStep(List<Move> moves) {
      return moves.stream().anyMatch(move -> !move.getOperation().isHintOperation());
   }
   
   public boolean solve(Solver solver) {
      long solvingSteps = 0L;
      while ( !isSolved() && solvingSteps<DIMENSION*DIMENSION*DIMENSION ) {
         solvingSteps++;
         List<Move> moves = solver.getMove();
         if ( moves==null || moves.isEmpty() || !isSolutionStep(moves) ) {
            break;
         }

         for ( Move move : moves ) {
            switch (move.getOperation()) {
               case WRITE:
                  set(move.getRow(), move.getColumn(), move.getValue());
                  break;
               case DISABLE:
                  disable(move.getRow(), move.getColumn(), move.getValue());
                  break;
               case CLUE:
               case CONCLUSION:
                  break;
            }
         }
      }

      return isSolved();
   }

   private String lastTaskPosition = "xxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\nxxxxxxxxx\n";

   public List<Move> load() {
      return load(lastTaskPosition);
   }

   /**
    * Ucitava sadrzaj fajla u kome se nalazi opis pocetne pozicije sudoku problema
    * @param file Ime fajla
    */
   public List<Move> load(File file) {
      if (file == null) return null;

      try {
         BufferedReader in = new BufferedReader(new FileReader(file));
         StringBuilder s = new StringBuilder("");

         String inputLine;
         while ((inputLine = in.readLine()) != null) {
            s.append(inputLine + EOL);
         }
         in.close();
         return load(s.toString());
      } catch (IOException ioe) {
         return null;
      }
   }

   /**
    * Ucitava pocetnu poziciju zadatka
    * @param text String koji definise pocetnu poziciju<br>
    * Svaki red je predstavljen nizom brojeva (od 1 do 9) ili znacima 'x' (tamo gde je polje nepopunjeno)<br>
    * Redovi su medjusobno razdvojeni EOL simbolom
    */
   public List<Move> load(String taskPosition) {
      lastTaskPosition = taskPosition;
      String[] rowData = taskPosition.split("[\n]");
      reset();

      List<Move> moves = new ArrayList<>();
      
      char minValue = '0';
      char maxValue = '9';      
      int currRow = 0;
      for ( int row=0 ; row<rowData.length ; row++ ) {
         boolean numberFound = false;
         int currColumn = -1;
         for ( int index=0 ; index<rowData[row].length() ; index++ ) {
            char c = rowData[row].charAt(index);
            if ( (c>=minValue && c<=maxValue) || c=='x' || c=='X' || c=='.' ) {
               currColumn++;
               numberFound = true;
            }
            if ( c<minValue || c>maxValue ) continue;

            try {
               int number = Integer.parseInt(rowData[row].substring(index, index+1));
               moves.add(new Move(currRow, currColumn, number-1, MoveOperation.WRITE, ""));
            } catch (NumberFormatException e) {
               // do nothing
            }
         }
         if ( numberFound ) currRow++;
      }
      return moves;
   }

   /**
    * Resetuje stanje na tabli
    */
   public void reset() {
      for ( int row=0 ; row<DIMENSION ; row++ ) {
         for ( int column=0 ; column<DIMENSION ; column++ ) {
            field[row][column].reset();
         }
      }
   }

   private void validateCoordinates (int rowIndex, int columnIndex) throws InvalidIndexException {
      if ( rowIndex<0 || rowIndex>=DIMENSION ) throw new InvalidIndexException();
      if ( columnIndex<0 || columnIndex>=DIMENSION ) throw new InvalidIndexException();
   }

   private Field getField(int row, int column) throws InvalidIndexException {
      validateCoordinates(row, column);
      return field[row][column];
   }

   Color getFieldColor(int row, int column) {
      try {
         return getField(row, column).backgroundColor;
      } catch (InvalidIndexException e) {
         return null;
      }
   }

   public void setColor (int row, int column, Color color) {
      try {
         getField(row, column).setColor(color);
      } catch (InvalidIndexException e) {
         // do nothing
      }
   }

   void resetColors() {
      for ( int row=0 ; row<DIMENSION ; row++ ) {
         for ( int column=0 ; column<DIMENSION ; column++ ) {
            setColor(row, column, null);
         }
      }
   }

   /**
    * Upisuje sadrzaj 'value' u polje sa koordinatama (row, column)
    * @param row x-coordinata polja u koje se vrsi upis
    * @param column y-coordinata polja u koje se vrsi upis
    * @param value Vrednost koja se upisuje
    */
   public void set (int row, int column, int value) {
      try {
         getField(row, column).set(value);
         for ( int index=0 ; index<DIMENSION ; index++ ) getField(row, index).disable(value);
         for ( int index=0 ; index<DIMENSION ; index++ ) getField(index, column).disable(value);
         int startx = 3*(row/3);
         int startY = 3*(column/3);
         for ( int i=0 ; i<SIZE ; i++ ) { 
            for ( int j=0 ; j<SIZE ; j++ ) {
               getField(startx+i, startY+j).disable(value);
            }
         }
      } catch (InvalidIndexException e) {
         // do nothing
      }	
   }

   /**
    * Vraca informaciju da li je reseno polje sa koordinatama (row, column)
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    */
   public boolean isSolved(int row, int column) {
      return field[row][column].isSolved();
   }

   /**
    * Vraca informaciju da li je zadatak resen
    */
   public boolean isSolved() {
      for ( int row=0 ; row<Board.DIMENSION ; row++ ) {
         for ( int column=0 ; column<Board.DIMENSION ; column++ ) {
            if ( !isSolved(row, column) ) {
//               System.out.println("not solved");
               return false;
            }
         }
      }
//      System.out.println("solved");
      return true;
   }

   /**
    * Vraca broj koji je resenje polja sa koordinatama (row, column)
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    */
   public int getSolution(int row, int column) {
      return field[row][column].solution;
   }

   /**
    * Vraca informaciju da li je u polju sa koordinatama (row, column) jos uvek moguce upisati broj value
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    * @param value Vrednost
    */
   public boolean isPossible(int row, int column, int value) {
      return field[row][column].possible[value];
   }

   /**
    * Vraca broj preostalih mogucnosti u polju sa koordinatama (row, column)
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    */
   public int getPossibilitiesCount(int row, int column) {
      return field[row][column].cntPossibilities;
   }

   /**
    * U polju sa koordinatama (row, column) onemogucava upis broj value
    * @param row x-coordinata polja
    * @param column y-coordinata polja
    * @param value Vrednost
    */
   public void disable(int row, int column, int value) {
      field[row][column].disable(value);
   }

   private int[] solutionCounter = new int[Board.DIMENSION];
   private void resetSolutionCounter() {
      for ( int i=0 ; i<Board.DIMENSION ; i++ ) {
         solutionCounter[i] = 0;
      }
   }
   private boolean isPartialSolutionGood() {
      for ( int i=0 ; i<Board.DIMENSION ; i++ ) {
         if ( solutionCounter[i]!=1 ) {
            return false;
         }
      }
      return true;
   }

   /**
    * Vraca informaciju da li je zadatak resen
    */
   public boolean isSolutionGood() {
      for ( int row=0 ; row<Board.DIMENSION ; row++ ) {
         resetSolutionCounter();
         for ( int column=0 ; column<Board.DIMENSION ; column++ ) {
            solutionCounter[getSolution(row, column)]++;
         }
         if ( !isPartialSolutionGood() ) return false;
      }
      for ( int column=0 ; column<Board.DIMENSION ; column++ ) {
         resetSolutionCounter();
         for ( int row=0 ; row<Board.DIMENSION ; row++ ) {
            solutionCounter[getSolution(row, column)]++;
         }
         if ( !isPartialSolutionGood() ) return false;
      }
      for ( int block=0 ; block<Board.DIMENSION ; block++ ) {
         resetSolutionCounter();
         for ( int row=Board.SIZE * (block / Board.SIZE) ; row<Board.SIZE * ((block / Board.SIZE) + 1) ; row++ ) {
            for ( int column=Board.SIZE * (block % Board.SIZE) ; column<Board.SIZE * ((block % Board.SIZE) + 1) ; column++ ) {
               solutionCounter[getSolution(row, column)]++;
            }
         }
         if ( !isPartialSolutionGood() ) return false;
      }
      return true;
   }
   
   // MOJ KOD ------------------------------------------------------------------------------------
   

}
