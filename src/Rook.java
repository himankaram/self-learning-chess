public class Rook extends Piece {
	public Rook (Location obj, String color, String ImageSource) {
		super(obj,color,ImageSource);
	}
	public void calculateLegalMovements(Piece[][] board) {
		int column=getLocation().getColumn();
		int row= getLocation().getRow();
		getLegalMovements().clear();

		int[][] directions = { {0,1}, {0,-1}, {1,0}, {-1,0} };
		for (int[] dir : directions) {
			int r = row + dir[0];
			int c = column + dir[1];
			while (r>=0 && r<=7 && c>=0 && c<=7) {
				if (board[r][c] == null) {
					getLegalMovements().add(new Location(r,c));
				} else {
					if (!board[r][c].getColor().equals(getColor())) {
						getLegalMovements().add(new Location(r,c)); // capture
					}
					break; // blocked either way, stop sliding this direction
				}
				r += dir[0];
				c += dir[1];
			}
		}
	}
}