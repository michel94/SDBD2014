package meeto.bean;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import meeto.garbage.DatabaseInterface;
import meeto.garbage.Group;
import meeto.garbage.InviteUser;
import meeto.garbage.InviteUsers;
import meeto.garbage.RemoveUserFromGroup;
import meeto.garbage.RemoveUsersFromGroup;

public class GroupBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database;
	private Map<String, Object> session;
	
	public GroupBean(){
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public int createGroup(String groupname){
		 Group grp= new Group();
		 grp.name = groupname;
		 
		 try {
			database.createGroup(grp);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
	}
	
	public int addUsersToGroup(ArrayList<String> userids, int groupid){
		InviteUsers invus = new InviteUsers();
		InviteUser invu = null;
		
		for(int i=0;i<userids.size();i++){
			invu = new InviteUser(Integer.parseInt(userids.get(i)),groupid);
			invus.add(invu);
		}
		
		try {
			database.inviteUsersToGroup(invus);
			return 0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public int removeUsersFromGroup(ArrayList<String> userids, int groupid){
		RemoveUserFromGroup ru= new RemoveUserFromGroup();
		
		ru.idgroup = groupid;
		
		for(int i=0;i<userids.size();i++){
			ru.iduser = Integer.parseInt(userids.get(i));
			try {
				database.removeUserFromGroup(ru);
				return 1;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
		}
		return 1;
	}
	
	
}
