import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
	public int id;
	public String username, password;

	public User(int id, String username){
		this.id = id;
		this.username = username;

	}
}