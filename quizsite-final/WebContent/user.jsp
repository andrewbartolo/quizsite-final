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
	// start out by viewing our own profile
	String whoseProfile =  request.getParameter("whoseProfile");
	if (whoseProfile == null) whoseProfile = username;
	
	friends = (new User(whoseProfile)).getFriends();
	
	boolean isOwnProfile = whoseProfile.equals(username);
	
	String owner = (isOwnProfile) ? "Your" : whoseProfile + "'s";
	
	Achievement.checkNewAchievements(whoseProfile);
	ArrayList<Achievement> achievements = Achievement.getAllAchievements(whoseProfile);
	ArrayList<History> history = History.getTakenQuizzes(whoseProfile);
	ArrayList<History> creationHistory = History.getCreatedQuizzes(whoseProfile);
	@SuppressWarnings("unchecked")
	ArrayList<Quiz> quizzes = (ArrayList<Quiz>)application.getAttribute("allQuizzes");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' href='css/bootstrap.css' type='text/css'>
<title><%= owner %> User Profile</title>
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
					out.println("<li class='active'><a href='user.jsp'>Your User Profile</a></li>");
					if (user.isAdmin()) out.println("<li><a href='AdminServlet'>Administrator Panel</a></li>");
					out.println("<li><a href='ResponderServlet?action=logout'>Log Out</a></li>");
				}
			%>
		</ul>
	</div>
</div>

<div class='hero-unit'>
	<%
		if (isOwnProfile) out.println("<h1>Your User Profile (" + username + ")</h1>");
		else {
			out.println("<h1>" + whoseProfile + "'s User Profile</h1>");
			if (user.isFriendWith(whoseProfile)) {
				  String str = "<div class='btn-group'>"
			        + "<button type='button' class='btn btn-large btn-warning dropdown-toggle' data-toggle='dropdown'>"
			        + "Challenge to a Quiz <span class='caret'></span></button>"
			        + "<ul class='dropdown-menu' role='menu'>";
			        
			        for (History h : user.getTakenQuizzes()) {
			        	str += "<li><a href='";
			        	str += "ResponderServlet?action=challenge&userToChallenge=" + whoseProfile;
			        	str += "&quizToChallenge=" + h.getQuizId();
			        	str += "'>" + User.getQuizTitle(h.getQuizId()) + "</a></li>";
			        }
			        
			        str += "</ul>"
			        + "</div>";
				
			    out.println(str);
				out.println("<a href='ResponderServlet?action=removeFriend&friendToRemove=" + whoseProfile
						+ "' class='btn btn-large btn-danger'>Remove Friend</a>");
				out.println("</div>");
			}
			else {
				out.println("<a href='#' class='btn btn-large btn-primary'>Send Friend Request</a>");
			}
		}
	%>    
</div>

<div class='row'>
	<div class='span3'></div>
	<div class='span6'>
	<h3>Send a friend request to someone:</h3>
	<form action='user.html?postType=searchForUser' method='post'>
	<input name='userToSearchFor' type='text' width='30'>
	<input type='submit' class='btn btn-info' value='Send'>
	</form>
	</div>
	<div class='span3'></div>
</div>

<div class='row'>
	<div class='span6'>
		<h3 class='muted'><%= owner %> Achievements</h3>
		<%
			ArrayList<Achievement> reversedAchievements = new ArrayList<Achievement>(achievements);
			Collections.reverse(reversedAchievements);
			
			for (Achievement a : reversedAchievements) {
				Date d = new Date(a.getTimeEarned());
				SimpleDateFormat dayFt = new SimpleDateFormat("E M/dd ' at ' h:mm a");
				
				out.println("On " + dayFt.format(d) + ",<br>" + whoseProfile
						+ " earned achievement <b>" + a.getName() + "</b><br><br>");
			}
			
		%>
	</div>
	<div class='span6'>
		<ul class='nav nav-list'>
			<h3 class='muted'><%= owner %> Friends</h3>
			<%
				// note - will get NPE here w/bad user
				if (friends != null) {
					for (Friend friend : friends) {
						out.write("<li><a href='user.jsp?whoseProfile=" + friend.getUserName() + "'>" + friend.getUserName() + "</a></li>");
					}
				}
			%>
		</ul>
	</div>
</div>

<div class="row">
	<div class="span4">
        <h3 class='muted'><%= owner %> Quizzes Taken</h3>
        <%
			ArrayList<History> reversedHistory= new ArrayList<History>(history);
			Collections.reverse(reversedHistory);
			
			
			
			for (History h : reversedHistory) {
				String quizTitle = null;
				for (Quiz q : quizzes) {
					if (Integer.parseInt(q.getQuizID()) == h.getQuizId()) {
						quizTitle = q.getTitle();
					}
				}
				
				Date d = new Date(h.getEndTime());
				SimpleDateFormat dayFt = new SimpleDateFormat("E M/dd ' at ' h:mm a");
				
				out.println("On " + dayFt.format(d) + ",<br>" + h.getUserName()
						+ " took quiz:<br>&nbsp;&nbsp;&nbsp;&nbsp;<i>" + quizTitle + "</i><br>"
						+ " and scored <b>" + h.getScore() + "</b><br><br>");
			}
			
		%>
    </div>
    <div class='span4'>
		<h3 class='muted'><%= owner %> Quizzes Created</h3>
		        <%
			ArrayList<History> reversedCreationHistory= new ArrayList<History>(creationHistory);
			Collections.reverse(reversedCreationHistory);
			
			
			
			for (History h : reversedCreationHistory) {
				String quizTitle = null;
				for (Quiz q : quizzes) {
					if (Integer.parseInt(q.getQuizID()) == h.getQuizId()) {
						quizTitle = q.getTitle();
					}
				}
				
				Date d = new Date(h.getEndTime());
				SimpleDateFormat dayFt = new SimpleDateFormat("E M/dd ' at ' h:mm a");
				
				out.println(h.getUserName()
						+ " created quiz:<br>&nbsp;&nbsp;&nbsp;&nbsp;<i>" + quizTitle + "</i><br><br>");
			}
			
		%>
	</div>
    <div class="span4">
    <ul class='nav nav-list'>
		
    	<%
    		if (isOwnProfile) {
    			out.println("<h3 class='muted'>Your Inbox</h3>");
    			ArrayList<Message> recd = user.getReceivedMessages();
    			ArrayList<Message> reversedMessages = new ArrayList(recd);
    			Collections.reverse(reversedMessages);
    			for (Message m : reversedMessages) {
    				String str = "<li>";
    				
    				str += "<b>[" + m.getMessageType() + " from " + m.getFromUser() + "]</b> " + m.getContent();
    				// friend request?  if so, offer the user the ability to [Accept | Reject]
    				if (m.getType() == Message.MESSAGE_FRIEND_REQUEST) {
    					str += "<span class=''>";	// squash the default bootstrap formatting
    					str += "[ <a href='ResponderServlet?action=acceptFriendRequest&userWhoSentRequest=" + m.getFromUser() + "' style='color: green; width: 20px;'>Accept</a>";
    					str += " | <a href='ResponderServlet?action=rejectFriendRequest&userWhoSentRequest=" + m.getFromUser() + "' style='color: red; width: 20px;'>Reject</a> ]";
    					str += "</span>";
    					str += "</li><br>";
    					out.write(str);
    				}
    				else if (m.getType() == Message.MESSAGE_CHALLENGE) {
    					String quizIDString = m.getContent().substring((m.getContent().indexOf("?")) + 1);
    					int quizID = Integer.parseInt(quizIDString);
    					
    					System.out.println(quizID);
    					
    					str = str.substring(0, str.indexOf("?") + 1);
    					str += "<span class=''><a href='QuizInfoServlet?quizID=" + quizID + "'> Take the Quiz</a></span>";
    					str += "</li><br>";
    					out.write(str);
    				}
    				else {
    					str += "</li><br>";
    					out.write(str);
    				}
    			}
    		}
    		else {
    			out.println("<h3 class='muted'>Send " + whoseProfile + " a Message</h3>");
    			// user.html redirects *through* the servlet
    			out.println("<form action='user.html?postType=sendNote' method='post'>");
    			// so we can forward dispatch to the same page  			
    			out.println("<input name ='whoseProfile' type='hidden' value='" + whoseProfile + "'>");
    			// erase the filler text on focus
    			out.println("<textarea name='content' class='form-control' rows='4'></textarea>");
    			out.println("<br>");
    			out.println("<input type='submit' value='Send' class='btn'>");
    			out.println("</form>");
	        	
	        	//out.println("<br>");
	        	//out.println("<button type='submit' class='btn btn-default'>Send a note</button>");
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