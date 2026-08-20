public class Location {
	private int row, column;
	public Location(int row, int column) {
		this.row=row;
		this.column=column;
	}
	public int getRow()
	{
		return row;
	}
	public int getColumn()
	{
		return column;
	}
	public void setRow(int x)
	{
		this.row=x;
	}
	public void setColumn(int y)
	{
		this.column=y;
	}
	public String toString()
	{
		return "(" + row + "," + column + ")";
	}
}
