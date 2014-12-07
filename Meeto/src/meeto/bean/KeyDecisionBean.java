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
	private int idkeydecision;
	
	public KeyDecisionBean(int idkeydecision){
		this.idkeydecision = idkeydecision;
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public int addKeyDecisionToItem(int iditem, String description){
		KeyDecision kd = new KeyDecision(-1, description, 1, iditem);
		
		try {
			database.insertKeyDecision(kd);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int editKeyDecision(int idkeydecision,String description, int item){
		KeyDecision kd= new KeyDecision(idkeydecision,description,item,1);
		
		try {
			database.updateKeyDecision(kd);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	public int deleteKeyDecision(int idkeydecision){
		try {
			database.deleteKeyDecision(idkeydecision);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	

}
