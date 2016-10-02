package se206_voxspell;

import java.io.FileNotFoundException;
import java.io.IOException;

public class VOXTest {

	public static void main(String[] args) throws IOException {
		//Playground for testing things before I get into the GUIs.
		
		WordListReader wlr = new WordListReader();
		GameModel game = new GameModel("resources/nzcer-wordlist.txt");
		System.out.println(game.getWordsFromLevel(4));
		for (LevelModel level : game.getLevels()) {
			System.out.println(level);
			System.out.println(level.getWords());
			QuizModel quiz = new QuizModel(level); //wont work lmao
			System.out.println(quiz.generateQuizWords());
		}
//		QuizModel quiz = new QuizModel(level, 3);

	}

}
