package quizsite;
import java.sql.*;
import java.lang.*;
import java.util.*;

/**
 * Super class Question
 * @author Josh Zhu
 *
 */
public class Question {
	public int type;
	public String quizID;
	public double score;
	public double grade;
	public String query;
	public List<String> possibleAnswers;
	public int index;
	public long time;
	public int questionID;
	
	public Question (){
		quizID = null;
		score = 0.0;
		query = null;
	}
	
	public void toDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection
						( "jdbc:mysql://" + MyDBInfo.MYSQL_DATABASE_SERVER, MyDBInfo.MYSQL_USERNAME,MyDBInfo.MYSQL_PASSWORD);
			Statement stmt = con.createStatement();
			stmt.executeQuery("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
			
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	}
	
	public void toUpdateDB(){
		return;
	}
	
	public double getScore(){
		return score;
	}
	
	public void setScore(double score){
		this.score = score;
	}
	
	public double getGrade(){
		return grade;
	}

	public int getType(){
		return type;
	}
	
	public String getQuery(){
		return query;
	}
	
	public void setQuery(String query){
		this.query = query;
	}

	public Map<String,String> getOptions(){
		Map<String,String> options = new HashMap<String,String>();
		return options;
	}
	
	public void setOptions(Map<String,String> options){
		return;
	}

	public List<String> getAnswers(){
		return possibleAnswers;
	}
	
	public void setAnswers(List<String> answers){
		possibleAnswers = answers;
	}
	
	public boolean getOrdered(){
		return false;
	}
	
	public void setOrdered(boolean ordered){
		return;
	}
	
	public List<List<String>>getMultAnswers(){
		return null;
	}

	public void setMultAnswers(List<List<String>> multAnswers){
		return;
	}
	
	public int getNumAns(){
		return 1;
	}
	
	public void setNumAns(int numAns){
		return;
	}
	/*public boolean checkSingleAnswer (String response, String legalAns){
		return true;
	}*/
	
	// for most questions where there is only one answer just check the first item in the list
	public boolean checkAnswer (List<String> response){
		if (possibleAnswers.isEmpty()){
			grade += score;
			return true;
		}
		for (String answer : possibleAnswers) {
			System.out.println(answer);
			if (answer.isEmpty()){
				System.out.println("shabi");
				grade += score;
				return true;
			}
			if (answer.equalsIgnoreCase(response.get(0))) {
				grade += score;
				return true;
			}
		}
		return false;
	}


}
