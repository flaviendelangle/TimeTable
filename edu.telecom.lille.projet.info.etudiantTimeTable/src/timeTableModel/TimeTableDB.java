/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import timeTableModel.Room;
import timeTableModel.TimeTable;


/**
 * Main file of the program. Only file called by the controller.
 * 
 * @author Flavien DELANGLE and Marie PAYET
 * @version 06/2016
 */
public class TimeTableDB {

	/**
	 * Activate or not the SQL storage
	 */
	private static final Boolean SQLactivated = false;		

	/**
	 * File where the program save all the data if the SQL mode is not activated.
	 */
	private static String fileXML = "";

	/**
	 * File where the program save all the data if the SQL mode is activated.
	 */
	private static String fileSQL = "timeTableDB.db";

	/**
	 * Date at which the XML database has been loaded for the last time
	 */
	private long modificationDate;
	
	/**
	 * List of all the timetables
	 */
	private Map<Integer, TimeTable> timeTables = new HashMap<Integer, TimeTable>();

	/**
	 * List of all the rooms
	 */
	private Map<Integer, Room> rooms = new HashMap<Integer, Room>();;

	/**
	 * Description of the last modification in order to check for conflict
	 */
	private String[] modification = { "", "", "" };
	
	/**
	 * The constructor.
	 * @param fileXML File where all the XML database is stored
	 */
	public TimeTableDB(String fileXML) {
		this.setFileXML(fileXML);
		this.loadDB();
	}
		
	/**
	 * Check is the SQL storage is activated
	 * @return Boolean which indicates if the SQL is activated '1' or not '0'
	 */
	public Boolean isSQL() {
		return TimeTableDB.SQLactivated;
	}

	/**
	 * Update the name of the XML database.
	 * @param newFileXML A file which will be set up as our new XML database
	 */
	public void setFileXML(String newFileXML) {
		TimeTableDB.fileXML = newFileXML;
	}
	
	/**
	 * Returns the name of the XML database.
	 * @return A file with our XML database 
	 */
	public String getFileXML() {
		return TimeTableDB.fileXML;
	}

	/**
	 * Update the name of the SQLite database. 
	 * @param newFileSQL A file which will be set up as our new SQL database
	 */
	public void setFileSQL(String newFileSQL) {
		TimeTableDB.fileSQL = newFileSQL;
	}
	
	/**
	 * Returns the name of the SQLite database.
	 * @return Name of the SQL database
	 */
	public String getFileSQL() {
		return TimeTableDB.fileSQL;
	}
	
	/**
	 * Update the date of the last modification of the XML database
	 * @param newModificationDate The new date of modification of the XML database
	 */
	public void setModificationDate(long newModificationDate) {
		this.modificationDate = newModificationDate;
	}

	/**
	 * Returns the date of the last modification of the XML database
	 * @return The last date of modification of the XML database
	 */
	public long getModificationDate() {
		return this.modificationDate;
	}

	/**
	 * Returns timeTablesGroup.
	 * @return A list of all the timeTables
	 */
	public Map<Integer, TimeTable> getTimeTables() {
		return this.timeTables;
	}
	
	/**
	 * Sets a value to attribute timeTables. 
	 * @param newTimeTables List which contains timeTables
	 */
	public void setTimeTables(Map<Integer, TimeTable> newTimeTables) {
		this.timeTables = newTimeTables;
	}

	/**
	 * Returns rooms.
	 * @return A list with all the rooms of the system 
	 */
	public Map<Integer, Room> getRooms() {
		return this.rooms;
	}

	/**
	 * Sets a value to attribute rooms. 
	 * @param newRooms A list which contains the new rooms to set
	 */
	public void setRooms(Map<Integer, Room> newRooms) {
		this.rooms = newRooms;
	}
	
	/**
	 * Returns modifications of the database.
	 * @return Contains the last modification of the database
	 */
	public String[] getModification() {
		return this.modification;
	}
	
	/**
	 * Update the last modification of the database
	 * @param newType The type of the new modification done
	 * @param newValue The change done in the database 
	 * @param parentId Optionnal Id usefull to store the TimeTable Id of a book
	 */
	public void setModification(String newType, String newValue, int parentId) {
		this.setModification(newType, newValue, parentId, false);
		if(!this.isSQL()) {
			this.saveDB();
		}
	}
	
	/**
	 * Update the type of the last modification (no saving of the database)
	 * Update the last modification of the database
	 * @param newType The type of the new modification done
	 * @param newValue The change done in the database 
	 * @param parentId Optionnal Id usefull to store the TimeTable Id of a book
	 * @param isTest Boolean that will indicate that this modification is just a test and that it doen not have to be save in the database  
	 */
	public void setModification(String newType, String newValue, int parentId, Boolean isTest) {
		this.modification[0] = newType;
		this.modification[1] = newValue;		
		this.modification[2] = String.valueOf(parentId);
	}
	
	/**
	 * Save the current state of the database into a XML file
	 * @return Indicates if the save of the DB has successfully be done 
	 */
	public boolean saveDB() {
		if(this.isSQL()) {
			return this.closeSQL();
		}
		else {
			if(!this.modifiedXML() || !this.conflictualXMLDB()) {
				Boolean success = this.saveXML();
				this.setModificationDate(this.getModificationDate());
				return success;
			}
			else {
				return false;
			}
		}
	}

	/**
	 * Load a XML file as the current database of the program
	 * @return Indicates if the load of the DB has successfully be done 
	 */
	public boolean loadDB() {
		if(this.isSQL()) {
			return this.initSQL();
		}
		else {
			Boolean success = this.loadXML(this.getTimeTables(), this.getRooms());
			this.setModificationDate(this.getLastModificationXML());
			return success;
		}
	}
	
	/**
	 * Return the timestamp of the last modification of the XML database
	 * @return The date of the last modification done in the database 
	 */
	public long getLastModificationXML() {
		File file = new File(this.getFileXML());
		return file.lastModified();		
	}
	
	/**
	 * Check if the XML database has been modified since the last time it has beed loaded
	 * @return Boolean which indicates if the database has been changed since the last load 
	 */
	public Boolean modifiedXML() {
		return (this.getModificationDate() != this.getLastModificationXML());
	}
	
	/**
	 * Check if the XML database and the live objects are conflictual
	 * @return Boolean which indicates true if the database change can cause any conflict  
	 */
	public Boolean conflictualXMLDB() {
		Boolean conflict;
		modification = this.getModification();
		try {
			Element fileXML = this.getXML();
			Element liveXML = this.getLiveXML();
			if(modification[0] != "add") {
				conflict = false;
			}
			else if(modification[1] == "timetable") {
				// Check if the ID is free
				conflict = true;				
			}
			else if(modification[1] == "room") {
				// Check if the Room is free (if not check if the capacity is the same
				conflict = true;
			}
			else if(modification[1] == "book") {
				int timeTableId = Integer.parseInt(modification[2]);
				int bookId = this.getBookingsMaxId(timeTableId);
				conflict = TimeTableBuilder.checkBookingConflict(fileXML, timeTableId, bookId, this.getTimeTables());
			}
			else {
				conflict = false;
			}
		} 
		catch (Exception e) {
			conflict = false;
			e.printStackTrace();
		}
		return conflict;
	}
	
	/**
	 * Save the actual state of the program into a XML database
	 * @return If the program has been correctly saved 
	 */
	public Boolean saveXML() {
		Element rootXML = this.getLiveXML();
		org.jdom2.Document document = new Document(rootXML);
		Boolean success;
		
		try {
			XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
			xml.output(document, new FileOutputStream(this.getFileXML()));
			success = true;
		}
		catch(java.io.IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;		
	}
	
	/**
	 * Load the data of the XML database into the program
	 * @param timeTables The list of all the timeTables
	 * @param rooms The list of all the rooms
	 * @return Boolean which indicates true if the data has been correctly load 
	 */
	public Boolean loadXML(Map<Integer, TimeTable> timeTables, Map<Integer, Room> rooms) {
		Element rootXML = null;
		Boolean success;
		try {
			rootXML = this.getXML();
			success = true;
		}
		catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		if(rootXML != null) {
			Room.parseXML(rootXML.getChild("Rooms"), rooms);
			TimeTable.parseXML(rootXML.getChild("TimeTables"), timeTables, rooms);			
		}
		return success;		
	}
	
	/**
	 * Return the XML representation of the live database
	 * @return The representation of the live database 
	 */
	public Element getLiveXML() {
		Element rootXML = new Element("TimeTablesDB");
		Element roomsXML = new Element("Rooms");
		Element timeTablesXML = new Element("TimeTables");
		
		for(Map.Entry<Integer, Room> entry : this.rooms.entrySet()) {
			roomsXML.addContent(entry.getValue().toXML());
		}
		for(Map.Entry<Integer, TimeTable> entry : this.timeTables.entrySet()) {
			timeTablesXML.addContent(entry.getValue().toXML());
		}

		rootXML.addContent(roomsXML);
		rootXML.addContent(timeTablesXML);
		
		return rootXML;
	}
	
	/**
	 * Return the content of the XML database
	 * @throws IOException Catch a IO Error
	 * @throws JDOMException Catch a JDOM Error
	 * @return The representation of the live database 
	 */
	public Element getXML() throws JDOMException, IOException {
		org.jdom2.Document document = null;
		Element rootXML = null;
		SAXBuilder sxb = new SAXBuilder();
		document = sxb.build(new File(this.getFileXML()));
		if(document != null) {
			rootXML = document.getRootElement();
		}
		return rootXML;
	}
	
	/**
	 * Create a connection between this program and the SQLite database
	 * @return Boolean which indicates if the initialization has been correctly done 
	 */
	public Boolean initSQL() {
		if(this.isSQL()) {
			String dbName = this.getFileSQL();
			File file = new File (dbName);
			if(!file.exists() || true) {
				this.XMLtoSQL();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Close the connection and the statement with the SQLite database
	 * @return Boolean hich indicates if the close of the connection has been correctly done 
	 */
	public Boolean closeSQL() {
		Boolean success;
		try {
			ORM.stmt.close();
			ORM.connection.close();
			ORM.stmt = null;
			ORM.connection = null;
			success = true;
		}
		catch(Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}
	
	/**
	 * Create a SQLite database with the data of a XML database
	 * @return Boolean which indicates if the conversion has been correctly done 
	 */
	public Boolean XMLtoSQL()  {
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
				if(ORM.connection == null) {
					ORM.connection = DriverManager.getConnection("jdbc:sqlite:" + this.getFileSQL());
				}
				if(ORM.stmt == null) {
					ORM.stmt = ORM.connection.createStatement();
				}
			}
			catch(Exception e) {
			}
			success &= this.sql("CREATE TABLE TimeTable (GroupId INT, Login VARCHAR(255));");
			success &= this.sql("CREATE TABLE Room (RoomId INT, Capacity INT);");
			success &= this.sql("CREATE TABLE Book (BookingId INT UNSIGNED , Login VARCHAR(255),"
				+ " DateBegin DATETIME, DateEnd DATETIME, RoomId INT UNSIGNED , TimeTableId INT UNSIGNED,"
				+ " CONSTRAINT fk_RoomId FOREIGN KEY (TimeTableId) REFERENCES TimeTable(GroupId),"
				+ " CONSTRAINT fk_RoomId FOREIGN KEY (RoomId) REFERENCES Room(RoomId));");
			for(Map.Entry<Integer, Room> entry : this.rooms.entrySet()) {
				success &= entry.getValue().toSQL();
			}		
			for(Map.Entry<Integer, TimeTable> entry : this.timeTables.entrySet()) {
				success &= entry.getValue().toSQL();
			}
		}
		return success;
	}
	
	/**
	 * Make SQL request
	 * @param request Name of the request 
	 * @return Boolean which indicates if the request has been correctly done 
	 */
	public Boolean sql(String request) {
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
	 * Check if room exist 
	 * @param roomId The Id of the room we want to check 
	 * @return Boolean which indicates if the room exists 
	 */
	public Boolean containsRoom(int roomId) {
		if(this.isSQL()) {
			return Room.objects.exist(roomId, false);
		}
		else {
			return this.getRooms().containsKey(roomId);
		}
	}
	
	/**
	 * Check if room exist 
	 * @param key The key (Id of the room) we want to check 
	 * @param roomId The Id of the room we want to check 
	 * @param capacity The Capacity of the room we want to check
	 * @return Boolean which indicates if the room exists 
	 */
	public Boolean checkRoom(int key, int roomId, int capacity) {
		if(this.isSQL()) {
			return Room.objects.exist(roomId, capacity, false);			
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
	
	/**
	 * Returns the size of the rooms 
	 * @return Size the the length of rooms
	 */
	public int getRoomsSize() {
		if(this.isSQL()) {
			return Room.objects.length();		
		}
		else {
			return this.getRooms().size();
		}
	}

	/**
	 * Check if a TimeTableGroup exists 
	 * @param timeTableId The Id of the timeTable we want to check 
	 * @return Boolean which indicates if the timeTable exists
	 */
	public Boolean containsTimeTable(int timeTableId) {
		if(this.isSQL()) {
			return TimeTable.objects.exist(timeTableId, false);
		}
		else {
			return this.getTimeTables().containsKey(timeTableId);
		}
	}
	
	/**
	 * Check if a TimeTableTeacher exists 
	 * @param login The login of the timeTableTeacher we want to check 
	 * @return Boolean which indicates if the timeTable exists
	 */
	public Boolean containsTimeTable(String login) {
		if(this.isSQL()) {
			return TimeTable.objects.exist(login, false);
		}
		else {
			Boolean success = false;
			for (Map.Entry<Integer, TimeTable> entry : this.getTimeTables().entrySet()) {
				success |= (entry.getValue().getType() == "teacher")
						&& (entry.getValue().getLogin() == login);
			}
			return success;
		}
	}

	/**
	 * Returns the size of the timeTables
	 * @return Number of timetables
	 */
	public int getTimeTablesSize() {
		if(this.isSQL()) {
			return TimeTable.objects.length();		
		}
		else {
			return this.getTimeTables().size();
		}
	}
	
	/**
	 * Check if a Book exists 
	 * @param timeTableId The Id of the timeTable where the book we want to check is supposed to be  
	 * @param bookingId The Id of the book
	 * @return Boolean which indicates if the book exists
	 */
	public Boolean containsBook(int timeTableId, int bookingId) {
		if(this.isSQL()) {
			return Book.objects.exist(timeTableId, bookingId, false);
		}
		else {
			if(this.containsTimeTable(timeTableId)) {
				return this.getTimeTables().get(timeTableId).getBooks().containsKey(bookingId);
			}
			else {
				return false;
			}
		}			
	}
	
	/**
	 * Check if a Book exists 
	 * @param timeTableId The Id of the timeTable where the book we want to check is supposed to be  
	 * @param bookingId The Id of the book
	 * @param login The login of the teacher associated to the book
	 * @param dateBegin The date of beginning of the book 
	 * @param dateEnd The date of end of the book 
	 * @param roomId The id if the room concerned 
	 * @return Boolean which indicates if the book exists
	 */
	public Boolean containsBook(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd, int roomId) {
		if(this.isSQL()) {
			return Book.objects.exist(timeTableId, bookingId, login, dateBegin, dateEnd, roomId, false);
		}
		else {
			if(this.getTimeTables().get(timeTableId).getBooks().containsKey(bookingId)) {
				Book book = this.getTimeTables().get(timeTableId).getBooks().get(bookingId);
				Boolean success = true;
				success &= (book.getId() == bookingId);
				success &= (book.getRoom().getId() == roomId);
				success &= book.getTeacherLogin().equals(login);
				success &= (book.getDateBegin().equals(dateBegin));
				success &= book.getDateEnd().equals(dateEnd);
				return success;
			}
			else {
				return false;
			}
		}		
	}

	/**
	 * Returns the size of the books in a timetable
	 * @param timeTableId Id of the timetable from which we want to count the books
	 * @return Number of books in a timetable
	 */
	public int getBooksSize(int timeTableId) {
		if(this.isSQL()) {
			return Book.objects.length(timeTableId);		
		}
		else {
			return this.getTimeTables().get(timeTableId).getBooks().size();
		}
	}

	/**
	 * Returns the login of the teacher of a given booking.
	 * @param timeTableId The id of the timetable
	 * @param bookId The id of the book
	 * @return The login associated with the teacher who have done the book
	 */
	public String getTeacherLogin(int timeTableId, int bookId) {
		String teacherLogin = "";
		if(this.isSQL()) {
			try {
				teacherLogin = Book.objects.get(timeTableId, bookId).getString("login");
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			if(this.containsTimeTable(timeTableId)) {
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
	 * @return StringArray containing the IDs of the rooms
	 */
	public String[] roomsIdToString() {
		if(this.isSQL()) {
			List<String> roomsIdList = new ArrayList<String>();
			ResultSet results = Room.objects.all();
			try {
				while(results.next()) {
					roomsIdList.add(results.getString("RoomId"));
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			return roomsIdList.toArray(new String[roomsIdList.size()]);
		}
		else {
			Set<Integer> roomsIdSet = this.rooms.keySet();
			String[] roomsId = new String[roomsIdSet.size()];
			int i = 0;
			for (Map.Entry<Integer, Room> entry : this.rooms.entrySet()) {
				roomsId[i] = String.valueOf(entry.getKey());
				i++;
			}
			return roomsId;
		}
	}

	/**
	 * Description of the method roomsToString.
	 * We browse all the map rooms to see the values of each Room and we put the informations into an array of String 
	 * (thanks to the method toString of Room)
	 * @return StringArray containing the IDs of the rooms and the capacities
	 */
	public String[] roomsToString() {
		if(this.isSQL()) {
			List<String> roomsList = new ArrayList<String>();
			ResultSet results = Room.objects.all();
			try {
				while(results.next()) {
					roomsList.add(Room.stringify(results.getInt("RoomId"), results.getInt("Capacity")));
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			return roomsList.toArray(new String[roomsList.size()]);			
		}
		else {
			String[] result = new String[this.rooms.size()];
			int i = 0 ;
			
			for (Map.Entry<Integer, Room> entry : this.rooms.entrySet())
				{
				result[i] =  entry.getValue().toString();
				i++;
				}
			
			return result; 
		}
	}

	/**
	 * Description of the method timeTablesIDToString.
	 * We recover all the keys of the map timeTable in a Set.
	 * Then we cast the set in an array of String.
	 * @return StringArray containing the IDs of the timeTable
	 */
	public String[] timeTablesIDToString() {
		if(this.isSQL()) {
			List<String> timeTableIdList = new ArrayList<String>();
			ResultSet results = TimeTable.objects.all();
			try {
				while(results.next()) {
					timeTableIdList.add(results.getString("GroupId"));
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			return timeTableIdList.toArray(new String[timeTableIdList.size()]);
		}
		else {
			Set<Integer> timeTablesIdSet = this.getTimeTables().keySet();
			String[] timeTablesId = new String[timeTablesIdSet.size()];
			int i = 0;
			for (Entry<Integer, TimeTable> entry : this.getTimeTables().entrySet()) {
				timeTablesId[i] = String.valueOf(entry.getKey());
				i++;
			}
			return timeTablesId;
		}
	}

	/**
	 * Description of the method booksIdToString.
	 * We get the timeTable matching with the ID.
	 * Then we use the function getBookingsId  of timeTable to return a String Array containing the BooksId. 
	 * @param timeTableId The id of the timetable
	 * @return StringArray containing the IDs of the books
	 */
	public String[] booksIdToString(Integer timeTableId) {
		if(this.isSQL()) {
			List<String> bookIdList = new ArrayList<String>();
			ResultSet results = Book.objects.get(timeTableId);
			try {
				while(results.next()) {
					bookIdList.add(results.getString("BookingId"));
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			return bookIdList.toArray(new String[bookIdList.size()]);			
		}
		else {
			TimeTable timeTableResult = this.getTimeTables().get(timeTableId);
			if(timeTableResult == null) {
				return new String[0];
			}
			return timeTableResult.getBookingsId();
		}
	}

	/**
	 * Description of the method addRoom.
	 * We check if the room we want to add already exists or not.
	 * If the room does not exist we add it to the map.
	 * @param roomId The id of the room 
	 * @param capacity The capacity of the room 
	 * @return True if the room is correctly added, false if the room already exists
	 */
	public Boolean addRoom(Integer roomId, Integer capacity) {
		Boolean success;
		if(this.isSQL()) {
			if(!this.containsRoom(roomId)) {
				success = Room.objects.create(roomId, capacity);
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
			
			success = !exist;
		}
		if(success) {
			this.setModification("add", "room", 0);
		}
		return success;
	}

	/**
	 * Description of the method removeRoom.
	 * We check if the room we want to remove exists or not.
	 * If the room exists we remove it from the map.
	 * @param roomId The id of the room
	 * @return True if the room is correctly removed, false if the room does not exist
	 */
	public Boolean removeRoom(int roomId) {
		Boolean success;
		if(this.isSQL()) {
			success = Room.objects.delete(roomId);
		}
		else {
			Boolean result = this.containsRoom(roomId);
			if(result){
				this.rooms.remove(roomId);
			}
			success = result;
		}
		if(success) {
			this.setModification("remove", "room", 0);
		}
		return success;
	}

	/**
	 * Description of the method getRoom.
	 * We get the timeTable matching with the ID.
	 * Then we use functions existing in Room to return the Room Id.
	 * @param timeTableId The id of the timetable
	 * @param bookId The id of the book
	 * @return The Id of the Room we search 
	 */
	public Integer getRoom(int timeTableId, int bookId) {
		if(this.isSQL()) {
			int result;
			try {
				result = Book.objects.get(timeTableId, bookId).getInt("RoomId");
			} 
			catch (SQLException e) {
				result = -1;
				e.printStackTrace();
			}
			return result;
		}
		else {
			TimeTable timeTableResult = this.getTimeTables().get(timeTableId);
			if(timeTableResult == null) {
				return -1;
			}
			else {
				return timeTableResult.getBook(bookId).getRoom().getId();
			}			
		}
	}

	/**
	 * Description of the method addTimeTable.
	 * @param groupId The id of the group
	 * @return Ttrue if the timetable has been correctly added, false if not
	 */
	public Boolean addTimeTable(int groupId) {
		Boolean success;
		if(this.isSQL()) {
			if(!this.containsTimeTable(groupId)) {
				success = TimeTable.objects.create(groupId, "");
			}
			else {
				success = false;
			}
		}
		else {
			if(this.containsTimeTable(groupId)) {
				success = false;
			}
			else {
				success = true;
				this.getTimeTables().put(groupId, new TimeTable(groupId));
			}
		}
		if(success) {
			this.setModification("add", "timetable", 0);
		}
		return success;
	}
	
	/**
	 * Description of the method addTimeTable.
	 * @param timeTableId The id of the timetable
	 * @param teacherLogin The login of the teacher 
	 * @return True if the timetable has been correctly added, false if not
	 */
	public Boolean addTimeTable(int timeTableId, String teacherLogin) {
		Boolean success;
		if(this.isSQL()) {
			if(!this.containsTimeTable(teacherLogin) && !this.containsTimeTable(timeTableId)) {
				success = TimeTable.objects.create(timeTableId, teacherLogin);
			}
			else {
				success = false;
			}
		}
		else {
			if(this.containsTimeTable(teacherLogin) || this.containsTimeTable(timeTableId)) {
				success = false;
			}
			else {
				success = true;
				this.getTimeTables().put(timeTableId, new TimeTable(timeTableId, teacherLogin));
			}
		}
		if(success) {
			this.setModification("add", "timetable", 0);
		}
		return success;
	}


	/**
	 * Description of the method removeTimeTable.
	 * @param timeTableId The id ofthe timetable we want to remove 
	 * @return True if the timetable has been correctly removed, false if not
	 */
	public Boolean removeTimeTable(int timeTableId) {
		Boolean success;
		if(this.isSQL()) {
			success = TimeTable.objects.delete(timeTableId);
		}
		else {
			Boolean result = this.containsTimeTable(timeTableId);
			if(result){
				this.timeTables.remove(timeTableId);
			}
			success = result;		
		}
		if(success) {
			this.setModification("remove", "timetable", timeTableId);
		}
		return success;
	}

	/**
	 * Add a booking to a given timetable.
	 * Check if the booking is possible by checking if :
	 * 	- the timetable exists
	 *  - the room exists
	 * @param timeTableId The id ofthe timetable
	 * @param bookingId The id of the book
	 * @param login the Login of the timetable teacher 
	 * @param dateBegin Date of beginning of the book 
	 * @param dateEnd Date of end of the book 
	 * @param roomId The id of the room we want to book
	 * @return True if the book has been correctly done, false if not
	 */
	public Boolean addBooking(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd, int roomId) {
		Boolean success;
		if(this.isSQL()) {
			if(!this.containsBook(timeTableId, bookingId)) {
				success = Book.objects.create(timeTableId, bookingId, login, dateBegin, dateEnd, roomId);
			}
			else {
				success = false;
			}
			return success;					
		}
		else {
			if(this.containsRoom(roomId) && this.containsTimeTable(timeTableId)) {
				TimeTable timetable = this.getTimeTables().get(timeTableId); 
				Room room = this.getRooms().get(roomId);
				success = TimeTableBuilder.isBookingPossible(timeTableId, bookingId, login, dateBegin, dateEnd, room, this.getTimeTables());
				if(success) {
					timetable.addBooking(bookingId, login, dateBegin, dateEnd, room);
				}
			}
			else {
				success = false;
			}
		}
		if(success) {
			this.setModification("add", "book", timeTableId);
		}
		return success;
	}

	/**
	 * Get the booking dates 
	 * @param timeTableId The id of the timetable
	 * @param dateBegin Date of beginning of the book 
	 * @param dateEnd Date of end of the book 
	 */
	public void getBookingsDate(int timeTableId, Hashtable<Integer, Date> dateBegin, Hashtable<Integer, Date> dateEnd) {
		if(this.isSQL()) {
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ResultSet books = Book.objects.get(timeTableId);
			try {
				while(books.next()) {
					dateBegin.put(books.getInt("BookingId"), dateformat.parse(books.getString("DateBegin")));
					dateEnd.put(books.getInt("BookingId"), dateformat.parse(books.getString("DateEnd")));
				}
			}
			catch (Exception e) {
			}
		}
		else {
			TimeTable timeTable = this.getTimeTables().get(timeTableId);
			if(timeTable != null) {
				timeTable.getBookingsDate(dateBegin, dateEnd);
			}
		}
	}

	/**
	 * Remove a booking from a given timetable.
	 * @param timeTableId The id of the timetable
	 * @param bookId The id of the book 
	 * @return True if the book has been correctly removed, false if not
	 */
	public Boolean removeBook(int timeTableId, int bookId) {
		Boolean success;
		if(this.isSQL()) {
			success = Book.objects.delete(timeTableId, bookId);
		}
		else {
			if(this.containsTimeTable(timeTableId)) {
				success = this.getTimeTables().get(timeTableId).removeBook(bookId);
			}
			else {
				success = false;
			}
		}
		if(success) {
			this.setModification("remove", "book", timeTableId);
		}
		return success;
	}

	/**
	 * Return the maximum identifier of the bookings of a given timetable.
	 * @param timeTableId the id of the timetable
	 * @return The maximum identifier of the bookings
	 */
	public int getBookingsMaxId(int timeTableId) {
		if(this.isSQL()) {
			int bookingsMaxId = -1;
			ResultSet books = Book.objects.get(timeTableId);
			try {
				while(books.next()) {
					int temp = books.getInt("BookingId");
					if(bookingsMaxId < temp) {
						bookingsMaxId = temp;
					}
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			return bookingsMaxId;
		}
		else {
			int bookingsMaxId;
			if(this.containsTimeTable(timeTableId)) {
				bookingsMaxId = this.getTimeTables().get(timeTableId).getBookingsMaxId();
			}
			else {
				bookingsMaxId = -1;
			}
			return bookingsMaxId;
		}
	}

	/**
	 * Return the maximum identifier of all the timetables.
	 * @return The maximum identifier
	 */
	public int getTimeTableMaxId() {
		if(this.isSQL()) {
			int timeTablesMaxId = -1;
			ResultSet timeTables = TimeTable.objects.all();
			try {
				while(timeTables.next()) {
					int temp = timeTables.getInt("BookingId");
					if(timeTablesMaxId < temp) {
						timeTablesMaxId = temp;
					}
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			return timeTablesMaxId;
		}
		else {
			int timeTablesMaxId;
			if(this.getTimeTables().isEmpty()) {
				timeTablesMaxId = -1;
			}
			else {
				timeTablesMaxId = Collections.max(this.getTimeTables().keySet());
			}
			return timeTablesMaxId;
		}
	}

}
