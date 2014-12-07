package meeto.bean;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import meeto.garbage.DatabaseInterface;
import meeto.garbage.Group;
import meeto.garbage.Groups;
import meeto.garbage.InviteUser;
import meeto.garbage.InviteUsers;
import meeto.garbage.RemoveUserFromGroup;
import meeto.garbage.RemoveUsersFromGroup;

public class GroupBean {
	private final String databaseIP = "localhost";
	private final int databasePort = 1200;
	private DatabaseInterface database;
	int iduser;
	

	public GroupBean(int idUser){
		
		this.iduser=idUser;
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
	
	public int addUsersToGroup(ArrayList<String> idusers, int idgroup){
		InviteUsers invus = new InviteUsers();
		InviteUser invu = null;
		
		for(int i=0;i<idusers.size();i++){
			invu = new InviteUser(Integer.parseInt(idusers.get(i)),idgroup);
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
	
	public int removeUsersFromGroup(ArrayList<String> idusers, int idgroup){
		RemoveUserFromGroup ru= new RemoveUserFromGroup();
		
		ru.idgroup = idgroup;
		
		for(int i=0;i<idusers.size();i++){
			ru.iduser = Integer.parseInt(idusers.get(i));
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
	
	public Groups getGroupsFromUser(){
		Groups grps = null;
		System.out.println("Im here and you?");
		try {
			grps = database.getGroupsOfUser(iduser);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return grps;
		
	}
	
	
}
