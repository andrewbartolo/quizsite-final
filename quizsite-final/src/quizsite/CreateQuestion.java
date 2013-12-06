package quizsite;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Servlet implementation class CreateQuestion
 */
@WebServlet("/CreateQuestion")
public class CreateQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuestion() {
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
		String finished = request.getParameter("sb");
		Quiz quiz = (Quiz) request.getSession().getAttribute("quiz");
		int type = Integer.parseInt(request.getParameter("questiontype"));
		String mark = request.getParameter("score");
		double score = 0.0;

		if (!mark.isEmpty()){
			try {
				score = Double.parseDouble(request.getParameter("score"));
			}
			catch (NumberFormatException ignored) { }
		}
		if (!mark.isEmpty() && isNumeric(mark)){
			score = Double.parseDouble(request.getParameter("score"));

		}
		List<String> answers = new ArrayList<String>();

		
		//load questionID
		String id = "0";
		try {
			Statement stmt = DatabaseUtils.setUp();
			ResultSet rs = stmt.executeQuery("SELECT max(questionID) FROM questions");
			while(rs.next()){
				id = rs.getString("max(questionID)");
				System.out.println(id);
			}
		} catch (SQLException e) {
					e.printStackTrace();
		} catch (ClassNotFoundException e) {
					e.printStackTrace();
		}
		if (id == null){
			id = "0";
		}
		int questionID = Integer.parseInt(id)+1;

		switch (type){
		case 0: //process question-response-single
			String query = request.getParameter("query");
			String answer = request.getParameter("answer");

			answers = Arrays.asList(answer.split(","));
			QuestionResponse sr = new QuestionResponse(quiz, 0, questionID, query, answers, score, false, 1);

			quiz.AddQuestion(sr);
			sr.toDB();
			break;
			
		case 1: //process question-response-multiple
			query = request.getParameter("query");
			boolean ordered = request.getParameter("ordered").equals("true");
			int numAns = 1;
			if (isNumeric(request.getParameter("numAns"))){
				numAns = Integer.parseInt(request.getParameter("numAns"));
				if (numAns > 20) numAns = 20;
				if (numAns < 1) numAns = 1;
			}
			List<List <String>> multAnsLists = new ArrayList<List<String>>();
			List <String> ansList = new ArrayList<String>();
			
			for (int i = 0; i < 20; i++){
				String ansString = request.getParameter("answer"+String.valueOf(i));
				if (ansString != ""){
				ansList = Arrays.asList(ansString.split(","));
				multAnsLists.add(ansList);
				}
			}

			QuestionResponse mr = new QuestionResponse(quiz, 1, questionID, query, ansList, score,ordered,numAns);
			mr.setMultAnswers(multAnsLists);
			quiz.AddQuestion(mr);
			mr.toDB();
			break;
			
		case 2: //process fill-in-blank 
			String pre = request.getParameter("pre");
			String post = request.getParameter("post");
			answer = request.getParameter("answer");
			answers = Arrays.asList(answer.split(","));
			Blank blank = new Blank(quiz,2,questionID, pre,post,answers,score);
			quiz.AddQuestion(blank);
			blank.toDB();
			break;
			
		case 3: //process multiple-choice 
			query = request.getParameter("query");
			HashMap<String, String> options = new HashMap<String,String>();
			for (int i = 0; i< 4; i++){
				String optElement = request.getParameter("option"+String.valueOf(i));
				if (!optElement.isEmpty()){
					options.put(String.valueOf(i+1),optElement);
				}
			}
			answer = request.getParameter("answer");
			answers = Arrays.asList(answer.split(","));
			
			MultiChoice mc = new MultiChoice(quiz, 3, questionID, query, options, answers,score);
			
			quiz.AddQuestion(mc);
			mc.toDB();
			break;
			
			
		case 4: //process picture-response single answer
			System.out.println(type);
			query = request.getParameter("query");
			answer = request.getParameter("answer");
			answers = Arrays.asList(answer.split(","));
			Picture ps = new Picture(quiz, 4, questionID, query, answers, score);
			quiz.AddQuestion(ps);
			ps.toDB();
			break;
		}
		
		request.getSession().setAttribute("quiz",quiz);

		if (finished.equals("Next Question")){
		RequestDispatcher dispatch = request.getRequestDispatcher("question_types.jsp");
		dispatch.forward(request, response);
		}else{
			RequestDispatcher dispatch = request.getRequestDispatcher("Quiz_creation_summary.jsp");
			dispatch.forward(request, response);
		}
		
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}

}
