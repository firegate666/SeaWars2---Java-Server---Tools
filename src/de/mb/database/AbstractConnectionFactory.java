/*
 * Created on 28.05.2005
 *
 */
package de.mb.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class is for retrieving databaseconnections from MySQL databases
 * 
 * @author Marco Behnke
 *
 */
public abstract class AbstractConnectionFactory {
	
	protected String _url;
	protected String _port;
	protected String _dbname;
	
	/**
	 * Public constructor
	 * 
	 * @param url		database servername / address
	 * @param port		database port
	 * @param dbname	database name
	 */
	public AbstractConnectionFactory(String url, String port, String dbname) {
		this._dbname = dbname;
		this._port = port;
		this._url = url;
		initialize();
	}
	
	/**
	 * initializes database driver
	 */
	protected abstract void initialize();
	
	/**
	 * Returns connection to database
	 * @param username		DBUser
	 * @param password		DBPassword
	 * @return				database connection
	 * @throws SQLException
	 */
	public abstract Connection getConnection(String username, String password) throws SQLException;

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
