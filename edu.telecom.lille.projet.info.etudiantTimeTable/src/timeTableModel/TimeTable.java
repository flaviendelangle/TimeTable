/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.util.Map;

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
	private Map<Integer,Room> rooms;
	
	
	// Start of user code (user defined attributes for TimeTable)
	
	// End of user code

	/**
	 * The constructor.
	 */
	public TimeTable() {
		// Start of user code constructor for TimeTable)
		super();
		// End of user code
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
	 * Return the XML representation of the timetable
	 * @return roomXML 
	 */
	public Element toXML() {
		Element timeTableXML = new Element("TimeTable");
		Element timeTableId = new Element("timeTableId");
		Element Rooms = new Element("Rooms");
		
		timeTableId.setText(String.valueOf(this.timeTableId));
		timeTableXML.addContent(timeTableId);
		
		for(Map.Entry<Integer, Room> entry : this.rooms.entrySet()) {
			Element roomId = new Element("roomId");
			roomId.setText(String.valueOf(entry.getKey()));
			Rooms.addContent(roomId);
		}
		timeTableXML.addContent(Rooms);
		
		return timeTableXML;
	}


}
