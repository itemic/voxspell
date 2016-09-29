package se206_voxspell;

import java.util.ArrayList;

public class QuizModel {
	public static final int quizSize = 3; ///words in a level
	private static double passThreshold = 0.9; // accuracy needed to pass
	
	private int _level;
	private int _correct;
	private int _attempts;
	private int _wordPosition; // where in the list has the user gotten to?
	
	public enum QuizState {
		NEW
	}
	
	private ArrayList<WordModel> _allWords;
	private ArrayList<WordModel> _quizWords;

	//Do I want a separate LevelModel?
	public QuizModel(ArrayList<WordModel> levelWords) {
		_correct = 0;
//		_level = levelID;
		_attempts = 0;
//		_allWords = 
	}
	
}
