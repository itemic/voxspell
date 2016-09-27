package se206_voxspell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class GameModel {
	private ArrayList<ArrayList<WordModel>> quizWords = new ArrayList<>();
	
	public GameModel(String wordList) throws IOException {
		WordListReader wlr = new WordListReader(wordList);
		ArrayList<ArrayList<String>> words = wlr.readWords();
		ArrayList<String> levels = wlr.getLevelNames();
		
		for (int i = 0; i < levels.size(); i++){
			quizWords.add(new ArrayList<WordModel>());
			for (String word: words.get(i)) {
				quizWords.get(i).add(new WordModel(word, levels.get(i)));
			}
		}
	}
	
	public ArrayList<WordModel> getWordsFromCategory(int categoryID) {
		return quizWords.get(categoryID);
	}
}
