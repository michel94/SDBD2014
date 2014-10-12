import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.sql.*;

public class Database extends UnicastRemoteObject implements DatabaseInterface{
	public Connection connection;

	protected Database() throws RemoteException, SQLException {
		super();
		//initialize JDBC
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Database started");
        String url = "jdbc:mysql://localhost:3306/test";
        connection = DriverManager.getConnection(url,"root","");
        System.out.println("Connected");
	}

	public ArrayList<String> getMeetings(){

		return new ArrayList<String>();
	}
	public static void main(String[] args) throws RemoteException, SQLException {
		DatabaseInterface di = new Database();
		LocateRegistry.createRegistry(1099).rebind("database", di);
		System.out.println("Database ready...");
	}
}