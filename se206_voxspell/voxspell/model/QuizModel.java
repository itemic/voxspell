package voxspell.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

import voxspell.util.MediaHandler;

public class QuizModel implements Serializable {

	private static final long serialVersionUID = -1958232043783152292L;
	public static int quizSize = 10; ///words in a level
	private LevelModel _level;
	private int _correct;
	private int _attempts;
	private int _wordPosition; // where in the list has the user gotten to?
	private int _currencyGain; //how much the user gained in $ this game.

	private ArrayList<WordModel> _allWords;
	private ArrayList<WordModel> _quizWords;

	//Creates a quiz based on the provided level
	public QuizModel(LevelModel level) {
		_correct = 0;
		_level = level;
		_attempts = 0;
		_allWords = level.getWords();
		_quizWords = generateQuizWords();
		_wordPosition = 0;
		_currencyGain = 0;
	}
	
	/**
	 * Creates a list of words to be quizzed
	 * using a random algorithm that prioritizes
	 * words that the user is not so great at
	 * @return
	 */
	public ArrayList<WordModel> generateQuizWords() {
		ArrayList<WordModel> generatedWords = new ArrayList<>();
		int bias = 0; 
		for (WordModel w: _allWords){
			bias += (100 - w.getWordScore());
		}
		
		if (_allWords.size() < quizSize) {
			//just shuffle the words if there are fewer words 
			//in this level than the number of words for a quiz
			Collections.shuffle(_allWords);
			generatedWords.addAll(_allWords);
			quizSize = _allWords.size(); //update wordcount
		} else {
			//Sort the words from worst to best in terms of wordscore
			Collections.sort(_allWords, new Comparator<WordModel>() {

				@Override
				public int compare(WordModel o1, WordModel o2) {
					int o1WS = o1.getWordScore();
					int o2WS = o2.getWordScore();
					return o1WS > o2WS ? 1 : o1WS < o2WS ? -1 : 0;
				}
				
			});
			for (int i = 0; i < quizSize; i++) {
				//randomly choose an integer and use rejection sampling to choose
				//the words to add to the quiz
				int chosen = ThreadLocalRandom.current().nextInt(0, bias);
				int selectedWord = (int) (((double)chosen/bias) * (_allWords.size()-1));
				WordModel candidate = _allWords.get(selectedWord);
				if (!generatedWords.contains(candidate)) {
					generatedWords.add(candidate);
				} else {
					i--;
				}
			}
		}
		
		return generatedWords;
	}

	/**
	 * Getter for the size of the quiz
	 * Note: it's not always 10 because if the number of words
	 * in the level is < 10, then it would change
	 * @return
	 */
	public int getQuizSize() {
		return quizSize;
	}
	

	/**
	 * Get what part of the wordlist the user is on in the quiz
	 * @return
	 */
	public int getCurrentWordPosition() {
		return _wordPosition;
	}
	
	/**
	 * Returns the words for this quiz
	 * @return
	 */
	public ArrayList<WordModel> getQuizWords() {
		return _quizWords;
	}
	
	/**
	 * Returns string value for the current accuracy of the level
	 * It is a String because in the beginning of a quiz there is no accuracy
	 * @return
	 */
	public String quizAccuracy() {
		if (_attempts == 0) {
			return "--%"; // no value yet
		} else {
			//REFERENCE: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
			double percentage = new BigDecimal((double)((double)_correct / (double)_attempts) * 100).setScale(2, RoundingMode.CEILING).doubleValue();
			return percentage + "%";
		}
	}
	
	/**
	 * Checks whether the user's input is spelled correct
	 * @param guess the user's input
	 * @return
	 */
	public boolean checkWord(String guess) {
		guess = guess.toLowerCase().trim();
		WordModel word = _quizWords.get(_wordPosition);
		boolean isCorrect = word.isCorrect(guess); //update the word stats with this
		if (isCorrect) { //ensure case insensitivity
			_correct++;
			_attempts++;
			_currencyGain += word.getCredits(); //gain credits if the user gets it right
		} else {
			_attempts++;
		}
		_wordPosition++;
		return isCorrect;
	}
	
	/**
	 * Checks if there are more words left in the game
	 * based on michael's implementation in prototype
	 * @return
	 */
	public boolean loadNext() {
		if (_wordPosition == quizSize) {
			MediaHandler.stop(); //stop the soundtrack
			//game is finished
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Get how much the user has earned this level
	 * @return
	 */
	public int getCurrencyGain() {
		return _currencyGain;
	}
	
	/**
	 * Get the current word that is being tested
	 * @return
	 */
	public String getCurrentWord() {
		return _quizWords.get(_wordPosition).toString();
	}
	
	/**
	 * Get the name of the level that the user is in
	 * @return
	 */
	public LevelModel getLevel() {
		return _level;
	}
	
	/**
	 * Get the number of words correct in the quiz
	 * @return
	 */
	public int getCorrect() {
		return _correct;
	}
}


