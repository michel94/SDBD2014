import java.io.Serializable;

public class Item implements Serializable{
	public String title, description;
	public int idItem;
	public ArrayList<Comment> comments;
	public ArrayList<Decision> decisions;
	public 

	private static final long serialVersionUID = 1L;
	
	public Item(String title, int id){
		this.title = title;
		this.id = id;
	}
}