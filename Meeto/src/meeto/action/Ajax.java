package meeto.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class Ajax extends ActionSupport implements SessionAware {
	private Map<String, Object> session;
	private InputStream stream;
	private String output;
	
	public String test(){
		System.out.println("Received ajax request");
		stream = new ByteArrayInputStream("asdasd".getBytes());
		output = "nice";
		return SUCCESS;
	}
	
	public String getOutput(){
		return output;
	}

	@Override
	public void setSession(Map<String, Object> s){
		// TODO Auto-generated method stub
		session = s;
	}
	
}
