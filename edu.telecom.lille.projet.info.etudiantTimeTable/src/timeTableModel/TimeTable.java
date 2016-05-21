/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Element;

// Start of user code (user defined imports)

// End of user code

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
	 * Map interface containing all the rooms related to the timetable.
	 */
	private Map<Integer,Reservation> reservations;
	
	
	// Start of user code (user defined attributes for TimeTable)
	
	// End of user code

	/**
	 * The constructor.
	 */
	public TimeTable(int timeTableId, Map<Integer,Reservation> reservations) {
		this.setId(timeTableId);
		this.setReservations(reservations);
	}

	/**
	 * Returns the identifier of this timetable.
	 * @return id 
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Update the identifier of this timetable.
	 * @param newId 
	 */
	public void setId(int newId) {
		this.id = newId;
	}

	/**
	 * Returns the reservations linked to this timetable.
	 * @return groupId 
	 */
	public Map<Integer,Reservation> getReservations() {
		return this.reservations;
	}

	/**
	 * Update the reservations linked to this timetable. 
	 * @param newGroupId 
	 */
	public void setReservations(Map<Integer,Reservation> newReservations) {
		this.reservations = newReservations;
	}
	
	/**
	 * Returns the reservation which have the correct identifier
	 * @param bookId
	 * @return reservation
	 */
	public Reservation getBook(int bookId) {
		return this.getReservations().get(bookId);
	}
	
	public Boolean addBooking(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd,
			Integer roomId) {
				return null;
	
	}

	/**
	 * Remove a reservation from this timetable
	 * @param bookId
	 * @return success
	 */
	public Boolean removeBook(int bookId) {
		if(this.getReservations().containsKey(bookId)) {
			this.getReservations().remove(bookId);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Return a table of strings containing the identifier of all the reservations linked to this timetable
	 * @return booksId
	 */
	public String[] getBookingsId() {
		int length = this.getReservations().size();
		int i = 0;
		String[] booksId = new String[length];
		for(Entry<Integer, Reservation> entry : this.getReservations().entrySet()) {
			booksId[i] = String.valueOf(entry.getKey());
			i++;
		}
		return booksId;
	}

	/**
	 * Return the maximum identifier of the reservations of this timetable
	 * @return bookID
	 */
	public int getBookingsMaxId() {
		int bookId = Collections.max(this.getReservations().keySet());
		return bookId;
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
		Element timeTableId = new Element("timeTableId");
		Element Reservations = new Element("Reservations");
		
		timeTableId.setText(String.valueOf(this.getId()));
		timeTableXML.addContent(timeTableId);
		
		for(Entry<Integer, Reservation> entry : this.getReservations().entrySet()) {
			Reservations.addContent(entry.getValue().toXML());
		}
		timeTableXML.addContent(Reservations);
		
		return timeTableXML;
	}

	/**
	 * Generate a MAP of TimeTable objects from a XML representation
	 * @param timeTableListXML
	 * @return timeTables
	 */
	public static Map<Integer, TimeTable> parseXML(Element timeTableListXML, Map<Integer, Room>rooms) {
		List<Element> timeTablesXML = timeTableListXML.getChildren("Room");
		Iterator<Element> itTimeTable = timeTablesXML.iterator();
		Map<Integer, TimeTable> timeTables = new HashMap<Integer, TimeTable>();
		
		while(itTimeTable.hasNext()) {
			Element timeTable = (Element)itTimeTable.next();
			int timeTableId = Integer.parseInt(timeTable.getChildText("roomId"));
			Map<Integer,Reservation> reservations = Reservation.parseXML(timeTable.getChild("Reservations"), rooms);
			timeTables.put(timeTableId, new TimeTable(timeTableId, reservations));
		}
		
		return timeTables;
	}

}
