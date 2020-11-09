package com.revature.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ConnectionFactory {
	
	private static Logger log = Logger.getLogger(ConnectionFactory.class);
	
	public static Connection getConnection() {
		
		//TODO: replace with call to properties file
		String url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=project0";
		String username = "postgres";
		String password = "postgres";
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			log.warn("Unable to obtain connection to database", e);
		}
		
		return conn;
	}
	

}