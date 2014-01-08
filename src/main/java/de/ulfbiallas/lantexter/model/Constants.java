package de.ulfbiallas.lantexter.model;

/**
 * This class holds a set of global constants.
 *
 * @author Ulf Biallas
 *
 */
public class Constants {

	/** Image file for the window and tray icon. */
	public static final String ICON_FILE = "icon.png";

	/** Name of the application. */
	public static final String PROGRAM_NAME = "LAN Texter";

	/** Current version of the application. */
	public static final String VERSION = "1.0";

	/** Path and file name of the properties file. */
	public static final String PROPERTIES_FILE = "settings.properties";

	/** Default port which is opened by the application. */
	public static final int DEFAULT_PORT = 12345;

	/** Default language */
	public static final String DEFAULT_LANGUAGE = "language_english";

	/**
	 * Time interval in [s] to broadcast an alive message and
	 * to ask for other participants.
	 */
	public static final int ALIVE_TIMER_INTERVAL = 20;

	/** Time interval in [s] after which a participant is set offline. */
	public static final int NOT_ALIVE_INTERVAL = 70;

	/** Text color of a notification in the chat history. */
	public static final String NOTIFICATION_COLOR = "#aaaaaa";

	/** Text for the about view */
	public static final String CREDITS = "by Ulf Biallas | 2013";

	/** List of used libraries. */
	public static final String USED_LIBRARIES =
		"Json-lib (Apache License Version 2.0)\n" +
		"JGoodies Common (BSD open source license)\n" +
		"JGoodies Forms (BSD open source license)";
}