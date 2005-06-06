/**
 * Package zum testen der anderen Klassen.
 * Letzte Änderung von Andreas am 22. Mai 2005:
 * Aufrufe an die neuen Klassen in "Welterstellung" angepasst
 */

package test;
import welterstellung.Archipelverteilung;
import java.io.*;
import welterstellung.InselErstellung;
import welterstellung.ZufallsNamen;

class testen{
	public static void main(String[] args) {
		
 ZufallsNamen name = new ZufallsNamen();
 for (int i=0; i<1000; i++)System.out.println(name.setZufallsName());
 /* Test der Zufallsgenerierung über ein Histogramm
  int[] j= new int[] {0,0,0,0,0,0,0,0,0,0};
 for (int i=0; i<1000000; i++)
 {
 	j[(int) (Math.random()*10)]++;
 }
 for (int i=0; i<j.length; i++) System.out.println("j["+i+"]= " + j[i] + "\n");
	*/	

 /* Test der Positionsverteilung
  * 		String Inselpositionen = "";
		Archipelverteilung neueWelt;
		//neueWelt = new Archipelverteilung(1000,"C:\\Eigene Dateien\\C- Programme\\SeaWars\\Antichrist.bmp",0.5);
		neueWelt = new Archipelverteilung(100,"C:\\Eigene Dateien\\C- Programme\\SeaWars\\Blubber.bmp",0.5);
		neueWelt.VerteilungAusfuehren();
		
		for (int i=0; i<neueWelt.Archipelzahl; i++)
		{
			Inselpositionen = Inselpositionen + neueWelt.Orte[i].x + ", "+
			neueWelt.Orte[i].y + "\r\n";
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
        System.out.println("Fertig. Dieses Mal wurden " + neueWelt.Archipelzahl + " Inseln erzeugt.");
        */
	}
}