package com.tc.corpus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String dbUrl = "jdbc:mysql://localhost:3306/corpus?"+ "user=root&password=4441o59";;
	
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
