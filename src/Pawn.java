public class Pawn extends Piece {
	private boolean startLocation;
	public Pawn (Location obj, String color, String ImageSource) {
		super(obj,color,ImageSource);
		startLocation = true;
	}
	public void calculateLegalMovements(Piece[][] board) {
		int column= getLocation().getColumn();
		int row= getLocation().getRow();
		getLegalMovements().clear();

		int direction = getColor().equals("white") ? 1 : -1;

		// Diagonal captures - only legal if an enemy piece is actually there
		int diagRow = row + direction;
		int diagCol1 = column + 1;
		if (diagRow >= 0 && diagRow <= 7 && diagCol1 >= 0 && diagCol1 <= 7) {
			if (board[diagRow][diagCol1] != null && !board[diagRow][diagCol1].getColor().equals(getColor())) {
				getLegalMovements().add(new Location(diagRow, diagCol1));
			}
		}
		int diagCol2 = column - 1;
		if (diagRow >= 0 && diagRow <= 7 && diagCol2 >= 0 && diagCol2 <= 7) {
			if (board[diagRow][diagCol2] != null && !board[diagRow][diagCol2].getColor().equals(getColor())) {
				getLegalMovements().add(new Location(diagRow, diagCol2));
			}
		}

		// Forward move - only legal if the square ahead is empty
		int forwardRow1 = row + direction;
		if (forwardRow1 >= 0 && forwardRow1 <= 7 && board[forwardRow1][column] == null) {
			getLegalMovements().add(new Location(forwardRow1, column));

			// Two-square opening move - only legal if BOTH squares ahead are empty
			int startRow = getColor().equals("white") ? 1 : 6;
			if (row == startRow) {
				startLocation = false;
				int forwardRow2 = row + 2 * direction;
				if (forwardRow2 >= 0 && forwardRow2 <= 7 && board[forwardRow2][column] == null) {
					getLegalMovements().add(new Location(forwardRow2, column));
				}
			}
		}
	}
}