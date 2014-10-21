import java.io.Serializable;
import java.util.ArrayList;

public class Meeting implements Serializable{
	public String title, description, datetime, location, created_datetime;
	public int idmeeting;
	public User leader;
	public int active;

	public ArrayList<Item> items;
	public ArrayList<Action> actions;
	private static final long serialVersionUID = 1L;
	
	public Meeting(){
		items = new ArrayList<Item>();
		actions = new ArrayList<Action>();
		idmeeting = 0;
	}

	public Meeting(String title, int idmeeting){
		this.title = title;
		this.idmeeting = idmeeting;
		items = new ArrayList<Item>();
		actions = new ArrayList<Action>();
	}

	public Meeting(int idmeeting, String title, String datetime, String location, int active){ //construtor do Bhovan :)
		this.title = title;
		this.idmeeting = idmeeting;
		this.datetime = datetime;
		this.location = location;
		this.active = active;
		items = new ArrayList<Item>();
		actions = new ArrayList<Action>();
	}
}