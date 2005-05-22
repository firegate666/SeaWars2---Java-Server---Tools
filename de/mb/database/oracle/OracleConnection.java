package de.mb.database.oracle;

import java.sql.Connection;

import oracle.jdbc.pool.OraclePooledConnection;
import de.mb.network.IP4;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class OracleConnection extends OraclePooledConnection {

    // Database variables

    protected IP4 ip;
    protected int port;
    protected String database;

    // User variables
    protected String userName;
    protected String password;

    public OracleConnection() {}
    public synchronized Connection getConnection() throws java.sql.SQLException {
        /**@todo: Override this oracle.jdbc.pool.OraclePooledConnection method*/
        return super.getConnection();
    }
    public synchronized void logicalClose() {
        /**@todo: Override this oracle.jdbc.pool.OraclePooledConnection method*/
        super.logicalClose();
    }
    public synchronized void close() throws java.sql.SQLException {
        /**@todo: Override this oracle.jdbc.pool.OraclePooledConnection method*/
        super.close();
    }
}