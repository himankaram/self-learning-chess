public class King extends Piece {
	public King (Location obj, String color, String ImageSource) {
		super(obj,color,ImageSource);
	}
	public void calculateLegalMovements() {
		int column = getLocation().getColumn();
		int row = getLocation().getRow();
	getLegalMovements().clear();
	if (row==0 && column==0) {
		getLegalMovements().add(new Location(row,column+1));
		getLegalMovements().add(new Location(row+1,column));
		getLegalMovements().add(new Location(row+1,column+1));
	}
	else if (row==0 && column==7) {
		getLegalMovements().add(new Location(row,column-1));
		getLegalMovements().add(new Location(row+1,column));
		getLegalMovements().add(new Location(row+1,column-1));	
	}
	else if (row==7 && column==0) {
		getLegalMovements().add(new Location(row-1,column));
		getLegalMovements().add(new Location(row,column+1));
		getLegalMovements().add(new Location(row-1,column+1));	
	}
	else if (row==7 && column==7) {
		getLegalMovements().add(new Location(row,column-1));
		getLegalMovements().add(new Location(row-1,column));
		getLegalMovements().add(new Location(row-1,column-1));
	}
	else if (row==0) {
		getLegalMovements().add(new Location(row,column-1));
		getLegalMovements().add(new Location(row,column+1));
		getLegalMovements().add(new Location(row+1,column));
		getLegalMovements().add(new Location(row+1,column-1));
		getLegalMovements().add(new Location(row+1,column+1));
	}
	else if (row==7) {
		getLegalMovements().add(new Location(row-1,column));
		getLegalMovements().add(new Location(row,column-1));
		getLegalMovements().add(new Location(row,column+1));
		getLegalMovements().add(new Location(row-1,column+1));
		getLegalMovements().add(new Location(row-1,column-1));
	}
	else if (column==0) {
		getLegalMovements().add(new Location(row+1,column+1));
		getLegalMovements().add(new Location(row-1,column+1));
		getLegalMovements().add(new Location(row,column+1));
		getLegalMovements().add(new Location(row-1,column));
		getLegalMovements().add(new Location(row+1,column));
	}
	else if (column==7) {
		getLegalMovements().add(new Location(row-1,column));
		getLegalMovements().add(new Location(row+1,column));
		getLegalMovements().add(new Location(row,column-1));
		getLegalMovements().add(new Location(row-1,column-1));
		getLegalMovements().add(new Location(row+1,column-1));
	}
	else {
		getLegalMovements().add(new Location(row,column-1));
		getLegalMovements().add(new Location(row,column+1));
		getLegalMovements().add(new Location(row-1,column));
		getLegalMovements().add(new Location(row+1,column));
		getLegalMovements().add(new Location(row-1,column+1));
		getLegalMovements().add(new Location(row-1,column-1));
		getLegalMovements().add(new Location(row+1,column+1));
		getLegalMovements().add(new Location(row+1,column-1));
	}

}}
