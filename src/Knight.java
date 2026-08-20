public class Knight extends Piece {
	public Knight (Location obj, String color, String ImageSource) {
		super(obj,color,ImageSource);
	}
	public void calculateLegalMovements() {
		int column= getLocation().getColumn();
		int row= getLocation().getRow();
	getLegalMovements().clear(); {
		int column1, row1;
		//KNIGHT MOVEMENTS
		column1= column+1;
		row1= row+2;
		if (column1<=7 && row1<=7) {
			getLegalMovements().add(new Location(row1,column1));
		}
		column1=column+1;
		row1=row-2;
		if (column1<=7 && row1>=0) {
			getLegalMovements().add(new Location(row1,column1));
		}
		column1=column-1;
		row1=row-2;
		if (column1>=0 && row1<=7) {
			getLegalMovements().add(new Location(row1,column1));
		}
		column1=column-1;
		row1=row+2;
		if (column1>=0 && row1<=7) {
			getLegalMovements().add(new Location(row1,column1));
		
	    }
		column1=column+2;
		row1=row+1;
		if (column1<=7 && row1<=7) {
			getLegalMovements().add(new Location(row1,column1));
		}
		column1=column+2;
		row1=row-1;
		if (column1<=7 && row1>=0) {
			getLegalMovements().add(new Location(row1,column1));
		}
		column1=column-2;
		row1=row+1;
		if (column1>=0 && row1<=7) {
			getLegalMovements().add(new Location(row1,column1));
		}
		column1=column-2;
		row1=row-1;
		if (column1>=0 && row1>=0) {
			getLegalMovements().add(new Location(row1,column1));
}}}}
