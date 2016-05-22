package timeTableController;

import java.util.Date;
import java.util.Hashtable;

import timeTableModel.TimeTableDB;
/**
 * Cette classe est le contr�leur d'emplois du temps que vous devez impl�menter. 
 * Elle contient un attribut correspondant � la base de donn�es d'emplois du temps que vous allez cr�er.
 * Elle contient toutes les fonctions de l'interface ITimeTableController que vous devez impl�menter.
 * 
 * @author Jose Mennesson (Mettre � jour)
 * @version 04/2016 (Mettre � jour)
 * 
 */

//TODO Classe � modifier

public class TimeTableController implements ITimeTableController{

	/**
	 * Contient une instance de base de donn�es d'emplois du temps
	 * 
	 */
	TimeTableDB tTDB;
	/**
	 * Constructeur de controleur d'emplois du temps cr�ant la base de donn�es d'emplois du temps
	 * 
	 * @param tTfile
	 * 		Fichier XML contenant la base de donn�es d'emplois du temps
	 */
	public TimeTableController(String tTfile) {
		TimeTableDB tTDB=new TimeTableDB(tTfile);
		this.tTDB=tTDB;
	}

	@Override
	public String getTeacherLogin(int timeTableId, int bookId) {
		return this.tTDB.getTeacherLogin(timeTableId, bookId);
	}

	@Override
	public String[] roomsIdToString() {
		return this.tTDB.roomsIdToString();
	}

	@Override
	public String[] roomsToString() {
		return this.tTDB.roomsToString();
	}

	@Override
	public String[] timeTablesIDToString() {
		return this.tTDB.timeTablesIDToString();
	}

	@Override
	public String[] booksIdToString(int timeTableId) {
		return this.tTDB.booksIdToString(timeTableId);
	}

	@Override
	public boolean addRoom(int roomId, int capacity) {
		return this.tTDB.addRoom(roomId, capacity);
	}

	@Override
	public boolean removeRoom(int roomId) {
		return this.tTDB.removeRoom(roomId);
	}

	@Override
	public int getRoom(int timeTableId, int bookId) {
		return this.tTDB.getRoom(timeTableId, bookId);
	}

	@Override
	public boolean addTimeTable(int timeTableId) {
		return this.tTDB.addTimeTable(timeTableId);
	}

	@Override
	public boolean removeTimeTable(int timeTableId) {
		return this.tTDB.removeTimeTable(timeTableId);
	}

	@Override
	public boolean addBooking(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd, int roomId) {
		return this.tTDB.addBooking(timeTableId, bookingId, login, dateBegin, dateEnd, roomId);
	}

	@Override
	public void getBookingsDate(int timeTableId, Hashtable<Integer, Date> dateBegin, Hashtable<Integer, Date> dateEnd) {
		this.tTDB.getBookingsDate(timeTableId, dateBegin, dateEnd);
	}

	@Override
	public boolean removeBook(int timeTableId, int bookId) {
		return this.tTDB.removeBook(timeTableId, bookId);
	}

	@Override
	public int getBookingsMaxId(int timeTableId) {
		return this.tTDB.getBookingsMaxId(timeTableId);
	}

	@Override
	public boolean saveDB() {
		return this.tTDB.saveDB();
	}

	@Override
	public boolean loadDB() {
		return this.tTDB.loadDB();
	}
	
	

}