package Server;

import java.io.*;

public class Request implements Serializable{
	String type;
	int id;
	public Request(String type){
		this.type = type;
	}
	public Request(String type, int id){
		this.type = type;
		this.id = id;	
	}
}