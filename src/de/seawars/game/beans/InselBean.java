/*
 * Created on 29.05.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.seawars.game.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InselBean implements Serializable {
	private int id;
	private String name;
	private int groesse;
	private int x_pos;
	private int y_pos;
	private int spieler_id;
	private int archipel_id;
	private Date timestamp;
	private int lager_id;
	/**
	 * @return Returns the archipel_id.
	 */
	public int getArchipel_id() {
		return archipel_id;
	}
	/**
	 * @param archipel_id The archipel_id to set.
	 */
	public void setArchipel_id(int archipel_id) {
		this.archipel_id = archipel_id;
	}
	/**
	 * @return Returns the groesse.
	 */
	public int getGroesse() {
		return groesse;
	}
	/**
	 * @param groesse The groesse to set.
	 */
	public void setGroesse(int groesse) {
		this.groesse = groesse;
	}
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
	 * @return Returns the lager_id.
	 */
	public int getLager_id() {
		return lager_id;
	}
	/**
	 * @param lager_id The lager_id to set.
	 */
	public void setLager_id(int lager_id) {
		this.lager_id = lager_id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the spieler_id.
	 */
	public int getSpieler_id() {
		return spieler_id;
	}
	/**
	 * @param spieler_id The spieler_id to set.
	 */
	public void setSpieler_id(int spieler_id) {
		this.spieler_id = spieler_id;
	}
	/**
	 * @return Returns the timestamp.
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp The timestamp to set.
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return Returns the x_pos.
	 */
	public int getX_pos() {
		return x_pos;
	}
	/**
	 * @param x_pos The x_pos to set.
	 */
	public void setX_pos(int x_pos) {
		this.x_pos = x_pos;
	}
	/**
	 * @return Returns the y_pos.
	 */
	public int getY_pos() {
		return y_pos;
	}
	/**
	 * @param y_pos The y_pos to set.
	 */
	public void setY_pos(int y_pos) {
		this.y_pos = y_pos;
	}

}
