package login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
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
		String action = (String)request.getParameter("action");
		
		if (action != null) {	// we gotta take special action... lulz
		
			if (action.equals("removeAnnouncement")) {
				int id = Integer.parseInt(request.getParameter("announcementToRemove"));
				System.out.println("removing announcement " + id);
				Announcement.removeAnnouncement(user.getUserName(), id);
			}
			
			if (action.equals("changeAdminStatus")) {
				String userToChange = (String)request.getParameter("user");
				boolean isAdmin = AccountManager.isAdmin(userToChange);
				
				int valueToSet = (isAdmin) ? 0 : 1;
				AccountManager.setAdmin(userToChange, valueToSet);
				
				// avoid NPE when user shouldn't be accessing 
				if (user.getUserName().equals(userToChange)) {
					request.getRequestDispatcher("index.html").forward(request, response);
					return;
				}
			}
			
			if (action.equals("removeUser")) {
				String userToRemove = (String)request.getParameter("user");
				user.removeUser(userToRemove);
				System.out.println("Removing " + userToRemove + " from database...");
				
				// avoid NPE when user shouldn't be accessing 
				if (user.getUserName().equals(userToRemove)) {
					request.getRequestDispatcher("index.html").forward(request, response);
					return;
				}
			}
			
			if (action.equals("removeQuiz")) {
				int quizToRemove = Integer.parseInt((String)request.getParameter("quiz"));
				System.out.println("Attempting to remove quiz #" + quizToRemove);
				
				user.removeQuiz(quizToRemove);
				
				request.getRequestDispatcher("RefreshAllQuiz").forward(request, response);
				return;
			}
			
			
			
			
		}
		
		request.getRequestDispatcher("admin.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// POST means an announcement was created
		// (though, if we wanted, we could use the postType parameter, as it is sent by admin.jsp)
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String announcement = (String)request.getParameter("announcement");
		user.createAnnouncement(announcement);
		request.getRequestDispatcher("admin.jsp").forward(request, response);
	}

}
