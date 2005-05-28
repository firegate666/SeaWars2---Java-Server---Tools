/*
 * Created on 09.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.seawars;

import de.seawars.gui.Mainframe;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SeaWarsUtilities {
	private Mainframe gui;
	
	public SeaWarsUtilities(){
		this.initialize();
	}
	private void initialize(){
		gui = new Mainframe();
		gui.setBounds(0,0,200,200 );
		gui.show();
	}
	
	public static void main(String[] args) {
		SeaWarsUtilities app = new SeaWarsUtilities();
	}
}
