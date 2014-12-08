package meeto.bean;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import meeto.garbage.Action;
import meeto.garbage.DatabaseInterface;
import meeto.garbage.InviteUser;
import meeto.garbage.InviteUsers;
import meeto.garbage.Item;
import meeto.garbage.Meeting;
import meeto.garbage.Meetings;
import meeto.garbage.User;
import meeto.garbage.Users;

public class MeetingBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	
	private Meetings meetings,previousmeetings,nextmeetings;
	private Meeting meeting;
	private int iduser, idmeeting;
	
	private DatabaseInterface database;

	private Map<String, Object> session;
	
	public MeetingBean(int iduser){
		this.iduser = iduser;

		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	private Boolean checkString(String field){
		return field != null && !field.equals("");
	}
	
	public boolean getDisplayMeeting(){
		return idmeeting != 0;
	}
	
	public void setUserId(int iduser){
		this.iduser = iduser;
	}
	
	public void setMeetingId(int idmeeting){
		this.idmeeting = idmeeting;
		System.out.println(idmeeting);
	}
	
	public Meetings getAllMeetings(){
		try {
			meetings = database.getMeetings(iduser);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return meetings;
	}
	
	public Meetings getPreviousMeetings(){
		previousmeetings=new Meetings();
		try {
			meetings = database.getMeetings(iduser);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		String currentDate = getCurrentDate();
		for(int i=0;i<meetings.size();i++){
			if(currentDate.compareTo(meetings.get(i).datetime)>0){
				previousmeetings.add(meetings.get(i));
			}
		}
		
		return previousmeetings;
	}
	
	public Meetings getNextMeetings(){
		nextmeetings=new Meetings();
		try {
			meetings = database.getMeetings(iduser);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		String currentDate = getCurrentDate();
		for(int i=0;i<meetings.size();i++){
			if(currentDate.compareTo(meetings.get(i).datetime)<=0){
				nextmeetings.add(meetings.get(i));
			}
		}
		
		return nextmeetings;
	}
	public Meeting getMeeting(){
		try {
			meeting = database.getMeeting(idmeeting);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return meeting;
	}
	
	public int createMeeting(String title, String description, String datetime, String location, User leader){
		Meeting mt = new Meeting(-1,title,description,datetime,location,leader,1);
		mt.users = new Users();
		
		try {
			database.insertMeeting(mt);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int createAction(String description, String due_to){
		Action act = new Action();
		
		act.description=description;
		act.due_to=due_to;
		
		User usr;
		try {
			usr = database.getUser(iduser);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		act.assigned_user = usr;
		act.meeting = idmeeting;
		
		try {
			database.insertAction(act);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public int editMeeting(String title, String description, String datetime, String location){
		Meeting meeting = getMeeting();
		
		if(!checkString(title))
			title = meeting.title;
		else if(!checkString(description))
			description = meeting.description;
		else if(!checkString(datetime))
			datetime = meeting.datetime;
		else if(!checkString(location))
			location = meeting.location;
		
		System.out.println(idmeeting);
		Meeting mt = new Meeting(idmeeting, title,description,datetime,location, meeting.leader, 1);
		
		try {
			database.updateMeeting(mt);
			
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int createItem(String title, String description){
		
		User user;
		try {
			user = database.getUser(iduser);
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
		
		Item it = new Item(-1, title,  description,  user, idmeeting);
		
		try {
			database.insertItem(it);
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
	
	public int leaveMeeting(int idmeeting,int iduser){
		try {
			database.leaveMeeting(idmeeting, iduser);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	
	public Users getUsersFromMeeting(){
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
	
	
	public int addUserToMeeting(int iduser, int idmeeting){
		User usr = null;
		Meeting mt = null;
		
		try {
			usr = database.getUser(iduser);
			mt = database.getMeeting(idmeeting);
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return 0;
		
	}
	
	public int addUsersToMeeting(ArrayList<String> userids, int idmeeting){
		InviteUsers invus = new InviteUsers();
		InviteUser invu = null;
		
		for(int i=0;i<userids.size();i++){
			invu = new InviteUser(Integer.parseInt(userids.get(i)),idmeeting);
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
	
	public String getCurrentDate(){
		Date d1 = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(d1);
		return formattedDate;
	}
	
}
