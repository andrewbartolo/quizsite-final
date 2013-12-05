package quizsite;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import login.User;

/**
 * Servlet implementation class QuizInfoView
 */
@WebServlet("/QuizInfoView")
public class QuizInfoView extends HttpServlet {
        private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizInfoView() {
        super();
        // TODO Auto-generated constructor stub
    }

        /**
         * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
         */
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                HttpSession session = request.getSession();
                Quiz quiz = (Quiz) session.getAttribute("quizToTake");
                
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                
                User user_ = (User)request.getSession().getAttribute("user");
        		out.println("<!DOCTYPE html>");
        		out.println("<html>");
        		out.println("<head>");
        		out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        		out.println("<link rel='stylesheet' href='css/bootstrap.css' type='text/css/'>");
        		//out.println("<link rel='stylesheet' href='style/QuizLandingPage.css'>");
        		out.println("<title>Quiz " + quiz.getTitle() + "</title>");
        		out.println("</head>");
        		out.println("<body>");
        		
        	    out.println("<div class='container'>");
        	    out.println("<h1>QuizSite</h1>");
        	    out.println("</div>");
        	    
        	    out.println("<div class='navbar'>");
            	out.println("<div class='navbar-inner'>");
            	out.println("<ul class='nav'>");
            	out.println("<li><a href='index.jsp'>Home</a></li>");
            	out.println("<li class='active'><a href='QuizLandingPage'>Quizzes</a></li>");
            	out.println("<li><a href='leaderboard.jsp'>Leaderboard</a></li>");
            	if (user_ == null) out.println("<li><a href='login_home.html'>Log In</a></li>");
            	else {
            		out.println("<li><a href='user.jsp'>Your User Profile</a></li>");
            		if (user_.isAdmin()) out.println("<li><a href='AdminServlet'>Administrator Panel</a></li>");
            		out.println("<li><a href='ResponderServlet?action=logout'>Log Out</a></li>");
            	}
            	out.println("</ul>");
            	out.println("</div>");
            	out.println("</div>");
              
                out.println("<h1>" + quiz.getTitle() + "</h1>");
                out.println("<h3>" + quiz.getDescription() + "</h3>");
                out.println("<p>Created by: " + quiz.getCreator() + "</p>");
                
                if (quiz.random)
                        Collections.shuffle(quiz.qlist);
                
                boolean hasTakenQuiz = false;
                try {
                        Statement stmt = DatabaseUtils.setUp();
                        User user = (User) session.getAttribute("user");
                        String[] fieldsToExtract = {"*"};
                        String[] tableNames0 = {"inappropriate"};
                        String[] args0 = {"quizID = \"" + quiz.getQuizID() + "\"", "and", "username = \"" + user.getUserName() + "\""};
                        String selectQuery0 = DatabaseUtils.constructSelectQuery(fieldsToExtract, tableNames0, args0);
                        ResultSet rs0 = DatabaseUtils.runSelectQuery(stmt, selectQuery0);
                        boolean markedInappropro = false;
                        while(rs0.next()) {
                                markedInappropro = true;
                        }
                        if (markedInappropro) {
                                out.println("<button type=\"button\" disabled style=\"color:red;\">Marked as Inappropriate</button>");
                        } else {
                                out.println("<form action=\"ReportQuizServlet\" method=\"POST\">");
                                out.println("<input type=\"submit\" value=\"Mark as Inappropriate\" />");
                                out.println("</form>");
                        }
                        
                        
                        SimpleDateFormat df = new SimpleDateFormat("mm 'mins,' ss 'seconds'");
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");
                        String[] tableNames = {"QuizHistory"};
                        String[] args = {"quizID = \"" + quiz.getQuizID() + "\"", "and", "username = \"" + user.getUserName() + "\""};
                        String selectQuery = DatabaseUtils.constructSelectQuery(fieldsToExtract, tableNames, args);
                        selectQuery = selectQuery.concat(" order by endTime, score, timeElapsed desc");
                        ResultSet rs = DatabaseUtils.runSelectQuery(stmt, selectQuery);
                        out.println("<div class=\"results\">");
                        out.println("<h3>Your Past Results</h3>");
                        out.println("<table style=\"border:1px solid black; border-collapse:collapse;\">");
                        out.println("<th>Score</th>");
                        out.println("<th>End Time</th>");
                        out.println("<th>Time Elapsed</th>");
                        while (rs.next()) {
                                hasTakenQuiz = true;
                                out.println("<tr>");
                                out.println("<td>");
                                out.println(rs.getString(3));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(sdf.format(new Long(rs.getString(4))));
                                out.println("<td>");
                                out.println(df.format(new Long(rs.getString(5))));
                                out.println("</td>");
                                out.println("</tr>");
                        }
                        out.println("</table>");
                        out.println("</div>");
                        
                        String[] args2 = {"quizID = \"" + quiz.getQuizID() + "\""};
                        String selectQuery2 = DatabaseUtils.constructSelectQuery(fieldsToExtract, tableNames, args2);
                        selectQuery2 = selectQuery2.concat(" order by score, timeElapsed desc limit 5");
                        ResultSet rs2 = DatabaseUtils.runSelectQuery(stmt, selectQuery2);
                        out.println("<div class=\"results\">");
                        out.println("<h3>Top 5 Results</h3>");
                        out.println("<table style=\"border:1px solid black; border-collapse:collapse;\">");
                        out.println("<th>Username</th>");
                        out.println("<th>Score</th>");
                        out.println("<th>End Time</th>");
                        out.println("<th>Time Elapsed</th>");
                        while (rs2.next()) {
                                out.println("<tr>");
                                out.println("<td>");
                                out.println(rs2.getString(1));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs2.getString(3));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(sdf.format(new Long(rs2.getString(4))));
                                out.println("<td>");
                                out.println(df.format(new Long(rs2.getString(5))));
                                out.println("</td>");
                                out.println("</tr>");
                        }
                        out.println("</table>");
                        out.println("</div>");
                        
                        long millisecondsInDay = 86400000;
                        //long millisecondsInDay = 3600000; // really an hour (only for testing)
                        long oneDayAgo = System.currentTimeMillis() - millisecondsInDay;
                        String[] args3 = {"quizID = \"" + quiz.getQuizID() + "\"", "and", "endTime > \"" + oneDayAgo + "\""};
                        String selectQuery3 = DatabaseUtils.constructSelectQuery(fieldsToExtract, tableNames, args3);
                        selectQuery3 = selectQuery3.concat(" order by score, timeElapsed desc limit 5");
                        ResultSet rs3 = DatabaseUtils.runSelectQuery(stmt, selectQuery3);
                        out.println("<div class=\"results\">");
                        out.println("<h3>Top 5 Results in the Past 24 Hours</h3>");
                        out.println("<table style=\"border:1px solid black; border-collapse:collapse;\">");
                        out.println("<th>Username</th>");
                        out.println("<th>Score</th>");
                        out.println("<th>End Time</th>");
                        out.println("<th>Time Elapsed</th>");
                        while (rs3.next()) {
                                out.println("<tr>");
                                out.println("<td>");
                                out.println(rs3.getString(1));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs3.getString(3));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(sdf.format(new Long(rs3.getString(4))));
                                out.println("<td>");
                                out.println(df.format(new Long(rs3.getString(5))));
                                out.println("</td>");
                                out.println("</tr>");
                        }
                        out.println("</table>");
                        out.println("</div>");
                        
                        long millisecondsIn12Hours = 43200000;
                        long halfDayAgo = System.currentTimeMillis() - millisecondsIn12Hours;
                        String[] args4 = {"quizID = \"" + quiz.getQuizID() + "\"", "and", "endTime > \"" + halfDayAgo + "\""};
                        String selectQuery4 = DatabaseUtils.constructSelectQuery(fieldsToExtract, tableNames, args4);
                        selectQuery4 = selectQuery4.concat(" order by endTime desc, score, timeElapsed");
                        ResultSet rs4 = DatabaseUtils.runSelectQuery(stmt, selectQuery4);
                        out.println("<div class=\"results\">");
                        out.println("<h3>Results in the Past 12 Hours</h3>");
                        out.println("<table style=\"border:1px solid black; border-collapse:collapse;\">");
                        out.println("<th>Username</th>");
                        out.println("<th>Score</th>");
                        out.println("<th>End Time</th>");
                        out.println("<th>Time Elapsed</th>");
                        while (rs4.next()) {
                                out.println("<tr>");
                                out.println("<td>");
                                out.println(rs4.getString(1));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(rs4.getString(3));
                                out.println("</td>");
                                out.println("<td>");
                                out.println(sdf.format(new Long(rs4.getString(4))));
                                out.println("<td>");
                                out.println(df.format(new Long(rs4.getString(5))));
                                out.println("</td>");
                                out.println("</tr>");
                        }
                        out.println("</table>");
                        out.println("</div>");
                        
                } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                } catch (SQLException e) {
                        e.printStackTrace();
                }
                
                out.println("There are " + quiz.qlist.size() + " questions in this quiz.");
                if (quiz.qlist.size() > 0){
        			if (quiz.onepage) {
        				out.println("<form action=\"TakeQuizView\" method=\"GET\">");
        				out.println("<input type=\"submit\" value=\"Begin Quiz!\" />");
        				out.println("</form>");
        			} else {
        				session.setAttribute("quizToTakeIndex", 0);
        				out.println("<form action=\"TakeQuizViewMultPage\" method=\"GET\">");
        				out.println("<input type=\"submit\" value=\"Begin Quiz!\" />");
        				out.println("</form>");
        			}
        		} else {
        			out.println("<p>Whoops, this quiz has no questions :)</p>");
        			out.println("<form action=\"QuizLandingPage\" method=\"GET\">");
        			out.println("<input type=\"submit\" value=\"Go Back\" />");
        			out.println("</form>");
        		}
                
                if (hasTakenQuiz) {
                        
                        out.println("<form action=\"RateQuizServlet\" method=\"POST\">");
                        session.setAttribute("quizToRate", quiz);
                        out.println("<p style=\"margin-bottom:-15px;\">Rating:</p>"); 
                        out.println("<p class=\"stars\">");
                        out.println("<span>");
                        out.println("<input type=\"image\" name=\"1\" class=\"star-1\"></input>");
                        out.println("<input type=\"image\" name=\"2\" class=\"star-2\"></input>");
                        out.println("<input type=\"image\" name=\"3\" class=\"star-3\"></input>");
                        out.println("<input type=\"image\" name=\"4\" class=\"star-4\"></input>");
                        out.println("<input type=\"image\" name=\"5\" class=\"star-5\"></input>");
                                                        
                        out.println("</span>");
                        out.println("</p>");
                        out.println("</form>");
                        
                        out.println("<form action=\"write_review.jsp\" method=\"GET\">");
                        out.println("<input type=\"submit\" value=\"Write Review\" />");
                        out.println("</form>");
                        
                }
                
                out.println("</body>");
                out.println("</html>"); 
        }

        /**
         * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
         */
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                // TODO Auto-generated method stub
        }

}