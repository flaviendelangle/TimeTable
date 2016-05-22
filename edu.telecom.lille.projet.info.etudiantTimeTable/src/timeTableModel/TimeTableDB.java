/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
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
	 * List of all the timetables
	 */
	private Map<Integer, TimeTable> timeTables;

	/**
	 * List of all the rooms
	 */
	private Map<Integer, Room> rooms;

	/**
	 * File where the program save all the data.
	 */
	private String file = "timeTableDB.xml";

	/**
	 * The constructor.
	 */
	public TimeTableDB(String file) {
		this.setFile(file);
		this.loadDB();
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
	 * Sets a value to attribute file. 
	 * @param newFile 
	 */
	public void setFile(String newFile) {
		this.file = newFile;
	}
	
	/**
	 * Returns file.
	 * @return file 
	 */
	public String getFile() {
		return this.file;
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
		Element rootXML = new Element("TimeTablesDB");
		Element rooms = new Element("Rooms");
		Element timeTables = new Element("TimeTables");
		
		for(Map.Entry<Integer, Room> entry : this.rooms.entrySet()) {
			rooms.addContent(entry.getValue().toXML());
		}		
		for(Map.Entry<Integer, TimeTable> entry : this.timeTables.entrySet()) {
			timeTables.addContent(entry.getValue().toXML());
		}
		
		org.jdom2.Document document = new Document(rootXML);
		Boolean success;
		try {
			XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
			xml.output(document, new FileOutputStream(this.getFile()));
			success = true;
		}
		catch(java.io.IOException e) {
			success = false;
		}
		return success;
	}

	/**
	 * Load a XML file as the current database of the program
	 */
	public boolean loadDB() {
		org.jdom2.Document document = null;
		Element rootXML;
		SAXBuilder sxb = new SAXBuilder();
		Boolean success = false;;
		try {
			document = sxb.build(new File(this.getFile()));
		}
		catch(Exception e) {
		}
		if(document != null) {
			rootXML = document.getRootElement();
			try {
				success = true;
				this.rooms = Room.parseXML(rootXML.getChild("Rooms"));
				this.timeTables = TimeTable.parseXML(rootXML.getChild("TimeTables"), this.rooms);
			}
			catch(Exception e) {
			}
		}
		return success;
	}

	/**
	 * Returns the login of the teacher of a given booking.
	 * @param timeTableId 
	 * @param bookId 
	 * @return teacherLogin
	 */
	public String getTeacherLogin(int timeTableId, int bookId) {
		String teacherLogin = "";
		if(this.getTimeTables().containsKey(timeTableId)) {
			Book booking = this.getTimeTables().get(timeTableId).getBook(bookId);
			if(booking != null) {
				teacherLogin = booking.getTeacherLogin();
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
	 */
	public Boolean addRoom(Integer roomId, Integer capacity) {	
		Boolean exist = (rooms.containsKey(roomId));
		
		if(!exist){
			Room newRoom = new Room(roomId, capacity);
			rooms.put(roomId, newRoom);
		}
		
		return !exist;
	}

	/**
	 * Description of the method removeRoom.
	 * We check if the room we want to remove exists or not.
	 * If the room exists we remove it from the map.
	 * @param roomId (Integer)
	 * @return Boolean ( true if the room is correctly removed, false if the room does not exist)
	 */
	public Boolean removeRoom(Integer roomId) {
		Boolean result = this.rooms.containsKey(roomId);
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
		TimeTable timeTableResult = timeTables.get(timeTableId); // récupère le timetable correspondant à l'ID recherché

		if(timeTableResult == null)return -1;
		else {return timeTableResult.getBook(bookId).getRoom().getId();}
	}

	/**
	 * Description of the method addTimeTable.
	 * @param timeTableId 
	 * @return 
	 */
	public Boolean addTimeTable(int timeTableId) {
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
