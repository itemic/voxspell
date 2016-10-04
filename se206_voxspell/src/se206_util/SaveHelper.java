package se206_util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SaveHelper {
	private static SaveHelper _instance = null;
	private String directory = SaveGame.DIR;
	private SaveHelper() {
		
	}
	
	public static SaveHelper getInstance() {
		if (_instance == null) {
			_instance = new SaveHelper();
		}
		return _instance;
	}
	
	public ArrayList<File> findFiles() {
		ArrayList<File> profiles = new ArrayList<>();
		File[] dir = new File(directory).listFiles();
		for (File f : dir) {
			if (f.getName().endsWith(SaveGame.EXTENSION)) {
				profiles.add(f);
			}
		}
		return profiles;
	}
}
