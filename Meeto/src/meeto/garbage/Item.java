package meeto.garbage;
import java.io.Serializable;

public class Item implements Serializable{
	public String title, description;
	public int iditem, meeting;
	public User user;
	public Comments comments;
	public KeyDecisions keydecisions;

	private static final long serialVersionUID = 1L;
	
	
	public Item(){
		comments = new Comments();
		keydecisions = new KeyDecisions();
		this.iditem = 0;
	}

	public Item(int iditem, String title, String description, User user, int meeting){
		this.title = title;
		this.iditem = iditem;
		this.description = description;
		this.user = user;
		this.meeting = meeting;
		this.comments = new Comments();
		this.keydecisions = new KeyDecisions();
	}
	
	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}


	public int getId() {
		return iditem;
	}


	public int getMeeting() {
		return meeting;
	}


	public User getUser() {
		return user;
	}


	public Comments getComments() {
		return comments;
	}

	public KeyDecisions getKeydecisions() {
		return keydecisions;
	}
	
}
