/*
 * Created on 15.07.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.seawars.kampfskript;

/**
 * @author Whistler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ship {

	public Weapon [] weapon;	//Bewaffnung
	public Ship target;				//Aktuelles Ziel
	int order;								//Befehl
	double location = 0;			//Standpunkt im Bezug zu Startdistanz (class battle)
	double speed;							//Maximale Geschwindigkeit des Schiffes
	double maxrange;					//Maximal mögliche Beschußdistanz
	double minrange;					//Kleinste Beschußdistanz
	double manoeuverability; 	//Manövrierfähigkeit
	double hitpoints;					//Hitpoints eben
	double armour;						//Panzerung gegen Beschuß
	double damage;						//Derzeitiges Schadensniveau
	double damageperc;				//Schaden in Prozent
	double hold; 							//Frachtraum
	double cargo;							//Derzeitige Zuladung
	double ramdamage;					//Rammschaden
	int marines;							//Marineinfanteristen um enteraktionen durchzuführen
	boolean spur = false; 		//Rammdorn?
	boolean underfire = false;//Letzte Runde unter Beschuß

	int number;								//nur zu Testzwecken

}
