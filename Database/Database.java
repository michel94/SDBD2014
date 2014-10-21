import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.sql.*;


public class Database extends UnicastRemoteObject implements DatabaseInterface{
	public Connection connection;
	public Statement stmt;
	public String host;
	public boolean banned;

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
		connection = DriverManager.getConnection(url,"root","toor");
		System.out.println("Connected");
		
		//addMeeting("asd", "asdad", "2014-03-03 00:00:00", "coimbra", 1);
		/*Meeting meeting = getMeeting(1);
		System.out.println(meeting.items.get(1).title);*/
	}

	private ResultSet executeQuery(String q){
		try{
			System.out.println(q);
			Statement st = connection.createStatement();
			return st.executeQuery(q);
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}


	public Meeting getMeeting(int idmeeting)
	{
		Meeting meeting = null;
		try
		{
			ResultSet rs = executeQuery("SELECT * FROM meeting WHERE idmeeting="+idmeeting+" AND active=1;");
			if(rs.next())
			{
				meeting = new Meeting(rs.getInt("idmeeting"), rs.getString("title"), rs.getString("datetime"), rs.getString("location"), rs.getInt("active"));
				meeting.description = rs.getString("description");
				meeting.created_datetime = rs.getString("created_datetime");

				//--- Obter user do leader ---
				rs = executeQuery("SELECT iduser, username FROM user WHERE iduser="+rs.getInt("leader")+" AND active=1;");
				if(rs.next())
				{
					User user = new User(rs.getInt("iduser"), rs.getString("username"));
					meeting.leader = user;
				}

				//--- Obter items ---
				rs = executeQuery("SELECT iditem, title, description FROM item WHERE meeting="+meeting.idmeeting+" AND active=1;");	
				while(rs.next())
				{
					Item item = new Item(rs.getInt("iditem"), rs.getString("title"));
					meeting.items.add(item);
				}

				//--- Obter actions ---
				rs = executeQuery("SELECT * FROM action WHERE meeting="+meeting.idmeeting+" AND active=1;");	
				while(rs.next())
				{
					Action action = new Action(rs.getInt("idaction"), rs.getString("due_to"), null, rs.getInt("done"), meeting, rs.getInt("active"));

					ResultSet subrs = executeQuery("SELECT iduser, username FROM user WHERE iduser="+rs.getInt("assigned_user")+" AND active=1;");
					if(subrs.next())
					{
						User user_of_action = new User(subrs.getInt("iduser"), subrs.getString("username"));
						action.assigned_user = user_of_action;
					}

					meeting.actions.add(action);
				}
			}
		}
		catch(SQLException e)
		{
			System.out.println("Mysql error");
			e.printStackTrace();
		}
		
		return meeting;
	}

@Override
	public Meetings getMeetings(){
		System.out.println("OK");
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

	public Meeting insertMeeting(Meeting m, User u) {
		try{
			String query = "insert into meeting(title, description, datetime, location, leader, created_datetime) values('" + 
				m.title + "', '" + m.description + "', '" + m.datetime + "', '" + m.location + "', '" + u.id + "', " + "now()" + ");";
			System.out.println(query);
			stmt.executeUpdate(query);
			u.password = "";
			m.leader = u;
			return m;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}

	public boolean stonith(){
		banned = true;
		return true;
	}


		private User readUser(ResultSet rs){
			User u = null;
			try{
				rs.next();
				u = new User(rs.getInt("iduser"), rs.getString("username"));
				u.password = rs.getString("password");

			}catch(SQLException e){
				e.printStackTrace();
			}
			return u;
		}

	public Authentication login(Authentication aut){
		String query = "select * from user where username='" + aut.username + "' and password='" + aut.password + "'";

		ResultSet rs = executeQuery(query);
		aut.userData = readUser(rs);
		System.out.println(aut.userData.username);

		aut.confirm(aut.userData.id);

		return aut;

	}

	public static void main(String[] args) throws RemoteException, SQLException {
		DatabaseInterface di = new Database();
		LocateRegistry.createRegistry(1099).rebind("database", di);

		
		System.out.println("Database ready...");
	}

	/*public void register(){
		try{
			host = getClientHost();
		}catch(Exception e){;}
	}*/
}
