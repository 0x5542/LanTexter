package de.ulfbiallas.lantexter.view;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;

import de.ulfbiallas.lantexter.model.ChatHistory;
import de.ulfbiallas.lantexter.model.Constants;
import de.ulfbiallas.lantexter.model.ModelNotification;
import de.ulfbiallas.lantexter.model.Settings;

/**
 * Tray view of the application.
 * 
 * @author Ulf Biallas
 *
 */
public class TrayView implements Observer {

	/** Menu elements. */
	private TrayIcon trayIcon;
	private MenuItem openGuiMenuItem;
	private MenuItem quitMenuItem;
	private MenuItem settingsMenuItem;
	
	/** Flag which is true if the tray bar icon is supported by the system. */
	private Boolean supported = false;
	
	/** Flag which is set to true if the application is minimized to tray */
	private Boolean chatGuiMinimizedToTray = false;
	
	/** A List of controllers the listen which can react on actions. */
	private ArrayList<ChatGuiListener> chatGuiListener = new ArrayList<ChatGuiListener>();
	
	private ChatHistory chatHistory;
	private Settings constsAndProps;
	
	
	/**
	 * Constructor. Checks whether the tray bar is supported.
	 */
	public TrayView() {
		supported = SystemTray.isSupported();
		
		chatHistory = ChatHistory.getInstance();
		constsAndProps = Settings.getInstance();
		
		if(supported) {
			createTrayIcon();		
		}
	}
	
	/**
	 * Creates tray icon and menu.
	 */
	private void createTrayIcon() {
		if(supported) {	

			URL iconUrl = ClassLoader.getSystemResource(Constants.ICON_FILE);
			Image icon = new ImageIcon( iconUrl ).getImage();
		    
		    PopupMenu menu = new PopupMenu();
		    openGuiMenuItem = new MenuItem();
		    settingsMenuItem = new MenuItem();
		    quitMenuItem = new MenuItem();		 
		    menu.add(openGuiMenuItem);
		    menu.add(settingsMenuItem);
		    menu.add(quitMenuItem);
		    
		    trayIcon = new TrayIcon(icon, Constants.PROGRAM_NAME, menu);
		    trayIcon.setImageAutoSize(true);

		    refreshLabels();
		    
		    openGuiMenuItem.addActionListener(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int k=0; k<chatGuiListener.size(); ++k) {
						chatGuiListener.get(k).openChatView();
					}					
				}
			});
		    
		    settingsMenuItem.addActionListener(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int k=0; k<chatGuiListener.size(); ++k) {
						chatGuiListener.get(k).openSettingsView();
					}
				}
			});
		    
		    quitMenuItem.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					for(int k=0; k<chatGuiListener.size(); ++k) {
						chatGuiListener.get(k).quit();
					}
				}
			}); 
		}	
	}	
	
	/** 
	 * Returns the tray icon.
	 * @return The tray icon.
	 */
	public TrayIcon getTrayIcon() {
		return trayIcon;
	}
	
	/**
	 * Returns true if the tray bar icon is supported by the system.
	 * 
	 * @return Boolean which is true if the tray icon is supported.
	 */
	public Boolean isSupported() {
		return supported;
	}
	
	/**
	 * Sets the minimized to tray flag.
	 * 
	 * @param chatGuiMinimizedToTray Boolean which is true if
	 * the application has been minimized to the tray bar.
	 */
	public void setChatGuiMinimizedToTray(Boolean chatGuiMinimizedToTray) {
		this.chatGuiMinimizedToTray = chatGuiMinimizedToTray;
	}
	
	/**
	 * Adds an controller to the GUI.
	 * 
	 * @param listener A controller.
	 */
	public void addChatGuiListener(ChatGuiListener listener) {
		chatGuiListener.add(listener);
	}
	
	/**
	 * Removes a controller from the GUI.
	 * 
	 * @param listener A controller.
	 */
	public void removeChatGuiListener(ChatGuiListener listener) {
		chatGuiListener.remove(listener);
	}

	/**
	 * Refreshes all labels in the view. (Called if language was changed)
	 */
	private void refreshLabels() {
		openGuiMenuItem.setLabel(constsAndProps.getLanguage().getLabel("menu_openchatview"));
	    settingsMenuItem.setLabel(constsAndProps.getLanguage().getLabel("menu_file_settings"));
	    quitMenuItem.setLabel(constsAndProps.getLanguage().getLabel("menu_file_quit"));			
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		ModelNotification notificationType = (ModelNotification) arg1;
		switch(notificationType) {
			case LIST_OF_PARTICIPANTS_CHANGED: 
				
				break;
			case LANGUAGE_CHANGED:
				refreshLabels();
				break;
			case MESSAGE_RECEIVED: 
				if(chatGuiMinimizedToTray) {
					trayIcon.displayMessage(constsAndProps.getLanguage().getLabel("notification_newmessage"), chatHistory.getLatestMessage().toString(), MessageType.INFO);
				}
				break;						
		}
	}	




}
