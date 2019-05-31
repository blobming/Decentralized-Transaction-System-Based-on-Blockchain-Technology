package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import obj.User;

public class SQLDB {
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://59.110.140.54/4PJT";
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
	
	//用户名查重
	public static boolean checkUsername(String username) {
		SQLDB.ReConnect();
		Statement statement;
		try {
			statement = conn.createStatement();
			String sql = "select count(*) count from user where username=\"" + username + "\"";
			ResultSet rs = statement.executeQuery(sql);
			int count = 0;
			while(rs.next()) {
				count = rs.getInt("count");
			}
			return count == 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static void createUser(User user) {
		SQLDB.ReConnect();
		PreparedStatement psql;
		try {
			psql = conn.prepareStatement("insert into user (username,password,publickey,privatekey) "
			        + "values(?,?,?,?)");
			psql.setString(1, user.getUsername());
			psql.setString(2, user.getPwd());
			psql.setString(3, user.getPubkey());
			psql.setString(4, user.getPrivateKey());
			psql.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void changePwd(User user) {
		SQLDB.ReConnect();
		//没有检查用户是不是存在...默认存在
		PreparedStatement psql;
		try {
			psql = conn.prepareStatement("update user set password=? where username = ?");
			psql.setString(1, user.getPwd());
			psql.setString(2, user.getUsername());
			psql.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static User getUserByUsername(String username, String password) {
		SQLDB.ReConnect();
		User user = null;
		Statement statement;
		try {
			statement = conn.createStatement();
			String sql = String.format("select * from user where username=\"%s\" and password = \"%s\"", username, password);
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				user = new User(rs.getString("username"), rs.getString("password"),rs.getString("publickey"),rs.getString("privatekey"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	public static User getUserByKey(String pubkey) {
		SQLDB.ReConnect();
		User user = null;
		Statement statement;
		try {
			statement = conn.createStatement();
			String sql = "select * from user where publickey=\"" + pubkey + "\"";
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				user = new User(rs.getString("username"), rs.getString("password"),rs.getString("publickey"),rs.getString("privatekey"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	public static void ReConnect() {
		try {
			if(SQLDB.conn == null || SQLDB.conn.isValid(0)) {
				SQLDB.connSqlDB();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("reconnect fail");
			e.printStackTrace();
		}
	}
}
