package meeto.garbage;
import java.io.*;

public class RemoveUserFromGroup implements Serializable{
	public int iduser, idgroup;
	public void setIduser(int iduser) {
		this.iduser = iduser;
	}
	public void setIdgroup(int idgroup) {
		this.idgroup = idgroup;
	}
	public RemoveUserFromGroup(int iduser, int idgroup){
		this.iduser = iduser;
		this.idgroup = idgroup;
	}
	public RemoveUserFromGroup(){
	}
}
