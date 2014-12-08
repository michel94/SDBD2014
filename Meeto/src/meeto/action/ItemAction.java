package meeto.action;

import java.util.Map;

import meeto.bean.ConnectionBean;
import meeto.bean.ItemBean;
import meeto.bean.KeyDecisionBean;
import meeto.bean.MeetingBean;
import meeto.bean.UserBean;
import meeto.garbage.User;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class ItemAction extends ActionSupport implements SessionAware {
	private String title, description;
	private ItemBean itemBean;
	private UserBean userBean;
	private KeyDecisionBean keyDecisionBean;
	private Map<String, Object> session;
	private String view;
	private int iditem ,idkeydecision;
	
	public void setIdkeydecision(int idkeydecision) {
		this.idkeydecision = idkeydecision;
	}

	private Boolean checkString(String field){
		return field != null && !field.equals("");
	}
	
	public String selectItem() {
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		itemBean = new ItemBean((int)session.get("iduser"), iditem);
		userBean = new UserBean();
		userBean.setUserId((int)session.get("iduser"));
		System.out.println(iditem);
		view = "item";
		
		return SUCCESS;
		
	}
	
	public String editItem(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		System.out.println(iditem);
		int iduser = (int) session.get("iduser");
		itemBean = new ItemBean(iduser, iditem);
		userBean = new UserBean();
		userBean.setUserId((int)session.get("iduser"));
		
		itemBean.editItem(title, description, iduser);
		view = "item";
		
		return SUCCESS;
	}
	
	public String editKeyDecision(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		System.out.println(idkeydecision);
		int iduser = (int) session.get("iduser");
		keyDecisionBean = new KeyDecisionBean(idkeydecision);
		keyDecisionBean.editKeyDecision(idkeydecision,description,iditem);
		selectItem();
		view = "item";
		
		return SUCCESS;
	}
	
	public String createKeyDecision(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		System.out.println(idkeydecision);
		keyDecisionBean = new KeyDecisionBean(idkeydecision);
		keyDecisionBean.addKeyDecisionToItem(iditem, description);
		selectItem();
		view = "item";
		return SUCCESS;
	}
	public void setIdItem(int iditem){
		this.iditem = iditem;
	}
	
	public ItemBean getItemBean(){
		return itemBean;
	}
	
	public UserBean getUserBean(){
		return userBean;
	}
	
	public void setTitle(String t){
		title = t;
	}
	public void setDescription(String d){
		description = d;
	}
	
	public String getView(){
		return view;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session = session;
	}
	
}
