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
		connection = DriverManager.getConnection(url,"root","");
		System.out.println("Connected");
		stmt = connection.createStatement();

		//addMeeting("asd", "asdad", "2014-03-03 00:00:00", "coimbra", 1);
		/*Meeting meeting = getMeeting(1);
		System.out.println(meeting.items.get(1).title);*/

		//Meetings teste = getFinishedMeetings(3);
		//System.out.println(teste.get(0).title);

		/*User user = getUser(1);

		Meeting meeting = new Meeting(5, "Reuniao y", "descricao da reuniao y", "2014-05-23 07:03:00", "DEI", user, 1);
		updateMeeting(meeting);*/
		/*Users users = new Users();
		users = getAllUsersFromMeeting(1);
		System.out.println(users.get(2).username);*/

		/*Item item = getItem(1);
		for(int i=0; i<item.comments.size(); i++){
			System.out.println(item.comments.get(i).text);
		}
		for(int i=0; i<item.keydecisions.size(); i++){
			System.out.println(item.keydecisions.get(i).description);
		}*/

		//System.out.println(item.user.username);

		/*Item item = new Item(-1, "titulo item", "descricao", getUser(1), 1);
		insertItem(item);*/

		//addUserToMeeting(getUser(5), getMeeting(1));

		//finishMeeting(getMeeting(5));

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
			return -1;
		}
	}

	public Meeting getMeeting(int idmeeting){
		Meeting meeting = null;
		User user = null;
		try
		{
			ResultSet rs = executeQuery("SELECT * FROM meeting WHERE idmeeting="+idmeeting+" AND active=1;");
			if(rs.next())
			{
				user = getUser(rs.getInt("leader"));

				if(user == null)
					return null;

				meeting = new Meeting(rs.getInt("idmeeting"), rs.getString("title"), rs.getString("description"), rs.getString("datetime"), rs.getString("location"), user, rs.getInt("active"));
				meeting.created_datetime = rs.getString("created_datetime");

				//--- Obter items ---
				rs = executeQuery("SELECT iditem, title, description, user FROM item WHERE meeting="+meeting.idmeeting+" AND active=1;");	
				while(rs.next())
				{
					user = getUser(rs.getInt("user"));

					if(user == null)
						return null;

					Item item = new Item(rs.getInt("iditem"), rs.getString("title"), rs.getString("description"), user, idmeeting);
					meeting.items.add(item);
					System.out.println("item");
				}

				//--- Obter actions ---
				rs = executeQuery("SELECT * FROM action WHERE meeting="+meeting.idmeeting+" AND active=1;");	
				while(rs.next())
				{
					System.out.println("OK");
					Action action = new Action(rs.getInt("idaction"), rs.getString("description"), rs.getString("due_to"), null, rs.getInt("done"), meeting.idmeeting, rs.getInt("active"));

					ResultSet subrs = executeQuery("SELECT iduser, username FROM user WHERE iduser="+rs.getInt("assigned_user")+" AND active=1;");
					System.out.println("OK1");
					if(subrs.next())
					{
						User user_of_action = new User(subrs.getInt("iduser"), subrs.getString("username"));
						action.assigned_user = user_of_action;
					}
					System.out.println("OK2");

					meeting.actions.add(action);
				}


			}

			meeting.users = getAllUsersFromMeeting(idmeeting);

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
		User user = null;

		try{
			ResultSet rs = executeQuery("SELECT m.* FROM meeting as m, meeting_user as mu WHERE mu.user = "+ iduser +" AND mu.meeting = m.idmeeting AND m.active = 1");
			while(rs.next())
			{
				user = getUser(rs.getInt("leader"));

				if(user == null)
					return null;

				Meeting meeting = new Meeting(rs.getInt("idmeeting"), rs.getString("title"), rs.getString("description"), rs.getString("datetime"), rs.getString("location"), user, rs.getInt("active"));

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
		User user = null;

		try{
			ResultSet rs = executeQuery("SELECT m.* FROM meeting as m, meeting_user as mu WHERE mu.user = "+ iduser +" AND mu.meeting = m.idmeeting AND m.finished = 1 AND m.active = 1");
			while(rs.next())
			{
				user = getUser(rs.getInt("leader"));

				if(user == null)
					return null;

				Meeting meeting = new Meeting(rs.getInt("idmeeting"), rs.getString("title"), rs.getString("description"), rs.getString("datetime"), rs.getString("location"), user, rs.getInt("active"));
				meetings.add(meeting);
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return meetings;
	}

	public Action getAction(int idaction){
		try{
			ResultSet rs = executeQuery("SELECT * from action where idaction=" + idaction);

			if(rs.next()){
				ResultSet srs = executeQuery("SELECT * from user where iduser=" + rs.getInt("assigned_user"));
				User u = null;
				if(srs.next()) u = new User(srs.getInt("iduser"), srs.getString("username") );
				return new Action(rs.getInt("idaction"), rs.getString("description"), rs.getTimestamp("due_to").toString(), u, rs.getInt("done"), rs.getInt("meeting"), rs.getInt("active"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public int insertMeeting(Meeting meeting) {
		try{
			String query = "INSERT INTO meeting(title, description, datetime, location, leader, created_datetime) values('" + meeting.title + "', '" + meeting.description + "', '" + meeting.datetime + "', '" + meeting.location + "', '" + meeting.leader.iduser + "', " + "NOW()" + ");";
			if(executeUpdate(query) == -1) return -1;

			ResultSet rs = executeQuery("SELECT max(idmeeting) AS m from meeting"); rs.next();
			query = "INSERT INTO meeting_user(meeting, user) values(" + rs.getInt("m") + ", " + meeting.leader.iduser + ")";
			if(executeUpdate(query) == -1) return -1;

			rs = executeQuery("select max(idmeeting) as mid from meeting;");
			if(!rs.next()) return -1;
			int mid = rs.getInt("mid");

			Users users = meeting.users;
			query = "INSERT IGNORE INTO meeting_user(user, meeting) values ";
			for(int i=0; i<users.size(); i++){
				query += "(" + users.get(i).iduser + ", " + mid + ")";
				if(i < users.size() - 1) query += ", ";
			}
			return executeUpdate(query);

		}catch(SQLException e){
			return -1;
		}

	}

	public int updateMeeting(Meeting meeting){

		String query = "UPDATE meeting SET title='" + meeting.title + "', description='" + meeting.description + "', datetime='" + meeting.datetime + "', location='" + meeting.location + "', leader='" + meeting.leader.iduser + "', finished='"+meeting.finished+"', active='" + meeting.active + "' WHERE idmeeting="+meeting.idmeeting+" AND datetime > NOW() AND active=1;";
		return executeUpdate(query);
	}

	private Users getAllUsersFromMeeting(int idmeeting){
		Users users = new Users();

		try{
			ResultSet rs = executeQuery("SELECT u.iduser, u.username FROM user as u, meeting_user as mu WHERE mu.meeting="+idmeeting+" AND u.iduser=mu.user AND u.active=1;");
			while(rs.next())
			{
				User user = new User(rs.getInt("iduser"), rs.getString("username"));
				users.add(user);
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return users;
	}

	public Users getAllUsers(){
		Users users = new Users();

		try{
			ResultSet rs = executeQuery("SELECT * FROM user where active=1;");
			while(rs.next())
			{
				User user = new User(rs.getInt("iduser"), rs.getString("username"));
				users.add(user);
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return users;
	}

	public Item getItem(int iditem){
		Item item = null;
		User user = null;
		
		try{
			ResultSet rs = executeQuery("SELECT * FROM item WHERE iditem="+iditem+" AND active = 1;");
			if(rs.next())
			{
				item = new Item(rs.getInt("iditem"), rs.getString("title"), rs.getString("description"), user, rs.getInt("meeting"));

				user = getUser(rs.getInt("user"));
				if(user == null)
					return null;

				ResultSet srs = executeQuery("SELECT com.*, u.username, u.iduser FROM comment as com, user as u WHERE com.user=u.iduser and item=" + iditem);
				while(srs.next()){
					Comment c = new Comment(srs.getInt("idcomment"), srs.getString("comment"), new User(srs.getInt("iduser"), srs.getString("username")), item, srs.getTimestamp("created_datetime").toString() );
					item.comments.add(c);
				}
				srs = executeQuery("SELECT * FROM keydecision WHERE item=" + iditem);
				while(srs.next()){
					item.keydecisions.add(new KeyDecision(srs.getInt("idkeydecision"), srs.getString("description"), srs.getInt("active"), iditem ));
				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}

		return item;
	}

	public int updateItem(Item it){
		return executeUpdate("UPDATE item set description='" + it.description + "' where iditem=" + it.iditem);
	}

	public int insertItem(Item item){
		String query = "INSERT INTO item(title, description, user, meeting, created_datetime) values('" + item.title + "', '" + item.description + "', '" + item.user.iduser + "', '" + item.meeting + "', NOW())";
		return executeUpdate(query);
	}

	public int confirmAction(Action a){
		return executeUpdate("UPDATE action set done=1 where idaction=" + a.idaction);
	}

	public User getUser(int iduser){
		User user = null;
		try{
		ResultSet subrs = executeQuery("SELECT iduser, username FROM user WHERE iduser="+iduser+" AND active=1;");
		if(subrs.next())
			user = new User(subrs.getInt("iduser"), subrs.getString("username"));
		return user;
		}
		catch(SQLException e){
			return null;
		}
	}

	public int addUserToMeeting(User user, Meeting meeting){
		String query = "INSERT INTO meeting_user (meeting, user) values((SELECT idmeeting FROM meeting WHERE idmeeting = "+meeting.idmeeting+" AND datetime > NOW() AND active = 1), (SELECT iduser FROM user WHERE  iduser = "+user.iduser+" AND active = 1));";
		return executeUpdate(query);
	}

	public int finishMeeting(Meeting meeting){
		meeting.finished = 1;
		return updateMeeting(meeting);
	}

	public int updateItem(Item it, User u){

		return 0;
	}

	public int insertComment(Comment com, User u){
		String query = "INSERT INTO comment(comment, item, user, created_datetime) values('" + com.text + "', " + com.item.iditem + ", " + u.iduser + ",  now() )";
		
		executeUpdate(query);

		return 0;
	}

	public int insertAction(Action act){
		return executeUpdate("INSERT INTO action(description, due_to, meeting, assigned_user, created_datetime) values('" + act.description + "', '" + act.due_to + "', " + act.meeting + ", " + act.assigned_user.iduser + ", now() )");
	}

	public int assignUserToAction(Action act){
		return executeUpdate("INSERT INTO action(assigned_user) values(" + act.assigned_user.iduser + ")");
	}

	public int updateAction(Action act){
		return executeUpdate("UPDATE action SET description='" + act.description + "', due_to='" + act.due_to + "', assigned_user='" + act.assigned_user.iduser + "', done='"+act.done+"' WHERE idaction="+act.idaction);
		
	}

	public int deleteAction(int idaction){
		return -1;
	}

	public int insertKeyDecision(KeyDecision kd){
		return executeUpdate("INSERT INTO keydecision(description, item, created_datetime) values('" + kd.description + "', " + kd.item + ", now() )");
	}

	public int deleteKeyDecision(int idkeydecision){
		return executeUpdate("UPDATE keydecision set active=0 where idkeydecision=" + idkeydecision);
	}

	public int updateKeyDecision(KeyDecision kd){
		return executeUpdate("UPDATE keydecision set description='" + kd.description + "' where idkeydecision=" + kd.idkeydecision);
	}

	public int inviteUsers(InviteUsers iu){
		String query = "INSERT IGNORE INTO meeting_user(user, meeting) values ";
		for(int i=0; i<iu.size(); i++){
			query += "(" + iu.get(i).user + ", " + iu.get(i).meeting + ")";
			if(i < iu.size() - 1) query += ", ";
		}
		return executeUpdate(query);
	}

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
				aut.confirm(aut.userData.iduser);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

		return aut;
	}

	public User createAccount(User u){
		ResultSet rs = executeQuery("SELECT * from user where username='" + u.username+"'");
		try{
			if(rs.next()) return u;
		}catch(SQLException e){
			return u;
		}
		
		executeUpdate("INSERT INTO user(username, password,created_datetime) values('" + u.username + "', '" + u.password + "',now())" );
		rs = executeQuery("SELECT iduser from user where username='" + u.username+"'");
		
		try{
			if(rs.next()){
				u.iduser = rs.getInt("iduser");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

		return u;
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
