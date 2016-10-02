package se206_voxspell;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class GameMenuController {

	private QuizModel _quiz;
	
    @FXML
    private Label levelLabel;

    @FXML
    private Label allTimeLabel; //temporaily use as progress (current game percentage)

    @FXML
    private TextField inputField;

    @FXML
    private Button replayBtn;

    @FXML
    private Button hintBtn;

    @FXML
    private Button submitBtn;

    @FXML
    void hintPressed(ActionEvent event) {

    }

    @FXML
    void replayPressed(ActionEvent event) {

    }

    @FXML
    void submitPressed(ActionEvent event) {
    	String guess = inputField.getText();
    	boolean isSpelledCorrectly = _quiz.checkWord(guess);
    	update();
    	inputField.clear();
    	boolean levelHasMore = _quiz.loadNext();
    	if (!levelHasMore) {
    		try {
        		FXMLLoader loader = new FXMLLoader();
        		loader.setLocation(MainApp.class.getResource("LevelCompleteMenu.fxml"));
        		BorderPane levelSelectPane = (BorderPane)loader.load();
        		
        		BorderPane root = MainApp.getRoot();
        		root.setCenter(levelSelectPane);
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
    	} 

    	//TEMP CODE TO TEST DUMMY
    	
    }

    void loadGame(LevelModel level) {
    	System.out.println("Starting game: " + level);
    	_quiz = new QuizModel(level);
    	System.out.println("Generating words: " + _quiz.generateQuizWords());
    	update();
    	//used to pass in necessary stuff
    }
    
    void update() {
    	levelLabel.setText(_quiz.getLevel().toString());
    	allTimeLabel.setText(_quiz.quizAccuracy());
    }

}

