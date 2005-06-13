package welterstellung;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.ArrayList;

import de.mb.database.SQLAnswerTable;
import de.mb.database.mysql.MysqlConnectionFactory;
import de.mb.database.mysql.MysqlSQLExecution;
import de.mb.database.*;
import welterstellung.ZufallsNamen;

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
	

	
	public Welterstellung(int kartenabschnittID)
	{
		dateiAusgabeFlag = 1;
		dateiAusgabePfad = "C:\\Inselpositionen.txt";
		testausgabe = 1;
		archipelAnzahl = 500;
		bildPfad = "C:\\Eigene Dateien\\C- Programme\\SeaWars\\Beispiel.bmp";
		empfindlichkeit = 0.5;
		this.kartenabschnitt = kartenabschnittID;

	}
	
	/**
	 * �ffnet einen Kanal in die Datenbank. 
	 * @return gibt 1 zur�ck, wenn es nicht geklappt hat, sonst 0.
	 */
	public int OpenConn(){
		try {
			String url = "localhost";
			String dbname = "usr_web4_2";
			SQLcf = new MysqlConnectionFactory(url, dbname);
			SQLexec = new MysqlSQLExecution(SQLcf.getConnection("web4",
					"sw666#GHf"));

		} catch (SQLException e1) {
			e1.printStackTrace();
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
	            System.out.println("Sorry, die Datei konnte nicht erstellt werden");
	            return 1;
        } 
        catch (IOException e) {
	            System.out.println("Sorry, die Datei konnte nicht erstellt werden");
	            return 2;
        }
        return 0;
	}
	
	public int Erstellung(){
		if (OpenConn()==1) return 1; //Datenbankanbindung �ffnen wenn m�glich.
		
		/* Zun�chst wird das neueWelt-Objekt erzeugt, das die Archipelpositionen und -gr��en enth�lt.
		 */
		if (testausgabe>0) System.out.println("Welterstellung gestartet. Archipel werden verteilt...");
		Archipelverteilung neueWelt;
		neueWelt = new Archipelverteilung(archipelAnzahl, bildPfad, empfindlichkeit);
		neueWelt.VerteilungAusfuehren();
		/* Neue Archipel in die Datenbank eintragen und mit Namen versehen.
		 * TODO:
		 */
		String archipelQuery="";
		ZufallsNamen name = new ZufallsNamen();
		String SqlSyntaxName;
		for (int i=0; i<neueWelt.Archipelzahl; i++)
		{
			SqlSyntaxName = name.setZufallsName().replaceAll("'","\\\\'");
			archipelQuery = "INSERT LOW_PRIORITY "+
			"INTO archipel (x_pos, y_pos, name, groessenklasse, kartenabschnitt_id) "+
			"VALUES("+ neueWelt.Orte[i].x+", "+ neueWelt.Orte[i].y +", '"+ SqlSyntaxName +"', "+ neueWelt.Orte[i].groesse +", "+ kartenabschnitt +");";
			try
			{
				SQLexec.executeInsert(archipelQuery);
			}
			catch(SQLException e)
			{
				//Jaja... Ein bis zwei Archipel gehen in die Hose, aber wen st�rts?
				//Es werden vorher auch zwei bis drei Archipel zu viel erzeugt :-)
			}

		}
		if (testausgabe>0) System.out.println(neueWelt.Archipelzahl + " Archipel wurden verteilt. Inselverteilung wird gestartet...");

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
		SendQuery("SELECT id, x_pos, y_pos, groessenklasse "+
				"FROM archipel "+
				"WHERE kartenabschnitt_id = "+ this.kartenabschnitt + ";");

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
				archipelQuery = "INSERT LOW_PRIORITY "+
				"INTO insel (name, groesse, x_pos, y_pos, archipel_id) "+
				"VALUES('"+SqlSyntaxName+"'"+
				", "+ archipel.insel[j].groesse +
				", "+ archipel.insel[j].x_pos +
				", "+ archipel.insel[j].y_pos +
				", "+ archipel.insel[j].archipelID +
				");";
				try
				{
					SQLexec.executeInsert(archipelQuery);
				}
				catch(SQLException e)
				{
					System.out.println("SQL-Insert funktioniert nicht mit folgendem Query: " + archipelQuery);
				}
				inselzahl++;
			}
			
		}
		if (testausgabe>0) System.out.println("Fertig. Dieses Mal wurden " + inselzahl+ " Inseln erzeugt.");
	    CloseConn(); //SQL-Datenbankanbindung wieder schlie�en
		return 0;
	} //Verteilung
}