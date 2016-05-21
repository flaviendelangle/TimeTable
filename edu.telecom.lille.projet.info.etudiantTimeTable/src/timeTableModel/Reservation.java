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
 * Description of Reservation.
 * 
 * @author Flavien DELANGLE and Marie PAYET
 */
public class Reservation {

	/**
	 * Number of the reservation.
	 */
	private int id = 0;

	/**
	 * Room in which the reservation take place.
	 */
	private Room room = null;

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
	public Reservation(int id, Room room, String teacherLogin, Date dateBegin, Date dateEnd) {
		this.setId(id);
		this.setRoom(room);
		this.setTeacherLogin(teacherLogin);
		this.setDateBegin(dateBegin);
		this.setDateEnd(dateEnd);
	}

	/**
	 * Returns the identifier of this reservation.
	 * @return id 
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Update the identifier of this reservation
	 * @param newId 
	 */
	public void setId(int newId) {
		this.id = newId;
	}

	/**
	 * Returns the room in which the reservation will take place.
	 * @return rooms 
	 */
	public Room getRoom() {
		return this.room;
	}

	/**
	 * Update the room in which the reservation will take place.
	 * @param newRooms 
	 */
	public void setRoom(Room newRoom) {
		this.room = newRoom;
	}

	/**
	 * Returns the login of the teacher who has made the reservation.
	 * @return teacherLogin 
	 */
	public String getTeacherLogin() {
		return this.teacherLogin;
	}

	/**
	 * Update the login of the teacher who has made the reservation
	 * @param newTeacherLogin 
	 */
	public void setTeacherLogin(String newTeacherLogin) {
		this.teacherLogin = newTeacherLogin;
	}

	/**
	 * Returns the date at which the reservation will begin.
	 * @return dateBegin 
	 */
	public Date getDateBegin() {
		return this.dateBegin;
	}

	/**
	 * Update the date at which the reservation will begin. 
	 * @param newDateBegin 
	 */
	public void setDateBegin(Date newDateBegin) {
		this.dateBegin = newDateBegin;
	}

	/**
	 * Returns the date at which the reservation will end.
	 * @return dateEnd 
	 */
	public Date getDateEnd() {
		return this.dateEnd;
	}

	/**
	 * Update the date at which the reservation will end. 
	 * @param newDateEnd 
	 */
	public void setDateEnd(Date newDateEnd) {
		this.dateEnd = newDateEnd;
	}	

	/**
	 * Return the string representation of this reservation.
	 * @return toString
	 */
	public String toString() {
		String toString = "Reservation n°" + this.getId() + "in room " + this.room.getId();
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
		
		roomId.setText(String.valueOf(this.room.getId()));
		bookId.setText(String.valueOf(this.getId()));
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
