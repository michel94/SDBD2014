package ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSession;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import meeto.bean.ClientData;
import meeto.bean.ItemBean;
import meeto.bean.UserBean;
import meeto.garbage.Item;
import meeto.garbage.Comment;
import meeto.garbage.User;

@ServerEndpoint(value = "/ws", configurator = GetHttpSessionConfigurator.class)

public class WebSocketServer {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private static final HashMap<Integer, ArrayList<ClientData> > itemRooms = new HashMap<Integer, ArrayList<ClientData> >();
    private int iditem;
    private int iduser;
    
    private String username;
    private Session session;
	private HttpSession httpSession;
	
	private UserBean userBean;
	private ItemBean itemBean;
	private Item item;
	private User user;
	
	
	public WebSocketServer() {
        UserBean userBean = new UserBean();
	}
    
	@OnOpen
	public void start(Session session, EndpointConfig config) {
    	this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        iduser = (int) httpSession.getAttribute("iduser");
        username = (String) httpSession.getAttribute("username");
        
        userBean = new UserBean();
        userBean.setUserId(iduser);
        user = userBean.getUser();
        
    }

    @OnClose
    public void end() {
    	// clean up once the WebSocket connection is closed
    	ArrayList<ClientData> endpoints = itemRooms.get(iditem);
    	for(int i=0; i<endpoints.size(); i++){
    		if(endpoints.get(i).conn == session.getBasicRemote()){
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
    		iditem = Integer.parseInt(message);
    		
    		if(!itemRooms.containsKey(iditem))
    			itemRooms.put(iditem, new ArrayList<ClientData>());
    		
    		itemRooms.get(iditem).add(new ClientData(session.getBasicRemote(), user) );
    		
    		enterChat();
    	}else{
    		try{
    			itemBean.commentOnItem(message, iduser);
    			broadcast(message);
    		}catch(IOException e){}
    	}
    }
    
	private void broadcast(String message) throws IOException{
		if(!itemRooms.containsKey(iditem))
			return;
		
		for(ClientData client : itemRooms.get(iditem)){
			client.conn.sendText(user.username + ": " + message);
		}
	}
	
	private void enterChat(){
		itemBean = new ItemBean(iduser, iditem);
        
        Item item = itemBean.getItem();
        for(Comment comment : item.comments){
        	sendMessage(comment.user.username + ": " + comment.text);
        }
        
        String message = username + " entered the chat.";
        sendMessage(message);
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
