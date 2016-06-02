package timeTableModel;

import java.sql.ResultSet;

/**
 * Implementation of the ORM of the Room class.
 * 
 * @author Flavien DELANGLE and Marie PAYET
 * @version 06/2016
 */
public class RoomORM extends ORM {
	
	/**
	 * Name of the SQL table where the timetables are stored
	 */
	protected String table = "Room";

	/**
	 * Name of the SQL column where the ids are stored
	 */
	protected String idColumn = "RoomId";
	
	/**
	 * Name of the SQL column where the capacities are stored
	 */
	protected String capacityColumn = "Capacity";

	/**
	 * The constructor
	 */
	public RoomORM() {
		
	}

	/**
	 * Return the conditional part of a SELECT SQL request
	 * Work only to get a room by giving the id
	 * @param Id of the room
	 * @return Stringified version of this condition
	 */
	private String conditionId(int roomId) {
		return super.condition(this.idColumn, roomId);
	}

	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a room by giving the capacity
	 * @param capacity Capacity of the room
	 * @return Stringified version of this condition
	 */
	private String conditionCapacity(int capacity) {
		return super.condition(this.capacityColumn, capacity);
	}
	
	/**
	 * Return the list of the columns to create a new room in the database
	 * @return Columns used to create this room
	 */
	private String insertColumns() {
		return this.idColumn + "," + this.capacityColumn;
	}

	/**
	 * Return the list of the values to create a new room in the database
	 * @param roomId Id of the new room
	 * @param capacity Capacity of the new room
	 * @return Values used to create this room
	 */
	private String insertValues(int roomId, int capacity) {
		return roomId + "," + capacity;
	}
	
	/**
	 * Get all the rooms
	 * @return All the rooms stored in this database
	 */
	public ResultSet all() {
		return super.get(this.table, "*", "1");
	}
	
	/**
	 * Get all the rooms with the right id (there should never be more than one)
	 * @param roomId Id of the room we want to retrieve
	 * @return All the rooms with the right id
	 */
	public ResultSet get(int roomId) {
		String conditions = this.conditionId(roomId);
		return super.get(this.table, "*", conditions);
	}

	/**
	 * Create a new Room in the database
	 * @param roomId Id of the new room
	 * @param capacity Vapacity of the new room
	 * @return Has the room been successfully created ?
	 */
	public Boolean create(int roomId, int capacity) {
		return super.create(this.table, this.insertColumns(), this.insertValues(roomId, capacity));
	}
	
	/**
	 * Remove the Room with the right id
	 * @param roomId Id of the room we want to delete
	 * @return Has the room been successfully deleted ?
	 */
	public Boolean delete(int roomId) {
		String conditions = this.conditionId(roomId);
		return super.delete(this.table, conditions);
	}
	
	/**
	 * Check if there is a room with the right id
	 * @param roomId Id of the room of which we want to check the existence
	 * @param default_value Value to return is there is an issue
	 * @return Does this room exist ?
	 */
	public Boolean exist(int roomId, Boolean default_value) {
		String conditions = this.conditionId(roomId);
		return super.exist(this.table, conditions, default_value);
	}
	
	/**
	 * Check if there is a room with the right id and the right capacity
	 * @param roomId Id of the room of which we want to check the existence
	 * @param capacity Capacity that the room we want to find must have
	 * @param default_value Value to return is there is an issue
	 * @return Does this room exist ?
	 */
	public Boolean exist(int roomId, int capacity, Boolean default_value) {
		String conditions = this.conditionId(roomId) + "AND" + this.conditionCapacity(capacity);
		return super.exist(this.table, conditions, default_value);		
	}
	
	/**
	 * Get the length of the Room table
	 * @return Number of rooms stored in this database
	 */
	public int length() {
		return super.length(this.table);
	}
	


}
