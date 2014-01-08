package de.ulfbiallas.lantexter.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.UIManager;


/**
 * Singleton class which manages provides all labels, texts and descriptions
 * in the supported languages.
 * 
 * @author Ulf Biallas
 *
 */
public class Language {
	
	/** The singleton object of the class. */
	private static final Language language = new Language();
	
	/** Map of all supported languages. */
	private HashMap<String, Locale> supportedLanguages;
	
	/** Current localization. */
	private Locale locale;
	
	/** Resource Bundle with the language data. */
	private ResourceBundle languageResourceBundle;
	
	
	/**
	 * Inner class which represents a language identification.
	 * 
	 * @author Ulf Biallas
	 *
	 */
	public class LanguageId {
		
		/** The id of the language. */
		private String id;

		/** The name of the language. */
		private String name;
		
		
		/**
		 * Constructor. Creates a new language id.
		 * 
		 * @param id The id of the language.
		 * @param name The name of the language.
		 */
		public LanguageId(String id, String name) {
			this.id = id;
			this.name = name;
		}
		
		/**
		 * Returns the id of the language.
		 * 
		 * @return The id of the language.
		 */
		public String getId() {
			return id;
		}
		
		/**
		 * Returns the name of the language.
		 * 
		 * @return The name of the language.
		 */
		public String toString() {
			return name;
		}
	}
	
	
	/**
	 * Constructor. Creates the map with the supported languages.
	 */
	private Language() {	
		supportedLanguages = new HashMap<String, Locale>();
		supportedLanguages.put("language_english", Locale.ENGLISH);
		supportedLanguages.put("language_german", Locale.GERMAN);	
	}
	
	/**
	 * Initializes the class with one of the supported languages.
	 * 
	 * @param language The chosen language.
	 */
	public void init(String language) {
	
		locale = supportedLanguages.get(language);

		try {

			InputStream is = ClassLoader.getSystemResourceAsStream("language_"+locale.toString()+".properties");
		//	System.out.println("url.getPath(): " + url.getPath());
			
			//File f = new File("language_"+locale.toString()+".properties");
			//System.out.println("f.getAbsolutePath(): " + f.getAbsolutePath());
			
			//languageResourceBundle = PropertyResourceBundle.getBundle("language", locale, ClassLoader);
			languageResourceBundle = new PropertyResourceBundle(is);
			UIManager.put("OptionPane.yesButtonText", getLabel("button_yes"));
			UIManager.put("OptionPane.noButtonText", getLabel("button_no"));
			UIManager.put("OptionPane.cancelButtonText", getLabel("button_cancel"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Returns the singleton object.
	 * 
	 * @return The singleton object.
	 */
	public static Language getInstance() {
		return language;
	}
	
	/**
	 * Returns a specific label, description or text in the current language.
	 * 
	 * @param label The id of the specific label.
	 * @return The label text in the current language.
	 */
	public String getLabel(String label) {
		return languageResourceBundle.getString(label);
	}
	
	/**
	 * Returns a map with the supported languages.
	 * 
	 * @return The map with the supported languages.
	 */
	public HashMap<String, String> getListOfLanguages() {
		HashMap<String, String> languagesList = new HashMap<String, String>();
		
		Set<String> supportedLanguagesKeys = supportedLanguages.keySet();
		Iterator<String> iter = supportedLanguagesKeys.iterator();
		String key;
		while(iter.hasNext()) {
			key = iter.next();
			languagesList.put(key, languageResourceBundle.getString(key));
		}
		
		return languagesList;
	}
	
	/**
	 * Returns a list with the id's of the supported languages.
	 * 
	 * @return The list with the id's of the supported languages.
	 */
	public ArrayList<LanguageId> getListOfLanguagesAsId() {
		ArrayList<LanguageId> languagesList = new ArrayList<LanguageId>();
		Set<String> supportedLanguagesKeys = supportedLanguages.keySet();
		Iterator<String> iter = supportedLanguagesKeys.iterator();
		String key;
		while(iter.hasNext()) {
			key = iter.next();
			languagesList.add(new LanguageId(key, languageResourceBundle.getString(key)));
		}	
		return languagesList;
	}
	
	/**
	 * Returns the ResourceBundle;
	 * 
	 * @return The ResourceBundle;
	 */
	public ResourceBundle getLanguageResourceBundle() {
		return languageResourceBundle;
	}

	
	
}
