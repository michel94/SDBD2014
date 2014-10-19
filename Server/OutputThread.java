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
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;

public class OutputThread implements Runnable {
	Thread t;
	Socket clientSocket;
	int clientId;
	DatabaseInterface database;
	
	ObjectInputStream threadIn;
	ObjectOutputStream out;
	ConcurrentHashMap<Integer, ClientData> clients;

	public OutputThread(Socket c, int n, ObjectInputStream is, DatabaseInterface database, ConcurrentHashMap<Integer, ClientData> clients){
		t = new Thread(this, "out");
		clientId = n;
		clientSocket = c;
		threadIn = is;
		this.database = database;
		this.clients = clients;

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
				Object req = (Object) threadIn.readObject();

				Object r = null;
				
				if(req instanceof Request){
					Request rq = (Request) req;
					if(rq.type.equals("meetings")){
						System.out.println("Getting meetings");
						r = database.getMeetings();
						System.out.println("Obtained meetings");
					}else if(rq.type.equals("meeting")){
						r = database.getMeeting((Request)rq);
						System.out.println( ((Meeting)r).title);
					}
				}
				if(req instanceof Authentication){
					System.out.println("Trying to login");
					r = database.login((Authentication)req);
				}

				if(r != null){
					out.writeObject(r);
					if(req instanceof Request)
						System.out.println("Wrote " + ((Request)req).type + " to client " + clientId);
				}else{
					System.out.println("Error accessing RMI");
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

	public void parseRequest(){
		
	}

	public void broadcastMessage(Object message, String context){
		broadcastMessage(message, context, 0);
	}	

	public void broadcastMessage(Object message, String context, int contextId){
		Iterator it = clients.keySet().iterator();
		ClientData cd;

		try{
			while(it.hasNext()){
				Integer key = (Integer) it.next();
				cd = clients.get(key);
				if(cd.context.equals(context) && cd.contextId == contextId)
					cd.out.writeObject(message);
			}
		}catch(IOException e){System.out.println("IO Exception"); e.printStackTrace();}
	}
}
