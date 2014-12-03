package meeto.garbage;
import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable{
	public String title, description;
	public int iditem, meeting;
	public User user;
	public Comments comments;
	public KeyDecisions keydecisions;

	private static final long serialVersionUID = 1L;
	
	/*public Item(String title, int iditem){
		this.title = title;
		this.iditem = iditem;
		comments = new ArrayList<Comment>();
		decisions = new ArrayList<KeyDecision>();
	}
	*/
	public Item(){
		comments = new Comments();
		keydecisions = new KeyDecisions();
		this.iditem = 0;
	}
	/*
	public Item(int id, String title){
		this.title = title;
		this.id = id;
		comments = new ArrayList<Comment>();
		decisions = new ArrayList<KeyDecision>();
	}*/

	public Item(int iditem, String title, String description, User user, int meeting){
		this.title = title;
		this.iditem = iditem;
		this.description = description;
		this.user = user;
		this.meeting = meeting;
		this.comments = new Comments();
		this.keydecisions = new KeyDecisions();
	}
	
	public String getTitle(){
		return title;
	}
}
