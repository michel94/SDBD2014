package meeto.action;

import java.util.Map;
import java.util.Scanner;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.interceptor.SessionAware;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import meeto.bean.ConnectionBean;
import meeto.bean.GroupBean;
import meeto.bean.MeetingBean;
import meeto.bean.UserBean;
import meeto.garbage.Authentication;

public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private String username = null, password = null;
	private Map<String, Object> session;
	private ConnectionBean connectionBean;
	private MeetingBean meetingBean;
	private int iduser;
	private String view;
	private GroupBean groupBean;
	private static final String NETWORK_NAME = "Google";
	private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
	private static final String SCOPE = "https://www.googleapis.com/auth/calendar"; 
	private static final Token EMPTY_TOKEN = null;
	
	private Boolean checkString(String field){
		return field != null && !field.equals("");
	}
	
	@Override
	public String execute() {
		System.out.println(username);
		System.out.println(password);
		
		if(checkString(username) && checkString(password)) {
			System.out.println("Logged In");
			connectionBean = new ConnectionBean();
			
			Authentication auth = connectionBean.login(username, password);
			if (auth.confirmation > 0) {
				this.session.put("username", username);
				iduser = auth.clientID;
				this.session.put("iduser", iduser);
				
				meetingBean = new MeetingBean(iduser);
				connectionBean.setSession(session);
				meetingBean.setUserId(iduser);
				groupBean = new GroupBean(iduser,-1);
				session.put("groupBean",groupBean);
				view = "meetingsnext";
				  
				OAuthService service = new ServiceBuilder()
				      .provider(GoogleApi.class)
				      .apiKey("1071197260604-nseqc1eioq74g9pbf0t2tg497cgoo5mj.apps.googleusercontent.com")
				      .apiSecret("bUb2vEiLOGTz9kembph6ozjl")
				      .scope(SCOPE)
				      .callback("http://localhost:8080/Meeto/googlecallback")
				      .build();
				Scanner in = new Scanner(System.in);
				
				System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
				System.out.println();
				
				// Obtain the Request Token
				System.out.println("AAA...");
				String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
				System.out.println(authorizationUrl);	
				session.put("googleService", service);
				session.put("authorizationUrl", authorizationUrl);
				
				
				return SUCCESS;
			}else{
				return LOGIN;
			}
			
		}
		else
			return LOGIN;
	}
	
	public String register(){
		UserBean userBean = new UserBean();
		if(userBean.registerUser(username, password)==-1){
			return LOGIN;
		}else return execute();
		
	}
	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session = session;
	}
	
	public void setUsername(String u){
		username = u;
	}
	public void setPassword(String p){
		password = p;
	}
	
	public ConnectionBean getConnectionBean(){
		return connectionBean;
	}
	
	public MeetingBean getMeetingBean(){
		return meetingBean;
	}
	
	public String getView(){
		System.out.println("View: " + view);
		return view;
	}
	
}
