package meeto.bean;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import meeto.garbage.DatabaseInterface;
import meeto.garbage.User;
import meeto.garbage.Users;

public class UserBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	
	private DatabaseInterface database;

	
	private int userid;
	
	public UserBean(){
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public void setUserid(int userid) {
		this.userid = userid;
	}

	public User getUser(){
		User usr=null;
		try {
			usr=database.getUser(userid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return usr;
	}
	
	public Users getAllUsers(){
		Users usrs = null;
		
		try {
			usrs=database.getAllUsers();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return usrs;
	}
	
	
	public int registerUser(String username, String password){
		User usr=new User(username,password);
		
		try {
			database.createAccount(usr);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	

}
