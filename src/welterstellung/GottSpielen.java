package welterstellung;


class GottSpielen
{
	/**
	 * Ausf�hrbare Welterstellung. Vorsicht, kann einige Minuten dauern.
	 * @param args
	 * -cardpart 7 setzt den Kartenabschnitt der neuen Welt auf 7
	 * -path "c:\Bild.bmp" setzt den Pfad auf ein BMP nach Wahl
	 * -archipelago 500 setzt die Archipelanzahl auf 500
	 * -fileOutputFlag 1 schaltet die Ausgabe der Inseln in eine Excel-Textdatei ein
	 * -fileOutputPath "C:\Eigene Dateien" gibt die Insel.txt in C:\Eigene Dateien aus
	 * -talkative schaltet die Konsolen-Kommentierung w�hrend des Vorgangs ein.
	 * 
	 */	
	public static void main(String[] args) {

		Welterstellung welt = new Welterstellung(1);
		welt.archipelAnzahl = 500;
		welt.bildPfad = "C:\\Beispiel.bmp";
		welt.empfindlichkeit = 0.7;
		welt.dateiAusgabeFlag = 0;
		welt.dateiAusgabePfad = "C:\\Inseln.txt";
		welt.testausgabe = 0;
		welt.kartenabschnitt=0;

		for (int i=0; i<args.length; i++)
		{
			try
			{
				if (args[i] == "-cardpart")
				{
					i++;
					welt.kartenabschnitt = Integer.parseInt(args[i]);
				}
				if (args[i] == "-path")
				{
					i++;
					welt.bildPfad = args[i];
				}
				if (args[i] == "-archipelago")
				{
					i++;
					welt.archipelAnzahl = Integer.parseInt(args[i]);
				}
				if (args[i] == "-fileOutputFlag")
				{
					i++;
					welt.dateiAusgabeFlag = Integer.parseInt(args[i]);
				}
				if (args[i] == "-fileOutputPath")
				{
					i++;
					welt.dateiAusgabeFlag = Integer.parseInt(args[i]);
				}
				if (args[i] == "-talkative")
				{
					welt.testausgabe = 1;
				}
				if (args[i] == "-h")
				{
					 System.out.println("-cardpart 7 setzt den Kartenabschnitt der neuen Welt auf 7,");
					 System.out.println("            standardm��ig ist die 1 eingestellt. Dieser Parameter sollte unbedingt gesetzt werden!");
					 System.out.println("-path  c:\\Bild.bmp setzt den Pfad auf ein BMP nach Wahl,");
					 System.out.println("            standardm��ig ist c:\\Beispiel.bmp eingestellt.");
					 System.out.println("-archipelago 456 setzt die Archipelanzahl auf 456, standardm��ig sind 500 eingestellt.");
					 System.out.println("-fileOutputFlag 1 schaltet die Ausgabe der Inseln in eine Excel-Textdatei ein.");
					 System.out.println("            Wenn dieses Flag gesetzt wird, sollte auch fileOutputPath gesetzt werden.");
					 System.out.println("-fileOutputPath \"C:\\Eigene Dateien\" gibt die Insel.txt in C:\\Eigene Dateien aus");
					 System.out.println("            standard ist der Pfad C:\\Inseln.txt");
					 System.out.println("-talkative schaltet die Konsolen-Kommentierung w�hrend des Vorgangs ein.");
				}
			}
			catch (NumberFormatException e)
			{
				 System.out.println("Welt wurde nicht erstellt. Falsche Kommandozeilenparameter.");
				 System.out.println("G�ltig sind folgende Parameter:");
				 System.out.println("-cardpart 7 setzt den Kartenabschnitt der neuen Welt auf 7,");
				 System.out.println("            Wenn nichts eingestellt wird, funktioniert auch nichts.");
				 System.out.println("-path  c:\\Bild.bmp setzt den Pfad auf ein BMP nach Wahl,");
				 System.out.println("            standardm��ig ist c:\\Beispiel.bmp eingestellt.");
				 System.out.println("-archipelago 456 setzt die Archipelanzahl auf 456, standardm��ig sind 500 eingestellt.");
				 System.out.println("-fileOutputFlag 1 schaltet die Ausgabe der Inseln in eine Excel-Textdatei ein.");
				 System.out.println("            Wenn dieses Flag gesetzt wird, sollte auch fileOutputPath gesetzt werden.");
				 System.out.println("-fileOutputPath \"C:\\Eigene Dateien\" gibt die Insel.txt in C:\\Eigene Dateien aus");
				 System.out.println("            standard ist der Pfad C:\\Inseln.txt");
				 System.out.println("-talkative schaltet die Konsolen-Kommentierung w�hrend des Vorgangs ein.");
				return;
			}
		}	
		if (welt.kartenabschnitt==0)
		{
			 System.out.println("Welt wurde nicht erstellt. Falsche Kommandozeilenparameter.");
			 System.out.println("G�ltig sind folgende Parameter:");
			 System.out.println("-cardpart 7 setzt den Kartenabschnitt der neuen Welt auf 7,");
			 System.out.println("            Wenn nichts eingestellt wird, funktioniert auch nichts.");
			 System.out.println("-path  c:\\Bild.bmp setzt den Pfad auf ein BMP nach Wahl,");
			 System.out.println("            standardm��ig ist c:\\Beispiel.bmp eingestellt.");
			 System.out.println("-archipelago 456 setzt die Archipelanzahl auf 456, standardm��ig sind 500 eingestellt.");
			 System.out.println("-fileOutputFlag 1 schaltet die Ausgabe der Inseln in eine Excel-Textdatei ein.");
			 System.out.println("            Wenn dieses Flag gesetzt wird, sollte auch fileOutputPath gesetzt werden.");
			 System.out.println("-fileOutputPath \"C:\\Eigene Dateien\" gibt die Insel.txt in C:\\Eigene Dateien aus");
			 System.out.println("            standard ist der Pfad C:\\Inseln.txt");
			 System.out.println("-talkative schaltet die Konsolen-Kommentierung w�hrend des Vorgangs ein.");
			return;
		}
		welt.Erstellung();
	}
}