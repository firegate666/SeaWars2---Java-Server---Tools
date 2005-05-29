package welterstellung;

import java.util.ArrayList;

/**
 * Diese Klasse erstellt Inseln f�r die Archipele und verteilt sie angemessen.
 * Es k�nnen Atolle oder Archipele erzeugt werden.
 */
public class InselErstellung
{
	// Die ID wird von der Datenbank automatisch gesetzt. Nur f�r die Kompatibilit�t
	// mit sp�teren Inselklassen vorhanden.
	public int id;
	// Der Name kann maximal 100 Buchstaben haben. Darum Get und Set zur Kontrolle
	private String name;
	/**
	 * Setzt den Namen einer Insel.
	 * @param name ist der Name, den die Insel zuk�nftig tragen wird.
	 * @return gibt 1 zur�ck, wenn der Name nicht in die Datenbank passt, oder
	 * 0 wenn der Name in Ordnung ist.
	 */
	public int setName(String name)
	{
		if (name.length()>100)
		{
			return 1;
		}
		else
		{
			this.name=name;
			return 0;
		}
	}
	/**
	 * W�hlt einen zuf�lligen Namen f�r die Insel, basierend auf zuf�llig ausgew�hlten
	 * Silben und Buchstaben. Ich freu mich schon auf die Ergebnisse. :-)
	 * @return Gibt den erstellten Namen zur�ck.
	 */
	public String setZufallsName()
	{
		/**
		 * TODO: Methode zu Ende schreiben
		 */ 
		String name = "Zufallsname";
		ArrayList silben = new ArrayList();
		int silbenlaenge = 0;
		int silbenzahl = 0;
		int[] c = new int[4];
		String[] silbe = new String[1000]; 
		String[] kuKonso = new String[] {"b", "c", "d", "g", "k", "p", "qu", "t", "x", "z"}; 
		String[] laKonso = new String[] {"f", "h", "m", "n", "r", "s", "v", "w", "y", "sch", "ch"}; 
		String[] voka = new String[] {"a", "e", "i", "j", "o", "u", "�", "�", "�"}; 
		for (int i=0; i < 1000;  i++)
		{
			//Zahlen zwischen 2 und 4
			silbenlaenge = (int) (Math.random()*3+1.9999);
			for (int j=0; j < silbenlaenge; j++)
			{
				c[j]=(int)(Math.random()*(kuKonso.length + laKonso.length + voka.length)+0.9999);
			}
			boolean kuKonsoErlaubt = true;
			for (int j=0; j < silbenlaenge; j++)
			{
				if (c[j]<kuKonso.length && kuKonsoErlaubt)
				{
					silbe[i]=silbe[i]+kuKonso[c[j]];
					kuKonsoErlaubt = false;
				}
				else if (c[j]<(kuKonso.length+laKonso.length) && c[j]>=kuKonso.length)
				{
					silbe[i]=silbe[i]+laKonso[c[j]-kuKonso.length];
				}
				else 
				{
					silbe[i]=silbe[i]+voka[c[j]-kuKonso.length-laKonso.length];
				}
			}
		}
		silbenzahl = (int) (Math.random() * 2.999+1);
		name = "";
		for (int i=0; i<silbenzahl; i++)
		{
			name = name + silbe[(int)(Math.random()*999.999+1)];
		}
		this.name = name;
		return name;
	}
	public String getName()
	{
		return this.name;
	}
	// Die Gr��e reicht von 1000 bis 1000000 
	public int groesse;
	// x_pos von 0 bis 1000, theoretisch auf der Hauptkarte anzeigbar
	public int x_pos; 	 	 	 	 	 	 	 	
	 // y_pos von 0 bis 1000, theoretisch auf der Hauptkarte anzeigbar
	public int y_pos; 	 	 	 	 	 	 	 	
	// IDs von Inselbezogenem Kram
	public long spieler_id; 	 	 	 	 	 	 	 	
	public long archipel_id; 	 	 	 	 	 	 	 	
	public int lager_id; 
	
	/**
	 * Erstellt eine neue Insel mit zuf�lligen Koordinaten und zuf�lliger Gr��e
	 */
	public InselErstellung()
	{
		this.x_pos = (int)(Math.random()*1000);
		this.y_pos = (int)(Math.random()*1000);
		this.groesse = (int)(Math.random()*999000 + 1000);
	}
	/**
	 * Erstellt eine Insel
	 * @param x ist die x-Koordinate der Insel auf der Hauptkarte.
	 * @param y ist die y-Koordinate der Insel auf der Hauptkarte.
	 * @param groesse ist ein Wert zwischen 1k und 1M und gibt den Platz auf der Insel an.
	 */
	public InselErstellung(int x, int y, int groesse)
	{
		this.x_pos = x;
		this.y_pos = y;
		this.groesse =  groesse;
	}
	/**
	 * Erstellt eine Insel
	 * @param x ist die x-Koordinate der Insel auf der Hauptkarte.
	 * @param y ist die y-Koordinate der Insel auf der Hauptkarte.
	 */
	public InselErstellung(int x, int y)
	{
		this();
		this.x_pos = x;
		this.y_pos = y;
	}
	
	/**
	 * Wer sich nicht mit den Gr��en besch�ftigen will aber eine gewisse Kontrolle �ber
	 * die Gr��e der Insel behalten will, benutzt diese Methode. Sehr zu empfehlen, weil
	 * die Gr��enklasse mit der Technologiestufe ziemlich gleichzusetzen sein sollte. 
	 * @param groessenklasse setzt die Maximalgr��e der Insel, wobei die Klasse maximal
	 * um 2 unterschritten wird. Das hei�t zum Beispiel: Klasse 4 kann von 2 bis 4 reichen.
	 * Sonderfall Klasse 1: Die Starterinsel ist f�r alle gleich.
	 */
	public int setGroesse(int groessenklasse)
	{
		double groesse=0;
		switch (groessenklasse)
		{
			case 1: this.groesse = 1000;
			case 2: {
				this.groesse = (int)(Math.random()*9000) + 1000;
			}
			case 3: {
				this.groesse = (int)(Math.random()*99000) + 1000;
			}
			case 4: {
			this.groesse = (int)(Math.random()*490000)+ 10000;
			}
			case 5: {
				this.groesse = (int)(Math.random()*9900000)+ 100000;
			}
			default:{
				//Gr��er als Klasse 5? Na gut, meinetwegen.
				this.groesse = (int)(Math.random()*49900000)+ 100000;
			}
		}
		return (int)groesse;
	}
}


