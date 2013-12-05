package login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import quizsite.DatabaseUtils;

public class Announcement {

	private int id;
	private String username;
	private String content;
	private long time;
	
	public int getId() {
		return id;
	}
	
	public String getUserName() {
		return username;
	}
	
	public String getContent() {
		return content;
	}
	
	public long getTime() {
		return time;
	}
	
	public Announcement(int id, String username, String content, long time) {
		this.id = id;
		this.username = username;
		this.content = content;
		this.time = time;
	}
	
	public static ArrayList<Announcement> getAllAnnouncements() {
		ArrayList<Announcement> announcementList = new ArrayList<Announcement>();
		String query = DatabaseUtils.constructSelectQuery(new String[]{"*"}, new String[]{"QuizAnnouncements"}, new String[]{});
		ResultSet rs = DBConnection.getResult(query);
		try {
			while (rs.next() ) {
				int id = Integer.parseInt(rs.getString("id"));
				String user = rs.getString("username");
				String content = rs.getString("content");
				long time = Long.parseLong( rs.getString("time") );
				
				Announcement ann = new Announcement(id, user, content, time);
				announcementList.add(ann);

			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return announcementList;
	}
	public static int createAnnouncement(String username, String content) {
		if (!AccountManager.isAdmin(username)) {
			return -1;
		}
//		String query = DatabaseUtils.constructInsertQuery("QuizAnnouncements", new String[] {null, username, content, Long.toString( System.currentTimeMillis() )});
		String query = "insert into QuizAnnouncements values(null, '"+username+"','"+content+"',"+Long.toString( System.currentTimeMillis() )+");";
		DBConnection.updateTable(query);
		return 0;
	} 
	
	public static int removeAnnouncement(String username, int id) {
		if (!AccountManager.isAdmin(username)) {
			return -1;
		}
		String query = DatabaseUtils.constructDeleteQuery("QuizAnnouncements", new String[] {"id = " + id});
		DBConnection.updateTable(query);
		return 0;
	}
}
