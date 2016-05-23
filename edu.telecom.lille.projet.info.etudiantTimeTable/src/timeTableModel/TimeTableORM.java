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
	 * The constructor
	 */
	public TimeTableORM() {
		
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a timetable by giving the id
	 * @param timeTableId
	 * @return conditions
	 */
	private String conditionId(int timeTableId) {
		return super.condition(this.idColumn, timeTableId);
	}

	/**
	 * Return the list of the columns to create a new timetable in the database
	 * @return columns
	 */
	private String insertColumns() {
		return this.idColumn;
	}
	
	/**
	 * Return the list of the values to create a new timetable in the database
	 * @param timeTableId
	 * @return values
	 */
	private String insertValues(int timeTableId) {
		return String.valueOf(timeTableId);
	}

	/**
	 * Get all the rooms
	 * @return
	 */
	public ResultSet all() {
		return super.get(this.table, "*", "1");
	}
	
	/**
	 * Get all the timetables with the right id (there should never be more than one)
	 * @param timeTableId
	 * @return timetables
	 */
	public ResultSet get(int timeTableId) {
		String conditions = this.conditionId(timeTableId);
		return super.get(this.table, "*", conditions);
	}

	/**
	 * Create a new TimeTable in the database
	 * @param timeTableId
	 * @return success
	 */
	public Boolean create(int timeTableId) {
		return super.create(this.table, this.insertColumns(), this.insertValues(timeTableId));
	}

	/**
	 * Remove the TimeTable with the right id
	 * @param timeTableId
	 * @return success
	 */
	public Boolean delete(int timeTableId) {
		String conditions = this.conditionId(timeTableId);
		return super.delete(this.table, conditions);
	}
	
	/**
	 * Check if there is a timetable with the right id
	 * @param timeTableId
	 * @param default_value
	 * @return exist
	 */
	public Boolean exist(int timeTableId, Boolean default_value) {
		String conditions = this.conditionId(timeTableId);
		return super.exist(this.table, conditions, default_value);
	}
	
	/**
	 * Get the length of the TimeTable table
	 * @return length
	 */
	public int length() {
		return super.length(this.table);
	}
}
