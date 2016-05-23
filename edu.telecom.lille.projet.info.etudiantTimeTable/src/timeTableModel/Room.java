/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;


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
	 * Simple ORM for the Room Object
	 */
	public static RoomORM objects = new RoomORM();

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
		return Room.stringify(this.getId(), this.getCapacity());
	}
	
	/**
	 * Return a string representation of a Room (static version)
	 * @return toString
	 */
	public static String stringify(int roomId, int capacity) {
		String toString = "Room n°" + roomId + " (" + capacity + " student)";
		return toString;		
	}

	/**
	 * Returns the XML representation of the room
	 * @return roomXML 
	 */
	public Element toXML() {
		Element roomXML = new Element("Room");
		Element roomId = new Element("RoomId");
		Element capacity = new Element("Capacity");
		
		roomId.setText(String.valueOf(this.getId()));
		capacity.setText(String.valueOf(this.getCapacity()));
		
		roomXML.addContent(roomId);
		roomXML.addContent(capacity);
		
		return roomXML;
	}
	
	/**
	 * Returns a SQL request to create this Room in the database
	 * @return roomSQL
	 */
	public String toSQL() {
		return Room.createSQL(this.getId(), this.getCapacity());
	}
	
	/**
	 * Return a SQL request to create a new Room in the database
	 * @param roomId
	 * @param capacity
	 * @return roomSQL
	 */
	public static String createSQL(int roomId, int capacity) {
		String roomSQL = "INSERT INTO Room (RoomId, Capacity) VALUES(" + roomId + "," + capacity + ");";
		return roomSQL;
	}
	
	/**
	 * Generate a MAP of Room objects from a XML representation
	 * @param roomListXML
	 * @return rooms
	 */
	public static void parseXML(Element roomListXML, Map<Integer, Room> rooms) {
		List<Element> roomsXML = roomListXML.getChildren("Room");
		Iterator<Element> itRooms = roomsXML.iterator();
		
		while(itRooms.hasNext()) {
			Element room = (Element)itRooms.next();
			int roomId = Integer.parseInt(room.getChildText("RoomId"));
			int capacity = Integer.parseInt(room.getChildText("Capacity"));
			rooms.put(roomId, new Room(roomId, capacity));
		}
	}

}
