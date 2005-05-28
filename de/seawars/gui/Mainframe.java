/*
 * Created on 09.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.seawars.gui;

import java.awt.BorderLayout;

import de.mb.swing.JDefaultFrame;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mainframe extends JDefaultFrame{

	PixelFarm iv=new PixelFarm(this);;
	
	public void setBounds(int arg0, int arg1, int arg2, int arg3) {
		super.setBounds(arg0, arg1, arg2, arg3);
	}
	public void show(boolean arg0) {
		super.show(arg0);
	}
	public Mainframe(){
		super();
		initialize(); 
	}
	public void initialize(){
		this.getContentPane().setLayout(new BorderLayout() );
		this.getContentPane().add(iv,BorderLayout.CENTER );
	}
}
