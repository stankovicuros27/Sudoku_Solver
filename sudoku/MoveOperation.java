package sudoku;

import java.awt.Color;

public enum MoveOperation {
   /** Upisivanje konacnog resenja u polje tabele */
	WRITE (false, null),
	/** Izbacivanje mogucnosti da se neka vrednost upise u polje tabele */
	DISABLE (false, null),
	/** Farbanje polja koje posredno ucestvuje u formiranju zakljucka **/
	CLUE (true, new Color(255, 255, 166)),
	/** Farbanje polja na kojem se izvodi zakljucak **/
	CONCLUSION (true, new Color(166, 255, 166));
	
   /** Boja kojom treba ofarbati pozadinu polja, kada se prikazuje hint **/
   private Color backgroundColor = null;
   
   /** Indikator da li se radi o "hint"-operaciji ili i operaciji upisa **/
   private boolean hintOperation = false;
   
   MoveOperation(boolean hintOperation, Color backgroundColor) {
      this.hintOperation = hintOperation;
      this.backgroundColor = backgroundColor;
   }

   public Color getBackgroundColor() {
      return backgroundColor;
   }

   public boolean isHintOperation() {
      return hintOperation;
   }
}
