package timeTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ORM implementation for the Book class.
 * 
 * @author Flavien DELANGLE and Marie PAYET
 * @version 06/2016
 */
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
	 * @param bookId Id of the book
	 * @return Stringified version of this condition
	 */
	private String conditionId(int bookId) {
		return super.condition(this.idColumn, bookId);
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the timetable
	 * @param timeTableId Id of the timeTable of the book
	 * @return Stringified version of this condition
	 */
	private String conditionTimeTable(int timeTableId) {
		return super.condition(this.timeTableColumn, timeTableId);
	}

	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the login of the teacher
	 * @param timeTableId Login of the teacher of the book
	 * @return Stringified version of this condition
	 */
	private String conditionLogin(String login) {
		return super.condition(this.loginColumn, login);
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the beginning date
	 * @param dateBegin Beginning date of the book
	 * @return Stringified version of this condition
	 */
	private String conditionDateBegin(Date dateBegin) {
		return super.condition(this.dateBeginColumn, dateBegin);
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the ending date
	 * @param dateEnd Ending date of the book
	 * @return Stringified version of this condition
	 */
	private String conditionDateEnd(Date dateEnd) {
		return super.condition(this.dateEndColumn, dateEnd);
	}
	
	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a book by giving the room
	 * @param roomId Id of the room of this book
	 * @return Stringified version of this condition
	 */
	private String conditionRoom(int roomId) {
		return super.condition(this.roomColumn, roomId);
	}
	
	/**
	 * Return the list of the columns to create a new book in the database
	 * @return Columns to use to create a new book
	 */
	private String insertColumns() {
		return this.idColumn + "," + this.loginColumn + "," + this.dateBeginColumn + "," + 
			   this.dateEndColumn + "," + this.roomColumn + "," + this.timeTableColumn;
	}
	
	/**
	 * Return the list of the values to create a new book in the database
	 * @param timeTableId Id of the timetable in which this book is created
	 * @param bookingId Id of this book
	 * @param login Login of the teacher of this book
	 * @param dateBegin Beginning date of this book
	 * @param dateEnd Ending date of this book
	 * @param roomId Id of the room of this book
	 * @return Values to use to create a new book
	 */
	private String insertValues(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd, int roomId) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateBeginStr = dateformat.format(dateBegin);
		String dateEndStr = dateformat.format(dateEnd);
		String values = bookingId + ",\"" + login + "\",\"" + dateBeginStr + "\",\"" + dateEndStr
						+ "\"," + roomId + "," + timeTableId;
		return values;
	}
	
	/**
	 * Get all the books in the right timetable
	 * @param timeTableId Id of the timetable of which we want to retrieve the books
	 * @return All the books linked to the right timetable
	 */
	public ResultSet get(int timeTableId) {
		String conditions = this.conditionTimeTable(timeTableId);
		return super.get(this.table, "*", conditions);
	}
	
	/**
	 * Get all the books in the right timetable and with the right id (there should never be more than one)
	 * @param timeTableId Id of the timetable of which we want to retrieve a book
	 * @param bookId Id of the book we want to retrieve
	 * @return All the books with the right id
	 */
	public ResultSet get(int timeTableId, int bookId) {
		String conditions = this.conditionId(bookId) + " AND " + this.conditionTimeTable(timeTableId);
		return super.get(this.table, "*", conditions);
	}
	
	/**
	 * Create a new Book in the database
	 * @param timeTableId Id of the timetable of the new book
	 * @param bookingId Id of the new book
	 * @param login Login of the teacher of the new book
	 * @param dateBegin Beginning date of the new book
	 * @param dateEnd Ending date of the new book
	 * @param roomId Id of the room of the new book
	 * @return Has the book been successfully created ?
	 */
	public Boolean create(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd, int roomId) {
		return super.create(this.table, this.insertColumns(), this.insertValues(timeTableId, bookingId, login, dateBegin, dateEnd, roomId));
	}

	/**
	 * Remove the Book in the right timetable and with the right id
	 * @param timeTableId Timetable in which the book the delete is
	 * @param bookId Id of the book we want to remove
	 * @return Has the book been successfully deleted ?
	 */
	public Boolean delete(int timeTableId, int bookId) {
		String conditions = this.conditionId(bookId) + " AND " + this.conditionTimeTable(timeTableId);
		return super.delete(this.table, conditions);
	}	
		
	/**
	 * Check if there is a book with the right id
	 * @param bookId Id of the book of which we want to check the extistence
	 * @param default_value Value to return is there is an issue
	 * @return Does this book exist ?
	 */
	public Boolean exist(int bookId, Boolean default_value) {
		String conditions = this.conditionId(bookId);
		return super.exist(this.table, conditions, default_value);
	}

	
	/**
	 * Check if there is a book with the right id and the right timetable
	 * @param timeTableId Id of the timetable that the book we want to find must have
	 * @param bookId Id that the book we want to find must have
	 * @param default_value Value to return is there is an issue
	 * @return Does this book exist ?
	 */
	public Boolean exist(int timeTableId, int bookId, Boolean default_value) {
		String conditions = this.conditionId(bookId) + " AND " + this.conditionTimeTable(timeTableId);
		return super.exist(this.table, conditions, default_value);
	}

	/**
	 * Check if there is a book which match all the conditions
	 * @param timeTableId Id of the timetable that the book we want to find must have
	 * @param bookId Id that the book we want to find must have
	 * @param login Login that the book we want to find must have
	 * @param dateBegin Beginning date that the book we want to find must have
	 * @param dateEnd Ending date that the book we want to find must have
	 * @param roomId Id of the room that the book we want to find must have
	 * @param default_value Value to return is there is an issue
	 * @return Does this book exist ?
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
	 * @return Number of books stored in this database)
	 */
	public int length() {
		return super.length(this.table);
	}
	
	/**
	 * Get the number of books in a timetable
	 * @param timeTableId ID of the timetable from which we want to have the number of book
	 * @return Number of books stored in this database in the right timetable
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
