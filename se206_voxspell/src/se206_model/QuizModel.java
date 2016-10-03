package se206_model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import se206_util.TextToSpeech;

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
	private int _quizXP; //how much the user gained in XP this game.
	
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
		_quizXP = 0;
		TextToSpeech.access().speak("Round starting: spell " + _quizWords.get(_wordPosition));
	}
	
	public ArrayList<WordModel> generateQuizxWords() {
		// TODO: Take into account of weighting
		ArrayList<WordModel> generatedWords = new ArrayList<>();
		Collections.shuffle(_allWords);
		if (_allWords.size() < quizSize) {
			generatedWords.addAll(_allWords);
			quizSize = _allWords.size(); //update wordcount
		} else {
			for (int i = 0; i < quizSize; i++) {
				generatedWords.add(_allWords.get(i));
			}
		}
		return generatedWords;
	}
	
	public ArrayList<WordModel> generateQuizWords() {
		ArrayList<WordModel> generatedWords = new ArrayList<>();
		int bias = 0;
		for (WordModel w: _allWords){
			bias += (100 - w.getWordScore());
		}
		
		Collections.shuffle(_allWords);
		if (_allWords.size() < quizSize) {
			generatedWords.addAll(_allWords);
			quizSize = _allWords.size(); //update wordcount
		} else {
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
			
		
		return generatedWords;
	}
	// REF: http://gamedev.stackexchange.com/questions/54551/using-random-numbers-with-a-bias
	public WordModel getBiasedWord(int bias) {
		if (bias > 0) {
			int random = (int)(Math.random() * bias);
			System.out.println(random);
			for (WordModel w: _allWords) {
				if (random < w.getWordScore()) {
					System.out.println("Adding:" + w);
					return w;
				} else {
					bias -= w.getWordScore();
				}
			}
		}
		return null;
	}
	
	public int getQuizSize() {
		return quizSize;
	}
	
	public boolean canReplay() {
		if (_canReplay) {
			TextToSpeech.access().speak("Listen carefully, spell " + _quizWords.get(_wordPosition));
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
		guess = guess.toLowerCase();
		WordModel word = _quizWords.get(_wordPosition);
		boolean isCorrect = word.isCorrect(guess);
		if (isCorrect) { //ensure case insensitivitys
			TextToSpeech.access().speak("You spelled it right.");
			_correct++;
			_attempts++;
			_quizXP += word.getXP();
		} else {
			TextToSpeech.access().speak("Incorrect spelling.");
			_attempts++;
		}
		_wordPosition++;
		return isCorrect;
	}
	
	//based on michael's implementation in prototype
	public boolean loadNext() {
		if (_wordPosition == quizSize) {
			TextToSpeech.access().speak("Round over.");
			//game is finished
			return false;
		} else {
			TextToSpeech.access().speak("Spell " + _quizWords.get(_wordPosition).toString());
			_canReplay = true; //
			return true;
		}
	}
	
	public int xpEarnedThisQuiz() {
		return _quizXP;
	}
	
	public LevelModel getLevel() {
		return _level;
	}
	
	public int getCorrect() {
		return _correct;
	}
}


