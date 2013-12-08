package quizsite;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import login.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Servlet implementation class LoadXMLServlet
 */
@WebServlet("/LoadXMLServlet")
public class LoadXMLServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadXMLServlet() {
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
		User user = (User) session.getAttribute("user");
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		out.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
				      + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		out.println("<html xmlns='http://www.w3.org/1999/xhtml'>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		//out.println("<title>Quiz " + quiz.getQuizID() + "</title>");
		out.println("</head>");
		out.println("<body>");
		
		try {
			Statement stmt = DatabaseUtils.setUp();
			String[] fieldsToExtract = {"max(quizID)"};
			String[] tables = {"quizzes"};
			String[] args = {};
			String selectQuery = DatabaseUtils.constructSelectQuery(fieldsToExtract, tables, args);
			ResultSet rs = DatabaseUtils.runSelectQuery(stmt, selectQuery);
			String quizID = "";
			while (rs.next()) {
				quizID = Integer.toString(Integer.parseInt(rs.getString(1)) + 1);
			}
			
			String[] fieldsToExtract2 = {"max(questionID)"};
			String[] tables2 = {"questions"};
			String selectQuery2 = DatabaseUtils.constructSelectQuery(fieldsToExtract2, tables2, args);
			ResultSet rs2 = DatabaseUtils.runSelectQuery(stmt, selectQuery2);
			String questionID = "";
			while (rs2.next()) {
				questionID = rs2.getString(1);
			}
			
			//String workingDirectory = "C://Users/Victoria/workspace/quizsite4/"; // CHANGE DEPENDING ON COMPUTER YOU USE
			File fXmlFile = new File(request.getParameter("fileName"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			if (doc.getDocumentElement().getNodeName().equals("quiz")) {
				String random = doc.getDocumentElement().getAttribute("random");
				if (random.length() == 0)
					random = "false";
				String onepage = doc.getDocumentElement().getAttribute("one-page");
				if (onepage.length() == 0)
					onepage = "false";
				String immcorr = doc.getDocumentElement().getAttribute("immediate-correction");
				if (immcorr.length() == 0)
					immcorr = "false";
				String practice = doc.getDocumentElement().getAttribute("practice-mode");
				if (practice.length() == 0)
					practice = "false";
				String title = doc.getElementsByTagName("title").item(0).getTextContent();
				//String category = doc.getElementsByTagName("category").item(0).getTextContent();
				String description = doc.getElementsByTagName("description").item(0).getTextContent();
				
				NodeList questionList = doc.getElementsByTagName("question");
				int numQuestions = questionList.getLength();
				String[][] questionInsertArgs = new String[numQuestions][5];
				for (int i = 0; i < numQuestions; ++i) {
					Node tempNode = questionList.item(i);
					if (tempNode.getNodeType() == Node.ELEMENT_NODE && tempNode.getNodeName().equals("question")) {
						Element question = (Element) tempNode;
						String questionType = question.getAttribute("type");
						questionInsertArgs[i][0] = Integer.toString(Integer.parseInt(questionID) + 1 + i);
						questionInsertArgs[i][1] = quizID;
						if (questionType.equals("question-response")) {
							questionInsertArgs[i][2] = "questionResponse";
							String[] query = {questionInsertArgs[i][0], question.getElementsByTagName("query").item(0).getTextContent()};
							String insertQuesQuery = DatabaseUtils.constructInsertQuery("questionResponse", query);
							DatabaseUtils.runInsertUpdateQuery(stmt, insertQuesQuery);
							if (question.getElementsByTagName("answer-list").getLength() == 0) {
								String[] answer = {questionInsertArgs[i][0], question.getElementsByTagName("answer").item(0).getTextContent()};
								String insertAnsQuery = DatabaseUtils.constructInsertQuery("questionResponseAnswers", answer);
								DatabaseUtils.runInsertUpdateQuery(stmt, insertAnsQuery);
							} else {
								Element answerList = (Element) question.getElementsByTagName("answer-list").item(0);
								NodeList allAnswers = answerList.getElementsByTagName("answer");
								for (int j = 0; j < allAnswers.getLength(); ++j) {
									Element answer = (Element) allAnswers.item(j);
									String[] answerText = {questionInsertArgs[i][0], answer.getTextContent()};
									String insertAnsQuery = DatabaseUtils.constructInsertQuery("questionResponseAnswers", answerText);
									DatabaseUtils.runInsertUpdateQuery(stmt, insertAnsQuery);
								}
							}
						} else if (questionType.equals("fill-in-blank")) {
							questionInsertArgs[i][2] = "fillInTheBlanks";
							Element entireQuery = (Element) question.getElementsByTagName("blank-query").item(0);
							String[] queryText = {questionInsertArgs[i][0], entireQuery.getElementsByTagName("pre").item(0).getTextContent() + " ___________ " + entireQuery.getElementsByTagName("post").item(0).getTextContent()};
							String insertQuesQuery = DatabaseUtils.constructInsertQuery("fillInTheBlanks", queryText);
							DatabaseUtils.runInsertUpdateQuery(stmt, insertQuesQuery);
							if (question.getElementsByTagName("answer-list").getLength() == 0) {
								String[] answer = {questionInsertArgs[i][0], question.getElementsByTagName("answer").item(0).getTextContent()};
								String insertAnsQuery = DatabaseUtils.constructInsertQuery("fillInTheBlanksAnswers", answer);
								DatabaseUtils.runInsertUpdateQuery(stmt, insertAnsQuery);
							} else {
								Element answerList = (Element) question.getElementsByTagName("answer-list").item(0);
								NodeList allAnswers = answerList.getElementsByTagName("answer");
								for (int j = 0; j < allAnswers.getLength(); ++j) {
									Element answer = (Element) allAnswers.item(j);
									String[] answerText = {questionInsertArgs[i][0], answer.getTextContent()};
									String insertAnsQuery = DatabaseUtils.constructInsertQuery("fillInTheBlanksAnswers", answerText);
									DatabaseUtils.runInsertUpdateQuery(stmt, insertAnsQuery);
								}
							}
						} else if (questionType.equals("multiple-choice")) {
							questionInsertArgs[i][2]= "multipleChoice";
							String[] query = {questionInsertArgs[i][0], question.getElementsByTagName("query").item(0).getTextContent()};
							String insertQuesQuery = DatabaseUtils.constructInsertQuery("multipleChoice", query);
							DatabaseUtils.runInsertUpdateQuery(stmt, insertQuesQuery);
							NodeList optionList = question.getElementsByTagName("option");
							for (int j = 0; j < optionList.getLength(); ++j) {
								Element option = (Element) optionList.item(j);
								String[] optionArgs = {questionInsertArgs[i][0], Integer.toString(j + 1), option.getTextContent()};
								String insertOptionQuery = DatabaseUtils.constructInsertQuery("multipleChoiceOptions", optionArgs);
								DatabaseUtils.runInsertUpdateQuery(stmt, insertOptionQuery);
								if (option.getAttribute("answer").length() > 0) {
									String[] answerArgs = {questionInsertArgs[i][0], Integer.toString(j + 1)};
									String insertAnswerQuery = DatabaseUtils.constructInsertQuery("multipleChoiceAnswers", answerArgs);
									DatabaseUtils.runInsertUpdateQuery(stmt, insertAnswerQuery);
								}
							}
						} else if (questionType.equals("picture-response")) {
							questionInsertArgs[i][2] = "pictureResponse";
							String[] query = {questionInsertArgs[i][0], question.getElementsByTagName("image-location").item(0).getTextContent()};
							String insertQuesQuery = DatabaseUtils.constructInsertQuery("pictureResponse", query);
							DatabaseUtils.runInsertUpdateQuery(stmt, insertQuesQuery);
							if (question.getElementsByTagName("answer-list").getLength() == 0) {
								String[] answer = {questionInsertArgs[i][0], question.getElementsByTagName("answer").item(0).getTextContent()};
								String insertAnsQuery = DatabaseUtils.constructInsertQuery("pictureResponseAnswers", answer);
								DatabaseUtils.runInsertUpdateQuery(stmt, insertAnsQuery);
							} else {
								Element answerList = (Element) question.getElementsByTagName("answer-list").item(0);
								NodeList allAnswers = answerList.getElementsByTagName("answer");
								for (int j = 0; j < allAnswers.getLength(); ++j) {
									Element answer = (Element) allAnswers.item(j);
									String[] answerText = {questionInsertArgs[i][0], answer.getTextContent()};
									String insertAnsQuery = DatabaseUtils.constructInsertQuery("pictureResponseAnswers", answerText);
									DatabaseUtils.runInsertUpdateQuery(stmt, insertAnsQuery);
								}
							}
						}
						questionInsertArgs[i][3] = "1";
						questionInsertArgs[i][4] = Integer.toString(i + 1);
						String questionInsertQuery = DatabaseUtils.constructInsertQuery("questions", questionInsertArgs[i]);
						DatabaseUtils.runInsertUpdateQuery(stmt, questionInsertQuery);
					}
				}
				String[] quizInsertArgs = {quizID, user.getUserName(), title, "Others", description, Integer.toString(numQuestions), random, onepage, immcorr, practice};
				String quizInsertQuery = DatabaseUtils.constructInsertQuery("quizzes", quizInsertArgs);
				DatabaseUtils.runInsertUpdateQuery(stmt, quizInsertQuery);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("A quiz has successfully been created with the xml file.");
		out.println("</br>");
		out.println("<a href=\"RefreshAllQuiz\">Return to All Quizzes</a>");
		out.println("</body>");
		out.println("</html>");
	}

}
