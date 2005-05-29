/**
 * This Package allows you to spread a variable Number of Points
 * over a field of 1000 by 1000 Points
 * Copyright (C) 2005  Andreas Wagener
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * The Author can be contacted by writing an email to "hohlebirne@yahoo.de"
 *________________________________________________________________________________ 
 * 
 * VerteilungAusfuehren() aus Konstruktor entfernt
 * Programmaufruf nach instatiierung ausführen
 * besser Lösung statt überladene Konstruktoren wären getter und setter
 * 
 * letzte Änderung von Andreas am 25. Mai 2005
 */

package welterstellung;

import java.io.*;
import java.lang.Math;


public class Archipelverteilung
{
	private double[][] Karte;
	public Archipel[] Orte;
	public int Archipelzahl;
	
	private double empfindlichkeit = 0.5;
	private int Archipelanzahl = 1000;
	private String Bildpfad = "Blubber.bmp";
	
/**
 *  Erzeugt 1000 Archipel mit dem Bild "Blubber.bmp" und der Empfindlichkeit 0.5
 */	
	public Archipelverteilung()
	{
	}
	/**
	 * Erzeugt mit Archipel dem Bild Blubber.bmp und der Empfindlichkeit 0.5 
	 * @param Archipelanzahl Anzahl der Archipel
	 */
	public Archipelverteilung(int Archipelanzahl)
	{
		this.Archipelanzahl = Archipelanzahl;
	}
	/**
	 * Erzeugt Archipel mit der Empfindlichkeit 0.5.
	 * @param Archipelanzahl Anzahl der Archipel
	 * @param Bildpfad Pfad zu einem Bild im BMP-Format, 1000x1000 Pixel, 
	 * 8-Bit Graustufen
	 */
	public Archipelverteilung(int Archipelanzahl, String Bildpfad)
	{
		this(Archipelanzahl);
		this.Bildpfad = Bildpfad;
	}
	/**
	 * Erzeugt Archipelkoordinaten
	 * @param Archipelanzahl Anzahl der Archipel
	 * @param Bildpfad Pfad zu einem Bild im BMP-Format, 1000*1000 Pixel,
	 * 8-Bit Graustufen
	 * @param empfindlichkeit stellt ein, wie empfindlich die Verteilungsfunktion
	 * auf das BMP reagiert. Bei Werten von 1 oder höher sammeln sich fast alle 
	 * Archipel nur in den weißen Bereichen des BMP. Je kleiner der Wert, desto weniger
	 * hält sich die Verteilungsfunktion an die Vorgaben des BMP. Sinnvoll ist 0,5 bis
	 * 0,7 bei den meisten Bildern. Bei empfindlichkeit =0 wird das Bild nicht beachtet.
	 */
	public Archipelverteilung(int Archipelanzahl, String Bildpfad, double empfindlichkeit)
	{
		this(Archipelanzahl, Bildpfad);
		this.empfindlichkeit = empfindlichkeit;
	}
	
	
	public void VerteilungAusfuehren()
	{
		// Reading from defaults
		int Archipelanzahl = this.Archipelanzahl;
		String Bildpfad = this.Bildpfad;
		double empfindlichkeit = this.empfindlichkeit;
		
	    int[] Bildpunkte;
		Karte = new double[1000][1000];

		for (int i=0; i<1000; i++)
			for (int j=0; j<1000; j++)
			{
				//Karte mit Werten zwischen 0 und 1 füllen
				Karte[i][j]=Math.random();
			}
		//Bilddatei öffnen und auslesen
		try
		{
			FileInputStream Bild = new FileInputStream(Bildpfad);
			DataInputStream Bilddaten = new DataInputStream(Bild);
			//Reader in = new BufferedReader(Bilddaten);
			int ch;
			//Dateikopf ignorieren
			do
			{			
				while (Bilddaten.readUnsignedByte() != 255);
			} 
			while (Bilddaten.readUnsignedByte()!=255);
			Bilddaten.readUnsignedByte();
			Bilddaten.readUnsignedByte();
			//Mit dem auslesen anfangen
			int i=0;
			Bildpunkte = new int[1000000]; //Dynamisch wäre wünschenswert
			while ( i<Bildpunkte.length) {
				Bildpunkte[i++]=  Bilddaten.readUnsignedByte();
			}
			Bilddaten.close();
			Bild.close();
		}
		catch(FileNotFoundException e){
			System.out.println("Sorry, die Datei scheint nicht zu existieren.");
			System.out.println(e);
			return;
		}
		catch(IOException e){
			System.out.println("Sorry, die BMP-Datei scheint nicht ins Schema zu passen, sie ist " +
					"vielleicht zu klein?");
			return;
		}
		/* Nachdem nun hoffentlich ein Integer-Array des Bilds vorliegt, sollen die Archipel
		 * mit dem Array verrechnet werden. 
		 */
		if (empfindlichkeit > 0.01)
		{
			if (empfindlichkeit >3) empfindlichkeit =3;
			double Punkt=0;
			for (int i=0; i<1000; i++)
				for (int j=0; j<1000; j++)
				{
					/*Kartenwerte mit Bild multiplizieren, es ergeben sich weiterhin Werte zwischen 0 und 1
					Die mordskomplizierte Formel ergibt sich für die komfortable Empfindlichkeitseinstellung.
					Wenn man auf die Empfindlichkeitseinstellung verzichten kann und mag, dann kann der 
					Ort auf der Karte rechenzeitsparender mit diesen Zeilen aktualisiert werden:
					Punkt = Bildpunkt [j*1000+i];
					Karte[i][j] = Karte[i][j] * Punkt/256; 
					*/
					Punkt = ((double) Bildpunkte[j*1000+i])/256*(3/empfindlichkeit)-2;
					// Tanh mittels Exponentialfunktionen 
					Punkt = (Math.exp(2*Punkt)-1)/(Math.exp(2*Punkt)+1);
					// Kartenwertumrechnung
					Karte[i][j]=Karte[i][j] * (Punkt +1)/2; 
				}
		}
		//Archipel aus dem Boden heben, Werte zwischen 0 und "genauigkeit"
		final int genauigkeit = 100000;
		for (int i=0; i<1000; i++)
			for (int j=0; j<1000; j++)
			{
				Karte[i][j]=Karte[i][j]*genauigkeit;
			}
		//Histogramm erstellen
		int[] histogramm = new int[genauigkeit];
		for (int i=0; i<1000; i++)
			for (int j=0; j<1000; j++)
			{
				if ((Karte[i][j]<genauigkeit) && (Karte[i][j]>0))
					histogramm[(int)Karte[i][j]]++;
			}
		//Meeresspiegel für die perfekte Archipelanzahl aus dem Histogramm auslesen
		int Archipelzaehler = 0;
		int Meeresspiegel =0;
		for (int i=(genauigkeit -1); ((Meeresspiegel==0) && (i>0)); i--)
		{
			Archipelzaehler += histogramm[i];
			if (Archipelzaehler > Archipelanzahl)
				Meeresspiegel = i;
		}
		// Positionen aller Archipel, die über dem Meeresspiegel liegen, speichern
		Orte = new Archipel[Archipelzaehler+1];
		Archipelzahl = Archipelzaehler;
		Archipelzaehler = 0;
		int Archipelgroesse = 0;
		for (int i=0; i<1000; i++)
			for (int j=0; j<1000; j++)
			{
				if (Karte[i][j] > Meeresspiegel)
				{
					Archipelgroesse = (int) (Math.random()*5.49 + 0.5);					
					try{
						Orte[Archipelzaehler++] = new Archipel(i, j, Archipelgroesse );
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						System.out.println("Die Empfindlichkeit war zu niedrig. \n" +
								"Wahrscheinlich ist nicht die ganze Karte mit Archipeln bedeckt" +
								"worden.");
						return;
					}
				}
			}//for
	}//Archipel erzeugen
}//Klasse

