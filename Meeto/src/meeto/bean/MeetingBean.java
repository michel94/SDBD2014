package meeto.bean;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import meeto.garbage.DatabaseInterface;
import meeto.garbage.Meeting;
import meeto.garbage.Meetings;

public class MeetingBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	
	private Meetings meetings;
	private int meetingid;
	

	private Meeting meeting;
	
	private DatabaseInterface database;
	private Map<String, Object> session;
	
	public MeetingBean(){
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	
	public Meetings getAllMeetings(){
		try {
			meetings = database.getMeetings((int)session.get("iduser"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return meetings;
	}

	
	public Meeting getMeeting(){
		try {
			meeting = database.getMeeting(meetingid);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return meeting;
	}
	
	public void setSession(Map<String, Object> s){
		session = s;
	}
	
	public void setMeetingid(int meetingid) {
		this.meetingid = meetingid;
	}

}
