package de.mb.database.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import de.mb.database.SQLAnswerTable;

/**
 * This Class ist for easy use of SQL Statements
 * 
 * @author Marco Behnke
 */
public class MySQLExecution {
	private Connection _conn;

	/**
	 * Public constructor
	 * 
	 * @param conn	database connection
	 */
	public MySQLExecution(Connection conn){
		this._conn = conn;
	}
	
	/**
	 * Executes SQL Statement and returns result as AnswerTable
	 * 
	 * @param query			SQL Statement
	 * @return				Answertable with results
	 * @throws SQLException
	 */
	public SQLAnswerTable executeQuery(String query) throws SQLException {
		Statement stmt = _conn.createStatement();
		SQLAnswerTable t = new SQLAnswerTable(stmt.executeQuery(query));
		return t;
	}

	/**
	 * private
	 */
	private String buildlist(ArrayList list, String prefix, String seperator){
		String result = prefix+" ";
		Iterator i = list.iterator();
		result += (String)i.next();
		while(i.hasNext()) {
			result += seperator+(String)i.next();
		}
		return result+" ";
	}
	
	/**
	 * private
	 */
	private String makeSelect(ArrayList fields){
		if(fields.isEmpty()) { // if no field are given, get all fields
			return "SELECT * ";
		}
		return buildlist(fields, "SELECT", ",");
	}

	/**
	 * private
	 */
	private String makeWhere(ArrayList conditions, boolean and){
		String result = "WHERE ";
		String op = " AND ";
		if(!and) op = "OR";
		if(conditions.isEmpty()) { // if no conditions are given, there is no where nowhere
			return "";
		}
		Iterator i = conditions.iterator();
		result += (String)i.next();
		while(i.hasNext()) {
			result += op+(String)i.next();
		}
		return result;
	}
	
	/**
	 * private
	 */
	private String makeFrom(ArrayList tables) throws IllegalArgumentException {
		if(tables.isEmpty()) { // if no field are given, get all fields
			throw new IllegalArgumentException("no table is given for FROM section of SQL statement");
		}
		return buildlist(tables, "FROM", ",");
	}
	
	/**
	 * Builds SQL Statement from given fields
	 * 
	 * @param fields			list of field to retrieve, if list empty, all fields are retrieved
	 * @param from				list if tables to select from
	 * @param where				list of where statments, f.e. name='value'
	 * @param and				if true, all where are AND, if not, they are OR
	 * @param orderby			list of orderby statements, f.e. island.id DESC
	 * @return					SQL Answertable
	 * @throws SQLException
	 */
	public SQLAnswerTable executeQuery(ArrayList fields, ArrayList from, ArrayList where, boolean and,  ArrayList orderby) throws SQLException{
		// TODO better handling of where conditions
		String sselect = makeSelect(fields);
		String swhere  = makeWhere(where, and);
		String sfrom   = makeFrom(from);
		String query   = sselect+" "+sfrom+" "+swhere;
		System.out.println(query);
		return executeQuery(query);
	}

	/**
	 * Builds SQL Statement from given fields
	 * 
	 * @param fields			list of field to retrieve, if list empty, all fields are retrieved
	 * @param from				list if tables to select from
	 * @return					SQL Answertable
	 * @throws SQLException
	 */
	public SQLAnswerTable executeQuery(ArrayList fields, ArrayList from) throws SQLException{
		return executeQuery(fields, from, new ArrayList(), true, new ArrayList());
	}

	/**
	 * Builds SQL Statement from given fields
	 * 
	 * @param fields			list of field to retrieve, if list empty, all fields are retrieved
	 * @param from				list if tables to select from
	 * @param where				list of where statments, f.e. name='value'
	 * @param and				if true, all where are AND, if not, they are OR
	 * @return					SQL Answertable
	 * @throws SQLException
	 */
	public SQLAnswerTable executeQuery(ArrayList fields, ArrayList from, ArrayList where, boolean and) throws SQLException{
		return executeQuery(fields, from, where, and, new ArrayList());
	}
	
	/**
	 * Closes database connection
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		_conn.close();
	}
	
}
