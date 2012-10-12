package welterstellung;

import de.mb.database.SQLAnswerTable;
import de.mb.database.mysql.MysqlConnectionFactory;
import de.mb.database.mysql.MysqlSQLExecution;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Diese Klasse kann eine neue Welt erschaffen.
 * @author Andreas
 *
 */

public class Welterstellung{
	public int dateiAusgabeFlag;
	public String dateiAusgabePfad;
	public int testausgabe;
	public int archipelAnzahl;
	public double empfindlichkeit;
	public String bildPfad;
	public int kartenabschnitt;

	private MysqlConnectionFactory SQLcf;
	private MysqlSQLExecution SQLexec;
	private SQLAnswerTable SQLAntwort;

	public String dbhost;
	public String dbname;
	public String dbuser;
	public String dbpass;



	public Welterstellung(int kartenabschnittID)
	{
		dateiAusgabeFlag = 1;
		testausgabe = 1;
		archipelAnzahl = 500;
		empfindlichkeit = 0.5;
		this.kartenabschnitt = kartenabschnittID;

	}

	/**
	 * �ffnet einen Kanal in die Datenbank.
	 * @return gibt 1 zur�ck, wenn es nicht geklappt hat, sonst 0.
	 */
	public int OpenConn(){
		try {
			SQLcf = new MysqlConnectionFactory(dbhost, dbname);
			SQLexec = new MysqlSQLExecution(SQLcf.getConnection(dbuser, dbpass));

		} catch (SQLException e1) {
			//e1.printStackTrace();
			return 1;
		}
		return 0;
	}

	public void CloseConn(){
		try {
			SQLexec.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void SendQuery(String Query)
	{
		try {
			// one way: Give Query
			SQLAntwort = SQLexec.executeQuery(Query);

			/*
			// second way
			ArrayList fields = new ArrayList();
			fields.add("id");
			fields.add("name");
			ArrayList from = new ArrayList();
			from.add("insel");
			t = exec.executeQuery(fields, from);
			System.out.println(t.toString(true));
			/* */
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Die Methode gibt einen String in eine Textdatei aus.
	 * @param ausgabe ist der Auszugebende String
	 * @param pfad ist der Pfad, an den die Datei geschrieben werden soll
	 * @return 0: Hat funktioniert,
	 * 1: konnte nicht erstellt werden da die Textdatei nicht gefunden wurde
	 * 2: Konnte aufgrund einer IOException nicht erstellt werden.
	 */
	public int dateiAusgabe(String ausgabe, String pfad)
	{
		try {
       		File file = new File(pfad);
        	FileOutputStream fout = new FileOutputStream(file);
        	fout.write(ausgabe.getBytes());
        	fout.close();
        }
        catch (FileNotFoundException e) {
	            System.out.println("Sorry, die Datei "+pfad+" konnte nicht erstellt werden");
	            return 1;
        }
        catch (IOException e) {
	            System.out.println("Sorry, die Datei konnte nicht erstellt werden");
	            return 2;
        }
        return 0;
	}

	public int Erstellung(){
		if (bildPfad == null) {
			System.err.println("Bildpfad nicht gesetzt");
			System.exit(1);
		}

		File f = new File(bildPfad);
		if (!f.exists()) {
			System.err.println("Bildpfad '" + bildPfad + "' existiert nicht");
			System.exit(1);
		}


		if (OpenConn()==1){//Datenbankanbindung �ffnen wenn m�glich.
			if (testausgabe > 0) System.out.println("Die Datenbankverbindung konnte nicht ge�ffnet werden.");
			return 1;
		}

		try {
			SQLexec.startTransaction();
		} catch (SQLException ex) {
			Logger.getLogger(Welterstellung.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
			System.exit(-1);
		}

		long zeitstempel = new Date().getTime();

		String createdAt = "'" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "'";

		/* Zunächst wird das neueWelt-Objekt erzeugt, das die Archipelpositionen und -größen enthält.
		 */
		SendQuery("SELECT max(id) FROM World;");
		int welt_id = 1;
		try
		{
			welt_id = Integer.parseInt(SQLAntwort.getDataCell(1, 1));
		} catch (NumberFormatException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Create a world prior to creating islands");
			System.exit(0);
		}

		if (testausgabe > 0) System.out.println("Nächster Kartenabschnitt");

		try
		{
			SQLexec.executeInsert("INSERT LOW_PRIORITY INTO MapSection (createdAt, worldId) VALUES ("+
				createdAt +", " + welt_id+ ");");

			SendQuery("SELECT max(id) FROM MapSection;");
			try
			{
				kartenabschnitt = Integer.parseInt(SQLAntwort.getDataCell(1,1));
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
				System.exit(1);
			}

			// TODO fix the section links
			/*if (kartenabschnitt > 1) {
				SQLexec.executeUpdate("UPDATE LOW_PRIORITY MapSection SET leftSectionId = "+(kartenabschnitt-1)+" WHERE id = " +
									(kartenabschnitt) + ";");
			}*/
		}
		catch(SQLException e)
		{
			System.err.println(SQLexec.getLastQuery());
			e.printStackTrace();
			System.exit(0);
		}
		if (testausgabe>0) System.out.println("Welterstellung gestartet. Archipel werden verteilt...");
		Archipelverteilung neueWelt;
		neueWelt = new Archipelverteilung(archipelAnzahl, bildPfad, empfindlichkeit);
		neueWelt.VerteilungAusfuehren();
		/* Neue Archipel in die Datenbank eintragen und mit Namen versehen.
		 */
		String archipelQuery="";
		ZufallsNamen name = new ZufallsNamen();
		String SqlSyntaxName;
		for (int i=0; i<neueWelt.Archipelzahl; i++)
		{
			SqlSyntaxName = name.setZufallsName().replaceAll("'","\\\\'");
			archipelQuery = "INSERT LOW_PRIORITY "+
			"INTO Archipelago (createdAt, xPos, yPos, name, magnitude, mapSectionId) "+
			"VALUES("+ createdAt + ", " + neueWelt.Orte[i].x+", "+ neueWelt.Orte[i].y +", '"+ SqlSyntaxName +"', "+ (neueWelt.Orte[i].groesse+1) +", "+ kartenabschnitt +");";
			try
			{
				SQLexec.executeInsert(archipelQuery);
			}
			catch(SQLException e)
			{
				//Jaja... Ein bis zwei Archipel gehen in die Hose, aber wen st�rts?
				//Es werden vorher auch zwei bis drei Archipel zu viel erzeugt (-;
				System.err.println(SQLexec.getLastQuery());
				e.printStackTrace();
				System.exit(0);
			}

		}

		if (testausgabe>0)
		{
			System.out.println("Bisherige Laufzeit: " + ((new Date().getTime())-zeitstempel) + " ms. Pro Archipel sind das " +((new Date().getTime())-zeitstempel)/neueWelt.Archipelzahl + " ms.");
			System.out.println(neueWelt.Archipelzahl + " Archipel wurden auf der Karte "+kartenabschnitt+" verteilt. Inselverteilung wird gestartet...");
		}

		/* Archipel sind jetzt auf der Karte verteilt und k�nnen in die Datenbank eingetragen werden.
		 * Ab hier werden die Inseln in den Archipeln verteilt. Es werden alle Archipele nacheinander
		 * durchgegangen und zu jedem Archipel die Inseln erzeugt. Aufgrund der vielen 2-D-Operatoren
		 * kann dieser Vorgang mehrere Minuten in Anspruch nehmen.
		 * In die Inseltabelle m�ssen folgende Werte eingetragen werden:
		 * name, groesse, x_pos, y_pos, archipel_id
		 * Alle Inseln werden mit nur einem Archipel erzeugt, das aber jeweils aus der Datenbank die
		 * neuen Eigenschaften bekommt. Der Grund ist, dass die Methode, die unter testen.java
		 * angewendet wurde und alle Inseln im Hauptspeicher hielt, den Rechner doch arg stark aus-
		 * gelastet hat. Jetzt entfallen extrem viele Konstruktoraufrufe, was das ganze schon schneller
		 * machen d�rfte.
		 */
		int inselzahl = 0;
		Archipel archipel = new Archipel();
		SendQuery("SELECT id, xPos, yPos, magnitude "+
				"FROM Archipelago "+
				"WHERE mapSectionId = "+ this.kartenabschnitt + ";");

		SQLAnswerTable SQLAntwortArchipel;
		SQLAntwortArchipel = SQLAntwort;

		// SQLAntwort z�hlt beginnend mit der 1! (Schweinerei, wer macht denn sowas? ;-) )
		for (int i=1; i<=SQLAntwortArchipel.rowCount(); i++)
		{
			archipel.archipelID = Integer.parseInt(SQLAntwortArchipel.getDataCell(1,i));
			archipel.x 			= Integer.parseInt(SQLAntwortArchipel.getDataCell(2,i));
			archipel.y 			= Integer.parseInt(SQLAntwortArchipel.getDataCell(3,i));
			archipel.groesse	= Integer.parseInt(SQLAntwortArchipel.getDataCell(4,i));
			while( archipel.inselnImArchipelVerteilen() == 0);
			if ((testausgabe>0) && (i%10 ==0)) System.out.println((int)((double)i*100/archipelAnzahl) + "% ");

			for (int j=0; j<archipel.inselAnzahl; j++)
			{
				SqlSyntaxName = archipel.insel[j].getName().replaceAll("'","\\\\'");
				// Lager f�r die Insel erstellen. Kapazit�t =0. Egal. Ihr wolltet es so.
				try
				{
					SQLexec.executeInsert("INSERT LOW_PRIORITY INTO Storage (createdAt, capacity) VALUES (" + createdAt + ", 0);");
				}
				catch (SQLException e)
				{

					System.out.println ("Es konnte kein Lager für diese Insel erstellt werden: "+ SqlSyntaxName);
					e.printStackTrace();
					System.exit(1);
				}
				SendQuery("SELECT  max(id) FROM Storage;");
				archipelQuery = "INSERT LOW_PRIORITY "+
				"INTO Island (name, size, xPos, yPos, archipelagoId, storageId, createdAt) "+
				"VALUES('"+SqlSyntaxName+"'"+
				", "+ archipel.insel[j].groesse +
				", "+ archipel.insel[j].x_pos +
				", "+ archipel.insel[j].y_pos +
				", "+ archipel.insel[j].archipelID +
				", "+ SQLAntwort.getDataCell(1,1) +
				", "+ createdAt +
				");";
				try
				{
					SQLexec.executeInsert(archipelQuery);
					inselzahl++;
				}
				catch(SQLException e)
				{
					System.out.println("SQL-Insert funktioniert nicht mit folgendem Query: " + archipelQuery);
					System.err.println(SQLexec.getLastQuery());
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
		if (testausgabe>0)
		{
			System.out.println("Fertig. Dieses Mal wurden " + inselzahl+ " Inseln erzeugt.");
			System.out.println("gesamte Laufzeit:" + ((new Date().getTime())-zeitstempel) + " ms." );
		}

		try {
			SQLexec.commitTransaction();
		} catch (SQLException ex) {
			Logger.getLogger(Welterstellung.class.getName()).log(Level.SEVERE, null, ex);
			System.exit(-1);
		}

	    CloseConn(); //SQL-Datenbankanbindung wieder schlie�en

		return 0;
	} //Verteilung
}
