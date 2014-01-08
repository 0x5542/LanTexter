package de.ulfbiallas.lantexter.control;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JOptionPane;

import de.ulfbiallas.lantexter.model.ChatCore;
import de.ulfbiallas.lantexter.model.ChatHistory;
import de.ulfbiallas.lantexter.model.Constants;
import de.ulfbiallas.lantexter.model.ParticipantList;
import de.ulfbiallas.lantexter.model.Settings;
import de.ulfbiallas.lantexter.view.ChatGui;
import de.ulfbiallas.lantexter.view.ChatGuiListener;
import de.ulfbiallas.lantexter.view.SettingsGui;
import de.ulfbiallas.lantexter.view.SettingsGuiListener;
import de.ulfbiallas.lantexter.view.TrayView;



/**
 * Main controller class.
 * Handles the instances of all view and model classes.
 * 
 * @author Ulf Biallas
 *
 */
public class ChatController implements ChatGuiListener, SettingsGuiListener {

	// model
	private Settings settings;
	private ParticipantList participantList;
	private ChatHistory chatHistory;
	private ChatCore chatCore;
	
	// view
	private ChatGui chatGui;
	private TrayView trayView;
	private SettingsGui settingsGui;
	
	
	/**
	 * Constructor. Creates instances of all view and model classes.
	 * Sets up the dependencies between the objects by adding listeners.
	 */
	public ChatController() {
		
		settings = Settings.getInstance();
		participantList = ParticipantList.getInstance();
		chatHistory = ChatHistory.getInstance();
		chatCore = new ChatCore();
		
		chatGui = new ChatGui();
		trayView = new TrayView();
		settingsGui = new SettingsGui();
		
		chatGui.addChatGuiListener(this);
		trayView.addChatGuiListener(this);
		settingsGui.addSettingsGuiListener(this);
		
		chatCore.addObserver(chatGui);
		chatCore.addObserver(trayView);
		participantList.addObserver(chatGui);
		participantList.addObserver(trayView);
		chatHistory.addObserver(chatGui);
		chatHistory.addObserver(trayView);
		settings.addObserver(settingsGui);
		settings.addObserver(chatGui);
		settings.addObserver(trayView);
		
		Boolean minimizeToTray = Boolean.parseBoolean(settings.getProperty("minimizeToTray"));
		if(minimizeToTray && trayView.isSupported()) {
			addIconToTrayBar();
		}
		
		
		chatGui.addWindowListener( new WindowAdapter () { 
			public void windowClosing(WindowEvent event) {
				Boolean minimizeToTray = Boolean.parseBoolean(settings.getProperty("minimizeToTray"));
				if(minimizeToTray) {
					if (trayView.isSupported()) {
						chatGui.setVisible(false);	
						trayView.setChatGuiMinimizedToTray(true);
					} else {
						quitProgram();
					}
				} else {
					quitProgram();
				}
			}
			
		});
	}
	
	/**
	 * Adds the tray icon to the tray bar. Shows an error message if it is not possible to add it.
	 */
	private void addIconToTrayBar() {
		try {   
			SystemTray tray = SystemTray.getSystemTray();
			tray.add(trayView.getTrayIcon());	
		} catch (AWTException e1) {
			JOptionPane.showMessageDialog(chatGui,
					settings.getLanguage().getLabel("tray_error_description"),
					settings.getLanguage().getLabel("error_title"),
				    JOptionPane.ERROR_MESSAGE);
		}			
	}
	
	/**
	 * Removes the tray icon from the tray bar.
	 */
	private void removeIconFromTrayBar() {
		SystemTray tray = SystemTray.getSystemTray();
		tray.remove(trayView.getTrayIcon());
	}
	
	/**
	 * Shows an dialog which asks for a quit confirmation. 
	 * If the user confirms the program will be quit.
	 */
	private void quitProgram() {
		int dialogResult = JOptionPane.showConfirmDialog(chatGui, 
				settings.getLanguage().getLabel("quit_msg_description"), 
				settings.getLanguage().getLabel("quit_msg_title"),
                JOptionPane.YES_NO_OPTION);
		if(dialogResult == JOptionPane.YES_OPTION){
			System.exit(0);
		}			
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void goOnline() {
		chatCore.startServer();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void goOffline() {
		chatCore.leaveChat();
		chatCore.stopServer();
		participantList.setAllParticipantsOffline();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void sendMessage(String msg) {
		chatCore.sendMessage(msg);
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public void quit() {
		quitProgram();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void openSettingsView() {
		settingsGui.setVisible(true);
		settingsGui.setLocationRelativeTo(chatGui);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void resetSettings() {
		settings.setDefault();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void openAboutView() {
		
		String aboutTxt = 
			Constants.PROGRAM_NAME + " " + Constants.VERSION +"\n" +
			Constants.CREDITS + "\n\n" + 
			settings.getLanguage().getLabel("text_used_libraries") + "\n" +
			Constants.USED_LIBRARIES;
			
		JOptionPane.showMessageDialog(chatGui,
				aboutTxt,
			    settings.getLanguage().getLabel("title_about"),
			    JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void saveSettings(HashMap<String, String> settingsToSave) {
		Set<String> keys = settingsToSave.keySet();
		Iterator<String> keysIterator = keys.iterator();
		String key;
		while(keysIterator.hasNext()) {
			key = keysIterator.next();

			if(key.equals("minimizeToTray")) {
				System.out.println("minimizeToTray");
				Boolean minimizeToTray = Boolean.parseBoolean(settings.getProperty("minimizeToTray")); 
				Boolean minimizeToTrayNew = Boolean.parseBoolean(settingsToSave.get(key));
				System.out.println("minimizeToTray: " + minimizeToTray);
				System.out.println("minimizeToTrayNew: " + minimizeToTrayNew);
				if(minimizeToTray != minimizeToTrayNew) {
					if(minimizeToTrayNew) {
						addIconToTrayBar();
					} else {
						removeIconFromTrayBar();
					}				
				}
			}
			
			if(key.equals("name")) {
				String name = settings.getProperty("name");
				String nameNew = settingsToSave.get(key);
				if(!name.equals(nameNew)) {
					chatCore.changeMyName(nameNew);
				}
			}
			
			settings.setProperty(key, settingsToSave.get(key));
		}
		settings.saveSettings();	
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void openChatView() {
		chatGui.setVisible(true);	
		trayView.setChatGuiMinimizedToTray(false);
	}
	





	
}
