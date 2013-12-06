<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, login.*, quizsite.*, java.text.*"%>
<%-- Note - the below scriplet establishes username as an *instance* variable. --%>
<% 
	User user = (User)session.getAttribute("user");
	String username = (user != null) ? user.getUserName() : "guest";
	ArrayList<Announcement> announcements = (new User("")).getAllAnnouncements();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' href='css/bootstrap.css' type='text/css'>
<title>Quizzap!</title>
</head>
<body>

<div class='container'>
 <h1>Quizzap!</h1>
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
	<h1>Welcome to Quizzap!, <%= username %>!</h1>
	<p>Here, you'll find quizzes on a wide range of topics from our many users.
    	Log in to create your own, or take a few existing quizzes and sign up later.</p>
    <a href='QuizLandingPage' class='btn btn-large btn-primary'>Take a Quiz</a>
    <%
    	if (user == null) out.println("<a href='#' class='btn btn-large btn-danger'>Create a Quiz</a>");
    	else out.println("<a href='quiz_setup.jsp' class='btn btn-large btn-success'>Create a Quiz</a>");
    %>
</div>

<div class='row'>
	<div class='span4'>
	<h3 class="muted text-center">Most Recent Quizzes</h3>
	<% 
	ArrayList<Quiz> recentQuizzes = (ArrayList<Quiz>) request.getServletContext().getAttribute("recentQuizzes");
	if (recentQuizzes == null) System.out.println("recentQuizzes was null");
	if (recentQuizzes != null) for (Quiz quiz : recentQuizzes) {
			out.println("<div class='quiz'>");
			//out.println("<li>");
			if (user != null) out.println("<form action='QuizInfoServlet' method='GET'>");
			out.println("<input name='quizID' type='hidden' value='" + quiz.getQuizID() + "'>");
			out.println("<ul style='list-style: none;'>");
			out.println("<li>");

			if (user == null) {
				out.println("<button type=\"button\" disabled>" + quiz.getTitle() + "</button>");
			} else {
				out.println("<input type=\"submit\" value=\"" + quiz.getTitle() + "\"/>");
			}

			out.println("</div>");
	}
		%>
	</div>
	<div class='span4'>
	<h3 class="muted text-center">Most Popular Quizzes</h3>
	<%
		ArrayList<String[]> quizStats = User.getQuizTakenTimes();
		

		for (int i = 0; i < Math.min(5, quizStats.size()); ++i) {
			String[] iArray = quizStats.get(i);
			out.println(iArray[1] + " - Taken <b>" + iArray[0] + "</b> times <br><br>");
			
			
		}
	%>
	</div>
	<div class='span4'>
	<h3 class="muted text-center">Announcements</h3>
		<%
		for (int i = announcements.size() - 1; i >= 0; --i) {
			Announcement a = announcements.get(i);
			
			Date d = new Date(a.getTime());
			SimpleDateFormat dayFt = new SimpleDateFormat("E M/dd ' at ' h:mm a");
			
			String str = "On " + dayFt.format(d) + ", ";
			if (user != null) str += "<a href='user.jsp?whoseProfile=" + a.getUserName() + "'>";
			
			str += a.getUserName();
			if (user != null) str += "</a>";
			str += " posted:<br>&nbsp;&nbsp;&nbsp;&nbsp;<i>" + a.getContent() + "</i>"
			+ " <br><br>";
			
			out.println(str);
		}
		%>
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
