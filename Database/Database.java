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
	public Meetings getMeetings(){
		Meetings res = new Meetings();
		
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

	public Meeting getMeeting(Request r){
		Meeting m = new Meeting();

		try{
			ResultSet rs = executeQuery("select idmeeting,title from meeting where idmeeting=" + r.id + ";");
			m.id = rs.getInt("idmeeting");
			m.title = rs.getString("title");
			System.out.println("title: " + m.title + " id: " + m.id);

			rs = executeQuery("select title from item where user=1;");
			while(rs.next()){
				m.items.add(new Item(rs.getString("title"), rs.getInt("id")) );
			}
		}catch(SQLException e){

		}
		
		return m;
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
