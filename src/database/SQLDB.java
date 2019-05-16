package database;

import java.sql.Connection;
import java.sql.DriverManager;



public class SQLDB {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbs:mysql://59.110.140.54/xxx";
	private static String user = "4pjt";
	private static String pwd = "supinfo4pjtaccess";
	private static Connection conn = null;
	
	public static void connSqlDB() {
		try {
	        Class.forName(driver).newInstance();
	        conn = DriverManager.getConnection(url, user, pwd);
	        System.out.println("sql connected");
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
