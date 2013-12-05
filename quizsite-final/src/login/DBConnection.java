package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	
	static String account = MyDBInfo.MYSQL_USERNAME; 
	static String password = MyDBInfo.MYSQL_PASSWORD; 
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER; 
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;
	
	private static Statement stmt;
	
	private static Connection con;
	
	
	
	/**
	 * Creates the connection to the database sepecified in the MyDBInfo.java file
	 */
	public static void initDB() {
		//initialize the database
		try { 
			Class.forName("com.mysql.jdbc.Driver"); 
 
			con = DriverManager.getConnection 
					( "jdbc:mysql://" + server, account ,password); 
 
			stmt = con.createStatement(); 
			
			stmt.executeQuery("USE " + database); 
			
		} catch (SQLException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} 
		catch (ClassNotFoundException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} 
	}
	
	/**
	 * Closes the connection created by the current instance
	 */
	public void closeDB() {
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ResultSet getResult(String query) {
		if (stmt == null) {
			initDB();
		}
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	//performs update on the DB. Returns 0 on success, -2 on exceptions.
	public static int updateTable(String query) {
		if (stmt == null) {
			initDB();
		}

		try {
			stmt.executeUpdate(query);
			return 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -2;

	}
	
	public DBConnection() {
		initDB();
	}

}
