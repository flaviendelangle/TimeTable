package timeTableController;

import java.util.Date;
import java.util.Hashtable;

/**
 * Cette classe est l'interface du contr�leur que vous devez impl�menter. 
 * Elle contient un certains nombre de fonctions qui sont utilis�es dans l'interface graphique. 
 * Elle permet � cette derni�re d'�tre ind�pendante par rapport � votre impl�mentation de la gestion des emplois du temps.
 * Cette classe doit �tre impl�ment�e par la classe TimeTableController qui devra impl�menter les fonctions d�crites ci-dessous.
 * 
 * @author Jose Mennesson
 * @version 04/2016
 * 
 */

//TODO Classe � ne pas modifier

public interface ITimeTableController {
	/**
	 * Fonction permettant de r�cup�rer le login du professeur qui a r�alis� la r�servation dont l'identifiant est bookId dans l'emploi du temps dont l'identifiant est timeTableId. 
	 * @param timeTableId
	 * 		L'identifiant de l'emploi du temps
	 * @param bookId
	 * 		L'identifiant de r�servation
	 * @return
	 * 		Le login du professeur qui a fait la r�servation.
	 */
	public String getTeacherLogin(int timeTableId, int bookId);
	/**
	 * Fonction qui cr�e une salle et qui la sauvegarde dans la base de donn�es. 
	 * @param roomId
	 * 		L'identifiant de la salle
	 * @param capacity
	 * 		La capacit� de la salle
	 * @return
	 * 		Un boolean indiquant si la salle a bien �t� cr��e
	 */
	public boolean addRoom(int roomId, int capacity);
	/**
	 * Fonction qui supprime une salle et qui sauvegarde la base de donn�es. 
	 * @param roomId
	 * 		L'identifiant de la salle
	 * @return
	 * 		Un boolean indiquant si la salle a bien �t� supprim�e
	 */
	public boolean removeRoom(int roomId);
	/**
	 * Fonction qui r�cup�re l'identifiant de la salle r�serv�e dans l'emploi du temps dont l'identifiant est timeTableId et dont l'identifiant de r�servation est bookId
	 * @param timeTableId
	 * 		L'identifiant d'emploi du temps
	 * @param bookId
	 * 		L'identifiant de r�servation
	 * @return
	 * 		L'identifiant de la salle r�serv�e
	 */
	public int getRoom(int timeTableId, int bookId);
	/**
	 * Fonction qui cr�e un emploi du temps et qui le sauvegarde dans la base de donn�es
	 * @param timeTableId
	 * 		L'identifiant d'emploi du temps
	 * @return
	 * 		Un boolean indiquant si l'emploi du temps a bien �t� cr��
	 */
	public boolean addTimeTable(int timeTableId);
	/**
	 * Fonction qui supprime un emploi du temps et qui sauvegarde la base de donn�es
	 * @param timeTableId
	 * 		L'identifiant d'emploi du temps
	 * @return
	 * 		Un boolean indiquant si l'emploi du temps a bien �t� cr��
	 */
	public boolean removeTimeTable(int timeTableId);
	/**
	 * Fonction qui ajoute une r�servation dans l'emploi du temps TimeTableId et qui la sauvegarde dans la base de donn�es
	 * 
	 * @param timeTableId
	 * 		L'identifiant d'emploi du temps
	 * @param bookingId
	 * 		L'identifiant de r�servation
	 * @param login
	 * 		Le login du professeur faisant la r�servation
	 * @param dateBegin
	 * 		La date de d�but de r�servation
	 * @param dateEnd
	 * 		La date de fin de r�servation
	 * @param roomId
	 * 		L'identifiant de la salle r�serv�e
	 * @return
	 * 		Un boolean indiquant si la r�servation a bien �t� faite
	 */
	public boolean addBooking(int timeTableId, int bookingId, String login, Date dateBegin, Date dateEnd, int roomId);
	/**
	 * Fonction qui retourne les dates de d�but et de fin des r�servations de l'emploi du temps dont l'identifiant est timeTableId.
	 * 
	 * @param timeTableId
	 * 		L'identifiant d'emploi du temps
	 * @param dateBegin
	 * 		Hashtable qui contiendra les dates de d�but des r�servations. La cl� de la Hashtable correspond � l'identifiant de r�servation.
	 * @param dateEnd
	 * 		Hashtable qui contiendra les dates de fin des r�servations. La cl� de la Hashtable correspond � l'identifiant de r�servation.
	 */
	public void getBookingsDate(int timeTableId, Hashtable<Integer, Date> dateBegin, Hashtable<Integer, Date> dateEnd);
	/**
	 * Fonction qui supprime la r�servation dont l'identifiant est bookId dans l'emploi du temps timeTableId.
	 * 
	 * @param timeTableId
	 * 		L'identifiant d'emploi du temps
	 * @param bookId
	 * 		L'identifiant de r�servation � supprimer
	 * @return
	 * 		Un boolean indiquant si la r�servation a bien �t� supprim�e
	 */
	public boolean removeBook(int timeTableId,int bookId);
	/**
	 * Fonction qui r�cup�re le plus grand identifiant de r�servation dans l'emploi du temps timeTableId.
	 * 
	 * @param timeTableId
	 * 		L'identifiant d'emploi du temps
	 * @return
	 * 		Le plus grand identifiant de r�servation
	 */
	public int getBookingsMaxId(int timeTableId);
	/**
	 * Fonction permettant de r�cup�rer tous les identifiants des salles sous la forme d'un 
	 * tableau de cha�nes de caract�res o� chaque ligne contient l'identifiant d'une salle.
	 * 
	 * @return
	 * 		Un tableau de String contenant toutes les informations de tous les groupes.
	 */
	public String[] roomsIdToString();
	/**
	 * Fonction permettant de r�cup�rer toutes les informations des salles sous la forme d'un 
	 * tableau de cha�nes de caract�res o� chaque ligne contient les informations d'une salle.
	 * 
	 * @return
	 * 		Un tableau de String contenant toutes les informations de toutes les salles.
	 */
	public String[] roomsToString();
	/**
	 * Fonction permettant de r�cup�rer tous les identifiants des emplois du temps sous la forme d'un 
	 * tableau de cha�nes de caract�res o� chaque ligne contient l'identifiant d'un emploi du temps.
	 * 
	 * @return
	 * 		Un tableau de String contenant toutes les identifiants de tous les emplois du temps.
	 */
	public String[] timeTablesIDToString();
	/**
	 * Fonction permettant de r�cup�rer tous les identifiants des r�servations de l'emploi du temps timeTableId sous la forme d'un 
	 * tableau de cha�nes de caract�res o� chaque ligne contient l'identifiant d'une r�servation.
	 * 
	 * @param timeTableId
	 * 			Un identifiant d'emploi du temps
	 * @return
	 * 		Un tableau de String contenant toutes les informations de tous les groupes.
	 */
	public String[] booksIdToString(int timeTableId);
	/**
	 * Fonction sauvegardant la base de donn�e dans un fichier XML.
	 * @return
	 * 		Un boolean indiquant si la sauvegarde a bien �t� r�alis�e.
	 */
	public boolean saveDB();
	/**
	 * Fonction chargeant la base de donn�e contenue dans un fichier XML.
	 * @return
	 * 		Un boolean indiquant si le chargement a bien �t� r�alis�e.
	 */
	public boolean loadDB();

}