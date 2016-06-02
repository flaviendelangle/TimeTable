/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;


/**
 * Book of a room by a teacher.
 * 
 * @author Flavien DELANGLE and Marie PAYET
 * @version 06/2016
 */
public class Book {

	/**
	 * Identifier of this book.
	 */
	private int id = 0;

	/**
	 * Room in which this booking take place.
	 */
	private Room room = null;

	/**
	 * Login of the teacher who has done this booking.
	 */
	private String teacherLogin = "";

	/**
	 * Date at which the booking will begin.
	 */
	private Date dateBegin = new Date();

	/**
	 * Date at which the booking will end.
	 */
	private Date dateEnd = new Date();
	
	/**
	 * Simple ORM for the Book Object
	 */
	public static BookORM objects = new BookORM();	
	
	/**
	 * The constructor.
	 * @param id Id of the new book
	 * @param room Room of the new book
	 * @param teacherLogin Login of the teacher of the new book
	 * @param dateBegin Beginning date of the new book
	 * @param dateEnd Ending date of the new book
	 */
	public Book(int id, Room room, String teacherLogin, Date dateBegin, Date dateEnd) {
		this.setId(id);
		this.setRoom(room);
		this.setTeacherLogin(teacherLogin);
		this.setDateBegin(dateBegin);
		this.setDateEnd(dateEnd);
	}

	/**
	 * Returns the identifier of this book.
	 * @return Id of this book
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Update the identifier of this book
	 * @param newId Id of this book
	 */
	public void setId(int newId) {
		this.id = newId;
	}

	/**
	 * Returns the room in which this book will take place.
	 * @return Room in which this book will take place
	 */
	public Room getRoom() {
		return this.room;
	}

	/**
	 * Update the room in which this book will take place.
	 * @param newRoom Room in which this book will take place
	 */
	public void setRoom(Room newRoom) {
		this.room = newRoom;
	}

	/**
	 * Returns the login of the teacher who has made this book.
	 * @return Login of the teacher of this book
	 */
	public String getTeacherLogin() {
		return this.teacherLogin;
	}

	/**
	 * Update the login of the teacher who has made this book.
	 * @param newTeacherLogin Login of the teacher of this book
	 */
	public void setTeacherLogin(String newTeacherLogin) {
		this.teacherLogin = newTeacherLogin;
	}

	/**
	 * Returns the date at which this booking will begin.
	 * @return Beginning date of this book
	 */
	public Date getDateBegin() {
		return this.dateBegin;
	}

	/**
	 * Update the date at which this booking will begin. 
	 * @param newDateBegin Beginning date of this book
	 */
	public void setDateBegin(Date newDateBegin) {
		this.dateBegin = newDateBegin;
	}

	/**
	 * Returns the date at which this booking will end.
	 * @return Ending date of this book
	 */
	public Date getDateEnd() {
		return this.dateEnd;
	}

	/**
	 * Update the date at which this booking will end. 
	 * @param newDateEnd Ending date of this book
	 */
	public void setDateEnd(Date newDateEnd) {
		this.dateEnd = newDateEnd;
	}	

	/**
	 * Return the string representation of this book.
	 * @return Stringified version of this book
	 */
	public String toString() {
		String toString = "Booking n°" + this.getId() + "in room " + this.room.getId();
		return toString;
	}

	/**
	 * Return the XML representation of this book.
	 * @return XML representation of this book
	 */
	public Element toXML() {
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		Element bookXML = new Element("Book");
		Element roomId = new Element("RoomId");
		Element bookId = new Element("BookingId");
		Element dateBegin = new Element("DateBegin");
		Element dateEnd = new Element("DateEnd");
		Element teacherLogin = new Element("Login");
		
		roomId.setText(String.valueOf(this.room.getId()));
		bookId.setText(String.valueOf(this.getId()));
		dateBegin.setText(dateformat.format(this.getDateBegin()));
		dateEnd.setText(dateformat.format(this.getDateEnd()));
		teacherLogin.setText(this.teacherLogin);
		
		bookXML.addContent(roomId);
		bookXML.addContent(bookId);
		bookXML.addContent(dateBegin);
		bookXML.addContent(dateEnd);
		bookXML.addContent(teacherLogin);
		
		return bookXML;
	}

	/**
	 * Returns a SQL request to create this Book in the database
	 * @param timeTableId Id of the timeTable in which this book is
	 * @return Has the book successfully been created
	 */
	public Boolean toSQL(int timeTableId) {
		return Book.objects.create(timeTableId, this.getId(), this.getTeacherLogin(), 
				this.getDateBegin(), this.getDateEnd(), this.getRoom().getId());
	}

	/**
	 * Generate a MAP of Book objects from a XML representation
	 * @param bookListXML XML representation of the books
	 * @param books Map in which we want to store the books
	 * @param rooms Map in which we have stored the rooms
	 */
	public static void parseXML(Element bookListXML, Map<Integer, Book>books, Map<Integer, Room>rooms) {
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		List<Element> booksXML = bookListXML.getChildren("Book");
		Iterator<Element> itBooks = booksXML.iterator();
		
		while(itBooks.hasNext()) {
			Element book = (Element)itBooks.next();
			int bookId = Integer.parseInt(book.getChildText("BookingId"));
			int roomId = Integer.parseInt(book.getChildText("RoomId"));
			Room room = rooms.get(roomId);
			String teacherLogin = book.getChildText("Login");
			Date dateBegin = null;
			Date dateEnd = null;
			try {
				dateBegin = dateformat.parse(book.getChildText("DateBegin"));
				dateEnd = dateformat.parse(book.getChildText("DateEnd"));
			}
			catch(ParseException e) {
				
			}
			books.put(bookId, new Book(bookId, room, teacherLogin, dateBegin, dateEnd));
		}
	}

}
