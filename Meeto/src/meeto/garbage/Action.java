package meeto.garbage;
import java.io.Serializable;

public class Action implements Serializable{

	public String due_to, description, created_datetime;
	public int done,idaction, active; // 1-done, 0-not done
	public String getDue_to() {
		return due_to;
	}

	public void setDue_to(String due_to) {
		this.due_to = due_to;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreated_datetime() {
		return created_datetime;
	}

	public void setCreated_datetime(String created_datetime) {
		this.created_datetime = created_datetime;
	}

	public int getDone() {
		return done;
	}

	public void setDone(int done) {
		this.done = done;
	}

	public int getIdaction() {
		return idaction;
	}

	public void setIdaction(int idaction) {
		this.idaction = idaction;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getMeeting() {
		return meeting;
	}

	public void setMeeting(int meeting) {
		this.meeting = meeting;
	}

	public User getAssigned_user() {
		return assigned_user;
	}

	public void setAssigned_user(User assigned_user) {
		this.assigned_user = assigned_user;
	}

	public int meeting;
	public User assigned_user; //assigned user
	private static final long serialVersionUID = 1L;

	public Action(){
		idaction = 0;
	}

	public Action(int idaction, String description, String due_to, User assigned_user, int done, int meeting, int active){
		this.idaction = idaction;
		this.description = description;
		this.due_to = due_to;
		this.assigned_user = assigned_user;
		this.done = done;
		this.meeting = meeting;
		this.active = active;
	}
	

}
