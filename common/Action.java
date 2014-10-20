import java.io.Serializable;

public class Action implements Serializable{
	public String duedatetime,description;
	public int status,id; // 1-done, 0-not done
	public User usr; //assigned user
	public Action(int id){
		this.id = id;
	}

}
