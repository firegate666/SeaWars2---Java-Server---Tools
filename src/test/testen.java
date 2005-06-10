/**
 * Package zum testen der anderen Klassen.
 * Letzte �nderung von Andreas am 22. Mai 2005:
 * Aufrufe an die neuen Klassen in "Welterstellung" angepasst
 */

package test;
import welterstellung.Archipelverteilung;
import java.io.*;
//import welterstellung.InselErstellung;
// import welterstellung.ZufallsNamen;

class testen{
	public static void main(String[] args) {
		
/*		
// ******************************************************************************************

  // Test des Namensgenerators
 for (int i=0; i<100; i++)
 	{
 	ZufallsNamen name = new ZufallsNamen();
 		System.out.println(name.setZufallsName());
 	}
// ******************************************************************************************
/*  */

/*
// ******************************************************************************************
	// Test der Zufallsgenerierung �ber ein Histogramm
   int[] j= new int[] {0,0,0,0,0,0,0,0,0,0};
   for (int i=0; i<1000000; i++)
   {
 		j[(int) (Math.random()*10)]++;
   }
   for (int i=0; i<j.length; i++) System.out.println("j["+i+"]= " + j[i] + "\n");
// *******************************************************************************************
/*	*/	

// *******************************************************************************************
  //Test der Positionsverteilung
   		String Inselpositionen = "";
		Archipelverteilung neueWelt;
		int Archipelzahl = 500;
		neueWelt = new Archipelverteilung(Archipelzahl,"C:\\Eigene Dateien\\C- Programme\\SeaWars\\Beispiel.bmp",0.7);
		//neueWelt = new Archipelverteilung(100,"C:\\Eigene Dateien\\C- Programme\\SeaWars\\Blubber.bmp",0.5);
		neueWelt.VerteilungAusfuehren();
		int inselzahl = 0;
//		int zeilenumbruch=0; String zeile = "";
		for (int i=0; i<neueWelt.Archipelzahl; i++)
		{
			int controller, kontrollzaehler=0;
			do{
				controller = neueWelt.Orte[i].inselnImArchipelVerteilen();
				kontrollzaehler++;
				if (kontrollzaehler > 1)System.out.println("kontrollzaehler = " + kontrollzaehler);
			}
			while(controller == 0);
//			System.out.println(neueWelt.Orte[i].inselAnzahl);
			if (i%10 ==0) System.out.println("         "+(int)((double)i*100/Archipelzahl) + "% ");
//			zeile = zeile + neueWelt.Orte[i].inselAnzahl + ";  ";
//			if (zeilenumbruch++ == 20) 
//				{
//				zeilenumbruch = 0;
//				System.out.println(zeile);
//				zeile = "";
//				}
			for (int j=0; j<neueWelt.Orte[i].inselAnzahl;j++)
			{
				Inselpositionen = Inselpositionen + neueWelt.Orte[i].insel[j].x_pos + ", "+
				neueWelt.Orte[i].insel[j].y_pos + ", "+
				neueWelt.Orte[i].insel[j].archipelID + ", "+
				neueWelt.Orte[i].insel[j].getName()+"\r\n";
				inselzahl++;
			}
		}

        try {
       		File file = new File("C:\\Eigene Dateien\\C- Programme\\SeaWars\\Inseln.txt");
        	FileOutputStream fout = new FileOutputStream(file);
        	fout.write(Inselpositionen.getBytes());
        	fout.close();
        	} catch (FileNotFoundException e) {
	            System.out.println("Sorry, die Positionsdatei konnte nicht erstellt werden");
        	} catch (IOException e) {
	            System.out.println("Sorry, die Positionsdatei konnte nicht erstellt werden");
        	}
        System.out.println("Fertig. Dieses Mal wurden " + inselzahl+ " Inseln erzeugt.");
/*	*/        
// *******************************************************************************************

	}

}