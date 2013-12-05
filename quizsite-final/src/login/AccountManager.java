package login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AccountManager {
	
	public AccountManager() {

	}
	
	public static boolean isAdmin(String account) {
		String query = "SELECT * FROM QuizUser WHERE userName = '" + account + "';";
		ResultSet rs = DBConnection.getResult(query);
		
		try {
			if (rs.next() ) {
				if (rs.getInt("admin") == 1) return true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void setAdmin(String account, int value) {
		String query = "UPDATE QuizUser SET admin = " + value + " WHERE userName = '" + account + "';";
		DBConnection.updateTable(query);
	}
	
	//checks if an account exists
	public static boolean checkAccount(String account) {
		String query = "SELECT userName FROM QuizUser WHERE userName = '"+account+"';";
		ResultSet rs = DBConnection.getResult(query);
		try {
			if (rs.next() ) {
				if (rs.getString("userName").equals(account)) {
					return true;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	//checks if the string matches with the password of the given account
	public static boolean checkPwd(String account, String pwd) {
		
		String query = "SELECT password, salt FROM QuizUser WHERE userName = '"+account+"';";
		ResultSet rs = DBConnection.getResult(query);
		try {
			if (rs.next() ) {
				String pwdHash = rs.getString("password");
				String salt = rs.getString("salt");
				
				MessageDigest md = MessageDigest.getInstance("SHA");
				byte[] hexCode = md.digest( (pwd + salt).getBytes() );
				if ( pwdHash.equals(hexToString(hexCode)) ) {
					return true;
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	//create a new account with the given password. return 0 on success. return -1 if account exists.
	public static int createAccount(String account, String pwd) {
		if (checkAccount(account)) {
			return -1; 
		}
		
		//randomly generate a 32-bit salt
		SecureRandom random = new SecureRandom();
	    byte[] bytes = new byte[32];
	    random.nextBytes(bytes);
	    String salt = bytes.toString();
	    
	    MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA");
			byte[] hexCode = md.digest( (pwd + salt).getBytes() );
			String pwdHash = hexToString(hexCode);
			
			String query = "INSERT INTO QuizUser VALUES ('" + account + "','" + pwdHash + "','"+salt+"', false);";
			int updateResult = DBConnection.updateTable(query);
			
			return updateResult;
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return -2;
		
	}
	
}
