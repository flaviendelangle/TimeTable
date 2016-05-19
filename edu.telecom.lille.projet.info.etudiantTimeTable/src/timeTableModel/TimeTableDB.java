/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import timeTableModel.Room;
import timeTableModel.TimeTable;
// Start of user code (user defined imports)

// End of user code

/**
 * Description of TimeTableDB.
 * 
 * @author delangle
 */
public class TimeTableDB {
	/**
	 * Description of the property timeTables.
	 */
	private Map<Integer, String> mapRooms; //contient toutes les rooms (Id + capacity)
	private Map<Integer, String> mapTimeTable; //contient tous les timetables (Id + reservations)
	
	public HashSet<TimeTable> timeTables = null;

	/**
	 * Description of the property rooms.
	 */
	public HashSet<Room> rooms = null;

	/**
	 * Description of the property file.
	 */
	private Object file = ;


	// Start of user code (user defined attributes for TimeTableDB)

	// End of user code

	/**
	 * The constructor.
	 */
	public TimeTableDB() {
		// Start of user code constructor for TimeTableDB)
		super();
		// End of user code
	}

	/**
	 * Description of the method saveDB.
	 */
	public void saveDB() {
		// Start of user code for method saveDB
		// End of user code
	}

	/**
	 * Description of the method loadDB.
	 */
	public void loadDB() {
		// Start of user code for method loadDB
		// End of user code
	}

	/**
	 * Description of the method getTeacherLogin.
	 * @param timeTableId 
	 * @param bookId 
	 * @return 
	 */
	public String getTeacherLogin(int timeTableId, int bookId) {
		// Start of user code for method getTeacherLogin
		String getTeacherLogin = "";
		return getTeacherLogin;
		// End of user code
	}

	/**
	 * Description of the method roomsIdToString.
	 * @return 
	 * @return 
	 */
	public String[] roomsIdToString() {
		// Start of user code for method roomsIdToString
		Set<Integer> roomsIdToString;
		
		roomsIdToString = mapRooms.keySet(); 
		
		return roomsIdToString.toArray(new String[roomsIdToString.size()]);
		// End of user code
	}

	/**
	 * Description of the method roomsToString.
	 * @return 
	 */
	public HashSet<String> roomsToString() {
		// Start of user code for method roomsToString
		HashSet<String> roomsToString = new HashSet<String>();
		return roomsToString;
		// End of user code
	}

	/**
	 * Description of the method timeTablesIDToString.
	 * @return 
	 */
	public HashSet<String> timeTablesIDToString() {
		// Start of user code for method timeTablesIDToString
		HashSet<String> timeTablesIDToString = new HashSet<String>();
		return timeTablesIDToString;
		// End of user code
	}

	/**
	 * Description of the method booksIdToString.
	 * @param timeTableId 
	 * @return 
	 */
	public HashSet<String> booksIdToString(Integer timeTableId) {
		// Start of user code for method booksIdToString
		HashSet<String> booksIdToString = new HashSet<String>();
		return booksIdToString;
		// End of user code
	}

	/**
	 * Description of the method addRoom.
	 * @param roomId 
	 * @param capacity 
	 * @return 
	 */
	public Boolean addRoom(Integer roomId, Integer capacity) {
		// Start of user code for method addRoom
		Boolean addRoom = Boolean.FALSE;
		return addRoom;
		// End of user code
	}

	/**
	 * Description of the method removeRoom.
	 * @param roomId 
	 * @return 
	 */
	public Boolean removeRoom(Integer roomId) {
		// Start of user code for method removeRoom
		Boolean removeRoom = Boolean.FALSE;
		return removeRoom;
		// End of user code
	}

	/**
	 * Description of the method getRoom.
	 * @param timeTableId 
	 * @param bookId 
	 * @return 
	 */
	public Integer getRoom(Integer timeTableId, Integer bookId) {
		// Start of user code for method getRoom
		Integer getRoom = Integer.valueOf(0);
		return getRoom;
		// End of user code
	}

	/**
	 * Description of the method addTimeTable.
	 * @param timeTableId 
	 * @return 
	 */
	public Boolean addTimeTable(int timeTableId) {
		// Start of user code for method addTimeTable
		Boolean addTimeTable = Boolean.FALSE;
		return addTimeTable;
		// End of user code
	}

	/**
	 * Description of the method removeTimeTable.
	 * @param timeTableId 
	 * @return 
	 */
	public Boolean removeTimeTable(int timeTableId) {
		// Start of user code for method removeTimeTable
		Boolean removeTimeTable = Boolean.FALSE;
		return removeTimeTable;
		// End of user code
	}

	/**
	 * Description of the method addBooking.
	 * @param timeTableId 
	 * @param bookingId 
	 * @param login 
	 * @param dateBegin 
	 * @param dateEnd 
	 * @param roomId 
	 * @return 
	 */
	public Boolean addBooking(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd,
			Integer roomId) {
		// Start of user code for method addBooking
		Boolean addBooking = Boolean.FALSE;
		return addBooking;
		// End of user code
	}

	/**
	 * Description of the method getBookingsDate.
	 * @param timeTableId 
	 * @param dateBegin 
	 * @param dateEnd 
	 */
	public void getBookingsDate(Integer timeTableId, Date dateBegin, Date dateEnd) {
		// Start of user code for method getBookingsDate
		// End of user code
	}

	/**
	 * Description of the method removeBook.
	 * @param timeTableId 
	 * @param bookId 
	 * @return 
	 */
	public Boolean removeBook(Integer timeTableId, Integer bookId) {
		// Start of user code for method removeBook
		Boolean removeBook = Boolean.FALSE;
		return removeBook;
		// End of user code
	}

	/**
	 * Description of the method getBookingsMaxId.
	 * @param timeTableId 
	 * @return 
	 */
	public Integer getBookingsMaxId(Integer timeTableId) {
		// Start of user code for method getBookingsMaxId
		Integer getBookingsMaxId = Integer.valueOf(0);
		return getBookingsMaxId;
		// End of user code
	}

	/**
	 * Returns timeTables.
	 * @return timeTables 
	 */
	public HashSet<TimeTable> getTimeTables() {
		return this.timeTables;
	}

	
	/**
	 * Sets a value to attribute timeTables. 
	 * @param newTimeTables 
	 */
	public void setTimeTables(HashSet<TimeTable> newTimeTables) {
		this.timeTables = newTimeTables;
	}

	/**
	 * Returns rooms.
	 * @return rooms 
	 */
	public HashSet<Room> getRooms() {
		return this.rooms;
	}

	/**
	 * Sets a value to attribute rooms. 
	 * @param newRooms 
	 */
	public void setRooms(HashSet<Room> newRooms) {
		this.rooms = newRooms;
	}

	/**
	 * Returns file.
	 * @return file 
	 */
	public Object getFile() {
		return this.file;
	}

	/**
	 * Sets a value to attribute file. 
	 * @param newFile 
	 */
	public void setFile(Object newFile) {
		this.file = newFile;
	}


}
