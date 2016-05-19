/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableModel;

import java.util.Date;
// Start of user code (user defined imports)

// End of user code

/**
 * Description of Reservation.
 * 
 * @author delangle
 */
public class Reservation {
	/**
	 * Description of the property dateEnd.
	 */
	public Date dateEnd = new Date();

	/**
	 * Description of the property bookId.
	 */
	public int bookId = 0;

	/**
	 * Description of the property teacherLogin.
	 */
	public String teacherLogin = "";

	/**
	 * Description of the property dateBegin.
	 */
	public Date dateBegin = new Date();

	/**
	 * Description of the property rooms.
	 */
	public Room room = null;

	// Start of user code (user defined attributes for Reservation)

	// End of user code

	/**
	 * The constructor.
	 */
	public Reservation() {
		// Start of user code constructor for Reservation)
		super();
		// End of user code
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

	// Start of user code (user defined methods for Reservation)

	// End of user code
	/**
	 * Returns dateEnd.
	 * @return dateEnd 
	 */
	public Date getDateEnd() {
		return this.dateEnd;
	}

	/**
	 * Sets a value to attribute dateEnd. 
	 * @param newDateEnd 
	 */
	public void setDateEnd(Date newDateEnd) {
		this.dateEnd = newDateEnd;
	}

	/**
	 * Returns bookId.
	 * @return bookId 
	 */
	public int getBookId() {
		return this.bookId;
	}

	/**
	 * Sets a value to attribute bookId. 
	 * @param newBookId 
	 */
	public void setBookId(int newBookId) {
		this.bookId = newBookId;
	}

	/**
	 * Returns teacherLogin.
	 * @return teacherLogin 
	 */
	public String getTeacherLogin() {
		return this.teacherLogin;
	}

	/**
	 * Sets a value to attribute teacherLogin. 
	 * @param newTeacherLogin 
	 */
	public void setTeacherLogin(String newTeacherLogin) {
		this.teacherLogin = newTeacherLogin;
	}

	/**
	 * Returns dateBegin.
	 * @return dateBegin 
	 */
	public Date getDateBegin() {
		return this.dateBegin;
	}

	/**
	 * Sets a value to attribute dateBegin. 
	 * @param newDateBegin 
	 */
	public void setDateBegin(Date newDateBegin) {
		this.dateBegin = newDateBegin;
	}

	/**
	 * Returns rooms.
	 * @return rooms 
	 */
	public Room getRoom() {
		return this.room;
	}

	/**
	 * Sets a value to attribute rooms. 
	 * @param newRooms 
	 */
	public void setRoom(Room newRoom) {
		this.room = newRoom;
	}

}
