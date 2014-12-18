package meeto.action;

import com.opensymphony.xwork2.ActionSupport;

import meeto.bean.ConnectionBean;
import meeto.bean.GroupBean;
import meeto.bean.MeetingBean;

import org.apache.struts2.interceptor.SessionAware;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.Map;

public class GoogleCallback extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String code;
	private String view;
	private MeetingBean meetingBean;
	private ConnectionBean connectionBean;
	private GroupBean groupBean;
	
	private Token EMPTY_TOKEN = null;
	
	public String receive() {
		System.out.println("Callback");
		//GoogleServiceBean googleServiceBean =  (GoogleServiceBean) session.get("googleServiceBean");
		System.out.println(session.containsKey("googleService"));
		OAuthService service = (OAuthService)session.get("googleService");
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
		System.out.println("access token: " + accessToken.getToken());
		session.put("accessToken", accessToken);
		System.out.println("googleCallback");
		
		int iduser = (int)this.session.get("iduser");
		meetingBean = new MeetingBean(iduser);
		connectionBean = new ConnectionBean();
		
		connectionBean.setSession(session);
		meetingBean.setUserId(iduser);
		groupBean = new GroupBean(iduser,-1);
		session.put("groupBean",groupBean);
		view = "meetingsnext";
		
		return SUCCESS;
	}
	
	public MeetingBean getMeetingBean(){
		return meetingBean;
	}
	public ConnectionBean getConnectionBean(){
		return connectionBean;
	}
	
	public void setCode(String code) {
		System.out.println(code);
		this.code = code;
	}
	
	public String getView(){
		return view;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}