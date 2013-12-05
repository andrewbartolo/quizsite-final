package login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import quizsite.DatabaseUtils;

public class History {
	
	private String username;
	private int quizid;
	private double score;
	private long elapsedTime;
	private long endTime;
	private boolean isCreator;
	
	public String getUserName() {
		return username;
	}

	public int getQuizId() {
		return quizid;
	}
	
	public double getScore() {
		return score;
	}
	
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	public long getEndTime() {
		return endTime;
	}
	
	public boolean getIsCreator() {
		return isCreator;
	}
	
	public History(String username, int quizid, double score, long elapsedTime, long endTime, boolean isCreator) {
		this.username = username;
		this.quizid = quizid;
		this.score = score;
		this.elapsedTime = elapsedTime;
		this.endTime = endTime;
		this.isCreator = isCreator;
	}
	
	public static ArrayList<History> getTakenQuizzes(String username) {
		String query = DatabaseUtils.constructSelectQuery(new String[] {"*"}, new String[] {"QuizHistory"}, new String[] {"username='"+username+"';"});
		ArrayList<History> takenQuizzes = new ArrayList<History>();
		
		ResultSet rs = DBConnection.getResult(query);
		try {
			while (rs.next() ) {

				int quizid = Integer.parseInt( rs.getString("quizid") );
				double score = Double.parseDouble( rs.getString("score"));
				long elapsedTime = Long.parseLong( rs.getString("timeElapsed"));
				long endTime = Long.parseLong( rs.getString("endTime"));
				
				takenQuizzes.add( new History(username, quizid, score, elapsedTime, endTime, false) );
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return takenQuizzes;
	}
	
	
	public static ArrayList<History> getCreatedQuizzes(String username) {
		String query = DatabaseUtils.constructSelectQuery(new String[] {"*"}, new String[] {"quizzes"}, new String[] {"creator='"+username+"';"});
		ArrayList<History> createdQuizzes = new ArrayList<History>();
		
		ResultSet rs = DBConnection.getResult(query);
		try {
			while (rs.next() ) {

				int quizid = Integer.parseInt( rs.getString("quizid") );
				
				createdQuizzes.add( new History(username, quizid, 0.0, 0, 0, true) );
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return createdQuizzes;
	}
	
}
