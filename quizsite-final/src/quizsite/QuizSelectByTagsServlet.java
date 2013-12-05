package quizsite;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QuizSelectByTagsServlet
 */
@WebServlet("/QuizSelectByTagsServlet")
public class QuizSelectByTagsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizSelectByTagsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String tag = request.getParameter("tag");
		if (tag == ""){
			RequestDispatcher dispatch = request.getRequestDispatcher("QuizLandingPage");
			dispatch.forward(request, response);
		} else{
    	try {
    		Statement stmt = DatabaseUtils.setUp();
    		ResultSet rs;
    		String[] extractFields = {"*"};
    		String[] tableNames = {"tags"};
    		
    		String[] args = {"tag = \"" + tag + "\""};
    		
    		
    		rs = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractFields, tableNames, args));
    		
    		
    		
    		ArrayList<String> quizIDList = new ArrayList<String>();
			while (rs.next()) {
				quizIDList.add(rs.getString(1));
			}
    		
    		ArrayList<String[]> quizInfo = new ArrayList<String[]>();

    		for (String quizID: quizIDList){
    			String[] extractTagFields = {"*"};
    			String[] tagTableNames = {"quizzes"};
    			String[] argsTag = {"quizID = \"" + quizID + "\""};
    			
    			ResultSet quizrs = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractTagFields, tagTableNames, argsTag));
    			while (quizrs.next()) {
    				String[] tempInfo = {quizrs.getString(1), quizrs.getString(2), quizrs.getString(3), quizrs.getString(4),quizrs.getString(5),quizrs.getString(6),quizrs.getString(7),quizrs.getString(8),quizrs.getString(9),quizrs.getString(10)}; 
    				quizInfo.add(tempInfo);
    			}
    		}
    		ArrayList<Quiz> targetQuiz = new ArrayList<Quiz>();
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
    			targetQuiz.add(tempQuiz);
    		}
    		
    		Collections.sort(targetQuiz, new Comparator<Quiz>() {

				@Override
				public int compare(Quiz arg0, Quiz arg1) {
					// TODO Auto-generated method stub
					return Integer.parseInt(arg0.getQuizID()) - Integer.parseInt(arg1.getQuizID());
				}
    	    });
    		request.getServletContext().setAttribute("targetQuizzes", targetQuiz);
			RequestDispatcher dispatch = request.getRequestDispatcher("QuizFilteredLandingPage");
			dispatch.forward(request, response);
    		
    	} catch (SQLException e){
    		e.printStackTrace();
    	}	catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
    
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}