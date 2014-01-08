package de.ulfbiallas.lantexter.model;


import java.util.ArrayList;
import java.util.Observable;

import de.ulfbiallas.lantexter.model.message.IMessage;

/**
 * Singleton class which represents a list of all chat messages and notifications.
 * 
 * @author Ulf Biallas
 *
 */
public class ChatHistory extends Observable  {

	/** The singleton object of the class. */
	private static final ChatHistory chatHistory = new ChatHistory();
	
	/** The message list. */
	private ArrayList<IMessage> history;
	
	
	/**
	 * Constructor. Creates an empty message list.
	 */
	private ChatHistory() {
		history = new ArrayList<IMessage>();
	}
	
	/** 
	 * Returns the singleton object.
	 * 
	 * @return The singleton object.
	 */
	public static ChatHistory getInstance() {
		return chatHistory;
	}
	
	/**
	 * Adds a new message to the chat history.
	 * 
	 * @param message The new message to add.
	 */
	public void addMessage(IMessage message) {
		history.add(message);
		setChanged();
		notifyObservers(ModelNotification.MESSAGE_RECEIVED);
	}
	
	/**
	 * Returns the complete message history.
	 * 
	 * @return The message history.
	 */
	public ArrayList<IMessage> getHistory() {
		return history;
	}
	
	/**
	 * Returns a String which contains all messages and notifications (HTML-formatted).
	 * 
	 * @return A HTML-formatted String with all messages and notifications.
	 */
	public String getChatText() {
		String chatText = "";
		String latestMessage;
		IMessage msg;
		for(int k=0; k<history.size(); ++k) {
			msg = history.get(k);
			latestMessage = msg.getMessageTextAsHTML();
			chatText += latestMessage + "<br><br>";
		}
		return chatText;
	}
	
	/**
	 * Returns the latest message or notification.
	 * 
	 * @return The latest message or notification.
	 */
	public String getLatestMessage() {
		if(history.size()>0) {
			return history.get(history.size()-1).getMessageText();
		} else {
			return null;
		}
	}
}
