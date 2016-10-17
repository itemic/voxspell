package se206_model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

import se206_util.MediaHandler;
import se206_util.TextToSpeech;
import se206_voxspell.MainApp;

public class QuizModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1958232043783152292L;
	public static int quizSize = 5; ///words in a level
	private static double passThreshold = 0.9; // accuracy needed to pass
	private LevelModel _level;
	private int _correct;
	private int _attempts;
	private int _wordPosition; // where in the list has the user gotten to?
	private boolean _canReplay; //can the user replay?
	private int _currencyGain; //how much the user gained in $ this game.
	private boolean _isCorrect;
	
	public enum QuizState {
		NEW
	}
	
	private ArrayList<WordModel> _allWords;
	private ArrayList<WordModel> _quizWords;

	//Do I want a separate LevelModel?
	public QuizModel(LevelModel level) {
		_correct = 0;
		_level = level;
		_attempts = 0;
		_allWords = level.getWords();
		_quizWords = generateQuizWords();
		_wordPosition = 0;
		_canReplay = true;
		_currencyGain = 0;
		TextToSpeech.access().speak("Round starting: spell " + _quizWords.get(_wordPosition));
	}
	
	
	public ArrayList<WordModel> generateQuizWords() {
		ArrayList<WordModel> generatedWords = new ArrayList<>();
		int bias = 0;
		for (WordModel w: _allWords){
			bias += (100 - w.getWordScore());
//			System.out.println("Bias: " + w.getWordScore() + " |" + w.getWord());
		}
		
		if (_allWords.size() < quizSize) {
			Collections.shuffle(_allWords);
			generatedWords.addAll(_allWords);
			quizSize = _allWords.size(); //update wordcount
		} else {
			Collections.sort(_allWords, new Comparator<WordModel>() {

				@Override
				public int compare(WordModel o1, WordModel o2) {
					int o1WS = o1.getWordScore();
					int o2WS = o2.getWordScore();
					//http://stackoverflow.com/questions/9150446/compareto-with-primitives-integer-int
					return o1WS > o2WS ? 1 : o1WS < o2WS ? -1 : 0;
				}
				
			});
//			System.out.println("All words, in order: " + _allWords.toString());
			for (int i = 0; i < quizSize; i++) {
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
//			System.out.println("Generated: " + generatedWords);
		
		return generatedWords;
	}

	
	public int getQuizSize() {
		return quizSize;
	}
	
	public boolean canReplay() {
		if (_canReplay) {
			TextToSpeech.access().speak(_quizWords.get(_wordPosition) + "");
			_canReplay = false;
		}
		return _canReplay;
	}
	
	public void setReplayable(boolean b) {
		_canReplay = b;
	}
	
	
	public int getCurrentWordPosition() {
		return _wordPosition;
	}
	public ArrayList<WordModel> getQuizWords() {
		return _quizWords;
	}
	
	public String quizAccuracy() {
		if (_attempts == 0) {
			return "--%"; // no value yet
		} else {
			//REFERENCE: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
			double percentage = new BigDecimal((double)((double)_correct / (double)_attempts) * 100).setScale(2, RoundingMode.CEILING).doubleValue();
			return percentage + "%";
		}
	}
	
	public boolean checkWord(String guess) {
		guess = guess.toLowerCase().trim();
		WordModel word = _quizWords.get(_wordPosition);
		boolean isCorrect = word.isCorrect(guess);
		if (isCorrect) { //ensure case insensitivitys
			TextToSpeech.access().speak("You spelled it right.");
			_correct++;
			_attempts++;
			_currencyGain += word.getXP();
		} else {
			TextToSpeech.access().speak("Incorrect.");
			_attempts++;
		}
		_wordPosition++;
		_isCorrect = isCorrect;
		return isCorrect;
	}
	
	//based on michael's implementation in prototype
	public boolean loadNext() {
		if (_wordPosition == quizSize) {
			TextToSpeech.access().speak("Round over.");
			MediaHandler.stop();
			//game is finished
			return false;
		} else {
			if (_isCorrect) {
				TextToSpeech.access().speak("Spell " + _quizWords.get(_wordPosition).toString());
			} else {
				TextToSpeech.access().speak("Spell " + _quizWords.get(_wordPosition).toString());
			}
			_canReplay = true; //
			return true;
		}
	}
	
	public int getCurrencyGain() {
		return _currencyGain;
	}
	
	public LevelModel getLevel() {
		return _level;
	}
	
	public int getCorrect() {
		return _correct;
	}
}


