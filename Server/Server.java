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
import java.util.concurrent.ConcurrentHashMap;

public class Server{
	private int clientNumber = 0;
	private int serverPort = 6000;
	private Boolean second;
	private DatabaseInterface database = null;
	private ConcurrentHashMap<Integer, ClientData> clients;


	protected Server(boolean s, int port){
		super();
		

		clients = new ConcurrentHashMap<Integer, ClientData>();

		serverPort = port;
		second = s;


		UdpConnection u = new UdpConnection(second);


		if(second){
			try{
				System.out.println("Waiting");
				synchronized(second){
					second.wait();
				}
				System.out.println("Ended Waiting");
			}catch(InterruptedException e){e.printStackTrace();}
		}
		connectDatabase();
		clientListener();
	}

	public void connectDatabase(){
		int retries = 0;
		
		try{
			while(retries<5){
				try{
				 	database = (DatabaseInterface) Naming.lookup("database");
				 	System.out.println("Connected to the database");
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

			System.out.println("Listening to port " + serverPort);
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
				new OutputThread(clientSocket, clientNumber, opipein, database, clients);
				clients.put(clientNumber, new ClientData(opipeout, opipein));

				clientNumber++;
				
				
			}
		}catch(IOException e)
		
		{
			System.out.println("Listen:" + e.getMessage());
		}

	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		boolean primary=false, second=false;
		int port=6000;

		for(int i=0; i<args.length; i++){
			if(args[i].equals("-p"))
				primary = true;
			else if(args[i].equals("-s"))
				second = true;
			else{
				try{
					port = Integer.parseInt(args[i]);
				}catch(NumberFormatException e){
					;
				}
			}
		}
		if(primary && second){
			System.out.println("The server cannot be primary and secondary at the same time");
			return;
		}


		Server server = new Server(second, port);
			

	}

}
