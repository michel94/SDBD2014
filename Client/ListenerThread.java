import java.net.*;
import java.io.*;

public class ListenerThread implements Runnable{
	private ObjectInputStream ois;

	public ListenerThread(ObjectInputStream ois){
		this.ois = ois;
		Thread t = new Thread();
		t.start();
	}
	public void run(){
		while(true){
			//Receive Updates from server
			try{
				Object receiveObject = ois.readObject();
			}catch(IOException|ClassNotFoundException uhe){
				System.out.println("Error! Could not connect to the server.");
			}

			

		}
	}
}