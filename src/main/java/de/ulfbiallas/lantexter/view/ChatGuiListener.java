package de.ulfbiallas.lantexter.view;

/**
 * Interface for a ChatGUI controller.
 * 
 * @author Ulf Biallas
 *
 */
public interface ChatGuiListener {
	
	/**
	 * Start the chat. Allows the user to send and receive messages.
	 */
	public void goOnline();
	
	/**
	 * Leave the chat.
	 */
	public void goOffline();
	
	/**
	 * Send a message to all other chat users.
	 * 
	 * @param msg The message to send.
	 */
	public void sendMessage(String msg);
	
	/**
	 * Quits the program.
	 */
	public void quit();
	
	/**
	 * Opens a view where the user can change the settings.
	 */
	public void openSettingsView();
	
	/**
	 * Opens a view with information about the program.
	 */
	public void openAboutView();
	
	/**
	 * Opens a view where the user can participate in the chat.
	 */
	public void openChatView();
	
}
