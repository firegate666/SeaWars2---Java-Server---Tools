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
package com.mysql.jdbc.log;

import com.mysql.jdbc.profiler.ProfilerEvent;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Logging functionality for JDK1.4
 * 
 * @author Mark Matthews
 *
 * @version $Id: Jdk14Logger.java,v 1.2 2008-04-01 22:49:35 firegate666 Exp $
 */
public class Jdk14Logger implements Log {
    private static final Level DEBUG = Level.FINE;
    private static final Level ERROR = Level.SEVERE;
    private static final Level FATAL = Level.SEVERE;
    private static final Level INFO = Level.INFO;
    private static final Level TRACE = Level.FINEST;
    private static final Level WARN = Level.WARNING;

    /**
     * The underlying logger from JDK-1.4
     */
    protected Logger jdkLogger = null;

    /**
     * Creates a new Jdk14Logger object.
     *
     * @param name DOCUMENT ME!
     */
    public Jdk14Logger(String name) {
    	this.jdkLogger = Logger.getLogger(name);
    }

    /**
     * @see com.mysql.jdbc.log.Log#isDebugEnabled()
     */
    public boolean isDebugEnabled() {
        return this.jdkLogger.isLoggable(Level.FINE);
    }

    /**
     * @see com.mysql.jdbc.log.Log#isErrorEnabled()
     */
    public boolean isErrorEnabled() {
        return this.jdkLogger.isLoggable(Level.SEVERE);
    }

    /**
     * @see com.mysql.jdbc.log.Log#isFatalEnabled()
     */
    public boolean isFatalEnabled() {
        return this.jdkLogger.isLoggable(Level.SEVERE);
    }

    /**
     * @see com.mysql.jdbc.log.Log#isInfoEnabled()
     */
    public boolean isInfoEnabled() {
        return this.jdkLogger.isLoggable(Level.INFO);
    }

    /**
     * @see com.mysql.jdbc.log.Log#isTraceEnabled()
     */
    public boolean isTraceEnabled() {
        return this.jdkLogger.isLoggable(Level.FINEST);
    }

    /**
     * @see com.mysql.jdbc.log.Log#isWarnEnabled()
     */
    public boolean isWarnEnabled() {
        return this.jdkLogger.isLoggable(Level.WARNING);
    }

	/**
	 * Logs the given message instance using the 
	 * 'debug' level
	 * 
	 * @param message the message to log
	 */
    public void logDebug(Object message) {
        logInternal(DEBUG, message, null);
    }

	/**
	 * Logs the given message and Throwable at
	 * the 'debug' level.
	 * 
	 * @param message the message to log
	 * @param exception the throwable to log (may be null)
	 */
    public void logDebug(Object message, Throwable exception) {
        logInternal(DEBUG, message, exception);
    }

	/**
	 * Logs the given message instance using the 
	 * 'error' level
	 * 
	 * @param message the message to log
	 */
    public void logError(Object message) {
        logInternal(ERROR, message, null);
    }

	/**
	 * Logs the given message and Throwable at
	 * the 'error' level.
	 * 
	 * @param message the message to log
	 * @param exception the throwable to log (may be null)
	 */
    public void logError(Object message, Throwable exception) {
        logInternal(ERROR, message, exception);
    }

	/**
	 * Logs the given message instance using the 
	 * 'fatal' level
	 * 
	 * @param message the message to log
	 */
    public void logFatal(Object message) {
        logInternal(FATAL, message, null);
    }

	/**
	 * Logs the given message and Throwable at
	 * the 'fatal' level.
	 * 
	 * @param message the message to log
	 * @param exception the throwable to log (may be null)
	 */
    public void logFatal(Object message, Throwable exception) {
        logInternal(FATAL, message, exception);
    }

	/**
	 * Logs the given message instance using the 
	 * 'info' level
	 * 
	 * @param message the message to log
	 */
    public void logInfo(Object message) {
        logInternal(INFO, message, null);
    }

	/**
	 * Logs the given message and Throwable at
	 * the 'info' level.
	 * 
	 * @param message the message to log
	 * @param exception the throwable to log (may be null)
	 */
    public void logInfo(Object message, Throwable exception) {
        logInternal(INFO, message, exception);
    }

	/**
	 * Logs the given message instance using the 
	 * 'trace' level
	 * 
	 * @param message the message to log
	 */
    public void logTrace(Object message) {
        logInternal(TRACE, message, null);
    }

	/**
	 * Logs the given message and Throwable at
	 * the 'trace' level.
	 * 
	 * @param message the message to log
	 * @param exception the throwable to log (may be null)
	 */
    public void logTrace(Object message, Throwable exception) {
        logInternal(TRACE, message, exception);
    }

	/**
	 * Logs the given message instance using the 
	 * 'warn' level
	 * 
	 * @param message the message to log
	 */
    public void logWarn(Object message) {
        logInternal(WARN, message, null);
    }

	/**
	 * Logs the given message and Throwable at
	 * the 'warn' level.
	 * 
	 * @param message the message to log
	 * @param exception the throwable to log (may be null)
	 */
    public void logWarn(Object message, Throwable exception) {
        logInternal(WARN, message, exception);
    }

    private static final int findCallerStackDepth(
        StackTraceElement[] stackTrace) {
        int numFrames = stackTrace.length;

        for (int i = 0; i < numFrames; i++) {
            String callerClassName = stackTrace[i].getClassName();

            if (!callerClassName.startsWith("com.mysql.jdbc")
                    || callerClassName.startsWith("com.mysql.jdbc.compliance")) {
                return i;
            }
        }

        return 0;
    }

    private void logInternal(Level level, Object msg, Throwable exception) {
        //
        // only go through this exercise if the message will actually
        // be logged.
        //
        
        if (this.jdkLogger.isLoggable(level)) {
            String messageAsString = null;
            String callerMethodName = "N/A";
            String callerClassName = "N/A";
            int lineNumber = 0;
            String fileName = "N/A";

            if (msg instanceof ProfilerEvent) {
                ProfilerEvent evt = (ProfilerEvent) msg;

                Throwable locationException = evt.getEventCreationPoint();

                if (locationException == null) {
                    locationException = new Throwable();
                }

                StackTraceElement[] locations = locationException.getStackTrace();

                int frameIdx = findCallerStackDepth(locations);

                if (frameIdx != 0) {
                    callerClassName = locations[frameIdx].getClassName();
                    callerMethodName = locations[frameIdx].getMethodName();
                    lineNumber = locations[frameIdx].getLineNumber();
                    fileName = locations[frameIdx].getFileName();
                }

                StringBuffer msgBuf = new StringBuffer("Profiler Event: [");

                switch (evt.getEventType()) {
                case ProfilerEvent.TYPE_EXECUTE:
                    msgBuf.append("EXECUTE");

                    break;

                case ProfilerEvent.TYPE_FETCH:
                    msgBuf.append("FETCH");

                    break;

                case ProfilerEvent.TYPE_OBJECT_CREATION:
                    msgBuf.append("CONSTRUCT");

                    break;

                case ProfilerEvent.TYPE_PREPARE:
                    msgBuf.append("PREPARE");

                    break;

                case ProfilerEvent.TYPE_QUERY:
                    msgBuf.append("QUERY");

                    break;

                case ProfilerEvent.TYPE_WARN:
                    msgBuf.append("WARN");

                    break;

                default:
                    msgBuf.append("UNKNOWN");
                }

                msgBuf.append("] at ");
                msgBuf.append(callerClassName);
                msgBuf.append(".");
                msgBuf.append(callerMethodName);
                msgBuf.append("(");
                msgBuf.append(fileName);
                msgBuf.append(":");
                msgBuf.append(lineNumber);
                msgBuf.append(") duration: ");
                msgBuf.append(evt.getEventDurationMillis());
                msgBuf.append(" ms, connection-id: ");
                msgBuf.append(evt.getConnectionId());
                msgBuf.append(", statement-id: ");
                msgBuf.append(evt.getStatementId());
                msgBuf.append(", resultset-id: ");
                msgBuf.append(evt.getResultSetId());

                String evtMessage = evt.getMessage();

                if (evtMessage != null) {
                    msgBuf.append(", message: ");
                    msgBuf.append(evtMessage);
                }

                messageAsString = msgBuf.toString();
            } else {
                Throwable locationException = new Throwable();
                StackTraceElement[] locations = locationException.getStackTrace();

                int frameIdx = findCallerStackDepth(locations);

                if (frameIdx != 0) {
                    callerClassName = locations[frameIdx].getClassName();
                    callerMethodName = locations[frameIdx].getMethodName();
                    lineNumber = locations[frameIdx].getLineNumber();
                    fileName = locations[frameIdx].getFileName();
                }

                messageAsString = String.valueOf(msg);
            }

            if (exception == null) {
            	this.jdkLogger.logp(level, callerClassName, callerMethodName,
                    messageAsString);
            } else {
            	this.jdkLogger.logp(level, callerClassName, callerMethodName,
                    messageAsString, exception);
            }
        }
    }
}
