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
	
	// warning below is unavoidable, I think... should be fine
	ArrayList<User> allUsers = user.getAllUsers();
	ArrayList<Quiz> allQuizzes = (ArrayList<Quiz>)application.getAttribute("allQuizzes");
	ArrayList<Announcement> allAnnouncements = user.getAllAnnouncements();
	
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' href='css/bootstrap.css' type='text/css'>
<title>Administrator Panel</title>
</head>
<body>
<div class='container'>
 <h1>Quizzap!</h1>
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

<div class='hero-unit'>
	<h1>Administrator Panel</h1>
	<p>With great power comes great responsibility...</p>
</div>

<div class='row'>
	<div class='span6'>
		<!--  need # of users, # of quizzes taken -->
		<h2>Site Statistics</h2>
		<p><i>Total # of users:</i> <b><%= user.getNumUsers() %></b><br><i>Total # of quizzes taken:</i> <b><%= user.getNumQuizTaken() %></b></p>
	</div>
	<div class='span6'>
		<h2>Post an Announcement</h2>
		<form action='AdminServlet?postType=announcement' method='post'>
		<textarea name='announcement' class='form-control' rows='2' style='width: 335px'></textarea>
		<br>
		<input type='submit' value='Post'>
		</form>
	</div>
</div>

<div class='row'>
	<div class='span4'>
		<ul class='nav nav-list'>
			<h4 class="muted text-center">All Quizzes</h4>
			<%
				for (Quiz q : allQuizzes) {
					String str = "<li>" + q.getTitle()
						+ " [<span><a href='AdminServlet?action=clearQuizHistory&quiz=" + q.getQuizID() + "' style='color: #ff9500;'>Clear History</a> | "
					+ "<a href='AdminServlet?action=removeQuiz&quiz=" + q.getQuizID() + "' style='color: red;'>Remove</a>]</li>";
					out.println(str);
				}
			%>
		</ul>
	</div>
	<div class='span4'>
		<ul class='nav nav-list'>
			<h4 class="muted text-center">All Users</h4>
			<%
				// why oh why couldn't we use templating languages... :(
			
				for (User u : allUsers) {
					String changeAdminStatus = (u.isAdmin()) ? "Demote Admin" : "Make Admin";
					
					String str = "<li>" + u.getUserName() + " [";
					str += "<span><a href='AdminServlet?action=changeAdminStatus&user=";
					str += u.getUserName() + "'>" + changeAdminStatus + "</a>";
					str += " | <a href='AdminServlet?action=removeUser&user=";
					str += u.getUserName() + "' style='color: red;'>Remove</a></span>]";
					
					str += "</li>";
					out.println(str);
				}
			%>
		</ul>
	</div>
		<div class='span4'>
		<ul class='nav nav-list'>
			<h4 class="muted text-center">All Announcements</h4>
			<%
				for (int i = allAnnouncements.size() - 1; i >= 0; --i) {
					Announcement a = allAnnouncements.get(i);
					
					Date d = new Date(a.getTime());
					SimpleDateFormat dayFt = new SimpleDateFormat("E M/dd ' at ' h:mm a");
					
					out.println("On " + dayFt.format(d) + ", " + a.getUserName()
							+ " posted:<br>&nbsp;&nbsp;&nbsp;&nbsp;<i>" + a.getContent() + "</i>"
							+ " <br>[<span><a href='AdminServlet?action=removeAnnouncement&announcementToRemove=" + a.getId()
							+ "' style='color: red'>Remove</span></a>]</li><br><br>");
				}
			%>
		</ul>
	</div>
</div>

<hr>
<div class="footer">
	<p>&copy; 2013 The Quizzap! Team.  All rights reserved.</p>
</div>

<script src='http://code.jquery.com/jquery-1.10.1.js'></script>
<script src='js/bootstrap.js'></script>
</body>
</html>