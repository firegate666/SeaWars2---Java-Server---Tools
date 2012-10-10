/*
 * Created on 09.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.seawars.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;

import de.seawars.gui.constants.PaintActionName;

/**
 * PixelFarm stellt das Handylogo-Display dar.
 *
 * @author Marco Behnke
 *
 */
public class PixelFarm extends JPanel {

	private Mainframe _app;
	private PaintActionName _paintAction;
	private JPanelWhite _internal;

	private int _width=20;
	private int _height=20;

	private Pixel [][] _pixels = new Pixel[20][20];


	/**
	 * Returns the application where the PixelFarm is set.
	 *
	 * @return Logoeditor application
	 */
	public Mainframe getApp() {
		return _app;
	}

	private void setPixel(Pixel p,int x,int y){
		_pixels[x][y]=p;
	}

	/**
	 * Returns pixel in pixelfarm at point (x, y)
	 *
	 * @param pixel at (x, y)
	 */
	public Pixel getPixel(int x,int y){
		return _pixels[x][y];
	}


	/**
	 * Gibt die aktuelle Zeichenart zurück, z.B. Zeichenstift oder Radiergummi.
	 *
	 * @return Zeichenart
	 */
	public PaintActionName getPaintAction() {
		return _paintAction;
	}

	/**
	 * Setzt die aktuelle Zeichenart, z.B. Zeichenstift oder Radiergummi.
	 *
	 * @param pa Zeichenart
	 */
	public void setPaintAction(PaintActionName pa) {
		_paintAction = pa;
	}

	/**
	 * Setzt alle Pixel auf die Farbe schwarz.
	 */
	public void clear() {
		for(int j=0;j<_height;j++)
			for(int i=0;i<_width;i++)
				_pixels[i][j].unset();
	}

	/**
	 * Ändert die Farbe aller Pixel von grün auf schwarz bzw. von schwarz auf grün
	 */
	public void invert() {
		for(int j=0;j<_height;j++)
			for(int i=0;i<_width;i++)
				_pixels[i][j].invert();
	}

	public void initPixels() {
		for(int j=0;j<_height;j++)
			for(int i=0;i<_width;i++) {
				_pixels[i][j]=new Pixel(i,j);
				add(_pixels[i][j]);
			}
	}

	/**
	 * Public constructor
	 */
	public PixelFarm(Mainframe app) {
		setOpaque(true);
		setBackground(new Color(0,0,0));
		GridLayout layout = new GridLayout(_height,_width);
		layout.setHgap( 2);
		layout.setVgap( 2);
		setLayout(layout);
		_app=app;
		initPixels();
		invert();
		clear();
	}
}