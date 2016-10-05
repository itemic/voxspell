package se206_model;

import java.io.IOException;
import java.io.Serializable;

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
	private final double modifier = 1.2; // how much to next level
	private GameModel _game;
	
	//EXPERIENCE REQUIRED: base (100) * 1.2 ^ (level-1)
	
	public UserModel(String name) throws IOException {
		_name = name;
		_level = 1;
		_experience = 0;
		_xpToNextLevel = base;
		_game = new GameModel("resources/nzcer-wordlist.txt");
	}
	
	public UserModel(String name, String filename) throws IOException {
		_name = name;
		_level = 1;
		_experience = 0;
		_xpToNextLevel = base;
		_game = new GameModel(filename);
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
		while (checkIfLevelUp()) {
			levelUp();
		}
		// coupling :( fix later
		MainApp.instance().getHUD().update();
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
