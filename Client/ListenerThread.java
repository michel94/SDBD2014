import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ListenerThread implements Runnable{
	private ObjectInputStream ois;
	private ArrayList<String> historic;
    private AtomicBoolean loggedIn;
    private WaitClient wait;

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
			System.out.println("Listener Thread waiting...");
			try{
				//Implement mechanism to recognize type of input
				Object r = ois.readObject();


				if(r instanceof Meetings){
					Meetings m = (Meetings) r;
					println("Meetings");
					for(int i=0; i<m.size(); i++){
						println(m.get(i).id + ": " + m.get(i).title);
					}
				}else if(r instanceof Meeting){
					Meeting m = (Meeting) r;
					println("Meeting " + m.title);
					for(int i=0; i<m.items.size(); i++){
						println(m.items.get(i).id + ": " + m.items.get(i).title);
					}
				}else if(r instanceof Authentication){
					Authentication auth = (Authentication)r;
					if(auth.confirmation == 0){
						System.out.println("Login failed. Try again.");
					}else{
                        System.out.println("Authentication successful");
                        loggedIn.getAndSet(true);
                    }
                    wait.notifyAuth();
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
