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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *  Description of the Class
 *
 *@author     mb
 *@created    November 19, 2001
 */
public class SQLAnswerTable {
    private ArrayList fdata = new ArrayList();
    private ArrayList fheader = new ArrayList();

    /**
     * return iterator of header cells
     * 
     * @return	iterator
     */
    public Iterator getHeaderIterator() {
    	return fheader.iterator();
    }
    
    /**
     * return iterator of data rows
     * 
     * @return	iterator
     */
    public Iterator getDataIterator() {
    	return fdata.iterator();
    }
    
    /**
     *  Constructor for the SQLAnswerTable object
     *
     *@param  rs contains a <code>ResultSet</code> with the 
     *return data from a select query
     *@exception  SQLException
     */
    public SQLAnswerTable(ResultSet rs) throws SQLException {
        initializeHeader( (rs.getMetaData()));
        initializeData(rs);
    }

    /**
     * Gets the column description of column number columnNumber
     *
     *@param  columnNumber  column number from which to get the description starting
     * with the first one
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
        return (String) (((ArrayList) fdata.get(rowNumber - 1)).get(columnNumber - 1));
    }
    
    /**
     * Gets the content of data cell at columnNumber@rowNumber.
     *
     *@param  columnNumber  column number
     *@param  rowNumber     row number
     *@return               data cell
     */
    public String getDataCellStandard(int columnNumber, int rowNumber) {
        return (String) (((ArrayList) fdata.get(rowNumber - 1)).get(columnNumber - 1));
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
     *  Gets the number of rows this answer table contains, excluding the header. 
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
    private void initializeData(ResultSet rs) throws SQLException {
        int rowNumber = 0;

        while (rs.next()) {
            fdata.add(new ArrayList());
            for (int i = 1; i <= columnCount(); i++)
                 ((ArrayList) (fdata.get(rowNumber))).add(rs.getString(i));

            rowNumber++;
        }
    }

    /**
     *  Sets the column header names by getting the attributes
     * from a given <code>ResultSetMetaData</code>.
     *
     *@param  metaData          contains header information
     *@exception  SQLException
     */
    private void initializeHeader(ResultSetMetaData metaData)
        throws SQLException {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            fheader.add(metaData.getColumnName(i));
        }
    }

}