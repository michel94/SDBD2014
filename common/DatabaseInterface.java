import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.sql.*;
import java.util.Calendar;

public interface DatabaseInterface extends Remote{
	public Meeting getMeeting(int idmeeting) throws RemoteException;
	public Meetings getMeetings(int iduser) throws RemoteException;
	public Meetings getFinishedMeetings(int iduser) throws RemoteException;
	public int insertMeeting(Meeting meeting) throws RemoteException;
	public int updateMeeting(Meeting meeting) throws RemoteException;
	public User getUser(int iduser) throws RemoteException;
	//private Users getAllUsersFromMeeting(int idmeeting); FOI REMOVIDA DA INTERFACE
	public Item getItem(int id) throws RemoteException;
	public int insertItem(Item item) throws RemoteException;
	public int addUserToMeeting(User user, Meeting meeting) throws RemoteException;



	public Authentication login(Authentication auth) throws RemoteException;
	public int insertComment(Comment com, User u) throws RemoteException;
	
	public int updateItem(Item it, User u) throws RemoteException;
	//public Comment updateComment(Comment com, User u) throws RemoteException;
}
