package se206_voxspell;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;

public class QuizModel {
	public static int quizSize = 5; ///words in a level
	private static double passThreshold = 0.9; // accuracy needed to pass
	private LevelModel _level;
	private int _correct;
	private int _attempts;
	private int _wordPosition; // where in the list has the user gotten to?
	
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
		TextToSpeech.access().speak("Round starting: spell " + _quizWords.get(_wordPosition));
	}
	
	public ArrayList<WordModel> generateQuizWords() {
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
	
	public ArrayList<WordModel> getQuizWords() {
		return _quizWords;
	}
	
	public String quizAccuracy() {
		if (_attempts == 0) {
			return "-- %"; // no value yet
		} else {
			//REFERENCE: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
			double percentage = new BigDecimal((double)((double)_correct / (double)_attempts) * 100).setScale(2, RoundingMode.CEILING).doubleValue();
			return percentage + " %";
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
			System.out.println(_correct + " " + _attempts);
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
			return true;
		}
	}
	
	public LevelModel getLevel() {
		return _level;
	}
}
