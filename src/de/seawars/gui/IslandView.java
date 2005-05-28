/*
 * Created on 09.01.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.seawars.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IslandView extends JPanel{
	public IslandView(){
		setOpaque(true);
		setBackground(new Color(100,100,100));
		GridLayout layout = new GridLayout(20,20);
		layout.setHgap( 1);
		layout.setVgap( 1);
		setLayout(layout);
		this.addMouseListener(new TogglePixel(this) );
	}
	
	class TogglePixel implements MouseListener{
		IslandView app;
		public TogglePixel(IslandView app){
			this.app = app;
		}
			/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			Point p = arg0.getPoint();
			

		}
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
}
}
