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

public class Server{
	private int clientNumber = 0;
	private int serverPort = 6000;
	private Boolean second;

	protected Server(boolean s, int port) throws RemoteException, MalformedURLException, NotBoundException {
		super();

		serverPort = port;
		second = s;


		UdpConnection u = new UdpConnection(second);


		if(second)
			try{
				System.out.println("Waiting");
				synchronized(second){
					second.wait();	
				}
				System.out.println("Ended Waiting");
			}catch(InterruptedException e){e.printStackTrace();}


		ObjectOutputStream opipeout;
		ObjectInputStream opipein;
		DatabaseInterface database = (DatabaseInterface) Naming.lookup("database");


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
				new OutputThread(clientSocket, clientNumber, opipein, database);
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
