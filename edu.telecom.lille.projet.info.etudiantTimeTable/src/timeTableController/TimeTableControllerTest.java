package timeTableController;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import timeTableModel.Room;
import timeTableModel.TimeTable;
import timeTableModel.TimeTableBuilder;
/**
 * Unit testing of the program
 * 
 * @author Flavien DELANGLE and Marie PAYET
 * @version 06/2016
 * 
 */

public class TimeTableControllerTest {

	private TimeTableController controller;
	private SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private int timeTableId;
	private int roomId;
	private String login = "user";
	
	private int defaultTimeTableId = 2;
	private int defaultRoomId = 4;
	
	@Before
	public void setUp() throws Exception {
		this.setUpDB();
		this.controller = new TimeTableController("timeTableDB.xml");
		this.timeTableId = this.defaultTimeTableId;
		this.roomId = this.defaultRoomId;
	}
	
	public void setUpDB() {
    	FileInputStream instream = null;
    	FileOutputStream outstream = null;
    	try{
    	    File infile = new File("timeTableDBTest.xml");
    	    File outfile = new File("timeTableDB.xml");
    	    instream = new FileInputStream(infile);
    	    outstream = new FileOutputStream(outfile);
    	    byte[] buffer = new byte[1024];
    	    int length;
    	    while ((length = instream.read(buffer)) > 0){
    	    	outstream.write(buffer, 0, length);
    	    }
    	    instream.close();
    	    outstream.close();
    	}
    	catch(IOException ioe){
    		ioe.printStackTrace();
    	}		
	}

	@After
	public void tearDown() throws Exception {
		this.controller.tTDB.saveDB();
	}
	
	@Test
	public void testLoadDBRoom() {
		assertEquals(this.controller.tTDB.getRoomsSize(), 3);
		assertTrue(this.controller.tTDB.containsRoom(1));
		assertTrue(this.controller.tTDB.checkRoom(1, 1, 50));
	}
	
	@Test
	public void testLoadDBTimeTable() throws ParseException {
		assertEquals(this.controller.tTDB.getTimeTablesSize(), 1);
		assertTrue(this.controller.tTDB.containsTimeTable(1));
		assertEquals(this.controller.tTDB.getBooksSize(1), 4);
		
		Date dateBegin = this.dateformat.parse("07/04/2016 08:15:00");
		Date dateEnd = this.dateformat.parse("07/04/2016 11:45:00");
		String login = "GS";
		assertTrue(this.controller.tTDB.containsBook(1, 2, login, dateBegin, dateEnd, 2));
	}
	
	@Test
	public void testGetTeacherLogin() throws ParseException {
		this.testAddBooking();
		int bookId = this.controller.getBookingsMaxId(this.timeTableId);
		String login = this.controller.getTeacherLogin(this.timeTableId, bookId);
		assertEquals(login, this.login);
	}
	
	@Test
	public void testRoomsIdToString() {
		this.testAddRoom();
		String[] ids = this.controller.roomsIdToString();
		assertEquals(ids.length, 4);
		assertEquals(ids[3], "4");
	}
	
	@Test
	public void testRoomsToString() {
		this.testAddRoom();
		String[] rooms = this.controller.roomsToString();
		assertEquals(rooms.length, 4);
		assertEquals(rooms[3], "Room n°4 (50 student)");
	}
	
	@Test
	public void testTimeTablesIDToString() {
		this.testAddTimeTable();
		String[] timeTables = this.controller.timeTablesIDToString();
		assertEquals(timeTables.length, 2);
		assertEquals(timeTables[1], "2");
	}
	
	@Test
	public void testBooksIdToString() {
		String[] books = this.controller.booksIdToString(1);
		assertEquals(books.length, 4);
		assertEquals(books[1], "1");
	}
	
	@Test
	public void testAddRoom() {
		this.controller.addRoom(this.roomId, 50);
		assertEquals(this.controller.tTDB.getRoomsSize(), 4);
		assertTrue(this.controller.tTDB.checkRoom(this.roomId, this.roomId, 50));
	}
	
	@Test
	public void testRemoveRoom() {
		this.testAddRoom();
		Boolean success = this.controller.removeRoom(this.roomId);
		assertTrue(success);
		assertEquals(this.controller.tTDB.getRooms().size(), 3);
	}
	
	@Test
	public void testGetRoom() {
		int room = this.controller.getRoom(1, 1);
		assertEquals(room, 1);
	}
	
	@Test
	public void testAddTimeTable() {
		this.controller.addTimeTable(this.timeTableId);
		assertEquals(this.controller.tTDB.getTimeTablesSize(), 2);
		assertTrue(this.controller.tTDB.containsTimeTable(2));
	}

	@Test
	public void testRemoveTimeTable() {
		this.testAddTimeTable();
		Boolean success = this.controller.removeTimeTable(this.timeTableId);
		assertTrue(success);
		assertEquals(this.controller.tTDB.getTimeTables().size(), 1);
	}

	@Test
	public void testAddBooking() throws ParseException {
		this.testAddRoom();
		this.testAddTimeTable();
		
		int bookingId = this.controller.getBookingsMaxId(2) + 1;
		Date dateBegin = this.dateformat.parse("08/04/2016 16:00:00");
		Date dateEnd = this.dateformat.parse("08/04/2016 18:00:00");
		this.controller.addBooking(this.timeTableId, bookingId, this.login, dateBegin, dateEnd, this.roomId);
		assertEquals(this.controller.tTDB.getBooksSize(this.timeTableId), 1);
		assertTrue(this.controller.tTDB.containsBook(this.timeTableId, bookingId, this.login, dateBegin, dateEnd, this.roomId));
	}
	
	@Test
	public void testGetBookingsDate() {
		int timeTableId = 1;
		Hashtable<Integer, Date> dateBegin = new Hashtable<Integer, Date>();
		Hashtable<Integer, Date> dateEnd = new Hashtable<Integer, Date>();
		this.controller.getBookingsDate(timeTableId, dateBegin, dateEnd);
		assertEquals(dateBegin.size(), 4);
		assertEquals(dateEnd.size(), 4);
		assertEquals(this.dateformat.format(dateBegin.get(1)), "06/04/2016 13:00:00");
		assertEquals(this.dateformat.format(dateEnd.get(1)), "06/04/2016 17:00:00");
	}
	
	@Test
	public void testRemoveBook() {
		int timeTableId = 1;
		int bookId = 1;
		this.controller.removeBook(timeTableId, bookId);
		assertEquals(this.controller.tTDB.getBooksSize(timeTableId), 3);
	}
	
	@Test
	public void testGetBookingsMaxId() {
		int timeTableId = 1;
		int id = this.controller.getBookingsMaxId(timeTableId);
		assertEquals(id, 3);
	}
	
	@Test
	public void testSaveDBWithConflict() throws ParseException {
		if(!this.controller.tTDB.isSQL()) {
			// 1) We create a booking
			System.out.println("1) We create a booking");
			int timeTableId = 1;
			TimeTable timeTable = this.controller.tTDB.getTimeTables().get(timeTableId);
			Room room = this.controller.tTDB.getRooms().get(1);
			int bookId = this.controller.tTDB.getBookingsMaxId(timeTableId);
			Date dateBegin = this.dateformat.parse("08/04/2016 16:00:00");
			Date dateEnd = this.dateformat.parse("08/04/2016 18:00:00");
			timeTable.addBooking(bookId, "user", dateBegin, dateEnd, room);
			this.controller.tTDB.setModification("add", "book", timeTableId, false);
			
			// 2) We save the XML database
			System.out.println("2) We save the XML database");
			this.controller.tTDB.saveXML();
			
			// 3) We remove the last booking
			System.out.println("3) We remove the last booking");
			timeTable.removeBook(bookId);
			
			// 4) We add a new booking not compatible with the one we juste removed
			System.out.println("4) We add a new booking not compatible with the one we juste removed");
			timeTable.addBooking(bookId, "MS", dateBegin, dateEnd, room);
			this.controller.tTDB.setModification("add", "book", timeTableId, false);
			
			// 5) We try to save the XML database
			System.out.println("5) We try to save the XML database");
		}
	}
	
	@Test
	public void testBuildTimeTable() throws ParseException {
		Date dateBegin = this.dateformat.parse("02/05/2016 08:28:35");
		Date dateEnd = this.dateformat.parse("06/05/2016 18:00:00");
		TimeTableBuilder builder = new TimeTableBuilder("lessonsDB.xml", dateBegin, dateEnd);
		builder.create();
	}
}
