package com.revature.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionFactory {
	
	private static Logger log = Logger.getLogger(ConnectionFactory.class);
	
	public static Connection getConnection() {
		
		Connection conn = null;
		Properties prop = new Properties();
		
		try {
			prop.load(new FileReader("C:\\Users\\pjwar\\Documents\\Project 0\\project-0-PJWarring\\src\\main\\resources\\application.properties"));
			
			conn = DriverManager.getConnection(
					prop.getProperty("url"),
					prop.getProperty("username"),
					prop.getProperty("password")
					);
			
		} catch (SQLException e) {
			log.warn("Unable to obtain connection to database", e);
		} catch (FileNotFoundException e) {
			log.warn("Cannot locate properties file", e);
		} catch (IOException e) {
			log.warn("IOException in ConnectionFactory", e);
		}
		
		return conn;
	}
	

}