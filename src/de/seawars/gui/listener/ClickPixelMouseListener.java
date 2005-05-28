/*
 * Created on 09.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.seawars.gui.listener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import de.seawars.gui.Pixel;
import de.seawars.gui.constants.*;

public class ClickPixelMouseListener implements MouseListener {

		private Pixel _pixel;
		private boolean clicked = false;

		/**
		 * Public constructor
		 */
		public ClickPixelMouseListener(Pixel app) {
			_pixel=app;
		}
		
		/**
		 * Returns Pixel where listener has been activated
		 * 
		 * @return Pixel
		 */
		public Pixel getPixel() {
			return _pixel;
		}

		/**
		 * Modifies PixelFarm pixels depending on the PaintAction selected
		 */
		private void draw() {
			PaintActionName pa=PaintActionName.PEN ;
			
			if(pa==PaintActionName.PEN ) getPixel().invert();
			else if(pa==PaintActionName.ERASER ) getPixel().unset();
			else;
		}

		/**
		 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
		}

		/**
		 * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
			if (e.getModifiers() == e.BUTTON1_MASK)
				draw();
		}

		/**
		 * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {
		}

		/**
		 * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			if (e.getModifiers() == e.BUTTON1_MASK)
				draw();
		}

		/**
		 * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
		}

	}