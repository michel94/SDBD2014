package meeto.bean;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import meeto.garbage.Comment;
import meeto.garbage.DatabaseInterface;
import meeto.garbage.Item;
import meeto.garbage.KeyDecisions;
import meeto.garbage.Meeting;
import meeto.garbage.User;

public class ItemBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database;
	private Map<String, Object> session;
	private int iditem, iduser;
	
	
	public ItemBean(int iduser, int iditem){
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			this.iditem = iditem;
			this.iduser = iduser;
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public Item getItem(){
		Item it= null;
		
		try {
			it = database.getItem(iditem);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return it;
	}
	
	public int editItem(String title, String description, int userid){
		User user;
		
		try {
			user = database.getUser(userid);
			Item it = new Item(iditem,title,description,user, getItem().meeting);
			database.updateItem(it);
			return 0;
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}		
	}
	public int deleteItem(){
		try {
			
			database.deleteItem(iditem);
			return 0;
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}		
	}
	
	public int commentOnItem(String text, int userid){
		Item item = null;
		item = getItem();
		
		Comment cmt = new Comment(text, item);
		
		User usr;
		try {
			usr = database.getUser(userid);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		try {
			database.insertComment(cmt, usr);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	
	}
	
	
}