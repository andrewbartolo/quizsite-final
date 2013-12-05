package quizsite;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import login.User;

import login.User;

/**
 * Servlet implementation class AllQuizLandingPage
 */
@WebServlet("/QuizLandingPage")
public class QuizLandingPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizLandingPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.println("<link rel='stylesheet' href='css/bootstrap.css' type='text/css/'>");
		//out.println("<link rel='stylesheet' href='style/QuizLandingPage.css'>");
		out.println("<title>All Quizzes</title>");
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
		
    	out.println("<div class='row'>");
    	out.println("<div class='span6'>");
		out.println("<form action =\"QuizSelectByCatServlet\" method=\"GET\">");
		out.println("<p>Search by Category:</p>");
		out.println("<select name=\"category\">");
		out.println("<option value=\"history\">History</option>");
		out.println("<option value=\"geography\">Geography</option>");
		out.println("<option value=\"science\">Science</option>");
		out.println("<option value=\"arts\">Arts</option>");
		out.println("<option value=\"sports\">Sports</option>");
		out.println("<option value=\"others\">Others</option>");
		out.println("</select>");
		out.println("<input type=\"submit\" value = \"Go\" />");
		out.println("</form>");
		out.println("</div>");
		out.println("<div class='span6'>");
		out.println("<form action =\"QuizSelectByTagsServlet\" method=\"GET\">");
		out.println("<p>Search by tag:  <input type=\"text\" name=\"tag\" />");
		out.println("<input type=\"submit\" value = \"Go\" /></p>");
		out.println("</form>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("<h1>All Quizzes</h1>");
		out.println("<p>Quizzes available:</p>");
		out.println("<ul>");
		@SuppressWarnings("unchecked")
		ArrayList<Quiz> allQuizzes = (ArrayList<Quiz>) request.getServletContext().getAttribute("allQuizzes");
		for (Quiz quiz : allQuizzes) {
			out.println("<div class='quiz'>");
			out.println("<li>");
			out.println("<form action=\"QuizInfoServlet\" method=\"GET\">");
			out.println("<input name=\"quizID\" type=\"hidden\" value=\"" + quiz.getQuizID() + "\"/>");
			out.println("<ul style=\"list-style: none;\">");
			out.println("<li>");
			if (user == null) {
				out.println("<button type=\"button\" disabled>" + quiz.getTitle() + "</button>");
			} else {
				out.println("<input type=\"submit\" value=\"" + quiz.getTitle() + "\"/>");
			}
			out.println("</li>");
			out.println("<li>BY: " + quiz.getCreator() + "</li><li>" + quiz.getDescription() + "</li><ul>");
			out.println("</form>");
			out.println("</li>");
			out.println("</div>");
		}
		out.println("</ul>");
		out.println("<form action =\"index.jsp\">");
		out.println("<input type=\"submit\" value=\"Back To Homepage\"/>");
		out.println("</form>");
		out.println("<script src='http://code.jquery.com/jquery-1.10.1.js'></script>");
		out.println("<script src='js/bootstrap.js'></script>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
