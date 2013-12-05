<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, login.*, quizsite.Quiz, java.text.*"%>
<%-- Note - the below scriplet establishes username as an *instance* variable. --%>
<% 
	User user = (User)session.getAttribute("user");
	String username; 	// not used as-is; left in place for future use
	if (user != null) {
		username = user.getUserName();
	}
	else {
		username = "guest";
	}
	
	ArrayList<String[]> leaderboardContents = user.getLeaderboardContents();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' href='css/bootstrap.css' type='text/css/'>
<title>Leaderboard</title>
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
			<li class='active'><a href='leaderboard.jsp'>Leaderboard</a></li>
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

<div class='hero-unit'>
	<h1>Leaderboard</h1>
	<p>The best of the best...</p>
</div>

<div class='row'>
	<div class='span12' style='text-align: center;'>
		<!--  need # of users, # of quizzes taken -->
		<h4 class="muted text-center">Best Scores on Quizzes</h4>
		<%
			for (String[] sArray : leaderboardContents) {
				out.println(" [" + sArray[0] + " | " + sArray[1] + "]<br>");
			}
		%>
	</div>
</div>


<div class="footer">
	<p>&copy; 2013 The QuizSite Team.  All rights reserved.</p>
</div>

<script src='http://code.jquery.com/jquery-1.10.1.js'></script>
<script src='js/bootstrap.js'></script>
</body>
</html>