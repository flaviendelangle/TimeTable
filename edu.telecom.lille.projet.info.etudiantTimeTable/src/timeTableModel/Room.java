/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;


/**
 * Room in which we can make books
 * 
 * @author Flavien DELANGLE and Marie PAYET
 * @version 06/2016
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
	 * @param id Id of the new room
	 * @param capacity Capacity of the new room
	 */
	public Room(int id, int capacity) {
		this.setId(id);
		this.setCapacity(capacity);
	}

	/**
	 * Returns the identifier of this room.
	 * @return The Id of this room
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Update the identifier of this room. 
	 * @param newId New Id for this Room 
	 */
	public void setId(int newId) {
		this.id = newId;
	}

	/**
	 * Returns the capacity of this room.
	 * @return Capacity of this room
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * Update the capacity of this room. 
	 * @param newCapacity Capacity of this room
	 */
	public void setCapacity(int newCapacity) {
		this.capacity = newCapacity;
	}

	/**
	 * Return a string representation of this room.
	 * @return Stringified version of this room
	 */
	public String toString() {
		return Room.stringify(this.getId(), this.getCapacity());
	}
	
	/**
	 * Return a string representation of a Room (static version)
	 * @param roomId Id of the room
	 * @param capacity Capacity of the room
	 * @return Stringified version of a room
	 */
	public static String stringify(int roomId, int capacity) {
		String toString = "Room n°" + roomId + " (" + capacity + " student)";
		return toString;		
	}

	/**
	 * Returns the XML representation of the room
	 * @return XML representation of the room 
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
	 * @return Has the room successfully been created
	 */
	public Boolean toSQL() {
		return Room.objects.create(this.getId(), this.getCapacity());
	}
	
	/**
	 * Generate a MAP of Room objects from a XML representation
	 * @param roomListXML XML reprensentation of the rooms
	 * @param rooms Map in which we must put all the Room instances
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
