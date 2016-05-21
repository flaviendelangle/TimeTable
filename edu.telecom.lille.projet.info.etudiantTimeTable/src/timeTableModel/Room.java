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
 * @author Flavien DELANGLE and Marie PAYET
 */
public class Room {
	
	/**
	 * Identifier of this room.
	 */
	private int id = 0;

	/**
	 * Capacity of this room.
	 */
	private int capacity = 0;

	/**
	 * The constructor.
	 */
	public Room(int id, int capacity) {
		this.setId(id);
		this.setCapacity(capacity);
	}

	/**
	 * Returns the identifier of this room.
	 * @return id 
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Update the identifier of this room. 
	 * @param newRoomId 
	 */
	public void setId(int newId) {
		this.id = newId;
	}

	/**
	 * Returns the capacity of this room.
	 * @return capacity 
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * Update the capacity of this room. 
	 * @param newCapacity 
	 */
	public void setCapacity(int newCapacity) {
		this.capacity = newCapacity;
	}

	/**
	 * Return a string representation of this room.
	 * @return toString
	 */
	public String toString() {
		String toString = "Room n�" + this.getId() + " (" + this.getCapacity() + " student)";
		return toString;
	}	

	/**
	 * Return the XML representation of the room
	 * @return roomXML 
	 */
	public Element toXML() {
		Element roomXML = new Element("Room");
		Element roomId = new Element("roomId");
		Element capacity = new Element("capacity");
		
		roomId.setText(String.valueOf(this.getId()));
		capacity.setText(String.valueOf(this.getCapacity()));
		
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
			int roomId = Integer.parseInt(room.getChildText("roomId"));
			int capacity = Integer.parseInt(room.getChildText("capacity"));
			rooms.put(roomId, new Room(roomId, capacity));
		}
		
		return rooms;
	}

}
