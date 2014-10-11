import java.net.*;
import java.io.*;

public class Client{
	private int serverPort = 6000;
	private String serverIp = "127.0.0.1";
	private Socket socket;
	private ObjectInputStream ois;
	private ListenerThread listenerThread;

	public Client(){
		System.out.println("Client started");
		try{
			socket = new Socket(serverIp, serverPort);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			ois = new ObjectInputStream(in);
			listenerThread = new ListenerThread(ois);
		}catch(IOException uhe){
			System.out.println("Error! Could not connect to the server.");
		}

	}
	public static void main(String[] args){
		Client client = new Client();
	}
}