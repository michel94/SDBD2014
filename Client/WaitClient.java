public class WaitClient{
	Object auth, meetings, meeting, item, action;
	public WaitClient(){
		auth = new Object();
		meetings = new Object();
		meeting = new Object();
		item = new Object();
		action = new Object();
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

	public void waitForAuth(){
		wait(auth);
	}
	public void notifyAuth(){
		notify(auth);
	}
	
	public void waitForMeetings(){
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
	

}
