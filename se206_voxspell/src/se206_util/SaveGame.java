package se206_util;

//Taken from Michael's InputOutput class in A2 and heavily modified
//VOXSpell Final: Taken from Michael's code in the prototype

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import se206_model.UserModel;

/**
* <h1>SaveGame</h1> This class is responsible for saving a SpellingGame to a
* file and reading it again
* <p>
* It also can be used to see if a save file exists and it can be saved to a
* differently named file
* 
* @version 0.2.7
* @author mkem114(primary)
* @author tkro003(secondary)
* @since 2016-09-16
*/
public class SaveGame {
	/**
	 * Default save file name
	 */
	public static final String DEFAULTFILENAME = "save";
	/**
	 * Default file extension
	 */
	public static final String EXTENSION = ".vxs";
	
	public static final String DIR = "profile/";
	private String _filename;
	private UserModel _user;

	/**
	 * This constructor assumes the default filename and just assumes null for
	 * the game, this is for easy reading of save files
	 */
	public SaveGame() {
		this(null);
	}

	/**
	 * This constructor takes a game it's responsible for saving assumes the
	 * default file name
	 * 
	 * @param game
	 *            The game to be saved
	 */
	public SaveGame(UserModel user) {
		this("save", user);
	}
	

	/**
	 * This constructor allows definition of the save file name and the game to
	 * be saved
	 * 
	 * @param filename
	 *            The name of the save file
	 * @param game
	 *            The game to be saved
	 */
	public SaveGame(String filename, UserModel user) {
		makeDirectoryIfNotExist();
		_filename = DIR + filename + EXTENSION;
		_user = user;
	}

	/**
	 * This method saves the game to the file
	 */
	public void save() {
		try {
			delete();
			FileOutputStream file = new FileOutputStream(_filename);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(_user);
			out.close();
			file.close();
			System.out.println("save: "+ _user.toString());
		} catch (FileNotFoundException e) {
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
			System.out.println("SUCCESSFUL LOAD + " + _user);
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
	 * Deletes the savefile if it's in the right place
	 */
	public void delete() {
		try {
			Files.deleteIfExists(Paths.get(_filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Checks to see if the save file is there
	 * 
	 * @return Whether or not the the save file
	 */
	public boolean saveExists() {
		return (new File(_filename).isFile());
	}
	
	public boolean makeDirectoryIfNotExist() {
		File dir = new File(DIR);
		boolean doesExist = dir.exists();
		if (doesExist) {
			return true;
		} else {
			dir.mkdir();
			return true;
			
		}
	}
	public UserModel getUser() {
		return _user;
	}
}
