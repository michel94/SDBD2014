package meeto.garbage;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
	public int iduser;
	public String username, password;

	public User(int iduser, String username){
		this.iduser = iduser;
		this.username = username;

	}
	public User( String username, String password){
		this.password = password;
		this.username = username;

	}
	public User(){
		iduser = 0;
		username = "";
		password = "";
	}
}
