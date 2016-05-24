package timeTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookORM extends ORM {
	
	/**
	 * Name of the SQL table where the timetables are stored
	 */
	protected String table = "Book";

	/**
	 * Name of the SQL column where the ids of the bookings are stored
	 */
	protected String idColumn = "BookingId";
	
	/**
	 * Name of the SQL column where the logins are stored
	 */
	protected String loginColumn = "Login";

	/**
	 * Name of the SQL column where the beginning dates are stored
	 */
	protected String dateBeginColumn = "DateBegin";
	
	/**
	 * Name of the SQL column where the ending dates are stored
	 */
	protected String dateEndColumn = "DateEnd";
	
	/**
	 * Name of the SQL column where the ids of the rooms are stored
	 */
	protected String roomColumn = "RoomId";
	
	/**
	 * Name of the SQL column where the ids of the timetables are stored
	 */
	protected String timeTableColumn = "TimeTableId";
	
	/**
	 * The constructor
	 */
	public BookORM() {
		
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the id
	 * @param bookId
	 * @return conditions
	 */
	private String conditionId(int bookId) {
		return super.condition(this.idColumn, bookId);
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the timetable
	 * @param timeTableId
	 * @return conditions
	 */
	private String conditionTimeTable(int timeTableId) {
		return super.condition(this.timeTableColumn, timeTableId);
	}

	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the timetable
	 * @param timeTableId
	 * @return conditions
	 */
	private String conditionLogin(String login) {
		return super.condition(this.loginColumn, login);
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the timetable
	 * @param timeTableId
	 * @return conditions
	 */
	private String conditionDateBegin(Date dateBegin) {
		return super.condition(this.dateBeginColumn, dateBegin);
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the timetable
	 * @param timeTableId
	 * @return conditions
	 */
	private String conditionDateEnd(Date dateEnd) {
		return super.condition(this.dateEndColumn, dateEnd);
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the timetable
	 * @param timeTableId
	 * @return conditions
	 */
	private String conditionRoom(int roomId) {
		return super.condition(this.roomColumn, roomId);
	}
	
	/**
	 * Return the list of the columns to create a new book in the database
	 * @return columns
	 */
	private String insertColumns() {
		return this.idColumn + "," + this.loginColumn + "," + this.dateBeginColumn + "," + 
			   this.dateEndColumn + "," + this.roomColumn + "," + this.timeTableColumn;
	}
	
	/**
	 * Return the list of the values to create a new book in the database
	 * @param timeTableId
	 * @return values
	 */
	private String insertValues(int timeTableId, int bookingId, String login, Date dateBeginStr, Date dateEndStr, int roomId) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateBegin = dateformat.format(dateBeginStr);
		String dateEnd = dateformat.format(dateEndStr);
		String values = bookingId + ",\"" + login + "\",\"" + dateBegin + "\",\"" + dateEnd
						+ "\"," + roomId + "," + timeTableId;
		return values;
	}
	
	/**
	 * Get all the books in the right timetable
	 * @return books
	 */
	public ResultSet get(int timeTableId) {
		String conditions = this.conditionTimeTable(timeTableId);
		return super.get(this.table, "*", conditions);
	}
	
	/**
	 * Get all the books in the right timetable and with the right id (there should never be more than one)
	 * @param bookId
	 * @return books
	 */
	public ResultSet get(int timeTableId, int bookId) {
		String conditions = this.conditionId(bookId) + " AND " + this.conditionTimeTable(timeTableId);
		return super.get(this.table, "*", conditions);
	}
	
	/**
	 * Create a new Book in the database
	 * @param roomId
	 * @param capacity
	 * @return success
	 */
	public Boolean create(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd, int roomId) {
		return super.create(this.table, this.insertColumns(), this.insertValues(timeTableId, bookingId, login, dateBegin, dateEnd, roomId));
	}

	/**
	 * Remove the Book in the right timetable and with the right id
	 * @param timeTableId
	 * @param bookId
	 * @return success
	 */
	public Boolean delete(int timeTableId, int bookId) {
		String conditions = this.conditionId(bookId) + " AND " + this.conditionTimeTable(timeTableId);
		return super.delete(this.table, conditions);
	}	
		
	/**
	 * Check if there is a book with the right id
	 * @param bookId
	 * @param default_value
	 * @return exist
	 */
	public Boolean exist(int bookId, Boolean default_value) {
		String conditions = this.conditionId(bookId);
		return super.exist(this.table, conditions, default_value);
	}

	
	/**
	 * Check if there is a book with the right id and the right timetable
	 * @param timeTableId
	 * @param bookId
	 * @param default_value
	 * @return exist
	 */
	public Boolean exist(int timeTableId, int bookId, Boolean default_value) {
		String conditions = this.conditionId(bookId) + " AND " + this.conditionTimeTable(timeTableId);
		return super.exist(this.table, conditions, default_value);
	}

	/**
	 * Check if there is a book which match all the conditions
	 * @param timeTableId
	 * @param bookId
	 * @param login
	 * @param dateBegin
	 * @param dateEnd
	 * @param roomId
	 * @param default_value
	 * @return exist
	 */
	public Boolean exist(int timeTableId, int bookId, String login, Date dateBegin, Date dateEnd, int roomId, Boolean default_value) {
		String conditions = this.conditionTimeTable(timeTableId) + 
							" AND " + this.conditionId(bookId) +
							" AND " + this.conditionLogin(login) +
							" AND " + this.conditionDateBegin(dateBegin) +
							" AND " + this.conditionDateEnd(dateEnd) +
							" AND " + this.conditionRoom(roomId);
		return super.exist(this.table, conditions, default_value);
	}
	
	/**
	 * Get the length of the Book table
	 * @return number
	 */
	public int length() {
		return super.length(this.table);
	}
	
	/**
	 * Get the number of books in a timetable
	 * @return length
	 */
	public int length(int timeTableId) {
		String conditions = this.conditionTimeTable(timeTableId);
		ResultSet results = super.get(this.table, "COUNT(*) AS number", conditions);
		try {
			return results.getInt("number");
		} 
		catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}
