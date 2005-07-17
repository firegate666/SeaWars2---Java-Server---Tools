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
public class battle {

	private ship[] a;				//Attacker
	private ship[] d;				//Defender
	private weapon[] arms;	//Bewaffnung
	double startdistance;
	double distance;
	double weather;
	double time;
	double sunrise;
	double sunset;
	
	private void startdistance(){
		startdistance = 1000;
	}
	
	private void targets(){
		
		//Hier wird initial jedem Schiff ein Gegner zugewiesen.
		
		if(a.length <= d.length)
			for(int i = 0; i < a.length; i++)
				a[i].target = d[i];
		else
			for(int i = 0; i < a.length; i++)
				a[i].target = d[i%d.length];
	}
	
	private void damage(){
		/*
		 *Hier werden die Auswirkungen des Schadens eines Schiffes.
		 *ermittelt.
		 */
															
	}
	
	private void movement(){
		/*
		 *Hier werden die Bewegungen der einzelnen Schiffe durchgeführt.
		 */
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
