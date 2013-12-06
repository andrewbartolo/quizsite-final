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
<title>Rate & Write Review</title>
</head>
<body>

	<form action="ReviewQuizServlet" method="POST">
		<p>Write Review:</p>
		<textarea rows="10" cols="40" name="review"></textarea>
		<input type="submit" value="Submit Review"/>
	</form>
	
	<a href="QuizInfoView">Cancel</a>

</body>
</html>