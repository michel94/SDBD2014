import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client{
	private int serverPort = 6000, clientID=0;
	private String serverIp = "127.0.0.1";
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private BufferedReader in;
	private ListenerThread lt;
	private AtomicBoolean loggedIn = new AtomicBoolean(false);
	
	public WaitClient wait = new WaitClient();


	
	public Client(){
		System.out.println("Client started");
		
		try{
			socket = new Socket(serverIp, serverPort);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(in);
			oos = new ObjectOutputStream(out);
			lt = new ListenerThread(ois, loggedIn, wait);
		}catch(IOException uhe){
			System.out.println("Error! Could not connect to the server.");
		}

		in = new BufferedReader(new InputStreamReader(System.in));
		

		System.out.println("Welcome.");

		while(!loggedIn.get()){
			login();
		}

		inputHandler();
		
	}
	public static void main(String[] args){
		Client client = new Client();
		
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

	public void writeObject(Object r){
		try{
			oos.writeObject(r);
		}catch(IOException e){;}
	}

	protected void inputHandler(){
		String s;
		int sel;
		Request r;

		lt.context = "Main";

		while(true){
			if(lt.context.equals("Main")){
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
			
			}else if(lt.context.equals("Meetings")){
				print("Your Meetings:");
				r = new Request("meetings");
				writeObject(r);
				wait.waitForMeetings();
				Meetings m = lt.meetings;
				for(int i=0; i<m.size(); i++){
					print(i + ": " + m.get(i).title + ": "+m.get(i).datetime);
				}
				print("");

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
			}else if(lt.context.equals("NewMeeting")){
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

			}else if(lt.context.equals("ConsultMeeting")){
				Meetings ms = lt.meetings;
				print("Which meeting do you want to consult? Write its number:");
				
				r = new Request("meeting",ms.get(readInt(0,ms.size())).id);
				writeObject(r);
				wait.waitMeeting();
				Meeting m = lt.meeting;
				print("Title: "+m.title);
				print("Description: "+m.description);
				print("Date/Time: "+m.datetime);
				print("Location: "+m.location);
				print("\nItems:");
				for(int i=0; i<m.items.size();i++){
					print(i + ": " + m.items.get(i).title);
				}
				print("\nAction:");
				for(int i=0; i<m.actions.size();i++){
					System.out.print(i + ": " + m.actions.get(i).description);
					if(m.actions.get(i).status==0){
						print(" Status: Pending");				
					}else print(" Status: Done");
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
						lt.context = "ConsultMeeting";
						break;
				}
				if(lt.context.equals("ConsultItem")){
					print("Which item do you want to consult? Write its number:");
				
					r = new Request("item",m.items.get(readInt(0,m.items.size())).id);
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
					
					//Comentar
					//Apagar item||decision
					//update description||item||decision
					
				}
				else if(lt.context.equals("ConsultAction")){
					print("Which action do you want to consult? Write its number:");
				
					r = new Request("action",m.actions.get(readInt(0,m.actions.size())).id);
					writeObject(r);
					wait.waitAction();
				}
			}

		}
	}

	protected void login(){
		String s = null;
		System.out.println("Please write your username and password separated by a space.");
		
		s = readString();

		String[] words = s.split(" ");
		Authentication auth = new Authentication(words[0],words[1]);
		try{
			oos.writeObject((Object)auth);
		}catch(IOException e){
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
                }
	}
	
	
}
