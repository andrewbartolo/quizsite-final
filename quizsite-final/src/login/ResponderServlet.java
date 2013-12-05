package login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/ResponderServlet")
public class ResponderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResponderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		// TODO - NPE here... fix
		String username = user.getUserName();
		String action = (String)request.getParameter("action");
		
		RequestDispatcher dispatch = null;
		
		if (action.equals("logout")) {
			session.setAttribute("user", null);
			dispatch = request.getRequestDispatcher("index.jsp");
		}
		else if (action.equals("removeFriend")) {
			String friendToRemove = (String)request.getParameter("friendToRemove");
			user.removeFriend(friendToRemove);
			dispatch = request.getRequestDispatcher("user.jsp?whoseProfile=" + friendToRemove);
		}
		else if (action.equals("acceptFriendRequest")) {
			String userWhoSentRequest = (String)request.getParameter("userWhoSentRequest");
			user.acceptFriend(userWhoSentRequest, "");
			dispatch = request.getRequestDispatcher("user.jsp");
		}
		else if (action.equals("rejectFriendRequest")) {
			String userWhoSentRequest = (String)request.getParameter("userWhoSentRequest");
			
			// fromUser, toUser, type, content, isRead
			Message m = new Message(username, userWhoSentRequest, Message.MESSAGE_FRIEND_REJECT, "", false);
			Message.sendMessage(m);
			dispatch = request.getRequestDispatcher("user.jsp");
		}
		
		
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String textArea = (String)request.getParameter("ta");
//		String whoseProfile = (String)request.getParameter("whoseProfile");
//		System.out.println(textArea);
//		
//		RequestDispatcher dispatch = request.getRequestDispatcher("user.jsp?whoseProfile=" + whoseProfile);
//		dispatch.forward(request, response);
		
	}

}
