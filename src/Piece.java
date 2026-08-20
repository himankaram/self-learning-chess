import java.util.ArrayList;
public abstract class Piece {
	private Location currentLocation;
	private ArrayList<Location>LegalMovements; 
	private String color;
	private boolean status;
	private String ImageSource;
	
	public Piece(Location obj, String color, String ImageSource) {
		this.currentLocation= obj;
		this.color=color;
		this.status=true;
		this.ImageSource=ImageSource;
		this.LegalMovements= new ArrayList();
	}
	public Location getLocation()
	{
		return currentLocation;
	}
	public String getColor()
	{
		return color;
	}
	public boolean isStatus()
	{
		return status;
	}
	public String getImageSource()
	{
		return ImageSource;
	}
	public ArrayList<Location> getLegalMovements()
	{
		return LegalMovements;
	}
	public void setCurrentLocation(Location obj)
	{
		this.currentLocation=obj;
	}
	public void setStatus(boolean status)
	{
		this.status=status;			
	}
	public abstract void calculateLegalMovements();
}
