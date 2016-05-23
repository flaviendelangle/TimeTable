package timeTableModel;

import java.sql.ResultSet;

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
	 * @param roomId
	 * @return conditions
	 */
	private String conditionId(int roomId) {
		return super.condition(this.idColumn, roomId);
	}

	/**
	 * Return the conditional part of the SQL request
	 * Work only to get a room by giving the capacity
	 * @param capacity
	 * @return conditions
	 */
	private String conditionCapacity(int capacity) {
		return super.condition(this.capacityColumn, capacity);
	}
	
	/**
	 * Return the list of the columns to create a new room in the database
	 * @return columns
	 */
	private String insertColumns() {
		return this.idColumn + "," + this.capacityColumn;
	}

	/**
	 * Return the list of the values to create a new room in the database
	 * @param roomId
	 * @param capacity
	 * @return values
	 */
	private String insertValues(int roomId, int capacity) {
		return roomId + "," + capacity;
	}
	
	/**
	 * Get all the rooms
	 * @return
	 */
	public ResultSet all() {
		return super.get(this.table, "*", "1");
	}
	
	/**
	 * Get all the rooms with the right id (there should never be more than one)
	 * @param roomId
	 * @return rooms
	 */
	public ResultSet get(int roomId) {
		String conditions = this.conditionId(roomId);
		return super.get(this.table, "*", conditions);
	}

	/**
	 * Create a new Room in the database
	 * @param roomId
	 * @param capacity
	 * @return success
	 */
	public Boolean create(int roomId, int capacity) {
		return super.create(this.table, this.insertColumns(), this.insertValues(roomId, capacity));
	}
	
	/**
	 * Remove the Room with the right id
	 * @param roomId
	 * @return success
	 */
	public Boolean delete(int roomId) {
		String conditions = this.conditionId(roomId);
		return super.delete(this.table, conditions);
	}
	
	/**
	 * Check if there is a room with the right id
	 * @param roomId
	 * @param default_value
	 * @return exist
	 */
	public Boolean exist(int roomId, Boolean default_value) {
		String conditions = this.conditionId(roomId);
		return super.exist(this.table, conditions, default_value);
	}
	
	/**
	 * Check if there is a room with the right id and the right capacity
	 * @param roomId
	 * @param default_value
	 * @return exist
	 */
	public Boolean exist(int roomId, int capacity, Boolean default_value) {
		String conditions = this.conditionId(roomId) + "AND" + this.conditionCapacity(capacity);
		return super.exist(this.table, conditions, default_value);		
	}
	
	/**
	 * Get the length of the Room table
	 * @return number
	 */
	public int length() {
		return super.length(this.table);
	}
	


}
