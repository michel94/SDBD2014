package meeto.bean;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.struts2.interceptor.SessionAware;

import meeto.garbage.*;
public class ConnectionBean implements SessionAware {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database;
	private Authentication auth;
	private Meetings meetings;
	private Meeting meeting;
	private String choice;
	private Item item;
	
	//Selectors:
	private int selectMeeting=0,selectAction=0,selectItem=0;
	
	private Map<String, Object> session;
	
	public ConnectionBean(){
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			//
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public DatabaseInterface getConnection(){
		return database;
	}
	public int login(String username, String password){
		try {
		  auth=database.login(new Authentication(username, password));
		  return auth.confirmation;
		  
		} catch (RemoteException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private void rmiMeetings(){
		
		Meetings mts=null;
		try {
			meetings = database.getMeetings(auth.clientID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Meetings getMeetings(){
		rmiMeetings();
		return meetings;
	}
	
	private void rmiMeeting(int listIndex){
		try {
			System.out.println(meetings.size());
			int id = meetings.get(listIndex).idmeeting;
			meeting = database.getMeeting(id);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Meeting getMeeting(){
		rmiMeeting(selectMeeting);
		return meeting;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		
		this.session = session;
		
	}
	
	public Item getItem(){
		rmiMeeting(selectMeeting);
		return item;
	}
	
		
}
