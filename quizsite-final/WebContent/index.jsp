<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, login.*, quizsite.*"%>
<%-- Note - the below scriplet establishes username as an *instance* variable. --%>
<% 
	User user = (User)session.getAttribute("user");
	String username = (user != null) ? user.getUserName() : "guest";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' href='css/bootstrap.css' type='text/css/'>
<title>QuizSite</title>
</head>
<body>

<div class='container'>
 <h1>QuizSite</h1>
</div>

<div class='navbar'>
	<div class='navbar-inner'>
		<ul class='nav'>
						<li class='active'><a href='index.jsp'>Home</a></li>
			<li><a href='QuizLandingPage'>Quizzes</a></li>
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

<div class='hero-unit'>
	<h1>Welcome to QuizSite, <%= username %>!</h1>
	<p>Here, you'll find quizzes on a wide range of topics from our many users.
    	Log in to create your own, or take a few existing quizzes and sign up later.</p>
    <a href='QuizLandingPage' class='btn btn-large btn-primary'>Take a Quiz</a>
    <%
    	if (user == null) out.println("<a href='#' class='btn btn-large btn-danger'>Create a Quiz</a>");
    	else out.println("<a href='quiz_setup.jsp' class='btn btn-large btn-success'>Create a Quiz</a>");
    %>

</div>

<div class='recent-quiz'>
	<h1>5 Most Recent Quizzes:</h1>
	<% 
	ArrayList<Quiz> recentQuizzes = (ArrayList<Quiz>) request.getServletContext().getAttribute("recentQuizzes");
	if (recentQuizzes == null) System.out.println("recentQuizzes was null");
	if (recentQuizzes != null) for (Quiz quiz : recentQuizzes) {
			out.println("<div class='quiz'>");
			out.println("<li>");
			out.println("<form action='QuizInfoServlet' method='GET'>");
			out.println("<input name='quizID' type='hidden' value='" + quiz.getQuizID() + "'>");
			out.println("<ul style='list-style: none;'>");
			out.println("<li>");
			out.println("<input type='submit' value='" + quiz.getTitle() + "'>");
			out.println("</li>");
			out.println("</form>");
			out.println("</li>");
			out.println("</div>");
	}
		%>
		
</div>



<div class='row'>
	<div class='span4'>
		<ul class='nav nav-list'>
			<li class='nav-header'>Who are we?</li>
			<li class='active'><a href='#'>Home</a>
			<li><a href='#'>Option 1</a>
			<li><a href='#'>Option 2</a>
			<li><a href='#'>Option 3</a>
			<li><a href='#'>Option 4</a>
			<li class='nav-header'>Our friends</li>
			<li><a href='#'>Google</a>
			<li><a href='#'>Quizlet</a>
			<li><a href='#'>Wikipedia</a>
			<li><a href='#'>Quora</a>
			<li><a href='#'>Jeopardy! Online</a>
		</ul>
	</div>
	<div class='span8'>
		<h3>Some Additional Announcements</h3>
		<p>The site will be going down for maintenance on Thursday, Nov. 28 (Thanksgiving).</p>
		<h3>Some Announcements</h3>
		<p>Are you a quiz-taking champ?  If so, check out the <a href='leaderboards.jsp'>leaderboards</a>
		to see who's the current champ of QuizSite!</p>
	</div>
</div>

<div class="row">
	<div class="span4">
        <h4 class="muted text-center">Meet Our Clients</h4>
        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui.</p>
        <a href="#" class="btn"><i class="icon-user"></i> Our Clients</a>
    </div>
    <div class="span4">
        <h4 class="muted text-center">Know Our Employees</h4>
        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui.</p>
        <a href="#" class="btn btn-success"><i class="icon-star icon-white"></i> Our Employees</a>
    </div>
    <div class="span4">
        <h4 class="muted text-center">Reach Us</h4>
        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus. Etiam porta sem malesuada magna mollis euismod. Donec sed odio dui.</p>
        <a href="#" class="btn btn-info">Contact Us</a>
    </div>
</div>

<hr>
<div class="footer">
	<p>&copy; 2013</p>
</div>

<script src='http://code.jquery.com/jquery-1.10.1.js'></script>
<script src='js/bootstrap.js'></script>
</body>
</html>
