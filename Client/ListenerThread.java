public class ListenerThread() implements Runnable{
	private ObjectInputStream ois;


	public ListenerThread(ObjectInputStream ois){
		this.ois = ois;
		Thread t = new Thread();
		t.start();
	}
	public void run(){
		while(true){
			//Receive Updates from server
			Object receiveObject = ois.readObject();
			
		}
	}
}