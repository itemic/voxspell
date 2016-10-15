package se206_model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import se206_voxspell.MainApp;

public class UserModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7934526701176815705L;
	private String _name;
	private int _level;
	private GameModel _game;
	private boolean isCustomWordlist;
	private ArrayList<String> _music = new ArrayList<>();
	private ArrayList<Boolean> _activated = new ArrayList<>();
	private String _currentSoundtrack = "None";
	private String _displaySoundtrack = "None";
	private int _currency; //currency called betalpha
	private GameType _mode;

	//EXPERIENCE REQUIRED: base (100) * 1.2 ^ (level-1)
	
	public UserModel(String name, GameType mode) throws IOException {
		this(name, "resources/nzcer-wordlist.txt", mode);
	}
	
	
	public UserModel(String name, String filename, GameType mode) throws IOException {
		_name = name;
		_level = 1;
		_currency = 25;
		_game = new GameModel(filename);
		isCustomWordlist = true;
		_mode = mode;
		setUpMusic();
	}
	
	public GameType getGameType() {
		return _mode;
	}
	
	public String getCurrentSoundtrack() {
		return _currentSoundtrack;
	}
	
	public String getDisplaySoundtrack() {
		return _displaySoundtrack;
	}
	
	public void setCurrentSoundtrack(String s) {
		if (s.equals("None")) {
			_currentSoundtrack = s;	
		} else {
			_currentSoundtrack = "resources/soundtrack/" + s;
		}
		
		_displaySoundtrack = s;
	}
	
	public boolean getCanPlay(int pos) {
		return _activated.get(pos);
	}
	
	public void setCanPlay(int pos) {
		_activated.set(pos, true);
	}
	
	private void setUpMusic() {
		for (File f: new File("resources/soundtrack/").listFiles()) {
			_music.add(f.getName());
			_activated.add(false);
		}
//		System.out.println(_music);
	}
	
	public ArrayList<String> getMusicList() {
		return _music;
	}
	
	public ArrayList<Boolean> getAvailableMusic() {
		return _activated;
	}
	
	public boolean getIsCustomWordlist() {
		return isCustomWordlist;
	}
	
	public GameModel getGame() {
		return _game;
	}
	
	public int getCurrency() {
		return _currency;
	}
	
	public void gainCurrency(int gain) {
		_currency += gain;
		update();
	}
	
	
	
	public int getLevel(){ 
		return _level;
	}
	
	
	public void update() {
		MainApp.instance().getHUD().update();
	}
	
	public String toString() {
		return _name;
	}

	
}
