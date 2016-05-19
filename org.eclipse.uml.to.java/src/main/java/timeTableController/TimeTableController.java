/*******************************************************************************
 * 2016, All rights reserved.
 *******************************************************************************/
package timeTableController;

// Start of user code (user defined imports)

// End of user code

/**
 * Description of TimeTableController.
 * 
 * @author delangle
 */
public class TimeTableController implements ITimeTableController {
	/**
	 * Description of the property timeTableDB.
	 */
	public TimeTableDB timeTableDB = null;

	// Start of user code (user defined attributes for TimeTableController)

	// End of user code

	/**
	 * The constructor.
	 */
	public TimeTableController() {
		// Start of user code constructor for TimeTableController)
		super();
		// End of user code
	}

	// Start of user code (user defined methods for TimeTableController)

	// End of user code
	/**
	 * Returns timeTableDB.
	 * @return timeTableDB 
	 */
	public TimeTableDB getTimeTableDB() {
		return this.timeTableDB;
	}

	/**
	 * Sets a value to attribute timeTableDB. 
	 * @param newTimeTableDB 
	 */
	public void setTimeTableDB(TimeTableDB newTimeTableDB) {
		if (this.timeTableDB != null) {
			this.timeTableDB.set(null);
		}
		this.timeTableDB.set(this);
	}

}
