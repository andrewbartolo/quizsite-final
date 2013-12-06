<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, java.sql.*, quizsite.*, login.*"%>
<%
	User user = (User)session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' href='css/bootstrap.css' type='text/css'>
<title>Summary Page for Quiz Creation</title>
</head>
<body>
<div class='container'>
 <h1>QuizSite</h1>
</div>

<div class='navbar'>
	<div class='navbar-inner'>
		<ul class='nav'>
			<li><a href='index.jsp'>Home</a></li>
			<li><a href='QuizLandingPage'>Quizzes</a></li>
			<li><a href='leaderboard.jsp'>Leaderboard</a></li>
			<%
				if (user == null) out.println("<li><a href='login_home.html'>Log In</a></li>");
				else {
					out.println("<li><a href='user.jsp'>Your User Profile</a></li>");
					if (user.isAdmin()) out.println("<li class='active'><a href='AdminServlet'>Administrator Panel</a></li>");
					out.println("<li><a href='ResponderServlet?action=logout'>Log Out</a></li>");
				}
			%>
		</ul>
	</div>
</div>
<% Quiz quiz = (Quiz) request.getSession().getAttribute("quiz");
   quiz.toQuizDB();
   quiz.toTagDB();
   String title = quiz.getTitle();
   String description = quiz.getDescription();
   String category = quiz.getCategory();
   List <String> tags = quiz.getTags();
   String random = quiz.getRandom() ? "Yes" : "No";
   String onepage = quiz.getOnepage() ? "Yes" : "No";
   String immCorrect = quiz.getImmCorrect() ? "Yes" : "No";
   String practice = quiz.getPractice() ? "Yes" : "No";
   List <Question> qlist = quiz.getQuestionList();
%>

<h1>Quiz Summary</h1>
<h3>Title: <%=title %></h3>
<p>Description:</p>
<p><%=description %></p>
<h3>Category: <%=category %></h3>
<p>All tags:</p>
<% 
for (int j = 0; j < tags.size(); j++){
String tag = tags.get(j);
%>
<p><%=tag %></p>
<%} %>
<p>Total Marks:<%=quiz.getTotalScore() %></p>
<p>Do you want to randomize the order of questions? <%=random %></p>
<p>Do you prefer all the questions appear on a single webpage? <%=onepage %> </p>
<p>Do you want Immediate Feedback on an answer for the quiz taker (Only effective when questions appear in multiple pages)? <%=immCorrect %></p>
<p>Do you allow this quiz to be taken in practice mode? <%=practice %></p>

<ol>
<%
 	for (int i = 0; i<qlist.size(); i++){
 		Question question = qlist.get(i);
 		//question.toDB();
 		int type = question.getType();
 		String query = question.getQuery();
 		List <String> possibleAnswers = question.getAnswers();
 		String score = String.valueOf(question.getScore());
		if (type == 0){
 			%>
 			<li>
 			<p>Question Type: Question-Response (single answer)</p>
 			<p>Query: <%=query %></p>
 			<p>Maximum score: <%=score %></p>
 			<p>All legal Answers:</p>
 			<% 
 			for (int j = 0; j < possibleAnswers.size(); j++){
 				String ansElement = possibleAnswers.get(j);
 			%>
 			<p><%=ansElement %></p>
 			<%}
			}%>
 			</li>
 		<%
 		  if (type == 1){
 			String ordered = question.getOrdered() ? "Yes" : "No";
 			String numAns = String.valueOf(question.getNumAns());
 			List <List <String>> possibleMultAnswers = question.getMultAnswers();
 			%>
 			<li>
 			<p>Question Type: Question-Response (multiple answer)</p>
 			<p>Query: <%=query %></p>
 			<p>Maximum score: <%=score %></p>
 			<p>Does the responses need to be in a particular order? <%=ordered %> </p>
 			<p>Total number of required responses: <%=numAns %></p>
 			<p>All legal Answers:</p>
 			<% 
 			
 			for (int k = 0; k < possibleMultAnswers.size(); k++){
 				for (int j = 0; j< possibleMultAnswers.get(k).size();j++){
 				String ansElement = possibleMultAnswers.get(k).get(j);
 			%>
 			<p><%=ansElement %></p>
 			<%}
 			}
 			}%>
 			</li>
 		<%
 			if (type == 2){
 			%>
 			<li>
 			<p>Question Type: Fill-the-blank</p>
 			<p>Query: <%=query %></p>
 			<p>Maximum score: <%=score %></p>
 			<p>All legal Answers:</p>
 			<% 
 			for (int j = 0; j < possibleAnswers.size(); j++){
 				String ansElement = possibleAnswers.get(j);
 			%>
 			<p><%=ansElement %></p>
 			<%}
 			}%>
 			</li>
 		<%
 			if (type == 3){
 			Map<String,String> options = question.getOptions();
 			%>
 			<li>
 			<p>Question Type: Multiple Choice</p>
 			<p>Query: <%=query %></p>
 			<p>Maximum score: <%=score %></p>
 				<ol>
 				<% for (int j = 0; j < options.size(); j++){
 					String key = String.valueOf(j+1);
 					%>
 				<li>
 				<p><%=options.get(key) %></p>
 				</li>
 			<%}%>
 				</ol>
 			<p>All legal Answers:</p>
 			<% 
 			for (int j = 0; j < possibleAnswers.size(); j++){
 				String ansElement = possibleAnswers.get(j);
 			%>
 			<p><%=ansElement %></p>
 			<%}
 			}%>
 			</li>
 		<%
 		if (type == 4){
 		%>
 		 	<li>
 			<p>Question Type: Picture-Response (single answer)</p>
 			<p>Image Source URL: <%=query %></p>
 			<p>Maximum score: <%=score %></p>
 			<p>All legal Answers:</p>
 			<% 
 			for (int j = 0; j < possibleAnswers.size(); j++){
 				String ansElement = possibleAnswers.get(j);
 			%>
 			<p><%=ansElement %></p>
 			<%}
 			}%>
 			</li>
 			<%
 	}
%>
</ol>
<form action="RefreshAllQuiz" method="post">
<input type="submit" value="Finish">
</form>

</body>
</html>