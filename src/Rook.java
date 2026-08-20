public class Rook extends Piece {
	public Rook (Location obj, String color, String ImageSource) {
		super(obj,color,ImageSource);
	}
	public void calculateLegalMovements() {
		int column=getLocation().getColumn();
		int row= getLocation().getRow();
		getLegalMovements().clear();
		//MOVES HORIZONTALLY TO THE RIGHT UNTIL IT REACHES THE LIMIT
		int column1 = column + 1;
		while (column1<=7) {
			getLegalMovements().add(new Location(row,column1));
			column1++;
		}
		//MOVES HORIZONTALLY TO THE LEFT UNTIL IT REACHES THE LIMIT
		column1 =column -1;
		while (column1>=0) {
			getLegalMovements().add(new Location(row,column1));
			column1--;
		}
		//MOVES VERTICALLY UP UNTIL IT REACHES THE LIMIT
		int row1=row+1;
		while (row1<=7) {
			getLegalMovements().add(new Location(row1,column));
			row1++;
		}
		//MOVES VERTICALLY DOWN UNTIL IT REACHES THE LIMIT
		row1=row-1;
		while (row1>=0) {
			getLegalMovements().add(new Location(row1,column));
			row1--;
		}
		
		
		
	}

}
