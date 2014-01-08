package de.ulfbiallas.lantexter.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Properties;

/**
 * Singleton class to manage all program properties.
 * 
 * @author Ulf Biallas
 *
 */
public class Settings extends Observable {

	/** The singleton object of the class. */
	private static final Settings settings = new Settings();

	private Properties properties;
	private Language language;
	
	
	/**
	 * Private constructor which initializes the settings with the default values.
	 * If available, it overwrites the values with those from a properties file.
	 */
	private Settings() {	
		properties = new Properties();
		language = Language.getInstance();
		
		try {
			properties.load(new FileInputStream(new File(Constants.PROPERTIES_FILE)));
		} catch (FileNotFoundException e) {
			setDefault();
		} catch (IOException e) {
			setDefault();
		}
		
		language.init(getProperty("language"));
	}
	
	/** 
	 * Returns the singleton object.
	 * 
	 * @return The singleton object.
	 */
	public static Settings getInstance() {
		return settings;
	}
	
	/** 
	 * Returns the current language object
	 * 
	 * @return The language object
	 */
	public Language getLanguage() {
		return language;
	}
	
	/**
	 * Returns the value of specific property.
	 * 
	 * @param property The name of the specific property.
	 * @return The value of the specific property.
	 */
	public String getProperty(String property) {
		return properties.getProperty(property);
	}
	
	/**
	 * Sets a new property-value pair.
	 * If a property with the same name exists,
	 * it will be overwritten.
	 * 
	 * @param property The name of the specific property to set.
	 * @param value The value of the specific property to set.
	 */
	public void setProperty(String property, String value) {
		properties.setProperty(property, value);
		if(property.equals("language")) {
			language.init(value);
			setChanged();
			notifyObservers(ModelNotification.LANGUAGE_CHANGED);	
		}
		setChanged();
		notifyObservers(ModelNotification.SETTINGS_CHANGED);
	}
	
	/**
	 * Resets all settings to the default values.
	 */
	public void setDefault() {
		properties = new Properties();
		
		String name;
		try {
			name = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			name = "User";
		}
		
		System.out.println("name: " + name);
		
		properties.put("name", name);		
		properties.put("port", new Integer(Constants.DEFAULT_PORT).toString());
		properties.put("minimizeToTray", new Boolean(true).toString());	
		properties.put("language", Constants.DEFAULT_LANGUAGE);
		
		setChanged();
		notifyObservers(ModelNotification.SETTINGS_CHANGED);
	}
	
	/**
	 * Saves the current settings in a properties file.
	 */
	public void saveSettings() {		
		try {
			properties.store(new FileOutputStream(new File(Constants.PROPERTIES_FILE)), "");
		} catch (FileNotFoundException e) {	
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
