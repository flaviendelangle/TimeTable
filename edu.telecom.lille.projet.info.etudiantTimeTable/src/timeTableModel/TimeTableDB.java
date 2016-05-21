/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
	 * List of all the timetables
	 */
	private Map<Integer, TimeTable> timeTables;

	/**
	 * List of all the rooms
	 */
	private Map<Integer, Room> rooms;

	/**
	 * Description of the property file.
	 */
	private String file = "TimeTables.xml";


	// Start of user code (user defined attributes for TimeTableDB)

	// End of user code

	/**
	 * The constructor.
	 */
	public TimeTableDB() {
		super();
	}

	/**
	 * Description of the method saveDB.
	 */
	public void saveDB() {
		Element rootXML = new Element("DataBase");
		Element rooms = new Element("Rooms");
		Element timeTables = new Element("TimeTables");
		
		for(Map.Entry<Integer, Room> entry : this.rooms.entrySet()) {
			rooms.addContent(entry.getValue().toXML());
		}		
		for(Map.Entry<Integer, TimeTable> entry : this.timeTables.entrySet()) {
			timeTables.addContent(entry.getValue().toXML());
		}
		
		org.jdom2.Document document = new Document(rootXML);
		try {
			XMLOutputter xml = new XMLOutputter(Format.getPrettyFormat());
			xml.output(document, new FileOutputStream(this.file));
		}
		catch(java.io.IOException e) {
			
		}
	}

	/**
	 * Description of the method loadDB.
	 */
	public void loadDB() {
		org.jdom2.Document document = null;
		Element rootXML;
		SAXBuilder sxb = new SAXBuilder();
		try {
			document = sxb.build(new File(this.file));
		}
		catch(Exception e) {
			
		}
		if(document != null) {
			rootXML = document.getRootElement();
			
			this.rooms = Room.parseXML(rootXML.getChild("Rooms"));
			this.timeTables = TimeTable.parseXML(rootXML.getChild("TimeTables"), this.rooms);
		}
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
	 * @return String array (content : ID of the rooms)
	 */
	public String[] roomsIdToString() {
		Set<Integer> roomsIdToString;
		
		roomsIdToString = rooms.keySet(); // les clés sont les ID : on les récupère dans une liste
		
		return roomsIdToString.toArray(new String[roomsIdToString.size()]); //on transforme la liste en tableau
	}

	/**
	 * Description of the method roomsToString.
	 * @return String array (content : ID of the rooms + capacity)
	 */
	public String[] roomsToString() {
		
		String[] result = new String[rooms.size()];
		int i = 0 ;
		
		for (Map.Entry<Integer, Room> entry : rooms.entrySet())
			{
			result[i] =  entry.getValue().toString();
			i++;
			}
		
		return result; 
	}

	/**
	 * Description of the method timeTablesIDToString.
	 * @return String array (content : ID of the timeTable)
	 */
	public String[] timeTablesIDToString() {
		Set<Integer> timeTableIdToString;
		
		timeTableIdToString = rooms.keySet(); 
		
		return timeTableIdToString.toArray(new String[timeTableIdToString.size()]); //on transforme la liste en tableau
	}

	/**
	 * Description of the method booksIdToString.
	 * @param timeTableId 
	 * @return String array (content : booksId)
	 */
	public String[] booksIdToString(Integer timeTableId) {
			TimeTable timeTableResult = timeTables.get(timeTableId); // récupère le timetable correspondant à l'ID recherché

		return timeTableResult.getBooksId(); // fonction à définir dans classe TimeTable

	}

	/**
	 * Description of the method addRoom.
	 * @param roomId (Integer)
	 * @param capacity (Integer)
	 * @return Boolean (true if the room is correctly added, false if the room already exist)
	 */
	public Boolean addRoom(Integer roomId, Integer capacity) {	
		Boolean result;
		result = !(rooms.containsKey(roomId));
		
		if(result){
			Room newRoom = new Room(roomId, capacity);
			rooms.put(roomId, newRoom);
		}
		
		return result;
	}

	/**
	 * Description of the method removeRoom.
	 * @param roomId (Integer)
	 * @return Boolean ( true if the room is correctly removed, false if the room does not exist)
	 */
	public Boolean removeRoom(Integer roomId) {
		Boolean result;
		result = rooms.containsKey(roomId);
		
		if(result){
			rooms.remove(roomId);
		}
		
		return result;
	}

	/**
	 * Description of the method getRoom.
	 * @param timeTableId (Integer)
	 * @param bookId (Integer)
	 * @return Id of the Room we search (Integer)
	 */
	public Integer getRoom(Integer timeTableId, Integer bookId) {
		TimeTable timeTableResult = timeTables.get(timeTableId); // récupère le timetable correspondant à l'ID recherché

		return timeTableResult.getBook(bookId).getRoom().getId();
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

}
