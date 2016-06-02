/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Element;


/**
 * Description of TimeTable.
 * 
 * @author Flavien DELANGLE and Marie PAYET
 */
public class TimeTable {
	
	/**
	 * Id of the current timetable
	 */
	private int id = 0;
	
	/**
	 * Type of this TimeTable (teacher or group)
	 */
	private String type = "";

	/**
	 * Id of the group of the current timetable
	 */
	private int groupId = 0;

	/**
	 * Login of the teacher
	 */
	private String login ;
	
	/**
	 * Map interface containing all the books related to the timetable.
	 */
	private Map<Integer,Book> books;
	
	/**
	 * Simple ORM for the TimeTable Object
	 */
	public static TimeTableORM objects = new TimeTableORM();
	
	/**
	 * The constructor.
	 * @param groupId the ID of the group linked to this TimeTable
	 * @param books all the books already linked to this group
	 */
	public TimeTable(int groupId, Map<Integer,Book> books) {
		this.setId(groupId);
		this.setGroupId(groupId);
		this.setBooks(books);
	}

	/**
	 * The constructor.
	 * @param groupId the ID of the group linked to this TimeTable
	 */
	public TimeTable(int groupId) {
		this(groupId, new HashMap<Integer, Book>());
	}

	/**
	 * The constructor.
	 * @param timeTableId the ID of the timeTable we want to create
	 * @param login the login of the teacher who wants to create a timetable
	 * @param books all the books already linked to this group
	 */
	public TimeTable(int timeTableId, String login, Map<Integer,Book> books) {
		this.setId(timeTableId);
		this.setLogin(login);
		this.setBooks(books);
	}

	/**
	 * The constructor.
	 * @param timeTableId the ID of the timeTable we want to create
	 * @param login the login of the teacher who wants to create a timetable 
	 */
	public TimeTable(int timeTableId, String login) {
		this(timeTableId, login, new HashMap<Integer, Book>());
	}

	/**
	 * Returns the identifier of this timetable.
	 * @return id  the id of this timetable
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Update the identifier of this timetable.
	 * @param newId the id of this timetable
	 */
	public void setId(int newId) {
		this.id = newId;
	}

	/**
	 * Returns the type of this timetable.
	 * @return type the type of this timetable
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Update the type of this timetable.
	 * @param newType the type of this timetable
	 */
	public void setType(String newType) {
		this.type = newType;
	}

	/**
	 * Returns the identifier of the group of this timetable.
	 * @return groupId the ID of the group of this timetable
	 */
	public int getGroupId() {
		return this.groupId;
	}

	/**
	 * Update the identifier of the group of this timetable.
	 * @param newGroupId the ID of the group of this timetable
	 */
	public void setGroupId(int newGroupId) {
		this.groupId = newGroupId;
	}

	/**
	 * Returns the login of the teacher of this timetable.
	 * @return login the login of the teacher of this timetable
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * Update the login of the teacher of this timetable.
	 * @param newLogin the login of the teacher of this timetable 
	 */
	public void setLogin(String newLogin) {
		this.login = newLogin;
	}
	
	/**
	 * Returns the books linked to this timetable.
	 * @return books the books linked to this timetable 
	 */
	public Map<Integer,Book> getBooks() {
		return this.books;
	}

	/**
	 * Update the books linked to this timetable. 
	 * @param newBooks the books linked to this timetable  
	 */
	public void setBooks(Map<Integer,Book> newBooks) {
		this.books = newBooks;
	}
	
	/**
	 * Returns the booking which have the correct identifier
	 * @param bookId the id of the book we want to retrieve
	 * @return book the book with the correct id
	 */
	public Book getBook(int bookId) {
		return this.getBooks().get(bookId);
	}

	/**
	 * Add a booking to this timetable.
	 * @param bookingId the id of the new book
	 * @param teacherLogin the login of the teacher of the new book
	 * @param dateBegin the beginning date of the new book
	 * @param dateEnd the ending date of the new book
	 * @param room the room of the new book
	 * @return success has the book been successfully created
	 */
	public void addBooking(int bookingId, String teacherLogin, Date dateBegin, Date dateEnd, Room room) {
		Book book = new Book(bookingId, room, teacherLogin, dateBegin, dateEnd);
		this.getBooks().put(bookingId, book);
	}

	/**
	 * Remove a booking from this timetable
	 * @param bookId the id of the book we want to remove
	 * @return success has the book been removed
	 */
	public Boolean removeBook(int bookId) {
		if(this.getBooks().containsKey(bookId)) {
			this.getBooks().remove(bookId);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Return a table of strings containing the identifier of all the bookings linked to this timetable
	 * @return booksId array with all the ids of the books of this timetable
	 */
	public String[] getBookingsId() {
		int length = this.getBooks().size();
		int i = 0;
		String[] booksId = new String[length];
		for(Entry<Integer, Book> entry : this.getBooks().entrySet()) {
			booksId[i] = String.valueOf(entry.getKey());
			i++;
		}
		return booksId;
	}

	/**
	 * Return the maximum identifier of the bookings of this timetable
	 * @return bookID maximum id of all the books of this timetable
	 */
	public int getBookingsMaxId() {
		int bookId;
		if(this.getBooks().isEmpty()) {
			bookId = -1;
		}
		else {
			bookId = Collections.max(this.getBooks().keySet());
		}
		return bookId;
	}
	
	/**
	 * Create two hash tables that contains all the beginning dates and ending dates of the bookings of this timetable
	 * @param dateBegin list of all the beginning date of this timetable
	 * @param dateEnd list of all the ending date of this timetable
	 */
	public void getBookingsDate(Hashtable<Integer, Date> dateBegin, Hashtable<Integer, Date> dateEnd) {
		for(Entry<Integer, Book> entry : this.getBooks().entrySet()) {
			dateBegin.put(entry.getKey(), entry.getValue().getDateBegin());
			dateEnd.put(entry.getKey(), entry.getValue().getDateEnd());
		}		
	}
	
	/**
	 * Return the string representation of this timetable.
	 * @return toString stringified version of this timetable
	 */
	public String toString() {
		String toString;
		if(this.getType() == "teacher") {
			toString = "TimeTable of the teacher " + this.getLogin();
		}
		else {
			toString = "TimeTable of the group n°" + this.getGroupId();
		}
		return toString;
	}	
	
	/**
	 * Return the XML representation of the timetable
	 * @return roomXML XML representation of this timetable
	 */
	public Element toXML() {
		Element timeTableXML = new Element("TimeTable");
		Element Books = new Element("Books");
		
		if(this.getType() == "teacher") {
			Element login = new Element("Login");
			login.setText(this.getLogin());
			timeTableXML.addContent(login);
		}
		else {
			Element timeTableId = new Element("GroupId");
			timeTableId.setText(String.valueOf(this.getId()));
			timeTableXML.addContent(timeTableId);			
		}
		
		for(Entry<Integer, Book> entry : this.getBooks().entrySet()) {
			Books.addContent(entry.getValue().toXML());
		}
		timeTableXML.addContent(Books);
		
		return timeTableXML;
	}

	/**
	 * Returns a SQL request to create this TimeTable in the database
	 * @return has the timetable successfully been created ?
	 */
	public Boolean toSQL() {
		Boolean success = TimeTable.objects.create(this.getId());
		for(Entry<Integer, Book> entry : this.getBooks().entrySet()) {
			success &= entry.getValue().toSQL(this.getId());
		}
		return success;
	}	

	/**
	 * Generate a MAP of TimeTable objects from a XML representation
	 * @param timeTableListXML XML representation of the timetables
	 * @param timeTables Map in which we want to store the timtables from the database
	 * @param rooms Map in which we have store the rooms
	 */
	public static void parseXML(Element timeTableListXML, Map<Integer, TimeTable>timeTables, Map<Integer, Room>rooms) {
		List<Element> timeTablesXML = timeTableListXML.getChildren("TimeTable");
		Iterator<Element> itTimeTable = timeTablesXML.iterator();
		
		while(itTimeTable.hasNext()) {
			Element timeTable = (Element)itTimeTable.next();
			Map<Integer,Book> books = new HashMap<Integer, Book>();
			Book.parseXML(timeTable.getChild("Books"), books, rooms);
			int groupId = Integer.parseInt(timeTable.getChildText("GroupId"));
			Element login = timeTable.getChild("Login");
			
			if(login == null) {
				timeTables.put(groupId, new TimeTable(groupId, books));
			}
			else {
				timeTables.put(groupId, new TimeTable(groupId, login.getText(), books));
			}			
		}
	}

}
