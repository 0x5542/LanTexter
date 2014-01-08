package de.ulfbiallas.lantexter.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import de.ulfbiallas.lantexter.model.ChatHistory;
import de.ulfbiallas.lantexter.model.Constants;
import de.ulfbiallas.lantexter.model.ModelNotification;
import de.ulfbiallas.lantexter.model.ParticipantList;
import de.ulfbiallas.lantexter.model.Settings;


/**
 * Main view class of the application. Shows the chat history, 
 * a list of participants and allows the user to write chat messages.
 * 
 * @author Ulf Biallas
 *
 */
public class ChatGui extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
	
	/** The GUI elements */
	private JTextArea inputTextArea = new JTextArea();
	private JEditorPane outputTextArea = new JEditorPane();
	private DefaultListModel<String> participantsJListModel = new DefaultListModel<String>();
	private JList<String> participantsJList = new JList<String>(participantsJListModel);
	private JButton sendButton = new JButton();
	private JSplitPane textAreSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	
	/** Menu bar item */
	private JMenu fileMenu;
	private JMenuItem fileConnectMenuItem;
	private JMenuItem fileDisconnectMenuItem;
	private JMenuItem fileSettingsMenuItem;
	private JMenuItem fileQuitMenuItem;
	private JMenu helpMenu;
	private JMenuItem helpAbout;
	private JLabel participantsLabel;
	
	/** A List of controllers the listen which can react on actions. */
	private ArrayList<ChatGuiListener> chatGuiListener = new ArrayList<ChatGuiListener>();
	
	/** Flag which is set if the user is online. */
	private Boolean statusOnline = false;
	
	private Settings settings;
	private ParticipantList participantList;
	private ChatHistory chatHistory;
	
	
	/**
	 * Constructor. Creates the GUI.
	 */
	public ChatGui() {
		super();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		settings = Settings.getInstance();
		participantList = ParticipantList.getInstance();
		chatHistory = ChatHistory.getInstance();			
		
		URL iconUrl = ClassLoader.getSystemResource(Constants.ICON_FILE);
		Image icon = new ImageIcon( iconUrl ).getImage();
		setIconImage( icon );
		
		createMenuBar();
		createGui();
		refreshTitle();
		
		outputTextArea.setContentType("text/html");
		
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int k=0; k<chatGuiListener.size(); ++k) {
					chatGuiListener.get(k).sendMessage(inputTextArea.getText());
				}
				inputTextArea.setText("");
			}
		});
		
		
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	Dimension windowSize = getSize();
            	textAreSplitPane.setDividerLocation((int) (0.5 * windowSize.height));
            	mainSplitPane.setDividerLocation((int) (0.7 * windowSize.width));
            }
        });
		
		setSize(600, 400);
		setVisible(true);		
		
	}
	
	/**
	 * Refreshes the online status in the title.
	 */
	private void refreshTitle() {
		if(statusOnline) {
			setTitle(Constants.PROGRAM_NAME +" ("+settings.getLanguage().getLabel("status_online")+")");
		} else {
			setTitle(Constants.PROGRAM_NAME +" ("+settings.getLanguage().getLabel("status_offline")+")");
		}		
	}

	/**
	 * Enables or disables the input elements.
	 * 
	 * @param enabled If true the inputs will be enabled.
	 */
	private void setInputsEnabled(Boolean enabled) {
		inputTextArea.setEnabled(enabled);
		sendButton.setEnabled(enabled);
		statusOnline = enabled;
		refreshTitle();
	}

	/**
	 * Creates the GUI elements.
	 */
	private void createGui() {
		
		sendButton.setText(settings.getLanguage().getLabel("button_send"));
		outputTextArea.setEditable(false);
	
		setInputsEnabled(false);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
		JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
		inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputPanel.add(inputScrollPane);
		inputPanel.add(sendButton);
		
		JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
		outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		textAreSplitPane.setLeftComponent(outputScrollPane);
		textAreSplitPane.setRightComponent(inputPanel);
	
		JPanel participantsPanel = new JPanel(new BorderLayout());
		JScrollPane participantsScrollPane = new JScrollPane(participantsJList);
		participantsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		participantsLabel = new JLabel(settings.getLanguage().getLabel("label_participants"));
		participantsPanel.add(participantsLabel, BorderLayout.NORTH);
		participantsPanel.add(participantsScrollPane, BorderLayout.CENTER);
		
		mainSplitPane.setLeftComponent(textAreSplitPane);
		mainSplitPane.setRightComponent(participantsPanel);		
		
		add(mainSplitPane);

	}
	
	/**
	 * Creates the menu bar.
	 */
	private void createMenuBar() {
		
		fileMenu = new JMenu(settings.getLanguage().getLabel("menu_file")); 
		fileConnectMenuItem = new JMenuItem(settings.getLanguage().getLabel("menu_file_connect"));
		fileDisconnectMenuItem = new JMenuItem(settings.getLanguage().getLabel("menu_file_disconnect"));
		fileSettingsMenuItem = new JMenuItem(settings.getLanguage().getLabel("menu_file_settings"));
		fileQuitMenuItem = new JMenuItem(settings.getLanguage().getLabel("menu_file_quit"));
		fileMenu.add(fileConnectMenuItem);
		fileMenu.add(fileDisconnectMenuItem);
		fileMenu.add(fileSettingsMenuItem);
		fileMenu.add(fileQuitMenuItem);
		
		fileDisconnectMenuItem.setVisible(false);
		
		helpMenu = new JMenu(settings.getLanguage().getLabel("menu_help"));
		helpAbout = new JMenuItem(settings.getLanguage().getLabel("menu_help_about"));
		helpMenu.add(helpAbout);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		setJMenuBar(menuBar);
	
		fileConnectMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int k=0; k<chatGuiListener.size(); ++k) {
					chatGuiListener.get(k).goOnline();
				}
			}
		});		

		fileDisconnectMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setInputsEnabled(false);
				fileConnectMenuItem.setVisible(true);
				fileDisconnectMenuItem.setVisible(false);
				for(int k=0; k<chatGuiListener.size(); ++k) {
					chatGuiListener.get(k).goOffline();
				}				
			}
		});
	
		fileSettingsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int k=0; k<chatGuiListener.size(); ++k) {
					chatGuiListener.get(k).openSettingsView();
				}
			}
		});
		
		fileQuitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int k=0; k<chatGuiListener.size(); ++k) {
					chatGuiListener.get(k).quit();
				}
			}
		});
		
		helpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for(int k=0; k<chatGuiListener.size(); ++k) {
					chatGuiListener.get(k).openAboutView();
				}				
			}
		});			
	}
	
	/**
	 * Refreshes all labels in the view. (Called if language was changed)
	 */
	private void refreshLabels() {
		fileMenu.setText(settings.getLanguage().getLabel("menu_file")); 
		fileConnectMenuItem.setText(settings.getLanguage().getLabel("menu_file_connect"));
		fileDisconnectMenuItem.setText(settings.getLanguage().getLabel("menu_file_disconnect"));
		fileSettingsMenuItem.setText(settings.getLanguage().getLabel("menu_file_settings"));
		fileQuitMenuItem.setText(settings.getLanguage().getLabel("menu_file_quit"));
		helpMenu.setText(settings.getLanguage().getLabel("menu_help"));
		helpAbout.setText(settings.getLanguage().getLabel("menu_help_about"));	
		
		sendButton.setText(settings.getLanguage().getLabel("button_send"));
		participantsLabel.setText(settings.getLanguage().getLabel("label_participants"));
		
		refreshTitle();
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
	 * @inheritDoc
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		ModelNotification notificationType = (ModelNotification) arg1;
		
		switch(notificationType) {
			case LIST_OF_PARTICIPANTS_CHANGED: 
				participantsJListModel.clear();
				ArrayList<String> participants = participantList.getParticipantNames();
				for(int k=0; k<participants.size(); ++k) {
					participantsJListModel.addElement(participants.get(k));
				}			
				break;
				
			case MESSAGE_RECEIVED: 
				outputTextArea.setText(chatHistory.getChatText());	
				outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());			
				break;			
				
			case LANGUAGE_CHANGED:
				refreshLabels();
				outputTextArea.setText(chatHistory.getChatText());	
				outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());					
				break;
				
			case NAME_CHANGED:
				outputTextArea.setText(chatHistory.getChatText());	
				outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());	
				break;
				
			case CONNECTION_ESTABLISHED: 
				setInputsEnabled(true);
				fileConnectMenuItem.setVisible(false);
				fileDisconnectMenuItem.setVisible(true);	
				break;
				
			case CONNECTION_ERROR: 
				setInputsEnabled(false);
				JOptionPane.showMessageDialog(this,
						settings.getLanguage().getLabel("connection_error_description"),
					    settings.getLanguage().getLabel("error_title"),
					    JOptionPane.ERROR_MESSAGE);
				
				break;
				
			case ERROR: 
				
				break;							
		}
	}	
		
	
}
