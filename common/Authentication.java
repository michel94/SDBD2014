import java.io.*;

public class Authentication implements Serializable {
	private static final long serialVersionUID = 1L;
	public String username,password;
	public int confirmation = 0;
	public int clientID = 0;
	public User userData;
	
	public  Authentication(String usr, String pass){
		username = usr;
		password = pass;
	}
	
	public void confirm(int c){
		confirmation = 1;
		clientID = c;
	}
}
