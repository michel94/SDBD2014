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
	//public int deleteMeeting(int idmeeting)  throws RemoteException;
	public User getUser(int iduser) throws RemoteException;
	//private Users getAllUsersFromMeeting(int idmeeting); FOI REMOVIDA DA INTERFACE
	public Item getItem(int id) throws RemoteException;
	public int insertItem(Item item) throws RemoteException;
	public int addUserToMeeting(User user, Meeting meeting) throws RemoteException;
	public int finishMeeting(Meeting meeting) throws RemoteException;


	public Action getAction(int idaction) throws RemoteException;
	public int updateItem(Item item) throws RemoteException;
	public int deleteItem(int iditem) throws RemoteException;

	public Authentication login(Authentication auth) throws RemoteException;
	public int insertComment(Comment com, User u) throws RemoteException;


	public int insertKeyDecision(KeyDecision kd) throws RemoteException;
	public int deleteKeyDecision(int idkeydecision) throws RemoteException;
	public int updateKeyDecision(KeyDecision kd) throws RemoteException;
	public Users getAllUsers() throws RemoteException;

	public int insertAction(Action act) throws RemoteException;
	public int assignUserToAction(Action act) throws RemoteException;
	public int updateAction(Action act) throws RemoteException;
	public int deleteAction(int idaction) throws RemoteException;
	
	public int inviteUsers(InviteUsers iu) throws RemoteException;
	public User createAccount(User u) throws RemoteException;

	public Groups getGroupsOfUser(int iduser) throws RemoteException;

	//public Comment updateComment(Comment com, User u) throws RemoteException;
}
