import java.io.Serializable;

public class Action implements Serializable{

	public String due_to, description, created_datetime;
	public int done,idaction, active; // 1-done, 0-not done
	public Meeting meeting;
	public User assigned_user; //assigned user
	private static final long serialVersionUID = 1L;

	public Action(){	
	}

	public Action(int idaction, String description, String due_to, User assigned_user, int done, Meeting meeting, int active){
		this.idaction = idaction;
		this.description = description;
		this.due_to = due_to;
		this.assigned_user = assigned_user;
		this.done = done;
		this.meeting = meeting;
		this.active = active;
	}
	

}
