package voxspell.util;

import java.io.File;
import java.util.ArrayList;

/**
 * Singleton class to find the save files and create them.
 * @author terran
 *
 */
public class FileHandler {
	private static FileHandler _instance = null;
	private static String directory = Save.DIRECTORY;
	private FileHandler() {
		
	}
	
	/**
	 * Singleton instance method
	 * @return the instance of the FileHandler
	 */
	public static FileHandler getInstance() {
		if (_instance == null) {
			_instance = new FileHandler();
		}
		return _instance;
	}
	
	/**
	 * Gets the list of all the profile savegames
	 * @return List of all profiles
	 */
	public static ArrayList<File> findProfiles() {
		ensureDirectory();
		ArrayList<File> profiles = new ArrayList<>();
		File[] dir = new File(directory).listFiles();
		for (File f : dir) {
			if (f.getName().endsWith(Save.EXTENSION)) {
				profiles.add(f);
			}
		}
		return profiles;
	}
	
	/**
	 * Makes sure directory for profiles exists
	 * @return whether the directory exists
	 */
	public static boolean ensureDirectory() {
		File directory = new File(Save.DIRECTORY);
		if (directory.exists()) {
			return true;
		} else {
			directory.mkdir();
			return true;
		}
	}
	

}
