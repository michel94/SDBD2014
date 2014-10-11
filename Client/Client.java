public class Client{
	private int serverPort = 6000;
	private String serverIp = "127.0.0.1";
	private Socket socket;
	private ObjectInputStream ois;
	private ListenerThread listenerThread;

	public Client(){
		socket = new Socket(serverIp, serverPort);
		DataInputStream in = new DataInputStream(socket.getInputStream());
		ois = new ObjectInputStream(pin);
		listenerThread = new ListenerThread(ois);

	}
	public static void main(String[] args){
		Client client = new Client();
	}
}