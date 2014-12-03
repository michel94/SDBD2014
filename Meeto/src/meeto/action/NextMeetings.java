package meeto.action;

import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import meeto.bean.ConnectionBean;
import meeto.bean.MeetingBean;
import meeto.garbage.Authentication;

public class NextMeetings extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private String username = null, password = null;
	private Map<String, Object> session;
	private MeetingBean meetingBean;
	
	
	private Boolean checkString(String field){
		return field != null && !field.equals("");
	}
	
	@Override
	public String execute() {
		if(!session.containsKey(username))
			return LOGIN;
		
		meetingBean = new MeetingBean();
		
		return SUCCESS;
		
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
	
	public MeetingBean getMeetingBean(){
		return meetingBean;
	}
	
}
