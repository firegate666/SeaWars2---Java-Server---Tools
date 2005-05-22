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

import java.sql.SQLException;
import java.util.Vector;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleResultSetMetaData;

/**
 *  Description of the Class
 *
 *@author     mb
 *@created    November 19, 2001
 */
public class SQLAnswerTable {
    private Vector fdata = new Vector();
    private Vector fheader = new Vector();

    /**
     *  Constructor for the SQLAnswerTable object
     *
     *@param  rs                rs contains a <code>ResultSet</code> with the return data from a select query
     *@exception  SQLException
     */
    public SQLAnswerTable(OracleResultSet rs) throws SQLException {
        initializeHeader((OracleResultSetMetaData) (rs.getMetaData()));
        initializeData(rs);
    }

    /**
     * Gets the column description of column number columnNumber
     *
     *@param  columnNumber  column number from which to get description starting with 1
     *@return               column description
     */
    public String getHeaderCell(int columnNumber) {
        return (String) (fheader.get(columnNumber - 1));
    }

    /**
     * Gets the content of data cell at columnNumber@rowNumber.
     *
     *@param  columnNumber  column number starting with 1
     *@param  rowNumber     row number starting with 1
     *@return               data cell
     */
    public String getDataCell(int columnNumber, int rowNumber) {
        return (String) (((Vector) fdata.get(rowNumber - 1)).get(columnNumber - 1));
    }

    /**
     * Returns table contents as tab formatted String.
     * @param withHeader  if true return string contains column names
     * @return String containing table
     */
    public String toString(boolean withHeader) {
        String result = "";

        if (withHeader) {
            for (int i = 1; i <= columnCount(); i++)
                result += getHeaderCell(i) + "\t";
            result += "\n";
        }

        for (int i = 1; i <= rowCount(); i++) {
            for (int j = 1; j <= columnCount(); j++)
                result += getDataCell(j, i) + "\t";
            result += "\n";
        }

        return result;
    }

    /**
     *  Gets the number of columns this answer table contains
     *
     *@return    column count
     */
    public int columnCount() {
        return fheader.size();
    }

    /**
     *  Gets the number of rows, excluding the header, this answer table contains
     *
     *@return    row count
     */
    public int rowCount() {
        return fdata.size();
    }

    /**
     *  This method initializes the table row data by parsing the giving <code>ResultSet</code>.
     *
     *@param  rs                return data from a select query
     *@exception  SQLException
     */
    private void initializeData(OracleResultSet rs) throws SQLException {
        int rowNumber = 0;

        while (rs.next()) {
            fdata.add(new Vector());
            for (int i = 1; i <= columnCount(); i++)
                 ((Vector) (fdata.get(rowNumber))).add(rs.getString(i));

            rowNumber++;
        }
    }

    /**
     *  Sets the column header names by getting the attributes from a given <code>ResultSetMetaData</code>.
     *
     *@param  metaData          contains header information
     *@exception  SQLException
     */
    private void initializeHeader(OracleResultSetMetaData metaData)
        throws SQLException {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            fheader.add(metaData.getColumnName(i));
        }
    }

}