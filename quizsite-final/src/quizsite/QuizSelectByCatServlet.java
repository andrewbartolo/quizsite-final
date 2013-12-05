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
 * Servlet implementation class QuizSelectByCatServlet
 */
@WebServlet("/QuizSelectByCatServlet")
public class QuizSelectByCatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizSelectByCatServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String category = request.getParameter("category");
    	try {
    		Statement stmt = DatabaseUtils.setUp();
    		
    		String[] extractFields = {"*"};
    		String[] tableNames = {"quizzes"};
    		String[] args = {"category = \"" + category + "\""};
    		
    		ResultSet rs = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractFields, tableNames, args));
    		ArrayList<String[]> quizInfo = new ArrayList<String[]>();
			while (rs.next()) {
				String[] tempInfo = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)}; 
				quizInfo.add(tempInfo);
			}
    		ArrayList<Quiz> targetQuiz = new ArrayList<Quiz>();
    		
    		for (String[] info: quizInfo){
    			String[] extractTagFields = {"tag"};
    			String[] tagTableNames = {"tags"};
    			String[] argsTag = {"quizID = \"" + info[0] + "\""};
    			ArrayList<String> tags = new ArrayList<String>();
    			ResultSet tagText = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractTagFields, tagTableNames, argsTag));
    			while (tagText.next()) {
    				tags.add(tagText.getString(1));
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
