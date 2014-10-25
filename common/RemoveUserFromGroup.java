import java.io.*;

public class RemoveUserFromGroup implements Serializable{
	public int user, group;
	public RemoveUserFromGroup(int user, int group){
		this.user = user;
		this.group = group;
	}
}
