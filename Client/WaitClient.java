public class WaitClient{
	Object auth, meetings, meeting, item, action, group, groups, reconnect, def, leavemeeting, deleteuser, users;

	public WaitClient(){
		auth = new Object();
		def = new Object();
		meetings = new Object();
		meeting = new Object();
		item = new Object();
		action = new Object();
		groups = new Object();
		group = new Object();
		reconnect = new Object();
		users = new Object();
		leavemeeting = new Object();
		deleteuser = new Object();
	}
	private void wait(Object o){
		synchronized(o) {
			try {
				o.wait();
			} catch (InterruptedException e) {}
		}
	}

	private void notify(Object o){
		synchronized(o) {
			o.notify();
		}
	}

	public void waitAuth(){
		wait(auth);
	}
	public void notifyAuth(){
		notify(auth);
	}

	public void waitMeetings(){
		wait(meetings);
	}
	public void notifyMeetings(){
		notify(meetings);
	}
	
	public void waitMeeting(){
		wait(meeting);
	}
	public void notifyMeeting(){
		notify(meeting);
	}
	public void waitItem(){
		wait(item);
	}
	public void notifyItem(){
		notify(item);
	}
	public void waitAction(){
		wait(action);
	}
	public void notifyAction(){
		notify(action);
	}

	public void waitGroup(){
		wait(group);
	}
	public void notifyGroup(){
		notify(group);
	}
	public void waitGroups(){
		wait(groups);
	}
	public void notifyGroups(){
		notify(groups);
	}

	public void waitReconnect(){
		wait(reconnect);
	}
	public void notifyReconnect(){
		notify(reconnect);

	}
	
	public void waitDefault(){
		wait(def);
	}

	public void notifyDefault(){
		notify(def);
	}
	public void waitLeaveMeeting(){
		wait(leavemeeting);
	}

	public void notifyLeaveMeeting(){
		notify(leavemeeting);
	}
	public void waitDeleteUser(){
		wait(deleteuser);
	}

	public void notifyDeleteUser(){
		notify(deleteuser);
	}

	public void waitUsers(){
		wait(users);
	}

	public void notifyUsers(){
		notify(users);
	}

}
