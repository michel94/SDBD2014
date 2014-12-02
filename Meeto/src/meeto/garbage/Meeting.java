package meeto.garbage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Meeting implements Serializable{
	public String title, description, datetime, location, created_datetime;
	public int idmeeting;
	public User leader;
	public int active, finished;

	public ArrayList<Item> items;
	public ArrayList<Action> actions;
	public Users users;
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

	public Meeting(int idmeeting, String title, String description, String datetime, String location, User leader, int active){ //construtor do Bhovan :)
		this.title = title;
		if(idmeeting != -1){
			this.idmeeting = idmeeting;
		}
		this.description = description;
		this.datetime = datetime;
		this.location = location;
		this.leader = leader;
		this.active = active;
		items = new ArrayList<Item>();
		actions = new ArrayList<Action>();
	}
	
	public String getTitle(){
		return title;
	}
	
<<<<<<< HEAD
	public ArrayList<Item> getItems(){
		return items;
	}
	
=======
	public String getId(){
		return Integer.toString(idmeeting);
	}
	public String getDescription(){
		return description;
	}
	public String getDatetime(){
		return datetime;
	}
	public String getLocation(){
		return location;
	}
	public  ArrayList<Item> getItems(){
		
		return items;
	}
	public ArrayList<Action> getAction(){
		
		return actions;
	}
	
}