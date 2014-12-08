package meeto.action;

import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import meeto.bean.ConnectionBean;
import meeto.bean.GroupBean;
import meeto.bean.MeetingBean;
import meeto.bean.UserBean;

public class GroupAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	private ConnectionBean connectionBean;
	private GroupBean groupBean;
	private String view, groupname;
	private UserBean userBean;
	private ArrayList<String> userList;
	private int idGroup;
	public int getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(int idGroup) {
		this.idGroup = idGroup;
	}

	public void setUserList(ArrayList<String> userList) {
		this.userList = userList;
	}

	public String menugroups(){
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		System.out.println("Iwenthere");
		groupBean = new GroupBean((int)session.get("iduser"),idGroup);
		session.put("groupBean",groupBean);
		view = "groups";
		
		return SUCCESS;
	}
	
	public String menucreategroup(){
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		userBean = new UserBean();
		session.put("userBean", userBean);
		view = "creategroup";
		
		return SUCCESS;
	}
	
	public String createGroup(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		groupBean = new GroupBean((int)session.get("iduser"),-1);
		groupBean.createGroup(groupname,userList);
		view = "groups";
		
		return SUCCESS;
	}
	
	public String selectGroup(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		groupBean = new GroupBean((int)session.get("iduser"),idGroup);
		userBean = new UserBean();
		session.put("groupBean", groupBean);
		session.put("userBean", userBean);
		view = "group";
		
		return SUCCESS;
	}
	
	public String addUsersToGroup(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		groupBean = new GroupBean((int)session.get("iduser"),idGroup);
		groupBean.addUsersToGroup(userList, idGroup);
		view = "group";
		
		return SUCCESS;
	}
	
	public String removeUsersFromGroup(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		groupBean = new GroupBean((int)session.get("iduser"),idGroup);
		groupBean.removeUsersFromGroup(userList, idGroup);
		view = "group";
		
		return SUCCESS;
	}
	
	


	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session = session;
		
	}
	
	public String getView(){
		return view;
	}
}
