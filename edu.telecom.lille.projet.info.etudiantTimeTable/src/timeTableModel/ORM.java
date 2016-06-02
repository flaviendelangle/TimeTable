package timeTableModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ORM {

	/**
	 * Database connection object
	 */
	public static Connection connection;
	
	/**
	 * Statement of the database connection
	 */
	public static Statement stmt;
	
	/**
	 * Format of the SQL dates
	 */
	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Format of the SELECT request
	 */
	private final String SELECT = "SELECT %s FROM %s WHERE %s";
	
	/**
	 * Format of the INSERT request
	 */
	private final String INSERT = "INSERT INTO %s (%s) VALUES(%s)";
	
	/**
	 * Format of the DELETE request
	 */
	private final String DELETE = "DELETE FROM %s WHERE %s";

	/**
	 * Check if there is a line in the right table matching all the conditions
	 * @param table (table in which we want to to check the existence of something)
	 * @param conditions (conditions needed to execute this query)
	 * @param default_value (value to return is there is an issue)
	 * @return exist (does this line exist ?)
	 */
	protected Boolean exist(String table, String conditions, Boolean default_value) {
		Boolean exist;
		ResultSet result =  this.get(table, "COUNT(*) AS number", conditions);
		try {
			exist = (result.getInt("number") > 0);
		}
		catch (SQLException e) {
			e.printStackTrace();
			exist = default_value;
		}
		return exist;		
	}
	
	/**
	 * Get the number of lines in the given table
	 * @param table (table of which we want to have the length)
	 * @return length (number of line in the right table)
	 */
	protected int length(String table) {
		int length;
		ResultSet result =  this.get(table, "COUNT(*) AS number", "1");
		try {
			length = result.getInt("number");
		}
		catch (SQLException e) {
			e.printStackTrace();
			length = 0;
		}
		return length;
	}
	
	/**
	 * Return the conditional part of a SELECT SQL request 
	 * @param column (column in which the value must be)
	 * @param value (value that must be in the right column)
	 * @return condition (main function to create a subrequest to find with a String)
	 */
	protected String condition(String column, String value) {
		return " " + column + " = \"" + value + "\" ";
	}

	/**
	 * Return the conditional part of a SELECT SQL request
	 * @param column (column in which the value must be)
	 * @param value (value that must be in the right column)
	 * @return condition (main function to create a subrequest to find with an int)
	 */
	protected String condition(String column, int value) {
		return this.condition(column, String.valueOf(value));
	}
	
	/**
	 * Return the conditional part of a SELECT SQL request
	 * @param column (column in which the value must be)
	 * @param value (value that must be in the right column)
	 * @return condition (main function to create a subrequest to find with a date)
	 */
	protected String condition(String column, Date value) {
		return this.condition(column, ORM.dateformat.format(value));
	}
	
	/**
	 * Get all the lines in the right table that match all the conditions
	 * @param table (table in which we want to retrieve something)
	 * @param columns (columns needed to execute this query)
	 * @param conditions (conditions needed to execute this query)
	 * @return results (lines matching the conditions)
	 */
	protected ResultSet get(String table, String columns, String conditions) {
		String request = String.format(this.SELECT, columns, table, conditions);
		ResultSet results = null;
		System.out.println(request);
		try {
			results = ORM.stmt.executeQuery(request);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error in the request : " + request);
		}
		return results;		
	}
	
	/**
	 * Create a new line in the right table
	 * @param table (table in which we want to create something)
	 * @param columns (columns used to execute this query)
	 * @param values (values used to execute this query)
	 * @return success (has this line been successfully created ?)
	 */
	protected Boolean create(String table, String columns, String values) {
		String request = String.format(this.INSERT, table, columns, values);
		System.out.println(request);
		Boolean success;
		try {
			ORM.stmt.executeUpdate(request);
			success = true;
		}
		catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	 * Delete a line in the right table
	 * @param table (table in which we want to delete something)
	 * @param conditions (conditions needed to execute this query)
	 * @return success (has the line been successfully deleted ?)
	 */
	protected Boolean delete(String table, String conditions) {
		String request = String.format(this.DELETE, table, conditions);
		System.out.println(request);
		Boolean success;
		try {
			ORM.stmt.executeUpdate(request);
			success = true;
		}
		catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		return success;		
	}
}
