import java.io.*;

public class InviteGroup implements Serializable{
	public int group, meeting;
	public InviteGroup(int g, int m){
		this.group = g;
		this.meeting = m;
	}
}
