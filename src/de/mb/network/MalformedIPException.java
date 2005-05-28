package de.mb.network;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MalformedIPException extends Exception {

	/**
	 * Constructor for MalformedIPException.
	 */
	public MalformedIPException() {
		super();
	}

	/**
	 * Constructor for MalformedIPException.
	 * @param message
	 */
	public MalformedIPException(String message) {
		super(message);
	}

	/**
	 * Constructor for MalformedIPException.
	 * @param message
	 * @param cause
	 */
	public MalformedIPException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor for MalformedIPException.
	 * @param cause
	 */
	public MalformedIPException(Throwable cause) {
		super(cause);
	}

}
