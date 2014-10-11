import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Database extends UnicastRemoteObject implements DatabaseInterface{
	protected Database() throws RemoteException {
		super();
		//initialize JDBC
	}
	public ArrayList<String> getMeetings(){

		return new ArrayList<String>();
	}
	public static void main(String[] args) throws RemoteException {
		DatabaseInterface di = new Database();
		LocateRegistry.createRegistry(1099).rebind("database", di);
		System.out.println("Database ready...");
	}
}