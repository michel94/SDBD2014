package meeto.bean;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import meeto.garbage.DatabaseInterface;
import meeto.garbage.Meeting;
import meeto.garbage.Meetings;
import meeto.garbage.User;

public class MeetingBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	
	private Meetings meetings;
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
	
	
	public Meetings getAllMeetings(int userid){
		try {
			meetings = database.getMeetings((int)session.get("iduser"));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return meetings;
	}

	
	public Meeting getMeeting(int meetingid){
		try {
			meeting = database.getMeeting(meetingid);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return meeting;
	}
	
	public int createMeeting(String title, String description, String datetime, String location, User leader){
		Meeting mt = new Meeting(-1,title,description,datetime,location,leader,1);
		try {
			database.insertMeeting(mt);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int editMeeting(int idmeeting, String title, String description, String datetime, String location, User leader){
		Meeting mt = new Meeting(idmeeting,title,description,datetime,location,leader,1);
		try {
			database.updateMeeting(mt);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int closeMeeting(int idmeeting){
		Meeting mt = new Meeting();
		mt.idmeeting=idmeeting;
		try {
			database.finishMeeting(mt);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public void setSession(Map<String, Object> s){
		session = s;
	}
	
}
