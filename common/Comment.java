import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{
	public String text;
	public int commentId;
	public Date date;
	public User user;
	private static final long serialVersionUID = 1L;
	
	public Comment(String text, User user, int commentId, Date date){
		this.text = text;
		this.user= user;
		commentId = commentId;
		this.date = date;
	}
}