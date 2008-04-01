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

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Types;


/**
 * Field is a class used to describe fields in a
 * ResultSet
 *
 * @author Mark Matthews
 * @version $Id: Field.java,v 1.2 2008-04-01 22:49:35 firegate666 Exp $
 */
public class Field {
    //~ Static fields/initializers ---------------------------------------------

    private static final int AUTO_INCREMENT_FLAG = 512;
    private static final int NO_CHARSET_INFO = -1;

    //~ Instance fields --------------------------------------------------------

    private Connection connection = null;
    private String charsetName = null;
    private String databaseName = null;
    private String fullName = null;
    private String fullOriginalName = null;
    private String name; // The Field name
    private String originalColumnName = null;
    private String originalTableName = null;
    private String tableName; // The Name of the Table
    private byte[] buffer;
    private int charsetIndex = 0;
    private int colDecimals;
    private int databaseNameLength = -1;

    // database name info
    private int databaseNameStart = -1;
    private int defaultValueLength = -1;

    // default value info - from COM_LIST_FIELDS execution
    private int defaultValueStart = -1;
    private int length; // Internal length of the field;
    private int mysqlType = -1; // the MySQL type
    private int nameLength;
    private int nameStart;
    private int originalColumnNameLength = -1;

    // column name info (before aliasing)
    private int originalColumnNameStart = -1;
    private int originalTableNameLength = -1;

    // table name info (before aliasing)
    private int originalTableNameStart = -1;
    private int precisionAdjustFactor = 0;
    private int sqlType = -1; // the java.sql.Type
    private int tableNameLength;
    private int tableNameStart;
    private short colFlag;
    
    private String collationName = null;
    private boolean isImplicitTempTable = false;

    //~ Constructors -----------------------------------------------------------

    /**
    * Constructor used by DatabaseMetaData methods.
    */
    Field(String tableName, String columnName, int jdbcType, int length) {
        this.tableName = tableName;
        this.name = columnName;
        this.length = length;
        this.sqlType = jdbcType;
        this.colFlag = 0;
        this.colDecimals = 0;
    }

    /**
     * Constructor used when communicating with pre 4.1 servers
     */
    Field(Connection conn, byte[] buffer, int nameStart, int nameLength,
        int tableNameStart, int tableNameLength, int length, int mysqlType,
        short colFlag, int colDecimals) throws SQLException {
        this(conn, buffer, -1, -1, tableNameStart, tableNameLength, -1, -1,
            nameStart, nameLength, -1, -1, length, mysqlType, colFlag,
            colDecimals, -1, -1, NO_CHARSET_INFO);
    }

    /**
     * Constructor used when communicating with 4.1 and newer
     * servers
     */
    Field(Connection conn, byte[] buffer, int databaseNameStart,
        int databaseNameLength, int tableNameStart, int tableNameLength,
        int originalTableNameStart, int originalTableNameLength, int nameStart,
        int nameLength, int originalColumnNameStart,
        int originalColumnNameLength, int length, int mysqlType, short colFlag,
        int colDecimals, int defaultValueStart, int defaultValueLength,
        int charsetIndex) throws SQLException {
        this.connection = conn;
        this.buffer = buffer;
        this.nameStart = nameStart;
        this.nameLength = nameLength;
        this.tableNameStart = tableNameStart;
        this.tableNameLength = tableNameLength;
        this.length = length;
        this.colFlag = colFlag;
        this.colDecimals = colDecimals;
        this.mysqlType = mysqlType;

        // 4.1 field info...
        this.databaseNameStart = databaseNameStart;
        this.databaseNameLength = databaseNameLength;

        this.originalTableNameStart = originalTableNameStart;
        this.originalTableNameLength = originalTableNameLength;

        this.originalColumnNameStart = originalColumnNameStart;
        this.originalColumnNameLength = originalColumnNameLength;

        this.defaultValueStart = defaultValueStart;
        this.defaultValueLength = defaultValueLength;

        // Re-map to 'real' blob type, if we're a BLOB

        if (this.mysqlType == MysqlDefs.FIELD_TYPE_BLOB) {
        	setBlobTypeBasedOnLength();
        }
        
        // Map MySqlTypes to java.sql Types
        this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
        
        if (this.sqlType == Types.TINYINT && this.length == 1 
        		&& this.connection.getTinyInt1isBit()) {
        	this.sqlType = Types.BIT;
        }
        
        if (this.mysqlType == MysqlDefs.FIELD_TYPE_BIT) {
        	if (this.length == 0) {
        		this.sqlType = Types.BIT;
        	} else {
        		this.sqlType = Types.VARBINARY;
        		this.colFlag |= 128; // we need to pretend this is a full
        		this.colFlag |= 16; // binary blob
        	}
        }

	
        // If we're not running 4.1 or newer, use the connection's
        // charset
        this.charsetIndex = charsetIndex;
        
        this.charsetName = this.connection.getCharsetNameForIndex(this.charsetIndex);

		
        boolean isBinary = isBinary();

        //
        // Handle TEXT type (special case), Fix proposed by Peter McKeown
        //
        if ((this.sqlType == java.sql.Types.LONGVARBINARY) && !isBinary) {
            this.sqlType = java.sql.Types.LONGVARCHAR;
        } else if ((this.sqlType == java.sql.Types.VARBINARY) && !isBinary) {
            this.sqlType = java.sql.Types.VARCHAR;
        }

        //
        // Handle odd values for 'M' for floating point/decimal numbers
        //
        if (!isUnsigned()) {
            switch (this.mysqlType) {
            case MysqlDefs.FIELD_TYPE_DECIMAL:
                this.precisionAdjustFactor = -1;

                break;
            case MysqlDefs.FIELD_TYPE_DOUBLE:
            case MysqlDefs.FIELD_TYPE_FLOAT:
                this.precisionAdjustFactor = 1;

                break;
            }
        } else {
            switch (this.mysqlType) {
            case MysqlDefs.FIELD_TYPE_DOUBLE:
            case MysqlDefs.FIELD_TYPE_FLOAT:
                this.precisionAdjustFactor = 1;

                break;
            }
        }
        
        checkForImplicitTemporaryTable();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAutoIncrement() {
        return ((this.colFlag & AUTO_INCREMENT_FLAG) > 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBinary() {
        return ((this.colFlag & 128) > 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBlob() {
        return ((this.colFlag & 16) > 0);
    }

    /**
     * Returns the character set (if known) for this
     * field.
     *
     * @return the character set
     */
    public String getCharacterSet() {
        return this.charsetName;
    }
    
    public int getMaxBytesPerCharacter() throws SQLException {
    	return this.connection.getMaxBytesPerChar((String)CharsetMapping.JAVA_TO_MYSQL_CHARSET_MAP.get(getCharacterSet()));
    }
    
    public synchronized String getCollation() throws SQLException {
    	if (this.collationName == null) {
    		if (this.connection != null) {
    			if (this.connection.versionMeetsMinimum(4, 1, 0)) {
    				java.sql.DatabaseMetaData dbmd = this.connection.getMetaData();
    			
    				String quotedIdStr = dbmd.getIdentifierQuoteString();
    			
    				if (" ".equals(quotedIdStr)) { //$NON-NLS-1$
    					quotedIdStr = ""; //$NON-NLS-1$
    				}
    				
    				String csCatalogName = getDatabaseName();
    				String csTableName = getOriginalTableName();
    				String csColumnName = getOriginalName();
    				
    				if (csCatalogName != null && csCatalogName.length() != 0
    						&& csTableName != null && csTableName.length() != 0
    						&& csColumnName != null && csColumnName.length() != 0) {
    					StringBuffer queryBuf = new StringBuffer(csCatalogName.length() + csTableName.length() + 28);
    					queryBuf.append("SHOW FULL COLUMNS FROM "); //$NON-NLS-1$
    					queryBuf.append(quotedIdStr);
    					queryBuf.append(csCatalogName);
    					queryBuf.append(quotedIdStr);
    					queryBuf.append("."); //$NON-NLS-1$
    					queryBuf.append(quotedIdStr);
    					queryBuf.append(csTableName);
						queryBuf.append(quotedIdStr);

						java.sql.Statement collationStmt = null;
						java.sql.ResultSet collationRs = null;

						try {
							collationStmt = this.connection.createStatement();
							
							collationRs = collationStmt.executeQuery(queryBuf.toString());

							while (collationRs.next()) {
								if (csColumnName.equals(collationRs.getString("Field"))) { //$NON-NLS-1$
									this.collationName = collationRs.getString("Collation"); //$NON-NLS-1$

									break;
								}
							}
						} finally {
							if (collationRs != null) {
								collationRs.close();
								collationRs = null;
							}

							if (collationStmt != null) {
								collationStmt.close();
								collationStmt = null;
							}
						}

    				}
    			}
    			
    		}
    		
    	}
    	
    	return this.collationName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conn DOCUMENT ME!
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
        
		this.charsetName = this.connection.getEncoding();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDatabaseName() {
        if ((this.databaseName == null) && (this.databaseNameStart != -1)
                && (this.databaseNameLength != -1)) {
            this.databaseName = getStringFromBytes(this.databaseNameStart,
                    this.databaseNameLength);
        }

        return this.databaseName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFullName() {
        if (this.fullName == null) {
            StringBuffer fullNameBuf = new StringBuffer(getTableName().length()
                    + 1 + getName().length());
            fullNameBuf.append(this.tableName);

            // much faster to append a char than a String
            fullNameBuf.append('.');
            fullNameBuf.append(this.name);
            this.fullName = fullNameBuf.toString();
            fullNameBuf = null;
        }

        return this.fullName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFullOriginalName() {
        getOriginalName();

        if (this.originalColumnName == null) {
            return null; // we don't have this information
        }

        if (this.fullName == null) {
            StringBuffer fullOriginalNameBuf = new StringBuffer(getOriginalTableName()
                                                                    .length()
                    + 1 + getOriginalName().length());
            fullOriginalNameBuf.append(this.originalTableName);

            // much faster to append a char than a String
            fullOriginalNameBuf.append('.');
            fullOriginalNameBuf.append(this.originalColumnName);
            this.fullOriginalName = fullOriginalNameBuf.toString();
            fullOriginalNameBuf = null;
        }

        return this.fullOriginalName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return this.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMultipleKey() {
        return ((this.colFlag & 8) > 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMysqlType() {
        return this.mysqlType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        if (this.name == null) {
            this.name = getStringFromBytes(this.nameStart, this.nameLength);
        }

        return this.name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOriginalName() {
        if ((this.originalColumnName == null)
                && (this.originalColumnNameStart != -1)
                && (this.originalColumnNameLength != -1)) {
            this.originalColumnName = getStringFromBytes(this.originalColumnNameStart,
                    this.originalColumnNameLength);
        }

        return this.originalColumnName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOriginalTableName() {
        if ((this.originalTableName == null)
                && (this.originalTableNameStart != -1)
                && (this.originalTableNameLength != -1)) {
            this.originalTableName = getStringFromBytes(this.originalTableNameStart,
                    this.originalTableNameLength);
        }

        return this.originalTableName;
    }

    /**
     * Returns amount of correction that
     * should be applied to the precision value.
     *
     * Different versions of MySQL report different
     * precision values.
     *
     * @return the amount to adjust precision value by.
     */
    public int getPrecisionAdjustFactor() {
        return this.precisionAdjustFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isPrimaryKey() {
        return ((this.colFlag & 2) > 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSQLType() {
        return this.sqlType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTable() {
        return getTableName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTableName() {
        if (this.tableName == null) {
            this.tableName = getStringFromBytes(this.tableNameStart, this.tableNameLength);
        }

        return this.tableName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isUniqueKey() {
        return ((this.colFlag & 4) > 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isUnsigned() {
        return ((this.colFlag & 32) > 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isZeroFill() {
        return ((this.colFlag & 64) > 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
    	return this.getDatabaseName() + " . " +  this.getTableName() + "(" + this.getOriginalTableName() + ") . " + this.getName() + "(" + this.getOriginalName() + ")" + ", Mysql type: " + getMysqlType(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
       
    }

    int getDecimals() {
        return this.colDecimals;
    }

    boolean isNotNull() {
        return ((this.colFlag & 1) > 0);
    }
    
    boolean isOpaqueBinary() throws SQLException {
    	
    	//
    	// Detect CHAR(n) CHARACTER SET BINARY which is a synonym for
    	// fixed-length binary types
    	//
    	
    	if (this.charsetIndex == 63 
    		&& isBinary() 
    		&& this.getMysqlType() == MysqlDefs.FIELD_TYPE_STRING) {
    		
			if (this.originalTableNameLength == 0) {
				return false; // Probably from function
			}
			
    		// Okay, queries resolved by temp tables also have this 'signature',
    		// check for that
    		
    		return !isImplicitTemporaryTable();
    	}
    	
    	return (this.connection.versionMeetsMinimum(4, 1, 0) &&
    			"binary".equalsIgnoreCase(getCharacterSet()));
    			
    }

	/**
	 * Is this field owned by a server-created temporary table?
	 * @return
	 */
	private boolean isImplicitTemporaryTable() {
		return this.isImplicitTempTable;
	}
	
	private void checkForImplicitTemporaryTable() {
		this.isImplicitTempTable = this.tableNameLength > 5 
		&& this.buffer[tableNameStart] == (byte)'#'
		&& this.buffer[tableNameStart + 1] == (byte)'s'
		&& this.buffer[tableNameStart + 2] == (byte)'q'
		&& this.buffer[tableNameStart + 3] == (byte)'l'
		&& this.buffer[tableNameStart + 4] == (byte)'_';
	}
	
    /**
     * Is this field _definitely_ not writable?
     * 
     * @return true if this field can not be written to in an INSERT/UPDATE
     * statement.
     */
    boolean isReadOnly() throws SQLException {
    	if (this.connection.versionMeetsMinimum(4, 1, 0)) {
    		String orgColumnName = getOriginalName();
    		String orgTableName = getOriginalTableName();
    		
    		return !(orgColumnName != null && orgColumnName.length() > 0 &&
    				orgTableName != null && orgTableName.length() > 0);
    	}
    		
    	return false; // we can't tell definitively in this case.
    }
    
    void setMysqlType(int type) {
    	this.mysqlType = type;
    	this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
    }

    /**
     * Create a string with the correct charset encoding from the
     * byte-buffer that contains the data for this field
     */
    private String getStringFromBytes(int stringStart, int stringLength) {
        if ((stringStart == -1) || (stringLength == -1)) {
            return null;
        }

        String stringVal = null;

        if (this.connection != null) {
            if (this.connection.getUseUnicode()) {
            	String encoding = this.connection.getCharacterSetMetadata();
                
                if (encoding == null) {
                	encoding = connection.getEncoding();
                }

                if (encoding != null) {
                    SingleByteCharsetConverter converter = null;

                    if (this.connection != null) {
                        converter = this.connection.getCharsetConverter(encoding);
                    }

                    if (converter != null) { // we have a converter
                        stringVal = converter.toString(this.buffer, stringStart,
                                stringLength);
                    } else {
                        // we have no converter, use JVM converter 
                        byte[] stringBytes = new byte[stringLength];

                        int endIndex = stringStart + stringLength;
                        int pos = 0;

                        for (int i = stringStart; i < endIndex; i++) {
                            stringBytes[pos++] = this.buffer[i];
                        }

                        try {
                            stringVal = new String(stringBytes, encoding);
                        } catch (UnsupportedEncodingException ue) {
                            throw new RuntimeException(
                                Messages.getString("Field.12") + encoding //$NON-NLS-1$
                                + Messages.getString("Field.13")); //$NON-NLS-1$
                        }
                    }
                } else {
                    // we have no encoding, use JVM standard charset
                    stringVal = StringUtils.toAsciiString(this.buffer, stringStart,
                            stringLength);
                }
            } else {
                // we are not using unicode, so use JVM standard charset 
                stringVal = StringUtils.toAsciiString(this.buffer, stringStart,
                        stringLength);
            }
        } else {
            // we don't have a connection, so punt 
            stringVal = StringUtils.toAsciiString(this.buffer, stringStart,
                    stringLength);
        }

        return stringVal;
    }

	//
	// MySQL only has one protocol-level BLOB type that it exposes
	// which is FIELD_TYPE_BLOB, although we can divine what the
	// actual type is by the length reported ...
	//
	private void setBlobTypeBasedOnLength() {
		 if (this.length == MysqlDefs.LENGTH_TINYBLOB) {
		 	this.mysqlType = MysqlDefs.FIELD_TYPE_TINY_BLOB;
		 } else if (this.length == MysqlDefs.LENGTH_BLOB) {
		 	this.mysqlType = MysqlDefs.FIELD_TYPE_BLOB;
		 } else if (this.length == MysqlDefs.LENGTH_MEDIUMBLOB) {
		 	this.mysqlType = MysqlDefs.FIELD_TYPE_MEDIUM_BLOB;
		 } else if (this.length == MysqlDefs.LENGTH_LONGBLOB) {
		 	this.mysqlType = MysqlDefs.FIELD_TYPE_LONG_BLOB;
		 }
	}
}
