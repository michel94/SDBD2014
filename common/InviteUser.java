import java.io.*;

public class InviteUser implements Serializable{
	public int user, id;
	public InviteUser(int user, int id){
		this.user = user;
		this.id = id;
	}
}
