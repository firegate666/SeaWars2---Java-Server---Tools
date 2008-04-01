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
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import com.mysql.jdbc.log.Jdk14Logger;
import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.StandardLogger;


/**
 * Represents configurable properties for Connections and DataSources. Can also
 * expose properties as JDBC DriverPropertyInfo if required as well.
 *
 * @author Mark Matthews
 * @version $Id: ConnectionProperties.java,v 1.2 2008-04-01 22:49:35 firegate666 Exp $
 */
public class ConnectionProperties {
    private static final String STANDARD_LOGGER_NAME = StandardLogger.class.getName();
	private static final ArrayList PROPERTY_LIST = new ArrayList();
    private static final String CONNECTION_AND_AUTH_CATEGORY = "Connection/Authentication";
    private static final String HA_CATEGORY = "High Availability and Clustering";
    private static final String SECURITY_CATEGORY = "Security";
    private static final String PERFORMANCE_CATEGORY = "Performance Extensions";
    private static final String DEBUGING_PROFILING_CATEGORY = "Debuging/Profiling";
    private static final String MISC_CATEGORY = "Miscellaneous";
    private static final String[] propertyCategories = new String[] {
            CONNECTION_AND_AUTH_CATEGORY, HA_CATEGORY, SECURITY_CATEGORY,
            PERFORMANCE_CATEGORY, DEBUGING_PROFILING_CATEGORY, MISC_CATEGORY
        };
    protected static final String ZERO_DATETIME_BEHAVIOR_EXCEPTION = "exception";
    protected static final String ZERO_DATETIME_BEHAVIOR_ROUND = "round";
    protected static final String ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL = "convertToNull";
    
    static {
        try {
            java.lang.reflect.Field[] declaredFields = ConnectionProperties.class.getDeclaredFields();

            for (int i = 0; i < declaredFields.length; i++) {
                if (ConnectionProperties.ConnectionProperty.class.isAssignableFrom(
                            declaredFields[i].getType())) {
                    PROPERTY_LIST.add(declaredFields[i]);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    private BooleanConnectionProperty alwaysSendSetIsolation = new BooleanConnectionProperty("alwaysSendSetIsolation", true, "Should the driver always communicate with the database when "
    		+" Connection.setTransactionIsolation() is called? " 
    		+ "If set to false, the driver will only communicate with the " 
    		+ "database when the requested transaction isolation is different " 
    		+ "than the whichever is newer, the last value that was set via " 
    		+ "Connection.setTransactionIsolation(), or the value that was read from "
    		+ "the server when the connection was established.", "3.1.7", PERFORMANCE_CATEGORY, Integer.MAX_VALUE);
    
    private BooleanConnectionProperty allowLoadLocalInfile = new BooleanConnectionProperty("allowLoadLocalInfile",
            true,
            "Should the driver allow use of 'LOAD DATA LOCAL INFILE...' (defaults to 'true').",
            "3.0.3", SECURITY_CATEGORY, Integer.MAX_VALUE);
    private BooleanConnectionProperty allowNanAndInf = new BooleanConnectionProperty("allowNanAndInf", false, "Should the driver allow NaN or +/- INF values in PreparedStatement.setDouble()?", "3.1.5", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty allowUrlInLocalInfile = new BooleanConnectionProperty("allowUrlInLocalInfile", false, "Should the driver allow URLs in 'LOAD DATA LOCAL INFILE' statements?", "3.1.4", SECURITY_CATEGORY, Integer.MAX_VALUE);
    private BooleanConnectionProperty allowMultiQueries = new BooleanConnectionProperty("allowMultiQueries",
            false,
            "Allow the use of ';' to delimit multiple queries during one statement (true/false, defaults to 'false'",
            "3.1.1", SECURITY_CATEGORY, 1);
    private BooleanConnectionProperty autoDeserialize = new BooleanConnectionProperty("autoDeserialize", false, 
    		"Should the driver automatically detect and de-serialize objects stored in BLOB fields?", "3.1.5", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty autoReconnect = new BooleanConnectionProperty("autoReconnect",
            false, "Should the driver try to re-establish bad connections?",
            "1.1", HA_CATEGORY, 0);
    private BooleanConnectionProperty autoReconnectForPools = new BooleanConnectionProperty("autoReconnectForPools",
            false,
            "Use a reconnection strategy appropriate for connection pools (defaults to 'false')",
            "3.1.3", HA_CATEGORY, 1);
    private MemorySizeConnectionProperty blobSendChunkSize = new MemorySizeConnectionProperty("blobSendChunkSize", 1024 * 1024, 1, 
    		Integer.MAX_VALUE, 
    		"Chunk to use when sending BLOB/CLOBs via ServerPreparedStatements", "3.2.1", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty cacheCallableStatements = new BooleanConnectionProperty("cacheCallableStmts",
            false,
            "Should the driver cache the parsing stage of CallableStatements",
            "3.1.2", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty cacheServerConfiguration = new BooleanConnectionProperty("cacheServerConfiguration", false, "Should the driver cache the results of " 
    		+ "'SHOW VARIABLES' and 'SHOW COLLATION' on a per-URL basis?", "3.1.5", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty cachePreparedStatements = new BooleanConnectionProperty("cachePrepStmts",
            false,
            "Should the driver cache the parsing stage of PreparedStatements?",
            "3.0.10", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty cacheResultSetMetadata = new BooleanConnectionProperty("cacheResultSetMetadata",
            false,
            "Should the driver cache ResultSetMetaData for Statements and PreparedStatements? (Req. JDK-1.4+, true/false, default 'false')",
            "3.1.1", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty capitalizeTypeNames = new BooleanConnectionProperty("capitalizeTypeNames",
            false,
            "Capitalize type names in DatabaseMetaData? (usually only useful when using WebObjects, true/false, defaults to 'false')",
            "2.0.7", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty clobberStreamingResults = new BooleanConnectionProperty("clobberStreamingResults",
            false,
            "This will cause a 'streaming' ResultSet to be automatically closed, " +
            "and any outstanding data still streaming from the server to be discarded if another query is executed " +
            "before all the data has been read from the server.", "3.0.9",
            MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty continueBatchOnError = new BooleanConnectionProperty("continueBatchOnError",
            true,
            "Should the driver continue processing batch commands if " +
            "one statement fails. The JDBC spec allows either way (defaults to 'true').",
            "3.0.3", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty detectServerPreparedStmts = new BooleanConnectionProperty("useServerPrepStmts",
            true,
            "Use server-side prepared statements if the server supports them? (defaults to 'true').",
            "3.1.0", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty dontTrackOpenResources = new BooleanConnectionProperty("dontTrackOpenResources", false, 
    		"The JDBC specification requires the driver to automatically track and close resources, " + 
    		"however if your application doesn't do a good job of " +
    		"explicitly calling close() on statements or result sets, " +
    		"this can cause memory leakage. Setting this property to true " + 
    		"relaxes this constraint, and can be more memory efficient for " +
    		"some applications.", "3.1.7", PERFORMANCE_CATEGORY, 
    		Integer.MIN_VALUE); 
    private BooleanConnectionProperty emulateUnsupportedPstmts = new BooleanConnectionProperty("emulateUnsupportedPstmts", true,
    		"Should the driver detect prepared statements that are not supported by the server, and " +
    		"replace them with client-side emulated versions?","3.1.7", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty dumpQueriesOnException = new BooleanConnectionProperty("dumpQueriesOnException",
            false,
            "Should the driver dump the contents of the query sent to the server in the message for SQLExceptions?",
            "3.1.3", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty dynamicCalendars = new BooleanConnectionProperty("dynamicCalendars", false, "Should the driver retrieve the default"
    	+ " calendar when required, or cache it per connection/session?", "3.1.5", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty elideSetAutoCommits = new BooleanConnectionProperty("elideSetAutoCommits",
            false,
            "If using MySQL-4.1 or newer, should the driver only issue 'set autocommit=n' queries when the server's state doesn't match the requested state by Connection.setAutoCommit(boolean)?",
            "3.1.3", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty emptyStringsConvertToZero = new BooleanConnectionProperty("emptyStringsConvertToZero", true,
    		"Should the driver allow conversions from empty string "
    		+ "fields to numeric values of '0'?", "3.1.8", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty emulateLocators = new BooleanConnectionProperty("emulateLocators",
            false, "N/A", "3.1.0", MISC_CATEGORY, Integer.MIN_VALUE);
	private MemorySizeConnectionProperty locatorFetchBufferSize = new MemorySizeConnectionProperty("locatorFetchBufferSize", 1024 * 1024, 
			0, Integer.MAX_VALUE, "If 'emulateLocators' is configured to 'true', what size "
			+ " buffer should be used when fetching BLOB data for getBinaryInputStream?", "3.2.1", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty enablePacketDebug = new BooleanConnectionProperty("enablePacketDebug",
            false,
            "When enabled, a ring-buffer of 'packetDebugBufferSize' packets will be kept, and dumped when exceptions are thrown in key areas in the driver's code",
            "3.1.3", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty enableDeprecatedAutoreconnect = new BooleanConnectionProperty("enableDeprecatedAutoreconnect",
    		false,
    		"Auto-reconnect functionality is deprecated starting with version 3.2, and will be removed in version 3.3. Set this " +
    		"property to 'true' to disable the check for the feature being configured.",
    		"3.2.1",
    		HA_CATEGORY,
    		Integer.MIN_VALUE);
    private BooleanConnectionProperty explainSlowQueries = new BooleanConnectionProperty("explainSlowQueries",
            false,
            "If 'logSlowQueries' is enabled, should the driver automatically issue an 'EXPLAIN' on the" +
            " server and send the results to the configured log at a WARN level?",
            "3.1.2", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);

    /** When failed-over, set connection to read-only? */
    private BooleanConnectionProperty failOverReadOnly = new BooleanConnectionProperty("failOverReadOnly",
            true,
            "When failing over in autoReconnect mode, should the connection be set to 'read-only'?",
            "3.0.12", HA_CATEGORY, 2);
    private BooleanConnectionProperty gatherPerformanceMetrics = new BooleanConnectionProperty("gatherPerfMetrics",
            false,
            "Should the driver gather performance metrics, and report them via the configured logger every 'reportMetricsIntervalMillis' milliseconds?",
            "3.1.2", DEBUGING_PROFILING_CATEGORY, 1);
    private BooleanConnectionProperty holdResultsOpenOverStatementClose = new BooleanConnectionProperty("holdResultsOpenOverStatementClose", false, 
    		"Should the driver close result sets on Statement.close() as required by the JDBC specification?", 
    		"3.1.7", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty ignoreNonTxTables = new BooleanConnectionProperty("ignoreNonTxTables",
            false,
            "Ignore non-transactional table warning for rollback? (defaults to 'false').",
            "3.0.9", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty isInteractiveClient = new BooleanConnectionProperty("interactiveClient",
            false,
            "Set the CLIENT_INTERACTIVE flag, which tells MySQL " +
            "to timeout connections based on INTERACTIVE_TIMEOUT instead of WAIT_TIMEOUT",
            "3.1.0", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty jdbcCompliantTruncation = new BooleanConnectionProperty("jdbcCompliantTruncation",
            true,
            "Should the driver throw java.sql.DataTruncation" +
            " exceptions when data is truncated as is required by the JDBC specification when connected to a server that supports warnings" +
            "(MySQL 4.1.0 and newer)?", "3.1.2", MISC_CATEGORY,
            Integer.MIN_VALUE);
    private BooleanConnectionProperty logSlowQueries = new BooleanConnectionProperty("logSlowQueries",
            false,
            "Should queries that take longer than 'slowQueryThresholdMillis' be logged?",
            "3.1.2", DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty paranoid = new BooleanConnectionProperty("paranoid",
            false,
            "Take measures to prevent exposure sensitive information in error messages and clear " +
            "data structures holding sensitive data when possible? (defaults to 'false')",
            "3.0.1", SECURITY_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty pedantic = new BooleanConnectionProperty("pedantic",
            false, "Follow the JDBC spec to the letter.", "3.0.0",
            MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty profileSQL = new BooleanConnectionProperty("profileSQL",
            false,
            "Trace queries and their execution/fetch times to the configured logger (true/false) defaults to 'false'",
            "3.1.0", DEBUGING_PROFILING_CATEGORY, 1);
    private StringConnectionProperty propertiesTransform = new StringConnectionProperty(NonRegisteringDriver.PROPERTIES_TRANSFORM_KEY, null, "An implementation of com.mysql.jdbc.ConnectionPropertiesTransform that the driver will use to modify URL properties passed to the driver before attempting a connection", "3.1.4", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty reconnectAtTxEnd = new BooleanConnectionProperty("reconnectAtTxEnd",
            false,
            "If autoReconnect is set to true, should the driver attempt reconnections" +
            "at the end of every transaction?", "3.0.10", HA_CATEGORY, 4);
    private BooleanConnectionProperty relaxAutoCommit = new BooleanConnectionProperty("relaxAutoCommit",
            false,
            "If the version of MySQL the driver connects to does not support transactions, still allow calls to commit(), rollback() and setAutoCommit() (true/false, defaults to 'false')?",
            "2.0.13", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty requireSSL = new BooleanConnectionProperty("requireSSL",
            false,
            "Require SSL connection if useSSL=true? (defaults to 'false').",
            "3.1.0", SECURITY_CATEGORY, 3);
    private BooleanConnectionProperty rollbackOnPooledClose = new BooleanConnectionProperty("rollbackOnPooledClose",
            true,
            "Should the driver issue a rollback() when the logical connection in a pool is closed?",
            "3.0.15", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty roundRobinLoadBalance = new BooleanConnectionProperty("roundRobinLoadBalance",
            false,
            "When autoReconnect is enabled, and failoverReadonly is false, should we pick hosts to connect to on a round-robin basis?",
            "3.1.2", HA_CATEGORY, 5);
    private BooleanConnectionProperty runningCTS13 = new BooleanConnectionProperty("runningCTS13", false, 
    		"Enables workarounds for bugs in Sun's JDBC compliance testsuite version 1.3", 
    		"3.1.7", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty strictFloatingPoint = new BooleanConnectionProperty("strictFloatingPoint",
            false, "Used only in older versions of compliance test", "3.0.0",
            MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty strictUpdates = new BooleanConnectionProperty("strictUpdates",
            true,
            "Should the driver do strict checking (all primary keys selected) of updatable result sets (true, false, defaults to 'true')?",
            "3.0.4", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty traceProtocol = new BooleanConnectionProperty("traceProtocol",
            false, "Should trace-level network protocol be logged?", "3.1.2",
            DEBUGING_PROFILING_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty tinyInt1isBit = new BooleanConnectionProperty("tinyInt1isBit", true, "Should the driver treat the datatype TINYINT(1) as the BIT type " +
    		"(because the server silently converts BIT -> TINYINT(1) when creating tables)?", "3.0.16", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useCompression = new BooleanConnectionProperty("useCompression",
            false,
            "Use zlib compression when communicating with the server (true/false)? Defaults to 'false'.",
            "3.1.0", CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE);
    private StringConnectionProperty useConfig = new StringConnectionProperty("useConfigs", null, "Load the comma-delimited list of configuration properties before parsing the " +
    		"URL or applying user-specified properties. These configurations are explained in the 'Configurations' of the documentation.", "3.1.5", CONNECTION_AND_AUTH_CATEGORY, Integer.MAX_VALUE);
    private BooleanConnectionProperty useFastIntParsing = new BooleanConnectionProperty("useFastIntParsing",
            true,
            "Use internal String->Integer conversion routines to avoid excessive object creation?",
            "3.1.4", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useHostsInPrivileges = new BooleanConnectionProperty("useHostsInPrivileges",
            true,
            "Add '@hostname' to users in DatabaseMetaData.getColumn/TablePrivileges() (true/false), defaults to 'true'.",
            "3.0.2", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useLocalSessionState = new BooleanConnectionProperty("useLocalSessionState", false,
    		"Should the driver refer to the internal values of autocommit and transaction isolation that are set "
    		+ " by Connection.setAutoCommit() and Connection.setTransactionIsolation(), rather than querying the database?",
    		"3.1.7", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useNewIo = new BooleanConnectionProperty("useNewIO",
            false,
            "Should the driver use the java.nio.* interfaces for network communication (true/false), defaults to 'false'",
            "3.1.0", PERFORMANCE_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useOldUTF8Behavior = new BooleanConnectionProperty("useOldUTF8Behavior", false, 
    		"Use the UTF-8 behavior the driver did when communicating with 4.0 and older servers", "3.1.6", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useOnlyServerErrorMessages = new BooleanConnectionProperty("useOnlyServerErrorMessages",
            true,
            "Don't prepend 'standard' SQLState error messages to error messages returned by the server.",
            "3.0.15", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useReadAheadInput = new BooleanConnectionProperty("useReadAheadInput", true, 
    		"Use newer, optimized non-blocking, buffered input stream when reading from the server?", "3.1.5", PERFORMANCE_CATEGORY, 
			Integer.MIN_VALUE);
    private BooleanConnectionProperty useSSL = new BooleanConnectionProperty("useSSL",
            false,
            "Use SSL when communicating with the server (true/false), defaults to 'false'",
            "3.0.2", SECURITY_CATEGORY, 2);
    private BooleanConnectionProperty useSqlStateCodes = new BooleanConnectionProperty("useSqlStateCodes",
            true,
            "Use SQL Standard state codes instead of 'legacy' X/Open/SQL state codes (true/false), default is 'true'",
            "3.1.3", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useStreamLengthsInPrepStmts = new BooleanConnectionProperty("useStreamLengthsInPrepStmts",
            true,
            "Honor stream length parameter in " +
            "PreparedStatement/ResultSet.setXXXStream() method calls (true/false, defaults to 'true')?",
            "3.0.2", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useTimezone = new BooleanConnectionProperty("useTimezone",
            false,
            "Convert time/date types between client and server timezones (true/false, defaults to 'false')?",
            "3.0.2", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useUltraDevWorkAround = new BooleanConnectionProperty("ultraDevHack",
            false,
            "Create PreparedStatements for prepareCall() when required, because UltraDev " +
            " is broken and issues a prepareCall() for _all_ statements? (true/false, defaults to 'false')",
            "2.0.3", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useUnbufferedInput = new BooleanConnectionProperty("useUnbufferedInput",
            true,
            "Don't use BufferedInputStream for reading data from the server",
            "3.0.11", MISC_CATEGORY, Integer.MIN_VALUE);
    private BooleanConnectionProperty useUnicode = new BooleanConnectionProperty("useUnicode",
            false,
            "Should the driver use Unicode character encodings when handling strings? Should only be used when the driver can't determine the character set mapping, or you are trying to 'force' the driver to use a character set that MySQL either doesn't natively support (such as UTF-8), true/false, defaults to 'true'",
            "1.1g", MISC_CATEGORY, 0);
    private BooleanConnectionProperty useUsageAdvisor = new BooleanConnectionProperty("useUsageAdvisor",
            false,
            "Should the driver issue 'usage' warnings advising proper and efficient usage of JDBC and MySQL Connector/J to the log (true/false, defaults to 'false')?",
            "3.1.1", DEBUGING_PROFILING_CATEGORY, 10);
    private IntegerConnectionProperty callableStatementCacheSize = new IntegerConnectionProperty("callableStmtCacheSize",
            100, 0, Integer.MAX_VALUE,
            "If 'cacheCallableStmts' is enabled, how many callable statements should be cached?",
            "3.1.2", PERFORMANCE_CATEGORY, 5);
    private IntegerConnectionProperty connectTimeout = new IntegerConnectionProperty("connectTimeout",
            0, 0, Integer.MAX_VALUE,
            "Timeout for socket connect (in milliseconds), with 0 being no timeout. " +
            "Only works on JDK-1.4 or newer. Defaults to '0'.", "3.0.1",
            CONNECTION_AND_AUTH_CATEGORY, 9);
    private IntegerConnectionProperty initialTimeout = new IntegerConnectionProperty("initialTimeout",
            2, 1, Integer.MAX_VALUE,
            "If autoReconnect is enabled, the" +
            " initial time to wait between" +
            " re-connect attempts (in seconds, defaults to '2').", "1.1",
            HA_CATEGORY, 5);
    private IntegerConnectionProperty maxQuerySizeToLog = new IntegerConnectionProperty("maxQuerySizeToLog",
            2048, 0, Integer.MAX_VALUE,
            "Controls the maximum length/size of a query that will get logged when profiling or tracing",
            "3.1.3", DEBUGING_PROFILING_CATEGORY, 4);
    private IntegerConnectionProperty maxReconnects = new IntegerConnectionProperty("maxReconnects",
            3, 1, Integer.MAX_VALUE,
            "Maximum number of reconnects to attempt if autoReconnect is true, default is '3'.",
            "1.1", HA_CATEGORY, 4);
    private IntegerConnectionProperty maxRows = new IntegerConnectionProperty("maxRows",
            -1, -1, Integer.MAX_VALUE,
            "The maximum number of rows to return " +
            " (0, the default means return all rows).", "all versions",
            MISC_CATEGORY, Integer.MIN_VALUE);
    private IntegerConnectionProperty metadataCacheSize = new IntegerConnectionProperty("metadataCacheSize",
            50, 1, Integer.MAX_VALUE,
            "The number of queries to cache" +
            "ResultSetMetadata for if cacheResultSetMetaData is set to 'true' (default 50)",
            "3.1.1", PERFORMANCE_CATEGORY, 5);
    private BooleanConnectionProperty noDatetimeStringSync = new BooleanConnectionProperty("noDatetimeStringSync", false, 
    		"Don't ensure that ResultSet.getDatetimeType().toString().equals(ResultSet.getString())", 
    		"3.1.7", MISC_CATEGORY, Integer.MIN_VALUE);
	private BooleanConnectionProperty nullCatalogMeansCurrent = new BooleanConnectionProperty("nullCatalogMeansCurrent",
			true, "When DatabaseMetadataMethods ask for a 'catalog' parameter, does the value null mean use the current catalog? " 
			+ "(this is not JDBC-compliant, but follows legacy behavior from earlier versions of the driver)",
			"3.1.8", MISC_CATEGORY, Integer.MIN_VALUE);	
	private BooleanConnectionProperty nullNamePatternMatchesAll = new BooleanConnectionProperty("nullNamePatternMatchesAll",
			true, "Should DatabaseMetaData methods that accept *pattern parameters treat null the same as '%' "
			+ " (this is not JDBC-compliant, however older versions of the driver accepted this departure from the specification)",
			"3.1.8", MISC_CATEGORY, Integer.MIN_VALUE);
    private IntegerConnectionProperty packetDebugBufferSize = new IntegerConnectionProperty("packetDebugBufferSize",
            20, 0, Integer.MAX_VALUE,
            "The maximum number of packets to retain when 'enablePacketDebug' is true",
            "3.1.3", DEBUGING_PROFILING_CATEGORY, 7);
    private IntegerConnectionProperty preparedStatementCacheSize = new IntegerConnectionProperty("prepStmtCacheSize",
            25, 0, Integer.MAX_VALUE,
            "If prepared statement caching is enabled, " +
            "how many prepared statements should be cached?", "3.0.10",
            PERFORMANCE_CATEGORY, 10);
    private IntegerConnectionProperty preparedStatementCacheSqlLimit = new IntegerConnectionProperty("prepStmtCacheSqlLimit",
            256, 1, Integer.MAX_VALUE,
            "If prepared statement caching is enabled, " +
            "what's the largest SQL the driver will cache the parsing for?",
            "3.0.10", PERFORMANCE_CATEGORY, 11);
    private IntegerConnectionProperty queriesBeforeRetryMaster = new IntegerConnectionProperty("queriesBeforeRetryMaster",
            50, 1, Integer.MAX_VALUE,
            "Number of queries to issue before falling back to master when failed over " +
            "(when using multi-host failover). Whichever condition is met first, " +
            "'queriesBeforeRetryMaster' or 'secondsBeforeRetryMaster' will cause an " +
            "attempt to be made to reconnect to the master. Defaults to 50.",
            "3.0.2", HA_CATEGORY, 7);
    private IntegerConnectionProperty reportMetricsIntervalMillis = new IntegerConnectionProperty("reportMetricsIntervalMillis",
            30000, 0, Integer.MAX_VALUE,
            "If 'gatherPerfMetrics' is enabled, how often should they be logged (in ms)?",
            "3.1.2", DEBUGING_PROFILING_CATEGORY, 3);
    private IntegerConnectionProperty secondsBeforeRetryMaster = new IntegerConnectionProperty("secondsBeforeRetryMaster",
            30, 1, Integer.MAX_VALUE,
            "How long should the driver wait, when failed over, before attempting " +
            "to reconnect to the master server? Whichever condition is met first, " +
            "'queriesBeforeRetryMaster' or 'secondsBeforeRetryMaster' will cause an " +
            "attempt to be made to reconnect to the master. Time in seconds, defaults to 30",
            "3.0.2", HA_CATEGORY, 8);
    private IntegerConnectionProperty slowQueryThresholdMillis = new IntegerConnectionProperty("slowQueryThresholdMillis",
            2000, 0, Integer.MAX_VALUE,
            "If 'logSlowQueries' is enabled, how long should a query (in ms) before it is logged as 'slow'?",
            "3.1.2", DEBUGING_PROFILING_CATEGORY, 9);
    private IntegerConnectionProperty socketTimeout = new IntegerConnectionProperty("socketTimeout",
            0, 0, Integer.MAX_VALUE,
            "Timeout on network socket operations (0, the default means no timeout).",
            "3.0.1", CONNECTION_AND_AUTH_CATEGORY, 10);
    private String characterEncodingAsString = null;
    private StringConnectionProperty characterEncoding = new StringConnectionProperty("characterEncoding",
            null,
            "If 'useUnicode' is set to true, what character encoding should the driver use when dealing with strings? (defaults is to 'autodetect')",
            "1.1g", MISC_CATEGORY, 5);
    private StringConnectionProperty characterSetResults = new StringConnectionProperty("characterSetResults",
            null, "Character set to tell the server to return results as.",
            "3.0.13", MISC_CATEGORY, 6);
    private StringConnectionProperty connectionCollation = new StringConnectionProperty("connectionCollation",
            null,
            "If set, tells the server to use this collation via 'set connection_collation'",
            "3.0.13", MISC_CATEGORY, 7);
    private StringConnectionProperty loggerClassName = new StringConnectionProperty("logger",
            STANDARD_LOGGER_NAME,
            "The name of a class that implements '" + Log.class.getName() +
            "' that will be used to log messages to." + "(default is '" +
            STANDARD_LOGGER_NAME + "', which " + "logs to STDERR)",
            "3.1.1", DEBUGING_PROFILING_CATEGORY, 0);
    private StringConnectionProperty profileSql = new StringConnectionProperty("profileSql",
            null,
            "Deprecated, use 'profileSQL' instead. Trace queries and their execution/fetch times on STDERR (true/false) defaults to 'false'",
            "2.0.14", DEBUGING_PROFILING_CATEGORY, 3);
    private StringConnectionProperty serverTimezone = new StringConnectionProperty("serverTimezone",
            null,
            "Override detection/mapping of timezone. Used when timezone from server doesn't map to Java timezone",
            "3.0.2", MISC_CATEGORY, Integer.MIN_VALUE);
    private StringConnectionProperty sessionVariables = new StringConnectionProperty("sessionVariables",
    		null, "A comma-separated list of name/value pairs to be sent as SET SESSION ... to "
    		+ " the server when the driver connects.", "3.1.8", MISC_CATEGORY, Integer.MAX_VALUE);
    private StringConnectionProperty socketFactoryClassName = new StringConnectionProperty("socketFactory",
            StandardSocketFactory.class.getName(),
            "The name of the class that the driver should use for creating socket connections to the server. This class must implement the interface 'com.mysql.jdbc.SocketFactory' and have public no-args constructor.",
            "3.0.3", CONNECTION_AND_AUTH_CATEGORY, 4);
    private StringConnectionProperty zeroDateTimeBehavior = new StringConnectionProperty("zeroDateTimeBehavior", 
    		ZERO_DATETIME_BEHAVIOR_EXCEPTION, 
			new String[] {ZERO_DATETIME_BEHAVIOR_EXCEPTION, 
    			ZERO_DATETIME_BEHAVIOR_ROUND, 
				ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL}, 
			"What should happen when the driver encounters DATETIME values that are composed " 
			+ "entirely of zeroes (used by MySQL to represent invalid dates)? "
			+ "Valid values are '" + ZERO_DATETIME_BEHAVIOR_EXCEPTION + "', '" 
			+ ZERO_DATETIME_BEHAVIOR_ROUND + "' and '" 
			+ ZERO_DATETIME_BEHAVIOR_CONVERT_TO_NULL + "'." , 
			"3.1.4", MISC_CATEGORY, Integer.MIN_VALUE);
    
    private boolean autoReconnectForPoolsAsBoolean = false;
    private boolean cacheResultSetMetaDataAsBoolean;
    private boolean highAvailabilityAsBoolean = false;
    private boolean profileSQLAsBoolean = false;
    private boolean reconnectTxAtEndAsBoolean = false;

    // Cache these values, they are 'hot'
    private boolean useUnicodeAsBoolean = true;
    private boolean useUsageAdvisorAsBoolean = false;
    private int maxRowsAsInt = -1;
    private boolean useOldUTF8BehaviorAsBoolean = false;

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setAllowLoadLocalInfile(boolean property) {
        this.allowLoadLocalInfile.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getAllowLoadLocalInfile() {
        return this.allowLoadLocalInfile.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setAllowMultiQueries(boolean property) {
        this.allowMultiQueries.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getAllowMultiQueries() {
        return this.allowMultiQueries.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The autoReconnect to set.
     */
    public void setAutoReconnect(boolean flag) {
        this.autoReconnect.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setAutoReconnectForConnectionPools(boolean property) {
        this.autoReconnectForPools.setValue(property);
        this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The autoReconnectForPools to set.
     */
    public void setAutoReconnectForPools(boolean flag) {
        this.autoReconnectForPools.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getAutoReconnectForPools() {
        return this.autoReconnectForPoolsAsBoolean;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The cacheCallableStatements to set.
     */
    public void setCacheCallableStatements(boolean flag) {
        this.cacheCallableStatements.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns if cacheCallableStatements is enabled
     */
    public boolean getCacheCallableStatements() {
        return this.cacheCallableStatements.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The cachePreparedStatements to set.
     */
    public void setCachePreparedStatements(boolean flag) {
        this.cachePreparedStatements.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the cachePreparedStatements.
     */
    public boolean getCachePreparedStatements() {
        return ((Boolean) this.cachePreparedStatements.getValueAsObject()).booleanValue();
    }

    /**
     * Sets whether or not we should cache result set metadata.
     *
     * @param property
     */
    public void setCacheResultSetMetadata(boolean property) {
        this.cacheResultSetMetadata.setValue(property);
        this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getCacheResultSetMetadata() {
        return this.cacheResultSetMetaDataAsBoolean;
    }

    /**
     * Configures the number of callable statements to cache. (this is
     * configurable during the life of the connection).
     *
     * @param size The callableStatementCacheSize to set.
     */
    public void setCallableStatementCacheSize(int size) {
        this.callableStatementCacheSize.setValue(size);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the callableStatementCacheSize.
     */
    public int getCallableStatementCacheSize() {
        return this.callableStatementCacheSize.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setCapitalizeDBMDTypes(boolean property) {
        this.capitalizeTypeNames.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The capitalizeTypeNames to set.
     */
    public void setCapitalizeTypeNames(boolean flag) {
        this.capitalizeTypeNames.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getCapitalizeTypeNames() {
        return this.capitalizeTypeNames.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param encoding The characterEncoding to set.
     */
    public void setCharacterEncoding(String encoding) {
        this.characterEncoding.setValue(encoding);
    }

    /**
     * DOCUMENT ME!
     *
     * @param characterSet The characterSetResults to set.
     */
    public void setCharacterSetResults(String characterSet) {
        this.characterSetResults.setValue(characterSet);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the characterSetResults.
     */
    public String getCharacterSetResults() {
        return this.characterSetResults.getValueAsString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The clobberStreamingResults to set.
     */
    public void setClobberStreamingResults(boolean flag) {
        this.clobberStreamingResults.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the clobberStreamingResults.
     */
    public boolean getClobberStreamingResults() {
        return this.clobberStreamingResults.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param timeoutMs
     */
    public void setConnectTimeout(int timeoutMs) {
        this.connectTimeout.setValue(timeoutMs);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public int getConnectTimeout() {
        return this.connectTimeout.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param collation The connectionCollation to set.
     */
    public void setConnectionCollation(String collation) {
        this.connectionCollation.setValue(collation);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the connectionCollation.
     */
    public String getConnectionCollation() {
        return this.connectionCollation.getValueAsString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setContinueBatchOnError(boolean property) {
        this.continueBatchOnError.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getContinueBatchOnError() {
        return this.continueBatchOnError.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setDetectServerPreparedStmts(boolean property) {
        this.detectServerPreparedStmts.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The dumpQueriesOnException to set.
     */
    public void setDumpQueriesOnException(boolean flag) {
        this.dumpQueriesOnException.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the dumpQueriesOnException.
     */
    public boolean getDumpQueriesOnException() {
        return this.dumpQueriesOnException.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The elideSetAutoCommits to set.
     */
    public void setElideSetAutoCommits(boolean flag) {
        this.elideSetAutoCommits.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the elideSetAutoCommits.
     */
    public boolean getElideSetAutoCommits() {
        return this.elideSetAutoCommits.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setEmulateLocators(boolean property) {
        this.emulateLocators.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getEmulateLocators() {
        return this.emulateLocators.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The enablePacketDebug to set.
     */
    public void setEnablePacketDebug(boolean flag) {
        this.enablePacketDebug.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the enablePacketDebug.
     */
    public boolean getEnablePacketDebug() {
        return this.enablePacketDebug.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setEncoding(String property) {
        this.characterEncoding.setValue(property);
        this.characterEncodingAsString = this.characterEncoding.getValueAsString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The explainSlowQueries to set.
     */
    public void setExplainSlowQueries(boolean flag) {
        this.explainSlowQueries.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the explainSlowQueries.
     */
    public boolean getExplainSlowQueries() {
        return this.explainSlowQueries.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The failOverReadOnly to set.
     */
    public void setFailOverReadOnly(boolean flag) {
        this.failOverReadOnly.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the failOverReadOnly.
     */
    public boolean getFailOverReadOnly() {
        return this.failOverReadOnly.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The gatherPerformanceMetrics to set.
     */
    public void setGatherPerformanceMetrics(boolean flag) {
        this.gatherPerformanceMetrics.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the gatherPerformanceMetrics.
     */
    public boolean getGatherPerformanceMetrics() {
        return this.gatherPerformanceMetrics.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setIgnoreNonTxTables(boolean property) {
        this.ignoreNonTxTables.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getIgnoreNonTxTables() {
        return this.ignoreNonTxTables.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setInitialTimeout(int property) {
        this.initialTimeout.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public int getInitialTimeout() {
        return this.initialTimeout.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getInteractiveClient() {
        return this.isInteractiveClient.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setIsInteractiveClient(boolean property) {
        this.isInteractiveClient.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the isInteractiveClient.
     */
    public boolean getIsInteractiveClient() {
        return this.isInteractiveClient.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The jdbcCompliantTruncation to set.
     */
    public void setJdbcCompliantTruncation(boolean flag) {
        this.jdbcCompliantTruncation.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the jdbcCompliantTruncation.
     */
    public boolean getJdbcCompliantTruncation() {
        return this.jdbcCompliantTruncation.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The logSlowQueries to set.
     */
    public void setLogSlowQueries(boolean flag) {
        this.logSlowQueries.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the logSlowQueries.
     */
    public boolean getLogSlowQueries() {
        return this.logSlowQueries.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setLogger(String property) {
        this.loggerClassName.setValueAsObject(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getLogger() {
    	return this.loggerClassName.getValueAsString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param className The loggerClassName to set.
     */
    public void setLoggerClassName(String className) {
        this.loggerClassName.setValue(className);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the loggerClassName.
     */
    public String getLoggerClassName() {
        return this.loggerClassName.getValueAsString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param sizeInBytes The maxQuerySizeToLog to set.
     */
    public void setMaxQuerySizeToLog(int sizeInBytes) {
        this.maxQuerySizeToLog.setValue(sizeInBytes);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the maxQuerySizeToLog.
     */
    public int getMaxQuerySizeToLog() {
        return this.maxQuerySizeToLog.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setMaxReconnects(int property) {
        this.maxReconnects.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public int getMaxReconnects() {
        return this.maxReconnects.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setMaxRows(int property) {
        this.maxRows.setValue(property);
        this.maxRowsAsInt = this.maxRows.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public int getMaxRows() {
        return this.maxRowsAsInt;
    }

    /**
     * Sets the number of queries that metadata can be cached if caching is
     * enabled.
     *
     * @param value the number of queries to cache metadata for.
     */
    public void setMetadataCacheSize(int value) {
        this.metadataCacheSize.setValue(value);
    }

    /**
     * Returns the number of queries that metadata can be cached if caching is
     * enabled.
     *
     * @return the number of queries to cache metadata for.
     */
    public int getMetadataCacheSize() {
        return this.metadataCacheSize.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param size The packetDebugBufferSize to set.
     */
    public void setPacketDebugBufferSize(int size) {
        this.packetDebugBufferSize.setValue(size);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the packetDebugBufferSize.
     */
    public int getPacketDebugBufferSize() {
        return this.packetDebugBufferSize.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setParanoid(boolean property) {
        this.paranoid.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getParanoid() {
        return this.paranoid.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setPedantic(boolean property) {
        this.pedantic.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getPedantic() {
        return this.pedantic.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param cacheSize The preparedStatementCacheSize to set.
     */
    public void setPreparedStatementCacheSize(int cacheSize) {
        this.preparedStatementCacheSize.setValue(cacheSize);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the preparedStatementCacheSize.
     */
    public int getPreparedStatementCacheSize() {
        return ((Integer) this.preparedStatementCacheSize.getValueAsObject()).intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param cacheSqlLimit The preparedStatementCacheSqlLimit to set.
     */
    public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) {
        this.preparedStatementCacheSqlLimit.setValue(cacheSqlLimit);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the preparedStatementCacheSqlLimit.
     */
    public int getPreparedStatementCacheSqlLimit() {
        return ((Integer) this.preparedStatementCacheSqlLimit.getValueAsObject()).intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The profileSQL to set.
     */
    public void setProfileSQL(boolean flag) {
        this.profileSQL.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the profileSQL flag
     */
    public boolean getProfileSQL() {
        return this.profileSQL.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setProfileSql(boolean property) {
        this.profileSQL.setValue(property);
        this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getProfileSql() {
        return this.profileSQLAsBoolean;
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setQueriesBeforeRetryMaster(int property) {
        this.queriesBeforeRetryMaster.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public int getQueriesBeforeRetryMaster() {
        return this.queriesBeforeRetryMaster.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setReconnectAtTxEnd(boolean property) {
        this.reconnectAtTxEnd.setValue(property);
        this.reconnectTxAtEndAsBoolean = this.reconnectAtTxEnd.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getReconnectAtTxEnd() {
        return this.reconnectTxAtEndAsBoolean;
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setRelaxAutoCommit(boolean property) {
        this.relaxAutoCommit.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getRelaxAutoCommit() {
        return this.relaxAutoCommit.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param millis The reportMetricsIntervalMillis to set.
     */
    public void setReportMetricsIntervalMillis(int millis) {
        this.reportMetricsIntervalMillis.setValue(millis);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the reportMetricsIntervalMillis.
     */
    public int getReportMetricsIntervalMillis() {
        return this.reportMetricsIntervalMillis.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setRequireSSL(boolean property) {
        this.requireSSL.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getRequireSSL() {
        return this.requireSSL.getValueAsBoolean();
    }

    /**
     * Sets whether or not hosts will be picked in a round-robin fashion.
     *
     * @param flag The roundRobinLoadBalance property to set.
     */
    public void setRoundRobinLoadBalance(boolean flag) {
        this.roundRobinLoadBalance.setValue(flag);
    }

    /**
     * Returns whether or not hosts will be picked in a round-robin fashion.
     *
     * @return Returns the roundRobinLoadBalance property.
     */
    public boolean getRoundRobinLoadBalance() {
        return this.roundRobinLoadBalance.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setSecondsBeforeRetryMaster(int property) {
        this.secondsBeforeRetryMaster.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public int getSecondsBeforeRetryMaster() {
        return this.secondsBeforeRetryMaster.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property DOCUMENT ME!
     */
    public void setServerTimezone(String property) {
        this.serverTimezone.setValue(property);
    }

    /**
     * Returns the 'serverTimezone' property.
     *
     * @return the configured server timezone property.
     */
    public String getServerTimezone() {
        return this.serverTimezone.getValueAsString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param millis The slowQueryThresholdMillis to set.
     */
    public void setSlowQueryThresholdMillis(int millis) {
        this.slowQueryThresholdMillis.setValue(millis);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the slowQueryThresholdMillis.
     */
    public int getSlowQueryThresholdMillis() {
        return this.slowQueryThresholdMillis.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setSocketFactoryClassName(String property) {
        this.socketFactoryClassName.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getSocketFactoryClassName() {
        return this.socketFactoryClassName.getValueAsString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setSocketTimeout(int property) {
        this.socketTimeout.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public int getSocketTimeout() {
        return this.socketTimeout.getValueAsInt();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setStrictFloatingPoint(boolean property) {
        this.strictFloatingPoint.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getStrictFloatingPoint() {
        return this.strictFloatingPoint.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setStrictUpdates(boolean property) {
        this.strictUpdates.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getStrictUpdates() {
        return this.strictUpdates.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The logProtocol to set.
     */
    public void setTraceProtocol(boolean flag) {
        this.traceProtocol.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the logProtocol.
     */
    public boolean getTraceProtocol() {
        return this.traceProtocol.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setUseCompression(boolean property) {
        this.useCompression.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getUseCompression() {
        return this.useCompression.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setUseHostsInPrivileges(boolean property) {
        this.useHostsInPrivileges.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getUseHostsInPrivileges() {
        return this.useHostsInPrivileges.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setUseNewIo(boolean property) {
        this.useNewIo.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getUseNewIo() {
        return this.useNewIo.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setUseSSL(boolean property) {
        this.useSSL.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getUseSSL() {
        return this.useSSL.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The detectServerPreparedStmts to set.
     */
    public void setUseServerPreparedStmts(boolean flag) {
        this.detectServerPreparedStmts.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getUseServerPreparedStmts() {
        return this.detectServerPreparedStmts.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The useSqlStateCodes to set.
     */
    public void setUseSqlStateCodes(boolean flag) {
        this.useSqlStateCodes.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the useSqlStateCodes state.
     */
    public boolean getUseSqlStateCodes() {
        return this.useSqlStateCodes.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setUseStreamLengthsInPrepStmts(boolean property) {
        this.useStreamLengthsInPrepStmts.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getUseStreamLengthsInPrepStmts() {
        return this.useStreamLengthsInPrepStmts.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setUseTimezone(boolean property) {
        this.useTimezone.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getUseTimezone() {
        return this.useTimezone.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    public void setUseUltraDevWorkAround(boolean property) {
        this.useUltraDevWorkAround.setValue(property);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getUseUltraDevWorkAround() {
        return this.useUltraDevWorkAround.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The useUnbufferedInput to set.
     */
    public void setUseUnbufferedInput(boolean flag) {
        this.useUnbufferedInput.setValue(flag);
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the useUnbufferedInput.
     */
    public boolean getUseUnbufferedInput() {
        return this.useUnbufferedInput.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag The useUnicode to set.
     */
    public void setUseUnicode(boolean flag) {
        this.useUnicode.setValue(flag);
        this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean getUseUnicode() {
        return this.useUnicodeAsBoolean;
    }

    /**
     * Sets whether or not the driver advises of proper usage.
     *
     * @param useUsageAdvisorFlag whether or not the driver advises of proper
     *        usage.
     */
    public void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {
        this.useUsageAdvisor.setValue(useUsageAdvisorFlag);
        this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
    }

    /**
     * Returns whether or not the driver advises of proper usage.
     *
     * @return the value of useUsageAdvisor
     */
    public boolean getUseUsageAdvisor() {
        return this.useUsageAdvisorAsBoolean;
    }

    /**
     * Returns a description of the connection properties as an XML document.
     *
     * @return the connection properties as an XML document.
     *
     * @throws SQLException if an error occurs.
     */
    public String exposeAsXml() throws SQLException {
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<ConnectionProperties>");

        int numPropertiesToSet = PROPERTY_LIST.size();

        int numCategories = propertyCategories.length;

        Map propertyListByCategory = new HashMap();

        for (int i = 0; i < numCategories; i++) {
            propertyListByCategory.put(propertyCategories[i],
                new Map[] { new TreeMap(), new TreeMap() });
        }

        //
        // The following properties are not exposed as 'normal' properties, but they are
        // settable nonetheless, so we need to have them documented, make sure
        // that they sort 'first' as #1 and #2 in the category
        //
        StringConnectionProperty userProp = new StringConnectionProperty(NonRegisteringDriver.USER_PROPERTY_KEY,
                null, "The user to connect as", "all",
                CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE + 1);
        StringConnectionProperty passwordProp = new StringConnectionProperty(NonRegisteringDriver.PASSWORD_PROPERTY_KEY,
                null, "The password to use when connecting", "all",
                CONNECTION_AND_AUTH_CATEGORY, Integer.MIN_VALUE + 2);

        Map[] connectionSortMaps = (Map[]) propertyListByCategory.get(CONNECTION_AND_AUTH_CATEGORY);
        connectionSortMaps[0].put(new Integer(userProp.getOrder()), userProp);
        connectionSortMaps[0].put(new Integer(passwordProp.getOrder()),
            passwordProp);

        try {
            for (int i = 0; i < numPropertiesToSet; i++) {
                java.lang.reflect.Field propertyField = (java.lang.reflect.Field) PROPERTY_LIST.get(i);
                ConnectionProperty propToGet = (ConnectionProperty) propertyField.get(this);
                Map[] sortMaps = (Map[]) propertyListByCategory.get(propToGet.getCategoryName());
                int orderInCategory = propToGet.getOrder();

                if (orderInCategory == Integer.MIN_VALUE) {
                    sortMaps[1].put(propToGet.getPropertyName(), propToGet);
                } else {
                    sortMaps[0].put(new Integer(orderInCategory), propToGet);
                }
            }

            for (int j = 0; j < numCategories; j++) {
                Map[] sortMaps = (Map[]) propertyListByCategory.get(propertyCategories[j]);
                Iterator orderedIter = sortMaps[0].values().iterator();
                Iterator alphaIter = sortMaps[1].values().iterator();

                xmlBuf.append("\n <PropertyCategory name=\"");
                xmlBuf.append(propertyCategories[j]);
                xmlBuf.append("\">");

                while (orderedIter.hasNext()) {
                    ConnectionProperty propToGet = (ConnectionProperty) orderedIter.next();
                    propToGet.syncDriverPropertyInfo();

                    xmlBuf.append("\n  <Property name=\"");
                    xmlBuf.append(propToGet.getPropertyName());
                    xmlBuf.append("\" required=\"");
                    xmlBuf.append(propToGet.required ? "Yes" : "No");

                    xmlBuf.append("\" default=\"");

                    if (propToGet.getDefaultValue() != null) {
                        xmlBuf.append(propToGet.getDefaultValue());
                    }

                    xmlBuf.append("\" sortOrder=\"");
                    xmlBuf.append(propToGet.getOrder());
                    xmlBuf.append("\" since=\"");
                    xmlBuf.append(propToGet.sinceVersion);
                    xmlBuf.append("\">\n");
                    xmlBuf.append("    ");
                    xmlBuf.append(propToGet.description);
                    xmlBuf.append("\n  </Property>");
                }

                while (alphaIter.hasNext()) {
                    ConnectionProperty propToGet = (ConnectionProperty) alphaIter.next();
                    propToGet.syncDriverPropertyInfo();

                    xmlBuf.append("\n  <Property name=\"");
                    xmlBuf.append(propToGet.getPropertyName());
                    xmlBuf.append("\" required=\"");
                    xmlBuf.append(propToGet.required ? "Yes" : "No");

                    xmlBuf.append("\" default=\"");

                    if (propToGet.getDefaultValue() != null) {
                        xmlBuf.append(propToGet.getDefaultValue());
                    }

                    xmlBuf.append("\" sortOrder=\"alpha\" since=\"");
                    xmlBuf.append(propToGet.sinceVersion);
                    xmlBuf.append("\">\n");
                    xmlBuf.append("    ");
                    xmlBuf.append(propToGet.description);
                    xmlBuf.append("\n  </Property>");
                }

                xmlBuf.append("\n </PropertyCategory>");
            }
        } catch (IllegalAccessException iae) {
            throw new SQLException("Internal properties failure",
                SQLError.SQL_STATE_GENERAL_ERROR);
        }

        xmlBuf.append("\n</ConnectionProperties>");

        return xmlBuf.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    protected String getEncoding() {
        return this.characterEncodingAsString;
    }

    /**
     * Exposes all ConnectionPropertyInfo instances as DriverPropertyInfo
     *
     * @param info the properties to load into these ConnectionPropertyInfo
     *        instances
     * @param slotsToReserve the number of DPI slots to reserve for 'standard'
     *        DPI properties (user, host, password, etc)
     *
     * @return a list of all ConnectionPropertyInfo instances, as
     *         DriverPropertyInfo
     *
     * @throws SQLException if an error occurs
     */
    protected static DriverPropertyInfo[] exposeAsDriverPropertyInfo(
        Properties info, int slotsToReserve) throws SQLException {
        return (new ConnectionProperties() {
            }).exposeAsDriverPropertyInfoInternal(info, slotsToReserve);
    }

    /**
     * DOCUMENT ME!
     *
     * @param property
     */
    protected void setHighAvailability(boolean property) {
        this.autoReconnect.setValue(property);
        this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    protected boolean getHighAvailability() {
        return this.highAvailabilityAsBoolean;
    }

    protected DriverPropertyInfo[] exposeAsDriverPropertyInfoInternal(
        Properties info, int slotsToReserve) throws SQLException {
        initializeProperties(info);

        int numProperties = PROPERTY_LIST.size();

        int listSize = numProperties + slotsToReserve;

        DriverPropertyInfo[] driverProperties = new DriverPropertyInfo[listSize];

        for (int i = slotsToReserve; i < listSize; i++) {
            java.lang.reflect.Field propertyField = (java.lang.reflect.Field) PROPERTY_LIST.get(i -
                    slotsToReserve);

            try {
                ConnectionProperty propToExpose = (ConnectionProperty) propertyField.get(this);

                if (info != null) {
                    propToExpose.initializeFrom(info);
                }

                propToExpose.syncDriverPropertyInfo();
                driverProperties[i] = propToExpose;
            } catch (IllegalAccessException iae) {
                throw new SQLException("Internal properties failure",
                    SQLError.SQL_STATE_GENERAL_ERROR);
            }
        }

        return driverProperties;
    }

    protected Properties exposeAsProperties(Properties info)
        throws SQLException {
        if (info == null) {
            info = new Properties();
        }

        int numPropertiesToSet = PROPERTY_LIST.size();

        for (int i = 0; i < numPropertiesToSet; i++) {
            java.lang.reflect.Field propertyField = (java.lang.reflect.Field) PROPERTY_LIST.get(i);

            try {
                ConnectionProperty propToGet = (ConnectionProperty) propertyField.get(this);

                Object propValue = propToGet.getValueAsObject();

                if (propValue != null) {
                    info.setProperty(propToGet.getPropertyName(),
                        propValue.toString());
                }
            } catch (IllegalAccessException iae) {
                throw new SQLException("Internal properties failure",
                    SQLError.SQL_STATE_GENERAL_ERROR);
            }
        }

        return info;
    }

    /**
     * Initializes driver properties that come from a JNDI reference (in the
     * case of a javax.sql.DataSource bound into some name service that
     * doesn't handle Java objects directly).
     *
     * @param ref The JNDI Reference that holds RefAddrs for all properties
     *
     * @throws SQLException DOCUMENT ME!
     */
    protected void initializeFromRef(Reference ref) throws SQLException {
        int numPropertiesToSet = PROPERTY_LIST.size();

        for (int i = 0; i < numPropertiesToSet; i++) {
            java.lang.reflect.Field propertyField = (java.lang.reflect.Field) PROPERTY_LIST.get(i);

            try {
                ConnectionProperty propToSet = (ConnectionProperty) propertyField.get(this);

                if (ref != null) {
                    propToSet.initializeFrom(ref);
                }
            } catch (IllegalAccessException iae) {
                throw new SQLException("Internal properties failure",
                    SQLError.SQL_STATE_GENERAL_ERROR);
            }
        }

        postInitialization();
    }

    /**
     * Initializes driver properties that come from URL or properties passed to
     * the driver manager.
     *
     * @param info DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    protected void initializeProperties(Properties info)
        throws SQLException {
        if (info != null) {
            // For backwards-compatibility
            String profileSqlLc = info.getProperty("profileSql");

            if (profileSqlLc != null) {
                info.put("profileSQL", profileSqlLc);
            }

            Properties infoCopy = (Properties) info.clone();

            infoCopy.remove(NonRegisteringDriver.HOST_PROPERTY_KEY);
            infoCopy.remove(NonRegisteringDriver.USER_PROPERTY_KEY);
            infoCopy.remove(NonRegisteringDriver.PASSWORD_PROPERTY_KEY);
            infoCopy.remove(NonRegisteringDriver.DBNAME_PROPERTY_KEY);
            infoCopy.remove(NonRegisteringDriver.PORT_PROPERTY_KEY);
            infoCopy.remove("profileSql");

            int numPropertiesToSet = PROPERTY_LIST.size();

            for (int i = 0; i < numPropertiesToSet; i++) {
                java.lang.reflect.Field propertyField = (java.lang.reflect.Field) PROPERTY_LIST.get(i);

                try {
                    ConnectionProperty propToSet = (ConnectionProperty) propertyField.get(this);

                    propToSet.initializeFrom(infoCopy);
                } catch (IllegalAccessException iae) {
                    throw new SQLException(
                        "Unable to initialize driver properties due to " +
                        iae.toString(), SQLError.SQL_STATE_GENERAL_ERROR);
                }
            }

            // TODO -- Not yet
            /*
            int numUnknownProperties = infoCopy.size();

            if (numUnknownProperties > 0) {
                StringBuffer errorMessageBuf = new StringBuffer(
                        "Unknown connection ");
                errorMessageBuf.append((numUnknownProperties == 1)
                    ? "property " : "properties ");

                Iterator propNamesItor = infoCopy.keySet().iterator();

                errorMessageBuf.append("'");
                errorMessageBuf.append(propNamesItor.next().toString());
                errorMessageBuf.append("'");

                while (propNamesItor.hasNext()) {
                    errorMessageBuf.append(", '");
                    errorMessageBuf.append(propNamesItor.next().toString());
                    errorMessageBuf.append("'");
                }

                throw new SQLException(errorMessageBuf.toString(), SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE);
            }
            */
            postInitialization();
        }
    }

    protected void postInitialization() throws SQLException {
    	/*
    	 * 
    	 Configure logger
    	
    	 If the value has been set by the user, then use that,
    	 otherwise, autodetect it

    	 We prefer, Log4j, if it's available,
    	 Then JDK1.4 logging,
    	 Then fallback to our STDERR logging.
    	 
    	*/
    	
    	// Yes, this looks goofy (String == instead of .equals), 
    	// but it's how we tell whether we're using defaults
    	// or not, and it survives JNDI/Properties initialization, etc.
    	
    	if (getLogger() == STANDARD_LOGGER_NAME) {
    		String environmentLoggerName = null;
    		
    		try {
    			environmentLoggerName = 
    				System.getProperty("com.mysql.jdbc.logger");
    		} catch (Throwable noAccessToSystemProperties) {
    			environmentLoggerName = null;
    		}
    			
    		if (environmentLoggerName != null) {
    			setLogger(environmentLoggerName);
    		} else {
		    	try {
		    		// Is Log4J available?
		    		Class.forName("org.apache.log4j.Level");
		    		//setLogger(Log4JLogger.class.getName());
		    	} catch (Throwable t) {	
			    	try {
			    		// Are we running on JDK-1.4?
			    		Class.forName("java.util.logging.Level");
			    		setLogger(Jdk14Logger.class.getName());
			    	} catch (Throwable t2) {
			    		// guess not
			    		setLogger(STANDARD_LOGGER_NAME);
			    	}
		    	}
    		}
    	}
    	
        // Support 'old' profileSql capitalization
        if (this.profileSql.getValueAsObject() != null) {
            this.profileSQL.initializeFrom(this.profileSql.getValueAsObject()
                                                          .toString());
        }

        this.reconnectTxAtEndAsBoolean = ((Boolean) this.reconnectAtTxEnd.getValueAsObject()).booleanValue();

        // Adjust max rows
        if (this.getMaxRows() == 0) {
            // adjust so that it will become MysqlDefs.MAX_ROWS
            // in execSQL()
            this.maxRows.setValueAsObject(new Integer(-1));
        }

        //
        // Check character encoding
        //
        String testEncoding = this.getEncoding();

        if (testEncoding != null) {
            //	Attempt to use the encoding, and bail out if it
            // can't be used
            try {
                String testString = "abc";
                testString.getBytes(testEncoding);
            } catch (UnsupportedEncodingException UE) {
                throw new SQLException("Unsupported character " + "encoding '" +
                    testEncoding + "'.", "0S100");
            }
        }

        // Metadata caching is only supported on JDK-1.4 and newer
        // because it relies on LinkedHashMap being present.
        // Check (and disable) if not supported
        if (((Boolean) this.cacheResultSetMetadata.getValueAsObject()).booleanValue()) {
            try {
                Class.forName("java.util.LinkedHashMap");
            } catch (ClassNotFoundException cnfe) {
                this.cacheResultSetMetadata.setValue(false);
            }
        }

        this.cacheResultSetMetaDataAsBoolean = this.cacheResultSetMetadata.getValueAsBoolean();
        this.useUnicodeAsBoolean = this.useUnicode.getValueAsBoolean();
        this.characterEncodingAsString = ((String) this.characterEncoding.getValueAsObject());
        this.highAvailabilityAsBoolean = this.autoReconnect.getValueAsBoolean();
        this.autoReconnectForPoolsAsBoolean = this.autoReconnectForPools.getValueAsBoolean();
        this.maxRowsAsInt = ((Integer) this.maxRows.getValueAsObject()).intValue();
        this.profileSQLAsBoolean = this.profileSQL.getValueAsBoolean();
        this.useUsageAdvisorAsBoolean = this.useUsageAdvisor.getValueAsBoolean();
        this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
    }

    protected void storeToRef(Reference ref) throws SQLException {
        int numPropertiesToSet = PROPERTY_LIST.size();

        for (int i = 0; i < numPropertiesToSet; i++) {
            java.lang.reflect.Field propertyField = (java.lang.reflect.Field) PROPERTY_LIST.get(i);

            try {
                ConnectionProperty propToStore = (ConnectionProperty) propertyField.get(this);

                if (ref != null) {
                    propToStore.storeTo(ref);
                }
            } catch (IllegalAccessException iae) {
                throw new SQLException("Huh?");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the useUnbufferedInput.
     */
    protected boolean useUnbufferedInput() {
        return this.useUnbufferedInput.getValueAsBoolean();
    }

    /**
     * @return Returns the rollbackOnPooledClose.
     */
    public boolean getRollbackOnPooledClose() {
        return this.rollbackOnPooledClose.getValueAsBoolean();
    }

    /**
     * @param rollbackOnPooledClose The rollbackOnPooledClose to set.
     */
    public void setRollbackOnPooledClose(boolean flag) {
        this.rollbackOnPooledClose.setValue(flag);
    }

    /**
     * @return Returns the useFastIntParsing.
     */
    public boolean getUseFastIntParsing() {
        return this.useFastIntParsing.getValueAsBoolean();
    }

    /**
     * @param useFastIntParsing The useFastIntParsing to set.
     */
    public void setUseFastIntParsing(boolean flag) {
        this.useFastIntParsing.setValue(flag);
    }

    /**
     * @return Returns the useOnlyServerErrorMessages.
     */
    public boolean getUseOnlyServerErrorMessages() {
        return this.useOnlyServerErrorMessages.getValueAsBoolean();
    }

    /**
     * @param useOnlyServerErrorMessages The useOnlyServerErrorMessages to set.
     */
    public void setUseOnlyServerErrorMessages(boolean flag) {
        this.useOnlyServerErrorMessages.setValue(flag);
    }

    abstract class ConnectionProperty extends DriverPropertyInfo {
        Object defaultValue;
        Object valueAsObject;
        String propertyName;
        String sinceVersion;
        String[] allowableValues;
        int lowerBound;
        int upperBound;
        String categoryName;
        int order;

        ConnectionProperty(String propertyNameToSet, Object defaultValueToSet,
            String[] allowableValuesToSet, int lowerBoundToSet,
            int upperBoundToSet, String descriptionToSet,
            String sinceVersionToSet, String category, int orderInCategory) {
            super(propertyNameToSet, null);

            this.description = descriptionToSet;
            this.propertyName = propertyNameToSet;
            this.defaultValue = defaultValueToSet;
            this.valueAsObject = defaultValueToSet;
            this.allowableValues = allowableValuesToSet;
            this.lowerBound = lowerBoundToSet;
            this.upperBound = upperBoundToSet;
            this.required = false;
            this.sinceVersion = sinceVersionToSet;
            this.categoryName = category;
            this.order = orderInCategory;
        }

        String[] getAllowableValues() {
            return this.allowableValues;
        }

        int getLowerBound() {
            return this.lowerBound;
        }

        int getUpperBound() {
            return this.upperBound;
        }

        void initializeFrom(Properties extractFrom) throws SQLException {
            String extractedValue = extractFrom.getProperty(getPropertyName());
            extractFrom.remove(getPropertyName());
            initializeFrom(extractedValue);
        }

        void initializeFrom(Reference ref) throws SQLException {
            RefAddr refAddr = ref.get(getPropertyName());

            if (refAddr != null) {
                String refContentAsString = (String) refAddr.getContent();

                initializeFrom(refContentAsString);
            }
        }

        abstract void initializeFrom(String extractedValue)
            throws SQLException;

        Object getDefaultValue() {
            return this.defaultValue;
        }

        String getPropertyName() {
            return this.propertyName;
        }

        abstract boolean isRangeBased();

        abstract boolean hasValueConstraints();

        void setValueAsObject(Object obj) {
            this.valueAsObject = obj;
        }

        Object getValueAsObject() {
            return this.valueAsObject;
        }

        void storeTo(Reference ref) {
            if (getValueAsObject() != null) {
                ref.add(new StringRefAddr(getPropertyName(),
                        getValueAsObject().toString()));
            }
        }

        /**
         * Synchronizes the state of a ConnectionProperty so that it can be
         * exposed as a DriverPropertyInfo instance.
         */
        void syncDriverPropertyInfo() {
            this.choices = getAllowableValues();
            this.value = (this.valueAsObject != null)
                ? this.valueAsObject.toString() : null;
        }

        void validateStringValues(String valueToValidate)
            throws SQLException {
            String[] validateAgainst = getAllowableValues();

            if (valueToValidate == null) {
                return;
            }

            if ((validateAgainst == null) || (validateAgainst.length == 0)) {
                return;
            }

            for (int i = 0; i < validateAgainst.length; i++) {
                if ((validateAgainst[i] != null) &&
                        validateAgainst[i].equalsIgnoreCase(valueToValidate)) {
                    return;
                }
            }

            StringBuffer errorMessageBuf = new StringBuffer();

            errorMessageBuf.append("The connection property '");
            errorMessageBuf.append(getPropertyName());
            errorMessageBuf.append("' only accepts values of the form: ");

            if (validateAgainst.length != 0) {
                errorMessageBuf.append("'");
                errorMessageBuf.append(validateAgainst[0]);
                errorMessageBuf.append("'");

                for (int i = 1; i < (validateAgainst.length - 1); i++) {
                    errorMessageBuf.append(", ");
                    errorMessageBuf.append("'");
                    errorMessageBuf.append(validateAgainst[i]);
                    errorMessageBuf.append("'");
                }

                errorMessageBuf.append(" or '");
                errorMessageBuf.append(validateAgainst[validateAgainst.length -
                    1]);
                errorMessageBuf.append("'");
            }

            errorMessageBuf.append(". The value '");
            errorMessageBuf.append(valueToValidate);
            errorMessageBuf.append("' is not in this set.");

            throw new SQLException(errorMessageBuf.toString(),
                SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
        }

        /**
         * @return Returns the categoryName.
         */
        String getCategoryName() {
            return this.categoryName;
        }

        /**
         * @param categoryName The categoryName to set.
         */
        void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        /**
         * @return Returns the order.
         */
        int getOrder() {
            return this.order;
        }

        /**
         * @param order The order to set.
         */
        void setOrder(int order) {
            this.order = order;
        }
    }

    class BooleanConnectionProperty extends ConnectionProperty {
        /**
         * DOCUMENT ME!
         *
         * @param propertyNameToSet
         * @param defaultValueToSet
         * @param descriptionToSet DOCUMENT ME!
         * @param sinceVersionToSet DOCUMENT ME!
         */
        BooleanConnectionProperty(String propertyNameToSet,
            boolean defaultValueToSet, String descriptionToSet,
            String sinceVersionToSet, String category, int orderInCategory) {
            super(propertyNameToSet, new Boolean(defaultValueToSet), null, 0,
                0, descriptionToSet, sinceVersionToSet, category,
                orderInCategory);
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#getAllowableValues()
         */
        String[] getAllowableValues() {
            return new String[] { "true", "false", "yes", "no" };
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#isRangeBased()
         */
        boolean isRangeBased() {
            return false;
        }

        void setValue(boolean valueFlag) {
            this.valueAsObject = new Boolean(valueFlag);
        }

        boolean getValueAsBoolean() {
            return ((Boolean) this.valueAsObject).booleanValue();
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#hasValueConstraints()
         */
        boolean hasValueConstraints() {
            return true;
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#initializeFrom(java.util.Properties)
         */
        void initializeFrom(String extractedValue) throws SQLException {
            if (extractedValue != null) {
                validateStringValues(extractedValue);

                this.valueAsObject = new Boolean(extractedValue.equalsIgnoreCase(
                            "TRUE") || extractedValue.equalsIgnoreCase("YES"));
            } else {
                this.valueAsObject = this.defaultValue;
            }
        }
    }

	class MemorySizeConnectionProperty extends IntegerConnectionProperty {

		MemorySizeConnectionProperty(String propertyNameToSet, int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet, String descriptionToSet, String sinceVersionToSet, String category, int orderInCategory) {
			super(propertyNameToSet, defaultValueToSet, lowerBoundToSet, upperBoundToSet,
					descriptionToSet, sinceVersionToSet, category, orderInCategory);
			// TODO Auto-generated constructor stub
		}
		
		void setValue(String value) throws SQLException {
			initializeFrom(value);
		}
		
		void initializeFrom(String extractedValue) throws SQLException {
			if (extractedValue != null) {
				if (extractedValue.endsWith("k") ||
					extractedValue.endsWith("K") ||
					extractedValue.endsWith("kb") ||
					extractedValue.endsWith("Kb") ||
					extractedValue.endsWith("kB")) {
					multiplier = 1024;
					int indexOfK = StringUtils.indexOfIgnoreCase(extractedValue, "k");
					extractedValue = extractedValue.substring(0, indexOfK);
				} else if (extractedValue.endsWith("m") ||
						extractedValue.endsWith("M") ||
						extractedValue.endsWith("G") ||
						extractedValue.endsWith("mb") ||
						extractedValue.endsWith("Mb") ||
						extractedValue.endsWith("mB")) {
						multiplier = 1024 * 1024;
						int indexOfM = StringUtils.indexOfIgnoreCase(extractedValue, "m");
						extractedValue = extractedValue.substring(0, indexOfM);
				} else if (extractedValue.endsWith("g") ||
						extractedValue.endsWith("G") ||
						extractedValue.endsWith("gb") ||
						extractedValue.endsWith("Gb") ||
						extractedValue.endsWith("gB")) {
						multiplier = 1024 * 1024 * 1024;
						int indexOfG = StringUtils.indexOfIgnoreCase(extractedValue, "g");
						extractedValue = extractedValue.substring(0, indexOfG); 
				}
			} 
			
			super.initializeFrom(extractedValue);
		}
	}
	
    class IntegerConnectionProperty extends ConnectionProperty {
		int multiplier = 1;
   
        /**
         * DOCUMENT ME!
         *
         * @param propertyNameToSet
         * @param defaultValueToSet
         * @param descriptionToSet
         * @param sinceVersionToSet DOCUMENT ME!
         */
		
		
        IntegerConnectionProperty(String propertyNameToSet,
            int defaultValueToSet, String descriptionToSet,
            String sinceVersionToSet, String category, int orderInCategory) {
            this(propertyNameToSet, defaultValueToSet, 0, 0, descriptionToSet,
                sinceVersionToSet, category, orderInCategory);
        }

        IntegerConnectionProperty(String propertyNameToSet,
            int defaultValueToSet, int lowerBoundToSet, int upperBoundToSet,
            String descriptionToSet, String sinceVersionToSet, String category,
            int orderInCategory) {
            super(propertyNameToSet, new Integer(defaultValueToSet), null,
                lowerBoundToSet, upperBoundToSet, descriptionToSet,
                sinceVersionToSet, category, orderInCategory);
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#getAllowableValues()
         */
        String[] getAllowableValues() {
            return null;
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#getLowerBound()
         */
        int getLowerBound() {
            return this.lowerBound;
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#isRangeBased()
         */
        boolean isRangeBased() {
            return getUpperBound() != getLowerBound();
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#getUpperBound()
         */
        int getUpperBound() {
            return this.upperBound;
        }

        void setValue(int valueFlag) {
            this.valueAsObject = new Integer(valueFlag);
        }

        int getValueAsInt() {
            return ((Integer) this.valueAsObject).intValue();
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#hasValueConstraints()
         */
        boolean hasValueConstraints() {
            return false;
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#initializeFrom(java.lang.String)
         */
        void initializeFrom(String extractedValue) throws SQLException {
            if (extractedValue != null) {
                try {
                    // Parse decimals, too
                    int intValue = Double.valueOf(extractedValue).intValue();

                    /*
                    if (isRangeBased()) {

                    if ((intValue < getLowerBound())
                    || (intValue > getUpperBound())) {
                    throw new SQLException("The connection property '"
                    + getPropertyName()
                    + "' only accepts integer values in the range of "
                    + getLowerBound() + " - " + getUpperBound()
                    + ", the value '" + extractedValue
                    + "' exceeds this range.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
                    }
                    }*/
                    this.valueAsObject = new Integer(intValue * multiplier);
                } catch (NumberFormatException nfe) {
                    throw new SQLException("The connection property '" +
                        getPropertyName() +
                        "' only accepts integer values. The value '" +
                        extractedValue +
                        "' can not be converted to an integer.",
                        SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
                }
            } else {
                this.valueAsObject = this.defaultValue;
            }
        }
    }

    class StringConnectionProperty extends ConnectionProperty {
        /**
         * DOCUMENT ME!
         *
         * @param propertyNameToSet
         * @param defaultValueToSet
         * @param allowableValuesToSet
         * @param descriptionToSet
         * @param sinceVersionToSet DOCUMENT ME!
         */
        StringConnectionProperty(String propertyNameToSet,
            String defaultValueToSet, String[] allowableValuesToSet,
            String descriptionToSet, String sinceVersionToSet, String category,
            int orderInCategory) {
            super(propertyNameToSet, defaultValueToSet, allowableValuesToSet,
                0, 0, descriptionToSet, sinceVersionToSet, category,
                orderInCategory);
        }

        StringConnectionProperty(String propertyNameToSet,
            String defaultValueToSet, String descriptionToSet,
            String sinceVersionToSet, String category, int orderInCategory) {
            this(propertyNameToSet, defaultValueToSet, null, descriptionToSet,
                sinceVersionToSet, category, orderInCategory);
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#isRangeBased()
         */
        boolean isRangeBased() {
            return false;
        }

        void setValue(String valueFlag) {
            this.valueAsObject = valueFlag;
        }

        String getValueAsString() {
            return (String) this.valueAsObject;
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#hasValueConstraints()
         */
        boolean hasValueConstraints() {
            return (this.allowableValues != null) &&
            (this.allowableValues.length > 0);
        }

        /**
         * @see com.mysql.jdbc.ConnectionProperties.ConnectionProperty#initializeFrom(java.util.Properties)
         */
        void initializeFrom(String extractedValue) throws SQLException {
            if (extractedValue != null) {
                validateStringValues(extractedValue);

                this.valueAsObject = extractedValue;
            } else {
                this.valueAsObject = this.defaultValue;
            }
        }
    }
	/**
	 * @return Returns the propertiesTransform.
	 */
	public String getPropertiesTransform() {
		return this.propertiesTransform.getValueAsString();
	}
	/**
	 * @param propertiesTransform The propertiesTransform to set.
	 */
	public void setPropertiesTransform(String value) {
		this.propertiesTransform.setValue(value);
	}
	/**
	 * @return Returns the allowUrlInLocalInfile.
	 */
	public boolean getAllowUrlInLocalInfile() {
		return this.allowUrlInLocalInfile.getValueAsBoolean();
	}
	/**
	 * @param allowUrlInLocalInfile The allowUrlInLocalInfile to set.
	 */
	public void setAllowUrlInLocalInfile(boolean flag) {
		this.allowUrlInLocalInfile.setValue(flag);
	}
	/**
	 * @return Returns the zeroDateTimeBehavior.
	 */
	public String getZeroDateTimeBehavior() {
		return this.zeroDateTimeBehavior.getValueAsString();
	}
	/**
	 * @param zeroDateTimeBehavior The zeroDateTimeBehavior to set.
	 */
	public void setZeroDateTimeBehavior(String behavior) {
		this.zeroDateTimeBehavior.setValue(behavior);
	}
	/**
	 * @return Returns the tinyInt1isBit.
	 */
	public boolean getTinyInt1isBit() {
		return this.tinyInt1isBit.getValueAsBoolean();
	}
	/**
	 * @param tinyInt1isBit The tinyInt1isBit to set.
	 */
	public void setTinyInt1isBit(boolean flag) {
		this.tinyInt1isBit.setValue(flag);
	}
	/**
	 * @return Returns the cacheServerConfiguration.
	 */
	public boolean getCacheServerConfiguration() {
		return cacheServerConfiguration.getValueAsBoolean();
	}
	/**
	 * @param cacheServerConfiguration The cacheServerConfiguration to set.
	 */
	public void setCacheServerConfiguration(boolean flag) {
		this.cacheServerConfiguration.setValue(flag);
	}
	/**
	 * @return Returns the allowNanAndInf.
	 */
	protected boolean getAllowNanAndInf() {
		return allowNanAndInf.getValueAsBoolean();
	}
	/**
	 * @param allowNanAndInf The allowNanAndInf to set.
	 */
	protected void setAllowNanAndInf(boolean flag) {
		this.allowNanAndInf.setValue(flag);
	}
	/**
	 * @return Returns the dynamicCalendars.
	 */
	public boolean getDynamicCalendars() {
		return this.dynamicCalendars.getValueAsBoolean();
	}
	/**
	 * @param dynamicCalendars The dynamicCalendars to set.
	 */
	public void setDynamicCalendars(boolean flag) {
		this.dynamicCalendars.setValue(flag);
	}
	/**
	 * @return Returns the autoDeserialize.
	 */
	public boolean getAutoDeserialize() {
		return autoDeserialize.getValueAsBoolean();
	}
	/**
	 * @param autoDeserialize The autoDeserialize to set.
	 */
	public void setAutoDeserialize(boolean flag) {
		this.autoDeserialize.setValue(flag);
	}
	/**
	 * @return Returns the useReadAheadInput.
	 */
	public boolean getUseReadAheadInput() {
		return this.useReadAheadInput.getValueAsBoolean();
	}
	/**
	 * @param useReadAheadInput The useReadAheadInput to set.
	 */
	public void setUseReadAheadInput(boolean flag) {
		this.useReadAheadInput.setValue(flag);
	}
	/**
	 * @return Returns the useOldUTF8Behavior.
	 */
	public boolean getUseOldUTF8Behavior() {
		return this.useOldUTF8BehaviorAsBoolean;
	}
	/**
	 * @param useOldUTF8Behavior The useOldUTF8Behavior to set.
	 */
	public void setUseOldUTF8Behavior(boolean flag) {
		this.useOldUTF8Behavior.setValue(flag);
		this.useOldUTF8BehaviorAsBoolean = this.useOldUTF8Behavior.getValueAsBoolean();
	}
	/**
	 * @return Returns the alwaysSendSetIsolation.
	 */
	public boolean getAlwaysSendSetIsolation() {
		return this.alwaysSendSetIsolation.getValueAsBoolean();
	}
	/**
	 * @param alwaysSendSetIsolation The alwaysSendSetIsolation to set.
	 */
	public void setAlwaysSendSetIsolation(boolean flag) {
		this.alwaysSendSetIsolation.setValue(flag);
	}

	/**
	 * @return Returns the holdResultsOpenOverStatementClose.
	 */
	public boolean getHoldResultsOpenOverStatementClose() {
		return holdResultsOpenOverStatementClose.getValueAsBoolean();
	}

	/**
	 * @param holdResultsOpenOverStatementClose The holdResultsOpenOverStatementClose to set.
	 */
	public void setHoldResultsOpenOverStatementClose(boolean flag) {
		this.holdResultsOpenOverStatementClose.setValue(flag);
	}
	/**
	 * @return Returns the emulateUnsupportedPstmts.
	 */
	public boolean getEmulateUnsupportedPstmts() {
		return this.emulateUnsupportedPstmts.getValueAsBoolean();
	}
	/**
	 * @param emulateUnsupportedPstmts The emulateUnsupportedPstmts to set.
	 */
	public void setEmulateUnsupportedPstmts(boolean flag) {
		this.emulateUnsupportedPstmts.setValue(flag);
	}
	
	/**
	 * @return Returns the dontTrackOpenResources.
	 */
	public boolean getDontTrackOpenResources() {
		return this.dontTrackOpenResources.getValueAsBoolean();
	}
	/**
	 * @param dontTrackOpenResources The dontTrackOpenResources to set.
	 */
	public void setDontTrackOpenResources(boolean flag) {
		this.dontTrackOpenResources.setValue(flag);
	}
	/**
	 * @return Returns the noDatetimeStringSync.
	 */
	public boolean getNoDatetimeStringSync() {
		return this.noDatetimeStringSync.getValueAsBoolean();
	}
	/**
	 * @param noDatetimeStringSync The noDatetimeStringSync to set.
	 */
	public void setNoDatetimeStringSync(boolean flag) {
		this.noDatetimeStringSync.setValue(flag);
	}
	/**
	 * @return Returns the useLocalSessionState.
	 */
	public boolean getUseLocalSessionState() {
		return this.useLocalSessionState.getValueAsBoolean();
	}
	/**
	 * @param useLocalSessionState The useLocalSessionState to set.
	 */
	public void setUseLocalSessionState(boolean flag) {
		this.useLocalSessionState.setValue(flag);
	}
	/**
	 * @return Returns the runningCTS13.
	 */
	public boolean getRunningCTS13() {
		return this.runningCTS13.getValueAsBoolean();
	}
	/**
	 * @param runningCTS13 The runningCTS13 to set.
	 */
	public void setRunningCTS13(boolean flag) {
		this.runningCTS13.setValue(flag);
	}
	/**
	 * @return Returns the sessionVariables.
	 */
	public String getSessionVariables() {
		return sessionVariables.getValueAsString();
	}
	/**
	 * @param sessionVariables The sessionVariables to set.
	 */
	public void setSessionVariables(String variables) {
		this.sessionVariables.setValue(variables);
	}

	/**
	 * @return Returns the blobSendChunkSize.
	 */
	public int getBlobSendChunkSize() {
		return blobSendChunkSize.getValueAsInt();
	}

	/**
	 * @param blobSendChunkSize The blobSendChunkSize to set.
	 */
	public void setBlobSendChunkSize(String value) throws SQLException {
		this.blobSendChunkSize.setValue(value);
	}
	
	public boolean getEmptyStringsConvertToZero() {
		return this.emptyStringsConvertToZero.getValueAsBoolean();
	}
	
	public void setEmptyStringsConvertToZero(boolean flag) {
		this.emptyStringsConvertToZero.setValue(flag);
	}

	public boolean getNullCatalogMeansCurrent() {
		return this.nullCatalogMeansCurrent.getValueAsBoolean();
	}

	public void setNullCatalogMeansCurrent(boolean value) {
		this.nullCatalogMeansCurrent.setValue(value);
	}

	public boolean getNullNamePatternMatchesAll() {
		return this.nullNamePatternMatchesAll.getValueAsBoolean();
	}

	public void setNullNamePatternMatchesAll(boolean value) {
		this.nullNamePatternMatchesAll.setValue(value);
	}
}
