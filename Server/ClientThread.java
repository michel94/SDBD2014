import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.Calendar;
import java.rmi.RemoteException;
import java.rmi.*;
import java.sql.Date;
import java.util.*;
import java.lang.*;

public class ClientThread implements Runnable {
	Thread t;
	Socket clientSocket;
	int clientId;
	DatabaseInterface database;
	
	ObjectInputStream in;
	ObjectOutputStream out;
	ConcurrentHashMap<Integer, ClientData> clients;
	User userData = null;
	private String failoverserver,databaseIP,databasePort;

	public ClientThread(Socket socket, int n, DatabaseInterface database, ConcurrentHashMap<Integer, ClientData> cl, String failoverserver, String databaseIP, String databasePort ){
		t = new Thread(this, "clientThread");

		clientSocket = socket;
		clientId = n;
		clients = cl;
		this.database = database;
		this.clients = clients;
		this.failoverserver = failoverserver;
		this.databaseIP = databaseIP;
		this.databasePort = databasePort;

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
				accessClient();

			} catch (ClassNotFoundException e) {
				System.out.println("Error: Class not found while reading pipe of client "+clientId +".");

			} catch (IOException e) {
				System.out.println("Closed connection with client " + clientId + ".");
				clients.remove(clientId);
				//e.printStackTrace();
				break;
			}
		}
		
		clients.remove(clientId);
		
	}

	public void accessClient() throws ClassNotFoundException, IOException{
		Confirmation conf = new Confirmation();

		Object data = (Object) in.readObject();
		System.out.println("Received Object");
		
		DatabaseAccess access = accessDatabase(data);
		int qres = access.getQRes();
		Object r = access.getObject();


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

		}
	}

	public DatabaseAccess accessDatabase(Object data){
		DatabaseAccess result = null;
		Object r = null;
		int qres = -2;

		while(true){
			try{
				if(data instanceof Request){
					Request req = (Request) data;
					int id = ((Request)req).id;

					if(req.type.equals("meetings")){
						r = database.getMeetings(id);
					}else if(req.type.equals("meeting")){
						r = database.getMeeting(id);
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
					}else if(req.type.equals("actionsofuser")){
						r = database.getUserActions(userData.iduser);
					}else if(req.type.equals("leavemeeting")){
						qres = database.leaveMeeting(id,userData.iduser);
					}

				}
				else if(data instanceof Authentication){ //A ideia do Rui de devolver a classe com um campo alterado pode ser usada no resto, por exemplo ao fazer um add, completa-se os campos
					r = database.login((Authentication)data);
					clients.get(clientId).userData = ((Authentication)r).userData;
					userData = clients.get(clientId).userData;
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
						Item item = database.getItem(com.item.iditem);
						Meeting meeting = database.getMeeting(item.meeting);
						if(qres != -1){
							broadcastComment(com, meeting);
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
					if(iu.flag == 1){
						qres = database.inviteUsersToMeeting(iu);
					}else if (iu.flag == 2){
						qres = database.inviteUsersToGroup(iu);
					}
				}else if(data instanceof User){
					User u = (User) data;
					r = database.createAccount(u);
				}else if(data instanceof RemoveUserFromGroup){
					RemoveUserFromGroup ru = (RemoveUserFromGroup) data;
					qres = database.removeUserFromGroup(ru);
				}else if(data instanceof Group){
					Group g = (Group) data;
					qres = database.createGroup(g);
					System.out.println("group created");
				}else if(data instanceof InviteGroup){
					InviteGroup g = (InviteGroup) data;
					qres = database.addGroupToMeeting(g.group, g.meeting);
				}

				return new DatabaseAccess(qres, r);
			}catch(RemoteException e){
				System.out.println("Database server is down. Reconnecting");
				reconnectDatabase();

				e.printStackTrace();
			}
		}

		
	}

	public void reconnectDatabase(){
		int retries = 0;

		try{
			while(true){
				try{
				 	database = (DatabaseInterface) Naming.lookup("//"+databaseIP+":"+databasePort+"/database");
				 	System.out.println("Connected to the database");
					break;
				}catch(NotBoundException e){
					System.out.println("Database could not be reached. Failed connection. Retrying connection in 2 seconds.");
					retries++;
					Thread.sleep(2000);
				}catch(MalformedURLException e){
					System.out.println("Could not find the the database hostname. Retrying connection in 2 seconds.");
					retries++;
					Thread.sleep(2000);
				}catch(RemoteException e){
					System.out.println("Remote exception. Retrying connection in 2 seconds.");
					retries++;
					Thread.sleep(2000);
				}
			}
		}catch(InterruptedException ex) {
    		Thread.currentThread().interrupt();
		}

		System.out.println("Connection to database established.");
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
	public void broadcastComment(Comment comment, Meeting meeting){
		Iterator it = clients.keySet().iterator();
		ClientData cd;

		System.out.println("Broadcast");
		while(it.hasNext()){
			Integer key = (Integer) it.next();
			System.out.println("Key: " + key);
			cd = clients.get(key);
			if( findUser(meeting, cd.userData) )
				try{
					cd.out.writeObject(comment);
					System.out.println("Wrote comment to client");
				}catch(IOException e){System.out.println("IO Exception");}
		}
	}
	private boolean findUser(Meeting m, User u){
		for(int i=0; i<m.users.size(); i++){
			if(m.users.get(i).iduser == u.iduser)
				return true;
		}
		return false;
	}

}

class DatabaseAccess{
	private int qres;
	private Object r;
	public DatabaseAccess(int qres, Object r){
		this.qres = qres;
		this.r = r;
	}
	public DatabaseAccess(){
		this.qres = -2;
		this.r = null;
	}
	public int getQRes(){
		return qres;
	}
	public Object getObject(){
		return r;
	}


}