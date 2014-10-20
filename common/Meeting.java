import java.io.Serializable;
import java.util.ArrayList;

public class Meeting implements Serializable{
	public String title, description, datetime, location;
	public int id;
	public User leader;

	public ArrayList<Item> items;
	public ArrayList<Action> actions;
	private static final long serialVersionUID = 1L;
	
	public Meeting(){
		items = new ArrayList<Item>();
		actions = new ArrayList<Action>();
		id = 0;
	}

	public Meeting(String title, int id){
		this.title = title;
		this.id = id;
		items = new ArrayList<Item>();
		actions = new ArrayList<Action>();
	}
}