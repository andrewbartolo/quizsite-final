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
<link rel='stylesheet' href='css/bootstrap.css' type='text/css/'>
<title>Picture-response (with single answer)</title>
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
					out.println("<li class='active'><a href='user.jsp'>Your User Profile</a></li>");
					if (user.isAdmin()) out.println("<li><a href='AdminServlet'>Administrator Panel</a></li>");
					out.println("<li><a href='ResponderServlet?action=logout'>Log Out</a></li>");
				}
			%>
		</ul>
	</div>
</div>

<h3>Please fill out the following fields to create a question:</p></h3>

<form action="CreateQuestion" method="post">
<p>Picture Source URL: <input type="text" maxlength="100" name="query" /></p>
<p>Legal Answers (please list all the legal answers, separated by ','): <input type="text" maxlength="200" name="answer" /></p>
<p>Maximum score (please use numbers, e.g. 1): <input type="text" maxlength="4" name="score" /></p>
<input type="submit" name = "sb" value = "Next Question">
<input type="submit" name = "sb" value = "Submit Quiz">
<input name="questiontype" type="hidden" value="4"></input>
</form>


</body>
</html>