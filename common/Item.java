import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable{
	public String title, description;
	public int id;
	public ArrayList<Comment> comments;
	public ArrayList<KeyDecision> decisions;
	public ArrayList<User> usersThatCommented;

	private static final long serialVersionUID = 1L;
	
	public Item(String title, int id){
		this.title = title;
		this.id = id;
	}
}