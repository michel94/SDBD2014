import java.io.*;

public class KeyDecision implements Serializable{
	public int idkeydecision, active;
	public int item;
	public String description;
	public String datetime;
	

	public KeyDecision(){
		idkeydecision = 0;
	}
	public KeyDecision(int idkeydecision, String description, int active, int item){
		this.idkeydecision = idkeydecision;
		this.description = description;
		this.item = item;
	}
}
