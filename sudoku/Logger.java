package sudoku;

import java.io.Serializable;

public class Logger implements Serializable {
   private static final long serialVersionUID = 1L;

//   public void logtext(Object message) {
//      System.out.print(message.toString());
//   }
   
   public void log(String message) {
      System.out.println(message);
   }
   
   public void log() {
      log("");
   }

   public void info(String message) {
      log(message);
   }

   public void debug(String message) {
      log(message);
   }

   public void error(String message) {
      log(message);
   }

   public void warn(String message) {
      log(message);
   }
}
