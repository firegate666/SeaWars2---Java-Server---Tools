/**
 * Klasse zum Erstellen von zufälligen Namen. Buchstaben werden nach ihrer Häufigkeit
 * verwendet, einige Regeln für Konsonanten und Vokale implementiert. 
 * 
 * Letzte Änderungen von 
 * Andreas Wagener am 1. 06. 2005: Kommentar und Änderungsdatum hinzugefügt ;-)
 * Marco Behnke am 04.06.2005: Vorschlag zur Ausgliederung von Inselnamen, Praefixen und Suffixen
 * Andreas Wagener am 06.06.2005: Das Silbensystem entfernt, Vorschlag zur Ausgliederung von Insel-
 * 				namen umgesetzt, Konsonanten als Anfangsbuchstaben erlaubt, "?" aus den Fremdsprach-
 * 				lichen Inselnamen entfernt und durch passende Umlaute ersetzt
 * Andreas Wagener am 06.06.2005: Diese Klasse wird langsam zum Hobby. Namensgenerierung beschleunigt,
 * 				"L" hinzugefügt... Jaja, es hat bislang einfach gefehlt, ich gebs ja zu!
 */
package welterstellung;

import java.util.StringTokenizer; // used for external properties file

// Namensklasse
 
/**
 * 
 * @author Andreas
 *
 * Diese Buchstabenklasse stellt für jeden Buchstaben auch die Wahrscheinlichkeit
 * seines auftretens in einem Lexikon zur Verfügung. Man könnte sich überlegen, 
 * ob zusätzlich die Wahrscheinlichkeit des Auftretens am Wortanfang berücksichtigt
 * wird, um zusätzliche Sprachähnlichkeit der generierten Wörter mit echten Wörtern
 * zu erreichen.
 */
class Buch {
	String buchstabe;
	double wahrscheinlichkeit;
	public Buch(String buchstabe, double wahrscheinlichkeit)
	{
		this.buchstabe = buchstabe;
		this.wahrscheinlichkeit = wahrscheinlichkeit;
	}
}



public class ZufallsNamen
{

	private Buch[] hartKon;
	private Buch[] weicKon;
	private Buch[] langKon;
	private Buch[] klinKon;
	private Buch[] voka;
	private Buch[] umlaute;
	
	public ZufallsNamen()
	{
		this.hartKon = new Buch[7]; 
		hartKon[0] = new Buch("c", 2.06); //$NON-NLS-1$
		hartKon[1] = new Buch("k", 1.21); //$NON-NLS-1$
		hartKon[2] = new Buch("qu", 0.02); //$NON-NLS-1$
		hartKon[3] = new Buch("p", 0.79); //$NON-NLS-1$
		hartKon[4] = new Buch("t", 6.15); //$NON-NLS-1$
		hartKon[5] = new Buch("x", 0.03); //$NON-NLS-1$
		hartKon[6] = new Buch("z", 1.13); //$NON-NLS-1$
	
		this.weicKon = new Buch[4]; 
	    weicKon[0] = new Buch("b", 1.89); //$NON-NLS-1$
	    weicKon[1] = new Buch("d", 5.08); //$NON-NLS-1$
	    weicKon[2] = new Buch("g", 3.01); //$NON-NLS-1$
	    weicKon[3] = new Buch("h", 4.76); //$NON-NLS-1$
								  
	    this.langKon = new Buch[5];// {"f", "s", "v", "sch", "ch"};
	    langKon[0] = new Buch("f", 1.66); //$NON-NLS-1$
	    langKon[1] = new Buch("s", 5.27); 
	    langKon[2] = new Buch("v", 0.67); //$NON-NLS-1$
	    langKon[3] = new Buch("sch", 2.00); 
	    langKon[4] = new Buch("ch", 1.00); //$NON-NLS-1$
	    
	    this.klinKon = new Buch[7];// {"m", "n", "r", "w", "y", "j"};
	    klinKon[0] = new Buch("m", 2.53); //$NON-NLS-1$
	    klinKon[1] = new Buch("n", 9.78); //$NON-NLS-1$
	    klinKon[2] = new Buch("r", 7.00); //$NON-NLS-1$
	    klinKon[3] = new Buch("w", 1.89); //$NON-NLS-1$
	    klinKon[4] = new Buch("y", 0.04); //$NON-NLS-1$
	    klinKon[5] = new Buch("j", 0.27); //$NON-NLS-1$
	    klinKon[6] = new Buch("l", 3.44);
	    
		this.voka = new Buch[5];// {"a", "e", "i", "o", "u" };
	    voka[0] = new Buch("a", 6.51); //$NON-NLS-1$
	    voka[1] = new Buch("e", 17.40); //$NON-NLS-1$
	    voka[2] = new Buch("i", 7.55); //$NON-NLS-1$
	    voka[3] = new Buch("o", 2.51); //$NON-NLS-1$
	    voka[4] = new Buch("u", 4.35); //$NON-NLS-1$
	
	    this.umlaute = new Buch[3]; // ä, ö und ü
		umlaute[0] = new Buch("ä", 0.4); //$NON-NLS-1$
		umlaute[1] = new Buch("ö", 0.4); //$NON-NLS-1$
		umlaute[2] = new Buch("ü", 0.4); //$NON-NLS-1$
		
		/*
		 * Buchstabenhäufigkeiten am Wortanfang
		 * 	3.00, 10.01, 1.50, 4.00, 2.00, 4.00, 5.71, 7.61, 0.90, 2.00, 9.91, 4.30, 6.11, 2.10, 1.10, 4.50, 0.20, 5.31, 13.21, 3.10, 0.50, 1.90, 5.81, 0.00, 0.10, 1.10
		 * Kumulative Buchstabenhäufigkeiten am Wortanfang
		 * 3.00, 13.01, 14.51, 18.52, 20.52, 24.52, 30.23, 37.84, 38.74, 40.74, 50.65, 54.95, 61.06, 63.16, 64.26, 68.77, 68.97, 74.27, 87.49, 90.59, 91.09, 92.99, 98.80, 98.80, 98.90, 100.00
		Sie sind in alphabetischer Reihenfolge. Das C enthält die Häufigkeit 
		für das CH gleich mit, das S ebenfalls die Häufigkeit von SCH. Ä,Ö,Ü und X 
		sind zu vernachlässigen, so selten sind sie. Das S kann vielleicht tatsächlich
		geteilt werden in S und SCH, weil das SCH so häufig ist, dass man es normal
		nicht vernachlässigen sollte.
		 */
	
	}
	 
	public String setZufallsName()
	{

		String name = "Zufallsname"; //$NON-NLS-1$
			/*
 			 * Weil ich das L und das N so schön finde, habe ich die Wahrscheinlichkeiten 
			 * ein wenig höher geschummelt. Sorry dafür! ;-)
 			*/
		double[] kumulativeBuchstabenHaeufigkeitWortAnfang = {0.00, 3.00, 13.01, 14.51, 18.52, 20.52, 
				24.52, 30.23, 37.84, 38.74, 39.14, 49.05, 54.55, 60.66, 63.16, 64.26, 68.77, 
				68.97, 74.27, 87.49, 90.59, 91.09, 92.99, 98.80, 98.90, 100.00};
		char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
							'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'Y', 'Z'};
		
		
		boolean vokalErlaubt = true,
		umlaErlaubt = true,
		klinKonErlaubt = true,
		langKonErlaubt = true,
		weicKonErlaubt = true,
		hartKonErlaubt = true;
		
		int vokaMuss = 1; //Je mehr Konsonanten nacheinander stehen, desto dringlicher wird ein Vokal
		int namenslaenge = (int) (Math.random()*8+4);
		if (namenslaenge <= 10) //Folgenden Kram nur machen,  wenn der Name auch übernommen wird
		{
			/*
			 * Hier wird ein neuer Name zusammengestellt. Dabei wird 
			 * erst eine Buchstabengruppe ausgesucht, die in der aktuellen Situation
			 * gerade erlaubt ist. Wenn das geschehen ist, wird auf Zufall einer der 
			 * Buchstaben aus der aktuellen Gruppe ausgesucht und mit einem Zufalls-
			 * generator sichergestellt, dass er in seiner "natürlichen" Häufigkeit vom
			 * Namensgenerator verwendet wird.
			 * Wenn dieser Buchstabe tatsächlich ausgesucht wird, dann werden die Er-
			 * laubnisse und Verbote für weitere Buchstaben neu gesetzt. Wenn nicht, wirds
			 * noch einmal probiert.
			 */
			int gruppenwahl=0, buchstabenwahl=0;
			name="";
			double anfang = Math.random() * 100;
			for (int i=kumulativeBuchstabenHaeufigkeitWortAnfang.length-1; i>=0 ; i--)
			{
				if (anfang > kumulativeBuchstabenHaeufigkeitWortAnfang[i])
				{
					name = name + alphabet[i];
					i=0;
				}
			}
			if (   name.charAt(0) == 'A' 
				|| name.charAt(0) == 'E'
				|| name.charAt(0) == 'I'
				|| name.charAt(0) == 'O'
				|| name.charAt(0) == 'U')
			{
				klinKonErlaubt = true;
				langKonErlaubt = true;
				weicKonErlaubt = true;
				hartKonErlaubt = true;
				vokalErlaubt = false;
				vokaMuss = 0;
			}
			else
			{
				klinKonErlaubt = false;
				langKonErlaubt = false;
				weicKonErlaubt = false;
				hartKonErlaubt = false;
				vokalErlaubt = true;
				vokaMuss = 2;
			}
			for (int i=0; name.length()<namenslaenge;i++)
			{
				if (vokaMuss >= 2) gruppenwahl =0;
				else gruppenwahl = (int)(Math.random()*5);
				
				switch (gruppenwahl)
				{
				case 0: {
					if (vokalErlaubt || vokaMuss >=2)
					{
						buchstabenwahl = (int) (Math.random()*voka.length);
						if (voka[buchstabenwahl].wahrscheinlichkeit > Math.random()*100)
						{
							name=name+voka[buchstabenwahl].buchstabe;
							//voka[buchstabenwahl].wahrscheinlichkeit=0;
							klinKonErlaubt = true;
							langKonErlaubt = true;
							weicKonErlaubt = true;
							hartKonErlaubt = true;
							vokalErlaubt = false;
							vokaMuss = 0;
							break;
						}
					}
				}
				case 1: {
					if (klinKonErlaubt)
					{
						buchstabenwahl = (int) (Math.random()*klinKon.length);
						if (klinKon[buchstabenwahl].wahrscheinlichkeit > Math.random()*100)
						{
							name=name+klinKon[buchstabenwahl].buchstabe;
							//klinKon[buchstabenwahl].wahrscheinlichkeit=0;
							klinKonErlaubt = false;
							langKonErlaubt = false;
							weicKonErlaubt = true;
							hartKonErlaubt = true;
							vokalErlaubt = true;
							//Falls es ein "L" ist, das direkt auf einen Vokal folgt...
							if (klinKon[buchstabenwahl].buchstabe == "l" && vokaMuss ==0)
							{
								klinKonErlaubt = true;
								langKonErlaubt = true;
							}
							else
								vokaMuss++;
							break;
						}
					}
				}
				case 2: {
					if (langKonErlaubt)
					{
						buchstabenwahl = (int) (Math.random()*langKon.length);
						if (langKon[buchstabenwahl].wahrscheinlichkeit > Math.random()*100)
						{
							name=name +langKon[buchstabenwahl].buchstabe;
							//langKon[buchstabenwahl].wahrscheinlichkeit=0;
							klinKonErlaubt = true;
							langKonErlaubt = false;
							weicKonErlaubt = false;
							hartKonErlaubt = true;
							vokalErlaubt = true;
							vokaMuss++;
							break;
						}
					}
				}
				case 3: {
					if (weicKonErlaubt)
					{
						buchstabenwahl = (int) (Math.random()*weicKon.length);
						if (weicKon[buchstabenwahl].wahrscheinlichkeit > Math.random()*100)
						{
							name=name+weicKon[buchstabenwahl].buchstabe;
							//weicKon[buchstabenwahl].wahrscheinlichkeit=0;
							klinKonErlaubt = false;
							langKonErlaubt = false;
							weicKonErlaubt = false;
							hartKonErlaubt = false;
							vokalErlaubt = true;
							vokaMuss++;
							break;
						}
					}
				}
				case 4: {
					if (hartKonErlaubt)
					{
						buchstabenwahl = (int) (Math.random()*hartKon.length);
						if (hartKon[buchstabenwahl].wahrscheinlichkeit > Math.random()*100)
						{
							name=name+hartKon[buchstabenwahl].buchstabe;
							hartKon[buchstabenwahl].wahrscheinlichkeit=0;
							klinKonErlaubt = false;
							langKonErlaubt = false;
							weicKonErlaubt = false;
							hartKonErlaubt = false;
							vokalErlaubt = true;
							vokaMuss++;
							break;
						}
					}
				}
				case 5: {
					if (umlaErlaubt && ((Math.random()*100) < 1))
					{
						buchstabenwahl = (int) (Math.random()*umlaute.length);
						if (umlaute[buchstabenwahl].wahrscheinlichkeit > Math.random()*100)
						{
							name=name+umlaute[buchstabenwahl].buchstabe;
							umlaErlaubt = false;
							klinKonErlaubt = true;
							langKonErlaubt = true;
							weicKonErlaubt = true;
							hartKonErlaubt = true;
							vokalErlaubt = false;
							vokaMuss = 0;
							break;
						}
					}
				}
				default:{ //Die Buchstabenwahl ist ins Leere gelaufen? Nochmal versuchen.
					if (vokaMuss<2) 
						gruppenwahl = (int)(Math.random()*5);
					else
						gruppenwahl =0;
					i--;
				}
				
				}
			}
			if (vokaMuss >=2 )
			{
				buchstabenwahl = (int) (Math.random()*voka.length);
				name=name+voka[buchstabenwahl].buchstabe;
			}

			/*
			 * Wenn der Name weniger als 9 Buchstaben enthält, wird eins der Präfixe oder Suffixe aus-
			 * gewählt. So kommen Namen wie "Quranameki Island" zustande: Das "Island" wird nachträglich
			 * angehängt. Dazu habe ich einen bunten Strauß aus Deutschen, Englischen, Spanischen und 
			 * Französischen Suffixen und Präfixen zusammengestellt. Wenn einem von euch noch mehr ein-
			 * fallen, nur zu, je bunter der Namensgenerator ist, desto besser!
			 */
			name= name.toUpperCase().charAt(0)+name.toLowerCase().substring(1, name.length());
			String [] praefix = getPraefix();
			String [] suffix = getSuffix();
	
			if (Math.random()<0.7){
				if (Math.random() > 0.5)
				{
					name = praefix[(int)(Math.random()*praefix.length)] + name;
				}
				else
				{
					name = name + suffix[(int)(Math.random()*suffix.length)];
				}
	
			}
		}
		else //else zu if (namenslaenge <= 10)
		{
			String [] fertigeNamen = getInselNamen();
			name = fertigeNamen[(int)(Math.random()*fertigeNamen.length)];
		}
		return name.trim();
	}

	private String[] getInselNamen() {
		//uncomment to use external properties file
		String inselnamen = Messages.getString("ZufallsNamen.Inselnamen"); //$NON-NLS-1$
		String delim_inselname = Messages.getString("ZufallsNamen.Delim_Inselnamen"); //$NON-NLS-1$
		StringTokenizer st = new StringTokenizer(inselnamen, delim_inselname); //$NON-NLS-1$
		String [] result = new String[st.countTokens()];
		int counter = 0;
		while(st.hasMoreTokens()) {
			result[counter++] = st.nextToken();
		}
		return result;
	}
	
	private String[] getSuffix(){
//		uncomment to use external properties file
		String inselsuffixe = Messages.getString("ZufallsNamen.Inselsuffixe"); //$NON-NLS-1$
		String delim_inselsuffixe = Messages.getString("ZufallsNamen.Delim_Inselsuffixe"); //$NON-NLS-1$
		StringTokenizer st = new StringTokenizer(inselsuffixe, delim_inselsuffixe); //$NON-NLS-1$
		String [] result = new String[st.countTokens()];
		int counter = 0;
		while(st.hasMoreTokens()) {
			result[counter++] = st.nextToken();
		}
		return result;
	}
	
	private String[] getPraefix() {
//		uncomment to use external properties file
		String inselpraefixe = Messages.getString("ZufallsNamen.Inselpraefixe"); //$NON-NLS-1$
		String delim_inselpraefixe = Messages.getString("ZufallsNamen.Delim_Inselpraefixe"); //$NON-NLS-1$
		StringTokenizer st = new StringTokenizer(inselpraefixe, delim_inselpraefixe); //$NON-NLS-1$
		String [] result = new String[st.countTokens()];
		int counter = 0;
		while(st.hasMoreTokens()) {
			result[counter++] = st.nextToken();
		}
		return result;
	}
}
 