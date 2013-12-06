package quizsite;
import java.sql.*;
import java.lang.*;
import java.util.*;

/**
 * Subclass MultiChoice, handle multiple choice type question,also handle multiple answers
 * @author Josh Zhu
 *
 */
public class MultiChoice extends Question{
	
	public Map<String, String> options;
	public int numAns;
	

	public MultiChoice (Quiz quiz, int type, int questionID, String questionText, HashMap<String, String> options, List<String> answer, double score) {

		this.quizID = quiz.getQuizID();
		this.type = type;
		this.questionID = questionID;
		query = questionText;
		this.options = options;
		List<String> multChoiceAnswer = new ArrayList<String>();
		for (String ans : answer) 
			multChoiceAnswer.add(ans);
		this.possibleAnswers = multChoiceAnswer;
		numAns = possibleAnswers.size();
		index = quiz.getNumQuestion();
		this.score = score;
	}
	
	@Override
	public void toDB(){
		try {
			Statement stmt = DatabaseUtils.setUp();
			
			String[] questionArgs = {String.valueOf(questionID), quizID, "multipleChoice", String.valueOf(score), String.valueOf(index)};
			DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questions", questionArgs));
			
			String[] choiceArgs = {String.valueOf(questionID), query};
			DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("multipleChoice", choiceArgs));
			
			for (String optionID : options.keySet()) {
				String[] optionArgs = {String.valueOf(questionID), optionID, options.get(optionID)};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("multipleChoiceOptions", optionArgs));
			}
			
			for (String ans : possibleAnswers) {
				String[] answerArgs = {String.valueOf(questionID), ans};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("multipleChoiceAnswers", answerArgs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean checkAnswer(List<String> response) {
		int numMatched = 0;
		Set<String> possAnswers = new HashSet<String>();
		if (possibleAnswers.isEmpty()){
			grade+=score;
			return true;
		}
		for (int i = 0; i < possibleAnswers.size();i++){
			if (!isNumeric(possibleAnswers.get(i))){
				grade +=score;
				return true;
			}
			if (Integer.parseInt(possibleAnswers.get(i)) < 0 || Integer.parseInt(possibleAnswers.get(i)) > options.size()){
				grade+=score;
				return true;
			}
		}
		possAnswers.addAll(possibleAnswers);
		for (int i = 0; i < response.size(); ++i) {
			if (possAnswers.contains(response.get(i))) {
				numMatched++;
				grade += score / possibleAnswers.size();
			} else{
				grade = 0.0; //wrong answer or extra answer will receive 0
				return false;
			}
		}
		if (numMatched == possibleAnswers.size())
			return true;
		return false;
	}


	@Override
	public Map<String,String> getOptions(){
		return options;
	}
	
	@Override
	public int getNumAns(){
		return numAns;
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}

}
