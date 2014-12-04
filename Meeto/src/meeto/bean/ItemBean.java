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
import meeto.garbage.Meeting;
import meeto.garbage.User;

public class ItemBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database;
	private int idmeeting, iditem;
	
	public void setIditem(int iditem) {
		this.iditem = iditem;
	}

	public void setIdmeeting(int idmeeting) {
		this.idmeeting = idmeeting;
	}

	public ItemBean(){
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public ArrayList<Item> getItemsFromMeeting(){
		Meeting mt = null;
		ArrayList<Item> items = null;
		try {
			mt = database.getMeeting(idmeeting);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return items;
		}
		
		items = mt.items;
		
		return items;
	}
	
	public Item getItem(){
		Item it= null;
		
		try {
			it= database.getItem(iditem);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return it;
	}
	
	public int createItem(String title, String description, int iduser, int idmeeting){
		
		User user;
		try {
			user = database.getUser(iduser);
			Item it = new Item(-1, title,  description,  user,  idmeeting);
			database.insertItem(it);
			return 0;
			
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
		

	}
	
	public int editItem(int iditem, String title, String description, int iduser, int idmeeting){
		User user;
		try {
			user = database.getUser(iduser);
			Item it = new Item(iditem,title,description,user,idmeeting);
			database.updateItem(it);
			return 0;
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		}
		
	}
	
	public int commentOnItem(String text, int iduser, int iditem){
		Item item = null;
		
		item = getItem();
		
		Comment cmt = new Comment(text, item);
		
		User usr;
		try {
			usr = database.getUser(iduser);
			database.insertComment(cmt, usr);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}	
	
	}
	
}
