package meeto.garbage;

import java.io.*;

public class Authentication implements Serializable {
	private static final long serialVersionUID = 1L;
	public String username, password;
	public int confirmation = 0;
	public int clientID = 0;
	public User userData = null;

	public Authentication(String usr, String pass) {
		username = usr;
		password = pass;
	}

	public void confirm(int c) {
		confirmation = 1;
		clientID = c;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getConfirmation() {
		return confirmation;
	}

	public int getClientID() {
		return clientID;
	}

	public User getUserData() {
		return userData;
	}
}
