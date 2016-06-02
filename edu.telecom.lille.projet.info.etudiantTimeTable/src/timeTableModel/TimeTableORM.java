package timeTableModel;
import java.sql.ResultSet;

public class TimeTableORM extends ORM {
	
	/**
	 * Name of the SQL table where the timetables are stored
	 */
	protected String table = "TimeTable";
	
	/**
	 * Name of the SQL column where the ids are stored
	 */
	protected String idColumn = "GroupId";

	/**
	 * Name of the SQL column where the logins of the teachers are stored
	 */
	protected String loginColumn = "Login";
	
	/**
	 * The constructor
	 */
	public TimeTableORM() {
		
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a timetable by giving the id
	 * @param timeTableId (id of the timetable
	 * @return conditions (stringified version of this condition)
	 */
	private String conditionId(int timeTableId) {
		return super.condition(this.idColumn, timeTableId);
	}

	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a timetable by giving the login of the teacher
	 * @param login (login of the teacher of the timetable)
	 * @return conditions (stringified version of this condition)
	 */
	private String conditionLogin(String login) {
		return super.condition(this.idColumn, login);
	}

	/**
	 * Return the list of the columns to create a new timetable in the database
	 * @return columns (columns used to create this timetable)
	 */
	private String insertColumns(boolean isTeacher) {
		return this.idColumn + (isTeacher ? ("," + this.loginColumn) : "");
	}
	
	/**
	 * Return the list of the values to create a new timetable for students in the database
	 * @param timeTableId (id of the new timetable)
	 * @return values (values used to create this timetable)
	 */
	private String insertValues(int timeTableId) {
		return String.valueOf(timeTableId);
	}

	/**
	 * Return the list of the values to create a new timetable for teacher in the database
	 * @param timeTableId (id of the new timetable)
	 * @param login (login of the teacher of the new timetable)
	 * @return values (values used to create this timetable)
	 */
	private String insertValues(int timeTableId, String login) {
		return timeTableId + ",\"" + login + "\"";
	}

	/**
	 * Get all the rooms
	 * @return timetables (all the timetables stored in the database)
	 */
	public ResultSet all() {
		return super.get(this.table, "*", "1");
	}
	
	/**
	 * Get all the timetables with the right id (there should never be more than one)
	 * @param timeTableId (id of the timetable we want to retrieve)
	 * @return timetables (all the timetables with the right id)
	 */
	public ResultSet get(int timeTableId) {
		String conditions = this.conditionId(timeTableId);
		return super.get(this.table, "*", conditions);
	}

	/**
	 * Create a new TimeTable in the database
	 * @param timeTableId (id of the new timetable)
	 * @return success (has the timetable been successfully created)
	 */
	public Boolean create(int timeTableId) {
		return super.create(this.table, this.insertColumns(false), this.insertValues(timeTableId));
	}

	/**
	 * Create a new TimeTable in the database
	 * @param timeTableId (id of the new timetable)
	 * @param login (login of the teacher of the new timetable)
	 * @return success (has the timetable been successfully created)
	 */
	public Boolean create(int timeTableId, String login) {
		return super.create(this.table, this.insertColumns(true), this.insertValues(timeTableId, login));
	}

	/**
	 * Remove the TimeTable with the right id
	 * @param timeTableId (id of the timetable we want to delete)
	 * @return success (has the timetable been successfully deleted)
	 */
	public Boolean delete(int timeTableId) {
		String conditions = this.conditionId(timeTableId);
		return super.delete(this.table, conditions);
	}
	
	/**
	 * Check if there is a timetable for students with the right id
	 * @param timeTableId (id that the timetable of which we want to check the existence must have)
	 * @param default_value (value to return is there is an issue)
	 * @return exist (does this timetable exist ?)
	 */
	public Boolean exist(int timeTableId, Boolean default_value) {
		String conditions = this.conditionId(timeTableId);
		return super.exist(this.table, conditions, default_value);
	}
	
	/**
	 * Check if there is a timetable for teachers with the right login
	 * @parem login (login that the timetable of which we want to check the existence must have)
	 * @param default_value (value to return is there is an issue)
	 * @return exist (does this timetable exist ?)
	 */
	public Boolean exist(String login, Boolean default_value) {
		String conditions = this.conditionLogin(login);
		return super.exist(this.table, conditions, default_value);
	}
	
	/**
	 * Get the length of the TimeTable table
	 * @return length (number of timetables stored in the database)
	 */
	public int length() {
		return super.length(this.table);
	}
}
