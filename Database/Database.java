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
		stmt = connection.createStatement();

		//addMeeting("asd", "asdad", "2014-03-03 00:00:00", "coimbra", 1);
		/*Meeting meeting = getMeeting(1);
		System.out.println(meeting.items.get(1).title);*/

		Meetings teste = getFinishedMeetings(3);
		System.out.println(teste.get(0).title);
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

	private int executeUpdate(String q){
		try{
			System.out.println(q);
			Statement st = connection.createStatement();
			return st.executeUpdate(q);
		}catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}

	public Meeting getMeeting(int idmeeting){
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
					System.out.println("item");
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
			return null;
		}
		
		return meeting;
	}

	public Meetings getMeetings(int iduser){
		Meetings meetings = new Meetings();

		try{
			ResultSet rs = executeQuery("SELECT m.* FROM meeting as m, meeting_user as mu WHERE mu.user = "+ iduser +" AND mu.meeting = m.idmeeting AND m.active = 1");
			while(rs.next())
			{
				Meeting meeting = new Meeting(rs.getInt("idmeeting"), rs.getString("title"), rs.getString("datetime"), rs.getString("location"), rs.getInt("active"));
				meetings.add(meeting);
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return meetings;
	}

	public Meetings getFinishedMeetings(int iduser){
		Meetings meetings = new Meetings();

		try{
			ResultSet rs = executeQuery("SELECT m.* FROM meeting as m, meeting_user as mu WHERE mu.user = "+ iduser +" AND mu.meeting = m.idmeeting AND m.datetime < NOW() AND m.active = 1");
			while(rs.next())
			{
				Meeting meeting = new Meeting(rs.getInt("idmeeting"), rs.getString("title"), rs.getString("datetime"), rs.getString("location"), rs.getInt("active"));
				meetings.add(meeting);
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return meetings;
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

	public Meeting updateMeeting(Meeting m, User u){


		return m;
	}

	public Item insertItem(Item it, User u){

		return it;
	}

	public Item updateItem(Item it, User u){

		return it;
	}

	public Comment insertComment(Comment com, User u){
		String query = "INSERT INTO comment(comment, item, user, created_datetime) values('" + com.text + "', " + com.item.id + ", " + u.id + ",  now() )";
		
		executeUpdate(query);

		return com;
	}

	public Item getItem(int id){
		Item it = null;
		
		try{
			ResultSet rs = executeQuery("SELECT * FROM item where iditem=" + id);
			if(rs.next())
				it = new Item(rs.getInt("iditem"), rs.getString("title"), rs.getString("description"), rs.getInt("user"), rs.getInt("meeting"));
			
		}catch(SQLException e){
			e.printStackTrace();
		}

		return it;
	}

	/*public Comment updateComment(Comment com, User u){
		
	}*/

	public boolean stonith(){
		banned = true;
		return true;
	}


	private User readUser(ResultSet rs){
		User u = null;
		
		try{
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

		try{
			if(rs.next()){
				aut.userData = readUser(rs);
				System.out.println(aut.userData.username);
				aut.confirm(aut.userData.id);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

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
