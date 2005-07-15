/*
      Copyright (C) 2002-2004 MySQL AB

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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Representation of stored procedures for JDBC
 *
 * @author Mark Matthews
 * @version $Id: CallableStatement.java,v 1.2 2005-07-15 14:49:37 sw Exp $
 */
public class CallableStatement extends PreparedStatement
    implements java.sql.CallableStatement {
    private final static int NOT_OUTPUT_PARAMETER_INDICATOR = Integer.MIN_VALUE;
    protected CallableStatementParamInfo paramInfo;

    //private List parameterList;
    //private Map parameterMap;
    private ResultSet outputParameterResults;
    private int[] parameterIndexToRsIndex;
    private boolean hasOutputParams = false;

    /**
     * Creates a new CallableStatement
     *
     * @param conn the connection creating this statement
     * @param sql the SQL to prepare
     * @param catalog the current catalog
     *
     * @throws SQLException if an error occurs
     */
    public CallableStatement(Connection conn, String sql, String catalog)
        throws SQLException {
        super(conn, conn.nativeSQL(sql), catalog);

        determineParameterTypes();
    }

    /**
     * Creates a new CallableStatement
     *
     * @param conn the connection creating this statement
     * @param paramInfo the SQL to prepare
     *
     * @throws SQLException if an error occurs
     */
    public CallableStatement(Connection conn,
        CallableStatementParamInfo paramInfo) throws SQLException {
        super(conn, paramInfo.nativeSql, paramInfo.catalogInUse);

        this.paramInfo = paramInfo;
    }

    /**
     * Creates a new CallableStatement
     *
     * @param conn the connection creating this statement
     * @param catalog catalog the current catalog
     *
     * @throws SQLException if an error occurs
     */
    public CallableStatement(Connection conn, String catalog)
        throws SQLException {
        super(conn, catalog, null);

        determineParameterTypes();
    }

    /**
     * @see java.sql.CallableStatement#getArray(int)
     */
    public Array getArray(int i) throws SQLException {
        return getOutputParameters().getArray(mapOutputParameterIndexToRsIndex(
                i));
    }

    /**
     * @see java.sql.CallableStatement#getArray(java.lang.String)
     */
    public Array getArray(String parameterName) throws SQLException {
        return getOutputParameters().getArray(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
     *      java.io.InputStream, int)
     */
    public void setAsciiStream(String parameterName, InputStream x, int length)
        throws SQLException {
        setAsciiStream(getNamedParamIndex(parameterName, false), x, length);
    }

    /**
     * @see java.sql.CallableStatement#setBigDecimal(java.lang.String,
     *      java.math.BigDecimal)
     */
    public void setBigDecimal(String parameterName, BigDecimal x)
        throws SQLException {
        setBigDecimal(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parameterIndex DOCUMENT ME!
     * @param scale DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     *
     * @see java.sql.CallableStatement#getBigDecimal(int, int)
     * @deprecated
     */
    public BigDecimal getBigDecimal(int parameterIndex, int scale)
        throws SQLException {
        return getOutputParameters().getBigDecimal(mapOutputParameterIndexToRsIndex(
                parameterIndex), scale);
    }

    /**
     * @see java.sql.CallableStatement#getBigDecimal(int)
     */
    public BigDecimal getBigDecimal(int parameterIndex)
        throws SQLException {
        return getOutputParameters().getBigDecimal(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
     */
    public BigDecimal getBigDecimal(String parameterName)
        throws SQLException {
        return getOutputParameters().getBigDecimal(fixParameterName(
                parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
     *      java.io.InputStream, int)
     */
    public void setBinaryStream(String parameterName, InputStream x, int length)
        throws SQLException {
        setBinaryStream(getNamedParamIndex(parameterName, false), x, length);
    }

    /**
     * @see java.sql.CallableStatement#getBlob(int)
     */
    public Blob getBlob(int parameterIndex) throws SQLException {
        return getOutputParameters().getBlob(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getBlob(java.lang.String)
     */
    public Blob getBlob(String parameterName) throws SQLException {
        return getOutputParameters().getBlob(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
     */
    public void setBoolean(String parameterName, boolean x)
        throws SQLException {
        setBoolean(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getBoolean(int)
     */
    public boolean getBoolean(int parameterIndex) throws SQLException {
        return getOutputParameters().getBoolean(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getBoolean(java.lang.String)
     */
    public boolean getBoolean(String parameterName) throws SQLException {
        return getOutputParameters().getBoolean(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
     */
    public void setByte(String parameterName, byte x) throws SQLException {
        setByte(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getByte(int)
     */
    public byte getByte(int parameterIndex) throws SQLException {
        return getOutputParameters().getByte(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getByte(java.lang.String)
     */
    public byte getByte(String parameterName) throws SQLException {
        return getOutputParameters().getByte(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
     */
    public void setBytes(String parameterName, byte[] x)
        throws SQLException {
        setBytes(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getBytes(java.lang.String)
     */
    public byte[] getBytes(String parameterName) throws SQLException {
        return getOutputParameters().getBytes(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#getBytes(int)
     */
    public byte[] getBytes(int parameterIndex) throws SQLException {
        return getOutputParameters().getBytes(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
     *      java.io.Reader, int)
     */
    public void setCharacterStream(String parameterName, Reader reader,
        int length) throws SQLException {
        setCharacterStream(getNamedParamIndex(parameterName, false), reader,
            length);
    }

    /**
     * @see java.sql.CallableStatement#getClob(int)
     */
    public Clob getClob(int parameterIndex) throws SQLException {
        return getOutputParameters().getClob(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getClob(java.lang.String)
     */
    public Clob getClob(String parameterName) throws SQLException {
        return getOutputParameters().getClob(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date,
     *      java.util.Calendar)
     */
    public void setDate(String parameterName, Date x, Calendar cal)
        throws SQLException {
        setDate(getNamedParamIndex(parameterName, false), x, cal);
    }

    /**
     * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
     */
    public void setDate(String parameterName, Date x) throws SQLException {
        setDate(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
     */
    public Date getDate(int parameterIndex, Calendar cal)
        throws SQLException {
        return getOutputParameters().getDate(mapOutputParameterIndexToRsIndex(
                parameterIndex), cal);
    }

    /**
     * @see java.sql.CallableStatement#getDate(int)
     */
    public Date getDate(int parameterIndex) throws SQLException {
        return getOutputParameters().getDate(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getDate(java.lang.String,
     *      java.util.Calendar)
     */
    public Date getDate(String parameterName, Calendar cal)
        throws SQLException {
        return getOutputParameters().getDate(fixParameterName(parameterName),
            cal);
    }

    /**
     * @see java.sql.CallableStatement#getDate(java.lang.String)
     */
    public Date getDate(String parameterName) throws SQLException {
        return getOutputParameters().getDate(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
     */
    public void setDouble(String parameterName, double x)
        throws SQLException {
        setDouble(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getDouble(int)
     */
    public double getDouble(int parameterIndex) throws SQLException {
        return getOutputParameters().getDouble(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getDouble(java.lang.String)
     */
    public double getDouble(String parameterName) throws SQLException {
        return getOutputParameters().getDouble(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
     */
    public void setFloat(String parameterName, float x)
        throws SQLException {
        setFloat(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getFloat(int)
     */
    public float getFloat(int parameterIndex) throws SQLException {
        return getOutputParameters().getFloat(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getFloat(java.lang.String)
     */
    public float getFloat(String parameterName) throws SQLException {
        return getOutputParameters().getFloat(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setInt(java.lang.String, int)
     */
    public void setInt(String parameterName, int x) throws SQLException {
        setInt(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getInt(int)
     */
    public int getInt(int parameterIndex) throws SQLException {
        return getOutputParameters().getInt(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getInt(java.lang.String)
     */
    public int getInt(String parameterName) throws SQLException {
        return getOutputParameters().getInt(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setLong(java.lang.String, long)
     */
    public void setLong(String parameterName, long x) throws SQLException {
    }

    /**
     * @see java.sql.CallableStatement#getLong(int)
     */
    public long getLong(int parameterIndex) throws SQLException {
        return getOutputParameters().getLong(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getLong(java.lang.String)
     */
    public long getLong(String parameterName) throws SQLException {
        return getOutputParameters().getLong(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setNull(java.lang.String, int,
     *      java.lang.String)
     */
    public void setNull(String parameterName, int sqlType, String typeName)
        throws SQLException {
    }

    /**
     * @see java.sql.CallableStatement#setNull(java.lang.String, int)
     */
    public void setNull(String parameterName, int sqlType)
        throws SQLException {
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String,
     *      java.lang.Object, int, int)
     */
    public void setObject(String parameterName, Object x, int targetSqlType,
        int scale) throws SQLException {
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String,
     *      java.lang.Object, int)
     */
    public void setObject(String parameterName, Object x, int targetSqlType)
        throws SQLException {
        setObject(getNamedParamIndex(parameterName, false), x, targetSqlType);
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String,
     *      java.lang.Object)
     */
    public void setObject(String parameterName, Object x)
        throws SQLException {
        setObject(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getObject(int, java.util.Map)
     */
    public Object getObject(int parameterIndex, Map map)
        throws SQLException {
        return getOutputParameters().getObject(mapOutputParameterIndexToRsIndex(
                parameterIndex), map);
    }

    /**
     * @see java.sql.CallableStatement#getObject(int)
     */
    public Object getObject(int parameterIndex) throws SQLException {
        CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);

        return getOutputParameters().getObjectStoredProc(mapOutputParameterIndexToRsIndex(
                parameterIndex), paramDescriptor.desiredJdbcType);
    }

    /**
     * @see java.sql.CallableStatement#getObject(java.lang.String,
     *      java.util.Map)
     */
    public Object getObject(String parameterName, Map map)
        throws SQLException {
        return getOutputParameters().getObject(fixParameterName(parameterName),
            map);
    }

    /**
     * @see java.sql.CallableStatement#getObject(java.lang.String)
     */
    public Object getObject(String parameterName) throws SQLException {
        return getOutputParameters().getObject(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#getRef(int)
     */
    public Ref getRef(int parameterIndex) throws SQLException {
        return getOutputParameters().getRef(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getRef(java.lang.String)
     */
    public Ref getRef(String parameterName) throws SQLException {
        return getOutputParameters().getRef(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setShort(java.lang.String, short)
     */
    public void setShort(String parameterName, short x)
        throws SQLException {
        setShort(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getShort(int)
     */
    public short getShort(int parameterIndex) throws SQLException {
        return getOutputParameters().getShort(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getShort(java.lang.String)
     */
    public short getShort(String parameterName) throws SQLException {
        return getOutputParameters().getShort(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setString(java.lang.String,
     *      java.lang.String)
     */
    public void setString(String parameterName, String x)
        throws SQLException {
        setString(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getString(int)
     */
    public String getString(int parameterIndex) throws SQLException {
        return getOutputParameters().getString(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getString(java.lang.String)
     */
    public String getString(String parameterName) throws SQLException {
        return getOutputParameters().getString(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time,
     *      java.util.Calendar)
     */
    public void setTime(String parameterName, Time x, Calendar cal)
        throws SQLException {
        setTime(getNamedParamIndex(parameterName, false), x, cal);
    }

    /**
     * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
     */
    public void setTime(String parameterName, Time x) throws SQLException {
        setTime(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
     */
    public Time getTime(int parameterIndex, Calendar cal)
        throws SQLException {
        return getOutputParameters().getTime(mapOutputParameterIndexToRsIndex(
                parameterIndex), cal);
    }

    /**
     * @see java.sql.CallableStatement#getTime(int)
     */
    public Time getTime(int parameterIndex) throws SQLException {
        return getOutputParameters().getTime(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getTime(java.lang.String,
     *      java.util.Calendar)
     */
    public Time getTime(String parameterName, Calendar cal)
        throws SQLException {
        return getOutputParameters().getTime(fixParameterName(parameterName),
            cal);
    }

    /**
     * @see java.sql.CallableStatement#getTime(java.lang.String)
     */
    public Time getTime(String parameterName) throws SQLException {
        return getOutputParameters().getTime(fixParameterName(parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
     *      java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
        throws SQLException {
        setTimestamp(getNamedParamIndex(parameterName, false), x, cal);
    }

    /**
     * @see java.sql.CallableStatement#setTimestamp(java.lang.String,
     *      java.sql.Timestamp)
     */
    public void setTimestamp(String parameterName, Timestamp x)
        throws SQLException {
        setTimestamp(getNamedParamIndex(parameterName, false), x);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
     */
    public Timestamp getTimestamp(int parameterIndex, Calendar cal)
        throws SQLException {
        return getOutputParameters().getTimestamp(mapOutputParameterIndexToRsIndex(
                parameterIndex), cal);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(int)
     */
    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return getOutputParameters().getTimestamp(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String,
     *      java.util.Calendar)
     */
    public Timestamp getTimestamp(String parameterName, Calendar cal)
        throws SQLException {
        return getOutputParameters().getTimestamp(fixParameterName(
                parameterName), cal);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
     */
    public Timestamp getTimestamp(String parameterName)
        throws SQLException {
        return getOutputParameters().getTimestamp(fixParameterName(
                parameterName));
    }

    /**
     * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
     */
    public void setURL(String parameterName, URL val) throws SQLException {
        setURL(getNamedParamIndex(parameterName, false), val);
    }

    /**
     * @see java.sql.CallableStatement#getURL(int)
     */
    public URL getURL(int parameterIndex) throws SQLException {
        return getOutputParameters().getURL(mapOutputParameterIndexToRsIndex(
                parameterIndex));
    }

    /**
     * @see java.sql.CallableStatement#getURL(java.lang.String)
     */
    public URL getURL(String parameterName) throws SQLException {
        return getOutputParameters().getURL(fixParameterName(parameterName));
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#addBatch()
     */
    public void addBatch() throws SQLException {
        setOutParams();

        super.addBatch();
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#execute()
     */
    public boolean execute() throws SQLException {
        boolean returnVal = false;

        checkClosed();

        checkStreamability();

        synchronized (this.connection.getMutex()) {
        	setInOutParamsOnServer();
            setOutParams();
          
            returnVal = super.execute();

            retrieveOutParams();
        }

        return returnVal;
    }

    /* (non-Javadoc)
    * @see java.sql.PreparedStatement#executeQuery()
    */
    public synchronized java.sql.ResultSet executeQuery()
        throws SQLException {
        checkClosed();

        checkStreamability();

        java.sql.ResultSet execResults = null;

        synchronized (this.connection.getMutex()) {
        	setInOutParamsOnServer();
            setOutParams();
           
            execResults = super.executeQuery();

            retrieveOutParams();
        }

        return execResults;
    }

    /* (non-Javadoc)
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public synchronized int executeUpdate() throws SQLException {
        int returnVal = -1;

        checkClosed();

        checkStreamability();

        synchronized (this.connection.getMutex()) {
        	setInOutParamsOnServer();
            setOutParams();
            
            returnVal = super.executeUpdate();

            retrieveOutParams();
        }

        return returnVal;
    }

    /**
	 * 
	 */
	private void setInOutParamsOnServer() throws SQLException {
		if (this.paramInfo.numParameters > 0) {
			int parameterIndex = 0;
			
            for (Iterator paramIter = this.paramInfo.iterator();
                    paramIter.hasNext();) {
            	
            	
            	
                CallableStatementParam inParamInfo = (CallableStatementParam) paramIter
                    .next();

                if (inParamInfo.isOut && inParamInfo.isIn) {
                    String inOutParameterName = mangleParameterName(inParamInfo.paramName);
                    StringBuffer queryBuf = new StringBuffer(4 + inOutParameterName.length() + 1 + 1);
                    queryBuf.append("SET "); //$NON-NLS-1$
                    queryBuf.append(inOutParameterName);
                    queryBuf.append("=?"); //$NON-NLS-1$
                    
                    PreparedStatement setPstmt = null;
                    
                    try {
                    	setPstmt = this.connection.clientPrepareStatement(queryBuf.toString());
                    	
                    	byte[] parameterAsBytes = this.getBytesRepresentation(parameterIndex);
                    	
                    	if (parameterAsBytes != null) {
                    		if (parameterAsBytes.length > 8 &&
                    			parameterAsBytes[0] == '_' &&
                    			parameterAsBytes[1] == 'b' &&
                    			parameterAsBytes[2] == 'i' &&
                    			parameterAsBytes[3] == 'n' &&
                    			parameterAsBytes[4] == 'a' &&
                    			parameterAsBytes[5] == 'r' &&
                    			parameterAsBytes[6] == 'y' &&
                    			parameterAsBytes[7] == '\'') {
                    			setPstmt.setBytesNoEscapeNoQuotes(1, parameterAsBytes);
                    		} else {
                    			setPstmt.setBytes(1, parameterAsBytes);
                    		}
                    	} else {
                    		setPstmt.setNull(1, Types.NULL);
                    	}
                    	
                    	setPstmt.executeUpdate();
                    } finally {
                    	if (setPstmt != null) {
                    		setPstmt.close();
                    	}
                    }
                    
                    //StringBuffer fullName = new StringBuffer("@");
                    //fullName.append(outParameterName);
                    /*
                    this.setBytesNoEscapeNoQuotes(paramInfo.index + 1,
                        StringUtils.getBytes(outParameterName,
                            this.charConverter, this.charEncoding,
                            this.connection.getServerCharacterEncoding(),
							this.connection.parserKnowsUnicode()));
							*/
                }
            }
            
            parameterIndex++;
        }
		
	}

	/**
     * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
     */
    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
        throws SQLException {
        registerOutParameter(parameterIndex, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int,
     *      java.lang.String)
     */
    public void registerOutParameter(int parameterIndex, int sqlType,
        String typeName) throws SQLException {
        checkIsOutputParam(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int)
     */
    public void registerOutParameter(int parameterIndex, int sqlType)
        throws SQLException {
        CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);
        paramDescriptor.desiredJdbcType = sqlType;
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
     *      int, int)
     */
    public void registerOutParameter(String parameterName, int sqlType,
        int scale) throws SQLException {
        registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
     *      int, java.lang.String)
     */
    public void registerOutParameter(String parameterName, int sqlType,
        String typeName) throws SQLException {
        registerOutParameter(getNamedParamIndex(parameterName, true), sqlType,
            typeName);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
     *      int)
     */
    public synchronized void registerOutParameter(String parameterName,
        int sqlType) throws SQLException {
        registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
    }

    /**
     * @see java.sql.CallableStatement#wasNull()
     */
    public boolean wasNull() throws SQLException {
        return getOutputParameters().wasNull();
    }

    private int getNamedParamIndex(String paramName, boolean forOut)
        throws SQLException {
        if ((paramName == null) || (paramName.length() == 0)) {
            throw new SQLException(Messages.getString("CallableStatement.2"), //$NON-NLS-1$
                SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        CallableStatementParam namedParamInfo = this.paramInfo.getParameter(paramName);

        if (this.paramInfo == null) {
            throw new SQLException(Messages.getString("CallableStatement.3") + paramName + Messages.getString("CallableStatement.4"), //$NON-NLS-1$ //$NON-NLS-2$
                SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        if (forOut && !namedParamInfo.isOut) {
            throw new SQLException(Messages.getString("CallableStatement.5") + paramName //$NON-NLS-1$
                + Messages.getString("CallableStatement.6"), //$NON-NLS-1$
                SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        return namedParamInfo.index + 1; // JDBC indices are 1-based
    }

    private void setOutParams() throws SQLException {
        if (this.paramInfo.numParameters > 0) {
            for (Iterator paramIter = this.paramInfo.iterator();
                    paramIter.hasNext();) {
                CallableStatementParam outParamInfo = (CallableStatementParam) paramIter
                    .next();

                if (outParamInfo.isOut) {
                    String outParameterName = mangleParameterName(outParamInfo.paramName);

                    this.setBytesNoEscapeNoQuotes(outParamInfo.index + 1,
                        StringUtils.getBytes(outParameterName,
                            this.charConverter, this.charEncoding,
                            this.connection.getServerCharacterEncoding(),
							this.connection.parserKnowsUnicode()));
                }
            }
        }
    }

    /**
     * Returns the ResultSet that holds the output parameters, or throws an
     * appropriate exception if none exist, or they weren't returned.
     *
     * @return the ResultSet that holds the output parameters
     *
     * @throws SQLException if no output parameters were defined, or if no
     *         output parameters were returned.
     */
    private ResultSet getOutputParameters() throws SQLException {
        if (this.outputParameterResults == null) {
            if (this.paramInfo.numberOfParameters() == 0) {
                throw new SQLException(Messages.getString("CallableStatement.7"), //$NON-NLS-1$
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
                throw new SQLException(Messages.getString("CallableStatement.8"), //$NON-NLS-1$
                    SQLError.SQL_STATE_GENERAL_ERROR);
           
        }
            return this.outputParameterResults;
        
    }

    private CallableStatementParam checkIsOutputParam(int paramIndex)
        throws SQLException {
        checkParameterIndexBounds(paramIndex);

        int localParamIndex = paramIndex - 1;

        CallableStatementParam paramDescriptor = this.paramInfo
            .getParameter(localParamIndex);

        if (!paramDescriptor.isOut) {
            throw new SQLException(Messages.getString("CallableStatement.9") + paramIndex //$NON-NLS-1$
                + Messages.getString("CallableStatement.10"), //$NON-NLS-1$
                SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        this.hasOutputParams = true;

        return paramDescriptor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param paramIndex
     *
     * @throws SQLException
     */
    private void checkParameterIndexBounds(int paramIndex)
        throws SQLException {
        int localParamIndex = paramIndex - 1;

        if ((paramIndex < 0)
                || (localParamIndex >= this.paramInfo.numberOfParameters())) {
            throw new SQLException(Messages.getString("CallableStatement.11") + paramIndex //$NON-NLS-1$
                + Messages.getString("CallableStatement.12") + this.paramInfo.numberOfParameters() //$NON-NLS-1$
                + Messages.getString("CallableStatement.13"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT); //$NON-NLS-1$
        }
    }

    /**
     * Checks whether or not this statement is supposed to be providing
     * streamable result sets...If output parameters are registered, the
     * driver can not stream the results.
     *
     * @throws SQLException DOCUMENT ME!
     */
    private void checkStreamability() throws SQLException {
        if (this.hasOutputParams && createStreamingResultSet()) {
            throw new SQLException(Messages.getString("CallableStatement.14"), //$NON-NLS-1$
                SQLError.SQL_STATE_DRIVER_NOT_CAPABLE);
        }
    }

    private void determineParameterTypes() throws SQLException {
        java.sql.ResultSet paramTypesRs = null;

        try {
            String procName = extractProcedureName();

            java.sql.DatabaseMetaData dbmd = this.connection.getMetaData();

            boolean useCatalog = false;
            
            if (procName.indexOf(".") == -1) {
            	useCatalog = true;
            }
            
            paramTypesRs = dbmd.getProcedureColumns(
            		this.connection.versionMeetsMinimum(5, 0, 2) & useCatalog ? 
            				this.currentCatalog : null , 
            		null, procName, "%"); //$NON-NLS-1$

            this.paramInfo = new CallableStatementParamInfo(paramTypesRs);
        } finally {
            SQLException sqlExRethrow = null;

            if (paramTypesRs != null) {
                try {
                    paramTypesRs.close();
                } catch (SQLException sqlEx) {
                    sqlExRethrow = sqlEx;
                }

                paramTypesRs = null;
            }

            if (sqlExRethrow != null) {
                throw sqlExRethrow;
            }
        }
    }

    private String extractProcedureName() throws SQLException {
        // TODO: Do this with less memory allocation
        int endCallIndex = StringUtils.indexOfIgnoreCase(this.originalSql, "CALL"); //$NON-NLS-1$

        if (endCallIndex != -1) {
            StringBuffer nameBuf = new StringBuffer();

            String trimmedStatement = this.originalSql.substring(endCallIndex
                    + 4).trim();

            int statementLength = trimmedStatement.length();

            for (int i = 0; i < statementLength; i++) {
                char c = trimmedStatement.charAt(i);

                if (Character.isWhitespace(c) || (c == '(') || (c == '?')) {
                    break;
                }
                    nameBuf.append(c);
              
            }

            return nameBuf.toString();
        }
            throw new SQLException(Messages.getString("CallableStatement.1"), //$NON-NLS-1$
                SQLError.SQL_STATE_GENERAL_ERROR);
    
    }

    /**
     * Adds 'at' symbol to beginning of parameter names if needed.
     *
     * @param paramNameIn the parameter name to 'fix'
     *
     * @return the parameter name with an 'a' prepended, if needed
     *
     * @throws SQLException if the parameter name is null or empty.
     */
    private String fixParameterName(String paramNameIn)
        throws SQLException {
        if ((paramNameIn == null) || (paramNameIn.length() == 0)) {
            throw new SQLException(((Messages.getString("CallableStatement.0") + paramNameIn) == null) //$NON-NLS-1$
                ? Messages.getString("CallableStatement.15") : Messages.getString("CallableStatement.16"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return mangleParameterName(paramNameIn);
        
        /*
        if (paramNameIn.startsWith("@")) {
            return paramNameIn;
        } else {
            StringBuffer paramNameBuf = new StringBuffer("@");
            paramNameBuf.append(paramNameIn);

            return paramNameBuf.toString();
        }*/
    }

    private int mapOutputParameterIndexToRsIndex(int paramIndex)
        throws SQLException {
        checkParameterIndexBounds(paramIndex);

        int localParamIndex = paramIndex - 1;

        int rsIndex = this.parameterIndexToRsIndex[localParamIndex];

        if (rsIndex == NOT_OUTPUT_PARAMETER_INDICATOR) {
            throw new SQLException(Messages.getString("CallableStatement.21") + paramIndex //$NON-NLS-1$
                + Messages.getString("CallableStatement.22"), //$NON-NLS-1$
                SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        return rsIndex + 1;
    }

    /**
     * Issues a second query to retrieve all output parameters.
     *
     * @throws SQLException if an error occurs.
     */
    private void retrieveOutParams() throws SQLException {
        int numParameters = this.paramInfo.numberOfParameters();

        this.parameterIndexToRsIndex = new int[numParameters];

        for (int i = 0; i < numParameters; i++) {
            this.parameterIndexToRsIndex[i] = NOT_OUTPUT_PARAMETER_INDICATOR;
        }

        int localParamIndex = 0;

        if (numParameters > 0) {
            StringBuffer outParameterQuery = new StringBuffer("SELECT "); //$NON-NLS-1$

            boolean firstParam = true;
            boolean hadOutputParams = false;

            for (Iterator paramIter = this.paramInfo.iterator();
                    paramIter.hasNext();) {
                CallableStatementParam retrParamInfo = (CallableStatementParam) paramIter
                    .next();

                if (retrParamInfo.isOut) {
                    hadOutputParams = true;

                    this.parameterIndexToRsIndex[retrParamInfo.index] = localParamIndex++;

                    String outParameterName = mangleParameterName(retrParamInfo.paramName);

                    if (!firstParam) {
                        outParameterQuery.append(","); //$NON-NLS-1$
                    } else {
                        firstParam = false;
                    }

                    if (!outParameterName.startsWith("@")) { //$NON-NLS-1$
                        outParameterQuery.append('@');
                    }

                    outParameterQuery.append(outParameterName);
                }
            }

            if (hadOutputParams) {
                // We can't use 'ourself' to execute this query, or any
                // pending result sets would be overwritten
                java.sql.Statement outParameterStmt = null;
                java.sql.ResultSet outParamRs = null;

                try {
                    outParameterStmt = this.connection.createStatement();
                    outParamRs = outParameterStmt.executeQuery(outParameterQuery
                            .toString());
                    this.outputParameterResults = ((com.mysql.jdbc.ResultSet) outParamRs)
                        .copy();

                    if (!this.outputParameterResults.next()) {
                        this.outputParameterResults.close();
                        this.outputParameterResults = null;
                    }
                } finally {
                    if (outParameterStmt != null) {
                        outParameterStmt.close();
                    }
                }
            } else {
                this.outputParameterResults = null;
            }
        } else {
            this.outputParameterResults = null;
        }
    }

    private final static String PARAMETER_NAMESPACE_PREFIX = "@com_mysql_jdbc_outparam_"; //$NON-NLS-1$
    
    private static String mangleParameterName(String origParameterName) {
    	if (origParameterName == null) {
    		return null;
    	}
    	
    	int offset = 0;
    	
    	if (origParameterName.length() > 0 && origParameterName.charAt(0) == '@') {
    		offset = 1;
    	}
    	
    	StringBuffer paramNameBuf = new StringBuffer(PARAMETER_NAMESPACE_PREFIX.length() + origParameterName.length());
    	paramNameBuf.append(PARAMETER_NAMESPACE_PREFIX);
    	paramNameBuf.append(origParameterName.substring(offset));
    	
    	return paramNameBuf.toString();
    }
    
    class CallableStatementParam {
        String paramName;
        boolean isIn;
        boolean isOut;
        int desiredJdbcType;
        int index;

        CallableStatementParam(String name, int idx, boolean in, boolean out) {
            this.paramName = name;
            this.isIn = in;
            this.isOut = out;
            this.index = idx;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#clone()
         */
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    class CallableStatementParamInfo {
        List parameterList;
        Map parameterMap;
        String catalogInUse;
        String nativeSql;
        int numParameters;

        CallableStatementParamInfo(java.sql.ResultSet paramTypesRs)
            throws SQLException {
            boolean hadRows = paramTypesRs.last();

            this.nativeSql = originalSql;
            this.catalogInUse = currentCatalog;

            if (hadRows) {
                this.numParameters = paramTypesRs.getRow();

                this.parameterList = new ArrayList(this.numParameters);
                this.parameterMap = new HashMap(this.numParameters);

                paramTypesRs.beforeFirst();

                addParametersFromDBMD(paramTypesRs);
            } else {
            	this.numParameters = 0;
            }
        }

        /* (non-Javadoc)
         * @see java.lang.Object#clone()
         */
        protected Object clone() throws CloneNotSupportedException {
            // TODO Auto-generated method stub
            return super.clone();
        }

        CallableStatementParam getParameter(String name) {
            return (CallableStatementParam) this.parameterMap.get(name);
        }

        CallableStatementParam getParameter(int index) {
            return (CallableStatementParam) this.parameterList.get(index);
        }

        Iterator iterator() {
            return this.parameterList.iterator();
        }

        int numberOfParameters() {
            return this.numParameters;
        }

        private void addParametersFromDBMD(java.sql.ResultSet paramTypesRs)
            throws SQLException {
            int i = 0;

            while (paramTypesRs.next()) {
                String paramName = paramTypesRs.getString(4);
                int inOutModifier = paramTypesRs.getInt(5);

                boolean isOutParameter = false;
                boolean isInParameter = false;

                if (inOutModifier == DatabaseMetaData.procedureColumnInOut) {
                    isOutParameter = true;
                    isInParameter = true;
                } else if (inOutModifier == DatabaseMetaData.procedureColumnIn) {
                    isOutParameter = false;
                    isInParameter = true;
                } else if (inOutModifier == DatabaseMetaData.procedureColumnOut) {
                    isOutParameter = true;
                    isInParameter = false;
                }

                CallableStatementParam paramInfoToAdd = new CallableStatementParam(paramName,
                        i++, isInParameter, isOutParameter);

                this.parameterList.add(paramInfoToAdd);
                this.parameterMap.put(paramName, paramInfoToAdd);
            }
        }
    }
}
