import java.io.*;

public class ClientData{
	
	public String context;
	public int contextId;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	public boolean loggedIn;
	public User userData;

	public ClientData(ObjectOutputStream out, ObjectInputStream in){
		this.out = out;
		this.in = in;
		this.context = "login";
		loggedIn = false;
	}
}
