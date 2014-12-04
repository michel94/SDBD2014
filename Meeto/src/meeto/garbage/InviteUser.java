package meeto.garbage;
import java.io.*;

public class InviteUser implements Serializable{
	private static final long serialVersionUID = 1L;
	public int user, id;
	
	public int getUser() {
		return user;
	}

	public int getId() {
		return id;
	}

	public InviteUser(int user, int id){
		this.user = user;
		this.id = id;
	}
}
