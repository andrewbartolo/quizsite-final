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
 * Servlet implementation class Creator
 */
@WebServlet("/CreatorServlet")
public class CreatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreatorServlet() {
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
		// TODO Auto-generated method stub
		//AccountManager am = (AccountManager) getServletContext().getAttribute("AccountManager");
		HttpSession session = request.getSession();
		String account = (String) request.getParameter("account");
		String password = (String) request.getParameter("password");
		int result = AccountManager.createAccount(account, password);
		if (result==-1) {
			RequestDispatcher dispatch = request.getRequestDispatcher("login_account_in_use.html");
			dispatch.forward(request, response); 
		}
		else if (result == -2) {
			//Error on the DB side. Need to address later.
		}
		else {
			User user = new User(account);
			session.setAttribute("user", user);
			RequestDispatcher dispatch = request.getRequestDispatcher("user.jsp");
			dispatch.forward(request, response); 
		}
	}

}
