package de.ulfbiallas.lantexter.model.message;

import de.ulfbiallas.lantexter.model.Constants;
import de.ulfbiallas.lantexter.model.Settings;

/**
 * Class for a chat message which is automatically send when 
 * a new participant joins the chat.
 * 
 * @author Ulf Biallas
 *
 */
public class NewParticipantMessage implements IMessage {

	/** The name of the new participant. */
	private String name;
	
	private Settings settings = Settings.getInstance();
	
	
	/**
	 * Constructor. Creates a new chat message which
	 * informs about a new participant.
	 * 
	 * @param name The name of the new participant.
	 */
	public NewParticipantMessage(String name) {
		this.name = name;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getMessageText() {
		String msg = name + " " + settings.getLanguage().getLabel("text_newparticipant");
		return msg;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getMessageTextAsHTML() {
		String msg = "";
		msg += "<font color="+Constants.NOTIFICATION_COLOR+">";
		msg += name + " " + settings.getLanguage().getLabel("text_newparticipant");
		msg += "</font>";
		return msg;
	}

}
