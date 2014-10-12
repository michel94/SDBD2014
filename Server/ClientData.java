import java.io.*;

public class ClientData{
	private int uid;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public ClientData(ObjectOutputStream out, ObjectInputStream in){
		this.out = out;
		this.in = in;
	}
}