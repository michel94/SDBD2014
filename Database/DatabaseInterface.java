import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface DatabaseInterface extends Remote{
	public ArrayList<String> getMeetings() throws RemoteException;
}
