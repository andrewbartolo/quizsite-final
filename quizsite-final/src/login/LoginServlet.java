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
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
//		if(user != null) {
//			RequestDispatcher dispatch = request.getRequestDispatcher("quiz_setup.jsp");
//			dispatch.forward(request, response); 
//		}
//		else {
			//AccountManager am = (AccountManager) getServletContext().getAttribute("AccountManager");
			
		String account = (String) request.getParameter("account");
		String password = (String) request.getParameter("password");
		
		if (AccountManager.checkPwd(account, password)) {
			user = new User(account);
			session.setAttribute("user", user);
			RequestDispatcher dispatch = request.getRequestDispatcher("user.jsp");
			dispatch.forward(request, response); 
		}
		else {
			RequestDispatcher dispatch = request.getRequestDispatcher("login_badpwd.html");
			dispatch.forward(request, response); 
		}
//		}
	}

}
