package sudoku;

enum Command {
   UNKNOWN("", ""),
   LOAD("Учитај", "Учитавање почетне позиције"),
   RESET("Ресетуј", "Враћање на почетну позицију"),
   SOLVE("Реши", "Решавање текуће позиције"),
   SOLVESTEP("Реши корак", "Решавање наредног корака"),
   HINT("Предлог", "Понуди предлог за решење следећег корака"),
   UNDO("Назад", "Врати се корак уназад");

   private String symbol;
   private String toolTip;

   Command(String symbol, String toolTip) {
      this.symbol = symbol;
      this.toolTip = toolTip;
   }

   static Command findBySymbol(String symbol) {
      for (Command c : Command.values()) {
         if (c.symbol.equalsIgnoreCase(symbol)) { return c; }
      }
      return UNKNOWN;
   }

   public String getSymbol() {
      return symbol;
   }

   public String getToolTip() {
      return toolTip;
   }
}
