package meeto.bean;

import javax.websocket.RemoteEndpoint;
import meeto.garbage.User;

public class ClientData {
	public RemoteEndpoint.Basic conn;
	public User user;
	public ClientData(RemoteEndpoint.Basic conn, User user){
		this.conn = conn;
		this.user = user;
	}
	
}

