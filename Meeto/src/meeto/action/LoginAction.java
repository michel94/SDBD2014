package meeto.action;

import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.interceptor.SessionAware;

import meeto.bean.ConnectionBean;
import meeto.bean.MeetingBean;
import meeto.garbage.Authentication;

public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private String username = null, password = null;
	private Map<String, Object> session;
	private ConnectionBean connectionBean;
	private MeetingBean meetingBean;
	private int iduser;
	private String view;
	
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
				
				view = "meetings";
				
				return SUCCESS;
			}else{
				return LOGIN;
			}
			
		}
		else
			return LOGIN;
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
