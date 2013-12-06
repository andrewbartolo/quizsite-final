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
 * Servlet implementation class RateQuizServlet
 */
@WebServlet("/RateQuizServlet")
public class RateQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RateQuizServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Quiz quiz = (Quiz) session.getAttribute("quizToRate");
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.println("<link rel='stylesheet' href='css/bootstrap.css' type='text/css'>");
		//out.println("<link rel='stylesheet' href='style/QuizLandingPage.css'>");
		out.println("<title>Quiz " + quiz.getQuizID() + "</title>");
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
    	out.println("</ul>");
    	out.println("</div>");
    	out.println("</div>");
		
		int numStars = 0;
		if (request.getParameter("1.x") != null) {
			numStars = 1;
		} else if (request.getParameter("2.x") != null) {
			numStars = 2;
		} else if (request.getParameter("3.x") != null) {
			numStars = 3;
		} else if (request.getParameter("4.x") != null) {
			numStars = 4;
		} else if (request.getParameter("5.x") != null) {
			numStars = 5;
		}
		out.println("<h3>" + user.getUserName() + "'s Rating for Quiz " + quiz.getQuizID() + "</h3>");
		out.println("<p> You rated this quiz " + numStars + " stars.</p>");
		try {
			Statement stmt = DatabaseUtils.setUp();
			String[] args = {user.getUserName(), quiz.getQuizID(), Integer.toString(numStars)};
			String insertQuery = DatabaseUtils.constructInsertQuery("ratings", args);
			DatabaseUtils.runInsertUpdateQuery(stmt, insertQuery);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.println("<form action=\"write_review.jsp\" method=\"GET\">");
		out.println("<input type=\"submit\" value=\"Write Review\" />");
		out.println("</form>");
		
		out.println("<a href=\"QuizInfoView\">Return to Quiz " + quiz.getQuizID() + "</a>");
		out.println("</br>");
		out.println("<a href=\"QuizLandingPage\">Return to All Quizzes</a>");
		
		out.println("</body>");
		out.println("</html>");
	}

}
