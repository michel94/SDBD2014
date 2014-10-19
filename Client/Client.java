import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client{
	private int serverPort = 6000;
	private String serverIp = "127.0.0.1";
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ListenerThread listenerThread;
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
			listenerThread = new ListenerThread(ois, loggedIn, wait);
		}catch(IOException uhe){
			System.out.println("Error! Could not connect to the server.");
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		

		System.out.println(loggedIn.get());

		while(!loggedIn.get()){
			System.out.println(loggedIn.get());
			login(in);
		}

		inputHandler(in);
		
	}
	public static void main(String[] args){
		Client client = new Client();
		
	}
	
	protected void inputHandler(BufferedReader in){
		String s;

		while(true){
			try{
				s = in.readLine();
				String[] words = s.split(" ");
				Request r;
				if(words[0].equals("meetings")){
					r = new Request("meetings");
					oos.writeObject(r);
				}else if(words[0].equals("meeting")){
					r = new Request("meeting", Integer.parseInt(words[1]));
					oos.writeObject((Object)r);
				}else if(words[0].equals("item")){
					r = new Request("item", Integer.parseInt(words[1]));
				}else if(words[0].equals("comments")){
					r = new Request("comments", Integer.parseInt(words[1]));
				}
			}catch(IOException e){
				System.out.println("Error: No connection!");
			}catch(NullPointerException e){
				System.out.println("Connection Error");
			}
		}
	}

	protected void login(BufferedReader in){
		String s=null;
		System.out.println("Welcome. Please write your username and password separated by a space.");
		try{
			s = in.readLine();
		}catch(IOException e){
			System.out.println("IO Exception while reading authentication input.");
		}

		String[] words = s.split(" ");
		Authentication auth = new Authentication(words[0],words[1]);
		try{
			oos.writeObject((Object)auth);
		}catch(IOException e){
			System.out.println("IO Exception while sending authentication input.");
		}
		wait.waitForAuth();
	}
	
	
}
