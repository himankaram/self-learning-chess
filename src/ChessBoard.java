import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class ChessBoard extends JFrame implements ActionListener {
   private JButton[][] chessBoard;
   private Piece[][] pieces;
   private boolean pieceSelected;
   private String turn = "white";
   private int rowSelected,columnSelected;
   private int counter = 0;
   JMenuItem mnResign;
   Timer gametimer;
   int timeleft=600;
   boolean gameEnd = false;
   private String humanColor = "white";
   private String difficulty = "medium";
   private JTextArea chatLog;
   private boolean isAiThinking = false;

   public ChessBoard() {
       setSize(1150, 800);
       setTitle("Chess - Educational AI Coach Platform");

       chessBoard = new JButton[8][8];
       pieces = new Piece[8][8];
       pieceSelected = false;
       designChessBoard();
       gametimer = new Timer();

       JPanel boardPanel = new JPanel(new GridLayout(8, 8));

       for (int i = 0; i < chessBoard.length; i++) {
           for (int j = 0; j < chessBoard.length; j++) {
               chessBoard[i][j] = new JButton();
               if (pieces[i][j] != null) {
                   URL imgURL = getClass().getResource(pieces[i][j].getImageSource());
                   if (imgURL != null) {
                       ImageIcon icon = new ImageIcon(imgURL);
                       Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                       chessBoard[i][j].setIcon(new ImageIcon(img));
                   } else {
                   }
               } else {
                   chessBoard[i][j].setText("-");
               }

               boardPanel.add(chessBoard[i][j]);
               chessBoard[i][j].addActionListener(this);
           }
       }

       JPanel sidePanel = new JPanel(new BorderLayout());
       sidePanel.setBorder(BorderFactory.createTitledBorder("Educational Coach Console"));

       chatLog = new JTextArea("Coach: Welcome! Make a move and I'll explain what it does and what to look for next.\n\n");
       chatLog.setEditable(false);
       chatLog.setLineWrap(true);
       chatLog.setWrapStyleWord(true);
       chatLog.setFont(new Font("SansSerif", Font.PLAIN, 14));
       chatLog.setBackground(new Color(245, 245, 245));
       JScrollPane scrollPane = new JScrollPane(chatLog);

       String[] levels = { "Easy", "Medium", "Hard" };
       JComboBox<String> diffBox = new JComboBox<>(levels);
       diffBox.setSelectedItem("Medium");
       diffBox.addActionListener(e -> difficulty = diffBox.getSelectedItem().toString().toLowerCase());

       JButton hintButton = new JButton("Suggest a move");
       hintButton.addActionListener(e -> giveHintForHuman());

       JPanel topControls = new JPanel(new GridLayout(1, 2));
       topControls.add(diffBox);
       topControls.add(hintButton);

       sidePanel.add(topControls, BorderLayout.NORTH);
       sidePanel.add(scrollPane, BorderLayout.CENTER);

       JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardPanel, sidePanel);
       splitPane.setDividerLocation(780);
       getContentPane().add(splitPane);

       JMenuBar JMB = new JMenuBar();
       JMenu mnOption = new JMenu(" Option ");
       mnResign = new JMenuItem(" Resign ");
       mnResign.addActionListener(this);
       mnOption.add(mnResign);
       JMB.add(mnOption);
       setJMenuBar(JMB);

       setVisible(true);
       startTimer();
   }

   public void startTimer() {
	   gametimer= new Timer();
	   timeleft=300;
	   gametimer.scheduleAtFixedRate(new TimerTask() {
		   public void run() {
			   if (timeleft>=0) {
				   int minutes=timeleft/60;
				   int seconds=timeleft%60;
				   String time=minutes+"m and "+seconds+"s";
			   setTitle(turn+":"+time);
			   timeleft--; }
			   if (timeleft==0) {
				  if (turn.equals("white")) {
					  System.out.println("black wins");
					  gameEnd = true;
					  gametimer.cancel();
					  setTitle(turn+":"+timeleft);
					  showEndMessage();
				  }
				  else {
					  System.out.println("White wins");
					  gameEnd = true;
					  gametimer.cancel();
					  setTitle(turn+":"+timeleft);
					  showEndMessage();
				  }

			   }
		   }
	   },0,1000);

   }
   public void highlightLegalSquares(ArrayList<Location> movements) {
       clearBoardHighlights1();
       if (movements == null) return;

       for (Location loc : movements) {
           int r = loc.getRow();
           int c = loc.getColumn();

           if (r >= 0 && r < 8 && c >= 0 && c < 8) {
               chessBoard[r][c].setBackground(new Color(173, 216, 230));
               chessBoard[r][c].setContentAreaFilled(false);
               chessBoard[r][c].setOpaque(true);
           }
       }
   }

   public void clearBoardHighlights1() {
       for (int i = 0; i < 8; i++) {
           for (int j = 0; j < 8; j++) {
               chessBoard[i][j].setBackground(null);
               chessBoard[i][j].setContentAreaFilled(true);
           }
       }
   }

   public void showEndMessage() {
	   if (turn.equals("white") && gameEnd == true) {
		   JOptionPane.showMessageDialog(this, "black wins");
		   System.exit(0);
	   }
	   else if(turn.equals("black") && gameEnd == true) {
		   JOptionPane.showMessageDialog(this, "white wins");
		   System.exit(0);
	   }
   }

   public void designChessBoard() {
       pieces[0][0] = new Rook(new Location(0, 0), "white", "/images/white-rook.png");
       pieces[0][7] = new Rook(new Location(0, 7), "white", "/images/white-rook.png");
       pieces[0][1] = new Knight(new Location(0, 1), "white", "/images/white-knight.png");
       pieces[0][6] = new Knight(new Location(0, 6), "white", "/images/white-knight.png");
       pieces[0][2] = new Bishop(new Location(0, 2), "white", "/images/white-bishop.png");
       pieces[0][5] = new Bishop(new Location(0, 5), "white", "/images/white-bishop.png");
       pieces[0][3] = new Queen(new Location(0, 3), "white", "/images/white-queen.png");
       pieces[0][4] = new King(new Location(0, 4), "white", "/images/white-king.png");
       pieces[1][0] = new Pawn(new Location(1, 0), "white", "/images/white-pawn.png");
       pieces[1][1] = new Pawn(new Location(1, 1), "white", "/images/white-pawn.png");
       pieces[1][2] = new Pawn(new Location(1, 2), "white", "/images/white-pawn.png");
       pieces[1][3] = new Pawn(new Location(1, 3), "white", "/images/white-pawn.png");
       pieces[1][4] = new Pawn(new Location(1, 4), "white", "/images/white-pawn.png");
       pieces[1][5] = new Pawn(new Location(1, 5), "white", "/images/white-pawn.png");
       pieces[1][6] = new Pawn(new Location(1, 6), "white", "/images/white-pawn.png");
       pieces[1][7] = new Pawn(new Location(1, 7), "white", "/images/white-pawn.png");


       pieces[7][0] = new Rook(new Location(7, 0), "black", "/images/black-rook.png");
       pieces[7][7] = new Rook(new Location(7, 7), "black", "/images/black-rook.png");
       pieces[7][1] = new Knight(new Location(7, 1), "black", "/images/black-knight.png");
       pieces[7][6] = new Knight(new Location(7, 6), "black", "/images/black-knight.png");
       pieces[7][2] = new Bishop(new Location(7, 2), "black", "/images/black-bishop.png");
       pieces[7][5] = new Bishop(new Location(7, 5), "black", "/images/black-bishop.png");
       pieces[7][3] = new Queen(new Location(7, 3), "black", "/images/black-queen.png");
       pieces[7][4] = new King(new Location(7, 4), "black", "/images/black-king.png");
       pieces[6][0] = new Pawn(new Location(6, 0), "black", "/images/black-pawn.png");
       pieces[6][1] = new Pawn(new Location(6, 1), "black", "/images/black-pawn.png");
       pieces[6][2] = new Pawn(new Location(6, 2), "black", "/images/black-pawn.png");
       pieces[6][3] = new Pawn(new Location(6, 3), "black", "/images/black-pawn.png");
       pieces[6][4] = new Pawn(new Location(6, 4), "black", "/images/black-pawn.png");
       pieces[6][5] = new Pawn(new Location(6, 5), "black", "/images/black-pawn.png");
       pieces[6][6] = new Pawn(new Location(6, 6), "black", "/images/black-pawn.png");
       pieces[6][7] = new Pawn(new Location(6, 7), "black", "/images/black-pawn.png");

   }

   public void actionPerformed(ActionEvent ae) {
       if (ae.getSource() == mnResign) {
           if (turn.equals("white"))
               JOptionPane.showMessageDialog(this, "Black Wins");
           else {
               JOptionPane.showMessageDialog(this, "White Wins");
           }
           System.exit(0);
       }

       if (isAiThinking || !turn.equals(humanColor)) {
           return;
       }

       for (int i = 0; i <= 7; i++) {
           for (int j = 0; j <= 7; j++) {
               if (chessBoard[i][j] == ae.getSource()) {

                   if (pieceSelected == false && checkWinner() == false) {

                       if (pieces[i][j] != null && pieces[i][j].getColor().equals(turn)) {
                           pieceSelected = true;
                           rowSelected = i;
                           columnSelected = j;
                           pieces[rowSelected][columnSelected].calculateLegalMovements(pieces);
                           highlightLegalSquares(pieces[rowSelected][columnSelected].getLegalMovements());
                       }
                   }
                   else {

                       if (pieces[i][j] == null && checkWinner() == false) {
                           ArrayList<Location> legalMovements = pieces[rowSelected][columnSelected].getLegalMovements();

                           for (Location obj : legalMovements) {
                               if (obj.getRow() == i && obj.getColumn() == j) {
                                   if (pieces[rowSelected][columnSelected].getImageSource().indexOf("pawn") != -1) {
                                       counter = 0;
                                   }
                                   counter++;

                                   Piece movedPiece = pieces[rowSelected][columnSelected];
                                   int fromRow = rowSelected, fromCol = columnSelected;

                                   chessBoard[i][j].setText("");
                                   pieces[i][j] = pieces[rowSelected][columnSelected];
                                   pieces[i][j].getLocation().setRow(i);
                                   pieces[i][j].getLocation().setColumn(j);
                                   chessBoard[i][j].setIcon(chessBoard[rowSelected][columnSelected].getIcon());
                                   pieces[rowSelected][columnSelected] = null;
                                   chessBoard[rowSelected][columnSelected].setIcon(null);
                                   chessBoard[rowSelected][columnSelected].setText("-");

                                   clearBoardHighlights1();
                                   pieceSelected = false;

                                   giveMoveAdvice(movedPiece, null, fromRow, fromCol, i, j);

                                   checkWinner();
                                   if (turn.equals("white")) {
                                       boolean a = blackCheck();
                                       if (a == true)
                                           blackCheckMate();

                                       if (isStalemateFor("black") == true || isInsufficientMaterial() == true || fifty() == true) {
                                           JOptionPane.showMessageDialog(this, "Game is a draw");
                                           System.exit(0);
                                       }
                                   }

                                   triggerAISequence();
                                   return;
                               }
                           }
                       }
                       else if (pieces[i][j] != null && !pieces[i][j].getColor().equals(turn) && checkWinner() == false) {
                           ArrayList<Location> legalMovements = pieces[rowSelected][columnSelected].getLegalMovements();

                           for (Location obj : legalMovements) {
                               if (obj.getRow() == i && obj.getColumn() == j) {
                                   counter = 0;

                                   Piece movedPiece = pieces[rowSelected][columnSelected];
                                   Piece capturedPiece = pieces[i][j];
                                   int fromRow = rowSelected, fromCol = columnSelected;

                                   pieces[i][j] = pieces[rowSelected][columnSelected];
                                   chessBoard[i][j].setText("");
                                   pieces[i][j].getLocation().setRow(i);
                                   pieces[i][j].getLocation().setColumn(j);
                                   chessBoard[i][j].setIcon(chessBoard[rowSelected][columnSelected].getIcon());
                                   pieces[rowSelected][columnSelected] = null;
                                   chessBoard[rowSelected][columnSelected].setIcon(null);
                                   chessBoard[rowSelected][columnSelected].setText("-");

                                   clearBoardHighlights1();
                                   pieceSelected = false;

                                   giveMoveAdvice(movedPiece, capturedPiece, fromRow, fromCol, i, j);

                                   checkWinner();
                                   if (turn.equals("white")) {
                                       boolean a = blackCheck();
                                       if (a == true)
                                           blackCheckMate();

                                       if (isStalemateFor("black") == true || isInsufficientMaterial() == true || fifty() == true) {
                                           JOptionPane.showMessageDialog(this, "Game is a draw");
                                           System.exit(0);
                                       }
                                   }

                                   triggerAISequence();
                                   return;
                               }
                           }
                       }
                       else if (pieces[i][j] != null && pieces[i][j].getColor().equals(turn)) {
                           rowSelected = i;
                           columnSelected = j;
                           pieces[rowSelected][columnSelected].calculateLegalMovements(pieces);
                           highlightLegalSquares(pieces[rowSelected][columnSelected].getLegalMovements());
                       }
                   }
               }
           }
       }
   }

public boolean checkWinner() {
	int counter=0;
	int counter1=0;
	for(int row=0; row<=7;row++) {
		for (int column=0;column<=7;column++) {
		   if (pieces[row][column] !=null && pieces[row][column].getColor().equals("white")) {
			   counter++;

		   }
		   if (pieces[row][column] != null && pieces[row][column].getColor().equals("black")) {
			   counter1++;
		   }
		}
	}
	if (counter==0) {
		JOptionPane.showMessageDialog(this,"The winner is; Black");
		return true;
		}
		else if (counter1==0) {
			JOptionPane.showMessageDialog(this, "The winner is: White");
			return true;
	}
		else {
			return false;
		}
}
// ---- Pure check detection (no popups, no side effects) - safe to call during move simulation ----
private boolean isKingInCheck(String color) {
	int kingRow = -1, kingCol = -1;
	String kingImage = color.equals("white") ? "/images/white-king.png" : "/images/black-king.png";
	for (int row = 0; row <= 7; row++) {
		for (int column = 0; column <= 7; column++) {
			if (pieces[row][column] != null && pieces[row][column].getImageSource().equals(kingImage)) {
				kingRow = row;
				kingCol = column;
			}
		}
	}
	if (kingRow == -1) return false;

	String enemyColor = color.equals("white") ? "black" : "white";
	for (int row = 0; row <= 7; row++) {
		for (int col = 0; col <= 7; col++) {
			if (pieces[row][col] != null && pieces[row][col].getColor().equals(enemyColor)) {
				pieces[row][col].calculateLegalMovements(pieces);
				for (Location obj : pieces[row][col].getLegalMovements()) {
					if (obj.getRow() == kingRow && obj.getColumn() == kingCol) {
						return true;
					}
				}
			}
		}
	}
	return false;
}

// ---- Simulates a move on the real board, checks for self-check, then undoes it ----
private boolean wouldLeaveKingInCheck(int fromRow, int fromCol, int toRow, int toCol, String color) {
	Piece movingPiece = pieces[fromRow][fromCol];
	Piece capturedPiece = pieces[toRow][toCol];
	int origRow = movingPiece.getLocation().getRow();
	int origCol = movingPiece.getLocation().getColumn();

	pieces[toRow][toCol] = movingPiece;
	pieces[fromRow][fromCol] = null;
	movingPiece.getLocation().setRow(toRow);
	movingPiece.getLocation().setColumn(toCol);

	boolean inCheck = isKingInCheck(color);

	pieces[fromRow][fromCol] = movingPiece;
	pieces[toRow][toCol] = capturedPiece;
	movingPiece.getLocation().setRow(origRow);
	movingPiece.getLocation().setColumn(origCol);

	return inCheck;
}

// ---- Correct, general checkmate detection: true only if in check AND no move escapes it ----
private boolean isCheckmate(String color) {
	if (!isKingInCheck(color)) return false;
	for (int row = 0; row <= 7; row++) {
		for (int col = 0; col <= 7; col++) {
			if (pieces[row][col] != null && pieces[row][col].getColor().equals(color)) {
				pieces[row][col].calculateLegalMovements(pieces);
				ArrayList<Location> moves = new ArrayList<>(pieces[row][col].getLegalMovements());
				for (Location dest : moves) {
					if (!wouldLeaveKingInCheck(row, col, dest.getRow(), dest.getColumn(), color)) {
						return false; // found an escape - not checkmate
					}
				}
			}
		}
	}
	return true;
}

// ---- Correct, general stalemate detection: no legal move exists AND not currently in check ----
private boolean isStalemateFor(String color) {
	if (isKingInCheck(color)) return false;
	for (int row = 0; row <= 7; row++) {
		for (int column = 0; column <= 7; column++) {
			if (pieces[row][column] != null && pieces[row][column].getColor().equals(color)) {
				pieces[row][column].calculateLegalMovements(pieces);
				ArrayList<Location> moves = new ArrayList<>(pieces[row][column].getLegalMovements());
				for (Location dest : moves) {
					if (!wouldLeaveKingInCheck(row, column, dest.getRow(), dest.getColumn(), color)) {
						return false; // a legal move exists - not stalemate
					}
				}
			}
		}
	}
	return true;
}

// ---- Public-facing wrappers that keep the popups your UI already relies on ----
public boolean whiteCheck() {
	boolean inCheck = isKingInCheck("white");
	if (inCheck) JOptionPane.showMessageDialog(this, " White Check");
	return inCheck;
}

public boolean blackCheck() {
	boolean inCheck = isKingInCheck("black");
	if (inCheck) JOptionPane.showMessageDialog(this, " Black Check");
	return inCheck;
}

public boolean whiteCheckMate() {
	boolean mate = isCheckmate("white");
	if (mate) {
		JOptionPane.showMessageDialog(this, "Checkmate - Black wins");
		System.exit(0);
	}
	return mate;
}

public boolean blackCheckMate() {
	boolean mate = isCheckmate("black");
	if (mate) {
		JOptionPane.showMessageDialog(this, "Checkmate - White wins");
		System.exit(0);
	}
	return mate;
}

public boolean isStalemate() {
	return isStalemateFor(turn);
}

public boolean isInsufficientMaterial() {
	ArrayList <Piece> remainingwhites = new ArrayList<>();
	ArrayList <Piece> remainingblacks = new ArrayList<>();
	for (int row=0;row<8;row++) {
		for (int column=0;column<8;column++) {
			if (pieces[row][column]!=null && pieces[row][column].getColor().equals("white")) {
				remainingwhites.add(pieces[row][column]);

			}
			if (pieces[row][column]!=null && pieces[row][column].getColor().equals("black")) {
				remainingblacks.add(pieces[row][column]);
			}
		}
	}
	if (remainingwhites.size()==1 && remainingblacks.size()==1 && remainingwhites.get(0).getImageSource().equals("/images/white-king.png")
			&& remainingblacks.get(0).getImageSource().equals("/images/black-king.png")) {
		return true;
	}
	else if (remainingwhites.size()==1 && remainingblacks.size()==2 && remainingwhites.get(0).getImageSource().equals("/images/white-king.png")
			&& remainingblacks.get(0).getImageSource().equals("/images/black-king.png") && remainingblacks.get(1).getImageSource().equals("/images/black-knight.png")) {
		return true;
	}
	else if (remainingwhites.size()==1 && remainingblacks.size()==2 && remainingwhites.get(0).getImageSource().equals("/images/white-king.png")
			&& remainingblacks.get(1).getImageSource().equals("/images/black-king.png") && remainingblacks.get(0).getImageSource().equals("/images/black-knight.png")) {
		return true;
	}
	else if (remainingwhites.size()==2 && remainingblacks.size()==1 && remainingblacks.get(0).getImageSource().equals("/images/black-king.png")
			&& remainingwhites.get(0).getImageSource().equals("/images/white-king.png") && remainingwhites.get(1).getImageSource().equals("/images/white-knight.png")) {
		return true;
	}
	else if (remainingwhites.size()==2 && remainingblacks.size()==1 && remainingblacks.get(0).getImageSource().equals("/images/black-king.png")
			&& remainingwhites.get(1).getImageSource().equals("/images/white-king.png") && remainingwhites.get(0).getImageSource().equals("/images/white-knight.png")) {
		return true;
	}
	else if (remainingwhites.size()==1 && remainingblacks.size()==2 && remainingwhites.get(0).getImageSource().equals("/images/white-king.png")
			&& remainingblacks.get(0).getImageSource().equals("/images/black-king.png") && remainingblacks.get(1).getImageSource().equals("/images/black-bishop.png")) {
		return true;
	}
	else if (remainingwhites.size()==1 && remainingblacks.size()==2 && remainingwhites.get(0).getImageSource().equals("/images/white-king.png")
			&& remainingblacks.get(1).getImageSource().equals("/images/black-king.png") && remainingblacks.get(0).getImageSource().equals("/images/black-bishop.png")) {
		return true;
	}
	else if (remainingwhites.size()==2 && remainingblacks.size()==1 && remainingblacks.get(0).getImageSource().equals("/images/black-king.png")
			&& remainingwhites.get(0).getImageSource().equals("/images/white-king.png") && remainingwhites.get(1).getImageSource().equals("/images/white-bishop.png")) {
		return true;
	}
	else if (remainingwhites.size()==2 && remainingblacks.size()==1 && remainingblacks.get(0).getImageSource().equals("/images/black-king.png")
			&& remainingwhites.get(1).getImageSource().equals("/images/white-king.png") && remainingwhites.get(0).getImageSource().equals("/images/white-bishop.png")) {
		return true;
	}
	else {
		return false;
	}
}
public boolean fifty() {
	if (counter==50) {
		return true;
	}
	else {
		return false;
	}

}

   public static void main(String[] args) {
       new ChessBoard();
   }

   public void clearBoardHighlights() {
       clearBoardHighlights1();
   }

   /**
    * NEW: The actual "tell me what to play and why" feature. Runs when the
    * player clicks "Suggest a move" - scans every legal move available to
    * the human, scores them with the same evaluator the AI uses on itself,
    * and recommends the best one WITHOUT playing it for you.
    */
   private void giveHintForHuman() {
       if (isAiThinking || !turn.equals(humanColor)) {
           chatLog.append("Coach: Hang on for your turn first!\n\n");
           return;
       }

       ArrayList<Location[]> allMoves = new ArrayList<>();
       for (int i = 0; i <= 7; i++) {
           for (int j = 0; j <= 7; j++) {
               if (pieces[i][j] != null && pieces[i][j].getColor().equals(humanColor)) {
                   pieces[i][j].calculateLegalMovements(pieces);
                   ArrayList<Location> targets = new ArrayList<>(pieces[i][j].getLegalMovements());
                   for (Location loc : targets) {
                       if (!wouldLeaveKingInCheck(i, j, loc.getRow(), loc.getColumn(), humanColor)) {
                           allMoves.add(new Location[]{ new Location(i, j), loc });
                       }
                   }
               }
           }
       }

       if (allMoves.isEmpty()) {
           chatLog.append("Coach: I don't see a legal move available for you right now.\n\n");
           return;
       }

       Location[] best = allMoves.get(0);
       int bestScore = Integer.MIN_VALUE;

       for (Location[] move : allMoves) {
           Location from = move[0];
           Location to = move[1];
           int score = evaluateMoveScore(from.getRow(), from.getColumn(), to.getRow(), to.getColumn(), humanColor);

           boolean isCentral = (to.getRow() == 3 || to.getRow() == 4) && (to.getColumn() == 3 || to.getColumn() == 4);
           if (isCentral) score += 4;

           if (score > bestScore) {
               bestScore = score;
               best = move;
           }
       }

       Location from = best[0];
       Location to = best[1];
       Piece movingPiece = pieces[from.getRow()][from.getColumn()];
       Piece targetPiece = pieces[to.getRow()][to.getColumn()];
       String pieceName = movingPiece.getClass().getSimpleName();

       StringBuilder msg = new StringBuilder("Coach hint: ");
       msg.append("If I were you, I'd move the ").append(pieceName)
          .append(" from ").append(squareName(from.getRow(), from.getColumn()))
          .append(" to ").append(squareName(to.getRow(), to.getColumn())).append(". ");

       if (targetPiece != null) {
           msg.append("It captures the ").append(targetPiece.getClass().getSimpleName())
              .append(" and, as far as I can see, doesn't lose material back.");
       } else {
           boolean isCentral = (to.getRow() == 3 || to.getRow() == 4) && (to.getColumn() == 3 || to.getColumn() == 4);
           boolean wasInDanger = isSquareAttacked(from.getRow(), from.getColumn(),
                   humanColor.equals("white") ? "black" : "white");
           if (wasInDanger) {
               msg.append("That piece was under attack where it stood - this gets it to safety.");
           } else if (isCentral) {
               msg.append("It helps take control of the center, which is usually a strong plan early on.");
           } else {
               msg.append("It looks like a safe, useful move from what I can calculate - but I'm only weighing material and safety, not deep strategy.");
           }
       }

       chatLog.append(msg.toString() + "\n\n");
   }

   private void giveMoveAdvice(Piece movedPiece, Piece capturedPiece, int fromRow, int fromCol, int toRow, int toCol) {
       String pieceName = movedPiece.getClass().getSimpleName();
       StringBuilder msg = new StringBuilder("Coach: ");

       if (capturedPiece != null) {
           msg.append("Your ").append(pieceName).append(" captured the ")
              .append(capturedPiece.getClass().getSimpleName()).append(". ");
       } else {
           msg.append("You moved your ").append(pieceName)
              .append(" from ").append(squareName(fromRow, fromCol))
              .append(" to ").append(squareName(toRow, toCol)).append(". ");
       }

       boolean isCentral = (toRow == 3 || toRow == 4) && (toCol == 3 || toCol == 4);

       if (capturedPiece != null) {
           int gained = pieceValue(capturedPiece) - pieceValue(movedPiece);
           if (gained >= 0) {
               msg.append("That looks like a good trade - you gave up less material than you won.");
           } else {
               msg.append("Worth double-checking: you used a more valuable piece to take a less valuable one. Was there a reason (like removing a defender or stopping a threat)?");
           }
       } else if (pieceName.equals("Pawn") && isCentral) {
           msg.append("Good instinct - controlling the center with a pawn early on gives your other pieces more options.");
       } else if (pieceName.equals("Knight") || pieceName.equals("Bishop")) {
           if (fromRow == 0 || fromRow == 7) {
               msg.append("Nice, that's called 'developing' a piece - getting it off the back rank and into the game early is usually strong.");
           } else {
               msg.append("Think about which squares this piece now attacks, and whether it's protected if your opponent goes after it.");
           }
       } else if (pieceName.equals("King")) {
           msg.append("Moving the king early can be risky since it's usually safer tucked behind your other pieces - make sure this was intentional.");
       } else {
           msg.append("Consider what this move opens up or defends, and whether your opponent has a strong reply.");
       }

       chatLog.append(msg.toString() + "\n\n");
   }

   private int pieceValue(Piece p) {
       String name = p.getClass().getSimpleName().toLowerCase();
       if (name.contains("pawn")) return 1;
       if (name.contains("knight") || name.contains("bishop")) return 3;
       if (name.contains("rook")) return 5;
       if (name.contains("queen")) return 9;
       if (name.contains("king")) return 1000;
       return 0;
   }

   private String squareName(int row, int col) {
       char file = (char) ('a' + col);
       int rank = row + 1;
       return "" + file + rank;
   }

   private void triggerAISequence() {
       turn = "black";
       isAiThinking = true;

       new Thread(() -> {
           try {
               Thread.sleep(900);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           ArrayList<Location[]> aiLegalMoves = scanAllAvailableAIMoves();
           if (aiLegalMoves.isEmpty()) {
               isAiThinking = false;
               return;
           }

           Location[] selectedAiMove = evaluateOptimalAIMove(aiLegalMoves);

           if (selectedAiMove != null) {
               Location from = selectedAiMove[0];
               Location to = selectedAiMove[1];

               SwingUtilities.invokeLater(() -> {
                   executeAIMovementOnGrid(from, to);
                   isAiThinking = false;
                   turn = "white";
               });
           }
       }).start();
   }

   private ArrayList<Location[]> scanAllAvailableAIMoves() {
       ArrayList<Location[]> movesList = new ArrayList<>();
       for (int i = 0; i < 8; i++) {
           for (int j = 0; j < 8; j++) {
               if (pieces[i][j] != null && pieces[i][j].getColor().equals("black")) {
                   pieces[i][j].calculateLegalMovements(pieces);
                   ArrayList<Location> targets = new ArrayList<>(pieces[i][j].getLegalMovements());
                   for (Location loc : targets) {
                       // Don't even consider moves that would leave the AI's own king in check
                       if (!wouldLeaveKingInCheck(i, j, loc.getRow(), loc.getColumn(), "black")) {
                           movesList.add(new Location[]{ new Location(i, j), loc });
                       }
                   }
               }
           }
       }
       return movesList;
   }

   /**
    * Shared move evaluator used by BOTH the AI's move selection and the
    * human hint feature. Scores a candidate move on:
    *  - material gained by capturing
    *  - whether the destination square is actually safe (avoids "AI hangs
    *    its own pieces" and gives some sense of self-preservation)
    *  - whether the move rescues a piece that was already under attack
    * Positive = better. This does NOT execute the move - it simulates,
    * measures, then undoes it.
    */
   private int evaluateMoveScore(int fromRow, int fromCol, int toRow, int toCol, String color) {
       Piece movingPiece = pieces[fromRow][fromCol];
       Piece targetPiece = pieces[toRow][toCol];
       String enemyColor = color.equals("white") ? "black" : "white";

       int score = 0;
       if (targetPiece != null) {
           score += pieceValue(targetPiece) * 10;
       }

       boolean wasInDanger = isSquareAttacked(fromRow, fromCol, enemyColor);

       // simulate the move
       int origRow = movingPiece.getLocation().getRow();
       int origCol = movingPiece.getLocation().getColumn();
       pieces[toRow][toCol] = movingPiece;
       pieces[fromRow][fromCol] = null;
       movingPiece.getLocation().setRow(toRow);
       movingPiece.getLocation().setColumn(toCol);

       boolean landsInDanger = isSquareAttacked(toRow, toCol, enemyColor);

       // undo the simulation
       pieces[fromRow][fromCol] = movingPiece;
       pieces[toRow][toCol] = targetPiece;
       movingPiece.getLocation().setRow(origRow);
       movingPiece.getLocation().setColumn(origCol);

       if (landsInDanger) {
           // Moving here risks losing this piece - penalize by its value
           score -= pieceValue(movingPiece) * 10;
       }
       if (wasInDanger && !landsInDanger) {
           // This move rescues a piece that was about to be lost
           score += pieceValue(movingPiece) * 8;
       }

       return score;
   }

   /**
    * True if any piece of byColor can legally reach (row, col) right now.
    * Used to judge whether a square is "safe" to move to or sit on.
    */
   private boolean isSquareAttacked(int row, int col, String byColor) {
       for (int r = 0; r <= 7; r++) {
           for (int c = 0; c <= 7; c++) {
               if (pieces[r][c] != null && pieces[r][c].getColor().equals(byColor)) {
                   pieces[r][c].calculateLegalMovements(pieces);
                   for (Location loc : pieces[r][c].getLegalMovements()) {
                       if (loc.getRow() == row && loc.getColumn() == col) {
                           return true;
                       }
                   }
               }
           }
       }
       return false;
   }

   private Location[] evaluateOptimalAIMove(ArrayList<Location[]> choices) {
       if (difficulty.equals("easy") && Math.random() < 0.4) {
           return choices.get((int)(Math.random() * choices.size()));
       }

       Location[] optimalChoice = choices.get(0);
       int highestHeuristicValue = Integer.MIN_VALUE;

       for (Location[] move : choices) {
           Location origin = move[0];
           Location destination = move[1];
           int computationalScore = evaluateMoveScore(
                   origin.getRow(), origin.getColumn(),
                   destination.getRow(), destination.getColumn(),
                   "black");

           if (difficulty.equals("hard")) {
               int r = destination.getRow();
               int c = destination.getColumn();
               if ((r == 3 || r == 4) && (c == 3 || c == 4)) {
                   computationalScore += 4;
               }
           }

           if (computationalScore > highestHeuristicValue) {
               highestHeuristicValue = computationalScore;
               optimalChoice = move;
           }
       }
       return optimalChoice;
   }

   private void executeAIMovementOnGrid(Location from, Location to) {
       int fRow = from.getRow();
       int fCol = from.getColumn();
       int tRow = to.getRow();
       int tCol = to.getColumn();

       Piece movingPiece = pieces[fRow][fCol];
       Piece targetCaptured = pieces[tRow][tCol];

       pieces[tRow][tCol] = movingPiece;
       pieces[tRow][tCol].getLocation().setRow(tRow);
       pieces[tRow][tCol].getLocation().setColumn(tCol);
       pieces[fRow][fCol] = null;

       chessBoard[tRow][tCol].setText("");
       chessBoard[tRow][tCol].setIcon(chessBoard[fRow][fCol].getIcon());
       chessBoard[fRow][fCol].setIcon(null);
       chessBoard[fRow][fCol].setText("-");

       sendCoachChatFeedback(movingPiece, targetCaptured, from, to);
       checkWinner();

       // --- THIS WAS MISSING: check whether the AI's move put the human in check ---
       boolean humanInCheck = whiteCheck();
       if (humanInCheck) {
           whiteCheckMate(); // exits the game if it's actually checkmate
       }
       if (isStalemateFor("white") || isInsufficientMaterial() || fifty()) {
           JOptionPane.showMessageDialog(this, "Game is a draw");
           System.exit(0);
       }
   }

   private void sendCoachChatFeedback(Piece attacker, Piece victim, Location from, Location to) {
       String attackerName = attacker.getClass().getSimpleName();
       StringBuilder narrative = new StringBuilder("Opponent: ");

       if (victim != null) {
           narrative.append("Took your ").append(victim.getClass().getSimpleName())
                    .append(" with a ").append(attackerName).append(". ")
                    .append("Was that piece defended? Worth checking before your next move.");
       } else {
           narrative.append("Moved a ").append(attackerName)
                    .append(" to ").append(squareName(to.getRow(), to.getColumn())).append(". ")
                    .append("Take a look at what squares it now covers before you respond.");
       }
       chatLog.append(narrative.toString() + "\n\n");
   }

}