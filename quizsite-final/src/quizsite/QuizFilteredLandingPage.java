package quizsite;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QuizLandingPage
 */
@WebServlet("/QuizFilteredLandingPage")
public class QuizFilteredLandingPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizFilteredLandingPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
				      + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		out.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		out.println("<title>All Quizzes</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Result Quizzes</h1>");
		out.println("<p>Quizzes available:</p>");
		out.println("<ul>");
		@SuppressWarnings("unchecked")
		ArrayList<Quiz> allQuizzes = (ArrayList<Quiz>) request.getServletContext().getAttribute("targetQuizzes");
		for (Quiz quiz : allQuizzes) {
			out.println("<li>");
			out.println("<form action=\"QuizInfoServlet\" method=\"GET\">");
			out.println("<input name=\"quizID\" type=\"hidden\" value=\"" + quiz.getQuizID() + "\"/>");
			out.println("<ul style=\"list-style: none;\">");
			out.println("<li>");
			out.println("<input type=\"submit\" value=\"" + quiz.getTitle() + "\"/>");
			out.println("</li>");
			out.println("<li>BY: " + quiz.getCreator() + "</li><li>" + quiz.getDescription() + "</li><ul>");
			out.println("</form>");
			out.println("</li>");
		}
		out.println("</ul>");
		out.println("<form action =\"index.jsp\">");
		out.println("<input type=\"submit\" value=\"Back To Homepage\"/>");
		out.println("</form>");
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
