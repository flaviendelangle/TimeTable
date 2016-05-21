/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

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
 * @author delangle
 */
public class TimeTable {
	/**
	 * Id of the current timetable
	 */
	private int timeTableId = 0;

	/**
	 * Map interface containing all the rooms related to the timetable
	 */
	private Map<Integer,Reservation> reservations;
	
	
	// Start of user code (user defined attributes for TimeTable)
	
	// End of user code

	/**
	 * The constructor.
	 */
	public TimeTable(int timeTableId, Map<Integer,Reservation> reservations) {
		this.timeTableId = timeTableId;
		this.reservations = reservations;
	}

	// Start of user code (user defined methods for TimeTable)

	// End of user code
	/**
	 * Returns groupId.
	 * @return groupId 
	 */
	public Object getTimeTableId() {
		return this.timeTableId;
	}

	/**
	 * Sets a value to attribute groupId. 
	 * @param newGroupId 
	 */
	public void setTimeTableId(int newTimeTableId) {
		this.timeTableId = newTimeTableId;
	}

	/**
	 * Stringified version of the TimeTable object.
	 * @return toString
	 */
	public String toString() {
		String toString = "TimeTable n°" + this.getTimeTableId();
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
		
		timeTableId.setText(String.valueOf(this.timeTableId));
		timeTableXML.addContent(timeTableId);
		
		for(Entry<Integer, Reservation> entry : this.reservations.entrySet()) {
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
