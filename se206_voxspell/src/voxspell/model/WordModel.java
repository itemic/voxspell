package voxspell.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


/**
 * Class associated with an individual word. Tracks how many times
 * a word has been spelled (incl. correct) and its "score".
 * @author terran
 *
 */
public class WordModel implements Serializable {

	private static final long serialVersionUID = 6323087623510671956L;
	public String _word;
	private int _correct;
	private int _attempts; //logically incorrect would be a-c
	private LevelModel _level; //level if you will
	private LocalDate _lastTried; //to be considered (NOT IN CONSTRUCTORS YET)
	private int _wordScore; // 0-100 based on user's performance
	private int _credits; // experience points earned
	
	
	public WordModel(String word, LevelModel level) {
		_word = word;
		_correct = 0;
		_attempts = 0;
		_level = level;
		_lastTried = null;
		_wordScore = 50; //middle ground for new words (neutral)
		_credits = word.length(); //experience proportional to word length
		
	}
	
	/**
	 * Checks if the user input is correct, and adjusts the stats and
	 * "score" associated with it if the user gets it right/wrong.
	 * @param input
	 * @return
	 */
	public boolean isCorrect(String input) {
		//checks if a word is correct and alters stats based on it
		boolean userCorrect = (input.trim().equals(_word.toLowerCase()));
		if (userCorrect) {
			_correct++;
			_attempts++;
			_level.isCorrect();
			if (_wordScore < 50) {
				_wordScore += 10;
			} else if (_wordScore < 75) {
				_wordScore += 15;
			} else if (_wordScore < 100) {
				_wordScore += 20;
			}
			
			if (_wordScore > 100) {
				_wordScore = 100;
			}
		} else {
			_attempts++;
			_level.isIncorrect();
			if (_wordScore > 75) {
				_wordScore -= 30;
			} else if (_wordScore > 50) {
				_wordScore -= 20;
			} else if (_wordScore > 0) {
				_wordScore -= 15;
			}
			
			if (_wordScore < 0) {
				_wordScore = 0;
			}
		}
		_lastTried = LocalDate.now(); //update the last tried date
		return userCorrect;
	}
	
	public boolean equals(Object obj){
		if (obj == null) {
			return false;
		}

		return this.toString().equals(obj.toString());

	}
	
////////// ACCESSOR METHODS //////////
	
	/**
	 * Gets a String representation of when the user last tried this word
	 * A String is used because the user may have not tried the word (ie null)
	 * @return Date cast to a String
	 */
	public String getLastTriedStr() {
		if (_lastTried == null) {
			return "not practiced";
		} else {
			return _lastTried.toString();
		}
	}
	
	/**
	 * Getter for the word itself
	 * @return
	 */
	public String getWord() {
		return _word;
	}
	
	/**
	 * Gets the accuracy represented as a String (because of --% if it has
	 * not been tried yet).
	 * @return
	 */
	public String getAccuracy() {
		if (_attempts == 0) {
			return "--%"; // no value yet
		} else {
			//REFERENCE: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
			double percentage = new BigDecimal((double)((double)_correct / (double)_attempts) * 100).setScale(2, RoundingMode.CEILING).doubleValue();
			return percentage + "%";
		}
	}

	public int getTimesCorrect() {
		return _correct;
	}
	
	public int getTotalAttempts() {
		return _attempts;
	}
	
	public int getWordScore() {
		return _wordScore;
	}
	
	public String toString() {
		return _word;
	}
	
	public int getCredits() {
		return _credits;
	}
	

		
}
