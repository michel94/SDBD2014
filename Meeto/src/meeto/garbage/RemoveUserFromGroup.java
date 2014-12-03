package meeto.garbage;
import java.io.*;

public class RemoveUserFromGroup implements Serializable{
	public int iduser, idgroup;
	public RemoveUserFromGroup(int iduser, int idgroup){
		this.iduser = iduser;
		this.idgroup = idgroup;
	}
	public RemoveUserFromGroup(){
	}
}
