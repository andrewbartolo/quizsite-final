package quizsite;
import java.sql.*;
import java.util.*;
import java.lang.*;
/**
 * Subclass Picture, handle picture response type question,also handle multiple answers
 * @author Josh Zhu
 *
 */
public class Picture extends Question{
	
	public Picture (Quiz quiz, int type, int questionID, String imageURL, List<String> answers, double score){
		this.type = type;
		this.questionID = questionID;
		query = imageURL;
		this.quizID = quiz.getQuizID();
		index = quiz.getNumQuestion();
		this.score = score;
		possibleAnswers = answers;
	}
	
	@Override
	public void toDB(){
		try {
			Statement stmt = DatabaseUtils.setUp();
			
			String[] questionArgs = {String.valueOf(questionID), quizID, "pictureResponse", String.valueOf(score), String.valueOf(index)};
			DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questions", questionArgs));
			
			String[] picArgs = {String.valueOf(questionID), query};
			DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("pictureResponse", picArgs));
			
			for (String answer : possibleAnswers) {
				String[] answerArgs = {String.valueOf(questionID), answer};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("pictureResponseAnswers", answerArgs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {	
			e.printStackTrace();
		}
	}

}
