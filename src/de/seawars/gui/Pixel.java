/*
 * Created on 09.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.seawars.gui;

import java.awt.Color;

import javax.swing.JLabel;

import de.seawars.gui.listener.ClickPixelMouseListener;


/**
 * Pixel stellt einen Pixel auf dem Display dar.
 *
 * @author Marco Behnke
 *
 */
public class Pixel extends JLabel {

	boolean _set;
	private int _x;
	private int _y;

	private PixelFarm _pixelFarm;

	private void setXY(int x,int y) {
		setX(x);
		setY(y);
	}

	/**
	 * Returns if pixel is set
	 *
	 * @return pixel set?
	 */
	public boolean isSet() {
		return _set;
	}

	/**
	 * Gibt die X-Koordinate des Pixels auf dem Display zurück.
	 *
	 * @return X-Koordinate in der PixelFarm
	 */
	public int getX() {
		return _x;
	}

	/**
	 * Gibt die Y-Koordinate des Pixels auf dem Display zurück.
	 *
	 * @return Y-Koordinate in der PixelFarm
	 */
	public int getY() {
		return _y;
	}

	private void setX(int x) {
		this._x = x;
	}


	private void setY(int y) {
		this._y = y;
	}

	/**
	 * Ändert die Farbe aller Pixel von grün auf schwarz bzw. von schwarz auf grün
	 */
	public void invert() {
		if (isSet())
			unset();
		else
			set();

	}

	/**
	 * Public constructor
	 *
	 * @param x X-Koordinate in der PixelFarm
	 * @param y Y-Koordinate in der PixelFarm
	 */
	public Pixel(int x,int y) {
		initialize(x,y);
	}

	private void initialize(int x, int y) {
		setXY(x,y);
		setOpaque(true);
		setBackground(new Color(0, 0, 255));
		unset();
		addMouseListener(new ClickPixelMouseListener(this));
	}

	/**
	 * Setzt die Farbe des Pixels auf grün.
	 */
	public void unset() {
		if(isSet()){
			_set = false;
			setBackground(new Color(0, 0, 255));
		}

	}
	/**
	 * Setzt die Farbe des Pixels auf schwarz.
	 */
	public void set() {
		if(!isSet()){
			_set = true;
			setBackground(new Color(0, 255, 0));
		}
	}

	// Interne Klassen



}