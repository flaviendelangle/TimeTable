/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import timeTableModel.Room;
import timeTableModel.TimeTable;


/**
 * Description of TimeTableDB.
 * 
 * @author Flavien DELANGLE and Marie PAYET
 */
public class TimeTableDB {

	/**
	 * Activate or not the SQL storage
	 */
	private static final Boolean SQLactivated = true;		

	/**
	 * File where the program save all the data if the SQL mode is not activated.
	 */
	private static String fileXML = "";

	/**
	 * File where the program save all the data if the SQL mode is activated.
	 */
	private static String fileSQL = "timeTableDB.db";

	/**
	 * Database connection object
	 */
	public Connection connection;
	
	/**
	 * Statement of the database connection
	 */
	public Statement stmt;

	/**
	 * List of all the timetables
	 */
	private Map<Integer, TimeTable> timeTables = new HashMap<Integer, TimeTable>();;

	/**
	 * List of all the rooms
	 */
	private Map<Integer, Room> rooms = new HashMap<Integer, Room>();;

	/**
	 * The constructor.
	 */
	public TimeTableDB(String fileXML) {
		this.setFileXML(fileXML);
		this.initSQL();
		this.loadDB();
	}
		
	/**
	 * Check is the SQL storage is activated
	 * @return SQL
	 */
	public Boolean isSQL() {
		return TimeTableDB.SQLactivated;
	}

	/**
	 * Sets a value to attribute file. 
	 * @param newFile 
	 */
	public void setFileXML(String newFileXML) {
		TimeTableDB.fileXML = newFileXML;
	}
	
	/**
	 * Returns file.
	 * @return file 
	 */
	public String getFileXML() {
		return TimeTableDB.fileXML;
	}

	/**
	 * Sets a value to attribute file. 
	 * @param newFile 
	 */
	public void setFileSQL(String newFileSQL) {
		TimeTableDB.fileSQL = newFileSQL;
	}
	
	/**
	 * Returns file.
	 * @return file 
	 */
	public String getFileSQL() {
		return TimeTableDB.fileSQL;
	}

	/**
	 * Returns timeTables.
	 * @return timeTables 
	 */
	public Map<Integer, TimeTable> getTimeTables() {
		return this.timeTables;
	}
	
	/**
	 * Sets a value to attribute timeTables. 
	 * @param newTimeTables 
	 */
	public void setTimeTables(Map<Integer, TimeTable> newTimeTables) {
		this.timeTables = newTimeTables;
	}

	/**
	 * Returns rooms.
	 * @return rooms 
	 */
	public Map<Integer, Room> getRooms() {
		return this.rooms;
	}

	/**
	 * Sets a value to attribute rooms. 
	 * @param newRooms 
	 */
	public void setRooms(Map<Integer, Room> newRooms) {
		this.rooms = newRooms;
	}	
	
	/**
	 * Save the current state of the database into a XML file
	 */
	public boolean saveDB() {
		if(this.isSQL()) {
			return true;
		}
		else {
			return this.saveXML(this.timeTables, this.rooms);
		}
	}

	/**
	 * Load a XML file as the current database of the program
	 */
	public boolean loadDB() {
		if(this.isSQL()) {
			return true;
		}
		else {
			return this.loadXML(this.timeTables, this.rooms);
		}
	}

	/**
	 * Save the actual state of the program into a XML database
	 * @param timeTables
	 * @param rooms
	 * @return success
	 */
	public Boolean saveXML(Map<Integer, TimeTable> timeTables, Map<Integer, Room> rooms) {
		Element rootXML = new Element("TimeTablesDB");
		Element roomsXML = new Element("Rooms");
		Element timeTablesXML = new Element("TimeTables");
		
		for(Map.Entry<Integer, Room> entry : rooms.entrySet()) {
			roomsXML.addContent(entry.getValue().toXML());
		}		
		for(Map.Entry<Integer, TimeTable> entry : timeTables.entrySet()) {
			timeTablesXML.addContent(entry.getValue().toXML());
		}
		
		org.jdom2.Document document = new Document(rootXML);
		Boolean success;
		try {
			XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
			xml.output(document, new FileOutputStream(this.getFileXML()));
			success = true;
		}
		catch(java.io.IOException e) {
			success = false;
		}
		return success;		
	}
	
	/**
	 * Load the data of a XML database into the program
	 * @param timeTables
	 * @param rooms
	 * @return success
	 */
	public Boolean loadXML(Map<Integer, TimeTable> timeTables, Map<Integer, Room> rooms) {
		org.jdom2.Document document = null;
		Element rootXML;
		SAXBuilder sxb = new SAXBuilder();
		Boolean success = false;;
		try {
			document = sxb.build(new File(this.getFileXML()));
		}
		catch(Exception e) {
		}
		if(document != null) {
			rootXML = document.getRootElement();
			try {
				success = true;
				Room.parseXML(rootXML.getChild("Rooms"), rooms);
				TimeTable.parseXML(rootXML.getChild("TimeTables"), timeTables, rooms);
			}
			catch(Exception e) {
			}
		}
		return success;		
	}
	
	/**
	 * Create a connection between this program and the SQLite database
	 */
	public void initSQL() {
		if(this.isSQL()) {
			String dbName = this.getFileSQL();
			File file = new File (dbName);
			if(!file.exists() || true) {
				this.XMLtoSQL();
			}
		}
	}
	
	/**
	 * Create a SQLite database with the data of a XML database
	 * @throws SQLException 
	 */
	public Boolean XMLtoSQL() {
		Boolean success = this.loadXML(this.timeTables, this.rooms);
		if(success) {
			File file = new File (this.getFileSQL());
			if(file.exists()) {
				try {
					Files.delete(file.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Class.forName("org.sqlite.JDBC");
				this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.getFileSQL());
				this.stmt = this.connection.createStatement();
			}
			catch(Exception e) {
			}
			success &= this.sql("CREATE TABLE TimeTable (GroupId INT);");
			success &= this.sql("CREATE TABLE Room (RoomId INT, Capacity INT);");
			success &= this.sql("CREATE TABLE Book (BookingId INT UNSIGNED , Login VARCHAR(255), "
				+ "DateBegin DATETIME, DateEnd DATETIME, RoomId INT UNSIGNED , TimeTableId INT UNSIGNED,"
				+ " CONSTRAINT fk_RoomId FOREIGN KEY (TimeTableId) REFERENCES TimeTable(GroupId),"
				+ " CONSTRAINT fk_RoomId FOREIGN KEY (RoomId) REFERENCES Room(RoomId));");
			for(Map.Entry<Integer, Room> entry : this.rooms.entrySet()) {
				success &= this.sql(entry.getValue().toSQL());
			}		
			for(Map.Entry<Integer, TimeTable> entry : this.timeTables.entrySet()) {
				success &= this.sql(entry.getValue().toSQL());
			}
		}
		return success;
	}
	
	public ResultSet query(String request) {
       ResultSet result = null;
       try {
    	   result = this.stmt.executeQuery(request);
       } 
       catch (SQLException e) {
           e.printStackTrace();
           System.out.println("Error in the request : " + request);
       }
       return result;		
	}
	
	public Boolean sql(String request) {
		Boolean success;
		try {
			this.stmt.executeUpdate(request);
			success = true;
		}
		catch (SQLException e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	
	public Boolean containsRoom(int roomId) {
		if(this.isSQL()) {
			Boolean success;
			ResultSet result = this.query("SELECT COUNT(*) as number FROM Room WHERE RoomId = " + roomId);
			try {
				success = (result.getInt("number") == 0);
			}
			catch (SQLException e) {
				success = false;
			}
			return success;
		}
		else {
			return this.getRooms().containsKey(roomId);
		}
	}
	
	public Boolean checkRoom(int key, int roomId, int capacity) {
		if(this.isSQL()) {
			Boolean success;
			ResultSet result = this.query("SELECT COUNT(*) as number FROM Room WHERE RoomId = " + roomId + " AND Capacity = " + capacity);
			try {
				success = (result.getInt("number") > 0);
			}
			catch (SQLException e) {
				success = false;
			}
			return success;			
		}
		else {
			if(this.containsRoom(key)) {
				Room room = this.getRooms().get(key);
				return (room.getId() == roomId && room.getCapacity() == capacity);
			}
			else {
				return false;
			}
		}
	}
	
	public int getRoomsSize() {
		if(this.isSQL()) {
			int size;
			ResultSet result = this.query("SELECT COUNT(*) as number FROM Room");
			try {
				size = result.getInt("number");
			}
			catch (SQLException e) {
				size = 0;
			}
			return size;		
		}
		else {
			return this.getRooms().size();
		}
	}

	public Boolean containsTimeTable(int timeTableId) {
		if(this.isSQL()) {
			Boolean success;
			ResultSet result = this.query("SELECT COUNT(*) as number FROM TimeTable WHERE GroupId = " + timeTableId);
			try {
				success = (result.getInt("number") == 0);
			}
			catch (SQLException e) {
				success = false;
			}
			return success;
		}
		else {
			return this.getTimeTables().containsKey(timeTableId);
		}
	}

	public int getTimeTablesSize() {
		if(this.isSQL()) {
			int size;
			ResultSet result = this.query("SELECT COUNT(*) as number FROM TimeTable");
			try {
				size = result.getInt("number");
			}
			catch (SQLException e) {
				size = 0;
			}
			return size;		
		}
		else {
			return this.getTimeTables().size();
		}
	}

	/**
	 * Returns the login of the teacher of a given booking.
	 * @param timeTableId 
	 * @param bookId 
	 * @return teacherLogin
	 */
	public String getTeacherLogin(int timeTableId, int bookId) {
		String teacherLogin = "";
		if(this.isSQL()) {
			ResultSet resultSet = this.query("SELECT login FROM Book WHERE BookingId = " + bookId);
			System.out.println(resultSet);
		}
		else {
			if(this.getTimeTables().containsKey(timeTableId)) {
				Book booking = this.getTimeTables().get(timeTableId).getBook(bookId);
				if(booking != null) {
					teacherLogin = booking.getTeacherLogin();
				}
			}
		}
		return teacherLogin;
	}

	/**
	 * Description of the method roomsIdToString.
	 * We recover all the keys of the map room in a Set.
	 * Then we cast the set in an array of String.
	 * @return String array (content : ID of the rooms)
	 */
	public String[] roomsIdToString() {
		Set<Integer> roomsIdSet = this.rooms.keySet();
		String[] roomsId = new String[roomsIdSet.size()];
		int i = 0;
		for (Map.Entry<Integer, Room> entry : this.rooms.entrySet()) {
			roomsId[i] = String.valueOf(entry.getKey());
			i++;
		}
		return roomsId;
	}

	/**
	 * Description of the method roomsToString.
	 * We browse all the map rooms to see the values of each Room and we put the informations into an array of String (thanks to the function toString() of Room)
	 * @return String array (content : ID of the rooms + capacity)
	 */
	public String[] roomsToString() {
		
		String[] result = new String[this.rooms.size()];
		int i = 0 ;
		
		for (Map.Entry<Integer, Room> entry : this.rooms.entrySet())
			{
			result[i] =  entry.getValue().toString();
			i++;
			}
		
		return result; 
	}

	/**
	 * Description of the method timeTablesIDToString.
	 * We recover all the keys of the map timeTable in a Set.
	 * Then we cast the set in an array of String.
	 * @return String array (content : ID of the timeTable)
	 */
	public String[] timeTablesIDToString() {
		Set<Integer> timeTablesIdSet = this.timeTables.keySet();
		String[] timeTablesId = new String[timeTablesIdSet.size()];
		int i = 0;
		for (Map.Entry<Integer, TimeTable> entry : this.timeTables.entrySet()) {
			timeTablesId[i] = String.valueOf(entry.getKey());
			i++;
		}
		return timeTablesId;
	}

	/**
	 * Description of the method booksIdToString.
	 * We get the timeTable matching with the ID.
	 * Then we use the function getBookingsId  of timeTable to return a String Array containing the BooksId. 
	 * @param timeTableId 
	 * @return String array (content : booksId)
	 */
	public String[] booksIdToString(Integer timeTableId) {
		TimeTable timeTableResult = timeTables.get(timeTableId);

		if(timeTableResult == null) {
			return new String[0];
		}
		return timeTableResult.getBookingsId();
	}

	/**
	 * Description of the method addRoom.
	 * We check if the room we want to add already exists or not.
	 * If the room does not exist we add it to the map.
	 * @param roomId (Integer)
	 * @param capacity (Integer)
	 * @return Boolean (true if the room is correctly added, false if the room already exist)
	 * @throws SQLException 
	 */
	public Boolean addRoom(Integer roomId, Integer capacity) {
		if(this.isSQL()) {
			Boolean success;
			if(this.containsRoom(roomId)) {
				success = this.sql(Room.createSQL(roomId, capacity));
			}
			else {
				success = false;
			}
			return success;
		}
		else {
			Boolean exist = this.containsRoom(roomId);
			if(!exist){
				Room newRoom = new Room(roomId, capacity);
				this.rooms.put(roomId, newRoom);
			}
			
			return !exist;
		}
	}

	/**
	 * Description of the method removeRoom.
	 * We check if the room we want to remove exists or not.
	 * If the room exists we remove it from the map.
	 * @param roomId (Integer)
	 * @return Boolean ( true if the room is correctly removed, false if the room does not exist)
	 */
	public Boolean removeRoom(Integer roomId) {
		Boolean result = this.containsRoom(roomId);
		if(result){
			this.rooms.remove(roomId);
		}
		return result;
	}

	/**
	 * Description of the method getRoom.
	 * We get the timeTable matching with the ID.
	 * Then we use functions existing in Room to return the Room Id.
	 * @param timeTableId (Integer)
	 * @param bookId (Integer)
	 * @return Id of the Room we search (Integer)
	 */
	public Integer getRoom(Integer timeTableId, Integer bookId) {
		TimeTable timeTableResult = timeTables.get(timeTableId); // r�cup�re le timetable correspondant � l'ID recherch�

		if(timeTableResult == null)return -1;
		else {return timeTableResult.getBook(bookId).getRoom().getId();}
	}

	/**
	 * Description of the method addTimeTable.
	 * @param timeTableId 
	 * @return 
	 */
	public Boolean addTimeTable(int timeTableId) {
		if(this.isSQL()) {
			Boolean success;
			if(this.containsTimeTable(timeTableId)) {
				success = this.sql(TimeTable.createSQL(timeTableId));
			}
			else {
				success = false;
			}
			return success;			
		}
		else {
			Boolean success;
			if(this.getTimeTables().containsKey(timeTableId)) {
				success = false;
			}
			else {
				success = true;
				this.getTimeTables().put(timeTableId, new TimeTable(timeTableId));
			}
			return success;
		}
	}

	/**
	 * Description of the method removeTimeTable.
	 * @param timeTableId 
	 * @return 
	 */
	public Boolean removeTimeTable(int timeTableId) {
		Boolean result = this.timeTables.containsKey(timeTableId);
		if(result){
			this.timeTables.remove(timeTableId);
		}
		return result;		
	}

	/**
	 * Add a booking to a given timetable.
	 * Check if the booking is possible by checking if :
	 * 	- the timetable exists
	 *  - the room exists
	 * @param timeTableId 
	 * @param bookingId 
	 * @param login 
	 * @param dateBegin 
	 * @param dateEnd 
	 * @param roomId 
	 * @return success
	 */
	public Boolean addBooking(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd, Integer roomId) {
		if(this.getRooms().containsKey(roomId) && this.getTimeTables().containsKey(timeTableId)) {
			TimeTable timetable = this.getTimeTables().get(timeTableId); 
			Room room = this.getRooms().get(roomId);
			return timetable.addBooking(bookingId, login, dateBegin, dateEnd, room);
		}
		else {
			return false;
		}
	}

	/**
	 * Description of the method getBookingsDate.
	 * @param timeTableId 
	 * @param dateBegin 
	 * @param dateEnd 
	 */
	public void getBookingsDate(Integer timeTableId, Hashtable<Integer, Date> dateBegin, Hashtable<Integer, Date> dateEnd) {
		TimeTable timeTable = this.getTimeTables().get(timeTableId);
		if(timeTable != null) {
			timeTable.getBookingsDate(dateBegin, dateEnd);
		}
	}

	/**
	 * Remove a booking from a given timetable.
	 * @param timeTableId 
	 * @param bookId 
	 * @return success
	 */
	public Boolean removeBook(Integer timeTableId, Integer bookId) {
		Boolean success;
		if(this.getTimeTables().containsKey(timeTableId)) {
			success = this.getTimeTables().get(timeTableId).removeBook(bookId);
		}
		else {
			success = false;
		}
		return success;
	}

	/**
	 * Return the maximum identifier of the bookings of a given timetable.
	 * @param timeTableId 
	 * @return bookingsMaxId
	 */
	public int getBookingsMaxId(Integer timeTableId) {
		int bookingsMaxId;
		if(this.getTimeTables().containsKey(timeTableId)) {
			bookingsMaxId = this.getTimeTables().get(timeTableId).getBookingsMaxId();
		}
		else {
			bookingsMaxId = -1;
		}
		return bookingsMaxId;
	}

}
