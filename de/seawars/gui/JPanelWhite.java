package de.seawars.gui;
import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Default white panel
 * 
 * @author Marco Behnke
 * 
 */
public class JPanelWhite extends JPanel {

	private void initialize() {
		this.setOpaque(true );
		this.setBackground( new Color(255,255,255));
	}
	
	/**
	 * Constructor for JPanelWhite.
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public JPanelWhite(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		initialize();
	}

	/**
	 * Constructor for JPanelWhite.
	 * @param layout
	 */
	public JPanelWhite(LayoutManager layout) {
		super(layout);
		initialize();
	}

	/**
	 * Constructor for JPanelWhite.
	 * @param isDoubleBuffered
	 */
	public JPanelWhite(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		initialize();
	}

	/**
	 * Constructor for JPanelWhite.
	 */
	public JPanelWhite() {
		super();
		initialize();
	}

}
