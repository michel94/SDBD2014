import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.sql.Date;
import java.util.*;

public class Server{
	private int clientNumber = 0;
	private int serverPort = 6000;
	public Hashtable<Integer, ClientData> clients = new Hashtable<Integer, ClientData>();

	protected Server() throws RemoteException, MalformedURLException, NotBoundException {
		super();

		ObjectOutputStream opipeout;
		ObjectInputStream opipein;
		DatabaseInterface database = (DatabaseInterface) Naming.lookup("database");
		
		Socket clientSocket;
		try{

			System.out.println("Listening to port 6000");
			ServerSocket listenSocket = new ServerSocket(serverPort);
			System.out.println("Server ready");
			
			while(true){
				clientSocket = listenSocket.accept();

				// create communication between client thread and server thread
				PipedOutputStream pipeout = new PipedOutputStream();
				PipedInputStream pipein = new PipedInputStream(pipeout);
				opipeout = new ObjectOutputStream(new DataOutputStream(pipeout));
				opipein = new ObjectInputStream(new DataInputStream(pipein));

				new InputThread(clientSocket, clientNumber, opipeout);
				new OutputThread(clientSocket, clientNumber, opipein, database);
				clientNumber++;
			}
		}catch(IOException e)
		
		{
			System.out.println("Listen:" + e.getMessage());
		}

	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		Server server = new Server();
		
	}

}
