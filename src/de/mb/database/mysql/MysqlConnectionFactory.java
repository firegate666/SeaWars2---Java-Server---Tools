package de.mb.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.mb.database.AbstractConnectionFactory;

/**
 * This class is for retrieving databaseconnections from MySQL databases
 * 
 * @author Marco Behnke
 *
 */
public class MysqlConnectionFactory extends AbstractConnectionFactory{
	
	/**
	 * Public constructor, port is default 3306
	 * 
	 * @param url		database servername / address
	 * @param dbname	database name
	 */
	public MysqlConnectionFactory(String url, String dbname) {
		super(url, "3306", dbname);
	}
	
	/**
	 * initializes database driver
	 */
	protected void initialize() {
		try {
			// load mysql driver from library
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println(e.getLocalizedMessage());
		}
		
	}
	
	/**
	 * Returns connection to database
	 * @param username		DBUser
	 * @param password		DBPassword
	 * @return				database connection
	 * @throws SQLException
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		String url = "jdbc:mysql://"+_url+":"+_port+"/"+_dbname+"?user="+username+"&password="+password;
		System.out.println(url);
		return DriverManager.getConnection(url);
	}
}
