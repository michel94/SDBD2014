package meeto.action;

import java.util.Map;

import meeto.bean.ActionBean;
import meeto.bean.ConnectionBean;
import meeto.bean.ItemBean;
import meeto.bean.MeetingBean;
import meeto.bean.UserBean;
import meeto.garbage.User;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class ActionAction extends ActionSupport implements SessionAware {
	private String description;
	private ActionBean actionBean;
	private UserBean userBean;
	private Map<String, Object> session;
	private String view;
	private int idaction;
	
	private Boolean checkString(String field){
		return field != null && !field.equals("");
	}
	
	public String selectAction() {
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		actionBean = new ActionBean((int)session.get("iduser"), idaction);
		userBean = new UserBean();
		userBean.setUserId((int)session.get("iduser"));
		System.out.println(idaction);
		view = "item";
		
		return SUCCESS;
		
	}
	
/*	 public String editAction(){
		/*if(!session.containsKey("iduser"))
			return LOGIN;
		
		System.out.println(iditem);
		int iduser = (int) session.get("iduser");
		itemBean = new ItemBean(iduser, iditem);
		userBean = new UserBean();
		userBean.setUserId((int)session.get("iduser"));
		
		itemBean.editItem(title, description, iduser);
		view = "item";
		
		return SUCCESS;
	}*/
	

	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
