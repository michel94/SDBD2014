package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import meeto.bean.UserBean;

@ServerEndpoint(value = "/ws")
public class WebSocketServer {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private static final HashMap<Integer, ArrayList<RemoteEndpoint.Basic > > itemRooms = new HashMap<Integer, ArrayList<RemoteEndpoint.Basic > >();
    private int iditem;
    private final String username;
    private Session session;

    public WebSocketServer() {
        username = "User" + sequence.getAndIncrement();
        UserBean userBean = new UserBean();
    }
    
    @OnOpen
    public void start(Session session) {
        this.session = session;
        String message = "*" + username + "* connected.";
        sendMessage(message);
    }

    @OnClose
    public void end() {
    	// clean up once the WebSocket connection is closed
    	ArrayList<RemoteEndpoint.Basic> endpoints = itemRooms.get(iditem);
    	for(int i=0; i<endpoints.size(); i++){
    		if(endpoints.get(i) == session.getBasicRemote()){
    			endpoints.remove(i);
    			return;
    		}
    		
    	}
    }

    @OnMessage
    public void receiveMessage(String message) {
		// one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
    	
    	if(message.startsWith("/s")){
    		message = message.substring(3);
    		System.out.println("number: " + message);
    		System.out.println("Ok");
    		iditem = Integer.parseInt(message);
    		if(!itemRooms.containsKey(iditem))
    			itemRooms.put(iditem, new ArrayList<RemoteEndpoint.Basic>());
    		itemRooms.get(iditem).add(session.getBasicRemote());
    	}else{
    		try{
    			broadcast(message);
    		}catch(IOException e){}
    	}
    	//String reversedMessage = new StringBuffer(message).reverse().toString();
    	//sendMessage("[" + username + "] " + reversedMessage);
    }
    
	private void broadcast(String message) throws IOException{
		if(!itemRooms.containsKey(iditem))
			return;
		
		for(RemoteEndpoint.Basic client : itemRooms.get(iditem)){
			client.sendText(message);
		}
	}
   
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }

    private void sendMessage(String text) {
    	try {
			this.session.getBasicRemote().sendText(text);
		} catch (IOException e) {
			try {
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
}
