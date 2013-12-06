package quizsite;
import java.sql.*;
import java.util.*;
import java.lang.*;

/**
 * Subclass SingleResponse, handle query-response type question,also handle multiple answers
 * @author Josh Zhu
 *
 */
public class QuestionResponse extends Question{

	public boolean ordered;
	public int numAns;
	public List<List<String>> possibleMultAnswers; //maximum 20 slots of possible answers available
	
	public QuestionResponse(Quiz quiz, int type, int questionID, String query, List<String> answers, double score, boolean ordered, int numAns) {
		this.quizID = quiz.getQuizID();
		this.type = type;
		this.questionID = questionID;
		this.possibleAnswers = answers;
		this.query = query;
		this.score = score;
		this.ordered = ordered;
		index = quiz.getNumQuestion();
		this.numAns = numAns;
		this.grade = 0.0;
		
	}
	
	@Override
	public List<List <String>> getMultAnswers(){
		return possibleMultAnswers;
	}
	
	@Override
	public void setMultAnswers(List<List <String>>answers ){
		possibleMultAnswers = answers;
		
	}
	@Override
	public boolean getOrdered(){
		return ordered;
	}
	
	@Override
	public void setOrdered(boolean ordered){
		this.ordered = ordered;
	}
	
	@Override
	public int getNumAns(){
		return numAns;
	}
	
	@Override
	public void setNumAns(int numAns){
		this.numAns = numAns;
	}

	@Override
	public void toDB(){
		try {
			Statement stmt = DatabaseUtils.setUp();
			
			if (type == 0) {
				String[] questionArgs = {String.valueOf(questionID), quizID, "questionResponse", String.valueOf(score), String.valueOf(index)};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questions", questionArgs));

				String[] questionResponseArgs = {String.valueOf(questionID), query};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questionResponse", questionResponseArgs));

				for (String answer : possibleAnswers) {
					String [] answerArgs = {String.valueOf(questionID), answer};
					DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questionResponseAnswers", answerArgs));
				}
			} else {
				String[] questionArgs = {String.valueOf(questionID), quizID, "questionResponseMult", String.valueOf(score), String.valueOf(index)};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questions", questionArgs));
				
				String[] questionResponseArgs = {String.valueOf(questionID), query, String.valueOf(numAns), String.valueOf(ordered)};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questionResponseMult", questionResponseArgs));
				
				for (int i = 0; i < possibleMultAnswers.size(); i++) {
					for (int j = 0; j < possibleMultAnswers.get(i).size(); j++){
					String response = possibleMultAnswers.get(i).get(j);
						String [] answerArgs = {String.valueOf(questionID), response, String.valueOf(i)};
						DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questionResponseMultAnswers", answerArgs));
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void toUpdateDB(){
		try {
			Statement stmt = DatabaseUtils.setUp();
			String[] setValues = {"score = \""+score+"\""};
			String[] args = {"questionID = \"" + questionID + "\""};
			DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructUpdateQuery("questions", setValues,args));
			
			if (type == 0) {
				String[] questionResponseValues = {"questionText = \""+query+"\""};
			
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructUpdateQuery("questionResponse", questionResponseValues, args));
				
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructDeleteQuery("questionResponseAnswers", args));

				for (String answer : possibleAnswers) {
					String [] answerArgs = {String.valueOf(questionID), answer};
					DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questionResponseAnswers", answerArgs));
				}
			} else {
				String[] questionResponseMultValues = {"questionText = \""+query+"\"","numAns=\""+String.valueOf(numAns)+"\"","ordered =\""+String.valueOf(ordered)+"\""};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructUpdateQuery("questionResponseMult", questionResponseMultValues, args));
				
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructDeleteQuery("questionResponseMultAnswers", args));
				
				for (int i = 0; i < possibleMultAnswers.size(); i++) {
					for (int j = 0; j < possibleMultAnswers.get(i).size(); j++){
					String response = possibleMultAnswers.get(i).get(j);
						String [] answerArgs = {String.valueOf(questionID), response, String.valueOf(i)};
						DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("questionResponseMultAnswers", answerArgs));
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean checkAnswer(List<String>response) {
		if (type == 0) {
			return super.checkAnswer(response);
		} else {
			int correct = 0;
			if (possibleMultAnswers.isEmpty()){
				grade+= score;
				return true;
			}

			if (ordered) {
				for (int i = 0; i < response.size(); ++i) {
					if (possibleMultAnswers.get(i).isEmpty()){
						grade+=score/numAns;
						correct++;
						break;
					}
					for (String ansElement: possibleMultAnswers.get(i)){
						if (response.get(i).equalsIgnoreCase(ansElement) || ansElement.isEmpty()){//get a right answer
							grade += score / numAns;
							correct++; 
							break;
						}
					}
					if (correct == numAns){
						return true;
					}
				}
				return false;
			} else {
				Map<Integer, Set<String>> possAnswers = new HashMap<Integer, Set<String>>();
				
				
				for (int i = 0; i < possibleMultAnswers.size();i++){
						Set <String> answerSet = new HashSet<String>();
						answerSet.addAll(possibleMultAnswers.get(i));
						//System.out.println(answerSet);
						possAnswers.put(i, answerSet);
				}
				Set<String> matchedAnswers = new HashSet<String>();
				Set<Integer> matchedIndex = new HashSet<Integer>();
				for (int i = 0; i < response.size(); ++i) {
					for (int j = 0; j < possibleMultAnswers.size();j++){
						if (possibleMultAnswers.get(j).isEmpty()){
							grade+=score/numAns;
							correct++;
							break;
						}
						if (possAnswers.get(j).contains(response.get(i)) && !matchedIndex.contains(j)) {
							grade += score / numAns;
							matchedAnswers.add(response.get(i));
							//System.out.println(j);
							matchedIndex.add(j);
							break;
						}
					}
				}
				if (matchedAnswers.size() == numAns)
					return true;
			}
		}
		return false;
	}
	
}
