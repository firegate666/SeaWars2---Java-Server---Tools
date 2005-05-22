package test;
import welterstellung.Inselverteilung;
import java.io.*;

class testen{
	public static void main(String[] args) {
		String Inselpositionen = "";
		Inselverteilung neueWelt;
//		neueWelt = new Inselverteilung(700,"C:\\Eigene Dateien\\C- Programme\\SeaWars\\BlubberDart.bmp",0.5);
//		neueWelt = new Inselverteilung(1000,"C:\\Eigene Dateien\\C- Programme\\SeaWars\\3ArmGalaxis.bmp",0.3);
		neueWelt = new Inselverteilung(1000,"C:\\Eigene Dateien\\C- Programme\\SeaWars\\Antichrist.bmp",0.5);
		//System.out.println(Math.random());
//		System.out.println(neueWelt.Inselzahl);
		for (int i=0; i<neueWelt.Inselzahl; i++)
		{
			Inselpositionen = Inselpositionen + neueWelt.Orte[i].x + ", "+
			neueWelt.Orte[i].y + "\r\n";
		}
		//System.out.println(Inselpositionen);

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
        System.out.println("Fertig. Dieses Mal wurden " + neueWelt.Inselzahl + " Inseln erzeugt.");
	}
}