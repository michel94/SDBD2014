import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerInterface, Runnable {
	/**
	 * 
	 */

	protected Server() throws RemoteException {
		super();
	}

	public static void main(String[] args) throws RemoteException {
		ServerInterface si = new Server();
		LocateRegistry.createRegistry(6000).rebind("serv", si);
		System.out.println("Server ready");
	}

}