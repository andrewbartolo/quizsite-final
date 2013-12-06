package quizsite;
import java.sql.*;
import java.util.*;

/**
 * Class quiz: handle methods for creating quizzes and importing to database
 * @author Josh Zhu
 *
 */

public class Quiz {
	 public String quizID;
	 public String creator;
	 public String title;
	 public String description;
	 public String category;
	 public List<String> tags;
	 public boolean random;
	 public boolean onepage;
	 public boolean practice;
	 public boolean immCorrect;
	 public int numQuestions;
	 public double score;
	 public double grade;
	 public long startTime;
	 //public long elapsedTime;
	 public List<Question> qlist;
	 
	 
	 
	 /**
	  * Constructor for a new quiz, taking parameters from the quiz setup page (prior setting any questions)
	  * @param username
	  * @param index - the number of the quizzes that the creator has created, start from 0
	  * @param description
	  * @param random
	  * @param onepage
	  * @param practice
	  * @param immCorrect
	  */

	 public Quiz(){
		 creator = null;
		 quizID = null;
		 this.title= null;
		 category = null;
		 tags = new ArrayList<String>();
		 this.description = null;
		 qlist = new ArrayList<Question>();
		 numQuestions = 0;
	 }

	 public Quiz(String quizID, String creator, String title, String category, List<String> tags, String description, int numQuestions, boolean random, boolean onepage, boolean immCorrect, boolean practice) {
		 this.quizID = quizID;
		 this.creator = creator;
		 this.title = title;
		 this.category = category;
		 this.tags = tags;
		 this.description = description;
		 this.numQuestions = numQuestions; 
		 this.random = random;
		 this.onepage = onepage;
		 this.immCorrect = immCorrect;
		 this.practice = practice;
		 this.qlist = new ArrayList<Question>();
	 }
	 
	 /**
	  * Adding each question to quiz
	  * @param q
	  */
	 public void AddQuestion (Question q){
		 qlist.add(q);
		 numQuestions++;
		 //score+=q.getScore();
		 //grade+=q.getGrade();
		 
	 }
	 
	 /**
	  * Import the quiz entry to the DB for table - "quizzes"
	  */
	 
	 public void toQuizDB(){
			try {
				Statement stmt = DatabaseUtils.setUp();
				
				String[] args = {quizID, creator, title, category, description, String.valueOf(numQuestions), String.valueOf(random), String.valueOf(onepage), String.valueOf(immCorrect), String.valueOf(practice)};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("quizzes", args));
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	 }
	 
	 public void updateQuizDB(){
			try {
				Statement stmt = DatabaseUtils.setUp();
				
				String[] setValues = {"title = \""+title+"\"", "category = \""+category+"\"", "description = \""+description+"\"", 
						"numQuestions = \""+String.valueOf(numQuestions)+"\"","random = \""+String.valueOf(random)+"\"", 
						"onepage = \""+String.valueOf(onepage)+"\"", "immCorrect = \""+String.valueOf(immCorrect)+"\"", "practice = \""+String.valueOf(practice)+"\""};
				String[] args = {"quizID = \"" + quizID + "\""};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructUpdateQuery("quizzes", setValues,args));
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		 
	 }
	 /**
	  * Import the tags entry to the DB for table - "tags"
	  */
	 
	 public void toTagDB(){
			try {
				Statement stmt = DatabaseUtils.setUp();
				for (String tag: tags){
				String[] args = {quizID, tag};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("tags", args));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	 }
	 
	 /**
	  * Import the tags entry to the DB for table - "tags"
	  */
	 
	 public void updateTagDB(){
			try {
				Statement stmt = DatabaseUtils.setUp();
				String[] args = {"quizID = \"" + quizID + "\""};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructDeleteQuery("tags", args));
				for (String tag: tags){
				String[] argIns = {quizID, tag};
				DatabaseUtils.runInsertUpdateQuery(stmt, DatabaseUtils.constructInsertQuery("tags", argIns));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	 }
	 
	 public String getCreator(){
		 return creator;
	 }
	 
	 public void setCreator(String username){
		 creator = username;
	 }
	 
	 public String getQuizID(){
		 return quizID;
	 }
	 
	 public void setQuizID(String quizID){
		 this.quizID = quizID;
	 }
	 
	 public String getTitle(){
		 return title;
	 }
	 
	 public void setTitle(String title){
		 this.title = title;
	 }
	 
	 public String getDescription(){
		 return description;
	 }
	 
	 public void setDescription(String description){
		 this.description = description;
	 }
	 
	 public String getCategory(){
		 return category;
	 }
	 
	 public void setCategory(String category){
		 this.category = category;
	 }
	 
	 public List<String> getTags(){
		 return tags;
	 }
	 
	 public void setTags(List<String> tags){
		 this.tags = tags;
	 }
	 
	 public boolean getRandom(){
		 return random;
	 }
	 
	 public void setRandom(boolean random){
		 this.random = random;
	 }
	 
	 public boolean getOnepage(){
		 return onepage;
	 }
	 
	 public void setOnepage(boolean onepage){
		 this.onepage = onepage;
	 }
	 
	 public boolean getPractice(){
		 return practice;
	 }
	 
	 public void setPractice(boolean practice){
		 this.practice = practice;
	 }
	 
	 public boolean getImmCorrect(){
		 return immCorrect;
	 }
	 
	 public void setImmCorrect(boolean immCorrect){
		 this.immCorrect = immCorrect;
	 }
	 
	 public int getNumQuestion(){
		 return numQuestions;
	 }
	 
	 
	 public double getTotalScore(){
		 score = 0.0;
		 for (int i = 0; i < qlist.size(); i++){
			 score += qlist.get(i).getScore();
		 }
		 return score;
	 }
	 
	 public double getTotalGrade(){
		 grade = 0.0;
		 for (int i = 0; i < qlist.size(); i++){
			 grade+=qlist.get(i).getGrade();
		 }
		 return grade;
	 }
	 
	 public long getTotalTime(){
		 long endTime = System.currentTimeMillis();
		 return endTime - startTime;
	 }
	 
	 public List<Question> getQuestionList(){
		 return qlist;
	 }
	 
		public static boolean isNumeric(String str)
		{
		  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
		}
	 
	
}
