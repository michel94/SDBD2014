package meeto.garbage;
import java.io.*;

public class InviteGroup implements Serializable{
	public int group, meeting;
	public InviteGroup(int m, int g){
		this.group = g;
		this.meeting = m;
	}

	public int getGroup() {
		return group;
	}
	public int getMeeting() {
		return meeting;
	}
}
