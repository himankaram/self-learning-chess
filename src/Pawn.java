public class Pawn extends Piece {
private boolean startLocation;
	public Pawn (Location obj, String color, String ImageSource) {
		super(obj,color,ImageSource);
		// Keep startLocation as true initially
		startLocation= true; 
	}
	public void calculateLegalMovements() {
		int column= getLocation().getColumn();
		int row= getLocation().getRow();
	getLegalMovements().clear();
	
	
	int direction = getColor().equals("white") ? 1 : -1;
	
	int diagCol1 = column + 1;
	int diagRow = row + direction;
	if (diagRow >= 0 && diagRow <= 7 && diagCol1 >= 0 && diagCol1 <= 7) {
	    getLegalMovements().add(new Location(diagRow, diagCol1));
	}
	
	
	int diagCol2 = column - 1;
	if (diagRow >= 0 && diagRow <= 7 && diagCol2 >= 0 && diagCol2 <= 7) {
	    getLegalMovements().add(new Location(diagRow, diagCol2));
	}
	
	
	int forwardRow1 = row + direction;
	if (forwardRow1 >= 0 && forwardRow1 <= 7) {
	    
	    getLegalMovements().add(new Location(forwardRow1, column));
	}
	
	int startRow = getColor().equals("white") ? 1 : 6;
	if (row == startRow) {startLocation=false;
		int forwardRow2 = row + 2 * direction;
		if (forwardRow2 >= 0 && forwardRow2 <= 7) {
			getLegalMovements().add(new Location(forwardRow2, column));
		}
	}
	
}

}//try this