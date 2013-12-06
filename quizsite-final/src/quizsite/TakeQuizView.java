package quizsite;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import login.User;

/**
 * Servlet implementation class TakeQuizView
 */
@WebServlet("/TakeQuizView")
public class TakeQuizView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TakeQuizView() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		Quiz quiz = (Quiz) session.getAttribute("quizToTake");
		session.setAttribute("quizStartTime", System.currentTimeMillis());
		
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
	    out.println("<h1>Quizzap!</h1>");
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
    	
		out.println("<h1>" + quiz.getTitle() + "</h1>");
		out.println("<h3>" + quiz.getDescription() + "</h3>");
		out.println("<form action=\"CheckQuizServlet\" method=\"POST\">");
		for (int i = 0; i < quiz.qlist.size(); ++i) {
			out.println("<h3>Question " + (i + 1) + "</h3>");
			if (quiz.qlist.get(i) instanceof QuestionResponse) {
				QuestionResponse tempQues = (QuestionResponse) quiz.qlist.get(i);
				out.println("<p>" + tempQues.getQuery() + "</p>");
				if (tempQues.getNumAns() == 1){
				out.println("<input type=\"text\" name=\"response" + i + "\" size=\"50\" value=\"Answer\">");
				} else{
					for (int j = 0; j < tempQues.getNumAns(); j++){
						out.println("<input type=\"text\" name=\"response" + i + j + "\" size=\"50\" value=\"Answer\">");
					}
				}
			} else if (quiz.qlist.get(i) instanceof Blank) {
				Blank tempQues = (Blank) quiz.qlist.get(i);
				out.println("<p>" + tempQues.getQuery() + "</p>");
				out.println("<input type=\"text\" name=\"response" + i + "\" size=\"50\" value=\"Answer\">");
			} else if (quiz.qlist.get(i) instanceof Picture) {
				Picture tempQues = (Picture) quiz.qlist.get(i);
				out.println("<img src=\"" + tempQues.getQuery() + "\" height = \"300\"/>");
				out.println("<br/><br/>");
				out.println("<input type=\"text\" name=\"response" + i + "\" size=\"50\" value=\"Answer\">");
			} else if (quiz.qlist.get(i) instanceof MultiChoice) {
				MultiChoice tempQues = (MultiChoice) quiz.qlist.get(i);
				out.println("<p>" + tempQues.getQuery() + "</p>");
				if (tempQues.getNumAns() == 1){
					for (String key : tempQues.getOptions().keySet()) {
						out.println("<input type=\"radio\" name=\"response" + i + "\" value=\"" + key + "\">" + tempQues.getOptions().get(key) + "</input>");
					}
				} else {
					out.println("<p> Please choose all that apply: </p>");
					int j = 0;
					for (String key : tempQues.getOptions().keySet()) {
						out.println("<input type=\"checkbox\" name=\"response" + i + j + "\" value=\"" + key + "\">" + tempQues.getOptions().get(key) + "</input>");
						j++;
					}
				}
			}
		}
		out.println("<br/><br/>");
		out.println("<input type=\"submit\" value=\"Submit\"/>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
