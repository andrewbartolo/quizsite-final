package quizsite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseUtils {

	public static Statement setUp() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection
					( "jdbc:mysql://" + MyDBInfo.MYSQL_DATABASE_SERVER, MyDBInfo.MYSQL_USERNAME,MyDBInfo.MYSQL_PASSWORD);
		Statement stmt = con.createStatement();
		stmt.executeQuery("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
		return stmt;
	}
	
	public static String constructInsertQuery(String tableName, String[] args) {
		String insertStatement = "INSERT INTO " + tableName + " VALUES(";
		for (int i = 0; i < args.length; ++i) {
			if (i > 0) {
				insertStatement = insertStatement.concat(",");
			}
			insertStatement = insertStatement.concat("\"" + args[i] + "\"");
		}
		insertStatement = insertStatement.concat(")");
		return insertStatement;
	}
	
	public static String constructSelectQuery(String[] fieldsToExtract, String[] tableNames, String[] args) {
		String selectStatement = "SELECT ";
		for (int i = 0; i < fieldsToExtract.length; ++i) {
			if (i > 0) {
				selectStatement = selectStatement.concat(", ");
			}
			selectStatement = selectStatement.concat(fieldsToExtract[i]);
		}
		selectStatement = selectStatement.concat(" FROM ");
		for (int i = 0; i < tableNames.length; ++i) {
			if (i > 0) {
				selectStatement = selectStatement.concat(", ");
			}
			selectStatement = selectStatement.concat(tableNames[i]);
		}
		if (args.length > 0) {
			selectStatement = selectStatement.concat(" WHERE ");
			for (int i = 0; i < args.length; ++i) {
				if (i > 0) {
					selectStatement = selectStatement.concat(" ");
				}
				selectStatement = selectStatement.concat(args[i]);
			}
		}
		return selectStatement;
	}
	
	public static String constructUpdateQuery(String tableName, String[] setValues, String[] args) {
		String updateStatement = "UPDATE " + tableName + " ";
		updateStatement = updateStatement.concat("SET ");
		for (int i = 0; i < setValues.length; ++i) {
			if (i > 0) {
				updateStatement = updateStatement.concat(", ");
			}
			updateStatement = updateStatement.concat(setValues[i]);
		}
		if (args.length > 0) {
			updateStatement = updateStatement.concat(" WHERE ");
			for (int i = 0; i < args.length; ++i) {
				if (i > 0) {
					updateStatement = updateStatement.concat(" ");
				}
				updateStatement = updateStatement.concat(args[i]);
			}
		}
		return updateStatement;
	}
	
	public static String constructDeleteQuery(String tableName, String[] args) {
		String deleteStatement = "DELETE FROM " + tableName;
		if (args.length > 0) {
			deleteStatement = deleteStatement.concat(" WHERE ");
			for (int i = 0; i < args.length; ++i) {
				if (i > 0) {
					deleteStatement = deleteStatement.concat(" ");
				}
				deleteStatement = deleteStatement.concat(args[i]);
			}
		}
		return deleteStatement;
	}
	
	public static void runInsertUpdateQuery(Statement stmt, String query) throws SQLException {
		stmt.executeUpdate(query);
	}
	
	public static void runDeleteQuery(Statement stmt, String query) throws SQLException {
		stmt.executeUpdate(query);
	}
	
	public static ResultSet runSelectQuery(Statement stmt, String query) throws SQLException {
		return stmt.executeQuery(query);
	}
	
}
