import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.sql.*;
import java.util.Calendar;

public interface DatabaseInterface extends Remote{
	public Meetings getMeetings() throws RemoteException;
	public Meeting getMeeting(Request r) throws RemoteException;
	public Authentication login(Authentication auth) throws RemoteException;
	public Meeting insertMeeting(Meeting m, User u) throws RemoteException;


}
