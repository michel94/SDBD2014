import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.sql.*;
import java.util.Calendar;

public interface DatabaseInterface extends Remote{
	public Meetings getMeetings(int iduser) throws RemoteException;
	public Meeting getMeeting(int idmeeting) throws RemoteException;
	public Item getItem(int id) throws RemoteException;
	
	public Authentication login(Authentication auth) throws RemoteException;
	
	public Meeting insertMeeting(Meeting m, User u) throws RemoteException;
	public Item insertItem(Item it, User u) throws RemoteException;
	public Comment insertComment(Comment com, User u) throws RemoteException;
	
	public Meeting updateMeeting(Meeting m, User u) throws RemoteException;
	public Item updateItem(Item it, User u) throws RemoteException;
	//public Comment updateComment(Comment com, User u) throws RemoteException;
}
