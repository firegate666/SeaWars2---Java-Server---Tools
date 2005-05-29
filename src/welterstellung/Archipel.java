package welterstellung;

public class Archipel
{
	public int x;
	public int y;
	public int groesse;
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
	}
}