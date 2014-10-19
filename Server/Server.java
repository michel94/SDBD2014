import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.sql.Date;
import java.util.*;
import java.lang.*;

public class Server{
	private int clientNumber = 0;
	private int serverPort = 6000;
	DatabaseInterface database = null;

	protected Server() {
		super();
		DatabaseInterface database = null;
				
		connectDatabase();
		clientListener();
	}

	public static void main(String[] args) {
		Server server = new Server();
		
	}

	public void connectDatabase(){
		int retries = 0;
		
		try{
			while(retries<5){
				try{
				 	database = (DatabaseInterface) Naming.lookup("database");
					break;
				}catch(NotBoundException e){
					System.out.println("Database could not be reached. Failed connection. Retrying connection in 10 seconds.");
					retries++;
					Thread.sleep(10000);
				}catch(MalformedURLException e){
					System.out.println("Could not find the the database hostname. Retrying connection in 10 seconds.");
					retries++;
					Thread.sleep(10000);		
				}catch(RemoteException e){
					System.out.println("Remote exception. Retrying connection in 10 seconds.");
					retries++;
					Thread.sleep(10000);	
				}
			}
		}catch(InterruptedException ex) {
    			Thread.currentThread().interrupt();
		}

		if(retries == 5){
			System.out.println("Database could not be reached after 1 minute. Closing server.");
			System.exit(0);
		}
		System.out.println("Connection to database established.");
	}
	
	public void clientListener(){
		ObjectOutputStream opipeout;
		ObjectInputStream opipein;
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

}
