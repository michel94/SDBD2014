import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.sql.*;
import java.util.Calendar;

public interface DatabaseInterface extends Remote{
	public ArrayList<Meeting> getMeetings() throws RemoteException;
	public boolean addMeeting(String title, String description, Date datetime, String location, int leader) throws RemoteException;

}
