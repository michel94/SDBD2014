package meeto.bean;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import meeto.garbage.DatabaseInterface;
import meeto.garbage.InviteUser;
import meeto.garbage.InviteUsers;
import meeto.garbage.Meeting;
import meeto.garbage.Meetings;
import meeto.garbage.User;
import meeto.garbage.Users;

public class MeetingBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	
	private Meetings meetings;
	private Meeting meeting;
	
	private DatabaseInterface database;
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
			meetings = database.getMeetings(userid);
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
	
	
	public Users getUsersFromMeeting(int idmeeting){
		Users usrs=null;
		Meeting mt=null;
		try {
			mt=database.getMeeting(idmeeting);
			usrs = mt.users;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return usrs;
	}
	
	
	public int addUserToMeeting(int userid, int meetingid){
		User usr = null;
		Meeting mt = null;
		
		try {
			usr = database.getUser(userid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		try {
			mt = database.getMeeting(meetingid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		try {
			database.addUserToMeeting(usr, mt, null);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public int addUsersToMeeting(ArrayList<String> userids, int meetingid){
		InviteUsers invus = new InviteUsers();
		InviteUser invu = null;
		
		for(int i=0;i<userids.size();i++){
			invu = new InviteUser(Integer.parseInt(userids.get(i)),meetingid);
			invus.add(invu);
		}
		
		try {
			database.inviteUsersToMeeting(invus);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public int addGroupToMeeting(int idgroup, int idmeeting){
		try {
			database.addGroupToMeeting(idmeeting, idgroup);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
	}
	
}
