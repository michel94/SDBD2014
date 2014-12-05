package meeto.action;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import meeto.bean.ConnectionBean;
import meeto.bean.ItemBean;
import meeto.bean.MeetingBean;
import meeto.bean.UserBean;
import meeto.garbage.User;

public class MeetingAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private ConnectionBean connectionBean;
	private MeetingBean meetingBean;
	private UserBean userBean;
	private int iduser, idmeeting=0;
	private String title, description, datetime, location;
	private int assigned_user;
	private User leader;
	private String view;
	private int done;
	private String dueTo;
	private ArrayList<Integer> iUsers;
	
	private Boolean checkString(String field){
		return field != null && !field.equals("");
	}
	
	public String selectMeeting() {
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		meetingBean = new MeetingBean((int)session.get("iduser"));
		meetingBean.setMeetingId(idmeeting);
		userBean = new UserBean();
		
		view = "meeting";
		
		return SUCCESS;
		
	}
	
	public String createMeeting() {
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		if( !(checkString(title) && checkString(description) && checkString(datetime) && checkString(location)) )
			return SUCCESS;
		
		
		meetingBean = new MeetingBean((int)session.get("iduser"));
		
		User leader = new User();
		leader.iduser = (int)session.get("iduser");
		meetingBean.createMeeting(title, description, datetime, location, leader);
		
		view = "createmeeting";
		
		return SUCCESS;
		
	}
	
	public String editMeeting(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		System.out.println(idmeeting);
		
		System.out.println("Edit Meeting");
		
		meetingBean = new MeetingBean((int)session.get("iduser"));
		meetingBean.setMeetingId(idmeeting);
		
		System.out.println("OK");
		meetingBean.editMeeting(title, description, datetime, location);
		System.out.println("OK2");
		view = "meeting";
		
		System.out.println("Meeting Edited");
		
		return SUCCESS;
	}
	
	public String menumeetings(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		meetingBean = new MeetingBean((int)session.get("iduser"));
		view = "meetings";
		
		return SUCCESS;
	}
	
	public String menucreatemeeting(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		meetingBean = new MeetingBean((int)session.get("iduser"));
		view = "menucreatemeeting";
		
		return SUCCESS;
	}
	
	public String createItem(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		iduser = (int) session.get("iduser");
		
		meetingBean = new MeetingBean(iduser);
		meetingBean.setMeetingId(idmeeting);
		
		System.out.println(iduser + " " + idmeeting);
		meetingBean.createItem(title, description);
		
		view = "meeting";
		return SUCCESS;
	}
	
	public String createAction(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		iduser = (int) session.get("iduser");
		
		meetingBean = new MeetingBean(iduser);
		meetingBean.setMeetingId(idmeeting);
		
		System.out.println(iduser + " " + idmeeting);
		meetingBean.createAction(description, dueTo);
		
		view = "meeting";
		return SUCCESS;
	}
	
	public String inviteUsers(){
		System.out.println("Invite Users");
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		iduser = (int) session.get("iduser");
		
		meetingBean = new MeetingBean(iduser);
		meetingBean.setMeetingId(idmeeting);
		
		System.out.println(iduser + " " + idmeeting);
		meetingBean.addUsersToMeeting(iUsers);
		
		
		view = "meeting";
		return SUCCESS;
	}
	
	public void setIdMeeting(int idmeeting){
		System.out.println(idmeeting);
		this.idmeeting = idmeeting;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session = session;
	}
	
	public MeetingBean getMeetingBean(){
		return meetingBean;
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
	public void setDatetime(String d){
		datetime = d;
	}
	public void setLocation(String l){
		location = l;
	}
	public void setAssignedUser(int u){
		assigned_user = u;
	}
	public void setDone(int done){
		this.done = done;
	}
	public void setDueTo(String dueTo){
		this.dueTo = dueTo;
	}
	public void setInvUsers(int[] iu){
		iUsers = new ArrayList<Integer>();
		System.out.println("Set invite users");
		for(int u : iu){
			System.out.println(u);
			iUsers.add(u);
		}
	}
	
	public String getView(){
		return view;
	}
	
}
