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
public class ship {
	
	public weapon [] weapon;	//Bewaffnung
	public ship target;				//Aktuelles Ziel
	int order;								//Befehl
	double location = 0;			//Standpunkt im Bezug zu Startdistanz (class battle)
	double speed;							//Maximale Geschwindigkeit des Schiffes
	double maxrange;					//Maximal m�gliche Beschu�distanz
	double minrange;					//Kleinste Beschu�distanz
	double manoeuverability; 	//Man�vrierf�higkeit
	double armour;						//Panzerung gegen Beschu� 
	double damage;						//Derzeitiges Schadensniveau 
	double hold; 							//Frachtraum
	double cargo;							//Derzeitige Zuladung
	double ramdamage;					//Rammschaden
	int marines;							//Marineinfanteristen um enteraktionen durchzuf�hren
	boolean spur = false; 		//Rammdorn?
	boolean underfire = false;//Letzte Runde unter Beschu�?
	
	int number;								//nur zu Testzwecken
	
}
