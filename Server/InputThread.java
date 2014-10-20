import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public class InputThread implements Runnable {
	Thread t;
	Socket clientSocket;
	int clientId;
	ObjectInputStream in;
	ObjectOutputStream threadOut;
	ConcurrentHashMap<Integer, ClientData> clients;

	public InputThread(Socket c, int n, ObjectOutputStream os, ConcurrentHashMap<Integer, ClientData> cl) {
		t = new Thread(this, "in");
		clientId = n;
		clientSocket = c;
		threadOut = os;
		clients = cl;

		t.start();
	}

	public void run() {
		Object data;
		System.out.println("Client out " + clientId + " " + "is running...");

		try{
			DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
			in = new ObjectInputStream(dis);
			clients.get(clientId).in = in;
			while(true){
				try {
					data = in.readObject();
					System.out.println("Received some data");
					threadOut.writeObject(data);
					System.out.println("Wrote on pipe of client " + clientId + "...");
					
				} catch (ClassNotFoundException e) {
					System.out.println("Error: Class not found while reading request from client " + clientId + ".");
				}
			}
		} catch (IOException e) {
			System.out.println("Error:IO Exception while reading request of client " + clientId + ".");
		}
		System.out.println("Closing connection from client " + clientId + ".");
		
	}
}
