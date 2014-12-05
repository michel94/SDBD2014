package meeto.garbage;
import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{
	public String text;
	public int commentId;
	public User user;
	public Item item;
	public String datetime;
	public String meetingTitle;
	private static final long serialVersionUID = 1L;

	public Comment(String text, Item item){
		this.text = text;
		this.commentId = 0;
		this.item = item;
	}
	
	public String getText() {
		return text;
	}

	public int getCommentId() {
		return commentId;
	}

	public User getUser() {
		return user;
	}

	public Item getItem() {
		return item;
	}

	public String getDatetime() {
		return datetime;
	}

	public String getMeetingTitle() {
		return meetingTitle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Comment(int idcomment, String text, User user, Item item, String date){
		this.text = text;
		this.user = user;
		commentId = idcomment;
		this.datetime = date;
		this.item = item;
	}
	
	public String getUsername(){
		return user.username;
	}
	
	public String getDate(){
		return datetime;
	}
}
