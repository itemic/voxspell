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
	private int _experience;
	private int _xpToNextLevel;
	private final int base = 20;
	private final double chanceOfReward = 0.7;
	private final double modifier = 1.18; // how much to next level
	private GameModel _game;
	private boolean isCustomWordlist;
	private ArrayList<String> _music = new ArrayList<>();
	private ArrayList<Boolean> _activated = new ArrayList<>();
	private String _currentSoundtrack = "None";
	private String _displaySoundtrack = "None";
	
	//EXPERIENCE REQUIRED: base (100) * 1.2 ^ (level-1)
	
	public UserModel(String name) throws IOException {
		_name = name;
		_level = 1;
		_experience = 0;
		_xpToNextLevel = base;
		_game = new GameModel("resources/nzcer-wordlist.txt");
		isCustomWordlist = false;
		setUpMusic();
	}
	
	public UserModel(String name, String filename) throws IOException {
		_name = name;
		_level = 1;
		_experience = 0;
		_xpToNextLevel = base;
		_game = new GameModel(filename);
		isCustomWordlist = true;
		setUpMusic();
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
		System.out.println(_music);
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
	
	
	public boolean checkIfLevelUp() {
		return _experience >= _xpToNextLevel;
	}
	
	public void levelUp() {
		_experience = _experience - _xpToNextLevel;
		_level++;
		_xpToNextLevel = (int) (_xpToNextLevel * modifier);
		double random = ThreadLocalRandom.current().nextDouble(); // ref http://stackoverflow.com/questions/3680637/generate-a-random-double-in-a-range
		if (random <= chanceOfReward) {
			int randomAward = ThreadLocalRandom.current().nextInt(_music.size());
			setCanPlay(randomAward);
			//every time you level up you have chanceOfReward chance of possibly unlocking a new track
			//if you do, you might be able to unlock a new track (RNG, you may unlock the same track)
		}
	}
	
	public double levelProgress() {
		return (double)_experience/_xpToNextLevel;
	}
	
	public int getLevel(){ 
		return _level;
	}
	
	public void gainExperience(int xp) {
		_experience += xp;
		update();
	}
	
	public void update() {
		boolean didLevelUp = false;
		while (checkIfLevelUp()) {
			levelUp();
			didLevelUp = true;
		}
		// coupling :( fix later
		MainApp.instance().getHUD().update(didLevelUp);
	}
	
	public int getXP() {
		return _experience;
	}
	
	public int getNextXP() {
		return _xpToNextLevel;
	}
	
	public String toString() {
		return _name;
	}

	
}
