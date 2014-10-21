import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable{
	public String title, description;
	public int id, user, meeting;
	public ArrayList<Comment> comments;
	public ArrayList<KeyDecision> decisions;

	private static final long serialVersionUID = 1L;
	
	public Item(String title, int id){
		this.title = title;
		this.id = id;
		comments = new ArrayList<Comment>();
		decisions = new ArrayList<KeyDecision>();
	}

	public Item(int id, String title){
		this.title = title;
		this.id = id;
		comments = new ArrayList<Comment>();
		decisions = new ArrayList<KeyDecision>();
	}

	public Item(int iditem, String title, String description, int user, int meeting){
		this.title = title;
		this.id = iditem;
		this.description = description;
		this.user = user;
		this.meeting = meeting;
		comments = new ArrayList<Comment>();
		decisions = new ArrayList<KeyDecision>();
	}
}
