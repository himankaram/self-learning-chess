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
   public ChessBoard() {
       setSize(800, 800);
       setTitle("Chess");
       setLayout(new GridLayout(8, 8));
       chessBoard = new JButton[8][8];
       pieces = new Piece[8][8];
       pieceSelected = false;
       designChessBoard();
       gametimer= new Timer();

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
               getContentPane().add(chessBoard[i][j]);
               chessBoard[i][j].addActionListener(this);
           }
       }
       JMenuBar JMB= new JMenuBar();
       JMenu mnOption= new JMenu(" Option ");
       mnResign= new JMenuItem(" Resign ");
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
       
       
       // Now for black pieces
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
	   if (ae.getSource()==mnResign) {
		   if (turn.equals("white"))
			   JOptionPane.showMessageDialog(this, "Black Wins");
		   else {
			   JOptionPane.showMessageDialog(this, "White Wins");
		   }
		   System.exit(0);
	   }
	   for (int i=0; i<=7; i++) {
		   for (int j=0; j<=7; j++) {
			   if (chessBoard[i][j]==ae.getSource()) {
				   if (pieceSelected== false && checkWinner()== false) {
					   
				   if (pieces[i][j]!=null && pieces[i][j].getColor().equals(turn) ) {
					   pieceSelected = true;
					   rowSelected = i;
					   columnSelected = j;
					   pieces[rowSelected][columnSelected].calculateLegalMovements();
				   }}
				   else {
					   if (pieces[i][j]==null && checkWinner()== false) {
						   ArrayList <Location>legalMovements=pieces[rowSelected][columnSelected].getLegalMovements();
						   boolean found = false;
						   for (Location obj:legalMovements) {
							   if (obj.getRow()==i && obj.getColumn()==j) {
								   if (pieces[rowSelected][columnSelected].getImageSource().indexOf("pawn")!=-1) {
									   counter=0;
									   if (Math.abs(i-rowSelected)==1 && Math.abs(j-columnSelected)==1)
									   {
										   return;
									   }
								   }
								   counter++;
								   chessBoard[i][j].setText("");
								   pieces[i][j]= pieces[rowSelected][columnSelected];
								   pieces[i][j].getLocation().setRow(i);
								   pieces[i][j].getLocation().setColumn(j);
								   chessBoard[i][j].setIcon(chessBoard[rowSelected][columnSelected].getIcon());
								   pieces[rowSelected][columnSelected] = null;
								   chessBoard[rowSelected][columnSelected].setIcon(null);
								   chessBoard[rowSelected][columnSelected].setText("-");
								   pieceSelected = false;
								   checkWinner();
								   if (turn.equals("white")) {
									   boolean a=blackCheck();
									   if (a==true)
									   blackCheckMate();
								   if(isStalemate()==true || isInsufficientMaterial()==true || fifty()==true) {
									   JOptionPane.showMessageDialog(this, "Game is a draw");
									   System.exit(0);
								   }
									   turn = "black";
									   startTimer();
								   }
									   else {
										   boolean a=whiteCheck();
										   if (a==true)
										   whiteCheckMate();
								   if(isStalemate()==true || isInsufficientMaterial()==true || fifty()==true) {
								        JOptionPane.showMessageDialog(this, "Game is a draw");
											   System.exit(0);
								   }
										   turn = "white";
										   startTimer();
									   }
								   found = true;
								   break;
							   }
						   }
		
							   
					   }
					   else if (pieces[i][j].getColor().equals(turn)==false && checkWinner()==false) {
						   ArrayList <Location>legalMovements=pieces[rowSelected][columnSelected].getLegalMovements();
						   boolean found = false;
						   for (Location obj:legalMovements) {
							   if (obj.getRow()==i && obj.getColumn()==j) {
								   
								   pieces[i][j]= null;
								   chessBoard[i][j].setText("");
								   pieces[i][j]= pieces[rowSelected][columnSelected];
								   pieces[i][j].getLocation().setRow(i);
								   pieces[i][j].getLocation().setColumn(j);
								   chessBoard[i][j].setIcon(chessBoard[rowSelected][columnSelected].getIcon());
								   pieces[rowSelected][columnSelected] = null;
								   chessBoard[rowSelected][columnSelected].setIcon(null);
								   chessBoard[rowSelected][columnSelected].setText("-");
								   pieceSelected = false;
								   counter=0;
								   checkWinner();
								   if (turn.equals("white")) {
									   boolean a=blackCheck();
									   if (a==true)
									   blackCheckMate();
								   if(isStalemate()==true || isInsufficientMaterial()==true || fifty()==true) {
									   JOptionPane.showMessageDialog(this, "Game is a draw");
									   System.exit(0);
								   }
									   turn = "black";
									   startTimer();
								   }
									   else {
										   boolean a=whiteCheck();
										   if (a==true)
										   whiteCheckMate();
								   if(isStalemate()==true || isInsufficientMaterial()==true || fifty()==true) {
								        JOptionPane.showMessageDialog(this, "Game is a draw");
											   System.exit(0);
								   }
										   turn = "white";
										   startTimer();
									   }
								   found = true;
								   break;
							   }
						   }
					   }
					   else {
						   pieceSelected=false;
						   rowSelected = i;
						   columnSelected = j;
						   pieces[rowSelected][columnSelected].calculateLegalMovements();
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
public boolean whiteCheck() {
	int kingrow=-1;
	int kingcol=-1;
	for (int row=0; row<=7;row++) {
		for (int column=0; column<=7; column++) {
			if (pieces[row][column]!=null && pieces[row][column].getImageSource().equals("/images/white-king.png")) {
				kingrow=row;
				kingcol=column;
				break; }}}
				for(int row1=0; row1<=7;row1++) {
					for (int col=0;col<=7;col++) {
						if (pieces[row1][col] != null && pieces[row1][col].getColor().equals("black")) {
							ArrayList<Location>pieceattacking=pieces[row1][col].getLegalMovements();
							for (Location obj:pieceattacking) {
								System.out.println(obj.getRow()+" "+obj.getColumn());	
								if (obj.getColumn()==kingrow && obj.getRow()==kingcol) {
									JOptionPane.showMessageDialog(this," White Check");
									System.out.println(obj.getRow()+" "+obj.getColumn()+"problem"+pieces[row1][col].getClass().toString());									
									return true;
								}
							}
						}
			}
		}
	return false;
}

public boolean blackCheck() {
	int kingrow=-1;
	int kingcol=-1;
	for (int row=0; row<=7;row++) {
		for (int column=0; column<=7; column++) {
			if (pieces[row][column]!=null && pieces[row][column].getImageSource().equals("/images/black-king.png")) {
				kingrow=row;
				kingcol=column;
				break; }}}
				for(int row1=0; row1<=7;row1++) {
					for (int col=0;col<=7;col++) {
						if (pieces[row1][col] != null && pieces[row1][col].getColor().equals("white")) {
							ArrayList<Location>pieceattacking=pieces[row1][col].getLegalMovements();
							for (Location obj:pieceattacking) {
								if (obj.getColumn()==kingrow && obj.getRow()==kingcol) {
									JOptionPane.showMessageDialog(this," Black Check");
									return true;
								}
							}
						}
			}
		}
	return false;
}

public boolean whiteCheckMate() {
	ArrayList<Location>pieceattacking=new ArrayList();
	int countemptyplaces=0;
		for (int row=0; row<=7;row++) {
			for (int column=0; column<=7;column++) {
				if (pieces[row][column]!=null && pieces[row][column].getImageSource().equals("/images/white-king.png")) {
					pieces[row][column].calculateLegalMovements();
					pieceattacking=pieces[row][column].getLegalMovements();
						
					
						for (Location obj: pieceattacking) {
							if (pieces[obj.getRow()][obj.getColumn()] == null) {
							 countemptyplaces++;
							}
						}}
					
		}		
	}
		if (countemptyplaces>1) {
			return false;
		}
		else {
			for (int row=0; row<=7;row++) {
				for (int column=0; column<=7;column++) {
					if (pieces[row][column]!=null && pieces[row][column].getImageSource().equals("/images/black-queen.png")) {
						pieces[row][column].calculateLegalMovements();
						ArrayList<Location>queenpositions=pieces[row][column].getLegalMovements();
							
						
							for (Location obj: queenpositions) {
								if (obj.getRow()==pieceattacking.get(0).getRow() && obj.getColumn()==pieceattacking.get(0).getColumn()) {
									JOptionPane.showMessageDialog(this, "White CheckMate");
									return true;
								}
							}
		}}}}
		JOptionPane.showMessageDialog(this, "White CheckMate");
		return true;
	
}
public boolean blackCheckMate() {
	ArrayList<Location>pieceattacking=new ArrayList();
	int countemptyplaces=0;
		for (int row=0; row<=7;row++) {
			for (int column=0; column<=7;column++) {
				if (pieces[row][column]!=null && pieces[row][column].getImageSource().equals("/images/black-king.png")) {
					pieces[row][column].calculateLegalMovements();
					pieceattacking=pieces[row][column].getLegalMovements(); 
						for (Location obj: pieceattacking) {							
							if (pieces[obj.getRow()][obj.getColumn()] == null) {
								countemptyplaces++;
							}
		}}}}
		if (countemptyplaces>1) {
			return false;
		}
		else {
			for (int row=0; row<=7;row++) {
				for (int column=0; column<=7;column++) {
					if (pieces[row][column]!=null && pieces[row][column].getImageSource().equals("/images/white-queen.png")) {
						pieces[row][column].calculateLegalMovements();
						ArrayList<Location>queenpositions=pieces[row][column].getLegalMovements();
							
						
							for (Location obj: queenpositions) {
								if (obj.getRow()==pieceattacking.get(0).getRow() && obj.getColumn()==pieceattacking.get(0).getColumn()) {
									JOptionPane.showMessageDialog(this, "Black CheckMate");
									return true;
								}
							}
		}}}}
		JOptionPane.showMessageDialog(this, "Black CheckMate");
		return true;
	
}
public boolean isStalemate() {
	if ((turn.equals("white") && whiteCheck()) || (turn.equals("black") && blackCheck())) {
		return false;
	}
	else {
		for (int row=0;row<=7;row++) {
			for (int column=0;column<=7;column++) {
				if (pieces[row][column]!=null && pieces[row][column].getColor().equals(turn)) {
					pieces[row][column].calculateLegalMovements();
					ArrayList<Location>LegalMovements=pieces[row][column].getLegalMovements();
					for (Location L:LegalMovements) {
						if (pieces[L.getRow()][L.getColumn()]==null ) {
							return false;
						}
						else {
							if(turn.equals("white")) {
								if(pieces[L.getRow()] [L.getColumn()].getColor().equals("black")) {
									return false;
								}
							}
							else {
								if(pieces[L.getRow()] [L.getColumn()].getColor().equals("white")) {
									return false;
								}
							}
						}
					}
					
				}
			}
		}
		return true;
	}
	
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
	if (counter==50) {     //once the counter has reached 50
		return true;
	}
	else {
		return false;
	}
	
}

   public static void main(String[] args) {
       new ChessBoard();
   }
}