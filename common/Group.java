import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable{
	public String name;
	public int idgroup;
	public User leader;
	public ArrayList<User> users;

	private static final long serialVersionUID = 1L;
	
	public Group(){
		users = new ArrayList<User>();
		idgroup = 0;
	}

	public Group(String name, int idgroup){
		this.name = name;
		this.idgroup = idgroup;
	}

}
