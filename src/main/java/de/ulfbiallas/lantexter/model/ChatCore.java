package de.ulfbiallas.lantexter.model;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import de.ulfbiallas.lantexter.model.message.ChatLeftMessage;
import de.ulfbiallas.lantexter.model.message.ChatMessage;
import de.ulfbiallas.lantexter.model.message.NameChangeMessage;
import de.ulfbiallas.lantexter.model.message.NewParticipantMessage;
import de.ulfbiallas.lantexter.model.network.IPacketProcessor;
import de.ulfbiallas.lantexter.model.network.JsonTools;
import de.ulfbiallas.lantexter.model.network.ServerThread;
import de.ulfbiallas.lantexter.model.network.UDPTools;




import net.sf.json.JSONObject;


/**
 * Class which processes the incoming chat messages and 
 * automatically sends messages.
 * 
 * @author Ulf Biallas
 *
 */
public class ChatCore extends Observable implements IPacketProcessor {

	private UDPTools updtools;	
	private Settings settings;
	private ParticipantList participantList;
	private ChatHistory chatHistory;
	private ServerThread serverThread;
	
	/** Timer for an alive signal */
	private Timer broadcastTimer;
	
	/** Error flag which is set if a connection could not be established */
	private Boolean errorsWhileStarting = false;
	
	
	/**
	 * Constructor.
	 */
	public ChatCore() {
		updtools = UDPTools.getInstance();
		settings = Settings.getInstance();
		participantList =  ParticipantList.getInstance();
		chatHistory = ChatHistory.getInstance();
	}


	/**
	 * Starts the UDP server and the alive signal timer.
	 */
	public void startServer() {		
		updtools.init(this, Integer.parseInt(settings.getProperty("port")));
		errorsWhileStarting = false;
		serverThread = new ServerThread(this, Integer.parseInt(settings.getProperty("port")));
			
		// Send an alive signal as broadcast to find out who is online
		broadcastTimer = new Timer();
		broadcastTimer.schedule(new TimerTask(){
	         @Override
	         public void run() {
	        	 	UDPTools updtools = UDPTools.getInstance();
					String msg = JsonTools.createWhoIsOnlineMessage(settings.getProperty("name"));
					updtools.sendStringToIp("255.255.255.255", msg);
	         } 
	     }, 0, Constants.ALIVE_TIMER_INTERVAL * 1000);	
	}
	
	/**
	 * Stops the UDP server.
	 */
	public void stopServer() {
		broadcastTimer.cancel();
		serverThread.shutdown();
	}
	
	/**
	 * Changes the nick name.
	 * 
	 * @param name The new nick name.
	 */
	public void changeMyName(String name) {
		ArrayList<Participant> participants = participantList.getParticipants();
		Participant receiver;
		String msg;
		for(int k=0; k<participants.size(); ++k) {
			receiver = participants.get(k);
			if(receiver.isOnline()) {
				msg = JsonTools.createNewNameMessage(name);
				updtools.sendMsg(receiver.getInetAddress(), msg.getBytes());			
			}
		}		
	}
	
	/**
	 * Adds a received message to the chat history.
	 * 
	 * @param author The author of the received message.
	 * @param msg The text of the received message.
	 */
	private void addMessageToChatText(String author, String msg) {
		ChatMessage cmsg = new ChatMessage(author, msg);		
		chatHistory.addMessage(cmsg);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void onError(ModelNotification notification) {		
		setChanged();
		notifyObservers(notification);	
		errorsWhileStarting = true;
		stopServer();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void onSuccessfulStarted() {
		System.out.println("onSuccessfulStarted!");
		if(!errorsWhileStarting) {
			setChanged();
			notifyObservers(ModelNotification.CONNECTION_ESTABLISHED);	
		}
	}

	/**
	 * Informs all participants which are online about leaving the chat.
	 */
	public void leaveChat() {
		ArrayList<Participant> participants = participantList.getParticipants();
		Participant receiver;
		String msg;
		for(int k=0; k<participants.size(); ++k) {
			receiver = participants.get(k);
			if(receiver.isOnline()) {
				msg = JsonTools.createChatLeftMessage(settings.getProperty("name"));
				updtools.sendMsg(receiver.getInetAddress(), msg.getBytes());			
			}
		}		
	}

	/**
	 * Send a chat text message to all participants which are online.
	 * 
	 * @param msg The message text.
	 */
	public void sendMessage(String cmsg) {
		ArrayList<Participant> participants = participantList.getParticipants();
		Participant receiver;
		String msg;
		for(int k=0; k<participants.size(); ++k) {
			receiver = participants.get(k);
			if(receiver.isOnline()) {
				msg = JsonTools.createChatMessage(settings.getProperty("name"), cmsg);
				updtools.sendMsg(receiver.getInetAddress(), msg.getBytes());			
			}
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void processMessage(JSONObject msg_, InetAddress addr_, int port_) {
		int code = msg_.getInt("code");
		String msg;
		String name = "";
		Boolean newParticipant = false;
		
		switch(code) {
			
			case 1:
				name = msg_.getString("name");
				System.out.println("received WhoIsOnlineMessage from " + addr_ + " : " + port_);
				msg = JsonTools.createIAmOnlineMessage(settings.getProperty("name"));
				updtools.sendMsg(addr_, msg.getBytes());
				
				newParticipant = false;
				if(!participantList.containsParticipant(addr_.toString())) {
					participantList.addParticipant(participantList.createNewParticipant(addr_.toString(), name, addr_));
					newParticipant = true;
				} else {
					if(!participantList.isOnline(addr_.toString())) newParticipant = true;
					participantList.setParticipantOnline(addr_.toString());
				}
				break;
				
			case 2: 
				name = msg_.getString("name");
				System.out.println("received IAmOnlineMessage from " + name + ": " + addr_ + " : " + port_);
				
				newParticipant = false;
				if(!participantList.containsParticipant(addr_.toString())) {
					participantList.addParticipant(participantList.createNewParticipant(addr_.toString(), name, addr_));
					newParticipant = true;
				} else {
					if(!participantList.isOnline(addr_.toString())) newParticipant = true;
					participantList.setParticipantOnline(addr_.toString());
				}				
				break;
				
			case 3: 
				name = msg_.getString("name");
				msg = msg_.getString("text");
				addMessageToChatText(addr_.toString(), msg);
				break;
				
			case 4:
				name = msg_.getString("name");
				String oldName = participantList.getNameOfParticipant(addr_.toString());
				participantList.changeName(addr_.toString(), name);
				chatHistory.addMessage(new NameChangeMessage(oldName, name));
				break;
				
			case 5:
				name = msg_.getString("name");
				participantList.setParticipantOffline(addr_.toString());
				chatHistory.addMessage(new ChatLeftMessage(name));
		}	
		
		if(newParticipant) {
			System.out.println("newParticipant");
			chatHistory.addMessage(new NewParticipantMessage(name));
		}
	}
	
}
