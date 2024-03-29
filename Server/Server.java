import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.sql.Date;
import java.util.*;
import java.lang.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Properties;

public class Server{
	private int clientNumber = 1;
	private int serverPort;
	private Boolean second;
	private DatabaseInterface database = null;
	private ConcurrentHashMap<Integer, ClientData> clients;
	private String failoverserver,databaseIP,databasePort;

	protected Server(boolean s){
		super();

		setConfigs();
		second = s;
		clients = new ConcurrentHashMap<Integer, ClientData>();
		UdpConnection u = new UdpConnection(second,failoverserver);
		
		

		if(second){
			try{
				System.out.println("Waiting");
				synchronized(second){
					second.wait();
				}
				System.out.println("Ended Waiting");
			}catch(InterruptedException e){e.printStackTrace();}
		}
		connectDatabase();
		clientListener();
	}

	public void setConfigs(){
		try{
			Properties prop = new Properties();
			FileInputStream txt = new FileInputStream("Server.properties");
			prop.load(txt);
			serverPort = Integer.parseInt(prop.getProperty("tcpport"));
			failoverserver = prop.getProperty("failoverserverip");
			databaseIP = prop.getProperty("databaseserverip");
			databasePort = prop.getProperty("databaseserverport");

		}catch(IOException e){
			System.out.println("Could not load configuration. Exiting.");
			System.exit(0);
		}
	}

	public void connectDatabase(){
		int retries = 0;
		System.out.println("connecting to: " + "//"+databaseIP+":"+databasePort+"/database");

		try{
			while(retries<8){
				try{
				 	database = (DatabaseInterface) Naming.lookup("//"+databaseIP+":"+databasePort+"/database");
				 	System.out.println("Connected to the database");
					break;
				}catch(NotBoundException e){
					System.out.println("Database could not be reached. Failed connection. Retrying connection in 10 seconds.");
					retries++;
					Thread.sleep(2000);
				}catch(MalformedURLException e){
					System.out.println("Could not find the the database hostname. Retrying connection in 10 seconds.");
					retries++;
					Thread.sleep(2000);		
				}catch(RemoteException e){
					System.out.println("Remote exception. Retrying connection in 10 seconds.");
					retries++;
					Thread.sleep(2000);
				}
			}

		}catch(InterruptedException ex) {
    			Thread.currentThread().interrupt();
		}

		if(retries == 8){
			System.out.println("Database could not be reached after 1 minute. Closing server.");
			System.exit(0);
		}
		System.out.println("Connection to database established.");
	}
	
	public void clientListener(){
		ObjectOutputStream opipeout;
		ObjectInputStream opipein;
		Socket clientSocket;
		
		try{

			System.out.println("Listening to port " + serverPort);
			ServerSocket listenSocket = new ServerSocket(serverPort);
			System.out.println("Server ready");

			InetAddress IP=InetAddress.getLocalHost();
			System.out.println("IP of this server is : "+ getIpAddress());

			while(true){
				clientSocket = listenSocket.accept();

				clients.put(clientNumber, new ClientData(null, null));
				new ClientThread(clientSocket, clientNumber, database, clients, failoverserver,databaseIP,databasePort);

				clientNumber++;

				//debugClients();
				
				
			}
		}catch(IOException e)
		
		{
			System.out.println("Listen:" + e.getMessage());
		}

	}

	private void debugClients(){
		ClientData cd;
		Iterator it = clients.keySet().iterator();

		System.out.println("Debug Clients");
		while(it.hasNext()){
			Integer key = (Integer) it.next();
			System.out.println("key: " + key);
			/*cd = clients.get(key);
			System.out.println(cd.userData.username);*/
		}
	}
	
	public static String getIpAddress() {
		try {
			for (Enumeration enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements();) {
				NetworkInterface netinterface = (NetworkInterface) enumeration.nextElement();
				for (Enumeration enumerationIpAddress = netinterface.getInetAddresses(); enumerationIpAddress.hasMoreElements();) {
					InetAddress inetAddress = (InetAddress) enumerationIpAddress.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
						return inetAddress.getHostAddress().toString();
				}
			}
		} catch (Exception e) { }
		return null;
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {

		Server server = new Server(true);

	}

}
