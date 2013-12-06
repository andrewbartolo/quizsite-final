package login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import quizsite.DatabaseUtils;

public class Achievement {

	public static String[] achievementList = {
		"Amateur Author",
		"Prolific Author",
		"Prodigious Author",
		"Quiz Machine",
		"I am the Greatest",
		"Practice Makes Perfect"
	};
	
	String achName;
	long timeEarned;
	
	public String getName() {
		return achName;
	}
	
	public long getTimeEarned() {
		return timeEarned;
	}
	
	public Achievement(String achName, long timeEarned) {
		this.achName = achName;
		this.timeEarned = timeEarned;
	}
	
	public static ArrayList<Achievement> getAllAchievements(String username) {
		String query = DatabaseUtils.constructSelectQuery(new String[] {"*"}, new String[] {"QuizAchievements"}, new String[] {"username= '"+username+"';"});
		ArrayList<Achievement> achievements = new ArrayList<Achievement>();
		
		ResultSet rs = DBConnection.getResult(query);
		try {
			while (rs.next() ) {
				String achievementName = rs.getString("achievement");
				long timeEarned = Long.parseLong(rs.getString("time"));
				achievements.add(new Achievement(achievementName, timeEarned));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return achievements;
	}
	
	public static void checkNewAchievements(String username) {
		ArrayList<Achievement> achievements = getAllAchievements(username);
		
		String query = DatabaseUtils.constructSelectQuery(
				new String[]{"count(*)"}, new String[]{"quizzes"}, new String[]{"creator='"+username+"'"});
		ResultSet rs = DBConnection.getResult(query);
		int numQuizCreated = 0;
		try {
			if (rs.next() ) {
				numQuizCreated = Integer.parseInt(rs.getString("count(*)"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//check Amateur Author
		if (numQuizCreated >= 1) {
			boolean earned = false;
			for (Achievement a : achievements) {
				if (a.getName().equals(achievementList[0])) {
					earned = true;
				}
			}
			if (!earned) {
				String insertQuery = DatabaseUtils.constructInsertQuery(
						"QuizAchievements", new String[]{username, achievementList[0],Long.toString(System.currentTimeMillis())});
				DBConnection.updateTable(insertQuery);
			}
		}
		
		//check Prolific Author
		if (numQuizCreated >= 5) {
			boolean earned = false;
			for (Achievement a : achievements) {
				if (a.getName().equals(achievementList[1])) {
					earned = true;
				}
			}
			if (!earned) {
				String insertQuery = DatabaseUtils.constructInsertQuery(
						"QuizAchievements", new String[]{username, achievementList[1],Long.toString(System.currentTimeMillis())});
				DBConnection.updateTable(insertQuery);
			}
		}
		
		//check Prodigious Author
		if (numQuizCreated >= 10) {
			boolean earned = false;
			for (Achievement a : achievements) {
				if (a.getName().equals(achievementList[2])) {
					earned = true;
				}
			}
			if (!earned) {
				String insertQuery = DatabaseUtils.constructInsertQuery(
						"QuizAchievements", new String[]{username, achievementList[2],Long.toString(System.currentTimeMillis())});
				DBConnection.updateTable(insertQuery);
			}
		}
		
		String query2 = DatabaseUtils.constructSelectQuery(
				new String[]{"count(*)"}, new String[]{"QuizHistory"}, new String[]{"username='"+username+"'"});
		ResultSet rs2 = DBConnection.getResult(query2);
		int numQuizTaken = 0;
		try {
			if (rs2.next() ) {
				numQuizTaken = Integer.parseInt(rs2.getString("count(*)"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//check Quiz Machine
		if (numQuizTaken >= 10) {
			boolean earned = false;
			for (Achievement a : achievements) {
				if (a.getName().equals(achievementList[3])) {
					earned = true;
				}
			}
			if (!earned) {
				String insertQuery = DatabaseUtils.constructInsertQuery(
						"QuizAchievements", new String[]{username, achievementList[3],Long.toString(System.currentTimeMillis())});
				DBConnection.updateTable(insertQuery);
			}
		}
		
		
		String query3 = "select distinct count(H1.quizID) from QuizHistory H1 where H1.username = '"+username+"' and H1.score in (select max(H2.score) from QuizHistory H2 where H1.quizID= H2.quizID);";
		ResultSet rs3 = DBConnection.getResult(query3);
		int numTopScore = 0;
		try {
			if (rs3.next() ) {
				numQuizTaken = Integer.parseInt(rs3.getString("count(H1.quizID)"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//check I am the Greatest
		if (numTopScore >= 1) {
			boolean earned = false;
			for (Achievement a : achievements) {
				if (a.getName().equals(achievementList[4])) {
					earned = true;
				}
			}
			if (!earned) {
				String insertQuery = DatabaseUtils.constructInsertQuery(
						"QuizAchievements", new String[]{username, achievementList[4],Long.toString(System.currentTimeMillis())});
				DBConnection.updateTable(insertQuery);
			}
		}
		
	}
	

	
}
