package login;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import quizsite.DatabaseUtils;
import quizsite.Quiz;


public class User {
	
	private String username;
	
	public User(String username) {
		this.username = username;
	}
	
	public String getUserName() {
		return username;
	}
	
	public boolean isAdmin() {
		return AccountManager.isAdmin(username);
	}
	
	public void makeAdmin() {
		AccountManager.setAdmin(username, 1);
	}
	
	public void demoteAdmin() {
		AccountManager.setAdmin(username, 0);
	}

	public ArrayList<Friend> getFriends() {
		return Friend.getFriendList(username);
	}
	
	public boolean isFriendWith(String user) {
		for (Friend f : Friend.getFriendList(username)) {
			if (f.getUserName().equals(user)) {
				return true;
			}
		}
		
		return false;
	}
	
	public int addFriend(String touser, String content) {
		return sendMessage(username, touser, Message.MESSAGE_FRIEND_REQUEST, "Friend request from " + username + "\n" + content);
	}
	
	public int acceptFriend(String touser, String content) {
		Friend.addFriends(username, touser);
		return sendMessage(username, touser, Message.MESSAGE_FRIEND_ACCEPT, username +" accepted your friend request");
	}
	
	public int removeFriend(String touser) {
		Friend.removeFriend(username, touser);
		return sendMessage(username, touser, Message.MESSAGE_FRIEND_REMOVED, "You are no longer friends with " + username);
	}
	
	public int sendMessage(String fromuser, String touser, int type, String content) {
		Message msg = new Message(fromuser, touser, type, content, false);
		return Message.sendMessage(msg);
	}
	
	public ArrayList<Message> getReceivedMessages() {
		return Message.getReceivedMessages(username);
	}
	
	public ArrayList<Message> getUnreadMessages() {
		return Message.getUnreadMessages(username);
	}
	
	public ArrayList<Message> getSentMessages() {
		return Message.getSentMessages(username);
	}
	
	public ArrayList<History> getTakenQuizzes() {
		return History.getTakenQuizzes(username);
	}
	
	public ArrayList<History> getCreatedQuizzes() {
		return History.getCreatedQuizzes(username);
	}
	
	public ArrayList<Achievement> getAllAchievements() {
		return Achievement.getAllAchievements(username);
	}
	
	public ArrayList<Announcement> getAllAnnouncements() {
		return Announcement.getAllAnnouncements();
	}
	
	//Administrator Methods. Return -1 if not admin.
	public int createAnnouncement(String content) {
		return Announcement.createAnnouncement(username, content);
	}
	
	public int removeAnnouncement(int announcementId) {
		return Announcement.removeAnnouncement(username, announcementId);
	}
	
	public int removeUser(String userToRemove) {
		if (!isAdmin()) {
			return -1;
		}
		
		String query = DatabaseUtils.constructDeleteQuery("QuizUser", new String[] {"username = '" + userToRemove +"';"});
		DBConnection.updateTable(query);
		return 0;
	}
	
	public int removeQuiz(int quizId) {
		if (!isAdmin()) {
			return -1;
		}
		
		String query = DatabaseUtils.constructDeleteQuery("quizzes", new String[] {"quizID = " + quizId});
		DBConnection.updateTable(query);
		
		removeQuizHistory(quizId);//remove all the history associated with the quiz
		
		return 0;
	}
	
	public int removeQuizHistory(int quizId) {
		if (!isAdmin()) {
			return -1;
		}
		
		String query = DatabaseUtils.constructDeleteQuery("QuizHistory", new String[] {"quizID = " + quizId});
		DBConnection.updateTable(query);
		return 0;
	}
	
	public int setAdmin(String userToPromote) {
		if (!isAdmin()) {
			return -1;
		}
		
		String query = DatabaseUtils.constructUpdateQuery(
				"QuizUser", new String[]{"admin = true"}, new String[]{"username='"+userToPromote+"'"});
		DBConnection.updateTable(query);
		return 0;
	}
	
	public int getNumUsers() {
		int numUsers = -2;
		if (!isAdmin()) {
			return -1;
		}
		
		String query = DatabaseUtils.constructSelectQuery(new String[] {"distinct count(*)"}, new String[] {"QuizUser"}, new String[]{});
		ResultSet rs = DBConnection.getResult(query);
		try {
			if (rs.next() ) {
				numUsers = Integer.parseInt(rs.getString("count(*)"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return numUsers;
	}
	
	public int getNumQuizTaken() {
		int numQuizTaken = -2;
		if (!isAdmin()) {
			return -1;
		}
		
		String query = DatabaseUtils.constructSelectQuery(new String[] {"distinct count(*)"}, new String[] {"QuizHistory"}, new String[]{});
		ResultSet rs = DBConnection.getResult(query);
		try {
			if (rs.next() ) {
				numQuizTaken = Integer.parseInt(rs.getString("count(*)"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return numQuizTaken;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User)) return false;
		return ((User)obj).getUserName().equals(username);
	}
	
    public ArrayList<User> getAllUsers() {
        if (!isAdmin()) {
                return null;
        }

        ArrayList<User> userList = new ArrayList<User>();

        String query = DatabaseUtils.constructSelectQuery(
                        new String[] {"username"}, new String[] {"QuizUser"}, new String[] {});
        ResultSet rs = DBConnection.getResult(query);
        try {
                while (rs.next() ) {
                        userList.add( new User(rs.getString("username")));

                }

        } catch (SQLException e) {
                e.printStackTrace();
        }

        return userList;
	}
    
    public static int getUserBestScore(String user, int quizid) {
    	int maxScore = -1;
    	String query = "select max(score) from QuizHistory where username = '"+user+"' and quizID = "+quizid+";";
    	ResultSet rs = DBConnection.getResult(query);
        try {
                if (rs.next() ) {
                	maxScore = Integer.parseInt(rs.getString("max(score)"));
                }

        } catch (SQLException e) {
                e.printStackTrace();
        }
        
        return maxScore;
    }
    
    //String[] is {quiz id, quiz title, number of times taken}
    public static ArrayList<String[]> getQuizTakenTimes() {
    	ArrayList<String[]> result = new ArrayList<String[]>();
    	
    	String query = "select QuizHistory.quizID, title, count(*) from QuizHistory, quizzes where quizzes.quizID=QuizHistory.quizID group by quizID order by quizID;";
    	
    	ResultSet rs = DBConnection.getResult(query);
        try {
                while (rs.next() ) {
                	String[] entry = new String[] {rs.getString("quizID"), rs.getString("title"), rs.getString("count(*)")};
                	result.add(entry);
                }

        } catch (SQLException e) {
                e.printStackTrace();
        }
    	return result;
    } 
    
    //String[] is {quiz id, quiz title, username, score}
    public static ArrayList<String[]> getLeaderboardContents() {
    	ArrayList<String[]> result = new ArrayList<String[]>();
    	
    	String query = "select QuizHistory.quizID, title, username, max(score) from QuizHistory, quizzes where quizzes.quizID=QuizHistory.quizID group by quizID order by quizID;";
    	
    	ResultSet rs = DBConnection.getResult(query);
        try {
                while (rs.next() ) {
                	String[] entry = new String[] {rs.getString("quizID"), rs.getString("title"), rs.getString("username"), rs.getString("max(score)")};
                	result.add(entry);
                }

        } catch (SQLException e) {
                e.printStackTrace();
        }
    	
    	return result;
    }
    
    public static String getQuizTitle(int quizId) {
    	String title = new String();
    	String query = "select title from quizzes where quizID = " + Integer.toString(quizId)+";";
    	ResultSet rs = DBConnection.getResult(query);
        try {
                if (rs.next() ) {
                	title = rs.getString("title");
                }

        } catch (SQLException e) {
                e.printStackTrace();
        }
        
        return title;
    	
    }

}
