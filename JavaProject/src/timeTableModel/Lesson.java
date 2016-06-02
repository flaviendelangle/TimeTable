package timeTableModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

/**
 * Lesson that need to be placed in a timetable
 * 
 * @author Flavien DELANGLE and Marie PAYET
 * @version 06/2016
 */
public class Lesson {

	/**
	 * ID of this lesson
	 */
	private int id;
	
	/**
	 * Title of this lesson
	 */
	private String title;
	
	/**
	 * Login of the teacher of this lesson
	 */
	private String teacherLogin;
	
	/**
	 * Length of this lesson in minutes
	 */
	private int length;
	
	/**
	 * Number of student expected at the lesson
	 */
	private int effective;
	
	/**
	 * Lessons that need to be followed before this one
	 */
	private ArrayList<Lesson> prerequisites = new ArrayList<Lesson>();
	
	/**
	 * Generic length of a lesson is minutes
	 */
	public final static int LENGTH = 90;
	
	/**
	 * The constructor.
	 * @param id Id of the lesson
	 * @param title Title of the lesson
	 * @param teacherLogin Login of the teacher of the lesson
	 * @param length Length in minutes of the lesson
	 * @param effective Number of student in the lesson
	 */
	public Lesson(int id, String title, String teacherLogin, int length, int effective) {
		this.setId(id);
		this.setTitle(title);
		this.setTeacherLogin(teacherLogin);
		this.setLength(length);
		this.setEffective(effective);
	}

	/**
	 * Update the id of this lesson
	 * @param newId Id of the lesson
	 */
	public void setId(int newId) {
		this.id = newId;
	}
	
	/**
	 * Returns the id of this lesson
	 * @return id Id of the lesson
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Update the title of this lesson
	 * @param newTitle Title of the lesson
	 */
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}
	
	/**
	 * Returns the title of this lesson
	 * @return title Title of the lesson
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Update the login of the teacher of this lesson
	 * @param newTeacherLogin Login of the teacher of the lesson
	 */
	public void setTeacherLogin(String newTeacherLogin) {
		this.teacherLogin = newTeacherLogin;
	}
	
	/**
	 * Returns the login of the teacher of this lesson
	 * @return teacherLogin Login of the teacher of the lesson
	 */
	public String getTeacherLogin() {
		return this.teacherLogin;
	}	
		
	/**
	 * Update the length of this lesson
	 * @param newLength Length in minute of the lesson
	 */
	public void setLength(int newLength) {
		this.length = newLength;
	}
	
	/**
	 * Returns the length of this lesson
	 * @return length Length in minute of the lesson
	 */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * Update the effective of this lesson
	 * @param newEffective Number of student in the lesson
	 */
	public void setEffective(int newEffective) {
		this.effective = newEffective;
	}
	
	/**
	 * Returns the effective of this lesson
	 * @return effective Number of student in the lesson
	 */
	public int getEffective() {
		return this.effective;
	}

	/**
	 * Update the prerequisites of this lesson
	 * @param newPrerequisites Lessons that should be done before this one
	 */
	public void setPrerequisites(ArrayList<Lesson> newPrerequisites) {
		this.prerequisites = newPrerequisites;
	}
	
	/**
	 * Returns the prerequisites of this lesson
	 * @return prerequisites Lessons that should be done before this one
	 */
	public ArrayList<Lesson> getPrerequisites() {
		return this.prerequisites;
	}
	
	/**
	 * Add a prerequisite to this lesson
	 * @param newPrerequisite Prerequisite to add to this lesson
	 */
	public void addPrerequisite(Lesson newPrerequisite) {
		this.getPrerequisites().add(newPrerequisite);
	}
	
	/**
	 * Remove a prerequisite from this lesson
	 * @param obsoletePrerequisite Prerequisite to remove from this lesson
	 */
	public void removePrerequisite(Lesson obsoletePrerequisite) {
		this.getPrerequisites().remove(obsoletePrerequisite);
	}
	
	/**
	 * Check if this lesson has a given prerequisite
	 * @param prerequisite Prerequisite to check
	 * @return Is this prerequisite in this lesson ?
	 */
	public boolean hasPrerequisite(Lesson prerequisite) {
		return this.getPrerequisites().contains(prerequisite);
	}
	
	/**
	 * Generate a MAP of Lessons objects from a XML representation
	 * @param lessonListXML XML representation of the lessons
	 * @param lessons Map in which we must store the lessons
	 */
	public static void parseXML(Element lessonListXML, Map<Integer, Lesson>lessons) {
		List<Element> lessonXML = lessonListXML.getChildren("Lesson");
		Iterator<Element> itLessons = lessonXML.iterator();
		
		while(itLessons.hasNext()) {
			Element lesson = (Element)itLessons.next();
			int lessonId = Integer.parseInt(lesson.getChildText("Id"));
			String title = lesson.getChildText("Title");
			String teacherLogin = lesson.getChildText("Login");
			int length = Integer.parseInt(lesson.getChildText("Length"));
			int effective = Integer.parseInt(lesson.getChildText("Effective"));
			lessons.put(lessonId, new Lesson(lessonId, title, teacherLogin, length, effective));
		}
		itLessons = lessonXML.iterator();
		Iterator<Element> itPrerequisites;
		while(itLessons.hasNext()) {
			Element lesson = (Element)itLessons.next();
			int lessonId = Integer.parseInt(lesson.getChildText("Id"));
			List<Element> prerequisites = lesson.getChild("Prerequisites").getChildren("Prerequisite");
			itPrerequisites = prerequisites.iterator();
			Lesson lessonObj = lessons.get(lessonId);
			while(itPrerequisites.hasNext()) {
				int prerequisite = Integer.parseInt(itPrerequisites.next().getText());
				lessonObj.addPrerequisite(lessons.get(prerequisite));			
			}	
		}
	}
}
