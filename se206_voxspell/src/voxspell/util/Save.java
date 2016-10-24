package voxspell.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import voxspell.model.UserModel;

/**
 * Class involved in saving all the Serializable classes
 * for future use
 * @author terran
 * @author michael kemp (original version)
 *
 */
public class Save {
	public static final String DEFAULT_FILE_NAME = "voxsave";
	public static final String EXTENSION = ".vxs";
	public static final String DIRECTORY = "profile/";
	
	private String _filename;
	private UserModel _user;
	
	/**
	 * Constructor for a new save game
	 * @param user the user that the save file is for
	 */
	public Save(UserModel user) {
		ensureDirectory();
		_user = user;
		String username = user.toString();
		_filename = DIRECTORY + username + EXTENSION;

	}
	
	/**
	 * Constructor for a save game (but used for loading)
	 * @param filename filename for the user to load
	 */
	public Save(String filename) {
		//for loading
		ensureDirectory();
		_filename = filename;

		try {
			_user = load();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Makes sure the directory for profiles exist otherwise create a new directory
	 * for it
	 * @return whether the file exists (always true as a new directory is created)
	 */
	public boolean ensureDirectory() {
		File directory = new File(DIRECTORY);
		if (directory.exists()) {
			return true;
		} else {
			directory.mkdir();
			return true;
		}
	}
	
	/**
	 * This method saves the game to the file
	 */
	public void save() {
		try {
			deleteSaveFile();
			FileOutputStream file = new FileOutputStream(_filename);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(_user);
			out.close();
			file.close();
		} catch (FileNotFoundException e) {
			System.out.println("Save FNF");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Loads the game from the file and returns the reference, a
	 * FileNotFoundException is thrown if there's no file to read from (in the
	 * directory the game is in)
	 * 
	 * @return The game read from the file
	 * @throws FileNotFoundException
	 *             There's no save file or it's in the wrong place
	 */
	public UserModel load() throws FileNotFoundException {
		try {
			FileInputStream file = new FileInputStream(_filename);
			ObjectInputStream in = new ObjectInputStream(file);
			_user = (UserModel) in.readObject();
			in.close();
			file.close();
			return _user;
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; // TODO Come up with something better than returning null
	}
	
	/**
	 * Delets the save file to prevent it from being overridden.
	 * Implementation by Michael Kemp
	 */
	public void deleteSaveFile() {
		try {
			Files.deleteIfExists(Paths.get(_filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks whether the save file exists.
	 * Michael's implementation from the prototype (from SaveGame.class)
	 * @return whether the file exists
	 */
	public boolean ensureSaveExists() {
		return (new File(_filename).isFile());
	}
	
	/**
	 * Accessor for the filename
	 * @return Filename
	 */
	public String getFileName() {
		return _filename;
	}

}
