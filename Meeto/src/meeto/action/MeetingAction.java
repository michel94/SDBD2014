package meeto.action;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import meeto.bean.ActionBean;
import meeto.bean.ConnectionBean;
import meeto.bean.GroupBean;
import meeto.bean.MeetingBean;
import meeto.bean.UserBean;
import meeto.garbage.User;

public class MeetingAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private ConnectionBean connectionBean;
	private MeetingBean meetingBean;
	private UserBean userBean;
	private int iduser, idmeeting=0, idgroup;
	private String title, description, datetime, location;
	private int assigneduser;
	private User leader;
	private String view;
	private int done, idaction;
	
	public void setIdAction(int idaction) {
		this.idaction = idaction;
	}

	private String dueto;
	private ArrayList<String> userList;
	private GroupBean groupBean;
	private ActionBean actionBean;
	
	private Boolean checkString(String field){
		return field != null && !field.equals("");
	}
	
	public ArrayList<String> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<String> userList) {
		this.userList = userList;
	}
	
	public void setIdgroup(int idgroup) {
		this.idgroup = idgroup;
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
		
		view = "menucreatemeeting";
		
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
	
	public String menumeetingsnext(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		meetingBean = new MeetingBean((int)session.get("iduser"));
		view = "meetingsnext";
		
		return SUCCESS;
	}
	
	public String menumeetingsprevious(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		meetingBean = new MeetingBean((int)session.get("iduser"));
		view = "meetingsprevious";
		
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
		
		selectMeeting();
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
		meetingBean.createAction(description, dueto);
		
		selectMeeting();
		view = "meeting";
		return SUCCESS;
	}
	
	public String leaveMeeting(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		iduser = (int) session.get("iduser");
		
		meetingBean = new MeetingBean(iduser);
		meetingBean.setMeetingId(idmeeting);
		
		System.out.println(iduser + " " + idmeeting);
		meetingBean.leaveMeeting(idmeeting,iduser);
	
		view = "meetingsnext";
		return SUCCESS;
	}
	
	public String addUsersToMeeting(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		iduser = (int) session.get("iduser");
		
		meetingBean = new MeetingBean(iduser);
		meetingBean.setMeetingId(idmeeting);
		
		System.out.println(iduser + " " + idmeeting);
		meetingBean.addUsersToMeeting(userList, idmeeting);
		selectMeeting();
		view = "meeting";
		return SUCCESS;
	}
	
	public String addGroupToMeeting(){
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		iduser = (int) session.get("iduser");
		
		meetingBean = new MeetingBean(iduser);
		meetingBean.setMeetingId(idmeeting);
		
		System.out.println(iduser + " " + idmeeting);
		meetingBean.addGroupToMeeting(idgroup, idmeeting);
		selectMeeting();
		view = "meeting";
		return SUCCESS;
	}
	
	public String selectAction() {
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		actionBean = new ActionBean((int)session.get("iduser"), idaction, done);
		userBean = new UserBean();
		userBean.setUserId((int)session.get("iduser"));
		session.put("actionBean", actionBean);
		session.put("userBean", userBean);
		System.out.println(idaction);
		view = "action";
		
		return SUCCESS;
		
	}
	public String editAction() {
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		actionBean = new ActionBean(assigneduser, idaction,done);
		userBean = new UserBean();
		userBean.setUserId((int)session.get("iduser"));
		
		actionBean.updateAction(description, dueto);
		session.put("actionBean", actionBean);
		session.put("userBean", userBean);
		System.out.println(idaction);
		view = "action";
		
		return SUCCESS;
		
	}
	
public String confirmAction() {
		
		if(!session.containsKey("iduser"))
			return LOGIN;
		
		actionBean = new ActionBean(iduser, idaction,1);
		actionBean.confirmAction();
		view = "action";
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
	public void setAssigneduser(int u){
		System.out.println("Imhere");
		assigneduser = u;
	}
	public void setDone(int done){
		this.done = done;
	}
	public void setDueto(String dueto){
		System.out.println("Imhere2");
		this.dueto = dueto;
	}
	
	public String getView(){
		return view;
	}
	
}
