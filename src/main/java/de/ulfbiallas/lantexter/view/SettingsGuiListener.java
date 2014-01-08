package de.ulfbiallas.lantexter.view;

import java.util.HashMap;

/**
 * Interface for a SettingsGUI controller.
 * 
 * @author Ulf Biallas
 *
 */
public interface SettingsGuiListener {

	/**
	 * Resets all settings to the default values.
	 */
	public void resetSettings();
	
	/**
	 * Save a set of settings.
	 * 
	 * @param settings The settings to save.
	 */
	public void saveSettings(HashMap<String, String> settings);
	
}
