import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ListenerThread implements Runnable{
	private ObjectInputStream ois;
	private ArrayList<String> historic;

	public ListenerThread(ObjectInputStream ois){
		this.ois = ois;
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


				if(r instanceof Authentication){
					Authentication auth = (Authentication)r;
					if(auth.confirmation == 0){
						System.out.println("Login failed. Try again.");
					}
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
