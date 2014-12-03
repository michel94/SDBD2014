package meeto.bean;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;

import meeto.garbage.DatabaseInterface;
import meeto.garbage.KeyDecision;

public class KeyDecisionBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database;
	private Map<String, Object> session;
	
	public KeyDecisionBean(){
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public int addKeyDecisionToItem(int itemid, String description){
		KeyDecision kd = new KeyDecision(-1, description, 1, itemid);
		
		try {
			database.insertKeyDecision(kd);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int editKeyDecision(int keydecisionid,String description, int item,int active){
		KeyDecision kd= new KeyDecision(keydecisionid,description,item,active);
		
		try {
			database.updateKeyDecision(kd);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int deleteKeyDecision(int keydecisionid){
		try {
			database.deleteKeyDecision(keydecisionid);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	

}
