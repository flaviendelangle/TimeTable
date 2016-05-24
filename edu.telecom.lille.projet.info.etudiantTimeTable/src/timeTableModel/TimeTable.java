/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.sql.SQLException;
import java.sql.Statement;
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
public abstract class TimeTable {
	

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
	 */
	public TimeTable(Map<Integer,Book> books) {
		this.setBooks(books);
	}

	public TimeTable() {
		this.setBooks(new HashMap<Integer, Book>());
	}


	/**
	 * Returns the booking linked to this timetable.
	 * @return groupId 
	 */
	public Map<Integer,Book> getBooks() {
		return this.books;
	}

	/**
	 * Update the booking linked to this timetable. 
	 * @param newGroupId 
	 */
	public void setBooks(Map<Integer,Book> newBooks) {
		this.books = newBooks;
	}
	
	/**
	 * Returns the booking which have the correct identifier
	 * @param bookId
	 * @return booking
	 */
	public Book getBook(int bookId) {
		return this.getBooks().get(bookId);
	}

	/**
	 * Add a booking to this timetable.
	 * Check if the booking is possible by checking the dates of every other booking in this room 
	 * @param bookingId
	 * @param teacherLogin
	 * @param dateBegin
	 * @param dateEnd
	 * @param room
	 * @return success
	 */
	public Boolean addBooking(int bookingId, String teacherLogin, Date dateBegin, Date dateEnd, Room room) {
		for(Entry<Integer, Book> entry : this.getBooks().entrySet()) {
			Book book = entry.getValue();
			if(book.getRoom().getId() == room.getId()) {
				if(dateEnd.after(book.getDateBegin()) || dateBegin.before(book.getDateEnd())) {
					return false;
				}
			}
		}
		Book book = new Book(bookingId, room, teacherLogin, dateBegin, dateEnd);
		this.getBooks().put(bookingId, book);
		return true;	
	}

	/**
	 * Remove a booking from this timetable
	 * @param bookId
	 * @return success
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
	 * @return booksId
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
	 * @return bookID
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
	 * @param dateBegin
	 * @param dateEnd
	 */
	public void getBookingsDate(Hashtable<Integer, Date> dateBegin, Hashtable<Integer, Date> dateEnd) {
		for(Entry<Integer, Book> entry : this.getBooks().entrySet()) {
			dateBegin.put(entry.getKey(), entry.getValue().getDateBegin());
			dateEnd.put(entry.getKey(), entry.getValue().getDateEnd());
		}		
	}
	
	/**
	 * Return the string representation of this timetable.
	 * @return toString
	 */
	public String toString() {
		String toString = "TimeTable n°" + this.getId();
		return toString;
	}	
	
	/**
	 * Return the XML representation of the timetable
	 * @return roomXML 
	 */
	public Element toXML() {
		Element timeTableXML = new Element("TimeTable");
		Element timeTableId = new Element("GroupId");
		Element Books = new Element("Books");
		
		timeTableId.setText(String.valueOf(this.getId()));
		timeTableXML.addContent(timeTableId);
		
		for(Entry<Integer, Book> entry : this.getBooks().entrySet()) {
			Books.addContent(entry.getValue().toXML());
		}
		timeTableXML.addContent(Books);
		
		return timeTableXML;
	}

	/**
	 * Returns a SQL request to create this TimeTable in the database
	 * @return timeTableSQL
	 */
	public String toSQL() {
		String timeTableSQL = TimeTable.createSQL(this.getId());
		for(Entry<Integer, Book> entry : this.getBooks().entrySet()) {
			timeTableSQL += entry.getValue().toSQL(this.getId());
		}
		return timeTableSQL;
	}

	/**
	 * Return a SQL request to create a new TimeTable in the database
	 * @param timeTableId
	 * @return timeTableSQL
	 */
	public static String createSQL(int timeTableId) {
		String timeTableSQL = "INSERT INTO TimeTable (GroupId) VALUES(" + timeTableId + ");";
		return timeTableSQL;
	}
	

	/**
	 * Generate a MAP of TimeTable objects from a XML representation
	 * @param timeTableListXML
	 * @return timeTables
	 */
	public static void parseXML(Element timeTableListXML, Map<Integer, TimeTable>timeTables, Map<Integer, Room>rooms) {
		List<Element> timeTablesXML = timeTableListXML.getChildren("TimeTable");
		Iterator<Element> itTimeTable = timeTablesXML.iterator();
		
		while(itTimeTable.hasNext()) {
			Element timeTable = (Element)itTimeTable.next();
			int timeTableId = Integer.parseInt(timeTable.getChildText("GroupId"));
			Map<Integer,Book> books = new HashMap<Integer, Book>();
			Book.parseXML(timeTable.getChild("Books"), books, rooms);
			timeTables.put(timeTableId, new TimeTable(timeTableId, books));
		}
	}

}
