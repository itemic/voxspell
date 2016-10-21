package voxspell.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import voxspell.util.WordListReader;

public class GameModel implements Serializable {

	private static final long serialVersionUID = 8437363611385886849L;
	private ArrayList<LevelModel> _quizWords = new ArrayList<>();

	
	/**
	 * Creates a Game objcet based on the wordlist provided
	 * @param wordList
	 * @throws IOException
	 */
	public GameModel(String wordList) throws IOException {
		WordListReader wlr = new WordListReader(wordList);
		ArrayList<ArrayList<String>> words = wlr.readWords();
		ArrayList<String> levels = wlr.getLevelNames();
		
		for (int i = 0; i < words.size(); i++){
			//each level
			_quizWords.add(new LevelModel(levels.get(i)));
			for (String word: words.get(i)) {
				_quizWords.get(_quizWords.size()-1).addWord(word);
			}
		}
	}
	
	/**
	 * Adds further words to the Game object if the user
	 * wants to have more words in their wordlist.
	 * @param wordList
	 * @throws IOException
	 */
	public void addImportedLists(String wordList) throws IOException {
		WordListReader wlr = new WordListReader(wordList);
		ArrayList<ArrayList<String>> words = wlr.readWords();
		ArrayList<String> levels = wlr.getLevelNames();
		for (int i = 0; i < words.size(); i++){
			//each level
			_quizWords.add(new LevelModel(levels.get(i)));
			for (String word: words.get(i)) {
				_quizWords.get(_quizWords.size()-1).addWord(word);
			}
		}
	}
	
	/**
	 * Adds a level to this Game object as well as specified words
	 * @param levelName
	 * @param words
	 */
	public void addSingleLevel(String levelName, ArrayList<String> words) {
		LevelModel lv = new LevelModel(levelName); //create the new Level Model object
		_quizWords.add(lv); //associate this new level with the game
		for (String word: words) {
			lv.addWord(word); //add the word
		}
	}
	
 

	/**
	 * Get all the levels associated with this Game object
	 * @return
	 */
	public ArrayList<LevelModel> getLevels() {
		return _quizWords;
	}
	
}
