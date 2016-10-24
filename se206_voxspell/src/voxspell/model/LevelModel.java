package voxspell.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Class to represent the Level, which contains all the words
 * in a given "level". Also handles statistics.
 * @author terran
 *
 */
public class LevelModel implements Serializable {

	private static final long serialVersionUID = 8876244034319084689L;
	private String _levelName;
	private ArrayList<WordModel> _levelWords;
	private int _correct;
	private int _attempts;
	private boolean _availableForPlay;
	
	public LevelModel(String levelName) {
		_levelName = levelName;
		_levelWords = new ArrayList<>();
		_correct = 0;
		_attempts = 0;
		_availableForPlay = false;
	}
	
	/**
	 * Add a word to the level (lowercase)
	 * @param word
	 */
	public void addWord(String word) {
		WordModel newWord = new WordModel(word.toLowerCase(), this); //ALL WORDS LOWERCASE
		if (!_levelWords.contains(newWord)){
			_levelWords.add(newWord);
		}

	}
	
	/**
	 * Remove a word from the level
	 * @param word
	 */
	public void removeWord(WordModel word){
		_levelWords.remove(word);
	}
	
	/**
	 * Check if the level is available to be played
	 * @return whether the user can play this level
	 */
	public boolean canPlayLevel() {
		return _availableForPlay;
	}
	
	/**
	 * Toggle whether the level can be played (usually just used to set to available)
	 */
	public void toggleCanPlay() {
		_availableForPlay = !_availableForPlay;
	}
	
	
	/**
	 * Gets all the words in a level
	 * @return the arraylist with all the words in the selected level
	 */
	public ArrayList<WordModel> getWords() {
		return _levelWords;
	}
	
	
	public String toString() {
		return "" + _levelName;
	}
	
	/**
	 * Incrememt level based statistics if the user gets it right
	 */
	public void isCorrect() {
		_correct++;
		_attempts++;
	}
	
	/**
	 * Increment level based statistics if the user gets it wrong
	 */
	public void isIncorrect() {
		_attempts++;
	}
	
	/**
	 * Get a string of the user's accuracy in this level
	 * @return
	 */
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
