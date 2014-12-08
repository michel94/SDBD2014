package meeto.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.Map;

public class GoogleCallback extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String code;
	
	private Token EMPTY_TOKEN = null;
	
	public String receive() {
		System.out.println("Callback");
		//GoogleServiceBean googleServiceBean =  (GoogleServiceBean) session.get("googleServiceBean");
		OAuthService service = (OAuthService)session.get("googleService");
		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
		System.out.println(accessToken.getToken());
		session.put("accessToken", accessToken);
		
		return SUCCESS;
	}
	
	public void setCode(String code) {
		System.out.println(code);
		this.code = code;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}