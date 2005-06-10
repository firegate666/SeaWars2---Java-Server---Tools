package welterstellung;
/**
 *	Diese Klasse stellt alle Datenbankrelevanten Werte für ein Archipel zur Verfügung und
 *	bietet obendrein die Möglichkeit, neue Archipele zu erstellen. Dabei werden verschiedene
 *  Archipel-Operatoren angeboten, mit denen man das Aussehen eines Archipels verändern kann.
 *  letzte Änderung von Andreas am 25. Mai 2005
 */
public class Archipel
{
	public int x;
	public int y;
	public int groesse;
	public int kartenabschnitt_id;
	public int inselAnzahl;
	public InselErstellung[] insel;
	private int archipelID;
	
	public Archipel(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.groesse = 3;
		this.archipelID = 0;
	}
	/**
	 * Diese Klasse beschreibt ein Archipel so, dass damit auch Datenbankeinträge
	 * generiert werden können.
	 * @param x gibt die x-Koordinate auf der Karte an.
	 * @param y gibt die y-Koordinate auf der Karte an.
	 * @param groesse gibt an, wie groß und fortgeschritten Inseln dieses Archipels
	 * sein können und liegt zwischen 1 und 5
	 */
	public Archipel (int x, int y, int groesse)
	{
		this.x = x;
		this.y = y;
		this.groesse = groesse;
		this.kartenabschnitt_id = 1;
		this.archipelID = 0;
	}
	/**
	 * Diese Klasse beschreibt ein Archipel so, dass damit auch Datenbankeinträge
	 * generiert werden können.
	 * @param x gibt die x-Koordinate auf der Karte an.
	 * @param y gibt die y-Koordinate auf der Karte an.
	 * @param groesse gibt an, wie groß und fortgeschritten Inseln dieses Archipels
	 * @param kartenabschnitt_id gibt an, in welchem Kartenabschnitt dieses Archipel zu finden ist.
	 * sein können und liegt zwischen 1 und 5
	 */
	public Archipel (int x, int y, int groesse, int kartenabschnitt_id)
	{
		this.x = x;
		this.y = y;
		this.groesse = groesse;
		this.kartenabschnitt_id = kartenabschnitt_id;
		this.archipelID = 0;
	}
	/**
	 * Diese Klasse beschreibt ein Archipel so, dass damit auch Datenbankeinträge
	 * generiert werden können.
	 * @param x gibt die x-Koordinate auf der Karte an.
	 * @param y gibt die y-Koordinate auf der Karte an.
	 * @param groesse gibt an, wie groß und fortgeschritten Inseln dieses Archipels
	 * @param kartenabschnitt_id gibt an, in welchem Kartenabschnitt dieses Archipel zu finden ist.
	 * sein können und liegt zwischen 1 und 5
	 */
	public Archipel (int x, int y, int groesse, int kartenabschnitt_id, int archipelID)
	{
		this.x = x;
		this.y = y;
		this.groesse = groesse;
		this.kartenabschnitt_id = kartenabschnitt_id;
		this.archipelID = archipelID;
	}
	
	/**
	 * Setzt die Position einer Insel in Abhängigkeit von ihrer Archipelposition.
	 * Dabei werden je nach Größenklasse verschiedene Verteilungen angestrebt.
	 * @param ArchipelX ist die Zentrale Postion des Archipels in X-Richtung
	 * @param ArchipelY ist die Zentrale Position des Archipels in Y-Richtung
	 * @param groessenklasse ist die Größenklasse des Archipels
	 * @return gibt die Anzahl der Inseln zurück, wenn alles geklappt hat.
	 * Wenn 0 zurückgegeben wird, muss die Inselherstellung wiederholt werden.
	 */
	public int inselnImArchipelVerteilen()
	{
		return inselnImArchipelVerteilen(this.x, this.y, this.groesse);
	}

	public int inselnImArchipelVerteilen(int ArchipelX, int ArchipelY, int groessenklasse)
	{
		int archipelGebiet = 13;
		// Der AtollOperator kann verwendet werden, wenn ein Atoll gewünscht wird.
		// Multipliziert man ihn mit einem zufällig gefüllten Archipelgebiet, 
		// bleibt nur noch der Atollring übrig.
		// Er muss genauso hoch und breit sein wie das archipelGebiet.
		int[][][] archipelOperator ={
		{{0,0,0,0,1,1,1,1,1,0,0,0,0},
		 {0,0,1,1,1,1,1,1,1,1,1,0,0},
		 {0,1,1,1,0,0,0,0,0,1,1,1,0},
		 {0,1,1,0,0,0,0,0,0,0,1,1,0},
		 {1,1,0,0,0,0,0,0,0,0,0,1,1},
		 {1,1,0,0,0,0,0,0,0,0,0,1,1},
		 {1,1,0,0,0,0,0,0,0,0,0,1,1},
		 {1,1,0,0,0,0,0,0,0,0,0,1,1},
		 {1,1,0,0,0,0,0,0,0,0,0,1,1},
		 {0,1,1,0,0,0,0,0,0,0,1,1,0},
		 {0,1,1,1,0,0,0,0,0,1,1,1,0},
		 {0,0,1,1,1,1,1,1,1,1,1,0,0},
		 {0,0,0,0,1,1,1,1,1,0,0,0,0}},
		
		{{0,0,0,0,0,0,0,0,0,0,0,0,0},
		 {0,0,0,1,1,1,1,1,1,0,0,0,0},
		 {0,0,1,1,1,1,1,1,1,1,1,0,0},
		 {0,1,1,1,1,1,1,1,1,1,1,0,0},
		 {0,1,1,1,1,1,1,1,1,1,1,1,0},
		 {0,1,1,1,1,1,1,1,1,1,1,1,0},
		 {0,1,1,1,1,1,1,1,1,1,1,1,0},
		 {0,1,1,1,1,1,1,1,1,1,1,1,0},
		 {0,1,1,1,1,1,1,1,1,1,1,1,0},
		 {0,0,1,1,1,1,1,1,1,1,1,0,0},
		 {0,0,1,1,1,1,1,1,1,1,1,0,0},
		 {0,0,0,0,1,1,1,1,1,0,0,0,0},
		 {0,0,0,0,0,0,0,0,0,0,0,0,0}},

		{{1,1,1,1,0,0,0,0,0,0,0,0,0},
		 {1,1,1,1,1,1,1,0,0,0,0,0,0},
		 {1,1,1,1,1,1,1,1,0,0,0,0,0},
		 {0,1,1,1,1,1,1,1,1,0,0,0,0},
		 {0,1,1,1,1,1,1,1,1,1,0,0,0},
		 {0,1,1,1,1,1,1,1,1,1,1,0,0},
		 {0,1,1,1,1,1,1,1,1,1,1,1,0},
		 {0,1,1,1,1,1,1,1,1,1,1,1,0},
		 {0,0,0,1,1,1,1,1,1,1,1,1,0},
		 {0,0,0,0,0,0,1,1,1,1,1,1,1},
		 {0,0,0,0,0,0,1,1,1,1,1,1,1},
		 {0,0,0,0,0,0,1,1,1,1,1,1,1},
		 {0,0,0,0,0,0,0,0,0,1,1,1,1}},

		{{0,0,0,0,0,0,0,0,0,1,1,0,0},
		 {0,0,0,0,0,0,1,1,1,1,1,1,0},
		 {0,0,0,0,0,1,1,1,1,1,1,1,1},
		 {0,0,0,0,1,1,1,1,1,1,1,0,0},
		 {0,0,0,1,1,1,1,1,1,1,0,0,0},
		 {0,1,1,1,1,1,1,1,1,0,0,0,0},
		 {1,1,1,1,1,1,1,1,0,0,0,0,0},
		 {0,1,1,1,1,1,1,1,1,0,0,0,0},
		 {0,0,0,1,1,1,1,1,1,1,0,0,0},
		 {0,0,0,0,0,1,1,1,1,1,1,0,0},
		 {0,0,0,0,0,0,1,1,1,1,1,1,0},
		 {0,0,0,0,0,0,0,1,1,1,1,1,0},
		 {0,0,0,0,0,0,0,0,0,1,1,0,0}},};

		int [][] abstandsOperator =
		{{0,0,0},
		 {0,1,0},
		 {0,0,0}};
		int archipelwaehler = 0;
		
		// Archipelgebiet mit Zufallszahlen füllen
		double [][] Archipel = new double[archipelGebiet][archipelGebiet];
		for (int i = 0; i<archipelGebiet; i++)
			for (int j =0; j< archipelGebiet; j++)
			{
				Archipel[i][j]=(Math.random());
			}
		// Je nach Größenklasse verchiedene Dinge mit dem Archipelgebiet anstellen
		archipelwaehler = (int) (Math.random()*4-2+this.groesse);
		if (archipelwaehler < 0) archipelwaehler = 0;
		if (archipelwaehler >= archipelOperator.length-1) archipelwaehler = archipelOperator.length-1;
		
		int operatorDrehung;
		if (archipelwaehler > 1)operatorDrehung = (int) (Math.random()*4);
		else operatorDrehung = 0;
		
		int[][] operator = new int[13][13];
		switch (operatorDrehung)
		{
		case 0:{
			for (int i=0; i< operator.length; i++)
			{
				for (int j=0; j< operator.length; j++)
				{
					operator[i][j] = archipelOperator[archipelwaehler][i][j];
				}
			}
			break;
		}
		case 1: {//90° drehen
			for (int i=0; i< operator.length; i++)
			{
				for (int j=0; j< operator.length; j++)
				{
					operator[j][operator.length-1-i] = archipelOperator[archipelwaehler][i][j];
				}
			}

			break;
			}
		case 2: {//180° drehen
			for (int i=0; i< operator.length; i++)
			{
				for (int j=0; j< operator.length; j++)
				{
					operator[operator.length -1-i][j] = archipelOperator[archipelwaehler][i][j];
				}
			}
			break;
			}
		case 3: {//270° drehen
			for (int i=0; i< operator.length; i++)
			{
				for (int j=0; j< operator.length; j++)
				{
					operator[operator.length -1-j][i] = archipelOperator[archipelwaehler][i][j];
				}
			}
			break;
			}
		default: {
			for (int i=0; i< operator.length; i++)
			{
				for (int j=0; j< operator.length; j++)
				{
					operator[i][j] = archipelOperator[archipelwaehler][i][j];
				}
			}
			break;
		}
		}
		
		// Jetzt wird das Archipelgebiet mit dem Operator getrimmt, so kann das Aussehen bestimmt werden.
		for (int i = 0; i<archipelGebiet; i++)
			for (int j =0; j<archipelGebiet; j++)
			{
				Archipel[i][j]=Archipel[i][j] * operator[i][j];
			}
	
		// Zur Sache: Inseln generieren, Schritt 1
		final int genauigkeit = 10000;
		for (int i=0; i<archipelGebiet; i++)
			for (int j=0; j<archipelGebiet; j++)
			{
				Archipel[i][j]=Archipel[i][j]*genauigkeit;
			}

		//Inseln generieren, Schritt 2: Histogramm erstellen
		int[] histogramm = new int[genauigkeit];
		for (int i=0; i<archipelGebiet; i++)
			for (int j=0; j<archipelGebiet; j++)
			{
				if ((Archipel[i][j]<genauigkeit) && (Archipel[i][j]>0))
					histogramm[(int)Archipel[i][j]]++;
			}

		//Inseln generieren, Schritt 3: Meeresspiegel für die perfekte 
		//Inselanzahl aus dem Histogramm auslesen
		int inselZaehler = 0;
		int meeresspiegel =0;
		int inselAnzahl = (int) (Math.random()*8)+8;
		for (int i=(genauigkeit -1); ((meeresspiegel==0) && (i>0)); i--)
		{
			inselZaehler += histogramm[i];
			if (inselZaehler > inselAnzahl)
				meeresspiegel = i;
		}
		if (meeresspiegel == 0) return 0;
		//Inseln generieren, Schritt 4: Positionen aller Inseln mit (höhe > meeresspiegel) auslesen, 
		//Abstände kontrollieren, notfalls Inseln löschen und Meeresspiegel senken
		this.inselAnzahl = inselZaehler;
		inselZaehler = 0;
		meeresspiegel++; 
		do
		{
			meeresspiegel--;

			inselZaehler=0;
			for (int i=0; i<archipelGebiet; i++)
				for (int j=0; j<archipelGebiet; j++)
				{
					if (Archipel[i][j] > meeresspiegel)
					{
						inselZaehler++; //Insel gefunden! Zählen!
						for (int k =0; k<3; k++)
							for (int l = 0; l<3; l++)
							{
								try{
									//Nachbarschaft eliminieren
									Archipel[i-1+k][j-1+l] *= abstandsOperator[k][l];
								}
								catch (ArrayIndexOutOfBoundsException e)
								{
									//Nichts schlimmes ist passiert. Nur der abstandsOperator
									//wurde am Rand des Archipels ausgeführt. Programm darf
									//ganz normal weiter laufen. Zum Vermeiden dieses Fehlers
									//könnte man das Archipel um jeweils eine blinde Reihe
									//an jedem Rand erweitern...
								}
							}
					}
				}//for
		}
		while(inselZaehler < inselAnzahl || meeresspiegel <= 1);
		this.inselAnzahl = inselZaehler;
		System.gc();		
		// Wenn der Meeresspiegel auf 0 sinkt, ist die Inselherstellung misslungen und muss
		// wiederholt werden.
		if (meeresspiegel <=1) 
		{
			System.out.println("Meeresspiegel auf Null gesunken, defektes Archipel erzeugt");
			return 0;
		}
		//Inseln generieren, Schritt 5: Inselobjekte erzeugen
		int inselGroesse;
		insel = new InselErstellung[inselZaehler];
		inselZaehler=0;
		for (int i=0; i<archipelGebiet; i++)
			for (int j=0; j<archipelGebiet; j++)
			{
				if (Archipel[i][j] > meeresspiegel)
				{
					inselGroesse = (int) (Math.random()*this.groesse);					
					try{
						insel[inselZaehler++] = new InselErstellung(i, j, this.groesse, this.archipelID);
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						System.out.println("Wahrscheinlich ist nicht die ganze Karte mit Inseln bedeckt" +
								"worden. Dieser Fehler kommt aus der Klasse 'Archipel'.");
						return 0;
					}
				}
			}//for

		
		//Inseln mittels der Archipelposition an den korrekten Platz auf der Karte rücken
		if (this.x < (int)(archipelGebiet/2))		this.x = (int)(archipelGebiet/2);
		if (this.y < (int)(archipelGebiet/2))		this.y = (int)(archipelGebiet/2);
		if (this.x > 999 - (int)(archipelGebiet/2))	this.x = (int)(archipelGebiet/2);
		if (this.y > 999 - (int)(archipelGebiet/2))	this.y = (int)(archipelGebiet/2);
		
		for (int i=0; i<this.inselAnzahl;i++)
		{
			insel[i].x_pos = insel[i].x_pos-(int)(archipelGebiet/2)+this.x;
			insel[i].y_pos = insel[i].y_pos-(int)(archipelGebiet/2)+this.y;
		}
		return inselZaehler;
	}
}