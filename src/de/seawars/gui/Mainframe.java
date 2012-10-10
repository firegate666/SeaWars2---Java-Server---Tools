package de.seawars.gui;

import java.awt.BorderLayout;

import de.mb.swing.JDefaultFrame;

public class Mainframe extends JDefaultFrame{

	PixelFarm iv=new PixelFarm(this);;

	public Mainframe(){
		super();
		this.getContentPane().setLayout(new BorderLayout() );
		this.getContentPane().add(iv,BorderLayout.CENTER );
	}

}
