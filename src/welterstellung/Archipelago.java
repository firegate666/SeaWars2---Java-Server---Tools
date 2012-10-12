package welterstellung;
/**
 *	Diese Klasse stellt alle Datenbankrelevanten Werte für ein Archipelago zur Verfügung und
 *	bietet obendrein die Möglichkeit, neue Archipele zu erstellen. Dabei werden verschiedene
 *  Archipelago-Operatoren angeboten, mit denen man das Aussehen eines Archipels verändern kann.
 *  letzte Änderung von Andreas am 25. Mai 2005
 */
public class Archipelago
{
	public int x;
	public int y;
	public int magnitude;
	public int mapSectionId;
	public int numberOfIslands;
	public IslandCreator[] island;
	public int archipelagoId;

	public Archipelago()
	{
		this.x = 0;
		this.y = 0;
		this.magnitude = 1;
		this.mapSectionId = 1;
		this.numberOfIslands = 10;
		this.archipelagoId = 0;
	}

	public Archipelago(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.magnitude = 3;
		this.archipelagoId = 0;
	}
	/**
	 * Diese Klasse beschreibt ein Archipelago so, dass damit auch Datenbankeinträge
	 * generiert werden können.
	 * @param x gibt die x-Koordinate auf der Karte an.
	 * @param y gibt die y-Koordinate auf der Karte an.
	 * @param magnitude gibt an, wie groß und fortgeschritten Inseln dieses Archipels
	 * sein können und liegt zwischen 1 und 5
	 */
	public Archipelago (int x, int y, int groesse)
	{
		this.x = x;
		this.y = y;
		this.magnitude = groesse;
		this.mapSectionId = 1;
		this.archipelagoId = 0;
	}
	/**
	 * Diese Klasse beschreibt ein Archipelago so, dass damit auch Datenbankeinträge
	 * generiert werden können.
	 * @param x gibt die x-Koordinate auf der Karte an.
	 * @param y gibt die y-Koordinate auf der Karte an.
	 * @param magnitude gibt an, wie groß und fortgeschritten Inseln dieses Archipels
	 * @param mapSectionId gibt an, in welchem Kartenabschnitt dieses Archipelago zu finden ist.
	 * sein können und liegt zwischen 1 und 5
	 */
	public Archipelago (int x, int y, int groesse, int kartenabschnitt_id)
	{
		this.x = x;
		this.y = y;
		this.magnitude = groesse;
		this.mapSectionId = kartenabschnitt_id;
		this.archipelagoId = 0;
	}
	/**
	 * Diese Klasse beschreibt ein Archipelago so, dass damit auch Datenbankeinträge
	 * generiert werden können.
	 * @param x gibt die x-Koordinate auf der Karte an.
	 * @param y gibt die y-Koordinate auf der Karte an.
	 * @param magnitude gibt an, wie groß und fortgeschritten Inseln dieses Archipels
	 * @param mapSectionId gibt an, in welchem Kartenabschnitt dieses Archipelago zu finden ist.
	 * sein können und liegt zwischen 1 und 5
	 */
	public Archipelago (int x, int y, int groesse, int kartenabschnitt_id, int archipelID)
	{
		this.x = x;
		this.y = y;
		this.magnitude = groesse;
		this.mapSectionId = kartenabschnitt_id;
		this.archipelagoId = archipelID;
	}

	/**
	 * Setzt die Positionen einiger Inseln in Abhängigkeit von ihrer Archipelposition.
	 * Dabei werden je nach Größenklasse verschiedene Verteilungen angestrebt.
	 * @return gibt die Anzahl der Inseln zurück, wenn alles geklappt hat.
	 * Wenn 0 zurückgegeben wird, muss die Inselherstellung wiederholt werden.
	 */
	public int inselnImArchipelVerteilen()
	{
		return inselnImArchipelVerteilen(this.x, this.y, this.magnitude);
	}

	/**
	 * Setzt die Positionen einiger Inseln in Abhängigkeit von ihrer Archipelposition.
	 * Dabei werden je nach Größenklasse verschiedene Verteilungen angestrebt.
	 * @param ArchipelX ist die Zentrale Postion des Archipels in X-Richtung
	 * @param ArchipelY ist die Zentrale Position des Archipels in Y-Richtung
	 * @param groessenklasse ist die Größenklasse des Archipels
	 * @return gibt die Anzahl der Inseln zurück, wenn alles geklappt hat.
	 * Wenn 0 zurückgegeben wird, muss die Inselherstellung wiederholt werden.
	 */
	public int inselnImArchipelVerteilen(int ArchipelX, int ArchipelY, int groessenklasse)
	{

		int archipelGebiet = 13;
		/* Der AtollOperator kann verwendet werden, wenn ein Atoll gew�nscht wird.
		 * Multipliziert man ihn mit einem zuf�llig gef�llten Archipelgebiet,
		 * bleibt nur noch der Atollring �brig.
		 * Er muss genauso hoch und breit sein wie das archipelGebiet.
		 * Wenn weitere Archipeloperatoren gew�nscht werden, tut euch keinen Zwang an, nur
		 * zu. Es muss beachtet werden, dass er 13x13 Felder gro� ist und ausreichend viele
		 * Einsen enth�lt, um 8 bis 15 Inseln darauf unterzubringen. Eine Insel verbraucht
		 * Aufgrund des Abstandsoperators im Schnitt 4.5 Einsen. Da die Inselverteilung zu-
		 * f�llig l�uft, sollte jedoch auf gar keinen Fall derartig knapp kalkuliert werden.
		 *
		 * Derzeit sind folgende Operatoren eingebaut:
		 * Der erste Operator erzeugt einen typischen Atollring.
		 * Der zweite Operator stellt im Prinzip den vollen Archipelplatz zur Verf�gung, ist
		 * jedoch abgerundet, damit den Spielern die Ecken nicht so ins Auge fallen.
		 * Der dritte Operator erzeugt ein schmaleres, langgestrecktes Archipelago, das schr�g
		 * liegt.
		 * Der vierte Operator erzeugt ein pfeilf�rmiges Archipelago.
		 */
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
		int i, j;

		// Archipelgebiet mit Zufallszahlen f�llen
		double [][] archipel = new double[archipelGebiet][archipelGebiet];
		for (i = 0; i<archipelGebiet; i++)
			for (j =0; j< archipelGebiet; j++)
			{
				archipel[i][j]=(Math.random());
			}
		// Je nach Gr��enklasse verchiedene Dinge mit dem Archipelgebiet anstellen.
		archipelwaehler = (int) (Math.random()*4-2+this.magnitude);
		if (archipelwaehler < 0) archipelwaehler = 0;
		if (archipelwaehler >= archipelOperator.length-1) archipelwaehler = archipelOperator.length-1;

		/* Der Archipeloperator wurde ausgew�hlt. Im Folgenden wird der Archipeloperator gedreht, damit
		 * die Karte nicht ganz so eint�nig aussieht und trotzdem nicht dutzende Operatoren im Quelltext
		 * stehen m�ssen.
		 */
		int operatorDrehung;
		if (archipelwaehler > 1)operatorDrehung = (int) (Math.random()*4);
		else operatorDrehung = 0;
		int[][] operator = new int[13][13];
		switch (operatorDrehung)
		{
		case 0:{//Nicht drehen
			for (i=0; i< operator.length; i++)
			{
				for (j=0; j< operator.length; j++)
				{
					operator[i][j] = archipelOperator[archipelwaehler][i][j];
				}
			}
			break;
		}
		case 1: {//90� drehen
			for (i=0; i< operator.length; i++)
			{
				for (j=0; j< operator.length; j++)
				{
					operator[j][operator.length-1-i] = archipelOperator[archipelwaehler][i][j];
				}
			}

			break;
			}
		case 2: {//180� drehen
			for (i=0; i< operator.length; i++)
			{
				for (j=0; j< operator.length; j++)
				{
					operator[operator.length -1-i][j] = archipelOperator[archipelwaehler][i][j];
				}
			}
			break;
			}
		case 3: {//270� drehen
			for (i=0; i< operator.length; i++)
			{
				for (j=0; j< operator.length; j++)
				{
					operator[operator.length -1-j][i] = archipelOperator[archipelwaehler][i][j];
				}
			}
			break;
			}
		default: { //Nicht drehen
			for (i=0; i< operator.length; i++)
			{
				for (j=0; j< operator.length; j++)
				{
					operator[i][j] = archipelOperator[archipelwaehler][i][j];
				}
			}
			break;
		}
		}// Switch

		/* Jetzt wird das Archipelgebiet mit dem Operator getrimmt, so kann das Aussehen bestimmt werden.
		 * Zus�tzlich wird das Archipelago um jeweils eine blinde Reihe rundherum erweitert, damit der Ab-
		 * standsoperator keine ArrayIndexOutOfBoundsExceptions hervorruft, wenn er ganz am Rand aus-
		 * gef�hrt wird. Das Verfahren beschleunigt die Inselherstellung ein wenig im Vergleich zur Try/-
		 * Catch-Arithmetik, die f�r die Exceptions notwendig w�re.
		 */
		double[][]archipelErweitert = new double[15][15];
		for (i = 0; i<archipelGebiet; i++)
			for (j =0; j<archipelGebiet; j++)
			{
				archipelErweitert[i+1][j+1]= archipel[i][j] * operator[i][j];
			}

		// Zur Sache: Inseln generieren, Schritt 1, Inseln aus dem Boden heben
		final int genauigkeit = 10000;
		for (i=1; i<=archipelGebiet; i++)
			for (j=1; j<=archipelGebiet; j++)
			{
				archipelErweitert[i][j]=archipelErweitert[i][j]*genauigkeit;
			}

		//Inseln generieren, Schritt 2: Histogramm erstellen
		int[] histogramm = new int[genauigkeit];
		for (i=1; i<=archipelGebiet; i++)
			for (j=1; j<=archipelGebiet; j++)
			{
				if ((archipelErweitert[i][j]<genauigkeit) && (archipelErweitert[i][j]>0))
					histogramm[(int)archipelErweitert[i][j]]++;
			}

		//Inseln generieren, Schritt 3: Meeresspiegel f�r die perfekte
		//Inselanzahl aus dem Histogramm auslesen
		int inselZaehler = 0;
		int meeresspiegel =0;
		int inselAnzahl = (int) (Math.random()*8)+7;
		for (i=(genauigkeit -1); ((meeresspiegel==0) && (i>0)); i--)
		{
			inselZaehler += histogramm[i];
			if (inselZaehler > inselAnzahl)
				meeresspiegel = i;
		}
		// TODO: Wenn der Fall "meeresspiegel == 0" auftritt, sind zu wenig Inseln in dem Archipelago
		// erzeugt worden. Derzeit wird einfach abgebrochen, das Wiederholen der Methode wird der
		// aufrufenden Klasse �berlassen. Hier sollte noch nachgebessert werden!
		if (meeresspiegel <= 0){
			System.out.println("Meeresspiegel auf 0 gefallen. Keine Inseln erzeugt.");
			return 0;

		}

		//Inseln generieren, Schritt 4: Positionen aller Inseln mit (h�he > meeresspiegel) auslesen,
		//Abst�nde kontrollieren, notfalls Inseln l�schen und Meeresspiegel senken
		this.numberOfIslands = inselZaehler;
		inselZaehler = 0;
		meeresspiegel++;
		do
		{
			meeresspiegel--;
			inselZaehler=0;
			for (i=1; i<=archipelGebiet; i++)
				for (j=1; j<=archipelGebiet; j++)
				{
					if (archipelErweitert[i][j] > meeresspiegel)
					{
						inselZaehler++; //Insel gefunden! Z�hlen!
						for (int k =0; k<3; k++)
							for (int l = 0; l<3; l++)
							{
								//Nachbarschaft eliminieren
								archipelErweitert[i-1+k][j-1+l] *= abstandsOperator[k][l];
							}
					}
				}//for
		}
		while(inselZaehler < inselAnzahl && meeresspiegel >= 1);
		this.numberOfIslands = inselZaehler;
		// TODO: Wenn der Meeresspiegel auf 0 sinkt, ist die Inselherstellung misslungen und muss
		// wiederholt werden.
		if (meeresspiegel <=1)
		{
			System.out.println("Meeresspiegel auf Null gesunken, defektes Archipel erzeugt");
			return 0;
		}
		//Inseln generieren, Schritt 5: Inselobjekte erzeugen
		int inselGroesse;
		island = new IslandCreator[inselZaehler];
		inselZaehler=0;
		for (i=0; i<archipelGebiet; i++)
			for (j=0; j<archipelGebiet; j++)
			{
				if (archipelErweitert[i+1][j+1] > meeresspiegel)
				{
					inselGroesse = (int) (Math.random()*this.magnitude) +1;
					island[inselZaehler++] = new IslandCreator(i, j, inselGroesse, this.archipelagoId);
				}
			}//for


		//Inseln mittels der Archipelposition an den korrekten Platz auf der Karte r�cken
		if (this.x < (int)(archipelGebiet/2))		this.x = (int)(archipelGebiet/2);
		if (this.y < (int)(archipelGebiet/2))		this.y = (int)(archipelGebiet/2);
		if (this.x > 999 - (int)(archipelGebiet/2))	this.x = (int)(archipelGebiet/2);
		if (this.y > 999 - (int)(archipelGebiet/2))	this.y = (int)(archipelGebiet/2);

		for (i=0; i<this.numberOfIslands;i++)
		{
			island[i].xPos = island[i].xPos-(int)(archipelGebiet/2)+this.x;
			island[i].yPos = island[i].yPos-(int)(archipelGebiet/2)+this.y;
		}
		return inselZaehler;
	}
}
