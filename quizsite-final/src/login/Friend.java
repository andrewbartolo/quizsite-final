package login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import quizsite.DatabaseUtils;

public class Friend {

	private String username;
	
	public Friend(String username) {
		this.username= username;
	}
	
	public String getUserName() {
		return username;
	}
	
	public static ArrayList<Friend> getFriendList(String username) {
		ArrayList<Friend> friends = new ArrayList<Friend>();
		String query = "SELECT user2 FROM QuizFriendship WHERE user1 = '"+ username +"';";
		ResultSet rs = DBConnection.getResult(query);
		try {
			while (rs.next() ) {
				friends.add( new Friend(rs.getString("user2")) );
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return friends;
	}
	
	public static int addFriends(String user1, String user2) {
		String query1 = DatabaseUtils.constructInsertQuery("QuizFriendship", new String[]{user1, user2});
		String query2 = DatabaseUtils.constructInsertQuery("QuizFriendship", new String[]{user2, user1});

		DBConnection.updateTable(query1);
		DBConnection.updateTable(query2);
		
		return 0;

	}
	
	public static int removeFriend(String user1, String user2) {
		
		String query1 = "delete from QuizFriendship where user1 = '" + user1 + "' and user2 = '" + user2 + "' ;";
		String query2 = "delete from QuizFriendship where user1 = '" + user2 + "' and user2 = '" + user1 + "' ;";
		
		DBConnection.updateTable(query1);
		DBConnection.updateTable(query2);
		
		return 0;
	}
}
