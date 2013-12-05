package quizsite;
import java.sql.*;
import java.util.*;
import java.lang.*;

/**
 * Subclass Blank, handle filling-the-blank question
 * @author Josh Zhu
 *
 */
public class Blank extends Question{
	

	public Blank (Quiz quiz, int type, int questionID, String pre, String post, List<String> answers, double score){


		this.quizID = quiz.getQuizID();
		this.type = type;
		this.questionID = questionID;
		possibleAnswers = answers;
		index = quiz.getNumQuestion();
		this.score = score;
		query = pre + "_____" + post;
	}
	
	public Blank(Quiz quiz, int type, int questionID, String query, List<String> answers, double score) {
		this.quizID = quiz.getQuizID();
		this.type = type;
		this.questionID = questionID;
		possibleAnswers = answers;
		index = quiz.getNumQuestion();
		this.score = score;
		this.query = query;
	}
	
	@Override
	public void toDB(){
		try {
			Statement stmt = DatabaseUtils.setUp();
			
			String[] questionArgs = {String.valueOf(questionID), quizID, "fillInTheBlanks", String.valueOf(score), String.valueOf(index)};
			DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questions", questionArgs));
			
			String[] blanksArgs = {String.valueOf(questionID), query};
			DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("fillInTheBlanks", blanksArgs));
			
			for (String answer : possibleAnswers) {
				String[] answerArgs = {String.valueOf(questionID), answer};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("fillInTheBlanksAnswers", answerArgs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {				
			e.printStackTrace();
		}
	}
}
