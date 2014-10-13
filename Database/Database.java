import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.sql.*;


public class Database extends UnicastRemoteObject implements DatabaseInterface{
	public Connection connection;
	public Statement stmt;

	protected Database() throws RemoteException, SQLException {
		super();
		//initialize JDBC
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Found Driver");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:mysql://localhost:3306/meeto";
        connection = DriverManager.getConnection(url,"root","");
        stmt = connection.createStatement();
        System.out.println("Connected");
	}

	private ResultSet executeQuery(String q){
		try{
			System.out.println(q);
			return stmt.executeQuery(q);
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<Meeting> getMeetings(){
		ArrayList<Meeting> res = new ArrayList<Meeting>();
		
		ResultSet rs = executeQuery("SELECT idmeeting, title FROM meeting");
		try{
			while(rs.next()){
				Meeting m = new Meeting(rs.getString("title"), rs.getInt("idmeeting"));
				res.add(m);
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return res;
	}
	public boolean addMeeting(String title, String description, Date t, String location, int leader) {
		try{
			String query = " insert into meeting(title, description, datetime, location, leader, created_datetime) values(\"" +
				title + "\", \"" + description + "\", \"" + t + "\", \"" + location + "\", \"" + leader + "\", " + "now()" + ");";
			System.out.println(query);
			stmt.executeUpdate(query);
		}catch(SQLException e){
			return false;
		}
		return true;

		
	}

	public static void main(String[] args) throws RemoteException, SQLException {
		DatabaseInterface di = new Database();
		LocateRegistry.createRegistry(1099).rebind("database", di);


		System.out.println("Database ready...");
	}
}
