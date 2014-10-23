import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.gnome.gtk.Gtk;
import org.gnome.notify.Notify;
import org.gnome.notify.Notification;

public class Client{
	private ListenerThread lt;

	private int clientID=0;
	private BufferedReader in;
	private boolean loggedIn = false;
	public WaitClient wait;
	
	public Client(){
		clear();

		Notify.init("Hello World");
		
		print("Client started");
		in = new BufferedReader(new InputStreamReader(System.in));
		lt = new ListenerThread();

		wait = lt.wait;

		while(!loggedIn){
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

	public void writeObject(Object r){
		while(true){
			try{
				lt.oos.writeObject(r);
				return;
			}catch(IOException e){
				Notification error = new Notification("Socket error", "Broken pipe.", "error-information");
				error.show();

				wait.waitReconnect();
				wait.waitAuth();
			}
		}
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

			}else if(lt.context.equals("Groups")){
				groupsMenu();

			}else if(lt.context.equals("NewItemMenu")){
				newItemMenu();
			}else if(lt.context.equals("NewActionMenu")){
				newActionMenu();
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

		print("What do you want to do?");
		print("1 - Consult meetings");
		print("2 - Consult groups");
		print("3 - Quit");
		sel = readInt(1, 3);

		switch(sel){
			case 1:
				lt.context = "Meetings";
				clear();
				break;
			case 2:	
				//lt.context = "Groups";
				
				clear();
				break;
			case 3:
				System.exit(0);
		}
	}

	public void meetingsMenu(){
		int sel;
		Request r;

		r = new Request("meetings");
		writeObject(r);
		wait.waitMeetings();
		Meetings ms = lt.meetings;

		print("Meetings:");
		for(int i=0; i<ms.size(); i++){
			print(i+1 + " - "+ms.get(i).datetime + "  " + ms.get(i).title);
		}
		print("");

		print("What do you want to do?");
		print("1 - Consult meeting details");
		print("2 - Schedule new meeting");
		print("3 - Leave one of your meetings");
		print("4 - Pending meeting invites");
		print("5 - Back");
		sel = readInt(1, 5);

		switch(sel){
			case 1:
				if(ms.size()!=0){
					print("Which meeting do you want to consult? Write its number:");
		
					r = new Request("meeting",ms.get(readInt(1,ms.size())-1 ).idmeeting);
					writeObject(r);
					wait.waitMeeting();
					lt.context = "ConsultMeeting";
					clear();
				}
				break;
			case 2:
				clear();
				lt.context = "NewMeeting";
				break;
			case 3:
				if(ms.size()!=0){
					print("Which meeting do you want to leave? Write its number:");
		
					r = new Request("leavemeeting",ms.get(readInt(1,ms.size())-1 ).idmeeting);
					writeObject(r);
					wait.waitLeaveMeeting();
					clear();
					print("Left meeting successfully");
				}
				break;
			case 4:
				clear();
				print("Not working yet");
				break;
			case 5:
				clear();
				lt.context = "Main";

		}
	}

	public void newMeetingMenu(){
		int sel;
		Request r;


		Meeting m = new Meeting();
		clear();
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
		m.leader = lt.user;
		m.location = readString();
		
		
		m.users = inviteUsers();

		writeObject(m);
		wait.waitDefault();
		clear();
		print("Meeting created successfully");
		print("É preciso mudar a função de criar meetings na base de dados para adicionar os users passados no array. Senão isto não adiciona logo.");
		
		lt.context = "Meetings";
	}

	

	public void consultMeetingMenu(){
		int sel;
		Request r;

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
				print(" Status: Pending");
			}else print(" Status: Done");
		}
		print("");
		print("What do you want to do?");
		print("1 - Consult item details");
		print("2 - Consult action details");
		print("3 - Add item");
		print("4 - Add action");
		print("5 - Edit meeting details");
		print("6 - Add users to meeting");
		print("7 - Add group to meeting");
		print("8 - Close this meeting");
		print("9 - Back");
		sel = readInt(1, 9);

		switch(sel){
			case 1:
				if(m.items.size()!=0){
					print("Which item do you want to consult? Write its number:");
		
					r = new Request("item", m.items.get(readInt(1,m.items.size())-1).iditem);
					writeObject(r);
					wait.waitItem();
					lt.context = "ConsultItem";
					clear();
				}
				break;
			case 2:	
				if(m.actions.size()!=0){
					print("Which action do you want to consult? Write its number:");
	
					r = new Request("action",m.actions.get(readInt(1,m.actions.size())-1 ).idaction);
					writeObject(r);
					wait.waitAction();
					lt.context = "ConsultAction";
					clear();
				}
				break;
			case 3:
				clear();
				
				lt.context = "NewItemMenu";
				break;
			case 4:
				clear();
				lt.context = "NewActionMenu";
				break;
			case 5: 
				clear();
				print("Not working yet");

				lt.context = "ConsultMeeting";
				break;
			case 6: 
				clear();
				Users users = inviteUsers();
				InviteUsers invus = new InviteUsers();
				InviteUser invu;
				for(int i= 0; i< users.size(); i++){
					invu = new InviteUser(users.get(i).iduser,lt.meeting.idmeeting);
					invus.add(invu);
				}
				writeobject(invus);
				wait.waitDefault();

				lt.context = "ConsultMeeting";
				break;
			case 7: 
				clear();
				print("Not working yet");
				lt.context = "ConsultMeeting";
				break;
			case 8: 
				clear();
				print("Not working yet");
				lt.context = "ConsultMeeting";
				break;

			case 9:
				clear();
				lt.context = "Meetings";
				break;
		}
		m = (Meeting)updateDataInClient("meeting",lt.meeting.idmeeting);
	}
	
	public void consultItemMenu(){
		int sel;
		Request r;
		Meeting m = lt.meeting;
		KeyDecision kd;
		Item it = lt.item;
		
		print("Item Title: " + it.title);
		print("Item Description: " + it.description);

		print("Key Decisions:");
		for (int i=0;i<it.keydecisions.size();i++){
			print(i +" -" +"Description: "+it.keydecisions.get(i).description);
		}
		print("Comments:");
		for (int i=0;i<it.comments.size();i++){
			print(it.comments.get(i).datetime+" "+ it.comments.get(i).user.username+": "+ it.comments.get(i).text);
		}

		print("What do you want to do?");
		print("1 - Comment on this item");
		print("2 - Delete this item");
		print("3 - Add Key Decision");
		print("4 - Edit Key Decision");
		print("5 - Edit item description");
		print("6 - Back");

		sel = readInt(1,6);

		switch(sel){
			case 1:
				print("Write your comment:");
				String s = readString();
				Object com = new Comment(s, lt.item);
				writeObject(com);
				clear();
				break;
			case 2:
				clear();
				print("Not working yet.");
				//lt.context="ConsultMeeting";
				break;
			case 3:
				clear();
				print("Insert the key description data:");
				print("Description: ");
				kd = new KeyDecision(readString());
				writeObject(kd);
				wait.waitDefault();

				break;
			case 4:
				print("Which key decision do you want to edit?");
				int ikd = readInt(1, it.keydecisions.size())-1;
				print("Description: ");
				kd = new KeyDecision(readString());
				kd.idkeydecision = ikd;
				writeObject(kd);
				wait.waitDefault();

				break;
			case 5:
				clear();
				print("Insert new description:");
				String desc = readString();
				Item nit = new Item(it.iditem, it.title, desc, it.user, it.meeting);
				writeObject(nit);
				wait.waitDefault();
				print("Not working yet");
				break;			
			case 6:
				lt.context="ConsultMeeting";
				break;

		}
		it = (Item)updateDataInClient("item", lt.item.iditem);

	}

	public void consultActionMenu(){
		int sel;
		Request r;
		
		Action a = lt.action;

		print("Action");
		print("Description: " + a.description);
		print("Due to: " + a.due_to);
		print("Assigned User: " + a.assigned_user.username);
		if(a.done == 0) print("Done: No");
		else print("Done: Yes");


		print("What do you want to do?");
		print("1 - Edit this action");
		print("2 - Delete this action");
		print("3 - Back");

		sel = readInt(1,3);

		switch(sel){
			case 1:
				clear();
				print("Not working yet.");
				lt.context="ConsultAction";
				break;
			case 2:
				clear();
				print("Not working yet.");

				lt.context="ConsultAction";
				break;
			case 3:
				clear();
				lt.context="ConsultMeeting";
				break;
		}
		a = (Action)updateDataInClient("action", lt.action.idaction);


	}

	public void newItemMenu(){
		int sel;
		Item it = new Item();

		print("Adding new item. Fill the following form:");
		print("Title: ");
		it.title = readString();
		print("Description: ");
		it.description = readString();
		it.meeting = lt.meeting.idmeeting;

		writeObject(it);
		wait.waitDefault();

		lt.context = "ConsultMeeting";

	}
	
	public void newActionMenu(){
		int sel;
		Action act = new Action();

		print("Adding new action. Fill the following form:");
		print("Due date (YYYY/MM/DD): ");
		act.due_to = readString();
		print("Description: ");
		act.description = readString();
		act.meeting = lt.meeting;
		listAllUsers();

		act.assigned_user = lt.user;

		writeObject(act);
		wait.waitDefault();

		lt.context = "ConsultMeeting";

	}

	

	protected void login(){
		int sel;
		
		try{
			String s = null;
			print("Welcome to Meeto! Would you like to:");
			print("1-Login");
			print("2-Register account");
			print("3-Exit");
			
			sel = readInt(1,3);
			switch(sel){
				case 1:
					clear();
					System.out.println("Please write your username and password separated by a space.");
			
					s = readString();

					String[] words = s.split(" ");
					lt.auth = new Authentication(words[0],words[1]);
					try{
						lt.oos.writeObject((Object)lt.auth);
					}catch(IOException e){
						clear();
						System.out.println("IO Exception while sending authentication input.");
					}
					wait.waitAuth();

					if(lt.auth.confirmation == 0){
						clear();
						System.out.println("Login failed. Try again.");
					}else{
						clear();
						System.out.println("Authentication successful");
						clientID = lt.auth.clientID;
						loggedIn = true;
					}
					break;
				case 2:
					clear();
					//print("Write your desired username:");
					break;
				case 3:
					System.exit(0);
					break;
			}
				
		}catch(Exception ex){
			ex.printStackTrace();
		}				
		
	}

	public void groupsMenu(){
		int sel;
		Request r;

		r = new Request("groups");
		writeObject(r);
		wait.waitGroups();
		Groups gs = lt.groups;
		clear();
		print("Groups:");
		for(int i=0; i<gs.size(); i++){
			print(i+1 + " - "+gs.get(i).name);
		print("");

		print("What do you want to do?");
		print("1 - Consult group users");
		print("2 - Add new group");
		print("3 - Delete one of your groups");
		print("4 - Back");
		sel = readInt(1, 4);

		switch(sel){
			case 1:
				if(gs.size()!=0){
					print("Which group do you want to consult? Write its number:");
		
					r = new Request("group",gs.get(readInt(1,gs.size())-1 ).idgroup);
					writeObject(r);
					wait.waitGroup();
					lt.context = "ConsultGroup";
				}
				break;
			case 2:
				//lt.context = "NewGroup";
				break;
			case 3:
				if(gs.size()!=0){
					//lt.context = "DeleteGroup";
				}
				break;
			case 4:
				lt.context = "Main";

			}
		}
			
	}
	public void consultGroupMenu(){
		int sel;
		Request r;
		Groups gs=lt.groups;

		Group g = lt.group;
		clear();
		print("Group name: "+g.name);
		
		print("\nUsers:");
		for(int i=0; i<g.users.size();i++){
			print(i+1 + " - " + g.users.get(i).username);
		}

		print("");
		print("What do you want to do?");
		print("1 - Add user to group");
		print("2 - Remove user from group");
		print("3 - Back");
		sel = readInt(1, 3);

		switch(sel){
			case 1:
				clear();
				print("Not yet working");
				break;
			case 2:	
				if(g.users.size()!=0){
					print("Which user do you want to remove? Write its number:");
					r = new Request("deleteuser",g.users.get(readInt(1,g.users.size())-1 ).iduser);
					writeObject(r);
					wait.waitDeleteUser();
					clear();
					print("User deleted successfully");
					lt.context = "ConsultGroup";
				}
				break;
			case 3:
				clear();
				lt.context = "Main";
				break;
		}
	}
	
	public void listAllUsers(){
		Request r = new Request("users");
		writeObject(r);
		wait.waitDefault();	
		for(int i=0;i<lt.users.size();i++){
			print(i+1+" - "+lt.users.get(i).username);		
		}
	}

	public void clear(){
		System.out.print("\033[H\033[2J");
	}
	
	public Object updateDataInClient(String flag, int id){
		Object o = new Object();
		if(flag.equals("meeting")){
			Request r = new Request("meeting", id);
			writeObject(r);
			wait.waitMeeting();
			return lt.meeting;
		}else if(flag.equals("group")){
			Request r = new Request("group", id);
			writeObject(r);
			wait.waitGroup();
			return lt.group;	

		}else if(flag.equals("item")){
			Request r = new Request("item", id);
			writeObject(r);
			wait.waitItem();
			return lt.item;		
		}else if(flag.equals("action")){
			Request r = new Request("action", id);
			writeObject(r);
			wait.waitAction();
			return lt.action;	
		}
		return o;
	}
	
	public Users inviteUsers(){
		Request req;
		Users users = new Users();
		listAllUsers();
		print("Invite users by writing their id number (To stop adding users write stop):");
		int sel;
		int inf = 1;
		int sup = lt.users.size();

		while(true){
			try{
				String s = readString();
				if(s.equals("stop")){
					break;
				}
				else{
					int r = Integer.parseInt(s);
				
					if(r >= inf && r <= sup){
						print("Adding user");
						sel = r-1;
						req = new Request("user", lt.users.get(sel).iduser);
						writeObject(req);
						wait.waitDefault();
						users.add(lt.user);
						print("Added user. Insert next id (To stop adding users write stop):");
					}						
					else
						print("Insert your selection, a number between " + inf + " and " + sup + ":");	
				}
			}catch(Exception e){
				print("Insert your selection, a number between " + inf + " and " + sup + ":");
			}

		}
		return users;
	}

	public static void main(String[] args){
		String[] a = new String[0];
		Gtk.init(a);
		Client client = new Client();
		
	}



	
}
