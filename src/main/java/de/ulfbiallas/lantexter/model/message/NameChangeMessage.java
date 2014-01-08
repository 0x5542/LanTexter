package de.ulfbiallas.lantexter.model.message;

import de.ulfbiallas.lantexter.model.Constants;
import de.ulfbiallas.lantexter.model.Settings;

/**
 * Class for a chat message which is automatically send when 
 * a participant changes his name.
 * 
 * @author Ulf Biallas
 *
 */
public class NameChangeMessage implements IMessage {

	/** The old name of the participant. */
	private String oldName;
	
	/** The new name of the participant. */
	private String newName;
	
	private Settings settings = Settings.getInstance();
	
	
	/**
	 * Constructor. Creates a new chat message which
	 * informs about the name change of a participant.
	 * 
	 * @param oldName The old name of the participant.
	 * @param newName The new name of the participant.
	 */
	public NameChangeMessage(String oldName, String newName) {
		this.oldName = oldName;
		this.newName = newName;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public String getMessageText() {
		String msg = oldName + " " + settings.getLanguage().getLabel("text_rename") + " " + newName;
		return msg;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getMessageTextAsHTML() {
		String msg = "";
		msg += "<font color="+Constants.NOTIFICATION_COLOR+">";
		msg += oldName + " " + settings.getLanguage().getLabel("text_rename") + " " + newName;
		msg += "</font>";
		return msg;
	}
	
}
