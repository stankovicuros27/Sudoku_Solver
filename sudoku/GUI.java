package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Korisnicki interfejs
 */
public class GUI extends JFrame implements ActionListener {
   private static final long serialVersionUID = 1L;
   private static final int FRAME_WIDTH = 800;
   private static final int FRAME_HEIGHT = 700;
   private static final int FIELD_SIZE = 60;
   private static final int BUTTON_X_POSITION = 30;
   private static final int BUTTON_Y_POSITION = 20;
   private static final int BUTTON_WIDTH = 120;
   private static final int BUTTON_HEIGHT = 30;
   private Board board;
   private Solver solver;
   private List<Button> button = new ArrayList<>();
   private BoardPanel panelBoard;
   private JTextArea message;

   private List<SolutionStep> solutionSteps = new ArrayList<>();
   private int lastSolutionStep = 0;

   GUI() {
      super("Судоку");
      this.board = new Board();
      this.solver = Factory.getSolver(board);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setResizable(false);
      setLayout(null);

      for (Command c : Command.values()) {
         if (!c.equals(Command.UNKNOWN)) {
            Button b = new Button(c, this);
            b.setBounds(BUTTON_X_POSITION, BUTTON_Y_POSITION + button.size() * (BUTTON_HEIGHT + 20), BUTTON_WIDTH, BUTTON_HEIGHT);
            button.add(b);
            add(b);
         }
      }
      panelBoard = new BoardPanel();
      panelBoard.setBounds(BUTTON_X_POSITION + BUTTON_WIDTH + 30, BUTTON_Y_POSITION, FRAME_WIDTH, FRAME_HEIGHT);
      add(panelBoard);

      message = new JTextArea();
      message.setEditable(false);
      message.setLineWrap(true);
      message.setWrapStyleWord(true);
      message.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      int x = 180;
      int y = 590;
      int width = 550;
      int height = 60;
      JScrollPane scroller = new JScrollPane(message);
      scroller.setBounds(x, y, width, height);
      add(scroller);

      List<Move> moves = board.load(new File("main.ss"));
      if (moves != null && !moves.isEmpty()) {
         solutionSteps.clear();
         solutionSteps.add(new SolutionStep(moves));
         playStep(0);
         lastSolutionStep = 1;
      }

      setSize(FRAME_WIDTH, FRAME_HEIGHT);
      setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
      setVisible(true);
   }

   private class BoardPanel extends JPanel {
      private static final long serialVersionUID = 1L;

      BoardPanel() {
         super();
      }

      @Override
      public void paint(Graphics g) {
         // polja
         g.setColor(Color.BLACK);
         for (int row = 0; row < Board.DIMENSION; row++) {
            for (int column = 0; column < Board.DIMENSION; column++) {
               repaintField(g, row, column);
            }
         }

         // unutrasnje linije
         g.setColor(Color.LIGHT_GRAY);
         for (int i = 1; i < Board.DIMENSION; i++) {
            g.drawLine(i * (FIELD_SIZE + 1), 0, i * (FIELD_SIZE + 1), Board.DIMENSION * (FIELD_SIZE + 1));
         }
         for (int i = 1; i < Board.DIMENSION; i++) {
            g.drawLine(0, i * (FIELD_SIZE + 1), Board.DIMENSION * (FIELD_SIZE + 1), i * (FIELD_SIZE + 1));
         }

         // okvir table
         g.setColor(Color.BLACK);
         for (int delta = -1; delta <= 1; delta++) {
            for (int i = 0; i <= Board.SIZE; i++) {
               g.drawLine(0, 3 * i * (FIELD_SIZE + 1) + delta, Board.DIMENSION * (FIELD_SIZE + 1), 3 * i * (FIELD_SIZE + 1) + delta);
            }
         }
         for (int delta = -1; delta <= 1; delta++) {
            for (int i = 0; i <= Board.SIZE; i++) {
               g.drawLine(3 * i * (FIELD_SIZE + 1) + delta, 0, 3 * i * (FIELD_SIZE + 1) + delta, Board.DIMENSION * (FIELD_SIZE + 1));
            }
         }
      }

      private final Font fontNormal = new Font("Times New Roman", Font.BOLD, FIELD_SIZE / 2);
      private final Font fontSmall = new Font("Times New Roman", Font.PLAIN, 12);

      void repaintField(Graphics g, int row, int column) {
         FontMetrics fm;
         int xPosition = 1 + (FIELD_SIZE + 1) * column;
         int yPosition = 1 + (FIELD_SIZE + 1) * row;

         g.setColor(board.getFieldColor(row, column) != null ? board.getFieldColor(row, column) : Color.WHITE);
         g.fillRect(xPosition, yPosition, FIELD_SIZE, FIELD_SIZE);

         g.setColor(Color.BLUE);
         if (board.isSolved(row, column)) {
            g.setFont(fontNormal);
            fm = g.getFontMetrics();
            String text = "" + (board.getSolution(row, column) + 1);
            g.drawString(text, xPosition + (FIELD_SIZE - fm.stringWidth(text)) / 2, yPosition + fm.getHeight());
         } else {
            g.setFont(fontSmall);
            fm = g.getFontMetrics();
            for (int i = 0; i < Board.SIZE; i++) {
               for (int j = 0; j < Board.SIZE; j++) {
                  if (board.isPossible(row, column, i * Board.SIZE + j)) {
                     g.drawString("" + (i * Board.SIZE + j + 1), xPosition + 10 + (2 * j * FIELD_SIZE / 7), yPosition + 4 + fm.getHeight() + (2 * i * FIELD_SIZE / 7));
                  }
               }
            }
         }
      }
   }

   private class Button extends JButton {
      private static final long serialVersionUID = 1L;

      Button(Command command, ActionListener listener) {
         super();
         setActionCommand(command.getSymbol());
         setText(command.getSymbol());
         setToolTipText(command.getSymbol());
         addActionListener(listener);
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      String c = e.getActionCommand();
      Command command = Command.findBySymbol(c);
      if (command == null) return;

      List<Move> moves;
      board.resetColors();
      switch (command) {
         case RESET:
            board.reset();

            moves = board.load();
            solutionSteps.clear();
            solutionSteps.add(new SolutionStep(moves));
            playStep(0);
            lastSolutionStep = 1;

            message.setText("");
            repaint();
            break;

         case LOAD:
            moves = load();
            solutionSteps.clear();
            solutionSteps.add(new SolutionStep(moves));
            playStep(0);
            lastSolutionStep = 1;

            message.setText("");
            repaint();
            break;

         case HINT:
            setHint(solver.getMove());
            break;

         case SOLVESTEP:
            getNextMove(solver.getMove());
            break;

         case SOLVE:
            while (!board.isSolved() && getNextMove(solver.getMove())) {
            }
            break;

         case UNDO:
            if (lastSolutionStep > 1) {
               board.reset();
               lastSolutionStep--;

               for (int step = 0; step < lastSolutionStep; step++) {
                  playStep(step);
               }
               repaint();
            }
            break;

         case UNKNOWN:
            break;
      }
   }

   /**
    * Ucitava novi zadatak
    * 
    * @return
    */
   private List<Move> load() {
      final JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
      int value = fc.showOpenDialog(this);
      if (value == JFileChooser.APPROVE_OPTION) { return board.load(fc.getSelectedFile()); }
      return null;
   }

   void setHint(List<Move> moves) {
      _setHint(moves);
      repaint();
   }

   void _setHint(List<Move> moves) {
      if (moves == null || moves.isEmpty()) {
         message.setText("");
         return;
      }

      StringBuilder text = new StringBuilder("");
      for (Move move : moves) {
         if (!move.getOperation().isHintOperation()) continue;

         if (move.getOperation().getBackgroundColor() != null) {
            board.setColor(move.getRow(), move.getColumn(), move.getOperation().getBackgroundColor());
         }
         if (move.getText().length() > 0) {
            text.append(move.getText() + "\n");
         }
      }
      message.setText(text.toString());
   }

   boolean getNextMove(List<Move> moves) {
      boolean b = _getNextMove(moves);
      repaint();
      return b;
   }

   boolean _getNextMove(List<Move> moves) {
      if (moves == null || moves.isEmpty()) {
         message.setText("");
         return false;
      }

      while (solutionSteps.size() > lastSolutionStep) {
         solutionSteps.remove(lastSolutionStep);
      }
      solutionSteps.add(new SolutionStep(moves));
      playStep(lastSolutionStep);
      lastSolutionStep++;
      return true;
   }

   void playStep(int index) {
      List<Move> moves = solutionSteps.get(index).moves;
      if (moves == null) {
         message.setText("");
         return;
      }

      String text = "";
      for (Move move : moves) {
         switch (move.getOperation()) {
            case WRITE:
               board.set(move.getRow(), move.getColumn(), move.getValue());
               if (move.getText().length() > 0) text += move.getText() + "\n";
               break;
            case DISABLE:
               board.disable(move.getRow(), move.getColumn(), move.getValue());
               if (move.getText().length() > 0) text += move.getText() + "\n";
               break;
            case CLUE:
            case CONCLUSION:
               break;
         }
      }
      message.setText(text);
   }
}