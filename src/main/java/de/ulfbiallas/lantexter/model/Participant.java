package de.ulfbiallas.lantexter.model;

import java.net.InetAddress;

/**
 * Class with represents a chat participant.
 * 
 * @author Ulf Biallas
 *
 */
public class Participant {

	/** The id of the participant (IP address). */
	private String id;
	
	/** The (nick) name of the participant. */
	private String name;
	
	/** The InetAddress of the participant. */
	private InetAddress addr;
	
	/** The timestamp of the last alive signal of the participant. */
	private long lastSignal;
	
	/** The text color that belongs to this participant in the message view */
	private String textColor;
	
	
	/**
	 * Constructor. Sets the participant online.
	 * 
	 * @param id The id of the participant (IP address).
	 * @param name The (nick) name of the participant.
	 * @param addr The InetAddress of the participant.
	 */
	public Participant(String id, String name, InetAddress addr, String textColor) {
		this.id = id;
		this.name = name;
		this.addr = addr;
		this.textColor = textColor;
		lastSignal = System.currentTimeMillis();
	}
	
	/**
	 * Sets the participant online by refreshing the alive signal timestamp.
	 */
	public void setOnline() {
		lastSignal = System.currentTimeMillis();
	}
	
	/**  
	 * Sets the alive signal timestamp to a time in the past.
	 */
	public void setOffline() {
		lastSignal = System.currentTimeMillis()-2*Constants.NOT_ALIVE_INTERVAL*1000;
	}
	
	/**
	 * Determines whether a participant is online or not by checking its alive signal timestamp.
	 * 
	 * @return Boolean which is true if the participant is online.
	 */
	public Boolean isOnline() {
		long time = System.currentTimeMillis();
		if(time-lastSignal > Constants.NOT_ALIVE_INTERVAL*1000) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns the (nick) name of the participant.
	 * 
	 * @return The name of the participant.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets a new name.
	 * 
	 * @param name The new name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the id of the participant.
	 * 
	 * @return The id of the participant.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the InetAddress of the participant.
	 * 
	 * @return The InetAddress of the participant.
	 */
	public InetAddress getInetAddress() {
		return addr;
	}
	
	/**
	 * Returns the text color of the participant.
	 * 
	 * @return The text color of the participant.
	 */	
	public String getTextColor() {
		return textColor;
	}
	
	/**
	 * Converts the objects to a string which contains the name of the participant.
	 * 
	 * @return The name of the participant.
	 */
	public String toString() {
		return getName();
	}

}
