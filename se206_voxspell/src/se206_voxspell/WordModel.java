package se206_voxspell;

import java.util.Date;

public class WordModel {
	public String _word;
	private int _correct;
	private int _attempts; //logically incorrect would be a-c
	private String _category; //level if you will
	private Date _lastTried; //to be considered (NOT IN CONSTRUCTORS YET)
	private int _userRating; // 1-9 based on user's performance
	private int _experience; // experience points earned
	
	public WordModel(String word, String category) {
		_word = word;
		_correct = 0;
		_attempts = 0;
		_category = category;
//		_lastTried = null
		_userRating = 5; //middle ground for new words (neutral)
		_experience = word.length(); //experience proportional to word length
	}
}
