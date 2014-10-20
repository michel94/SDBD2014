public class WaitClient{
	Object auth, meetings;
	public WaitClient(){
		auth = new Object();
		meetings = new Object();
		meeting = new Object();
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
	
	public void waitForMeeting(){
		wait(meeting);
	}
	public void notifyMeeting(){
		notify(meeting);
	}
	

}
