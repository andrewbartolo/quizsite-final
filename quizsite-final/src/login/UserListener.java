package login;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import quizsite.Question;
import quizsite.Quiz;

/**
 * Application Lifecycle Listener implementation class UserListener
 *
 */
@WebListener
public class UserListener implements HttpSessionListener {

    /**
     * Default constructor. 
     */
    public UserListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent event) {
        // TODO Auto-generated method stub
    	event.getSession().setAttribute("user", null);
    	System.out.println("User created");
    	Quiz quiz = new Quiz();
    	event.getSession().setAttribute("quiz", quiz);
    	System.out.println("Quiz created");
    	Question question = new Question();
    	event.getSession().setAttribute("question",question);
    	System.out.println("Session created");
    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        // TODO Auto-generated method stub
    	event.getSession().removeAttribute("user");
    	event.getSession().removeAttribute("quiz");
    	event.getSession().removeAttribute("question");
    }
	
}
