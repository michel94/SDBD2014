import java.io.Serializable;

public class Meeting implements Serializable{
	public String title;
	public int id;
	private static final long serialVersionUID = 1L;
	
	public Meeting(String title, int id){
		this.title = title;
		this.id = id;
	}
}