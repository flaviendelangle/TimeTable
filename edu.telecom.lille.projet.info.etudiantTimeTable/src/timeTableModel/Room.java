/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

// Start of user code (user defined imports)

// End of user code

/**
 * Description of Room.
 * 
 * @author delangle
 */
public class Room {
	/**
	 * Description of the property roomId.
	 */
	public int roomId = 0;

	/**
	 * Description of the property capacity.
	 */
	public int capacity = 0;

	// Start of user code (user defined attributes for Room)

	// End of user code

	/**
	 * The constructor.
	 */
	public Room(int roomId, int capacity) {
		this.roomId = roomId;
		this.capacity = capacity;
	}

	/**
	 * Description of the method toString.
	 * @return 
	 */
	public String toString() {
		// Start of user code for method toString
		String toString = "";
		return toString;
		// End of user code
	}

	// Start of user code (user defined methods for Room)

	// End of user code
	/**
	 * Returns roomId.
	 * @return roomId 
	 */
	public int getRoomId() {
		return this.roomId;
	}

	/**
	 * Sets a value to attribute roomId. 
	 * @param newRoomId 
	 */
	public void setRoomId(int newRoomId) {
		this.roomId = newRoomId;
	}

	/**
	 * Returns capacity.
	 * @return capacity 
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * Sets a value to attribute capacity. 
	 * @param newCapacity 
	 */
	public void setCapacity(int newCapacity) {
		this.capacity = newCapacity;
	}

	/**
	 * Return the XML representation of the room
	 * @return roomXML 
	 */
	public Element toXML() {
		Element roomXML = new Element("Room");
		Element roomId = new Element("roomId");
		Element capacity = new Element("capacity");
		
		roomId.setText(String.valueOf(this.roomId));
		capacity.setText(String.valueOf(this.capacity));
		
		roomXML.addContent(roomId);
		roomXML.addContent(capacity);
		
		return roomXML;
	}
	
	/**
	 * Generate a MAP of Room objects from a XML representation
	 * @param roomListXML
	 * @return rooms
	 */
	public static Map<Integer, Room> parseXML(Element roomListXML) {
		List<Element> roomsXML = roomListXML.getChildren("Room");
		Iterator<Element> itRooms = roomsXML.iterator();
		Map<Integer, Room> rooms = new HashMap<Integer, Room>();
		
		while(itRooms.hasNext()) {
			Element room = (Element)itRooms.next();
			int roomId = Integer.parseInt(room.getChild("roomId").getText());
			int capacity = Integer.parseInt(room.getChild("capacity").getText());
			rooms.put(roomId, new Room(roomId, capacity));
		}
		
		return rooms;
	}

}
