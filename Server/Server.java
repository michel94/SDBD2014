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
	    System.out.println("New thread: " + t +" "+ cnumber);
	    t.start(); // Start the thread
	    clientSocket = c;
	  }
	  
	  public void run() {      // entry point
		  System.out.println(name +" "+cnumber+ "is running...");
		  if(name.equals("ClientIN")){
			  try {
				  
				in = new DataInputStream(clientSocket.getInputStream());
				oin = new ObjectInputStream(in);
				
				while(true){
					try {
						data = oin.readObject();
						System.out.println("Received some shit...");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		  }
		  else{
			  try {
				  
				out = new DataOutputStream(clientSocket.getOutputStream());
				oout = new ObjectOutputStream(out);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(true){
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
            System.out.println("LISTEN SOCKET="+listenSocket);
            
            
            //new NewThread("UDP"); // create threads
            
            while(true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                new NewThread("ClientIN",clientSocket,ClientNumber);
                new NewThread("ClientOUT",clientSocket,ClientNumber);
                ClientNumber++;
                System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket);    
            }
        }catch(IOException e)
		
        {System.out.println("Listen:" + e.getMessage());}
		
		
		System.out.println("Server ready");
		
		while(true){
			
		}
	}

}