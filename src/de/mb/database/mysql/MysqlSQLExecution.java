package de.mb.database.mysql;

import java.sql.Connection;

import de.mb.database.AbstractSQLExecution;

/**
 * This Class ist for easy use of SQL Statements
 * 
 * @author Marco Behnke
 */
public class MysqlSQLExecution extends AbstractSQLExecution{

	/**
	 * Public constructor
	 * 
	 * @param conn	database connection
	 */
	public MysqlSQLExecution(Connection conn){
		super(conn);
	}
}
