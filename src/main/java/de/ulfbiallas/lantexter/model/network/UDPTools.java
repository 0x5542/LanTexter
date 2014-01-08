package de.ulfbiallas.lantexter.model.network;


import java.io.IOException; 
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import de.ulfbiallas.lantexter.model.ModelNotification;



/**
 * Singleton tool class to send UDP datagrams.
 * 
 * @author Ulf Biallas
 *
 */
public class UDPTools { 
	
	private static final UDPTools udptools = new UDPTools();

	/** UDP Socket to send the datagrams. */
	private MulticastSocket datagramSocket;

	/** Port on the receiver side. */
	private int port;
	
	/** 
	 * Private constructor which does nothing. 
	 */
	private UDPTools() {
	}
	
	/** 
	 * Returns the singleton object.
	 * 
	 * @return The singleton object.
	 */
    public static UDPTools getInstance(){
        return udptools;
    }
	
    /**
     * Initializes the singleton class by Opening an UDP socket.
     * 
     * @param packetProcessor Class which processes the incoming UDP datagrams.
     * @param port Port on the receiver side.
     * 
     */
    public void init(IPacketProcessor packetProcessor, int port) {
    	this.port = port;
    	
		try {
			
			datagramSocket = new MulticastSocket();
			datagramSocket.setBroadcast(true);			
			System.out.println("datagramSocket: " + datagramSocket.getInetAddress());
			
		} catch (SocketException e) {
			packetProcessor.onError(ModelNotification.CONNECTION_ERROR);
		} catch (IOException e) {
			packetProcessor.onError(ModelNotification.CONNECTION_ERROR);
		}
	}
    
	/**
	 * Sends a byte array to a specific receiver.
	 * 
	 * @param addr Address of the receiver.
	 * @param msg Message to send.
	 */
	public void sendMsg(InetAddress addr, byte[] msg) {
		System.out.println("sendMsg to " + addr);
		try {
			DatagramPacket packet;
			packet = new DatagramPacket( msg, msg.length, addr, port ); 
			datagramSocket.send( packet );
		} catch (IOException e) {
			e.printStackTrace();
		} 		
	}
	
	/**
	 * Sends a text message to a specific IP.
	 * 
	 * @param ip The IP of the receiver.
	 * @param msg The text message.
	 */
	public void sendStringToIp(String ip, String msg) {	
		try {
			byte[] bmsg = msg.getBytes();
			InetAddress addr = InetAddress.getByName( ip );
			sendMsg(addr, bmsg);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
	}
	

}