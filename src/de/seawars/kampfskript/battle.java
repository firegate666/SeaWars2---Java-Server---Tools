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
 * 
 * Der Kampfablauf soll wie folgt gestaltet werden:
 * 
 * A. Kampfvorbereitungen
 * - Die Daten der Schiffe und Waffen werden aus der Datenbank geholt und mit 
 *   den jeweiligen Klassen instanziert. 
 * - Die Startdistanz der beiden Flotten wird ermittelt. (startdistance())
 * - Einem jeden Schiff einer Flotte wird genau ein Ziel in der gegnerischen
 *   Flotte zugewiesen. (initialTargets())
 * 
 * B. Kampfablauf
 * - Jedes Schiff wird auf seinen prozentualen Schaden überprüft und die Kampfwerte
 *   werden dementsprechend angepsst. (damage())
 * - Jede Schiff einer Flotte wird, beginend mit dem Angreifer, bewegt.
 *   Ramm und Enterangriffe werden bereits in dieser Phase abgewickelt,
 *   da sich die potenziellen Ziele sonst wegbewegen würden. (movement())
 * - Jedem Schiff wird, basierend auf seinem Befehl, möglicherweise ein neues Ziel zugewiesen.
 *   Im Regelfall wird immer auf die nächsten Gegner geschossen. (closest())
 * - Die Schußphase wird abgehandelt. auch hier erst der Angreifer,
 *   dann der Vetreidiger. (shooting())
 * - Möglicherweise aufgetretene Verluste werden aus den arrays entfernt,
 *   der dann neu erstellt wird. (casulties())(Siehe closest())
 * - 
 */
public class battle {

	private ship[] a;				//Attacker
	private ship[] d;				//Defender
	private weapon[] arms;	//Bewaffnung
	double startdistance;
	double weather;
	double time;
	double sunrise;
	double sunset;
	
	private void startdistance(){
		//Aus dem Wetter und der Startzeit wird die Entfernung
		//zwischen beiden Flotten	berechnet in der der Kampf startet
		/**TODO Wetter, Uhrzeit und Sonnenauf bzw. untergangszeit einarbeiten**/
	  
	  
		startdistance = 1000;
	}
	
	private void initialTargets(){
		//Hier wird, ausschließlich zu Begin des Kampfes, jedem Schiff ein Gegner zugewiesen.
		
		if (a.length <= d.length )
			for(int i = 0; i < a.length; i++) 
				a[i].target = d[i];
		else
			for(int i = 0; i < a.length; i++)
				a[i].target = d[i%d.length];
	}
	
	private void damage(){
		//Hier werden die Auswirkungen des Schadens eines Schiffes ermittelt.
		/**TODO Alles!**/
	}
	
	private ship closest(ship s, ship[] ta){
		//Das nächste feindliche Schiff wird ermittelt
		/** TODO Sicherstellen das ta[] kein null enthält**/
		ship t = ta[0];
		
		for (int i = 1; i < ta.length; i++)
			t = distance(s, t) < distance(s, ta[i]) ? t : ta[i];
		return t;
	}
	
	private double distance(ship s, ship t){
		//Berechnet die Distanz zwischen einem Schiff und seinem Ziel
		
		return startdistance - s.location - t.location;
	}
	
	private double checkmove(ship s, double m){
		//Hier wird auf die maximale Bewegungsfähigkeit des Schiffes geprüft
		
		return m < s.speed ?  m : s.speed;
	}
	
	private void movement(ship[] ship){
		//Hier werden die Bewegungen der einzelnen Schiffe durchgeführt.
		 
		ship s;						//Das derzeitige Schiff
		ship t;						//Das Ziel von s
		double movement;	//Die nötige Bewegung um den Befehl zu erfüllen	
		double distance;	//Der Abstand zwischen s und t
		
		for (int i = 0; i < ship.length; i++){
			s = ship[i];
			t = s.target;
			switch (s.order){
			
				case 0:		//longrangefire: Kampf auf maximale Waffenreichweite
					movement = Math.abs(distance(s, t) - s.maxrange - 10);
					if (distance(s, t) <= s.maxrange)		
						s.location -= checkmove (s, movement);
					else
					  s.location += checkmove (s, movement);
					break;
					
				case 1:		//shortrangefire: Kampf auf optimale Waffenreichweite
					movement = Math.abs(distance(s, t) - s.minrange - 10);
					if (distance(s, t) <= s.minrange)		
						s.location -= checkmove (s, movement);
					else
					  s.location += checkmove (s, movement);
					break;
					
				case 2:		//boarding: Entermänöver
					movement = distance(s, t);
		      s.location += checkmove (s, movement);
		      break;
		      
				case 3:		//ramming: Rammmanöver (tolle deutsche Rechtschreibung)
					movement = distance(s, t);
		      s.location += checkmove (s, movement);
		      break;
		      
				case 4:		//disengage: Rückzug aus gegnerischer Waffenreichweite
					/**TODO Sicherstellen, daß Schiffe mit diesem Befehl nicht die dem
					 * Gegenr nächsten sind, ohne auf die Waffenreichweite des Feindes zu schielen.
					 * Solange Es nicht beschossen wurde und ein eignes Kampfschiff näher am gegner ist
					 * bleibt es an Ort und Stelle. Ansonsten fährt es eine Runde lang mit höchster
					 * Geschwindigkeit vom Gegner weg.**/
					
				default:	//withdraw: Flucht (Feiglinge!)
					s.location -= s.speed; 
			}
			
		}
		
		
	}
	
	private void newTargets(){
		/*
		 *Hier werden gegebenenfalls neue Gegner zugewiesen.
		 *Dies passiert nur, wenn das letzte Zielschiff nicht mehr das
		 *nächste ist.
		 */
	}
	
	private void shooting(){
		/*
		 *Hier wird der Beschuß der einzelnen Schiffe durchgeführt.
		 */
	}
	
	private void casulties(){
		/*
		 *Hier werden die Verluste entfernt und gegebenenfalls den nun
		 *ziellosen Schiffen neue Gegner zugewiesen.
		 */
	}
	
	
	
	public static void main(String[] args) {
		
		
	}
}
