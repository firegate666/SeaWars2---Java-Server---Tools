package de.mb.database.mysql;

import de.mb.database.AbstractSQLExecution;
import java.sql.Connection;

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
