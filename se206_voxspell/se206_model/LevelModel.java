package se206_model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class LevelModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8876244034319084689L;
	private String _levelName;
	private ArrayList<WordModel> _levelWords;
	private int _correct;
	private int _attempts;
	private int _charsInLevel;
	private boolean _availableForPlay;
	private int _levelCost;
	
	public LevelModel(String levelName) {
		_levelName = levelName;
		_levelWords = new ArrayList<>();
		_correct = 0;
		_attempts = 0;
		_charsInLevel = 0;
		_availableForPlay = false;
		_levelCost = 250;
	}
	
	public void addWord(String word) {
		WordModel newWord = new WordModel(word.toLowerCase(), this); //ALL WORDS LOWERCASE
		if (!_levelWords.contains(newWord)){
			_levelWords.add(newWord);
			_charsInLevel += newWord.size();
		}

	}
	
	public boolean canPlayLevel() {
		return _availableForPlay;
	}
	
	public void toggleCanPlay() {
		_availableForPlay = !_availableForPlay;
	}
	
	public void removeWord(WordModel word){
		_levelWords.remove(word);
		_charsInLevel -= word.size();
	}
	
	public int getLevelRequirement() {
		return _charsInLevel / _levelWords.size();
	}
	
	public int getLevelCost() {
		return _levelCost;
	}
	
	public ArrayList<WordModel> getWords() {
		return _levelWords;
	}
	
	public String getName() {
		return _levelName;
	}
	
	public String toString() {
		return "" + _levelName;
	}
	
	public void isCorrect() {
		_correct++;
		_attempts++;
	}
	
	public void isIncorrect() {
		_attempts++;
	}
	
	public String levelAccuracy() {
		if (_attempts == 0) {
			return "--%"; // no value yet
		} else {
			//REFERENCE: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
			double percentage = new BigDecimal((double)((double)_correct / (double)_attempts) * 100).setScale(2, RoundingMode.CEILING).doubleValue();
			return percentage + "%";
		}
	}
}
