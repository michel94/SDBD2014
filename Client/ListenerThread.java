import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.gnome.gtk.Gtk;
import org.gnome.notify.Notify;
import org.gnome.notify.Notification;

public class ListenerThread implements Runnable{

	public ServerData server1, server2, serverData;
	public Socket socket;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	
	public String context = "";
		
	public WaitClient wait;
	public Authentication auth;
	public Meetings meetings;
	public Meeting meeting;
	public Item item;
	public Action action;
	public Groups groups;
	public Group group;
	

	public ListenerThread(){
		server1 = new ServerData("127.0.0.1", 6000);
		server2 = new ServerData("127.0.0.1", 6001);
		serverData = server1;
		wait = new WaitClient();
		reconnect();

		Thread t = new Thread(this, "ListenerThread");
		t.start();

	}
	
	public void print(String s){
		System.out.println(s);
	}


	public void reconnect(){
		
		int tries = 0;
		while(tries < 3){
			print("Try: "+tries);
			try{
				Socket socket = new Socket(serverData.ip, serverData.port);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(in);
				oos = new ObjectOutputStream(out);

				return;
			}catch(IOException e){
				tries++;
				try{Thread.sleep(2000);
				}catch(InterruptedException ex){;}
			}
			
		}

		Notification error = new Notification("Connection error", "The main server is down. Connecting to secondary server.", "dialog-information");
		error.show();
		
		if(serverData == server1)
			serverData = server2;
		else
			serverData = server1;

		tries = 0;
		while(tries < 3){
			try{
				socket = new Socket(serverData.ip, serverData.port);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(in);
				oos = new ObjectOutputStream(out);
				
				return;
			}catch(IOException e){
				tries++;
				try{Thread.sleep(2000);
				}catch(InterruptedException ex){;}
			}
			
		}

		error = new Notification("Connection error", "Both servers are down. Try again later", "dialog-information");
		error.show();

		print("Error! Could not connect to the server.");
		System.exit(0);
				
	}

	public void run(){
		
		while(true){
			//Receive Updates from server
			try{
				//Implement mechanism to recognize type of input
				Object r = ois.readObject();

				if(r instanceof Meetings){
					meetings = (Meetings) r;
					wait.notifyMeetings();
				}else if(r instanceof Meeting){
					meeting = (Meeting) r;
					//Notification nmeet = new Notification("New Meeting", meeting.title, "dialog-information");
					//nmeet.show();
					wait.notifyMeeting();
				}else if(r instanceof Authentication){
					auth = (Authentication) r;
					wait.notifyAuth();
				}else if(r instanceof Item){
					item = (Item) r;
					wait.notifyItem();
				}else if(r instanceof Action){
					action = (Action) r;
					wait.notifyAction();
				}else if(r instanceof Comment){
					Comment comment = (Comment) r;
					Notification ncom = new Notification("New Comment on item " + comment.item.id, comment.text, "dialog-information");
					ncom.show();
				}else if(r instanceof Confirmation){
					if( ((Confirmation) r).error > 0)
						print("Sorry, we could not process your request due to conflicts with other accesses. Try again");
					wait.notifyDefault();
				}

			}catch(IOException ex){
				print("Error! Connection to the server lost.");
				reconnect();
				wait.notifyReconnect();

				try{
					oos.writeObject(auth);
				}catch(IOException e){;}

			}catch(ClassNotFoundException e){
				print("Error! Class not found.");
			}

			

		}
	}
}
