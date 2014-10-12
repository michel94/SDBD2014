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

public class InputThread implements Runnable {
	Thread t;
	Socket clientSocket;
	int cnumber;
	ObjectInputStream in;
	ObjectOutputStream threadOut;

	public InputThread(Socket c, int n, ObjectOutputStream os) {
		t = new Thread(this, "in");
		cnumber = n;
		clientSocket = c;
		threadOut = os;

		t.start();
	}

	public void run() {
		Object data;
		System.out.println("Client out " + cnumber + " " + "is running...");

		try{
			DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
			in = new ObjectInputStream(dis);
			
			while(true){
				try {
					data = in.readObject();
					System.out.println("Received some data");
					threadOut.writeObject(data);
					System.out.println("Wrote on pipe of client "+cnumber +"...");
					
				} catch (ClassNotFoundException e) {
					System.out.println("Error: Class not found while reading request from client " + cnumber + ".");
				}
			}
		} catch (IOException e) {
			System.out.println("Error:IO Exception while reading request of client "+cnumber +".");
		}
		System.out.println("Closing connection from client " + cnumber + ".");
		
	}
}
