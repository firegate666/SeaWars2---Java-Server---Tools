package welterstellung;

public class Archipel
{
	public int x;
	public int y;
	public int groesse;
	public int kartenabschnitt_id;
	public Archipel(int x, int y)
	{
		this.x = x;
		this.y = y;
		groesse = 3;
	}
	/**
	 * Diese Klasse beschreibt ein Archipel so, dass damit auch Datenbankeintr�ge
	 * generiert werden k�nnen.
	 * @param x gibt die x-Koordinate auf der Karte an.
	 * @param y gibt die y-Koordinate auf der Karte an.
	 * @param groesse gibt an, wie gro� und fortgeschritten Inseln dieses Archipels
	 * sein k�nnen
	 */
	public Archipel (int x, int y, int groesse)
	{
		this.x = x;
		this.y = y;
		this.groesse = groesse;
		this.kartenabschnitt_id = 1;
	}
	/**
	 * Diese Klasse beschreibt ein Archipel so, dass damit auch Datenbankeintr�ge
	 * generiert werden k�nnen.
	 * @param x gibt die x-Koordinate auf der Karte an.
	 * @param y gibt die y-Koordinate auf der Karte an.
	 * @param groesse gibt an, wie gro� und fortgeschritten Inseln dieses Archipels
	 * @param kartenabschnitt_id gibt an, in welchem Kartenabschnitt dieses Archipel zu finden ist.
	 * sein k�nnen
	 */
	public Archipel (int x, int y, int groesse, int kartenabschnitt_id)
	{
		this.x = x;
		this.y = y;
		this.groesse = groesse;
		this.kartenabschnitt_id = kartenabschnitt_id;
	}
	
	/**
	 * Setzt die Position einer Insel in Abh�ngigkeit von ihrer Archipelposition.
	 * Dabei werden je nach Gr��enklasse verschiedene Verteilungen angestrebt.
	 * @param ArchipelX ist die Zentrale Postion des Archipels in X-Richtung
	 * @param ArchipelY ist die Zentrale Position des Archipels in Y-Richtung
	 * @param groessenklasse ist die Gr��enklasse des Archipels
	 * @return gibt die Anzahl der Inseln zur�ck, wenn alles geklappt hat.
	 */
	public int inselnImArchipelVerteilen(int ArchipelX, int ArchipelY, int groessenklasse)
	{
		int archipelGebiet = 13;
		// Der AtollOperator kann verwendet werden, wenn ein Atoll gew�nscht wird.
		// Multipliziert man ihn mit einem zuf�llig gef�llten Archipelgebiet, 
		// bleibt nur noch der Atollring �brig.
		int[][] atollOperator =
		{{0,0,0,0,0,1,1,1,0,0,0,0,0},
		 {0,0,0,1,1,1,1,1,1,1,0,0,0},
		 {0,0,1,1,1,1,1,1,1,1,1,0,0},
		 {0,1,1,1,1,0,0,0,1,1,1,1,0},
		 {0,1,1,1,0,0,0,0,0,1,1,1,0},
		 {1,1,1,0,0,0,0,0,0,0,1,1,1},
		 {1,1,1,0,0,0,0,0,0,0,1,1,1},
		 {1,1,1,0,0,0,0,0,0,0,1,1,1},
		 {0,1,1,1,0,0,0,0,0,1,1,1,0},
		 {0,1,1,1,1,0,0,0,1,1,1,1,0},
		 {0,0,1,1,1,1,1,1,1,1,1,0,0},
		 {0,0,0,1,1,1,1,1,1,1,0,0,0},
		 {0,0,0,0,0,1,1,1,0,0,0,0,0}};
		
		int [][] abstandsOperator =
		{{0,0,0},
		 {0,1,0},
		 {0,0,0}};
		
	//	double winkel;  // im Bogenma�, nat�rlich
	//	double abstand; // in Feldern, von 1 bis 6
		
		// Archipelgebiet mit Zufallszahlen f�llen
		double [][] Archipel = new double[archipelGebiet][archipelGebiet];
		for (int i = 0; i<archipelGebiet; i++)
			for (int j =0; j< archipelGebiet; j++)
			{
				Archipel[i][j]=(Math.random());
			}
		if (groessenklasse == 1)
		{
			for (int i = 0; i<archipelGebiet; i++)
				for (int j =0; j<archipelGebiet; j++)
				{
					Archipel[i][j]=Archipel[i][j] * (double)atollOperator[i][j];
				}
		}
		
		final int genauigkeit = 1000;
		for (int i=0; i<archipelGebiet; i++)
			for (int j=0; j<archipelGebiet; j++)
			{
				Archipel[i][j]=Archipel[i][j]*genauigkeit;
			}
		//Histogramm erstellen
		int[] histogramm = new int[genauigkeit];
		for (int i=0; i<archipelGebiet; i++)
			for (int j=0; j<archipelGebiet; j++)
			{
				if ((Archipel[i][j]<genauigkeit) && (Archipel[i][j]>0))
					histogramm[(int)Archipel[i][j]]++;
			}
		//Meeresspiegel f�r die perfekte Inselanzahl aus dem Histogramm auslesen
		int Inselzaehler = 0;
		int Meeresspiegel =0;
		int Inselanzahl = (int) Math.random()*15;
		for (int i=(genauigkeit -1); ((Meeresspiegel==0) && (i>0)); i--)
		{
			Inselzaehler += histogramm[i];
			if (Inselzaehler > Inselanzahl)
				Meeresspiegel = i;
		}

		
		return Inselzaehler;
	}


}