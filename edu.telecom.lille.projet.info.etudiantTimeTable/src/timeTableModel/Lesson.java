package timeTableModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

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
	 * The constructor.
	 * @param length
	 * @param effective
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
	 * @param newId
	 */
	public void setId(int newId) {
		this.id = newId;
	}
	
	/**
	 * Returns the id of this lesson
	 * @return length
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Update the title of this lesson
	 * @param newTitle
	 */
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}
	
	/**
	 * Returns the title of this lesson
	 * @return title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Update the login of the teacher of this lesson
	 * @param newTitle
	 */
	public void setTeacherLogin(String newTeacherLogin) {
		this.teacherLogin = newTeacherLogin;
	}
	
	/**
	 * Returns the login of the teacher of this lesson
	 * @return title
	 */
	public String getTeacherLogin() {
		return this.teacherLogin;
	}	
		
	/**
	 * Update the length of this lesson
	 * @param newLength
	 */
	public void setLength(int newLength) {
		this.length = newLength;
	}
	
	/**
	 * Returns the length of this lesson
	 * @return length
	 */
	public int getLength() {
		return this.length;
	}
	
	/**
	 * Update the effective of this lesson
	 * @param newEffective
	 */
	public void setEffective(int newEffective) {
		this.effective = newEffective;
	}
	
	/**
	 * Returns the length of this lesson
	 * @return effective
	 */
	public int getEffective() {
		return this.effective;
	}

	/**
	 * Update the prerequisites of this lesson
	 * @param newPrerequisites
	 */
	public void setPrerequisites(ArrayList<Lesson> newPrerequisites) {
		this.prerequisites = newPrerequisites;
	}
	
	/**
	 * Returns the prerequisites of this lesson
	 * @return prerequisites
	 */
	public ArrayList<Lesson> getPrerequisites() {
		return this.prerequisites;
	}
	
	/**
	 * Add a prerequisite to this lesson
	 * @param newPrerequisite
	 */
	public void addPrerequisite(Lesson newPrerequisite) {
		this.getPrerequisites().add(newPrerequisite);
	}
	
	/**
	 * Remove a prerequisite from this lesson
	 * @param obsoletePrerequisite
	 */
	public void removePrerequisite(Lesson obsoletePrerequisite) {
		this.getPrerequisites().remove(obsoletePrerequisite);
	}
	
	/**
	 * Check if this lesson has a given prerequisite
	 * @return contains
	 */
	public boolean hasPrerequisite(Lesson prerequisite) {
		return this.getPrerequisites().contains(prerequisite);
	}
	
	/**
	 * Generate a MAP of Lessons objects from a XML representation
	 * @param lessonListXML
	 * @param lessons
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