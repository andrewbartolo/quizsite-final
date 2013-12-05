package quizsite;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class QuizHomeServlet
 */
@WebServlet("/QuizInfoServlet")
public class QuizInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			Quiz quiz = (Quiz) session.getAttribute("quizToTake");
			ArrayList<Quiz> allQuizzes = (ArrayList<Quiz>) request.getServletContext().getAttribute("allQuizzes");
			int quizid = Integer.parseInt(request.getParameter("quizID"));
			int count = 0;
			while (count < allQuizzes.size()){
				if (Integer.parseInt(allQuizzes.get(count).getQuizID()) == quizid){
					break;
				}
				count++;
			}
			quiz = allQuizzes.get(count);
			Statement stmt = DatabaseUtils.setUp();
			String[] extractFields = {"*"};
			String[] tableNames = {"questions"};
			String[] args = {"quizID = \"" + request.getParameter("quizID") + "\""};
			ResultSet rs = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractFields, tableNames, args));
			
			ArrayList<Question> questionList = new ArrayList<Question>();
			
			ArrayList<String[]> questionInfo = new ArrayList<String[]>();
			while (rs.next()) {
				String[] tempInfo = {rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5)}; 
				questionInfo.add(tempInfo);
			}
			
			Collections.sort(questionInfo, new Comparator<String[]>() {

				@Override
				public int compare(String[] arg0, String[] arg1) {
					// TODO Auto-generated method stub
					return Integer.parseInt(arg0[3]) - Integer.parseInt(arg1[3]);
				}
    	    });
			
			for (String[] info : questionInfo) {
				String[] extractFields2 = {"*"};
				String[] args2 = {"questionID = \"" + info[0] + "\""};
				String[] tableNames2 = {info[1]};
				ResultSet questionText = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractFields2, tableNames2, args2));
				String questionQuery = "";
				String numAnsQuery= "";
				String orderedQuery="";
				while (questionText.next()) {
					questionQuery = questionQuery.concat(questionText.getString(2));
					if (info[1].equals("questionResponseMult")){
						numAnsQuery = numAnsQuery.concat(questionText.getString(3));
						orderedQuery = orderedQuery.concat(questionText.getString(4));
					}
				}
				
				tableNames2[0] = tableNames2[0].concat("Answers");
				ResultSet answerText = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractFields2, tableNames2, args2));
				
				ArrayList<String> tempAnswers = new ArrayList<String>();
				List<List<String> > multAnswers = new ArrayList<List<String>>();
				if (info[1].equals("questionResponseMult")){
				ArrayList<Integer> indexList = new ArrayList<Integer>();
				while (answerText.next()) {
					tempAnswers.add(answerText.getString(2));
					indexList.add(Integer.parseInt(answerText.getString(3)));
				}
				int level = 0;
				ArrayList<String> ansList = new ArrayList<String>();
				for (int i = 0; i<indexList.size(); i++){
						if (indexList.get(i) == level){
							ansList.add(tempAnswers.get(i));
						} else{
							multAnswers.add(ansList);
							ansList = new ArrayList<String>();
							level = indexList.get(i);
							ansList.add(tempAnswers.get(i));
						}
				}
				multAnswers.add(ansList);
				} else {
					while (answerText.next()) {
						tempAnswers.add(answerText.getString(2));
					}
				}
				
				if (info[1].equals("questionResponse")) {
					QuestionResponse tempQues = new QuestionResponse(quiz, 0, Integer.parseInt(info[0]), questionQuery, tempAnswers, Double.parseDouble(info[2]), false, 1);
					questionList.add(tempQues);
				}else if (info[1].equals("questionResponseMult")) {
					QuestionResponse tempQues = new QuestionResponse(quiz, 1, Integer.parseInt(info[0]), questionQuery, tempAnswers, Double.parseDouble(info[2]), Boolean.parseBoolean(orderedQuery), Integer.parseInt(numAnsQuery));
					tempQues.setMultAnswers(multAnswers);
					questionList.add(tempQues);
				}else if (info[1].equals("fillInTheBlanks")) {
					Blank tempQues = new Blank(quiz, 2, Integer.parseInt(info[0]), questionQuery, tempAnswers, Double.parseDouble(info[2]));
					questionList.add(tempQues);
				} else if (info[1].equals("pictureResponse")) {
					Picture tempQues = new Picture(quiz, 4, Integer.parseInt(info[0]),questionQuery, tempAnswers, Double.parseDouble(info[2]));
					questionList.add(tempQues);
				} else if (info[1].equals("multipleChoice")) {
					tableNames2[0] = "multipleChoiceOptions";
					ResultSet optionsText = DatabaseUtils.runSelectQuery(stmt, DatabaseUtils.constructSelectQuery(extractFields2, tableNames2, args2));
					HashMap<String, String> optionsMap = new HashMap<String, String>();
					while(optionsText.next()) {
						optionsMap.put(optionsText.getString(2), optionsText.getString(3));
					}
					MultiChoice tempQues = new MultiChoice(quiz, 3, Integer.parseInt(info[0]), questionQuery, optionsMap, tempAnswers, Double.parseDouble(info[2]));
					questionList.add(tempQues);
				}
			}
			quiz.qlist = questionList;
			session.setAttribute("quizToTake", quiz);
			RequestDispatcher dispatch = request.getRequestDispatcher("QuizInfoView");
			dispatch.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
