package login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Message {
	//constants for the message type
	public static final int MESSAGE_FRIEND_REQUEST = 1;
	public static final int MESSAGE_CHALLENGE = 2;
	public static final int MESSAGE_NOTE = 3;
	public static final int MESSAGE_FRIEND_ACCEPT = 4;
	public static final int MESSAGE_FRIEND_REJECT = 5;
	public static final int MESSAGE_FRIEND_REMOVED = 6;

	private String fromuser;
	private String touser;
	private int type;
	private String content;
	private boolean isread;
	
	public Message(String fromuser, String touser, int type, String content, boolean isread) {
		this.fromuser = fromuser;
		this.touser = touser;
		this.type = type;
		this.content = content;
		this.isread = isread;
	}
	
	//getters
	public String getFromUser() {
		return fromuser;
	}
	
	public String getToUser() {
		return touser;
	}
	
	public int getType() {
		return type;
	}
	
	public String getContent() {
		return content;
	}
	
	public boolean getIsRead() {
		return isread;
	}
	
	
	public static int sendMessage(Message msg) {
		String fromuser = msg.getFromUser();
		String touser = msg.getToUser();
		String type = Integer.toString( msg.getType() );
		String content = msg.getContent();
		
		String query = "INSERT INTO QuizMessage VALUES ('" + fromuser + "','" + touser + "',"+ type 
						+ ",'" + content + "', false);";
		int updateResult = DBConnection.updateTable(query);
		
		return updateResult;
		
	}
	
	
	public static ArrayList<Message> getReceivedMessages(String username) {
		ArrayList<Message> msgList = new ArrayList<Message>();
		
		String query = "SELECT * FROM QuizMessage WHERE touser = '"+ username +"';";
		String query2 = "UPDATE QuizMessage SET isread = true WHERE touser = '" + username +"';";
		DBConnection.updateTable(query2);
		ResultSet rs = DBConnection.getResult(query);
		try {
			while (rs.next() ) {
				String fromuser = rs.getString("fromuser");
				String touser = rs.getString("touser");
				int type = Integer.parseInt( rs.getString("type") );
				String content = rs.getString("content");
				boolean isread = rs.getString("isread").equals("1");
				
				Message msg = new Message(fromuser, touser, type, content, isread);
				msgList.add(msg);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msgList;
	}
	
	public static ArrayList<Message> getUnreadMessages(String username) {
		ArrayList<Message> msgList = new ArrayList<Message>();
		
		String query = "SELECT * FROM QuizMessage WHERE touser = '"+ username +"' AND isread = false;";
		String query2 = "UPDATE QuizMessage SET isread = true WHERE touser = '" + username +"';";
		DBConnection.updateTable(query2);
		ResultSet rs = DBConnection.getResult(query);
		try {
			while (rs.next() ) {
				String fromuser = rs.getString("fromuser");
				String touser = rs.getString("touser");
				int type = Integer.parseInt( rs.getString("type") );
				String content = rs.getString("content");
				boolean isread = rs.getString("isread").equals("1");
				
				Message msg = new Message(fromuser, touser, type, content, isread);
				msgList.add(msg);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msgList;
	}
	
	public static ArrayList<Message> getSentMessages(String username) {
		ArrayList<Message> msgList = new ArrayList<Message>();
		
		String query = "SELECT * FROM QuizMessage WHERE fromuser = '"+ username +"';";
		ResultSet rs = DBConnection.getResult(query);
		try {
			while (rs.next() ) {
				String fromuser = rs.getString("fromuser");
				String touser = rs.getString("touser");
				int type = Integer.parseInt( rs.getString("type") );
				String content = rs.getString("content");
				boolean isread = rs.getString("isread").equals("1");
				
				Message msg = new Message(fromuser, touser, type, content, isread);
				msgList.add(msg);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msgList;
	}
	
	public String getMessageType() {
		switch (type) {
			case 1: return "Friend Request";
			case 2: return "Challenge";
			case 3: return "Note";
			case 4: return "Friend Request Accepted";
			case 5: return "Friend Request Rejected";
			case 6: return "Friend Removed";
			default: return "unrecognized type";
		}
	}
	
}
