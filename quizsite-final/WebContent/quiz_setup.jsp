<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, login.*, java.text.*, quizsite.Quiz"%>
<%-- Note - the below scriplet establishes username as an *instance* variable. --%>
<% 
	User user = (User)session.getAttribute("user");
	String username;
	List<Friend> friends;
	if (user != null) {
		username = user.getUserName();
	}
	else {
		username = "guest";
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' href='css/bootstrap.css' type='text/css'>
<title>Create a Quiz</title>
</head>
<body>

<div class='container'>
 <h1>QuizSite</h1>
</div>

<div class='navbar'>
	<div class='navbar-inner'>
		<ul class='nav'>
			<li><a href='index.jsp'>Home</a></li>
			<li class='active'><a href='QuizLandingPage'>Quizzes</a></li>
			<li><a href='leaderboard.jsp'>Leaderboard</a></li>
			<%
				if (user == null) out.println("<li><a href='login_home.html'>Log In</a></li>");
				else {
					out.println("<li><a href='user.jsp'>Your User Profile</a></li>");
					if (user.isAdmin()) out.println("<li><a href='AdminServlet'>Administrator Panel</a></li>");
					out.println("<li><a href='ResponderServlet?action=logout'>Log Out</a></li>");
				}
			%>
		</ul>
	</div>
</div>

<h1>Welcome to Quiz Creating Center</h1>

<form action="load_xml.html">
<input type="submit" value="Load Quiz from XML File!"/>
</form>

<h3>Please fill out the following fields to create a new quiz:</p></h3>

<form action="CreateQuiz" method="post">
<p>Title (the title of your quiz): <input type="text" maxlength="100" name="title" /></p>
<p>Description (a short description of the quiz content):  <input type="text" maxlength="200" name="description" /></p>
<p>Please specify the category of the quiz: </p>
<select name="category">
<option value="history">History</option>
<option value="geography">Geography</option>
<option value="science">Science</option>
<option value="arts">Arts</option>
<option value="sports">Sports</option>
<option value="others">Others</option>
</select>
<p>Tags (please list all the tags, separated by ','): <input type="text" maxlength="50" name="tags" /></p>
<p>Do you want to randomize the order of questions? </p>
<input type="radio" name="random" value="true">Yes<br>
<input type="radio" name="random" value="false" checked>No<br>

<p>Do you prefer all the questions appear on a single webpage? </p>
<input type="radio" name="onepage" value="true">Yes<br>
<input type="radio" name="onepage" value="false" checked>No<br>

<p>Do you want Immediate Feedback on an answer for the quiz taker (Only effective when questions appear in multiple pages)? </p>
<input type="radio" name="immCorrect" value="true">Yes<br>
<input type="radio" name="immCorrect" value="false" checked>No<br>

<p>Do you allow this quiz to be taken in practice mode? </p>
<input type="radio" name="practice" value="true">Yes<br>
<input type="radio" name="practice" value="false" checked>No<br>

</input>
<p>
<input type="submit" value = "Submit" /></p>
</form>

<p><a href="index.jsp">Back to Homepage</a></p>
</body>
</html>
