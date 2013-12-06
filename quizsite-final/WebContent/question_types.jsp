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
<title>Please choose the following question type</title>
</head>
<body>

<div class='container'>
 <h1>Quizzap!</h1>
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
<body>


<h3>Please choose the following question type</h3>
<ul>
<li><p><a href="Question_Response_single.jsp">Question-Response (with single answer)</a></p></li>
<li><p><a href="Question_Response_mult.jsp">Question-Response (with multiple answers)</a></p></li>
<li><p><a href="Fill_blank.jsp">Fill in the Blank</a></p></li>
<li><p><a href="Multiple_choice.jsp">Multiple Choice (with one or more answers)</a></p></li>
<li><p><a href="Picture_Response_single.jsp">Picture-Response</a></p></li>
</ul>
<button onclick="location.href = 'Quiz_creation_summary.jsp';" class="float-left submit-button" >Submit Quiz</button>
</body>
</html>