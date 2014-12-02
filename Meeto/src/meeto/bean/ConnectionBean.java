package meeto.bean;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import meeto.garbage.*;
public class ConnectionBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database;
	private Authentication auth;
	private Meetings meetings;
	private Meeting meeting;
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
		System.out.println("rmi m");
		Meetings mts=null;
		try {
			meetings = database.getMeetings(auth.clientID);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getMeetings(){
		rmiMeetings();
		ArrayList<String> meetingsList = new ArrayList<String>();
		System.out.println("get m");
		Iterator<Meeting> it = meetings.iterator();
		while(it.hasNext())
		{
		    Meeting mt = it.next();
		    meetingsList.add(mt.title);
		    
		}
		
		return meetingsList;
	}
	
	private void rmiMeeting(int listIndex){
		
		try {
			meeting = database.getMeeting(meetings.get(listIndex).idmeeting);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
		
}
