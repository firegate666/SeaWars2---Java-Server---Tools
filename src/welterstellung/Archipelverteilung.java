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
	 * auf das BMP reagiert. Bei Werten von 1 oder h�her sammeln sich fast alle
	 * Archipel nur in den wei�en Bereichen des BMP. Je kleiner der Wert, desto weniger
	 * h�lt sich die Verteilungsfunktion an die Vorgaben des BMP. Sinnvoll ist 0,5 bis
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
				//Karte mit Werten zwischen 0 und 1 f�llen
				Karte[i][j]=Math.random();
			}
		//Bilddatei �ffnen und auslesen
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
			Bildpunkte = new int[1000000]; //Dynamisch w�re w�nschenswert
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
					Die mordskomplizierte Formel ergibt sich f�r die komfortable Empfindlichkeitseinstellung.
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
		//meeresspiegel f�r die perfekte Archipelanzahl aus dem Histogramm auslesen
		int archipelZaehler = 0;
		int meeresspiegel =0;
		for (int i=(genauigkeit -1); ((meeresspiegel==0) && (i>0)); i--)
		{
			archipelZaehler += histogramm[i];
			if (archipelZaehler > Archipelanzahl)
				meeresspiegel = i;
		}

		//Inseln generieren, Schritt 4: Positionen aller Inseln mit (h�he > meeresspiegel) auslesen,
		//Abst�nde kontrollieren, notfalls Inseln l�schen und meeresspiegel senken
		{ //Neuen Bereich anfangen, damit der saugro�e Abstandsoperator wieder gel�scht wird,
		  //wenn er nicht mehr gebraucht wird
		int[][] abstandsOperator =
		{{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
		};
		this.Archipelzahl = archipelZaehler;
		archipelZaehler = 0;
		meeresspiegel++;
		do
		{
			meeresspiegel--;
			archipelZaehler=0;
			for (int i=0; i<1000; i++)
				for (int j=0; j<1000; j++)
				{
					if (Karte[i][j] > meeresspiegel)
					{
						archipelZaehler++; //Insel gefunden! Z�hlen!
						for (int k =0; k<25; k++)
							for (int l = 0; l<25; l++)
							{
								try{
									//Nachbarschaft eliminieren
									Karte[i-12+k][j-12+l] *= abstandsOperator[k][l];
								}
								catch (ArrayIndexOutOfBoundsException e)
								{
									//Nichts schlimmes ist passiert. Nur der abstandsOperator
									//wurde am Rand des Archipels ausgef�hrt. Programm darf
									//ganz normal weiter laufen. Zum Vermeiden dieses Fehlers
									//k�nnte man das Archipel um jeweils eine blinde Reihe
									//an jedem Rand erweitern...
								}
							}
					}
				}//for
		}
		while(archipelZaehler < Archipelzahl && meeresspiegel > 0);
		this.Archipelzahl = archipelZaehler;

		// Wenn der meeresspiegel auf 0 sinkt, ist die Inselherstellung misslungen und muss
		// wiederholt werden.
		if (meeresspiegel ==0)
		{
			System.out.println("Meeresspiegel auf Null gesunken, defekte Weltkarte erzeugt");
		}
		} //Abstandsoperator-Bereich beenden

		// Positionen aller Archipel, die �ber dem meeresspiegel liegen, speichern
		Orte = new Archipel[archipelZaehler+1];
		Archipelzahl = archipelZaehler;
		archipelZaehler = 0;
		int Archipelgroesse = 0;
		int archipelID = 0;
		for (int i=0; i<1000; i++)
			for (int j=0; j<1000; j++)
			{
				if (Karte[i][j] > meeresspiegel)
				{
					Archipelgroesse = (int) (Math.random()*5);
					try{
						Orte[archipelZaehler++] = new Archipel(i, j, Archipelgroesse, 0, archipelID++ );
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						System.out.println("Wahrscheinlich ist nicht die ganze Karte mit Archipeln bedeckt" +
								"worden. Dieser Fehler ist aus der Klasse 'Archipelverteilung'.");
						return;
					}
				}
			}//for
	}//Archipel erzeugen
}//Klasse
