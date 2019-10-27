package com.oracle.pcmcs.dbconn.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class JDBCDAOFactory extends DAOFactory {
	private final static Logger log = Logger.getLogger(DatabaseUtils.class);
	Connection connection = null;
	JDBCDAOFactory(String url,String user, String password){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch(ClassNotFoundException e){
            log.error("JDBC Driver class not found");
            throw new RuntimeException("JDBC Driver class not found");
		}
		//Connection connection = null;
		try{
			connection = DriverManager.getConnection(url, user, password);
		}catch(SQLException s){
			log.error(s.getMessage());
			throw new RuntimeException("Failed to create the connection.");
		}
	}
}
