package quizsite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class QuizLandingPage
 *
 */
@WebListener
public class QuizServletContext implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public QuizServletContext() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	try {
    		Statement stmt = DatabaseUtils.setUp();
    		
    		String[] extractFields = {"*"};
    		String[] tableNames = {"quizzes"};
    		String[] args = {};
    		
    		ResultSet rs = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractFields, tableNames, args));
    		ArrayList<String[]> quizInfo = new ArrayList<String[]>();
			while (rs.next()) {
				String[] tempInfo = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)}; 
				quizInfo.add(tempInfo);
			}
    		ArrayList<Quiz> allQuizzes = new ArrayList<Quiz>();
    		
    		for (String[] info: quizInfo){
    			String[] extractTagFields = {"*"};
    			String[] tagTableNames = {"tags"};
    			String[] argsTag = {"quizID = \"" + info[0] + "\""};
    			ArrayList<String> tags = new ArrayList<String>();
    			ResultSet tagText = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractTagFields, tagTableNames, argsTag));
    			while (tagText.next()) {
    				tags.add(tagText.getString(2));
    			}
    			Quiz tempQuiz = new Quiz(info[0],info[1],info[2],info[3], tags, info[4], Integer.parseInt(info[5]), Boolean.parseBoolean(info[6]), Boolean.parseBoolean(info[7]), Boolean.parseBoolean(info[8]), Boolean.parseBoolean(info[9]));
    			allQuizzes.add(tempQuiz);
    		}
    		
    		Collections.sort(allQuizzes, new Comparator<Quiz>() {

				@Override
				public int compare(Quiz arg0, Quiz arg1) {
					// TODO Auto-generated method stub
					return Integer.parseInt(arg0.getQuizID()) - Integer.parseInt(arg1.getQuizID());
				}
    	    });
 
    		ServletContext serv = arg0.getServletContext();
            serv.setAttribute("allQuizzes", allQuizzes);
            
            //recent quiz part
           quizInfo = new ArrayList<String[]>();
           String queryStmt = "select * from quizzes order by quizID desc limit 5";
           rs = DatabaseUtils.runSelectQuery(stmt,queryStmt);
			while (rs.next()) {
				String[] tempInfo = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)}; 
				quizInfo.add(tempInfo);
			}
			ArrayList<Quiz> recentQuizzes = new ArrayList<Quiz>();
    		
    		for (String[] info: quizInfo){
    			String[] extractTagFields = {"*"};
    			String[] tagTableNames = {"tags"};
    			String[] argsTag = {"quizID = \"" + info[0] + "\""};
    			ArrayList<String> tags = new ArrayList<String>();
    			ResultSet tagText = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractTagFields, tagTableNames, argsTag));
    			while (tagText.next()) {
    				tags.add(tagText.getString(2));
    			}
    			Quiz tempQuiz = new Quiz(info[0],info[1],info[2],info[3], tags, info[4], Integer.parseInt(info[5]), Boolean.parseBoolean(info[6]), Boolean.parseBoolean(info[7]), Boolean.parseBoolean(info[8]), Boolean.parseBoolean(info[9]));
    			recentQuizzes.add(tempQuiz);
    		}
    		arg0.getServletContext().setAttribute("recentQuizzes", recentQuizzes);
    		
            ArrayList<Quiz> targetQuizzes = new ArrayList<Quiz>();
            arg0.getServletContext().setAttribute("targetQuizzes", targetQuizzes);
    		
    	} catch (SQLException e){
    		e.printStackTrace();
    	}	catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
