public class Queen extends Piece {
	public Queen (Location obj, String color, String ImageSource) {
		super(obj,color,ImageSource);
	}
	public void calculateLegalMovements() {
		int column= getLocation().getColumn();
		int row= getLocation().getRow();
	getLegalMovements().clear();
	int column1=column+1;
	while (column1<=7) {
		getLegalMovements().add(new Location(row,column1));
		column1++;
	}
	column1=column-1;
	while (column1>=0) {
		getLegalMovements().add(new Location(row,column1));
		column1--;
	}
	int row1=row+1;
	while (row1<=7) {
		getLegalMovements().add(new Location(row1,column));
		row1++;
	}
	row1=row-1;
	while (row1>=0) {
		getLegalMovements().add(new Location(row1,column));          
		row1--;
	}
	//DIAGONAL MOVEMENT TOP RIGHT
	row1=row+1;
	column1=column+1;
	while (row1<=7 && column1<=7) {
		getLegalMovements().add(new Location(row1,column1));       
		column1++;
		row1++;
	//
	}
	//DIAGONAL MOVEMENT TOP LEFT
	row1=row+1;
	column1=column-1;
	while (row1<=7 && column1>=0) {
		getLegalMovements().add(new Location(row1,column1));
		column1--;
		row1++;	
	}
	//DIAGONAL MOVEMENT BOTTOM LEFT
	row1=row-1;
	column1=column-1;
	while (row1>=0 && column1>=0) {
		getLegalMovements().add(new Location(row1,column1));
		column1--;
		row1--;	
	}
	//DIAGONAL MOVEMENT BOTTOM RIGHT
	row1=row-1;
	column1=column+1;
	while (row1>=0 && column1<=7) {
		getLegalMovements().add(new Location(row1,column1));
		column1++;
		row1--;	

}}}
