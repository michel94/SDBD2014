import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{
	public String text;
	public int commentId;
	public User user;
	public Item item;
	public String datetime;
	private static final long serialVersionUID = 1L;

	public Comment(String text, Item item){
		this.text = text;
		this.commentId = 0;
		this.item = item;
	}
	
	public Comment(int idcomment, String text, User user, Item item, String date){
		this.text = text;
		this.user = user;
		commentId = idcomment;
		this.datetime = date;
		this.item = item;
	}
}
