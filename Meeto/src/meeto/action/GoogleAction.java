package meeto.action;

import java.util.*;

import org.apache.struts2.interceptor.SessionAware;
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import com.opensymphony.xwork2.ActionSupport;

public class GoogleAction  extends ActionSupport implements SessionAware{
  private static final String NETWORK_NAME = "Google";
  private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
  private static final String PROTECTED_RESOURCE_URL = "https://docs.google.com/feeds/default/private/full/";
  private static final String SCOPE = "https://docs.google.com/feeds/"; 
  private static final Token EMPTY_TOKEN = null;
  private Map<String, Object> session;
  
  
  public String connect(){
		OAuthService service = new ServiceBuilder()
		      .provider(GoogleApi.class)
		      .apiKey("1071197260604-nseqc1eioq74g9pbf0t2tg497cgoo5mj.apps.googleusercontent.com")
		      .apiSecret("bUb2vEiLOGTz9kembph6ozjl")
		      .scope(SCOPE)
		      .callback("http://localhost:8080/Meeto/googlecallback")
		      .build();
		Scanner in = new Scanner(System.in);
		
		System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
		System.out.println();
		
		// Obtain the Request Token
		System.out.println("AAA...");
		String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
		System.out.println(authorizationUrl);	
		session.put("googleService", service);
		return SUCCESS;
		
		
		/*Token requestToken = service.getRequestToken();
		System.out.println("Got the Request Token!");
		System.out.println("(if you are curious it looks like this: " + requestToken + " )");
		System.out.println();
		
		System.out.println("Now go and authorize Scribe here:");
		System.out.println(AUTHORIZE_URL + requestToken.getToken());
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();
		
		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: " + accessToken + " )");
		System.out.println();
		
		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		request.addHeader("GData-Version", "3.0");
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getCode());
		System.out.println(response.getBody());
		
		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");*/
		
  	}

@Override
public void setSession(Map<String, Object> s) {
	// TODO Auto-generated method stub
	session = s;
}
}