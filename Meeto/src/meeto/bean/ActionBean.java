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
	private int iduser, idaction,done;
	
	public int getIduser() {
		return iduser;
	}

	public int getIdAction() {
		return idaction;
	}

	public ActionBean(int iduser, int idaction,int done){
		this.iduser=iduser;
		this.idaction=idaction;
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public void setIdaction(int idaction) {
		this.idaction = idaction;
	}

	public int assignUserToAction(int iduser, int idaction){
		User usr;
		try {
			usr = database.getUser(iduser);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		Action act;
		try {
			act = database.getAction(idaction);
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

	public int updateAction(String description, String due_to){
		Action act = new Action();
		act.idaction = idaction;
		act.description=description;
		act.due_to=due_to;
		act.done=done;
		
		User usr;
		try {
			usr = database.getUser(iduser);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		act.assigned_user= usr;
		
		try {
			database.updateAction(act);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	
	public int confirmAction(){
		Action act = new Action();
		act.idaction = idaction;
		try {
			database.confirmAction(act);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public Actions getUserActions(){
		Actions acts;
		try {
			acts=database.getUserActions(iduser);
			return acts;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Action getAction(){
		Action act;
		try {
			act=database.getAction(idaction);
			return act;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
