package com.tc.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	private static String driverName = "com.mysql.jdbc.Driver";
	//private String ip="180.188.196.99";
	private static String dbUrl = "jdbc:mysql://localhost:3306/keywordweibo?"+ "user=root&password=4441o59";;
	
	public static Connection createConnection() {
		try {
			Class.forName(driverName);
			//System.out.println("成功加载MySQL驱动程序");
			Connection conn = DriverManager.getConnection(dbUrl);
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
