package voxspell.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a model that represents a user and stores some important
 * statistics associated with them, such as the Game Model, # credits,
 * and the available soundtracks.
 * @author terran
 *
 */
public class UserModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7934526701176815705L;
	private String _name;
	private GameModel _game;
	private ArrayList<String> _music = new ArrayList<>();
	private ArrayList<Boolean> _activated = new ArrayList<>();
	private String _currentSoundtrack = "None";
	private String _displaySoundtrack = "None";
	private int _currency; //currency called betalpha
	private GameType _mode;

	//Default user uses the built-in word-list
	public UserModel(String name, GameType mode) throws IOException {
		this(name, "resources/voxwords.txt", mode);
	}
	
	//Initializes the users
	public UserModel(String name, String filename, GameType mode) throws IOException {
		_name = name;
		_currency = 25;
		_game = new GameModel(filename);
		_mode = mode;
		setUpMusic();
	}
	
	//Get the game mode that the User is in.
	public GameType getGameType() {
		return _mode;
	}
	
	//Get the full name of the current-playing soundtrack
	public String getCurrentSoundtrack() {
		return _currentSoundtrack;
	}
	
	//Get the short name of the current-playing soundtrack
	// (This is the one without file extension)
	public String getDisplaySoundtrack() {
		return _displaySoundtrack;
	}
	
	/**
	 * Updates the current playing soundtrack
	 * @param s short name of the soundtrack
	 */
	public void setCurrentSoundtrack(String s) {
		if (s.equals("None")) {
			_currentSoundtrack = s;	
		} else {
			_currentSoundtrack = "resources/soundtrack/" + s;
		}
		
		_displaySoundtrack = s;
	}
	
	/**
	 * Check if a level is available for playing
	 * @param pos The position the level is in the ArrayList of levels
	 * @return whether the user can play
	 */
	public boolean getCanPlay(int pos) {
		return _activated.get(pos);
	}
	
	/**
	 * Activates ability to play
	 * @param pos The position the level is in the ArrayList
	 */
	public void setCanPlay(int pos) {
		_activated.set(pos, true);
	}
	
	/**
	 * Grabs all the music from the resource folder and loads it into
	 * the user (and disables them
	 */
	private void setUpMusic() {
		for (File f: new File("resources/soundtrack/").listFiles()) {
			_music.add(f.getName());
			_activated.add(false);
		}
	}
	
	/**
	 * Getter method for all the music available for the user
	 * @return ArrayList of music names
	 */
	public ArrayList<String> getMusicList() {
		return _music;
	}
	
	/**
	 * Returns the game object associated with the user
	 * @return a GameModel
	 */
	public GameModel getGame() {
		return _game;
	}
	
	/**
	 * Returns the amount of credits the user has
	 * @return Credit amount
	 */
	public int getCurrency() {
		return _currency;
	}
	
	/**
	 * Updates the user's currency amount
	 * @param gain The amount to be added (negative to subtract)
	 */
	public void gainCurrency(int gain) {
		_currency += gain;
	}
	
	/**
	 * Returns name of the User
	 */
	@Override
	public String toString() {
		return _name;
	}

	
}
