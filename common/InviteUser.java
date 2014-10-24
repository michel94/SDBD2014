import java.io.*;

public class InviteUser implements Serializable{
	public int user, meeting;
	public InviteUser(int user, int meeting){
		this.user = user;
		this.meeting = meeting;
	}
}