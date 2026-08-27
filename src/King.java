public class King extends Piece {
	public King (Location obj, String color, String ImageSource) {
		super(obj,color,ImageSource);
	}
	public void calculateLegalMovements(Piece[][] board) {
		int column = getLocation().getColumn();
		int row = getLocation().getRow();
		getLegalMovements().clear();

		int[][] offsets = {
			{0,1},{0,-1},{1,0},{-1,0},
			{1,1},{1,-1},{-1,1},{-1,-1}
		};
		for (int[] off : offsets) {
			int r = row + off[0];
			int c = column + off[1];
			if (r>=0 && r<=7 && c>=0 && c<=7) {
				if (board[r][c] == null || !board[r][c].getColor().equals(getColor())) {
					getLegalMovements().add(new Location(r,c));
				}
			}
		}
	}
}