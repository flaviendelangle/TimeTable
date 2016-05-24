package timeTableModel;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Element;

public class TimeTableGroup extends TimeTable {
	/**
	 * Id of the current timetable
	 */
	private int id = 0;
	
	
	/**
	 * The constructor.
	 */
	public TimeTableGroup(int timeTableId) {
		super();
		this.setId(timeTableId);
	}
	
	/**
	 * The constructor.
	 */
	public TimeTableGroup(int timeTableId, Map<Integer,Book> books) {
		super(books);
		this.setId(timeTableId);
	}
	
	/**
	 * Returns the identifier of this timetable.
	 * @return id 
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Update the identifier of this timetable.
	 * @param newId 
	 */
	public void setId(int newId) {
		this.id = newId;
	}
	
	public Element toXML() {
		Element timeTableXML = super.toXML();
		Element timeTableId = new Element("GroupId");
		
		timeTableId.setText(String.valueOf(this.getId()));
		timeTableXML.addContent(timeTableId);
		
		return timeTableXML;
	}
}
