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
import java.util.ArrayList;

public class OutputThread implements Runnable {
	Thread t;
	Socket clientSocket;
	int clientId;
	DatabaseInterface database;
	
	ObjectInputStream threadIn;
	ObjectOutputStream out;

	public OutputThread(Socket c, int n, ObjectInputStream is, DatabaseInterface database){
		t = new Thread(this, "out");
		clientId = n;
		clientSocket = c;
		threadIn = is;
		this.database = database;

		t.start();
	}

	public void run() {
		
		System.out.println("Client in " + clientId + " " + "is running...");
		
		try {
			DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
			out = new ObjectOutputStream(dos);
		} catch (IOException e) {
			System.out.println("Error:IO Exception while answering request of client "+clientId +".");
		}
		
		while(true){
			try {
				Request req = (Request) threadIn.readObject();

				Object r = null;
				System.out.println(req.type);
				
				if(req.type.equals("meetings")){
					System.out.println("Getting meetings");
					r = database.getMeetings();
					System.out.println("Obtained meetings");
				}else if(req.type.equals("meeting")){
					r = database.getMeeting(req);
					System.out.println( ((Meeting)r).title);
				}

				if(r != null){
					out.writeObject(r);
					System.out.println("Wrote " + req.type + " to client " + clientId);
				}else{
					System.out.println("Error obtain meetings through RMI");
				}
			} catch (ClassNotFoundException e) {
				System.out.println("Error: Class not found while reading pipe of client "+clientId +".");
			} catch (IOException e) {
				System.out.println("Error:IO Exception while reading pipe of client "+clientId +". Please reset connection with server.");
				break;
			}
		}
		
		System.out.println("Closing connection to client "+clientId +".");
		
	}
}
