import java.io.Serializable;
import java.util.ArrayList;

public class Meeting implements Serializable{
	public String title;
	public int id;
	public ArrayList<Item> items;
	public ArrayList<String> actions;
	private static final long serialVersionUID = 1L;
	
	public Meeting(){

	}

	public Meeting(String title, int id){
		this.title = title;
		this.id = id;
	}
}