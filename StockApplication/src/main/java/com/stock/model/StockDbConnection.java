package com.stock.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StockDbConnection {

	public static Connection getConnection() throws SQLException {
//		String url = "jdbc:mysql://database-2-instance-1.cbscbcwxhk49.us-east-2.rds.amazonaws.com";
//    	String userName = "admin";
//    	String password = "password";
    	String url = System.getenv("dbUrl");
    	String userName = System.getenv("userName");
    	String password = System.getenv("password");
    	Connection con = DriverManager.getConnection(url,userName,password);
    	return con;
    	
    }
}
