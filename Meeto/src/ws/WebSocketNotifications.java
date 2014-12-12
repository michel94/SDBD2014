package ws;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import meeto.bean.GroupBean;
import meeto.bean.ItemBean;
import meeto.bean.MeetingBean;
import meeto.bean.UserBean;
import meeto.garbage.Item;
import meeto.garbage.Comment;
import meeto.garbage.User;

@ServerEndpoint(value = "/wsnot", configurator = GetHttpSessionConfigurator.class)

public class WebSocketNotifications {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private static final ArrayList<ClientData> clientsonline = new ArrayList<ClientData>();
    private int iditem;
    private int iduser,idinviteduser;
    
    private String username;
    private Session session;
	private HttpSession httpSession;
	
	private UserBean userBean;
	private ItemBean itemBean;
	private Item item;
	private User user;
	private MeetingBean meetingBean;
	private  GroupBean groupBean;
	
	public WebSocketNotifications() {
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
        meetingBean = new MeetingBean(iduser);
        groupBean = new GroupBean(iduser,-1);
        
    }

    @OnClose
    public void end() {
    	// clean up once the WebSocket connection is closed
    	
    	for(int i=0; i<clientsonline.size(); i++){
    		if(clientsonline.get(i).conn == session.getBasicRemote()){
    			clientsonline.remove(i);
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
    		iduser = Integer.parseInt(message);
    		clientsonline.add(new ClientData(session.getBasicRemote(), user) );
    		
    	}
    }
    
	public static void broadcastMeeting(int iduser, String mname) throws IOException{
		for(int i=0;i<clientsonline.size();i++){
			
			if(clientsonline.get(i).user.iduser==iduser){
				clientsonline.get(i).conn.sendText("You were invited to meeting "+mname);
				break;
			}
		}
	}
	
	public static void broadcastGroup(int iduser, String gname) throws IOException{
		for(int i=0;i<clientsonline.size();i++){
			
			if(clientsonline.get(i).user.iduser==iduser){
				clientsonline.get(i).conn.sendText("You were invited to group "+gname);
				break;
			}
		}
	}
   
    @OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }
    
}
