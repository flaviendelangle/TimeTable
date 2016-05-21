/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
// Start of user code (user defined imports)
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

// End of user code

/**
 * Description of Reservation.
 * 
 * @author Flavien DELANGLE and Marie PAYET
 */
public class Reservation {

	/**
	 * Description of the property dateEnd.
	 */
	private Room room = null;

	/**
	 * Number of the reservation.
	 */
	private int bookId = 0;

	/**
	 * Login of the teacher who has done the reservation.
	 */
	private String teacherLogin = "";

	/**
	 * Date at which the reservation will begin.
	 */
	private Date dateBegin = new Date();

	/**
	 * Date at which the reservation will end.
	 */
	private Date dateEnd = new Date();
	
	/**
	 * The constructor.
	 */
	public Reservation(int bookId, Room room, String teacherLogin, Date dateBegin, Date dateEnd) {
		this.bookId = bookId;
		this.room = room;
		this.teacherLogin = teacherLogin;
		this.dateBegin = dateBegin;
		this.dateEnd = dateEnd;
	}

	// Start of user code (user defined methods for Reservation)

	// End of user code
	/**
	 * Returns dateEnd.
	 * @return dateEnd 
	 */
	public Date getDateEnd() {
		return this.dateEnd;
	}

	/**
	 * Sets a value to attribute dateEnd. 
	 * @param newDateEnd 
	 */
	public void setDateEnd(Date newDateEnd) {
		this.dateEnd = newDateEnd;
	}

	/**
	 * Returns bookId.
	 * @return bookId 
	 */
	public int getBookId() {
		return this.bookId;
	}

	/**
	 * Sets a value to attribute bookId. 
	 * @param newBookId 
	 */
	public void setBookId(int newBookId) {
		this.bookId = newBookId;
	}

	/**
	 * Returns teacherLogin.
	 * @return teacherLogin 
	 */
	public String getTeacherLogin() {
		return this.teacherLogin;
	}

	/**
	 * Sets a value to attribute teacherLogin. 
	 * @param newTeacherLogin 
	 */
	public void setTeacherLogin(String newTeacherLogin) {
		this.teacherLogin = newTeacherLogin;
	}

	/**
	 * Returns dateBegin.
	 * @return dateBegin 
	 */
	public Date getDateBegin() {
		return this.dateBegin;
	}

	/**
	 * Sets a value to attribute dateBegin. 
	 * @param newDateBegin 
	 */
	public void setDateBegin(Date newDateBegin) {
		this.dateBegin = newDateBegin;
	}

	/**
	 * Returns rooms.
	 * @return rooms 
	 */
	public Room getRoom() {
		return this.room;
	}

	/**
	 * Sets a value to attribute rooms. 
	 * @param newRooms 
	 */
	public void setRoom(Room newRoom) {
		this.room = newRoom;
	}

	/**
	 * Stringified version of the Reservation object.
	 * @return toString
	 */
	public String toString() {
		String toString = "Reservation n°" + this.getBookId() + "in room " + this.room.getRoomId();
		return toString;
	}

	/**
	 * Return the XML representation of the reservation
	 * @return roomXML 
	 */
	public Element toXML() {
		SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		Element reservationXML = new Element("Reservation");
		Element roomId = new Element("roomId");
		Element bookId = new Element("bookId");
		Element dateBegin = new Element("dateBegin");
		Element dateEnd = new Element("dateEnd");
		Element teacherLogin = new Element("teacherLogin");
		
		roomId.setText(String.valueOf(this.room.getRoomId()));
		bookId.setText(String.valueOf(this.bookId));
		dateBegin.setText(dateformat.format(this.dateBegin));
		dateEnd.setText(dateformat.format(this.dateEnd));
		teacherLogin.setText(this.teacherLogin);
		
		reservationXML.addContent(roomId);
		reservationXML.addContent(bookId);
		reservationXML.addContent(dateBegin);
		reservationXML.addContent(dateEnd);
		reservationXML.addContent(teacherLogin);
		
		return reservationXML;
	}

	/**
	 * Generate a MAP of Reservations objects from a XML representation
	 * @param reservationListXML
	 * @return rooms
	 */
	public static Map<Integer, Reservation> parseXML(Element reservationListXML, Map<Integer, Room>rooms) {
		SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		List<Element> reservationsXML = reservationListXML.getChildren("Room");
		Iterator<Element> itReservations = reservationsXML.iterator();
		Map<Integer, Reservation> reservations = new HashMap<Integer, Reservation>();
		
		while(itReservations.hasNext()) {
			Element reservation = (Element)itReservations.next();
			int bookId = Integer.parseInt(reservation.getChildText("bookId"));
			int roomId = Integer.parseInt(reservation.getChildText("roomId"));
			Room room = rooms.get(roomId);
			String teacherLogin = reservation.getChildText("teacherLogin");
			Date dateBegin = null;
			Date dateEnd = null;
			try {
				dateBegin = dateformat.parse(reservation.getChildText("dateBegin"));
				dateEnd = dateformat.parse(reservation.getChildText("dateEnd"));
			}
			catch(ParseException e) {
				
			}
			reservations.put(bookId, new Reservation(bookId, room, teacherLogin, dateBegin, dateEnd));
		}
		
		return reservations;
	}

}
