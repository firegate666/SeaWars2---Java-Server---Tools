/*
 * Created on 28.05.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.mb.database.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is for retrieving databaseconnections from MySQL databases
 * 
 * @author Marco Behnke
 *
 */
public class MySQLConnectionFactory {
	
	private String _url;
	private String _port;
	private String _dbname;
	
	/**
	 * Public constructor
	 * 
	 * @param url		database servername / address
	 * @param port		database port
	 * @param dbname	database name
	 */
	public MySQLConnectionFactory(String url, String port, String dbname) {
		this._dbname = dbname;
		this._port = port;
		this._url = url;
		initialize();
	}
	
	/**
	 * Public constructor, port is default 3306
	 * 
	 * @param url		database servername / address
	 * @param dbname	database name
	 */
	public MySQLConnectionFactory(String url, String dbname) {
		this(url, "3306", dbname);
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

	/**
	 * @return Returns the _dbname.
	 */
	public String get_dbname() {
		return _dbname;
	}
	/**
	 * @param _dbname The _dbname to set.
	 */
	public void set_dbname(String _dbname) {
		this._dbname = _dbname;
	}
	/**
	 * @return Returns the _port.
	 */
	public String get_port() {
		return _port;
	}
	/**
	 * @param _port The _port to set.
	 */
	public void set_port(String _port) {
		this._port = _port;
	}
	/**
	 * @return Returns the _url.
	 */
	public String get_url() {
		return _url;
	}
	/**
	 * @param _url The _url to set.
	 */
	public void set_url(String _url) {
		this._url = _url;
	}
}
