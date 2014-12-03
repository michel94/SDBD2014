package meeto.bean;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import meeto.garbage.Action;
import meeto.garbage.Actions;
import meeto.garbage.DatabaseInterface;
import meeto.garbage.User;

public class ActionBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database;
	private Map<String, Object> session;
	
	public ActionBean(){
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public int insertAction(String description, String due_to, int userid, int meetingid){
		Action act = new Action();
		
		act.description=description;
		act.due_to=due_to;
		
		User usr;
		try {
			usr = database.getUser(userid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		act.assigned_user= usr;
		act.meeting= meetingid;
		
		try {
			database.insertAction(act);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}

	public int assignUserToAction(int userid, int actionid){
		User usr;
		try {
			usr = database.getUser(userid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		Action act;
		try {
			act = database.getAction(actionid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		act.assigned_user=usr;
		try {
			database.assignUserToAction(act);
			return 1;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}

	public int updateAction(int actionid,String description, String due_to, int userid, int meetingid){
		Action act = new Action();
		act.idaction = actionid;
		act.description=description;
		act.due_to=due_to;
		
		User usr;
		try {
			usr = database.getUser(userid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		act.assigned_user= usr;
		act.meeting= meetingid;
		
		try {
			database.updateAction(act);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	
	public int confirmAction(int actionid){
		Action act = new Action();
		act.idaction = actionid;
		try {
			database.confirmAction(act);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public Actions getUserActions(int userid){
		Actions acts;
		try {
			acts=database.getUserActions(userid);
			return acts;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
