/*
   Copyright (C) 2004 MySQL AB

   This program is free software; you can redistribute it and/or modify
   it under the terms of version 2 of the GNU General Public License as 
   published by the Free Software Foundation.

   There are special exceptions to the terms and conditions of the GPL 
   as it is applied to this software. View the full text of the 
   exception in file EXCEPTIONS-CONNECTOR-J in the directory of this 
   software distribution.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package com.mysql.jdbc;

import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

/**
 * Connection that opens two connections, one two a replication master,
 * and another to one or more slaves, and decides to use master when
 * the connection is not read-only, and use slave(s) when the connection
 * is read-only.
 * 
 * @version $Id: ReplicationConnection.java,v 1.1 2005-06-20 20:46:00 sw Exp $
 */
public class ReplicationConnection implements java.sql.Connection {
	private Connection masterConnection;
	private Connection slavesConnection;
	private Connection currentConnection;
	
	public ReplicationConnection(Properties masterProperties, 
			Properties slaveProperties) throws SQLException {
		Driver driver = new Driver();
		
		this.masterConnection = (com.mysql.jdbc.Connection)driver.connect("jdbc:mysql:///", masterProperties);
		this.slavesConnection = (com.mysql.jdbc.Connection)driver.connect("jdbc:mysql:///", slaveProperties);
		this.currentConnection = this.masterConnection;
	}
	
	private synchronized void switchToMasterConnection() throws SQLException {
		String slaveCatalog = this.slavesConnection.getCatalog();
		String masterCatalog = this.masterConnection.getCatalog();
		
		if (slaveCatalog != null && !slaveCatalog.equals(masterCatalog)) {
			this.masterConnection.setCatalog(slaveCatalog);
		} else if (masterCatalog != null) {
			this.masterConnection.setCatalog(null);
		}
		
		boolean slavesAutoCommit = this.slavesConnection.getAutoCommit();
		
		if (this.masterConnection.getAutoCommit() != slavesAutoCommit) {
			this.masterConnection.setAutoCommit(slavesAutoCommit);
		}
		
		int slavesTransactionIsolation = this.slavesConnection.getTransactionIsolation();
		
		if (this.masterConnection.getTransactionIsolation() != slavesTransactionIsolation) {
			this.masterConnection.setTransactionIsolation(slavesTransactionIsolation);
		}
		
		this.currentConnection = this.masterConnection;
	}
	
	private synchronized void switchToSlavesConnection() throws SQLException {
		String slaveCatalog = this.slavesConnection.getCatalog();
		String masterCatalog = this.masterConnection.getCatalog();
		
		if (masterCatalog != null && !masterCatalog.equals(slaveCatalog)) {
			this.slavesConnection.setCatalog(masterCatalog);
		} else if (slaveCatalog != null) {
			this.slavesConnection.setCatalog(null);
		}
		
		boolean masterAutoCommit = this.masterConnection.getAutoCommit();
		
		if (this.slavesConnection.getAutoCommit() != masterAutoCommit) {
			this.slavesConnection.setAutoCommit(masterAutoCommit);
		}
		
		int masterTransactionIsolation = this.masterConnection.getTransactionIsolation();
		
		if (this.slavesConnection.getTransactionIsolation() != masterTransactionIsolation) {
			this.slavesConnection.setTransactionIsolation(masterTransactionIsolation);
		}
		this.currentConnection = this.slavesConnection;
		
		this.slavesConnection.setAutoCommit(this.masterConnection.getAutoCommit());
		this.slavesConnection.setTransactionIsolation(this.masterConnection.getTransactionIsolation());
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement()
	 */
	public Statement createStatement() throws SQLException {
		return this.currentConnection.createStatement();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return this.currentConnection.prepareStatement(sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	public CallableStatement prepareCall(String sql) throws SQLException {
		return this.currentConnection.prepareCall(sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	public synchronized String nativeSQL(String sql) throws SQLException {
		return this.currentConnection.nativeSQL(sql);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	public synchronized void setAutoCommit(boolean autoCommit) throws SQLException {
		this.currentConnection.setAutoCommit(autoCommit);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getAutoCommit()
	 */
	public synchronized boolean getAutoCommit() throws SQLException {
		return this.currentConnection.getAutoCommit();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#commit()
	 */
	public synchronized void commit() throws SQLException {
		this.currentConnection.commit();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback()
	 */
	public synchronized void rollback() throws SQLException {
		this.currentConnection.rollback();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#close()
	 */
	public synchronized void close() throws SQLException {
		this.masterConnection.close();
		this.slavesConnection.close();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#isClosed()
	 */
	public synchronized boolean isClosed() throws SQLException {
		return this.currentConnection.isClosed();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getMetaData()
	 */
	public synchronized DatabaseMetaData getMetaData() throws SQLException {
		return this.currentConnection.getMetaData();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	public synchronized void setReadOnly(boolean readOnly) throws SQLException {
		if (readOnly) {
			switchToSlavesConnection();
		} else {
			switchToMasterConnection();
		}
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#isReadOnly()
	 */
	public synchronized boolean isReadOnly() throws SQLException {
		return this.currentConnection == this.slavesConnection;
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	public synchronized void setCatalog(String catalog) throws SQLException {
		this.currentConnection.setCatalog(catalog);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getCatalog()
	 */
	public synchronized String getCatalog() throws SQLException {
		return this.currentConnection.getCatalog();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	public synchronized void setTransactionIsolation(int level) throws SQLException {
		this.currentConnection.setTransactionIsolation(level);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	public synchronized int getTransactionIsolation() throws SQLException {
		return this.currentConnection.getTransactionIsolation();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getWarnings()
	 */
	public synchronized SQLWarning getWarnings() throws SQLException {
		return this.currentConnection.getWarnings();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#clearWarnings()
	 */
	public synchronized void clearWarnings() throws SQLException {
		this.currentConnection.clearWarnings();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	public synchronized Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return this.currentConnection.createStatement(resultSetType, resultSetConcurrency);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	public synchronized PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return this.currentConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	public synchronized CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return this.currentConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getTypeMap()
	 */
	public synchronized Map getTypeMap() throws SQLException {
		return this.currentConnection.getTypeMap();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	public synchronized void setTypeMap(Map arg0) throws SQLException {
		this.currentConnection.setTypeMap(arg0);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setHoldability(int)
	 */
	public synchronized void setHoldability(int holdability) throws SQLException {
		this.currentConnection.setHoldability(holdability);		
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#getHoldability()
	 */
	public synchronized int getHoldability() throws SQLException {
		return this.currentConnection.getHoldability();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint()
	 */
	public synchronized Savepoint setSavepoint() throws SQLException {
		return this.currentConnection.setSavepoint();
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	 */
	public synchronized Savepoint setSavepoint(String name) throws SQLException {
		return this.currentConnection.setSavepoint(name);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	 */
	public synchronized void rollback(Savepoint savepoint) throws SQLException {
		this.currentConnection.rollback(savepoint);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	 */
	public synchronized void releaseSavepoint(Savepoint savepoint) throws SQLException {
		this.currentConnection.releaseSavepoint(savepoint);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	public synchronized Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return this.currentConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	public synchronized PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return this.currentConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	public synchronized CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return this.currentConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	public synchronized PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return this.currentConnection.prepareStatement(sql, autoGeneratedKeys);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	public synchronized PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return this.currentConnection.prepareStatement(sql, columnIndexes);
	}

	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
	 */
	public synchronized PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return this.currentConnection.prepareStatement(sql, columnNames);
	}
	
	// For testing
	
	public synchronized Connection getCurrentConnection() {
		return this.currentConnection;
	}
	
	public synchronized Connection getSlavesConnection() {
		return this.slavesConnection;
	}
	
	public synchronized Connection getMasterConnection() {
		return this.masterConnection;
	}
}
