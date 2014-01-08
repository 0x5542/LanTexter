package de.ulfbiallas.lantexter.model.network;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import de.ulfbiallas.lantexter.model.ModelNotification;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * Class which opens an UDP port and starts a separate thread to receive UDP datagrams.
 * 
 * @author Ulf Biallas
 *
 */
public class ServerThread extends Thread {

	private IPacketProcessor packetProcessor;
	private DatagramSocket socket;
	
	
	/**
	 * Constructor. Opens an UDP port and starts the thread to receive datagrams.
	 * 
	 * @param packetProcessor Class which processes the incoming UDP datagrams.
	 * @param port The port to open.
	 */
	public ServerThread(IPacketProcessor packetProcessor, int port) {
		super();
		this.packetProcessor = packetProcessor;
		
		try {
			socket = new DatagramSocket(port);
			start();
		} catch (java.lang.IllegalArgumentException e) {
			packetProcessor.onError(ModelNotification.CONNECTION_ERROR);
		} catch (SocketException e) {
			packetProcessor.onError(ModelNotification.CONNECTION_ERROR);
		}
	}
	
	/**
	 * Stops receiving datagrams and closes the UDP port.
	 */
	public void shutdown() {
		interrupt();
		socket.close();
	}
	
	/**
	 * Waits for incoming UDP datagrams.
	 */
	public void run() {

		packetProcessor.onSuccessfulStarted();
				
	    while ( true ) { 	   
	    	try {
   		
	    		DatagramPacket packet = new DatagramPacket( new byte[1024], 1024 ); 	      
				socket.receive( packet );
	
				InetAddress address = packet.getAddress(); 			  
				int         port    = packet.getPort(); 
				int         len     = packet.getLength(); 
				byte[]      data    = packet.getData(); 
				 
				String msg = new String( data, 0, len );
				JSONObject jsonMsg = (JSONObject) JSONSerializer.toJSON( msg );
				      
				packetProcessor.processMessage(jsonMsg, address, port);
				      
		    // Ignore invalid packets
			} catch (IOException e) {

			} catch (JSONException e) {

			}		      		      
	    } 
	}
	
}
