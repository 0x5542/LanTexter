package de.ulfbiallas.lantexter.model.message;

import de.ulfbiallas.lantexter.model.Constants;
import de.ulfbiallas.lantexter.model.Settings;

/**
 * Class for a chat message which is automatically send when 
 * a participant leaves the chat.
 * 
 * @author Ulf Biallas
 *
 */
public class ChatLeftMessage implements IMessage {

	/** The name of the user which has left the chat. */
	private String name;
	
	private Settings settings;
	
	
	/**
	 * Constructor. Creates a new chat message which
	 * informs about the leaving of a participant.
	 * 
	 * @param name The name of the user which has left the chat.
	 */
	public ChatLeftMessage(String name) {
		this.name = name;
		settings = Settings.getInstance();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getMessageText() {
		String msg = name + " " + settings.getLanguage().getLabel("text_leftchat");
		return msg;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getMessageTextAsHTML() {
		String msg = "";
		msg += "<font color="+Constants.NOTIFICATION_COLOR+">";
		msg += name + " " + settings.getLanguage().getLabel("text_leftchat");
		msg += "</font>";
		return msg;
	}

}
