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
	 * Format of the SELECT request
	 */
	private String SELECT = "SELECT %s FROM %s WHERE %s";
	
	/**
	 * Format of the INSERT request
	 */
	private String INSERT = "INSERT INTO %s (%s) VALUES(%s)";
	
	/**
	 * Format of the DELETE request
	 */
	private String DELETE = "DELETE FROM %s WHERE %s";

	/**
	 * Check if there is a line in the right table matching all the conditions
	 * @param table
	 * @param conditions
	 * @param default_value
	 * @return exist
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
	 * @param table
	 * @return length
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
	 * @param column
	 * @param value
	 * @return condition
	 */
	protected String condition(String column, String value) {
		return " " + column + " = \"" + value + "\" ";
	}

	/**
	 * Return the conditional part of a SELECT SQL request
	 * @param column
	 * @param value
	 * @return condition
	 */
	protected String condition(String column, int value) {
		return this.condition(column, String.valueOf(value));
	}
	
	/**
	 * Return the conditional part of a SELECT SQL request
	 * @param column
	 * @param value
	 * @return condition
	 */
	protected String condition(String column, Date value) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return this.condition(column, dateformat.format(value));
	}
	
	/**
	 * Get all the lines in the right table that match all the conditions
	 * @param table
	 * @param columns
	 * @param conditions
	 * @return results
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
	 * @param table
	 * @param columns
	 * @param values
	 * @return success
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
