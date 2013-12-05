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
 * Servlet implementation class TakeQuizViewImmCorr
 */
@WebServlet("/TakeQuizViewImmCorr")
public class TakeQuizViewImmCorr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TakeQuizViewImmCorr() {
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
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		Quiz quiz = (Quiz) session.getAttribute("quizToTake");
		int quizQuestionIndex = ((Integer) session.getAttribute("quizToTakeIndex")).intValue();
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.println("<link rel='stylesheet' href='css/bootstrap.css' type='text/css/'>");
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

		out.println("<h1>" + quiz.getTitle() + "</h1>");
		if (quiz.qlist.size() == quizQuestionIndex) {
			out.println("<form action=\"CheckQuizServlet\" method=\"POST\">");
		} else {
			out.println("<form action=\"TakeQuizViewMultPage\" method=\"POST\">");
		}
		
		for (int i = 0; i < quizQuestionIndex; ++i) {
			//System.out.println(quizQuestionIndex);
			if (quiz.qlist.get(i).getType() == 1){
				for (int j = 0; j < quiz.qlist.get(i).getNumAns(); j++){
					String tempResponse = request.getParameter("response"+i+j);
					out.println("<input type=\"hidden\" name=\"response" + i + j +"\" value=\"" + tempResponse + "\" />");
				}
			} else if (quiz.qlist.get(i).getType() == 3 && quiz.qlist.get(i).getNumAns() > 1){
				for (int j = 0; j < quiz.qlist.get(i).getOptions().size(); j++){
					String tempResponse = request.getParameter("response"+i+j);
					if (tempResponse != null){
					//out.println("<p>" + tempResponse + "</p>");
					out.println("<input type=\"hidden\" name=\"response" + i + j +"\" value=\"" + tempResponse + "\" />");
					}
					
				}
			} else{
			String tempResponse = request.getParameter("response" + i);
			out.println("<input type=\"hidden\" name=\"response" + i + "\" value=\"" + tempResponse + "\" />");
			}
		}
		
		out.println("<h3>Question " + quizQuestionIndex + "</h3>");
		if (quiz.qlist.get(quizQuestionIndex-1).getType() == 1){
			for (int j = 0; j < quiz.qlist.get(quizQuestionIndex-1).getNumAns(); j++){
				String tempResponse = request.getParameter("response"+(quizQuestionIndex-1)+j);
				out.println("<p>Your Answer: " + tempResponse + "</p>");
				if (quiz.qlist.get(quizQuestionIndex-1).getOrdered() == true){
				out.println("<p>Preferred Answer: " + quiz.qlist.get(quizQuestionIndex-1).getMultAnswers().get(j).get(0) + "</p>");
				}
			}
			if (quiz.qlist.get(quizQuestionIndex-1).getOrdered() == false){
				for (int j = 0; j < quiz.qlist.get(quizQuestionIndex-1).getMultAnswers().size(); j++){
					out.println("<p>All Possible Answer: " + quiz.qlist.get(quizQuestionIndex-1).getMultAnswers().get(j).get(0) + "</p>");
				}
			}
		} else if (quiz.qlist.get(quizQuestionIndex-1).getType() == 3 && quiz.qlist.get(quizQuestionIndex-1).getNumAns()> 1){
			for (int j = 0; j < quiz.qlist.get(quizQuestionIndex-1).getOptions().size(); j++){
				String tempResponse = request.getParameter("response"+(quizQuestionIndex-1)+j);
				if (tempResponse != null){ //checked
				out.println("<p>Your Answer: " + tempResponse + "</p>");
				}
			}
				for (int j = 0; j < quiz.qlist.get(quizQuestionIndex-1).getAnswers().size(); j++){
					out.println("<p>Preferred Answer: " + quiz.qlist.get(quizQuestionIndex-1).getAnswers().get(j) + "</p>");
				}
		} else{
		String tempResponse = request.getParameter("response" + (quizQuestionIndex - 1));
		out.println("<p>Your Answer: " + tempResponse + "</p>");
		out.println("<p>Preferred Answer: " + quiz.qlist.get(quizQuestionIndex - 1).possibleAnswers.get(0) + "</p>");
		}
		out.println("<input type=\"submit\" value=\"Next\" />");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

}
