import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{
	public String text;
	public int commentId;
	public String datetime;
	public String user;
	private static final long serialVersionUID = 1L;
	
	public Comment(String text, String user, int commentId, String date){
		this.text = text;
		this.user= user;
		commentId = commentId;
		this.datetime = date;
	}
}
