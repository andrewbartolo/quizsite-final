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
 * Servlet implementation class TakeQuizViewMultPage
 */
@WebServlet("/TakeQuizViewMultPage")
public class TakeQuizViewMultPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TakeQuizViewMultPage() {
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
		int quizQuestionIndex = ((Integer) session.getAttribute("quizToTakeIndex")).intValue();
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

		out.println("<h1>" + quiz.getTitle() + "</h1>");
		if (quiz.immCorrect) {
			out.println("<form action=\"TakeQuizViewImmCorr\" method=\"POST\">");
		} else {
			if (quiz.qlist.size() == quizQuestionIndex + 1) {
				out.println("<form action=\"CheckQuizServlet\" method=\"POST\">");
			} else {
				out.println("<form action=\"TakeQuizViewMultPage\" method=\"POST\">");
			}
		}
		if (quiz.qlist.get(quizQuestionIndex) instanceof QuestionResponse) {
			QuestionResponse tempQues = (QuestionResponse) quiz.qlist.get(quizQuestionIndex);
			out.println("<p>" + tempQues.getQuery() + "</p>");
			
			if (tempQues.getNumAns() == 1){
				out.println("<input type=\"text\" name=\"response" + quizQuestionIndex + "\" size=\"50\" value=\"Answer\">");
				} else{
					for (int j = 0; j < tempQues.getNumAns(); j++){
						
						out.println("<p><input type=\"text\" name=\"response" + quizQuestionIndex + j + "\" size=\"50\" value=\"Answer\"></p>");
					}
				}
		} else if (quiz.qlist.get(quizQuestionIndex) instanceof Blank) {
			Blank tempQues = (Blank) quiz.qlist.get(quizQuestionIndex);
			out.println("<p>" + tempQues.getQuery() + "</p>");
			out.println("<input type=\"text\" name=\"response" + quizQuestionIndex + "\" size=\"50\" value=\"Answer\">");
		} else if (quiz.qlist.get(quizQuestionIndex) instanceof Picture) {
			Picture tempQues = (Picture) quiz.qlist.get(quizQuestionIndex);
			out.println("<img src=\"" + tempQues.getQuery() + "\" height = \"300\"/>");
			out.println("<br/><br/>");
			out.println("<input type=\"text\" name=\"response" + quizQuestionIndex + "\" size=\"50\" value=\"Answer\">");
		} else if (quiz.qlist.get(quizQuestionIndex) instanceof MultiChoice) {
			MultiChoice tempQues = (MultiChoice) quiz.qlist.get(quizQuestionIndex);
			out.println("<p>" + tempQues.getQuery() + "</p>");
			System.out.println(tempQues.getNumAns());
			if (tempQues.getNumAns() == 1){
				for (String key : tempQues.getOptions().keySet()) {
					out.println("<input type=\"radio\" name=\"response" + quizQuestionIndex + "\" value=\"" + key + "\">" + tempQues.getOptions().get(key) + "</input>");
				}
			} else {
				out.println("<p> Please choose all that apply: </p>");
				int j = 0;
				for (String key : tempQues.getOptions().keySet()) {
					out.println("<input type=\"checkbox\" name=\"response" + quizQuestionIndex + j + "\" value=\"" + key + "\">" + tempQues.getOptions().get(key) + "</input>");
					j++;
				}
			}
		}
		session.setAttribute("quizToTakeIndex", quizQuestionIndex + 1);
		if (quiz.qlist.size() == quizQuestionIndex + 1) {
			if (quiz.immCorrect) {
				out.println("<input type=\"submit\" value=\"Next\" />");
			} else {
				out.println("<input type=\"submit\" value=\"Submit\" />");
			}
		} else {
			out.println("<input type=\"submit\" value=\"Next\" />");
		}
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Quiz quiz = (Quiz) session.getAttribute("quizToTake");
		int quizQuestionIndex = ((Integer) session.getAttribute("quizToTakeIndex")).intValue();
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
				      + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		out.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		out.println("<title>Quiz " + quiz.getQuizID() + " Question " + quizQuestionIndex + "</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>" + quiz.getTitle() + "</h1>");
		if (quiz.immCorrect) {
			out.println("<form action=\"TakeQuizViewImmCorr\" method=\"POST\">");
		} else {
			if (quiz.qlist.size() == quizQuestionIndex + 1) {
				out.println("<form action=\"CheckQuizServlet\" method=\"POST\">");
			} else {
				out.println("<form action=\"TakeQuizViewMultPage\" method=\"POST\">");
			}
		}
		if (quiz.qlist.get(quizQuestionIndex) instanceof QuestionResponse) {

			QuestionResponse tempQues = (QuestionResponse) quiz.qlist.get(quizQuestionIndex);
			out.println("<p>" + tempQues.getQuery() + "</p>");
			if (tempQues.getNumAns() == 1){
				out.println("<input type=\"text\" name=\"response" + quizQuestionIndex + "\" size=\"50\" value=\"Answer\">");
				} else{
					for (int j = 0; j < tempQues.getNumAns(); j++){
						out.println("<p><input type=\"text\" name=\"response" + quizQuestionIndex + j + "\" size=\"50\" value=\"Answer\"></p>");
					}
				}
			//out.println("<input type=\"text\" name=\"response" + quizQuestionIndex + "\" size=\"50\" value=\"Answer\">");
		} else if (quiz.qlist.get(quizQuestionIndex) instanceof Blank) {
			Blank tempQues = (Blank) quiz.qlist.get(quizQuestionIndex);
			out.println("<p>" + tempQues.getQuery() + "</p>");
			out.println("<input type=\"text\" name=\"response" + quizQuestionIndex + "\" size=\"50\" value=\"Answer\">");
		} else if (quiz.qlist.get(quizQuestionIndex) instanceof Picture) {
			Picture tempQues = (Picture) quiz.qlist.get(quizQuestionIndex);
			out.println("<img src=\"" + tempQues.getQuery() + "\" height = \"300\"/>");
			out.println("<br/><br/>");
			out.println("<input type=\"text\" name=\"response" + quizQuestionIndex + "\" size=\"50\" value=\"Answer\">");
		} else if (quiz.qlist.get(quizQuestionIndex) instanceof MultiChoice) {
			MultiChoice tempQues = (MultiChoice) quiz.qlist.get(quizQuestionIndex);
			out.println("<p>" + tempQues.getQuery() + "</p>");
			
			if (tempQues.getNumAns() == 1){
				for (String key : tempQues.getOptions().keySet()) {
					out.println("<input type=\"radio\" name=\"response" + quizQuestionIndex + "\" value=\"" + key + "\">" + tempQues.getOptions().get(key) + "</input>");
				}
			} else {
				out.println("<p> Please choose all that apply: </p>");
				int j = 0;
				for (String key : tempQues.getOptions().keySet()) {
					out.println("<input type=\"checkbox\" name=\"response" + quizQuestionIndex + j + "\" value=\"" + key + "\">" + tempQues.getOptions().get(key) + "</input>");
					j++;
				}
			}
		}
			
		for (int i = 0; i < quizQuestionIndex; ++i) {
			if (quiz.qlist.get(i).getType() == 1){
				for (int j = 0; j < quiz.qlist.get(i).getNumAns(); j++){
					String tempResponse = request.getParameter("response"+i+j);
					//out.println("<p>" + tempResponse + "</p>");
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
			}	else{
				String tempResponse = request.getParameter("response" + i);
				//out.println("<p>" + tempResponse + "</p>");
				out.println("<input type=\"hidden\" name=\"response" + i + "\" value=\"" + tempResponse + "\" />");
			}
		}
		session.setAttribute("quizToTakeIndex", quizQuestionIndex + 1);
		if (quiz.qlist.size() == quizQuestionIndex + 1) {
			if (quiz.immCorrect) {
				out.println("<input type=\"submit\" value=\"Next\" />");
			} else {
				out.println("<input type=\"submit\" value=\"Submit\" />");
			}
		} else {
			out.println("<input type=\"submit\" value=\"Next\" />");
		}
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}
	
}
