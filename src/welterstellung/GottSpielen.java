package welterstellung;


class GottSpielen
{
	/**
	 * Ausführbare Welterstellung. Vorsicht, kann einige Minuten dauern.
	 * @param args
	 * -cardpart 7 setzt den Kartenabschnitt der neuen Welt auf 7
	 * -path "c:\Bild.bmp" setzt den Pfad auf ein BMP nach Wahl
	 * -archipelago 500 setzt die Archipelanzahl auf 500
	 * -fileOutputFlag 1 schaltet die Ausgabe der Inseln in eine Excel-Textdatei ein
	 * -fileOutputPath "C:\Eigene Dateien" gibt die Insel.txt in C:\Eigene Dateien aus
	 * -talkative schaltet die Konsolen-Kommentierung während des Vorgangs ein.
	 *
	 */
	public static void main(String[] args) {
		Welterstellung welt = new Welterstellung(1);
		welt.archipelAnzahl = 500;
		welt.empfindlichkeit = 0.7;
		welt.dateiAusgabeFlag = 0;
		welt.testausgabe = 1;
		welt.kartenabschnitt=0;

		for (int i=0; i<args.length; i++)
		{
			try
			{
				if (args[i].matches("-dbhost"))
				{
					i++;
					welt.dbhost = args[i];
				}
				if (args[i].matches("-dbname"))
				{
					i++;
					welt.dbname = args[i];
				}
				if (args[i].matches("-dbuser"))
				{
					i++;
					welt.dbuser = args[i];
				}
				if (args[i].matches("-dbpass"))
				{
					i++;
					welt.dbpass = args[i];
				}

				if (args[i].matches("-sensitivity"))
				{
					i++;
					welt.empfindlichkeit = Double.parseDouble(args[i]);
				}
				if (args[i].matches("-path"))
				{
					i++;
					welt.bildPfad = args[i];
				}
				if (args[i].matches("-archipelago"))
				{
					i++;
					welt.archipelAnzahl = Integer.parseInt(args[i]);
				}
				if (args[i].matches("-fileOutputFlag"))
				{
					i++;
					welt.dateiAusgabeFlag = Integer.parseInt(args[i]);
				}
				if (args[i].matches("-fileOutputPath"))
				{
					i++;
					welt.dateiAusgabePfad = args[i];
				}
				if (args[i].matches("-talkative"))
				{
					welt.testausgabe = 1;
				}
				if (args[i].matches("-h"))
				{
					System.out.println("Argument -h gefunden");
					System.out.print("-path  c:\\Bild.bmp setzt den Pfad auf ein BMP nach Wahl,");
					System.out.println("            standardmäßig ist c:\\Beispiel.bmp eingestellt.");
					System.out.println("-archipelago 456 setzt die Archipelanzahl auf 456, standardmäßig sind 500 eingestellt.");
					System.out.print("-fileOutputFlag 1 schaltet die Ausgabe der Inseln in eine Excel-Textdatei ein.");
					System.out.println("            Wenn dieses Flag gesetzt wird, sollte auch fileOutputPath gesetzt werden.");
					System.out.print("-fileOutputPath \"C:\\Eigene Dateien\" gibt die Insel.txt in C:\\Eigene Dateien aus");
					System.out.println("            standard ist der Pfad C:\\Inseln.txt");
					System.out.println("-talkative schaltet die Konsolen-Kommentierung während des Vorgangs ein.");
					System.out.print("-sensitivity 0.76 setzt die Empfindlichkeit auf 0.76, was ziemlich viel ist.");
				}
			}
			catch (NumberFormatException e)
			{
				System.out.println("Exception: "+e.getMessage());
				System.out.println("Welt wurde nicht erstellt. Falsche Kommandozeilenparameter.");
				System.out.println("Gültig sind folgende Parameter:");
				System.out.print("-path  c:\\Bild.bmp setzt den Pfad auf ein BMP nach Wahl,");
				System.out.println("            standardmäßig ist c:\\Beispiel.bmp eingestellt.");
				System.out.print("-archipelago 456 setzt die Archipelanzahl auf 456, standardmäßig sind 500 eingestellt.");
				System.out.print("-fileOutputFlag 1 schaltet die Ausgabe der Inseln in eine Excel-Textdatei ein.");
				System.out.println("            Wenn dieses Flag gesetzt wird, sollte auch fileOutputPath gesetzt werden.");
				System.out.print("-fileOutputPath \"C:\\Eigene Dateien\" gibt die Insel.txt in C:\\Eigene Dateien aus");
				System.out.println("            standard ist der Pfad C:\\Inseln.txt");
				System.out.println("-talkative schaltet die Konsolen-Kommentierung während des Vorgangs ein.");
				System.out.print("-sensitivity 0.76 setzt die Empfindlichkeit auf 0.76, was ziemlich viel ist.");
				return;
			}
		}

		welt.Erstellung();
	}
}
