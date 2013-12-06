package quizsite;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import login.*;

/**
 * Servlet implementation class CreateQuiz
 */
@WebServlet("/CreateQuiz")
public class CreateQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuiz() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String title= request.getParameter("title");
		String description = request.getParameter("description");
		String category = request.getParameter("category");
		boolean random = request.getParameter("random").equals("true");
		boolean onepage = request.getParameter("onepage").equals("true");
		boolean practice = request.getParameter("practice").equals("true");
		boolean immCorrect = request.getParameter("immCorrect").equals("true");
		String alltags = request.getParameter("tags");
		List<String> tags = new ArrayList<String>();
		tags =  Arrays.asList(alltags.split(","));
		
		
		User user = (User) request.getSession().getAttribute("user");
		String creator = user.getUserName();
		Quiz quiz = new Quiz();
		//String creator = "josh";
		
		//load quizID
		String id = "0";
		int i = 0,j = 0;
		try {
			Statement stmt = DatabaseUtils.setUp();
			ResultSet rs = stmt.executeQuery("SELECT max(quizID) FROM quizzes");
			while(rs.next()){
				id = rs.getString("max(quizID)");			
			}
			if (id == null){
				id = "0";
			}
			i = Integer.parseInt(id);
			
			rs = stmt.executeQuery("SELECT max(quizID) FROM questions");
			while(rs.next()){
				id = rs.getString("max(quizID)");			
			}
			if (id == null){
				id = "0";
			}
			j = Integer.parseInt(id);
		} catch (SQLException e) {
					e.printStackTrace();
		} catch (ClassNotFoundException e) {
					e.printStackTrace();
		}
		int quizid = Math.max(i, j);
		
		quiz.setCreator(creator);
		quiz.setQuizID(String.valueOf(quizid+1));
		quiz.setTitle(title);
		quiz.setDescription(description);
		quiz.setCategory(category);
		quiz.setTags(tags);
		quiz.setRandom(random);
		quiz.setOnepage(onepage);
		quiz.setPractice(practice);
		quiz.setImmCorrect(immCorrect);
		
		
		request.getSession().setAttribute("quiz",quiz);
		
		RequestDispatcher dispatch = request.getRequestDispatcher("question_types.jsp");
		dispatch.forward(request, response);
		
		
	}

}
