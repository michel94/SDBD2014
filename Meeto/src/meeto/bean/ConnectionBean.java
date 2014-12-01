package meeto.bean;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import meeto.garbage.*;

public class ConnectionBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database; 
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
			return database.login(new Authentication(username, password)).confirmation;
		} catch (RemoteException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
