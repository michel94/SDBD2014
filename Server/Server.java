package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	  ObjectInputStream oin;
	  ObjectOutputStream oout;	  
	  Object data;
	  
	  NewThread(String threadname, Socket c, int n) {
	    name = threadname;
	    t = new Thread(this, name);
	    cnumber = n;    
	    t.start(); // Start the thread
	    clientSocket = c;
	  }
	  
	  public void run() {      // entry point
		  System.out.println(name +" "+cnumber+" "+ "is running...");
		  if(name.equals("ClientIN")){
			  try {
				  
				in = new DataInputStream(clientSocket.getInputStream());
				oin = new ObjectInputStream(in);
				
				while(true){
					try {
						data = oin.readObject();
						System.out.println("Received some shit...");
					} catch (ClassNotFoundException e) {
						System.out.println("Error: Class not found while reading request from client "+cnumber +".");
					}
				}
			} catch (IOException e) {
				System.out.println("Error:IO Exception while reading request of client "+cnumber +".");
			}
			 
		  }
		  else{
			  try {
				  
				out = new DataOutputStream(clientSocket.getOutputStream());
				oout = new ObjectOutputStream(out);
				
			
			} catch (IOException e) {
				System.out.println("Error:IO Exception while answering request of client "+cnumber +".");
			}
		  }
		  
	  }
	}

public class Server{
	static int ClientNumber = 1;
	protected Server() throws RemoteException {
		super();
	}

	public static void main(String[] args) throws RemoteException {
		
		try{
            int serverPort = 6000;
            System.out.println("Listenning to port 6000");
            ServerSocket listenSocket = new ServerSocket(serverPort);         
            System.out.println("Server ready");
            
            while(true) {
                Socket clientSocket = listenSocket.accept();
                new NewThread("ClientIN",clientSocket,ClientNumber);
                new NewThread("ClientOUT",clientSocket,ClientNumber);
                ClientNumber++;    
            }
        }catch(IOException e)
		
        {System.out.println("Listen:" + e.getMessage());}
		
		
		
	}

}