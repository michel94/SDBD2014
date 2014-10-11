import java.net.*;
import java.io.*;

public class Client{
	private int serverPort = 6000;
	private String serverIp = "127.0.0.1";
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ListenerThread listenerThread;

	public Client(){
		System.out.println("Client started");
		try{
			socket = new Socket(serverIp, serverPort);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(in);
			oos = new ObjectOutputStream(out);
			listenerThread = new ListenerThread(ois);
		}catch(IOException uhe){
			System.out.println("Error! Could not connect to the server.");
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s;
		
		while(true){
			try{
				s = in.readLine();
				System.out.println(s);
				String[] words = s.split(" ");
				if(words[0].equals("meetings")){
					Request r = new Request("meetings");
					oos.writeObject((Object)r);
				}
			}catch(IOException uhe){
				System.out.println("end");
			}catch(NullPointerException e){
				System.out.println("Connection Error");
			}
		}
		
	}
	public static void main(String[] args){
		Client client = new Client();
		
	}
}