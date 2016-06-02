package timeTableModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * Builder of timetable
 * For now the algorithms only handle the repartition of the lessons into a time range.
 * Also contain static functions to check the availaibility of a room.
 * 
 * @author Flavien DELANGLE and Marie PAYET
 * @version 06/2016
 */
public class TimeTableBuilder {

	/**
	 * Name of the XML database where the lessons are stored
	 */
	private String file;
	
	/**
	 * Map containing all the lessons to allocate in a timetable
	 */
	private Map<Integer, Lesson> lessons;
	
	/**
	 * Date before which they can be no lesson
	 */
	private Date dateBegin;

	/**
	 * Date after which they can be no lesson
	 */
	private Date dateEnd;
	
	/**
	 * Map containing all the block in which we can put a lesson
	 */
	private Map<Date, Boolean> timeMap;	
	
	/**
	 * Map containing the hours of all the lessons
	 */
	private Map<Lesson, Date[]> lessonDates = new HashMap<Lesson, Date[]>();

	/**
	 * List of the generic schedules of the lessons
	 */
	private final int[][] SCHEDULE = {
		{ 8, 30, 510 },
		{ 10, 15, 615 },
		{ 13, 00, 780 },
		{ 14, 45, 885 },
		{ 16, 30, 990 }
	};
	
	/**
	 * The constructor.
	 * @param file Name of the database in which the lessons are stored
	 * @param dateBegin Beginning date of the new timetable
	 * @param dateEnd Ending date of the new timetable
	 */
	public TimeTableBuilder(String file, Date dateBegin, Date dateEnd) {
		this.setFile(file);
		this.setDateBegin(dateBegin);
		this.setDateEnd(dateEnd);
		this.loadDB();
	}
	
	/**
	 * Update the name of the XML database
	 * @param newFile Name of the database in which the lessons are stored
	 */
	public void setFile(String newFile) {
		this.file = newFile;
	}
	
	/**
	 * Returns the name of the XML database
	 * @return Name of the database in which the lessons are stored
	 */
	public String getFile() {
		return this.file;
	}

	/**
	 * Update the name of the XML database
	 * @param newLessons List of lessons to link to this TimeTableBuilder
	 */
	public void setLessons(Map<Integer, Lesson> newLessons) {
		this.lessons = newLessons;
	}
	
	/**
	 * Returns the name of the XML database
	 * @return List of lessons linked to this TimeTableBuilder
	 */
	public Map<Integer, Lesson> getLessons() {
		return this.lessons;
	}

	/**
	 * Update the beginning date of the timetable to build
	 * @param newDateBegin Date a which the repartition must begin
	 */
	public void setDateBegin(Date newDateBegin) {
		this.dateBegin = newDateBegin;
	}
	
	/**
	 * Returns the beginning date of the timetable to build
	 * @return Date a which the repartition must begin
	 */
	public Date getDateBegin() {
		return this.dateBegin;
	}

	/**
	 * Update the ending date of the timetable to build
	 * @param newDateEnd Date a which the repartition must end
	 */
	public void setDateEnd(Date newDateEnd) {
		this.dateEnd = newDateEnd;
	}
	
	/**
	 * Returns the ending date of the timetable to build
	 * @return Date a which the repartition must end
	 */
	public Date getDateEnd() {
		return this.dateEnd;
	}	
	
	/**
	 * Update the map of the lessons
	 * @param newTimeMap Map containing all the block in which we can put a lesson
	 */
	public void setTimeMap(Map<Date, Boolean> newTimeMap) {
		this.timeMap = newTimeMap;
	}
	
	/**
	 * Returns the map of the lessons
	 * @return Map containing all the block in which we can put a lesson
	 */
	public Map<Date, Boolean> getTimeMap() {
		return this.timeMap;
	}
	
	/**
	 * Update the map of the hours of the lessons
	 * @param newLessonDates Map containing all the hours of the lessons
	 */
	public void setLessonDates(Map<Lesson, Date[]> newLessonDates) {
		this.lessonDates = newLessonDates;
	}
	
	/**
	 * Returns the map of the hours of the lessons
	 * @return Map containing all the hours of the lessons
	 */
	public Map<Lesson, Date[]> getLessonDates() {
		return this.lessonDates;
	}
		
	/**
	 * Parse a XML representation into a Map of lessons
	 */
	public void loadDB() {
		Element rootXML = null;
		Map<Integer, Lesson> lessons = new HashMap<Integer, Lesson>();
		try {
			org.jdom2.Document document = null;
			rootXML = null;
			SAXBuilder sxb = new SAXBuilder();
			document = sxb.build(new File(this.getFile()));
			if(document != null) {
				rootXML = document.getRootElement();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if(rootXML != null) {	
			Lesson.parseXML(rootXML, lessons);
			System.out.println(lessons.size());
		}
		this.setLessons(lessons);
	}
	
	/**
	 * Try to allocate all the courses into a timetable
	 * @return Map containing all the lessons as key and the dates as values
	 */
	public Map<Lesson, Date[]> create() {
		int safeguard = 0;
		Map<Integer, Lesson> placedLessons = new HashMap<Integer, Lesson>();
		this.initTime();
		while(this.getLessons().size() > 0 && safeguard < 100) {
			safeguard++;
			Map<Integer, Lesson> feasableLessons = this.getFeasableLessons(placedLessons);
			for(Map.Entry<Integer, Lesson> entry : feasableLessons.entrySet()) {
				Boolean success = this.placeLesson(entry.getValue());
				if(success) {
					placedLessons.put(entry.getKey(), entry.getValue());
					this.getLessons().remove(entry.getKey());
				}
				else {
					System.out.println("The TimeTable creation failed");
				}
			}	
		}
		return this.getLessonDates();
	}
	
	/**
	 * Split the time between the beginning date and the ending date into blocks in which we can put lessons
	 */
	public void initTime() {
		Map<Date, Boolean> timeMap = new LinkedHashMap<Date, Boolean>();
		Date actualDate = new Date(this.getDateBegin().getTime());
		while(actualDate.before(this.getDateEnd())) {
			actualDate = this.getCloserSchedule(actualDate);
			timeMap.put(new Date(actualDate.getTime()), true);
			actualDate = new Date(actualDate.getTime() + Lesson.LENGTH*60*1000);
		}
		this.setTimeMap(timeMap);
	}
	
	/**
	 * Try to find a place for a lesson
	 * @param lesson Lesson to place
	 * @return Has the lesson successfully been placed ?
	 */
	public Boolean placeLesson(Lesson lesson) {
		int length = Math.round(lesson.getLength()/Lesson.LENGTH);
		ArrayList<Date> freeBlocks = new ArrayList<Date>();
		Boolean success = false;
		for(Entry<Date, Boolean> entry : this.getTimeMap().entrySet()) {
			if(entry.getValue().booleanValue()) {
				freeBlocks.add(entry.getKey());
				if(freeBlocks.size() == length) {
					Date[] dates = {
						freeBlocks.get(0),
						new Date(freeBlocks.get(freeBlocks.size()-1).getTime() + Lesson.LENGTH*60*1000)
					};
					this.getLessonDates().put(lesson, dates);
					success = true;
					for (Date date : freeBlocks) {
						this.getTimeMap().put(date, false);
					}
				}
			}
			else {
				freeBlocks.clear();
			}
		}
		return success;
	}
	
	/**
	 * Modify a date to stick with the schedule layout 
	 * (for example will transform 8:25 into 8:30)
	 * @param date Date we want to edit
	 * @return Date modified
	 */
	@SuppressWarnings("deprecation")
	public Date getCloserSchedule(Date date) {
		int minutes = date.getHours()*60 + date.getMinutes();
		if(minutes > 990) {
			date = new Date(date.getTime() + (86400-minutes*60)*1000);
			minutes = 0;
		}
		for(int i=0; i<SCHEDULE.length; i++) {
			if(SCHEDULE[i][2] >= minutes) {
				date.setHours(SCHEDULE[i][0]);
				date.setMinutes(SCHEDULE[i][1]);
				date.setSeconds(0);
				break;
			}
		}
		return date;
	}
	
	/**
	 * Returns all the lessons with no prerequisite or only placed prerequisites
	 * @param placedLessons All the lessons that have already be placed
	 * @return All the lessons that can be placed in the timetable
	 */
	public Map<Integer, Lesson> getFeasableLessons(Map<Integer, Lesson> placedLessons) {
		Map<Integer, Lesson> lessons = new HashMap<Integer, Lesson>();
		for (Entry<Integer, Lesson> entry : this.getLessons().entrySet()) {
			if(entry.getValue().getPrerequisites().size() == 0) {
				lessons.put(entry.getKey(), entry.getValue());
			}
			else {
				Boolean feasable = true;
		        for (Lesson prerequisite : entry.getValue().getPrerequisites()) {
		            if(!placedLessons.containsKey(prerequisite.getId())) {
		            	feasable = false;
		            	break;
		            }
		        }
		        if(feasable) {
		        	lessons.put(entry.getKey(), entry.getValue());
		        }
			}
		}
		return lessons;
	}
	
	/**
	 * Try to find a Room suitable for the requested schedule and with the right capacity.
	 * It will always the smallest suitable room in term of capacity.
	 * @param dateBegin Date a which the book must begin
	 * @param dateEnd Date a which the book must end
	 * @param login Login of the teacher that wants to make this lesson
	 * @param minCapacity Number of student in the booking
	 * @param rooms All the rooms stored in the program
	 * @param timeTables All the timetables stored in the program
	 * @return A room that can host the lesson
	 */
	public static Room findFreeRoom(Date dateBegin, Date dateEnd, String login, int minCapacity, Map<Integer, Room>rooms, Map<Integer, TimeTable>timeTables) {
		Map<Integer, Room> compatibleRooms = new HashMap<Integer, Room>();
		for(Map.Entry<Integer, Room> entry : rooms.entrySet()) {
			Room room = entry.getValue();
			if(room.getCapacity() > minCapacity && TimeTableBuilder.isBookingPossible(login, dateBegin, dateEnd, room, timeTables)) {
				compatibleRooms.put(room.getId(), room);
			}
		}
		Room room = null;
		for(Map.Entry<Integer, Room> entry : compatibleRooms.entrySet()) {
			if(room == null || room.getCapacity() > entry.getValue().getCapacity()) {
				room = entry.getValue();
			}
		}
		return room;
	}

	/**
	 * Check if the booking is possible (most complete version)
	 * @param timeTableId ID the of the timetable in which we want to make the book
	 * @param bookingId ID we want to name the book with
	 * @param teacherLogin Login of the teacher that wants to make this lesson
	 * @param dateBegin Date a which the book must begin
	 * @param dateEnd Date a which the book must end
	 * @param room Room in which we want to make the lesson
	 * @param timeTables All the TimeTables in which we must search for the books
	 * @return Is this book possible ?
	 */
	public static Boolean isBookingPossible(int timeTableId, int bookingId, String teacherLogin, Date dateBegin, Date dateEnd, Room room, Map<Integer, TimeTable>timeTables) {
		TimeTable timeTable = timeTables.get(timeTableId);
		Boolean success = true;
		if(timeTable == null) {
			success = false;
		}
		else {
			success &= TimeTableBuilder.isBookIdFree(timeTable, bookingId);
			success &= TimeTableBuilder.isBookingPossible(teacherLogin, dateBegin, dateEnd, room, timeTables);
		}
		return success;
	}
	
	/**
	 * Check if the ID of a book is free to use
	 * @param timeTable Timetable in which the book has been made
	 * @param bookingId Actual ID of the book
	 * @return Is the bookId free in this timetable ?
	 */
	public static Boolean isBookIdFree(TimeTable timeTable, int bookingId) {
		for(Entry<Integer, Book> entryB : timeTable.getBooks().entrySet()) {
			Book book = entryB.getValue();
			if(book.getId() == bookingId) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if the booking is possible by checking the dates of every other booking in this room
	 * Warning : This method doesn't check if the ID is already taken 
	 * @param teacherLogin Login of the teacher that wants to make this lesson
	 * @param dateBegin Date a which the book must begin
	 * @param dateEnd Date a which the book must end
	 * @param room Room in which we want to make the lesson
	 * @param timeTables All the TimeTables in which we must search for the books
	 * @return Is this book possible ?
	 */
	public static Boolean isBookingPossible(String teacherLogin, Date dateBegin, Date dateEnd, Room room, Map<Integer, TimeTable>timeTables) {
		for(Entry<Integer, TimeTable>entryTT : timeTables.entrySet()) {
			for(Entry<Integer, Book> entryB : entryTT.getValue().getBooks().entrySet()) {
				Book book = entryB.getValue();
				if(dateEnd.after(book.getDateBegin()) || dateBegin.before(book.getDateEnd())) {
					if(book.getRoom().getId() == room.getId()) {
						return false;
					}
					else if(book.getTeacherLogin() == teacherLogin) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Check if a book is still possible 
	 * (to execute if the XML database has been updated since the last time it had been loaded)
	 * @param rootXML XML representation of the actual state of the database
	 * @param timeTableId ID in which we want to check if the book is still possible
	 * @param bookId ID of the book we want to check if it is still possible
	 * @param timeTablesLive Map of all the timeTables actually in the program
	 * @return Is there a conflict for this book ?
	 */
	public static Boolean checkBookingConflict(Element rootXML, int timeTableId, int bookId, Map<Integer, TimeTable>timeTablesLive) {
		Map<Integer, TimeTable> timeTablesFile = new HashMap<Integer, TimeTable>();
		Map<Integer, Room> roomsFile = new HashMap<Integer, Room>();
		Room.parseXML(rootXML.getChild("Rooms"), roomsFile);
		TimeTable.parseXML(rootXML.getChild("TimeTables"), timeTablesFile, roomsFile);
		
		TimeTable timeTableFile = timeTablesFile.get(timeTableId);
		TimeTable timeTableLive = timeTablesLive.get(timeTableId);
		Book bookLive = timeTableLive.getBook(bookId);
		Date dateBeginLive = bookLive.getDateBegin();
		Date dateEndLive = bookLive.getDateEnd();
		String loginLive = bookLive.getTeacherLogin();
		int bookingIdLive = bookLive.getId();
		Room roomLive = bookLive.getRoom();
		Room roomFile = roomsFile.get(roomLive.getId());
		if(timeTableFile == null) {
			// The TimeTable has been deleted
			return true;
		}
		else if(roomFile == null) {
			// The Room has been deleted
			return true;
		}
		else if(TimeTableBuilder.isBookingPossible(loginLive, dateBeginLive, dateEndLive, roomFile, timeTablesFile)) {
			// The Book is still possible
			return false;
		}
		else {
			// The Book is not possible anymore, probably because another one has been created with conflictual schedule
			TimeTableBuilder.resolveBookingConflict(timeTableFile, bookingIdLive, dateBeginLive, dateEndLive, loginLive, roomFile, roomsFile, timeTablesFile);
			return true;
		}
	}
	
	/**
	 * Try to resolve a conflictual booking
	 * @param timeTable TimeTable in which we want to resolve a book conflict
	 * @param bookId ID of the book we want to resolve
	 * @param dateBegin Date a which the book must begin
	 * @param dateEnd Date a which the book must end
	 * @param login Login of the teacher that wants to make this lesson
	 * @param exRoom Room in which this book was originally
	 * @param rooms All the rooms stored in the program
	 * @param timeTables All the timetables stored in the program
	 * @return Was there a conflict ?
	 */
	public static Boolean resolveBookingConflict(TimeTable timeTable, int bookId, Date dateBegin, Date dateEnd, String login, Room exRoom, Map<Integer, Room>rooms, Map<Integer, TimeTable>timeTables) {
		System.out.println("We are sorry to announce you that the room " + exRoom.getId() + " is not available anymore for your request.");
		Room room = TimeTableBuilder.findFreeRoom(dateBegin, dateEnd, login, exRoom.getCapacity(), rooms, timeTables);
		if(room == null) {
			// There is no compatible room for this book
			System.out.println("We have been unable to find any other room compatible with your request");
			return true;
		}
		else {
			Scanner reader = new Scanner(System.in);
			// There is a room compatible for this book
			System.out.println("The room " + room.getId() + " is compatible with yout demand. Do you want to book it ? (y/n)");
			if(reader.next().toLowerCase().equals("y")) {
				timeTable.addBooking(bookId, login, dateBegin, dateEnd, room);
			}
			if(!TimeTableBuilder.isBookIdFree(timeTable, bookId)) {
				// The ID of this book is already taken
				int newId = timeTable.getBookingsMaxId() + 1;
				System.out.println("Finally, the ID your requested is not enable, do you want to use the ID " + newId + " instead ? (y/n)");
				if(reader.next().toLowerCase().equals("y")) {
					timeTable.addBooking(newId, login, dateBegin, dateEnd, room);
				}
				else {
					System.out.println("The book is impossible to store, sorry");
				}
				reader.close();
				return true;
			}
			reader.close();
			return false;
		}		
	}
}
