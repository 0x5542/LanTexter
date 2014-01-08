package de.ulfbiallas.lantexter.model;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

/**
 * Singleton class to manage the list of chat participants.
 * 
 * @author Ulf Biallas
 *
 */
public class ParticipantList extends Observable {

	/** The singleton object of the class. */
	private static ParticipantList participantList = new ParticipantList();
	
	/** Map with all participants. The key is given by the method getId() of the class Participant */
	private HashMap<String, Participant> participants = new HashMap<String, Participant>();
	
	/** A List of possible text colors. */
	private ArrayList<String> textColors;
	
	
	/** Private constructor which initializes the list of text colors. */
	private ParticipantList() {
		textColors = new ArrayList<String>();
		textColors.add("0000FF");
		textColors.add("FF0000");
		textColors.add("00FF00");
		textColors.add("FF00FF");
		textColors.add("00FFFF");
		textColors.add("FFFF00");
	}
	
	/** 
	 * Returns the singleton object.
	 * 
	 * @return The singleton object.
	 */
	public static ParticipantList getInstance() {
		return participantList;
	}
	
	/**
	 * Creates a new participant. 
	 * 
	 * @param id The id of the new participant.
	 * @param name The name of the new participant.
	 * @param addr The InetAddress of the new participant.
	 * @return The new participant.
	 */
	public Participant createNewParticipant(String id, String name, InetAddress addr) {
		String color = textColors.get(participants.size() % textColors.size());
		return new Participant(id, name, addr, color);
	}
	
	/**
	 * Returns whether the list contains a specific participant.
	 * 
	 * @param id The id of the participant.
	 * @return A Boolean which is true if the list contains the specific participant.
	 */
	public Boolean containsParticipant(String id) {
		return participants.containsKey(id);
	}
	
	/**
	 * Returns whether a specific participant is online.
	 * 
	 * @param id The id of the participant.
	 * @return A Boolean which is true if the specific participant is online.
	 */
	public Boolean isOnline(String id) {
		return participants.get(id).isOnline();
	}
	
	/**
	 * Adds an participant to the participant list.
	 * 
	 * @param participant The participant to add.
	 */
	public void addParticipant(Participant participant) {
		participants.put(participant.getId(), participant);
		setChanged();
		notifyObservers(ModelNotification.LIST_OF_PARTICIPANTS_CHANGED);
	}
	
	/**
	 * Sets a specific participant online.
	 * 
	 * @param id The id of the participant.
	 */
	public void setParticipantOnline(String id) {
		participants.get(id).setOnline();
		setChanged();
		notifyObservers(ModelNotification.LIST_OF_PARTICIPANTS_CHANGED);		
	}
	
	/**
	 * Sets a specific participant offline.
	 * 
	 * @param id The id of the participant.
	 */
	public void setParticipantOffline(String id) {
		if(participants.containsKey(id)) {
			participants.get(id).setOffline();
		}
		setChanged();
		notifyObservers(ModelNotification.LIST_OF_PARTICIPANTS_CHANGED);
	}	
	
	/**
	 * Changes the name of a specific participant.
	 * 
	 * @param id The id of the participant.
	 * @param newName The new name of the participant.
	 */
	public void changeName(String id, String newName) {
		if(participants.containsKey(id)) {
			participants.get(id).setName(newName);
			setChanged();
			notifyObservers(ModelNotification.LIST_OF_PARTICIPANTS_CHANGED);			
			setChanged();
			notifyObservers(ModelNotification.NAME_CHANGED);			
		}
	}
	
	/**
	 * Returns the name of a specific participant
	 * 
	 * @param id The id of the participant.
	 * @return The name of a specific participant.
	 */
	public String getNameOfParticipant(String id) {
		return participants.get(id).getName();
	}
	
	/**
	 * Returns the text color of a specific participant
	 * 
	 * @param id The id of the participant.
	 * @return The text color of a specific participant.
	 */	
	public String getTextColorOfParticipant(String id) {
		return participants.get(id).getTextColor();
	}
	
	/**
	 * Returns a list of all participants which are online.
	 * 
	 * @return The list of participants.
	 */
	public ArrayList<Participant> getParticipants() {
		ArrayList<Participant> participantsList = new ArrayList<Participant>();
		Iterator<String> participantsIter = participants.keySet().iterator();
		Participant partp;
		while(participantsIter.hasNext()) {
			partp = participants.get(participantsIter.next());
			if(partp.isOnline()) participantsList.add(partp);
		}		
		return participantsList;
	}
	
	/**
	 * Returns a list of the names of all participants which are online.
	 * 
	 * @return The list of names of the participants.
	 */
	public ArrayList<String> getParticipantNames() {
		ArrayList<String> participantsList = new ArrayList<String>();
		Iterator<String> participantsIter = participants.keySet().iterator();
		Participant partp;
		while(participantsIter.hasNext()) {
			partp = participants.get(participantsIter.next());
			if(partp.isOnline()) participantsList.add(partp.getName());
		}		
		return participantsList;
	}
	
	/**
	 * Sets all participants offline.
	 */
	public void setAllParticipantsOffline() {
		Iterator<String> participantsIter = participants.keySet().iterator();
		Participant partp;
		while(participantsIter.hasNext()) {
			partp = participants.get(participantsIter.next());
			partp.setOffline();
		}
		setChanged();
		notifyObservers(ModelNotification.LIST_OF_PARTICIPANTS_CHANGED);
	}
	
}
