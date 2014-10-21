import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.gnome.gtk.Gtk;
import org.gnome.notify.Notify;
import org.gnome.notify.Notification;

public class Client{
	private ServerData server1, server2, serverData;

	private int clientID=0;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private BufferedReader in;
	private ListenerThread lt;
	private AtomicBoolean loggedIn = new AtomicBoolean(false);
	
	public WaitClient wait = new WaitClient();
	
	public Client(){
		server1 = new ServerData("127.0.0.1", 6000);
		server2 = new ServerData("127.0.0.1", 6001);
		serverData = server1;

		Notify.init("Hello World");

		System.out.println("Client started");
		
		connect();

		in = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Welcome.");

		while(!loggedIn.get()){
			login();
		}

		inputHandler();
		
	}

	public void print(String s){
		System.out.println(s);
	}
	
	public int readInt(int inf, int sup){
		while(true){
			try{
				String s = readString();
				int r = Integer.parseInt(s);
				if(r >= inf && r <= sup)
					return r;
				else
					print("Insert your selection, a number between " + inf + " and " + sup + ":");	
			}catch(Exception e){
				print("Insert your selection, a number between " + inf + " and " + sup + ":");
			}
		}
	}

	public String readString(){
		while(true){
			try{
				return in.readLine();
			}catch(Exception e){
				print("Exception: keep trying :>");
			}
		}
	}

	public void connect(){
		
		int tries = 0;
		while(tries < 3){
			print("Try: "+tries);
			try{
				socket = new Socket(serverData.ip, serverData.port);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(in);
				oos = new ObjectOutputStream(out);
				lt = new ListenerThread(ois, loggedIn, wait);
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
				lt = new ListenerThread(ois, loggedIn, wait);
				return;
			}catch(IOException e){
				tries++;
				try{Thread.sleep(2000);
				}catch(InterruptedException ex){;}
			}
			
		}

		error = new Notification("Connection error", "Both servers are down. Try again later", "dialog-information");
		error.show();

		System.out.println("Error! Could not connect to the server.");
		System.exit(0);
				
	}

	public void writeObject(Object r){
		try{
			oos.writeObject(r);
		}catch(IOException e){;}
	}

	protected void inputHandler(){
		String s;

		lt.context = "Main";

		while(true){
			if(lt.context.equals("Main")){
				mainMenu();
			
			}else if(lt.context.equals("Meetings")){
				meetingsMenu();

			}else if(lt.context.equals("NewMeeting")){
				newMeetingMenu();

			}else if(lt.context.equals("ConsultMeeting")){
				consultMeetingMenu();

			}else if(lt.context.equals("ConsultItem")){
				consultItemMenu();

			}else if(lt.context.equals("ConsultAction")){
				consultActionMenu();

			}

		}
	}

	public void mainMenu(){
		int sel;
		Request r;

		print("\n|   Meeto   |\n");
		print("");
		print("Main Menu");
		print("");

		
		/*print("Your Groups");
		r = new Request("groups");
		oos.writeObject(r);
		waitForMeetings();
		print("");*/

		print("What do you want to do?");
		print("1 - Consult meetings");
		print("2 - Consult groups");
		print("3 - Quit");
		sel = readInt(1, 3);

		switch(sel){
			case 1:
				lt.context = "Meetings";
				break;
			case 2:
				lt.context = "Groups";
				break;
			case 3:
				System.exit(0);
		}
	}

	public void meetingsMenu(){
		int sel;
		Request r;

		/*for(int i=0; i<m.size(); i++){
			print(i + ": " + m.get(i).title + ": "+m.get(i).datetime);
		}
		print("");*/

		print("What do you want to do?");
		print("1 - Schedule new meeting");
		print("2 - Consult meeting details");
		print("3 - Delete one of your meetings");
		print("4 - Back");
		sel = readInt(1, 4);

		switch(sel){
			case 1:
				lt.context = "NewMeeting";
				break;
			case 2:
				lt.context = "ConsultMeeting";
				break;
			case 3:
				lt.context = "DeleteMeeting";
				break;
			case 4:
				lt.context = "Main";

			//Edit meeting
		}
		/*Meetings m  = lt.meetings;
		for(int i=0; i<m.size(); i++){
			print(m.get(i).id + ": " + m.get(i).title);
		}*/
	}

	public void newMeetingMenu(){
		int sel;
		Request r;
		Meeting m = new Meeting();

		print("Creating new meeting. Fill the following form:");
		print("Title: ");
		m.title = readString();
		print("Description: ");
		m.description = readString();
		print("Date: ");
		String date = readString();
		print("Time: ");
		m.datetime = date + " " + readString();
		print("Location: ");
		m.location = readString();
		
		writeObject(m);
		lt.context = "Meetings";
	}

	public void consultMeetingMenu(){
		int sel;
		Request r;
		

		r = new Request("meetings");
		writeObject(r);
		wait.waitForMeetings();
		Meetings ms = lt.meetings;

		print("Meetings:");
		for(int i=0; i<ms.size(); i++){
			print(i+1 + " - " + ms.get(i).title + ": "+ms.get(i).datetime);
		}
		print("");

		print("Which meeting do you want to consult? Write its number:");
		
		r = new Request("meeting",ms.get(readInt(1,ms.size())-1 ).idmeeting);
		writeObject(r);
		wait.waitMeeting();
		Meeting m = lt.meeting;
		print("Title: "+m.title);
		print("Description: "+m.description);
		print("Date/Time: "+m.datetime);
		print("Location: "+m.location);
		print("\nItems:");
		for(int i=0; i<m.items.size();i++){
			print(i+1 + " - " + m.items.get(i).title);
		}
		print("\nAction:");
		for(int i=0; i<m.actions.size();i++){
			System.out.print(i+1 + " - " + m.actions.get(i).description);
			if(m.actions.get(i).done == 0){
				System.out.print(" Status: Pending");
			}else System.out.print(" Status: Done");
		}
		print("");

		print("What do you want to do?");
		print("1 - Consult item details");
		print("2 - Consult action details");
		print("3 - Back");
		sel = readInt(1, 3);

		switch(sel){
			case 1:
				lt.context = "ConsultItem";
				break;
			case 2:
				lt.context = "ConsultAction";
				break;
			case 3:
				lt.context = "Meetings";
				break;
		}
	}

	public void consultItemMenu(){
		int sel;
		Request r;
		Meeting m = lt.meeting;

		print("Which item do you want to consult? Write its number:");
		
		r = new Request("item",m.items.get(readInt(1,m.items.size())-1 ).id);
		writeObject(r);
		wait.waitItem();
		Item it = lt.item;
		
		print("Key Decisions:");
		for (int i=0;i<it.decisions.size();i++){
			print(it.decisions.get(i).date+" "+ it.decisions.get(i).description);
		}
		print("Comments:");
		for (int i=0;i<it.comments.size();i++){
			print(it.comments.get(i).datetime+" "+ it.comments.get(i).user+": "+ it.comments.get(i).text);
		}

		print("What do you want to do?");
		print("1 - Comment on this item");
		print("2 - Delete this item");
		print("3 - Add Key Decision");

		sel = readInt(1,3);

		switch(sel){
			case 1:
				String s = readString();
				Object com = new Comment(s, lt.item);
				writeObject(com);
				break;

		}
		
		//Comentar
		//Apagar item||decision
		//update description||item||decision
		
	}

	public void consultActionMenu(){
		int sel;
		Request r;
		Meeting m = lt.meeting;

		print("Which action do you want to consult? Write its number:");
	
		r = new Request("action",m.actions.get(readInt(1,m.actions.size())-1 ).idaction);
		writeObject(r);
		wait.waitAction();
	}

	protected void login(){
		int sel;
		while(true){
			try{
				String s = null;
				print("Welcome to Meeto! Would you like to:");
				print("1-Login");
				print("2-Register account");
				print("3-Exit");
				

				sel = readInt(1,3);
				switch(sel){
					case 1:
						System.out.println("Please write your username and password separated by a space.");
				
						s = readString();

						String[] words = s.split(" ");
						Authentication auth = new Authentication(words[0],words[1]);
						try{
							oos.writeObject((Object)auth);
						}catch(IOException e){
							connect();
							System.out.println("IO Exception while sending authentication input.");
						}
						wait.waitForAuth();
						auth=lt.auth;
						if(auth.confirmation == 0){
							System.out.println("Login failed. Try again.");
						}else{
							System.out.println("Authentication successful");
							clientID = auth.clientID;
							loggedIn.getAndSet(true);
							return;
						}
					break;
					case 2:
						print("Not yet implemented");
					break;
					case 3:
						System.exit(0);
					break;
				}
					
			}catch(Exception ex){;}
				
			
		}

	}
	
	public static void main(String[] args){
		String[] a = new String[0];
		Gtk.init(a);
		Client client = new Client();
		
	}
	
}
