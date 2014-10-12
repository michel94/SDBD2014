import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ListenerThread implements Runnable{
	private ObjectInputStream ois;

	public ListenerThread(ObjectInputStream ois){
		this.ois = ois;
		Thread t = new Thread(this, "ListenerThread");
		t.start();
	}
	public void run(){
		
		while(true){
			//Receive Updates from server
			System.out.println("Listener Thread waiting...");
			try{
				//Implement mechanism to recognize type of input
				ArrayList<Meeting> r = (ArrayList<Meeting>) ois.readObject();
				System.out.println(">>> Receive answer"); //Not exactly and answer, this will be fixed later
				for(int i=0; i<r.size(); i++){
					System.out.println(r.get(i).title + ", " + r.get(i).id);
				}
			}catch(IOException|ClassNotFoundException uhe){
				System.out.println("Error! Could not connect to the server.");
			}

			

		}
	}
}