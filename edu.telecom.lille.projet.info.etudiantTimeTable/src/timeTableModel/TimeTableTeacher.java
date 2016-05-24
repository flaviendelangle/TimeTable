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

public class TimeTableTeacher extends TimeTable {

	/**
	 * Login of the teacher
	 */
	private String login ;
	
	/**
	 * The constructor.
	 */
	public TimeTableTeacher(String loginTeacher) {
		super();
		this.setLogin(loginTeacher);
	}
	
	/**
	 * The constructor.
	 */
	public TimeTableTeacher(String loginTeacher, Map<Integer,Book> books) {
		super(books);
		this.setLogin(loginTeacher);
	}
	
	/**
	 * Returns the identifier of this timetable.
	 * @return id 
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * Update the identifier of this timetable.
	 * @param newId 
	 */
	public void setLogin(String newLogin) {
		this.login = newLogin;
	}
	
	public Element toXML() {
		Element timeTableXML = super.toXML();
		Element timeTableLogin = new Element("Login");
		
		timeTableLogin.setText(this.getLogin());
		timeTableXML.addContent(timeTableLogin);
		
		return timeTableXML;
	}
}