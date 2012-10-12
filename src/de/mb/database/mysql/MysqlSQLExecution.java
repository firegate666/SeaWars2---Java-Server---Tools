package de.mb.database.mysql;

import de.mb.database.AbstractSQLExecution;
import java.sql.Connection;
import java.sql.SQLException;

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

	public void startTransaction() throws SQLException {
		this._conn.setAutoCommit(false);
	}

	public void commitTransaction() throws SQLException {
		this._conn.commit();
		this._conn.setAutoCommit(true);
	}

	public void rollbackTransaction() throws SQLException {
		this._conn.rollback();
		this._conn.setAutoCommit(true);
	}


}
