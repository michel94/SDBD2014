package meeto.bean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import ws.WebSocketNotifications;
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
	private int iduser,idgroup;

	public GroupBean(int idUser,int idgroup){
		
		this.iduser=idUser;
		this.idgroup=idgroup;
		try {
			database = (DatabaseInterface) Naming.lookup("//" + databaseIP + ":" + databasePort + "/database");
			
		} catch (NotBoundException|MalformedURLException|RemoteException e) {
			System.out.println("Failed to connect to the rmi server");
			e.printStackTrace();
		}
	}
	
	public int createGroup(String groupname, ArrayList<String> userList){
		 Group grp= new Group();
		 grp.name = groupname;
		  
		 try {
			for(int i=0;i<userList.size();i++){
				grp.users.add(database.getUser(Integer.parseInt(userList.get(i))));
			}
			database.createGroup(grp);
			for(int i=0;i<userList.size();i++){
				try {
					WebSocketNotifications.broadcastGroup(Integer.parseInt(userList.get(i)), groupname);
				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
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
		this.idgroup=idgroup;
		Group gp = getGroup();
		try {
			database.inviteUsersToGroup(invus);
			for(int i=0;i<idusers.size();i++){
				try {
					WebSocketNotifications.broadcastGroup(Integer.parseInt(idusers.get(i)), gp.name);
				} catch (NumberFormatException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
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
		
		try {
			grps = database.getGroupsOfUser(iduser);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return grps;
		
	}
	
	public Group getGroup(){
		Group grp = null;
		
		try {
			grp = database.getGroup(idgroup);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return grp;
		
	}

	public int getIdgroup() {
		return idgroup;
	}

	public void setIdgroup(int idgroup) {
		this.idgroup = idgroup;
	}
	
	
}
