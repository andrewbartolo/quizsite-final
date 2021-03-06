package quizsite;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import login.User;

/**
 * Servlet implementation class CheckQuiz
 */
@WebServlet("/CheckQuizServlet")
public class CheckQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckQuizServlet() {
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
		Quiz quiz = (Quiz) session.getAttribute("quizToTake");
		User user = (User) session.getAttribute("user");
		
		long startTime = ((Long) session.getAttribute("quizStartTime")).longValue();
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		
		SimpleDateFormat df = new SimpleDateFormat("mm 'mins,' ss 'seconds'");
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
				      + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		out.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		out.println("<title>Quiz " + quiz.getQuizID() + " Results</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>" + quiz.getTitle() + "</h1>"); 
		out.println("<h3>" + quiz.getDescription() + "</h3>");
		out.println("<p>Start Time: " + sdf.format(startTime) + "</p>");
		out.println("<p>End Time: " + sdf.format(endTime) + "</p>");
		out.println("<p>Time Elapsed: " + df.format(new Date(elapsedTime)) + "</p>");
		
		int countCorrect = 0;
		String tempResponse = new String();
		
		for (int i = 0; i < quiz.qlist.size(); ++i) {
			List<String> tempResponseList = new ArrayList<String>();
			out.println("<h3>Question " + (i + 1) + "</h3>");
			if (quiz.qlist.get(i).getType() == 1){
				for (int j = 0; j < quiz.qlist.get(i).getNumAns(); j++){
					tempResponse = request.getParameter("response"+i+j);
					tempResponseList.add(tempResponse);
					out.println("<p>Your Answer: " + tempResponse + "</p>");
					if (quiz.qlist.get(i).getOrdered() == true){
						if (!quiz.qlist.get(i).getMultAnswers().get(j).isEmpty()){
					out.println("<p>Preferred Answer: " + quiz.qlist.get(i).getMultAnswers().get(j).get(0) + "</p>");
						}
					}
				}
				if (quiz.qlist.get(i).getOrdered() == false){
					for (int j = 0; j < quiz.qlist.get(i).getMultAnswers().size(); j++){
						if (!quiz.qlist.get(i).getMultAnswers().get(j).isEmpty()){
						out.println("<p>All Possible Answer: " + quiz.qlist.get(i).getMultAnswers().get(j).get(0) + "</p>");
						}
					}
				}
			} else if (quiz.qlist.get(i).getType() == 3 && quiz.qlist.get(i).getNumAns()> 1){
				for (int j = 0; j < quiz.qlist.get(i).getOptions().size(); j++){
					tempResponse = request.getParameter("response"+i+j);
					if (tempResponse != null){ //checked
					tempResponseList.add(tempResponse);
					out.println("<p>Your Answer: " + tempResponse + "</p>");
					}
				}
					for (int j = 0; j < quiz.qlist.get(i).getAnswers().size(); j++){
						out.println("<p>Preferred Answer: " + quiz.qlist.get(i).getAnswers().get(j) + "</p>");
					}
			} else{
			tempResponse = request.getParameter("response" + i);
			out.println("<p>Your Answer: " + tempResponse + "</p>");
			if (!quiz.qlist.get(i).possibleAnswers.isEmpty()){
			out.println("<p>Preferred Answer: " + quiz.qlist.get(i).possibleAnswers.get(0) + "</p>");
			}
			tempResponseList.add(tempResponse);
			}
			if (quiz.qlist.get(i).checkAnswer(tempResponseList)){
				countCorrect++;
			}
			out.println("<p>Your Grade: " + quiz.qlist.get(i).getGrade() + "/" + quiz.qlist.get(i).getScore()+ "</p>");
			}
		
		out.println("<h3>Final Score: " + quiz.getTotalGrade() + "/" + quiz.getTotalScore() + "</h3>"); // TODO: FIX JOSH's SCORE 
		out.println("<form action=\"QuizLandingPage\" method=\"GET\">");
		out.println("<input type=\"submit\" value=\"Go back to all Quizzes\"/>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		
		try {
			Statement stmt = DatabaseUtils.setUp();
			String[] insertArgs = {user.getUserName(), quiz.getQuizID(), Double.toString(quiz.getTotalGrade()), Long.toString(endTime), Long.toString(elapsedTime)};
			String insertQuery = DatabaseUtils.constructInsertQuery("QuizHistory", insertArgs);
			DatabaseUtils.runInsertUpdateQuery(stmt, insertQuery);
		} catch (SQLException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
