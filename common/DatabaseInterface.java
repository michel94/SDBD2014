import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.sql.*;
import java.util.Calendar;

public interface DatabaseInterface extends Remote{
	public Meetings getMeetings() throws RemoteException;
	public boolean addMeeting(String title, String description, String datetime, String location, int leader) throws RemoteException;
	public Meeting getMeeting(Request r) throws RemoteException;
	public Authentication login(Authentication auth) throws RemoteException;


}
