package quizsite;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import login.*;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user.html")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("user.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String username = user.getUserName();
		
		String postType = (String)request.getParameter("postType");
		if (postType.equals("searchForUser")) {
			String userToSearchFor = (String)request.getParameter("userToSearchFor");
			request.getRequestDispatcher("user.jsp").forward(request, response);
			
			// fromUser, toUser, type, content, isRead
			// need to check if 'searchForUser' is in database first
			Message m = new Message(username, userToSearchFor, Message.MESSAGE_FRIEND_REQUEST, "", false);
			Message.sendMessage(m);
			
			//System.out.println("Tried to send friend request to user " + userToSearchFor);
			
		}
		else if (postType.equals("sendNote")) {
			String whoseProfile = (String)request.getParameter("whoseProfile");
			String content = (String)request.getParameter("content");
			Message m = new Message(username, whoseProfile, Message.MESSAGE_NOTE, content, false);
			Message.sendMessage(m);
			request.getRequestDispatcher("user.jsp?whoseProfile=" + whoseProfile).forward(request, response);
			System.out.println("sent message w/content " + content + " from " + username + " to " + whoseProfile);
		}
		else {
			System.out.println("UserServlet received spuriuous POST request");
		}

	}

}
