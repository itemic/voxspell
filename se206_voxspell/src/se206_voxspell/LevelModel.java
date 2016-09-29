package se206_voxspell;

import java.util.ArrayList;

public class LevelModel {
	private String _levelName;
	private ArrayList<WordModel> _levelWords;
	
	public LevelModel(String levelName) {
		_levelName = levelName;
		_levelWords = new ArrayList<>();
	}
	
	public void addWord(String word) {
		WordModel newWord = new WordModel(word, _levelName);
		_levelWords.add(newWord);	
	}
	
	public ArrayList<WordModel> getWords() {
		return _levelWords;
	}
	
	public String getName() {
		return _levelName;
	}
	
}
