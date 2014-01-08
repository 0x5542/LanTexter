package de.ulfbiallas.lantexter.view;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.ulfbiallas.lantexter.model.Constants;
import de.ulfbiallas.lantexter.model.ModelNotification;
import de.ulfbiallas.lantexter.model.Settings;
import de.ulfbiallas.lantexter.model.Language.LanguageId;


/**
 * View to change the settings of the application. 
 * Shows a form to change the program properties.
 * 
 * @author Ulf Biallas
 *
 */
public class SettingsGui extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;
		
	/** The GUI elements */
	private JTextField nameTextField;
	private JTextField portTextField;
	private JCheckBox minimizeToTrayCheckbox;
	private JComboBox<LanguageId> languageComboBox;
	private JLabel languageLabel;
	private JLabel nameLabel;
	private JLabel portLabel;
	private JLabel minimizeToTrayLabel;
	private JButton resetButton;
	private JButton saveButton;

	/** A List of controllers the listen which can react on actions. */
	private ArrayList<SettingsGuiListener> settingsGuiListener = new ArrayList<SettingsGuiListener>();	
	
	/** A list of languages to show in the ComboBox */
	private HashMap<String, Integer> languageComboBoxItems = new HashMap<String, Integer>();
	
	private Settings settings;
	
	
	/**
	 * Constructor. Creates the GUI.
	 */
	public SettingsGui() {
		super();
		
		settings = Settings.getInstance();
		
		URL iconUrl = ClassLoader.getSystemResource(Constants.ICON_FILE);
		Image icon = new ImageIcon( iconUrl ).getImage();
		setIconImage( icon );
		
		languageLabel = new JLabel(settings.getLanguage().getLabel("label_language"));
		nameLabel = new JLabel(settings.getLanguage().getLabel("label_name"));
		portLabel = new JLabel(settings.getLanguage().getLabel("label_port"));
		minimizeToTrayLabel = new JLabel(settings.getLanguage().getLabel("label_minimizetotray"));
		

		ArrayList<LanguageId> languageList = settings.getLanguage().getListOfLanguagesAsId();
		LanguageId[] languageListArray = languageList.toArray(new LanguageId[0]);
		
		String key;
		for(int k=0; k<languageList.size(); ++k) {
			key = languageList.get(k).getId();
			languageComboBoxItems.put(key, k);
		}
	
		languageComboBox = new JComboBox<LanguageId>(languageListArray);
		nameTextField = new JTextField(10);
		portTextField = new JTextField(10);
		minimizeToTrayCheckbox = new JCheckBox();
		
		resetButton = new JButton(settings.getLanguage().getLabel("button_reset"));
		saveButton = new JButton(settings.getLanguage().getLabel("button_save"));

		
		 FormLayout layout = new FormLayout(
			      "3dlu, pref, 3dlu, pref, 3dlu",
			      "3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu");
		
		 CellConstraints cc = new CellConstraints();
		 
		JPanel settingsPanel = new JPanel(layout);
		settingsPanel.add(languageLabel, cc.xy  (2, 2));
		settingsPanel.add(languageComboBox, cc.xy  (4, 2));
		settingsPanel.add(nameLabel, cc.xy  (2, 4));
		settingsPanel.add(nameTextField, cc.xy  (4, 4));
		settingsPanel.add(portLabel, cc.xy  (2, 6));
		settingsPanel.add(portTextField, cc.xy  (4, 6));
		settingsPanel.add(minimizeToTrayLabel, cc.xy  (2, 8));
		settingsPanel.add(minimizeToTrayCheckbox, cc.xy  (4, 8));
		settingsPanel.add(resetButton, cc.xy  (2, 10));
		settingsPanel.add(saveButton, cc.xy  (4, 10));
	
		add(settingsPanel);
		
		pack();
		setResizable(false);
		
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(int k=0; k<settingsGuiListener.size(); ++k) {
					settingsGuiListener.get(k).resetSettings();
				}
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(int k=0; k<settingsGuiListener.size(); ++k) {
					settingsGuiListener.get(k).saveSettings(createHashMapWithSettings());
				}
			}
		});		
		
		refreshLabels();
		refreshForms();
		
	}
	
	/**
	 * Creates a map which contains all settings.
	 * 
	 * @return The map with the settings.
	 */
	private HashMap<String, String> createHashMapWithSettings() {
		HashMap<String, String> settings = new HashMap<String, String>();
		settings.put("name", nameTextField.getText());
		settings.put("port", portTextField.getText());
		settings.put("minimizeToTray", new Boolean(minimizeToTrayCheckbox.isSelected()).toString());
		LanguageId selectedLanguage = (LanguageId) languageComboBox.getSelectedItem();
		settings.put("language", selectedLanguage.getId());
		return settings;
	}

	/**
	 * Refreshes the values of the properties in the forms.
	 */
	private void refreshForms() {
		String name = settings.getProperty("name");
		String port = settings.getProperty("port");
		Boolean minimizeToTray = Boolean.parseBoolean(settings.getProperty("minimizeToTray"));
		String language = settings.getProperty("language");
		System.out.println("language: " + language);
		
		languageComboBox.setSelectedIndex(languageComboBoxItems.get(language));
		nameTextField.setText(name);
		portTextField.setText(port);
		minimizeToTrayCheckbox.setSelected(minimizeToTray);
		pack();
	}
	
	/**
	 * Refreshes all labels in the view. (Called if language was changed)
	 */
	private void refreshLabels() {
		setTitle(settings.getLanguage().getLabel("title_settings"));
		languageLabel.setText(settings.getLanguage().getLabel("label_language"));
		nameLabel.setText(settings.getLanguage().getLabel("label_name"));
		portLabel.setText(settings.getLanguage().getLabel("label_port"));
		minimizeToTrayLabel.setText(settings.getLanguage().getLabel("label_minimizetotray"));		
		resetButton.setText(settings.getLanguage().getLabel("button_reset"));
		saveButton.setText(settings.getLanguage().getLabel("button_save"));		
		pack();
	}
	
	/**
	 * Adds an controller to the GUI.
	 * 
	 * @param listener A controller.
	 */
	public void addSettingsGuiListener(SettingsGuiListener listener_) {
		settingsGuiListener.add(listener_);
	}
	
	/**
	 * Removes a controller from the GUI.
	 * 
	 * @param listener A controller.
	 */
	public void removeSettingsGuiListener(SettingsGuiListener listener_) {
		settingsGuiListener.remove(listener_);
	}


	/**
	 * @inheritDoc
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		ModelNotification notificationType = (ModelNotification) arg1;
		switch(notificationType) {
			case SETTINGS_CHANGED: 
				refreshForms();		
				break;
				
			case LANGUAGE_CHANGED:
				refreshLabels();
				break;
		}
		
		
	}
	
}
