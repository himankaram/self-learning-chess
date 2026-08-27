public class Knight extends Piece {
	public Knight (Location obj, String color, String ImageSource) {
		super(obj,color,ImageSource);
	}
	public void calculateLegalMovements(Piece[][] board) {
		int column= getLocation().getColumn();
		int row= getLocation().getRow();
		getLegalMovements().clear();

		int[][] offsets = {
			{1,2},{-1,2},{1,-2},{-1,-2},
			{2,1},{-2,1},{2,-1},{-2,-1}
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