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
	
	public LevelModel(String levelName) {
		_levelName = levelName;
		_levelWords = new ArrayList<>();
		_correct = 0;
		_attempts = 0;
	}
	
	public void addWord(String word) {
		WordModel newWord = new WordModel(word, this);
		_levelWords.add(newWord);	
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
