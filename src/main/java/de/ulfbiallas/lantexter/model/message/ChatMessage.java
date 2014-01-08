package de.ulfbiallas.lantexter.model.message;

import java.util.Date;

import de.ulfbiallas.lantexter.model.ParticipantList;


/**
 * Class for chat message which contains a part of the conversation.
 * 
 * @author Ulf Biallas
 *
 */
public class ChatMessage implements IMessage {
	
	/** The id of the participant which is the author of this message. */
	private String authorId;
	
	/** The text of the message. */
	private String message;
	
	/** The time when the message was received. */
	private Date time;
	
	private ParticipantList participantList = ParticipantList.getInstance();
	
	/**
	 * Creates a new chat text message.
	 * 
	 * @param authorId The id of the participant which is the author.
	 * @param message The text of the message.
	 */
	public ChatMessage(String authorId, String message) {
		this.authorId = authorId;
		this.message = message;
		time = new Date();
	}
	
	/**
	 * Returns the id of the participant which is the author.
	 * 
	 * @return The id of the author.
	 */
	public String getAuthorId() {
		return authorId;
	}

	/**
	 * Returns the pure text of the message.
	 * 
	 * @return The text of the message.
	 */
	public String getMessage() {
		return message;
	}

	/** 
	 * Returns the time when the message was received.
	 * 
	 * @return The time when the message was received.
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getMessageText() {
		String msg = "";
		msg += participantList.getNameOfParticipant(getAuthorId()) + " ("+getTime()+"):\n";
		msg += getMessage();
		return msg;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getMessageTextAsHTML() {
		String msg = "";
		msg += "<font color="+participantList.getTextColorOfParticipant(getAuthorId())+">";
		msg += participantList.getNameOfParticipant(getAuthorId()) + " ("+getTime()+"):<br>";
		msg += getMessage().replace("\n", "<br>");
		msg += "</font>";
		return msg;
	}
	
}
