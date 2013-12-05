package quizsite;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import login.User;

/**
 * Servlet implementation class ReviewQuizServlet
 */
@WebServlet("/ReviewQuizServlet")
public class ReviewQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReviewQuizServlet() {
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
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Quiz quiz = (Quiz) session.getAttribute("quizToRate");
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.println("<link rel='stylesheet' href='css/bootstrap.css' type='text/css/'>");
		//out.println("<link rel='stylesheet' href='style/QuizLandingPage.css'>");
		out.println("<title>Review Quiz</title>");
		out.println("</head>");
		out.println("<body>");
		
	    out.println("<div class='container'>");
	    out.println("<h1>QuizSite</h1>");
	    out.println("</div>");
	    
	    out.println("<div class='navbar'>");
    	out.println("<div class='navbar-inner'>");
    	out.println("<ul class='nav'>");
    	out.println("<li><a href='index.jsp'>Home</a></li>");
    	out.println("<li class='active'><a href='QuizLandingPage'>Quizzes</a></li>");
    	out.println("<li><a href='leaderboard.jsp'>Leaderboard</a></li>");
    	if (user == null) out.println("<li><a href='login_home.html'>Log In</a></li>");
    	else {
    		out.println("<li><a href='user.jsp'>Your User Profile</a></li>");
    		if (user.isAdmin()) out.println("<li><a href='AdminServlet'>Administrator Panel</a></li>");
    		out.println("<li><a href='ResponderServlet?action=logout'>Log Out</a></li>");
    	}
    	out.println("</ul></div></div>");
    	
		out.println("<h3>" + user.getUserName() + "'s Review for Quiz " + quiz.getQuizID() + "</h3>");
		out.println("<p>Thank you for reviewing this quiz!</p>");
		try {
			Statement stmt = DatabaseUtils.setUp();
			String[] args = {user.getUserName(), quiz.getQuizID(), request.getParameter("review")};
			String insertQuery = DatabaseUtils.constructInsertQuery("reviews", args);
			DatabaseUtils.runInsertUpdateQuery(stmt, insertQuery);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.println("<a href=\"QuizInfoView\">Return to Quiz " + quiz.getQuizID() + "</a>");
		out.println("</br>");
		out.println("<a href=\"QuizLandingPage\">Return to All Quizzes</a>");
	
	}

}
