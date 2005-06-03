/**
 * Klasse zum Erstellen von zufälligen Namen. Buchstaben werden nach ihrer Häufigkeit
 * verwendet, einige Regeln für Konsonanten und Vokale implementiert. 
 * 
 * Letzte Änderungen von 
 * Andreas Wagener am 1. 06. 2005: Kommentar und Änderungsdatum hinzugefügt ;-)
 * Marco Behnke am 04.06.2005: Vorschlag zur Ausgliederung von Inselnamen, Praefixen und Suffixen
 */
package welterstellung;

import java.util.StringTokenizer; // used for external properties file

// Namensklasse
 
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
	    langKon[1] = new Buch("s", 4.27); //$NON-NLS-1$
	    langKon[2] = new Buch("v", 0.67); //$NON-NLS-1$
	    langKon[3] = new Buch("sch", 3.00); //$NON-NLS-1$
	    langKon[4] = new Buch("ch", 1.00); //$NON-NLS-1$
	    
	    this.klinKon = new Buch[6];// {"m", "n", "r", "w", "y", "j"};
	    klinKon[0] = new Buch("m", 2.53); //$NON-NLS-1$
	    klinKon[1] = new Buch("n", 9.78); //$NON-NLS-1$
	    klinKon[2] = new Buch("r", 7.00); //$NON-NLS-1$
	    klinKon[3] = new Buch("w", 1.89); //$NON-NLS-1$
	    klinKon[4] = new Buch("y", 0.04); //$NON-NLS-1$
	    klinKon[5] = new Buch("j", 0.27); //$NON-NLS-1$
	    
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
	
	}
	 
	public String setZufallsName()
	{

		String name = "Zufallsname"; //$NON-NLS-1$
		//ArrayList silben = new ArrayList();
		int silbenlaenge = 0;
		int silbenzahl = 0;
		silbenzahl = (int) (Math.random() * 3+2);
		 
		int[] c = new int[4];
		String[] silbe = new String[silbenzahl];
		
		//Zahlen zwischen 2 und 3
		silbenlaenge = (int) (Math.random()*2+2);
		boolean vokalErlaubt = true,
		umlaErlaubt = true,
		klinKonErlaubt = false,
		langKonErlaubt = false,
		weicKonErlaubt = false,
		hartKonErlaubt = false;
		
		int gruppenwahl=0, buchstabenwahl=0;
		for (int i=0; i<silbenzahl;i++)
		{
			silbe[i] = ""; //$NON-NLS-1$
			vokalErlaubt = true;
			klinKonErlaubt = false;
			langKonErlaubt = false;
			weicKonErlaubt = false;
			hartKonErlaubt = false;
			for (int k=0; k<silbenlaenge; k++)
			{
				gruppenwahl = (int)(Math.random()*5);
				switch (gruppenwahl)
				{
				case 0: {
					if (vokalErlaubt)
					{
						buchstabenwahl = (int) (Math.random()*voka.length);
						if (voka[buchstabenwahl].wahrscheinlichkeit > Math.random()*100)
						{
							silbe[i]=silbe[i]+voka[buchstabenwahl].buchstabe;
							klinKonErlaubt = true;
							langKonErlaubt = true;
							weicKonErlaubt = true;
							hartKonErlaubt = true;
							vokalErlaubt = false;
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
							silbe[i]=silbe[i]+klinKon[buchstabenwahl].buchstabe;
							klinKonErlaubt = false;
							langKonErlaubt = false;
							weicKonErlaubt = true;
							hartKonErlaubt = true;
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
							silbe[i]=silbe[i]+langKon[buchstabenwahl].buchstabe;
							klinKonErlaubt = false;
							langKonErlaubt = false;
							weicKonErlaubt = false;
							hartKonErlaubt = false;
							vokalErlaubt = true;
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
							silbe[i]=silbe[i]+weicKon[buchstabenwahl].buchstabe;
							klinKonErlaubt = false;
							langKonErlaubt = false;
							weicKonErlaubt = false;
							hartKonErlaubt = false;
							vokalErlaubt = true;
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
							silbe[i]=silbe[i]+hartKon[buchstabenwahl].buchstabe;
							klinKonErlaubt = true;
							langKonErlaubt = false;
							weicKonErlaubt = false;
							hartKonErlaubt = false;
							vokalErlaubt = true;
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
							silbe[i]=silbe[i]+umlaute[buchstabenwahl].buchstabe;
							umlaErlaubt = false;
							klinKonErlaubt = true;
							langKonErlaubt = true;
							weicKonErlaubt = false;
							hartKonErlaubt = false;
							vokalErlaubt = false;
							break;
						}
					}
				}
				default:{ //Die Buchstabenwahl ist ins Leere gelaufen? Nochmal versuchen.
					gruppenwahl = (int)(Math.random()*5+1);
					k--;
				}
				
				}
			}//for k
			//System.out.println(silbe[i]);
		}
		
		int silbennummer;
		name=""; //$NON-NLS-1$
		for (int i=0; i<silbenzahl; i++)
		{
			name = name + silbe[i];
			if (name.length()>10) break;
		}
		name= name.toUpperCase().charAt(0)+name.toLowerCase().substring(1, name.length());
		String [] praefix = getPraefix();
		String [] suffix = getSuffix();
		String [] fertigeNamen = getIslandNames();
		if (name.length()<9){
			if (Math.random() > 0.5)
			{
				name = praefix[(int)(Math.random()*praefix.length)] + name;
			}
			else
			{
				name = name + suffix[(int)(Math.random()*suffix.length)];
			}

		}
		else if (name.length()>10)
			name = fertigeNamen[(int)(Math.random()*fertigeNamen.length)];
		return name.trim();
	}

	private String[] getIslandNames() {
//		uncomment to use external properties file
//		String inselnamen = Messages.getString("ZufallsNamen.Inselnamen"); //$NON-NLS-1$
//		String delim_inselname = Messages.getString("ZufallsNamen.Delim_Inselnamen"); //$NON-NLS-1$
//		StringTokenizer st = new StringTokenizer(inselnamen, delim_inselname); //$NON-NLS-1$
//		String [] result = new String[st.countTokens()];
//		int counter = 0;
//		while(st.hasMoreTokens()) {
//			result[counter++] = st.nextToken();
//		}
//		return result;

		return new String [] {"Grönland", "Königin-Elisabeth", "Amund-Ringnes", "Axel-Heiberg", "Bathurst", "Borden", "Brock", "Byam-Martin", "Cameron", "Coburg", "Cornwall", "Cornwallis", "Devon", "Eglinton", "Ellef-Ringnes", "Ellesmere", "Emerald", "Graham", "König-Christian", "Lougheed", "Mackenzie-King", "Meighen", "Melville", "Prinz-Patrick", "Baffin", "Banksinsel", "Prince-of-Wales", "Victoria", "Southampton", "Coats", "Mansel", "Belcherinseln", "Akimiski", "Wrangelinsel", "Sewernaja Semlja", "Nowaja Semlja", "Kolgujew", "Franz-Joseph Land", "Spitzbergen", "Bäreninsel", "Lofoten", "Jan Mayen", "Island", "Bermuda", "Färöer", "Shetland", "Britische Inseln", "Großbritannien", "Irland", "Isle of Man", "Hebriden", "Orkney", "Anglesey", "Isle of Wight", "Makaronesien", "Kapverden", "Barlavento-Gruppe", "Santo Antão", "Vicente", "Nicolau", "Sal", "Boa Vista", "Santa Luzia", "Branco", "Razo", "Sotavento-Gruppe", "Maio", "Santiago", "Fogo", "Brava", "Ilheus Secos ou do Rombo.", "Lanzarote", "Fuerteventura", "Gran Canaria", "Teneriffa", "La Gomera", "La Palma", "El Hierro", "Alegranza", "Graciosa", "Montaña Clara", "Los Lobos", "Roque del Este", "Roque del Oeste", "Anaga", "Salmor", "Garachico", "Madeira", "Azoren", "Corvo", "Flores", "Faial", "Pico", "Jorge", "Graciosa", "Terceira", "Miguel", "Santa Maria", "Formigas", "Alderney", "Brecqhou oder Brechou", "Guernsey", "Herm", "Jersey", "Jethou", "Lihou", "Sark", "Ouessant", "Belle-Isle", "Ré", "Oléron", "Île d'Yeu", "Texel", "Vlieland", "Terschelling", "Ameland", "Schiermonnikoog", "Rottumerplaat", "Rottumeroog", "Noord-Beeveland", "Borkum", "Memmert", "Kachelotplate", "Juist", "Norderney", "Baltrum", "Langeoog", "Spiekeroog", "Wangerooge", "Neuwerk", "Scharhörn", "Trischen", "Amrum", "Föhr", "Nordstrand", "Pellworm", "Sylt", "Halligen", "Langeneß", "Hooge", "Gröde", "Nordstrandischmoor", "Oland", "Süderoog", "Südfall", "Hamburger Hallig", "Norderoog", "Habel", "Helgoland", "Læsø", "Anholt", "Seeland", "Fünen", "Alsen", "Langeland", "Lolland", "Falster", "Møn", "Fehmarn", "Rügen", "Hiddensee", "Greifswalder Oie", "Usedom", "Wolin", "Bornholm", "Öland", "Gotland", "Åland", "Hiiumaa", "Dagö", "Saaremaa", "Ösel", "Balearen", "Mallorca", "Menorca", "Ibiza", "Formentera", "Îles d'Hyères", "Îles Lérins", "Korsika", "Sardinien", "Insel San Pietro", "Insel Sant'Antioco", "La Maddalena", "Caprera", "Capraia", "Elba", "Giglio", "Pianosa", "Montecristo", "Giannutri", "Gorgona", "Ponza", "Palmarola", "Zannone", "Ventotene", "Capri", "Ischia", "Procida", "Stromboli", "Panarea", "Vulcano", "Lipari", "Salina", "Filicudi", "Alicudi", "Favignana", "Levanzo", "Marettimo", "Sizilien", "Pantelleria", "Lampedusa", "Linosa", "Lampione", "Malta", "Malta", "Gozo", "Comino", "Cominotto", "Kemmunett", "Filfla", "Filfola", "St. Pauls Island", "La Galit", "Kerkenna", "Djerba", "Venedig", "Murano", "Burano", "Torcello", "Lido di Venezia", "Pellegrina", "Brijuni", "Krk", "Cres", "Rab", "Pag", "Losinj", "Unije", "Susak", "Veli", "Mali Sakrane", "Ilovik", "Sveti Petar", "Lošinj", "Dugi Otok", "Kornat", "Šolia", "Bra?", "Hvar", "Vis", "Kor?ula", "Lastovo", "Mijet", "Isole Tremiti", "Othoni", "Erikoussa", "Mathraki", "Korfu", "Paxos oder Paxi", "Antipaxi", "Lefkada", "Kephalonia", "Ithaka", "Zakynthos", "Strofaden", "Ägäis", "Thasos", "Samothrake", "Límnos", "Agios Efstratios", "Gökçe Ada", "Imbros", "Bozca Ada", "Skiathos", "Skopelos", "Alonnisos", "Peristéra", "Psathúra", "Kyrá Panagía", "Giúra", "Pipérion", "Skantzúra", "Skyros", "Skíros", "Skirópoula", "Sarakinón", "Euböa", "Évvia", "Uzun Ada", "Lesbos", "Alibey adas?", "Chios", "Psará", "Ikaria", "Fourni", "Samos", "Salamis", "Äjina/Ägina", "Póros", "Sapiéntza", "Schíza", "Benétiko", "Elafónisos", "Kythera", "Antikythera", "Vourtzi", "Spetsä", "Spétses", "Dokós", "Hydra", "Ídra", "Kykladen", "Amorgós", "Anafi", "Ándros", "Antiparos", "Delos", "Folégandros", "Gyáros", "Íos", "Kea", "Kímolos", "Makrónisos", "Makrónissos", "Mílos", "Náxos", "Paros", "Kythnos", "Kíthnos", "Mýkonos", "Serifos", "Sifnos", "Síkinos", "Sýros", "Tínos", "Thíra", "Thirasía", "Therasia", "Aspronísi", "Nea Kameni", "Palea Kameni", "Iráklia", "Schinoússa", "Epano Koufonissi", "Kato Koufonissi", "Glaros", "Glaronissi", "Kéros", "Antikeros", "Epano Antikeri", "Drima", "Kato Antikeri", "Donoússa", "Dodekanes", "Agathonisi", "Arki", "Astipalaia", "Chálki", "Khalki", "Kalymnos", "Karpathos", "Kasos", "Kastellórizo", "Kós", "Leros", "Lero", "Lipsi", "Nisyros", "Patmos", "Pserimos", "Rhodos", "Sými", "Telendos", "Tilos", "Giali", "Yiali", "Rhodos", "Kreta", "Afsia", "Aloni", "Pascha Liman", "?mral?", "Inseln ?stanbuls", "Kalolimui", "Kulali", "Marmara", "Türkeli adas?", "", "Sasyk", "Kunduk", "Sagany", "Alibej", "Dolgij", "Tendrovskaja kosa", "Dscharilgatsch", "Birutschi", "Obilotschnaja kosa", "Berdanskaja kosa", "Zypern", "Prinz-Eduard", "Cape Breton", "Anticosti", "Îles de la Madeleine", "Neufundland", "Fogo Island", "St. Pierre", "Miquelon", "Nantucket", "Martha's Vineyard", "Rhode Island", "Long Island", "Manhattan", "Hatteras", "Sea Islands", "Florida Keys", "Bahamas", "Kuba", "Hispaniola", "Puerto Rico", "Jamaika", "Bouvetinsel", "Gough", "Inaccessible", "Tristan da Cunha", "Trindade", "St. Helena", "Ascension", "Fernando de Noronha", "Sao Paulo", "Tomé und Príncipe", "Annobón", "Bioko", "Sankt-Peter-und-Sankt-Pauls-Felsen", "Queimada Grande", "Diego-Ramirez", "Falkland", "Feuerland", "Shadw?n", "Nora", "Dahlak", "Qishran", "Faras?n", "Hanish", "Great Hanish", "Little Hanish", "Jabal Zuqur", "Jabal al Zair", "Jabal Zal Tair", "Humar", "Muqayshit", "Bahrain", "Faylakah", "B?b?y?n", "Jaz?reh ye Kh?rk", "Jaz?reh ye K?sh", "Jaz?reh Qeshm", "Sokotra", "Kuria-Muria", "al-Marirah", "Sansibar", "Pemba", "Mafia", "Seychellen", "Mahé", "Praslin", "La Digue", "Amiranten", "Aldabra", "Cosmoledo", "Farquhar", "Komoren", "Grande Comore", "Anjouan", "Nzwani", "Moheli", "Mwali", "Mayotte", "Mahore", "Maore", "Îles Glorieuses", "Madagaskar", "Maskarenen", "Reunion", "Mauritius", "Rodrigues", "Cargados", "Lakkadiven", "Malediven", "Tschagos-Archipel", "Diego Garcia", "Kokosinseln/Keelinginseln", "Weihnachtsinsel", "Sri Lanka", "Ceylon", "Andamanen", "Nikobaren", "Mergui", "Koh Phayam", "Koh Chan", "Phuket", "Koh Yao Yai", "Koh Lanta", "Koh Muk", "Koh Libong", "Koh Sukhon", "Koh Kerutao", "Lankawi", "Pinang", "Prinz-Edward", "Crozetinseln", "Kerguelen", "Heard", "McDonald", "Neu Amsterdam", "St. Paul", "Große Sunda", "Borneo", "Kalimantan", "Sumatra", "Sulawesi", "Java", "Indonesien", "Kleine Sunda", "Bali", "Timor", "Molukken", "Philippinen", "Mindanao", "Luzon", "Palawan", "Leyte", "Sonstige", "Anamba", "Nias", "Hainan", "Honsh?", "Hokkaid?", "Kyushu", "Oshima", "Shikoku", "Tsushima", "Kurilen", "Paracel", "Ryukyu", "Sachalin", "Spratly", "Taiwan", "Phangan", "Phuket", "Samui", "Tao", "Kommandeur", "Aleuten", "Kodiak", "Königin-Charlotte", "Vancouver Island", "Guadalupe", "Revillagigedo", "Revillagigedo", "Clipperton", "Cocos", "Galápagos", "Juan-Fernández", "Chiloé", "", "", "Ozeanien", "Sala y Gomez", "Osterinsel", "Pitcairn", "Französisch-Polynesien", "Marquesas", "Tuamotu", "Gesellschaftsinseln", "Cookinseln", "Hawaii", "Johnston", "Wake", "Bonin", "Marianen", "Guam", "Karolinen", "Palau", "Eniwetok", "Marshallinsel", "Bikini-Atoll", "Nauru", "Gilbert", "Tuvalu", "Ellice", "Phoenix", "Tokelau", "Samoa", "West-Samoa", "Wallis", "Futuna", "Niue", "Tonga", "Fidschi", "Vanuatu", "Salomonen", "Bismarck", "Neuguinea", "Neukaledonien", "Norfolkinsel", "Kermadecinseln", "Neuseeland", "Nordinsel", "Südinsel", "Antipoden", "Tasmanien", "Alexander-I.", "Balleny", "Berkner", "Ross", "Südgeorgien", "Fraueninsel", "Heidseeinseln", "Herreninsel", "Lindau", "Mainau", "L'Ile d'Ogoz", "Reichenau", "Roseninsel", "Schnäggei", "Schwanau", "Ufenau"};
	}
	
	private String[] getSuffix(){
//		uncomment to use external properties file
//		String inselsuffixe = Messages.getString("ZufallsNamen.Inselsuffixe"); //$NON-NLS-1$
//		String delim_inselsuffixe = Messages.getString("ZufallsNamen.Delim_Inselsuffixe"); //$NON-NLS-1$
//		StringTokenizer st = new StringTokenizer(inselsuffixe, delim_inselsuffixe); //$NON-NLS-1$
//		String [] result = new String[st.countTokens()];
//		int counter = 0;
//		while(st.hasMoreTokens()) {
//			result[counter++] = st.nextToken();
//		}
//		return result;
		
		return new String [] {" Grove"," Island", "oog", " Felsen",  " Grande", "'s Vineyard", "er Hallig", "strand", "'s Home", " del Sol", " Rock", " Reef", "'s Reef", "'s Place", " Harbour", "der Höft", " Cave", " Cavern", " el Grotto"};
	}
	
	private String[] getPraefix() {
//		uncomment to use external properties file
//		String inselpraefixe = Messages.getString("ZufallsNamen.Inselpraefixe"); //$NON-NLS-1$
//		String delim_inselpraefixe = Messages.getString("ZufallsNamen.Delim_Inselpraefixe"); //$NON-NLS-1$
//		StringTokenizer st = new StringTokenizer(inselpraefixe, delim_inselpraefixe); //$NON-NLS-1$
//		String [] result = new String[st.countTokens()];
//		int counter = 0;
//		while(st.hasMoreTokens()) {
//			result[counter++] = st.nextToken();
//		}
//		return result;

		return new String[] {"Porta de ", "Große ", "La " , "Le ", "Little ", "Cruz de ", "St. ", "Îles de la ", "Isla del ", "Great ", "Santo ", "Sao ", ""};
	}
}
 