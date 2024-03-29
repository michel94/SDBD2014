package meeto.garbage;

import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.sql.*;
import java.net.*;
import java.util.Properties;
import java.util.Enumeration;

public class Database extends UnicastRemoteObject implements DatabaseInterface{
	public Connection connection;
	public Statement stmt;
	public String host;
	public boolean banned;
	
	static int RMIPort;

	protected Database() throws RemoteException, SQLException {
		
		super();
		//initialize JDBC
		setConfigs();
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
		
		
	}

	public void setConfigs(){
		try{
			Properties prop = new Properties();
			FileInputStream txt = new FileInputStream("DatabaseServer.properties");
			prop.load(txt);
			RMIPort = Integer.parseInt(prop.getProperty("rmiport"));
			System.out.println("RMI port " + RMIPort);

		}catch(IOException e){
			System.out.println("Could not load configuration. Exiting.");
			System.exit(0);
		}
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
		try{
			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = st.executeQuery("SELECT * FROM meeting WHERE idmeeting="+idmeeting+" AND active=1;");
			
			if(rs.next())
			{
				user = getUser(rs.getInt("leader"));

				if(user == null)
					return null;

				meeting = new Meeting(rs.getInt("idmeeting"), rs.getString("title"), rs.getString("description"), rs.getString("datetime"), rs.getString("location"), user, rs.getInt("active"));
				meeting.created_datetime = rs.getString("created_datetime");

				//--- Obter items ---
				rs = st.executeQuery("SELECT iditem, title, description, user FROM item WHERE meeting="+meeting.idmeeting+" AND active=1;");	
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
				rs = st.executeQuery("SELECT * FROM action WHERE meeting="+meeting.idmeeting+" AND active=1;");	
				while(rs.next())
				{
					System.out.println("OK");
					Action action = new Action(rs.getInt("idaction"), rs.getString("description"), rs.getString("due_to"), null, rs.getInt("done"), meeting.idmeeting, rs.getInt("active"));

					ResultSet subrs = executeQuery("SELECT iduser, username FROM user WHERE iduser="+rs.getInt("assigned_user")+" AND active=1;");
					if(subrs.next())
					{
						User user_of_action = new User(subrs.getInt("iduser"), subrs.getString("username"));
						action.assigned_user = user_of_action;
					}

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
			ResultSet rs = executeQuery("SELECT m.* FROM meeting as m, meeting_user as mu WHERE mu.user = "+ iduser +" AND mu.meeting = m.idmeeting AND m.active = 1 ORDER BY datetime ASC");
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
			connection.setAutoCommit(false);
			System.out.println("insert meeting");
			Statement st = connection.createStatement();
			String query = "INSERT INTO meeting(title, description, datetime, location, leader, created_datetime) values('" + meeting.title + "', '" + meeting.description + "', '" + meeting.datetime + "', '" + meeting.location + "', '" + meeting.leader.iduser + "', " + "NOW()" + ");";
			st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			connection.commit();
			ResultSet rsId = st.getGeneratedKeys();
			rsId.next();
			int mid = rsId.getInt(1);
			System.out.println("Meeting id: " + mid);
			
			query = "INSERT INTO meeting_user(meeting, user) values(" + mid + ", " + meeting.leader.iduser + ")";
			System.out.println(query);
			st.executeUpdate(query);
			connection.commit();
			
			Users users = meeting.users;
			query = "INSERT IGNORE INTO meeting_user(user, meeting) values ";
			for(int i=0; i<users.size(); i++){
				System.out.println("create meeting");
				query += "(" + users.get(i).iduser + ", " + mid + ")";
				if(i < users.size() - 1) query += ", ";
			}
			System.out.println(query);
			st.executeUpdate(query);
			connection.commit();
			connection.setAutoCommit(true);
			return 1;

		}catch(SQLException e){
			try{
				e.printStackTrace();
				connection.rollback();
				connection.setAutoCommit(true);
			}catch(SQLException e1){}
		}
		
		return -1;
	}

	public int updateMeeting(Meeting meeting){
		try {
			connection.setAutoCommit(false);
			Statement st = connection.createStatement();
			String query1 = "SELECT idmeeting from meeting where idmeeting=" + meeting.idmeeting + " for update;";
			ResultSet rs = st.executeQuery(query1);
			if(rs.next()){
				String query2 = "UPDATE meeting SET title='" + meeting.title + "', description='" + meeting.description + "', datetime='" + meeting.datetime + "', location='" + meeting.location + "', leader='" + meeting.leader.iduser + "', finished='"+meeting.finished+"', active='" + meeting.active + "' WHERE idmeeting="+meeting.idmeeting+" AND datetime > NOW() AND active=1;";
				st.executeUpdate(query2);
				connection.commit();
				connection.setAutoCommit(true);
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
		
	}

	/* int deleteMeeting(int idmeeting){ INACABADO****************
		executeUpdate("UPDATE comment set active=0 where item=(SELECT )");
		executeUpdate("UPDATE meeting set active=0 where idmeeting=" + idmeeting);
		executeUpdate("UPDATE meeting set active=0 where idmeeting=" + idmeeting);
		executeUpdate("UPDATE meeting set active=0 where idmeeting=" + idmeeting);
	}*/

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
			Statement st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = st.executeQuery("SELECT * FROM item WHERE iditem="+iditem+" AND active = 1;");
			if(rs.next())
			{
				item = new Item(rs.getInt("iditem"), rs.getString("title"), rs.getString("description"), user, rs.getInt("meeting"));

				user = getUser(rs.getInt("user"));
				if(user == null)
					return null;

				ResultSet srs = st.executeQuery("SELECT com.*, u.username, u.iduser FROM comment as com, user as u WHERE com.user=u.iduser and item=" + iditem);
				while(srs.next()){
					Comment c = new Comment(srs.getInt("idcomment"), srs.getString("comment"), new User(srs.getInt("iduser"), srs.getString("username")), item, srs.getTimestamp("created_datetime").toString() );
					item.comments.add(c);
				}
				srs = st.executeQuery("SELECT * FROM keydecision WHERE item=" + iditem);
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
		try {
			connection.setAutoCommit(false);
			Statement st = connection.createStatement();
			ResultSet rs =  st.executeQuery("select iditem from item where iditem=" + it.iditem + " for update");
			if(rs.next()){
				st.executeUpdate("UPDATE item set title='" + it.title + "', description='" + it.description + "' where iditem=" + it.iditem);
				connection.commit();
				connection.setAutoCommit(true);
				return 1;
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {}
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {}
		return -1;
	}

	public int deleteItem(int iditem){
		try {
			connection.setAutoCommit(false);
			Statement st = connection.createStatement();
			ResultSet rs =  st.executeQuery("select iditem from item where iditem=" + iditem + " for update");
			if(rs.next()){
				st.executeUpdate("UPDATE item set active=0 where iditem=" + iditem);
				connection.commit();
				connection.setAutoCommit(true);
				return 1;
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {}
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {}
		return -1;
	}

	public int insertItem(Item item){
		try {
			connection.setAutoCommit(false);
			Statement st = connection.createStatement();
			ResultSet rs =  st.executeQuery("select idmeeting from meeting where idmeeting=" + item.meeting + " for update");
			if(rs.next()){
				st.executeUpdate("INSERT INTO item(title, description, user, meeting, created_datetime) values('" + item.title + "', '" + item.description + "', '" + item.user.iduser + "', '" + item.meeting + "', NOW())");
				connection.commit();
				connection.setAutoCommit(true);
				return 1;
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {}
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {}
		return -1;
		
	}

	public int confirmAction(Action a){
		try {
			connection.setAutoCommit(false);
			Statement st = connection.createStatement();
			ResultSet rs =  st.executeQuery("select idaction from action where idaction=" + a.idaction + " for update");
			if(rs.next()){
				st.executeUpdate("UPDATE action set done=1 where idaction=" + a.idaction);
				connection.commit();
				connection.setAutoCommit(true);
				return 1;
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {}
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {}
		return -1;
		
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

	public int addUserToMeeting(User user, Meeting meeting, Group group){
		String query = null;

		if(group == null)
		{
			query = "INSERT IGNORE INTO meeting_user (meeting, user) VALUES((SELECT idmeeting FROM meeting WHERE idmeeting = "+meeting.idmeeting+" AND datetime > NOW() AND active = 1), (SELECT iduser FROM user WHERE  iduser = "+user.iduser+" AND active = 1));";
		}
		else
		{
			query = "INSERT IGNORE INTO meeting_user (meeting, user, group_def) VALUES((SELECT idmeeting FROM meeting WHERE idmeeting = "+meeting.idmeeting+" AND datetime > NOW() AND active = 1), (SELECT iduser FROM user WHERE  iduser = "+user.iduser+" AND active = 1), "+group.idgroup+");";
		}
		
		return executeUpdate(query);
	}

	/*public int addGroupToMeeting(int idgroup, int idmeeting){
		String query = "INSERT IGNORE INTO meeting_group (meeting, group_def) values((SELECT idmeeting FROM meeting WHERE idmeeting = "+meeting.idmeeting+" AND datetime > NOW() AND active = 1), (SELECT idgroup FROM group_def WHERE  idgroup = "+group.idgroup+" AND active = 1));";
		int i = 0;

		if(executeUpdate(query) >= 0)
		{
			for(i=0; i<group.users.size(); i++)
			{
				if(addUserToMeeting(group.users.get(i), meeting, null) < 1)
					return -1;
			}
		}

		return 1;
	}*/


	public int finishMeeting(Meeting meeting){
		try {
			connection.setAutoCommit(false);
			Statement st = connection.createStatement();
			ResultSet rs =  st.executeQuery("select idmeeting from meeting where idmeeting=" + meeting.idmeeting + " for update");
			if(rs.next()){
				connection.commit();
				connection.setAutoCommit(true);
				meeting.finished = 1;
				return updateMeeting(meeting);
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {}
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {}
		return -1;
		
	}

	public int insertComment(Comment com, User u){
		try {
			connection.setAutoCommit(false);
			Statement st = connection.createStatement();

				st.executeUpdate("INSERT INTO comment(comment, item, user, created_datetime) values('" + com.text + "', " + com.item.iditem + ", " + u.iduser + ",  now() )");
				connection.commit();
				connection.setAutoCommit(true);
				return 1;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {}
		}
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {}
		return -1;
		
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
		return executeUpdate("UPDATE action SET active=0 WHERE idaction="+idaction);
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

	public int inviteUsersToMeeting(InviteUsers iu){
		String query = "SELECT idmeeting from meeting where idmeeting=" + iu.get(0).id + " FOR UPDATE";
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			if(rs.next()){
				query = "INSERT IGNORE INTO meeting_user(user, meeting) values ";
				for(int i=0; i<iu.size(); i++){
					query += "(" + iu.get(i).user + ", " + iu.get(i).id + ")";
					if(i < iu.size() - 1) query += ", ";
				}
				st.executeUpdate(query);
			}
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		 
	}

	public int inviteUsersToGroup(InviteUsers iu)
	{
		String query = "INSERT IGNORE INTO group_user(group_def, user) values ";
		for(int i=0; i<iu.size(); i++){
			query += "(" + iu.get(i).id + ", " + iu.get(i).user+ ")"; 
			if(i < iu.size() - 1) query += ", ";
		}
		if(executeUpdate(query) < 0)
			return -1;

		String query2;
		try{
			ResultSet rs = executeQuery("SELECT * FROM meeting_group WHERE group_def = "+iu.get(0).id+";");
			if(rs.next())
			{
				query2 = "INSERT IGNORE INTO meeting_user(meeting, user, group_def) values ";
				
				do{
					query2 += "(" + rs.getInt("meeting") + ", " + iu.get(0).user+ ", "+iu.get(0).id+")      , "; 
				}
				while((rs.next()));

				query2 = query2.substring(0, query2.length()-4);
				return executeUpdate(query2);
			}
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}

		return -1;
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
		
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			if(rs.next()){
				aut.userData = new User(rs.getInt("iduser"), rs.getString("username"));
				aut.confirm(aut.userData.iduser);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return aut;
	}

	public User createAccount(User u){
		Statement st;
		try {
			connection.setAutoCommit(false);
			st = connection.createStatement();
			st.execute("lock table user write");
			ResultSet rs = st.executeQuery("SELECT * from user where username='" + u.username+"'");
			if(rs.next()){
				st.execute("unlock tables");
				return u;
			}
			st.executeUpdate("INSERT INTO user(username, password,created_datetime) values('" + u.username + "', '" + u.password + "',now())" );
			connection.commit();
			stmt.execute("unlock tables");
			
			rs = executeQuery("SELECT iduser from user where username='" + u.username+"'");
			if(rs.next()){
				u.iduser = rs.getInt("iduser");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try{
				connection.rollback();
				e.printStackTrace();
			}catch(SQLException e1){
				e.printStackTrace();
			}
		}
		
		try {
			connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return u;
	}

	public Groups getGroupsOfUser(int iduser){
		Groups groups = new Groups();

		try{
			ResultSet rs = executeQuery("SELECT g.* FROM group_def as g, group_user as gu WHERE gu.user = "+ iduser +" AND gu.group_def = g.idgroup AND g.active = 1");
			while(rs.next())
			{
				Group group = new Group(rs.getInt("idgroup"), rs.getString("name"));
				groups.add(group);
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		
		return groups;
	}

	public Group getGroup(int idgroup){
		Group group = new Group();
		try
		{
			ResultSet rs = executeQuery("SELECT * FROM group_def WHERE idgroup="+idgroup+" AND active=1;");
			if(rs.next())
			{
				group.idgroup = rs.getInt("idgroup");
				group.name = rs.getString("name");
				rs = executeQuery("SELECT u.iduser, u.username, gu.user FROM user as u, group_user as gu WHERE gu.group_def="+idgroup+" AND gu.user = u.iduser AND u.active = 1;");	
				while(rs.next())
				{
					User user = new User(rs.getInt("iduser"), rs.getString("username"));

					if(user == null)
						return null;

					group.users.add(user);
				}
			}
		}
		catch(SQLException e)
		{
			System.out.println("Mysql error");
			e.printStackTrace();
			return null;
		}
		
		return group;
	}

	public int leaveMeeting(int idmeeting, int iduser)
	{
		return executeUpdate("DELETE FROM meeting_user WHERE meeting = "+idmeeting+" AND user = "+iduser+";");	
	}
	
	public int removeUserFromGroup(RemoveUserFromGroup ru){
		int idgroup = ru.idgroup;
		int iduser = ru.iduser;

		if(executeUpdate("DELETE FROM group_user WHERE user = "+iduser+" AND group_def = "+idgroup+";" ) < 0)
			return -1;


		return 1;
	}

	public Actions getUserActions(int iduser){
		Action action = null;
		Actions actions = new Actions();
		try{
			ResultSet rs =executeQuery("SELECT * FROM action WHERE assigned_user = "+iduser+" AND active = 1;");	
			while(rs.next())
			{
				action = new Action(rs.getInt("idaction"), rs.getString("description"), rs.getString("due_to"), getUser(rs.getInt("assigned_user")), rs.getInt("done"), rs.getInt("meeting"), rs.getInt("active"));

				if(action == null)
					return null;

				actions.add(action);
			}
		}
		catch(SQLException e)
		{
			System.out.println("Mysql error");
			e.printStackTrace();
			return null;
		}

		return actions;
	}
	

	public int addGroupToMeeting(int idmeeting, int idgroup){
		
		if(executeUpdate("INSERT IGNORE INTO meeting_group(meeting, group_def) values(" + idmeeting + ", " + idgroup + ");" ) < 0)
			return -1;

		Group group = getGroup(idgroup);
		Meeting meeting = getMeeting(idmeeting);

		int i = 0;
		for(i=0; i<group.users.size(); i++)
		{
			if(addUserToMeeting(group.users.get(i), meeting, group) < 0)
				return -1;
		}

		return i+1;

	}

	public int createGroup(Group group){
		
		if(executeUpdate("INSERT INTO group_def(name, created_datetime, active) values('" + group.name + "', NOW(), 1);" ) < 1)
			return -1;

		int i = 0;
		String query;
		try{
			ResultSet rs = executeQuery("SELECT max(idgroup) as idgroup from group_def");
			if(rs.next())
			{
				System.out.println("groupg ggggggggggggggggggggggg");
				group.idgroup = rs.getInt("idgroup");

				query = "INSERT IGNORE INTO group_user(group_def, user) values ";
				for(i=0; i<group.users.size(); i++){
					query += "(" + group.idgroup + ", " + group.users.get(i).iduser+ ")"; 
					if(i < group.users.size() - 1) query += ", ";
				}
				return executeUpdate(query);
			}
			else
			{
				return -1;
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}

	}

	/*public Actions getActionsByUser(int iduser)
	{
		Action action = null;
		Actions actions = new Actions();
		try{
			ResultSet rs =executeQuery("SELECT * FROM action WHERE assigned_user = "+iduser+" AND active = 1;");	
			while(rs.next())
			{
				action = new Action(rs.getInt("idaction"), rs.getString("description"), rs.getString("due_to"), getUser(rs.getInt("assigned_user")), rs.getInt("done"), rs.getInt("meeting"), rs.getInt("active"));

				if(action == null)
					return null;

				actions.add(action);
			}
		}
		catch(SQLException e)
		{
			System.out.println("Mysql error");
			e.printStackTrace();
			return null;
		}

		return actions;
	}*/
	
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

	public static void main(String[] args) throws RemoteException, SQLException {

		try{
			String ipAddress = getIpAddress();
			System.out.println("Database server ip: "+ipAddress);
			if (ipAddress != null)
				System.setProperty("java.rmi.server.hostname", ipAddress );
		}catch(Exception e){
			System.out.print(e);
		}

		DatabaseInterface di = new Database();
		Registry r = LocateRegistry.createRegistry(RMIPort);
		r.rebind("database", di);
		
		System.out.println("Database ready...");
	}

	/*public void register(){
		try{
			host = getClientHost();
		}catch(Exception e){;}
	}*/
}
