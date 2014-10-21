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

public class ClientThread implements Runnable {
	Thread t;
	Socket clientSocket;
	int clientId;
	DatabaseInterface database;
	
	ObjectInputStream in;
	ObjectOutputStream out;
	ConcurrentHashMap<Integer, ClientData> clients;

	public ClientThread(Socket socket, int n, DatabaseInterface database, ConcurrentHashMap<Integer, ClientData> cl){
		t = new Thread(this, "clientThread");

		clientSocket = socket;
		clientId = n;
		clients = cl;
		this.database = database;
		this.clients = clients;

		t.start();
	}

	public void run() {
		
		System.out.println("Client in " + clientId + " " + "is running...");
		
		try {
			DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
			out = new ObjectOutputStream(dos);
			clients.get(clientId).out = out;

			DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
			in = new ObjectInputStream(dis);
			clients.get(clientId).in = in;

		} catch (IOException e) {
			System.out.println("Error:IO Exception while answering request of client "+clientId +".");
		}
		
		while(true){
			try {
				Object req = (Object) in.readObject();

				Object r = null;
				
				if(req instanceof Request){
					Request rq = (Request) req;
					if(rq.type.equals("meetings")){
						System.out.println("Getting meetings");
						r = database.getMeetings();
						System.out.println("Obtained meetings");
					}else if(rq.type.equals("meeting")){
						int id = ((Request)rq).id;
						r = database.getMeeting(id);
						System.out.println( ((Meeting)r).title);
					}
				}
				if(req instanceof Authentication){ //A ideia do Rui de devolver a classe com um campo alterado pode ser usada no resto, por exemplo ao fazer um add, completa-se os campos
					System.out.println("Trying to login");
					r = database.login((Authentication)req);
					clients.get(clientId).userData = ((Authentication)r).userData;
				}

				
				if(req instanceof Meeting){
					Meeting m = (Meeting) req;
					if(m.idmeeting == 0){
						Meeting result = database.insertMeeting(m, clients.get(clientId).userData);
						if(result != null)
							broadcastMessage(result, "meetings", 0);
					}else{
						//boolean result = database.updateMeeting(m);
					}
				}

				if(r != null){
					out.writeObject(r);
					if(req instanceof Request)
						System.out.println("Wrote " + ((Request)req).type + " to client " + clientId);
				}/*else{
					System.out.println("Error accessing RMI");
				}*/
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

		System.out.println("Broadcast");
		while(it.hasNext()){
			Integer key = (Integer) it.next();
			System.out.println("Key: " + key);
			cd = clients.get(key);
			if(cd.userData != null)
				try{
					cd.out.writeObject(message);
				}catch(IOException e){System.out.println("IO Exception"); e.printStackTrace();}
			/*if(cd.context.equals(context) && cd.contextId == contextId)
				cd.out.writeObject(message);*/
		}

	}
}
