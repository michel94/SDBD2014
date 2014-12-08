package meeto.action;

import java.util.Map;

import meeto.bean.ActionBean;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class TodoAction extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> session;
    private ActionBean actionBean;
	private String view;
	
	
	public String actionsdone(){
		
		actionBean = new ActionBean((int)session.get("iduser"),-1,-1);
		session.put("actionBean",actionBean);
		setView("actionsdone");
		return SUCCESS;
		
	}
	public String actionsnotdone(){

		actionBean = new ActionBean((int)session.get("iduser"),-1,-1);
		session.put("actionBean",actionBean);
		setView("actionsnotdone");
		return SUCCESS;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session=session;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	
	

}
