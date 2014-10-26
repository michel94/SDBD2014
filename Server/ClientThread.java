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
				Object data = (Object) in.readObject();
				System.out.println("Received Object");

				Object r = null;
				Confirmation conf = new Confirmation();
				User userData = clients.get(clientId).userData;

				int qres = -2;

				if(data instanceof Request){
					Request req = (Request) data;
					int id = ((Request)req).id;

					if(req.type.equals("meetings")){
						r = database.getMeetings(id);
					}else if(req.type.equals("meeting")){
						r = database.getMeeting(id);
						System.out.println( ((Meeting)r).title);
					}else if(req.type.equals("item")){
						r = database.getItem(id);
					}else if(req.type.equals("users")){
						r = database.getAllUsers();

					}else if(req.type.equals("action")){
						r = database.getAction(id);
					}else if(req.type.equals("group")){
						r = database.getGroup(id);
					}else if(req.type.equals("user")){
						r = database.getUser(id);					
					}else if(req.type.equals("deleteitem")){
						qres = database.deleteItem(id);					
					}else if(req.type.equals("deleteaction")){
						qres = database.deleteAction(id);					
					}else if(req.type.equals("groupsofuser")){
						r = database.getGroupsOfUser(id);
					}

				}
				else if(data instanceof Authentication){ //A ideia do Rui de devolver a classe com um campo alterado pode ser usada no resto, por exemplo ao fazer um add, completa-se os campos
					r = database.login((Authentication)data);
					clients.get(clientId).userData = ((Authentication)r).userData;
				}
				else if(data instanceof Meeting){
					Meeting m = (Meeting) data;
					if(m.idmeeting == 0){
						m.leader = userData;
						qres = database.insertMeeting(m);
					}else{
						qres = database.updateMeeting(m);
					}
				}else if(data instanceof Item){
					Item it = (Item) data;
					it.user = userData;
					System.out.println("Received item");
					if(it.iditem == 0){
						qres = database.insertItem(it);
					}else{
						qres = database.updateItem(it);
					}
				}else if(data instanceof Comment){
					System.out.println("Received Comment");
					Comment com = (Comment) data;
					if(com.commentId == 0){
						qres = database.insertComment(com, userData);
						if(qres != -1){
							broadcastMessage(com, "comment", com.item.iditem);
						}
					}
				}else if(data instanceof Action){
					Action act = (Action) data;
					System.out.println("Received action");
					if(act.idaction == 0){
						qres = database.insertAction(act);
					}else{
						qres = database.updateAction(act);
					}
				}else if(data instanceof KeyDecision){
					KeyDecision kd = (KeyDecision) data;
					if(kd.idkeydecision == 0){
						qres = database.insertKeyDecision(kd);
					}else{
						qres = database.updateKeyDecision(kd);
					}
				}else if(data instanceof InviteUsers){
					InviteUsers iu = (InviteUsers) data;
					qres = database.inviteUsers(iu);
				}else if(data instanceof User){
					User u = (User) data;
					r = database.createAccount(u);				
				}else if(data instanceof RemoveUserFromGroup){
					RemoveUserFromGroup ru = (RemoveUserFromGroup) data;
					qres = database.removeUserFromGroup(ru);	
				}

				if(r != null){
					out.writeObject(r);
					if(data instanceof Request){
						Request req = (Request) data;
						System.out.println("Wrote " + ((Request)req).type + " to client " + clientId);
					}
				}
				if(qres != -2){
					System.out.println("qres " + qres);
					if(qres == -1){
						conf.error = 1;
					}else{
						conf.error = 0;
					}
					
					System.out.println("Sending Confirmation");
					out.writeObject(conf);
					System.out.println("OK");

				}

			} catch(RemoteException e){
				System.out.println("Database server is down");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Error: Class not found while reading pipe of client "+clientId +".");

			} catch (IOException e) {
				System.out.println("Closed connect with client " + clientId + ".");
				clients.remove(clientId);
				//e.printStackTrace();
				break;
			}
		}
		
		System.out.println("Closing connection to client "+clientId +".");
		clients.remove(clientId);
		
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
					System.out.println("Wrote to client");
				}catch(IOException e){System.out.println("IO Exception"); e.printStackTrace();}
			/*if(cd.context.equals(context) && cd.contextId == contextId)
				cd.out.writeObject(message);*/
		}

	}
}
