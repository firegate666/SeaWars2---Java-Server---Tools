package welterstellung;

//import java.util.ArrayList;
import welterstellung.ZufallsNamen;

/**
  * Letzte Änderung von Andreas Wagener, 1.6.2005:
 */
public class InselErstellung
{
	// Die ID wird von der Datenbank automatisch gesetzt. Nur fär die Kompatibilität
	// mit späteren Inselklassen vorhanden.
	public int id;
	// Der Name kann maximal 100 Buchstaben haben. Darum Get und Set zur Kontrolle
	private String name;
	/**
	 * Setzt den Namen einer Insel.
	 * @param name ist der Name, den die Insel zukünftig tragen wird.
	 * @return gibt 1 zurück, wenn der Name nicht in die Datenbank passt, oder
	 * 0 wenn der Name in Ordnung ist.
	 */
	public int setName(String name)
	{
		if (name.length()>100)
		{
			this.name = name.substring(0, 99);
			return 1;
		}
		else
		{
			this.name=name;
			return 0;
		}
	}
	/**
	 * Wählt einen zufälligen Namen für die Insel, basierend auf zufällig ausgewählten
	 * Silben und Buchstaben. Ich freu mich schon auf die Ergebnisse. :-)
	 * @return Gibt den erstellten Namen zurück.
	 */
	public String setZufallsName()
	{
		ZufallsNamen neuerName = new ZufallsNamen();
		this.name = neuerName.setZufallsName();
		return this.name;
	}

	public String getName()
	{
		return this.name;
	}
	// Die Größe reicht von 100 bis 50000
	public int groesse;
	// x_pos von 0 bis 999, theoretisch auf der Hauptkarte anzeigbar
	public int x_pos;
	 // y_pos von 0 bis 999, theoretisch auf der Hauptkarte anzeigbar
	public int y_pos;
	// IDs von Inselbezogenem Kram
	public long spielerID;
	public long archipelID;
	public int lagerID;

	/**
	 * Erstellt eine neue Insel mit zufälligen Koordinaten und zufälliger Größe
	 */
	public InselErstellung()
	{
		this.x_pos = (int)(Math.random()*1000);
		this.y_pos = (int)(Math.random()*1000);
		this.groesse = setGroesse(3);
		this.name = this.setZufallsName();
		this.archipelID =0;
	}
	/**
	 * Erstellt eine Insel mit definierter Position und Größe
	 * @param x ist die x-Koordinate der Insel auf der Hauptkarte.
	 * @param y ist die y-Koordinate der Insel auf der Hauptkarte.
	 * @param groesse ist ein Wert zwischen 1 und 5 und gibt den Platz auf der Insel an.
	 */
	public InselErstellung(int x, int y, int groesse)
	{
		this();
		this.x_pos = x;
		this.y_pos = y;
		this.groesse =  setGroesse(groesse);
	}
	/**
	 * Erstellt eine Insel mit definierter Position und Größe
	 * @param x ist die x-Koordinate der Insel auf der Hauptkarte.
	 * @param y ist die y-Koordinate der Insel auf der Hauptkarte.
	 * @param groesse ist ein Wert zwischen 1k und 1M und gibt den Platz auf der Insel an.
	 * @param archipelID ist die ID des Archipels, zu dem diese Insel gehört.
	 */
	public InselErstellung(int x, int y, int groessenklasse, int archipelID)
	{
		this.x_pos = x;
		this.y_pos = y;

		this.groesse =  setGroesse(groessenklasse);
		this.archipelID = archipelID;
		this.name = this.setZufallsName();
	}
	/**
	 * Erstellt eine Insel mit definierter Position
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
	 * Wer sich nicht mit den Größen beschäftigen will aber eine gewisse Kontrolle über
	 * die Größe der Insel behalten will, benutzt diese Methode. Sehr zu empfehlen, weil
	 * die Größnklasse mit der Technologiestufe ziemlich gleichzusetzen sein sollte.
	 * @param groessenklasse setzt die Maximalgröße der Insel.
	 */
	public int setGroesse(int groessenklasse)
	{
		switch (groessenklasse)
		{
			case 1: {
				this.groesse =  100;
				break;
				}
			case 2: {
				this.groesse = (int)(Math.random()*600) + 400;
				break;
				}
			case 3: {
				this.groesse = (int)(Math.random()*4000)+ 1000;
				break;
				}
			case 4: {
				this.groesse = (int)(Math.random()*9000)+ 1000;
				break;
				}
			case 5: {
				this.groesse = (int)(Math.random()*40000)+ 10000;
				break;
				}
			default:{
				//Größer als Klasse 5? Na gut, meinetwegen.
				this.groesse = (int)(Math.random()*90000)+ 10000;
			}
		}
		return this.groesse;
	}
}
