package de.ulfbiallas.lantexter.model.network;

import net.sf.json.JSONObject;

/**
 * Tool class which offers functions to create JSON messages.
 * 
 * @author Ulf Biallas
 *
 */
public class JsonTools {

	/**
	 * Creates the network message to find out who is online.
	 * 
	 * @param myName The name of the user.
	 * @return The network message as JSON string.
	 */
	public static String createWhoIsOnlineMessage(String myName) {

		JSONObject jsonMsg = new JSONObject()  
        .element( "code", "1" )
        .element( "name", myName )
        ;
		
		return jsonMsg.toString();
	}
	
	/**
	 * Creates the network message to notify other participants of being online.
	 * 
	 * @param myName The name of the user.
	 * @return The network message as JSON string.
	 */
	public static String createIAmOnlineMessage(String myName) {

		JSONObject jsonMsg = new JSONObject()  
        .element( "code", "2" )
        .element( "name", myName )
        ;
		
		return jsonMsg.toString();
	}	
	
	/**
	 * Creates the network message for sending a chat message.
	 * 
	 * @param myName The name of the user.
	 * @param msg The message to send.
	 * @return The network message as JSON string.
	 */
	public static String createChatMessage(String myName, String msg) {
		
		JSONObject jsonMsg = new JSONObject()  
        .element( "code", "3" )
        .element( "name", myName )
        .element( "text", msg )
        ;
		
		return jsonMsg.toString();	
	}

	/**
	 * Creates the network message to notify other participants 
	 * of changing the name.
	 * 
	 * @param myNewName The new name of the user.
	 * @return The network message as JSON string.
	 */
	public static String createNewNameMessage(String myNewName) {
		
		JSONObject jsonMsg = new JSONObject()  
        .element( "code", "4" )
        .element( "name", myNewName )
        ;
		
		return jsonMsg.toString();	
	}
	
	/**
	 * Creates the network message to notify other participants 
	 * of leaving the chat.
	 * 
	 * @param myName The name of the user.
	 * @return The network message as JSON string.
	 */
	public static String createChatLeftMessage(String myName) {
		
		JSONObject jsonMsg = new JSONObject()  
        .element( "code", "5" )
        .element( "name", myName );
        ;
		
		return jsonMsg.toString();	
	}
	
	
}
