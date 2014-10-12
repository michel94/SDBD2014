//package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;


class NewThread implements Runnable {
	  String name;
	  Thread t;
	  Socket clientSocket;
	  int cnumber;
	  DataInputStream in;
	  DataOutputStream out;
	  ObjectInputStream oin,pipein;
	  ObjectOutputStream oout,pipeout;	  
	  
	  NewThread(String threadname, Socket c, int n, ObjectOutputStream os, ObjectInputStream is) {
	    name = threadname;
	    t = new Thread(this, name);
	    cnumber = n;    
	    t.start(); // Start the thread
	    clientSocket = c;
	    pipein=is;
	    pipeout = os;
	  }
	  
	  public void run() {      // entry point
		  Object data;
		  System.out.println(name +" "+cnumber+" "+ "is running...");
		  if(name.equals("ClientIN")){
			  try {
				  
				in = new DataInputStream(clientSocket.getInputStream());
				oin = new ObjectInputStream(in);
				
				while(true){
					try {
						data = oin.readObject();
						System.out.println("Received some shit...");
						pipeout.writeObject(data);
						System.out.println("Wrote on pipe of client "+cnumber +"...");
						
					} catch (ClassNotFoundException e) {
						System.out.println("Error: Class not found while reading request from client "+cnumber +".");
					}
				}
			} catch (IOException e) {
				System.out.println("Error:IO Exception while reading request of client "+cnumber +".");
			}
			  
			  System.out.println("Closing connection from client "+cnumber +".");
			 
		  }
		  else{
			  
			  try {
				out = new DataOutputStream(clientSocket.getOutputStream());
				oout = new ObjectOutputStream(out);
			} catch (IOException e) {
				System.out.println("Error:IO Exception while answering request of client "+cnumber +".");
			}
			
			while(true){
				try {
					data = pipein.readObject();
				} catch (ClassNotFoundException e) {
					System.out.println("Error: Class not found while reading pipe of client "+cnumber +".");
				} catch (IOException e) {
					System.out.println("Error:IO Exception while reading pipe of client "+cnumber +". Please reset connection with server.");
					break;
				}
				System.out.println("Reading from pipe of client "+cnumber +"... Sending request to DB...");
			}
			
			System.out.println("Closing connection to client "+cnumber +".");
			
		  }
		  
	  }
	}

public class Server{
	static int ClientNumber = 1;
	protected Server() throws RemoteException {
		super();
	}

	public static void main(String[] args) throws RemoteException {

		ObjectOutputStream opipeout;
		ObjectInputStream opipein;
		PipedOutputStream pipeout;
		PipedInputStream pipein;
		Socket clientSocket;
		try{
            int serverPort = 6000;
            System.out.println("Listenning to port 6000");
            ServerSocket listenSocket = new ServerSocket(serverPort);         
            System.out.println("Server ready");
            
            while(true) {
                 clientSocket = listenSocket.accept();
                 pipeout = new PipedOutputStream();
                 pipein = new PipedInputStream(pipeout);
				opipeout =new ObjectOutputStream(new DataOutputStream(pipeout));
                opipein = new ObjectInputStream(new DataInputStream(pipein));
				
                new NewThread("ClientIN",clientSocket,ClientNumber,opipeout,opipein);
                new NewThread("ClientOUT",clientSocket,ClientNumber,opipeout,opipein);
                ClientNumber++;    
            }
        }catch(IOException e)
		
        {System.out.println("Listen:" + e.getMessage());}
		
		
		
	}

}