/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;


/**
 * Description of Book.
 * 
 * @author Flavien DELANGLE and Marie PAYET
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
	 * The constructor.
	 */
	public Book(int id, Room room, String teacherLogin, Date dateBegin, Date dateEnd) {
		this.setId(id);
		this.setRoom(room);
		this.setTeacherLogin(teacherLogin);
		this.setDateBegin(dateBegin);
		this.setDateEnd(dateEnd);
	}

	/**
	 * Returns the identifier of this booking.
	 * @return id 
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Update the identifier of this booking
	 * @param newId 
	 */
	public void setId(int newId) {
		this.id = newId;
	}

	/**
	 * Returns the room in which this booking will take place.
	 * @return rooms 
	 */
	public Room getRoom() {
		return this.room;
	}

	/**
	 * Update the room in which this booking will take place.
	 * @param newRooms 
	 */
	public void setRoom(Room newRoom) {
		this.room = newRoom;
	}

	/**
	 * Returns the login of the teacher who has made this booking.
	 * @return teacherLogin 
	 */
	public String getTeacherLogin() {
		return this.teacherLogin;
	}

	/**
	 * Update the login of the teacher who has made this booking
	 * @param newTeacherLogin 
	 */
	public void setTeacherLogin(String newTeacherLogin) {
		this.teacherLogin = newTeacherLogin;
	}

	/**
	 * Returns the date at which this booking will begin.
	 * @return dateBegin 
	 */
	public Date getDateBegin() {
		return this.dateBegin;
	}

	/**
	 * Update the date at which this booking will begin. 
	 * @param newDateBegin 
	 */
	public void setDateBegin(Date newDateBegin) {
		this.dateBegin = newDateBegin;
	}

	/**
	 * Returns the date at which this booking will end.
	 * @return dateEnd 
	 */
	public Date getDateEnd() {
		return this.dateEnd;
	}

	/**
	 * Update the date at which this booking will end. 
	 * @param newDateEnd 
	 */
	public void setDateEnd(Date newDateEnd) {
		this.dateEnd = newDateEnd;
	}	

	/**
	 * Return the string representation of this booking.
	 * @return toString
	 */
	public String toString() {
		String toString = "Booking n°" + this.getId() + "in room " + this.room.getId();
		return toString;
	}

	/**
	 * Return the XML representation of this booking
	 * @return roomXML 
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
	 * Generate a MAP of Book objects from a XML representation
	 * @param bookListXML
	 * @return rooms
	 */
	public static Map<Integer, Book> parseXML(Element bookListXML, Map<Integer, Room>rooms) {
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		List<Element> booksXML = bookListXML.getChildren("Book");
		Iterator<Element> itBooks = booksXML.iterator();
		Map<Integer, Book> books = new HashMap<Integer, Book>();
		
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
		
		return books;
	}

}
