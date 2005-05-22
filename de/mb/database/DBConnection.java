package de.mb.database;

/*
 * SEP-Projekt WS 2001-2002 -- Questionaire
 * 
 * Projektteilnehmer:
 * Marco Behnke <marco@firegate.de>
 * Sebastian Davids <sdavids@gmx.de>
 * Martin Koose <martin@koose-hh.de>
 * 
 * Projektbegleitung: 
 * Prof. Dr. Bernd Kahlbrandt <Bernd.Kahlbrandt@informatik.fh-hamburg.de>
 * 
 * Copyright (c)2001 Behnke, Davids & Koose. Alle Rechte vorbehalten.
 * ===========================================================================
 */

import oracle.jdbc.OracleConnection;
import java.sql.SQLException;

/**
 *  <code>DBConnection</code> establishes connection to jdb driven databses.
 *
 *@author     mb
 *@created    November 18, 2001
 */
public abstract class DBConnection {

    private String fdbIp;
    private String fdbName;
    private String fdbPassword;
    private String fdbPort;
    private String fdbUser;

    /**
     *  Constructor for the DBConnection object
     */
    public DBConnection() {}

    /**
     *  Constructor for the DBConnection object
     *
     *@param  ip        Description of the Parameter
     *@param  port      Description of the Parameter
     *@param  database  Description of the Parameter
     *@param  user      Description of the Parameter
     *@param  pass      Description of the Parameter
     */
    public DBConnection(
        String ip,
        String port,
        String database,
        String user,
        String pass) {
        dbIp(ip);
        dbPort(port);
        dbName(database);
        dbUser(user);
        dbPassword(pass);
    }

    /**
     *  Description of the Method
     *
     *@param  conn              Description of the Parameter
     *@exception  SQLException  Description of the Exception
     */
    public void close(OracleConnection conn) throws SQLException {
        conn.close();
    }

    /**
     *  Connect to database using given username and password.
     *
     *@return                   A succesfull connection
     *@exception  SQLException
     */
    public abstract OracleConnection connect() throws SQLException;

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String dbIp() {
        return fdbIp;
    }

    /**
     *  Description of the Method
     *
     *@param  ip  Description of the Parameter
     */
    public void dbIp(String ip) {
        fdbIp = ip;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String dbName() {
        return fdbName;
    }

    /**
     *  Description of the Method
     *
     *@param  name  Description of the Parameter
     */
    public void dbName(String name) {
        fdbName = name;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String dbPassword() {
        return fdbPassword;
    }

    /**
     *  Description of the Method
     *
     *@param  password  Description of the Parameter
     */
    public void dbPassword(String password) {
        fdbPassword = password;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String dbPort() {
        return fdbPort;
    }

    /**
     *  Description of the Method
     *
     *@param  port  Description of the Parameter
     */
    public void dbPort(String port) {
        fdbPort = port;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String dbUser() {
        return fdbUser;
    }

    /**
     *  Description of the Method
     *
     *@param  user  Description of the Parameter
     */
    public void dbUser(String user) {
        fdbUser = user;
    }

    /**
     *  Register the JDBC Driver
     *
     *@exception  SQLException  Description of the Exception
     */
    protected abstract void registerDriver() throws SQLException;
}