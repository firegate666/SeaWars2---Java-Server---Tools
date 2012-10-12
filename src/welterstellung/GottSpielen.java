package welterstellung;


class GottSpielen
{
	/**
	 * Ausführbare WorldCreator. Vorsicht, kann einige Minuten dauern.
	 * @param args
	 * -cardpart 7 setzt den Kartenabschnitt der neuen Welt auf 7
	 * -path "c:\Bild.bmp" setzt den Pfad auf ein BMP nach Wahl
	 * -archipelago 500 setzt die Archipelanzahl auf 500
	 * -fileOutputFlag 1 schaltet die Ausgabe der Inseln in eine Excel-Textdatei ein
	 * -fileOutputFlag "C:\Eigene Dateien" gibt die Insel.txt in C:\Eigene Dateien aus
	 * -talkative schaltet die Konsolen-Kommentierung während des Vorgangs ein.
	 *
	 */
	public static void main(String[] args) {
		WorldCreator worldCreator = new WorldCreator(1);
		worldCreator.numberOfArchipelagos = 500;
		worldCreator.sensitivity = 0.7;
		worldCreator.fileOutputFlag = 0;
		worldCreator.debugOutput = 1;
		worldCreator.mapSection=0;

		for (int i=0; i<args.length; i++)
		{
			try
			{
				if (args[i].matches("-dbhost"))
				{
					i++;
					worldCreator.dbhost = args[i];
				}
				if (args[i].matches("-dbname"))
				{
					i++;
					worldCreator.dbname = args[i];
				}
				if (args[i].matches("-dbuser"))
				{
					i++;
					worldCreator.dbuser = args[i];
				}
				if (args[i].matches("-dbpass"))
				{
					i++;
					worldCreator.dbpass = args[i];
				}

				if (args[i].matches("-sensitivity"))
				{
					i++;
					worldCreator.sensitivity = Double.parseDouble(args[i]);
				}
				if (args[i].matches("-path"))
				{
					i++;
					worldCreator.imagePath = args[i];
				}
				if (args[i].matches("-archipelago"))
				{
					i++;
					worldCreator.numberOfArchipelagos = Integer.parseInt(args[i]);
				}
				if (args[i].matches("-fileOutputFlag"))
				{
					i++;
					worldCreator.fileOutputFlag = Integer.parseInt(args[i]);
				}
				if (args[i].matches("-fileOutputPath"))
				{
					i++;
					worldCreator.fileOutputPath = args[i];
				}
				if (args[i].matches("-talkative"))
				{
					worldCreator.debugOutput = 1;
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

		worldCreator.run();
	}
}
