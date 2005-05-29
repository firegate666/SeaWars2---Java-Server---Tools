/*
 * Created on 29.05.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.seawars.game.beans;

import java.io.Serializable;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LagerBean implements Serializable {
	private int id;
	private int kapazitaet;
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the kapazitaet.
	 */
	public int getKapazitaet() {
		return kapazitaet;
	}
	/**
	 * @param kapazitaet The kapazitaet to set.
	 */
	public void setKapazitaet(int kapazitaet) {
		this.kapazitaet = kapazitaet;
	}
}
