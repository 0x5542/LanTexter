package de.ulfbiallas.lantexter.model.network;

import java.net.InetAddress;

import de.ulfbiallas.lantexter.model.ModelNotification;

import net.sf.json.JSONObject;

/**
 * Interface for classes which can process incoming chat messages.
 * 
 * @author Ulf Biallas
 *
 */
public interface IPacketProcessor {

	/**
	 * Processes the incoming chat message.
	 * 
	 * @param msg The message.
	 * @param addr The sender of the the message.
	 * @param port The port of the sender.
	 */
	void processMessage(JSONObject msg, InetAddress addr, int port);
	
	/**
	 * Notifies the observers of an error during the connection process
	 * 
	 * @param notification A detailed notification.
	 */
	void onError(ModelNotification notification);
	
	/**
	 * Notifies the observers of the establishment of the connection
	 */
	void onSuccessfulStarted();
	
}
