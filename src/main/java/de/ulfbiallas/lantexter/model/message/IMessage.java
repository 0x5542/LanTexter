package de.ulfbiallas.lantexter.model.message;

/**
 * Interface for a message in the chat history.
 * 
 * @author Ulf Biallas
 *
 */
public interface IMessage {
	
	/**
	 * Returns the text of the message.
	 * 
	 * @return The text of the message.
	 */
	public String getMessageText();
	
	/**
	 * Returns the HTML-formatted text of the message.
	 * 
	 * @return The HTML-formatted text of the message.
	 */	
	public String getMessageTextAsHTML();
}
