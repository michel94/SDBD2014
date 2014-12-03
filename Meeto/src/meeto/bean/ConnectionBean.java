package meeto.bean;

import java.net.MalformedURLException;
import java.util.Map;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


import meeto.garbage.*;
public class ConnectionBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database;

	private Item item;
	//selectors
	private int selectMeeting=0, selectItem=0, selectAction=0;
	
	private Map<String, Object> session;
	
	public ConnectionBean(){
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public DatabaseInterface getConnection(){
		return database;
	}
	public Authentication login(String username, String password){
		Authentication auth = new Authentication(username, password); 
		try {
			auth = database.login(auth);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return auth;
	}

	public void setSession(Map<String, Object> s){
		session = s;
	}
	
	public Actions getUserActions(){
		try {
			return database.getUserActions((int)session.get("iduser"));
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
