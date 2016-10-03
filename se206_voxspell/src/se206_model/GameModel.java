package se206_model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import se206_util.WordListReader;

public class GameModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8437363611385886849L;
	private ArrayList<LevelModel> _quizWords = new ArrayList<>();

	
	public GameModel(String wordList) throws IOException {
		WordListReader wlr = new WordListReader(wordList);
		ArrayList<ArrayList<String>> words = wlr.readWords();
		ArrayList<String> levels = wlr.getLevelNames();
		
		for (int i = 0; i < words.size(); i++){
			//each level
			_quizWords.add(new LevelModel(levels.get(i)));
			for (String word: words.get(i)) {
				_quizWords.get(i).addWord(word);
			}
		}
	}
	
	public ArrayList<WordModel> getWordsFromLevel(int levelID) {
		return _quizWords.get(levelID).getWords();
	}
	
	public ArrayList<LevelModel> getLevels() {
		return _quizWords;
	}
	
}
