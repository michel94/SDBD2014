import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ListenerThread implements Runnable{
	private ObjectInputStream ois;
	private ArrayList<String> historic;
    	private AtomicBoolean loggedIn;
    	private WaitClient wait;

    	public String context = "";
    	public Meetings meetings;
    	public Meeting meeting;
    	public Authentication auth;
    	public Item item;
    	public Action action;



	public ListenerThread(ObjectInputStream ois, AtomicBoolean l, WaitClient w){
		this.ois = ois;
        loggedIn = l;
        wait = w;

		Thread t = new Thread(this, "ListenerThread");
		t.start();

	}
	
	public void println(String s){
		System.out.println(s);
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
				}

			}catch(IOException e){
				System.out.println("Error! Connection to the server lost.");
				break;
			}catch(ClassNotFoundException e){
				System.out.println("Error! Class not found.");
			}

			

		}
	}
}
