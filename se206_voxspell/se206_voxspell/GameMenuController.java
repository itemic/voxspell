package se206_voxspell;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import se206_model.LevelModel;
import se206_model.QuizModel;
import se206_util.MediaHandler;

public class GameMenuController {

	private QuizModel _quiz;
	
    @FXML
    private Label levelLabel;

    @FXML
    private Label allTimeLabel; // this level's accuracy

    @FXML
    private Label progressLabel;
    
    @FXML
    private Label levelAccuracyLabel; // this current game's accuracy
    
    @FXML
    private TextField inputField;

    @FXML
    private Button replayBtn;

    @FXML
    private Button hintBtn;
    
    @FXML
    private Button leaveQuizBtn;

    @FXML
    private Button submitBtn;

    private Alert alert = new Alert(AlertType.CONFIRMATION);
    

    @FXML
    void replayPressed(ActionEvent event) {
    	boolean canReplay = _quiz.canReplay();
    	replayBtn.setDisable(true);
    }

    @FXML
    void submitPressed(ActionEvent event) {
    	String guess = inputField.getText();
    	boolean isSpelledCorrectly = _quiz.checkWord(guess);
    	update();
    	inputField.clear();
    	boolean levelHasMore = _quiz.loadNext();
    	MainApp.instance().save(); //save on every click?
    	if (!levelHasMore) {
    		try {
    			int quizXP = _quiz.xpEarnedThisQuiz();
    			MainApp.instance().getUser().gainExperience(quizXP);
    			MediaHandler.stop();
        		FXMLLoader loader = new FXMLLoader();
        		loader.setLocation(MainApp.class.getResource("LevelCompleteMenu.fxml"));
        		BorderPane levelSelectPane = (BorderPane)loader.load();
        		LevelCompleteMenuController controller = loader.<LevelCompleteMenuController>getController();
        		controller.init(_quiz);
        		BorderPane root = MainApp.getRoot();
        		root.setCenter(levelSelectPane);
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
    	} else {
    		replayBtn.setDisable(false);
    	}
    	inputField.requestFocus();

    	//TEMP CODE TO TEST DUMMY
    	
    }

    void loadGame(LevelModel level) {
    	_quiz = new QuizModel(level);
    	update();
    	//used to pass in necessary stuff
    }
    
    void update() {
    	levelLabel.setText(_quiz.getLevel().toString());
    	levelAccuracyLabel.setText(_quiz.quizAccuracy());
    	allTimeLabel.setText(_quiz.getLevel().levelAccuracy());
    	progressLabel.setText(_quiz.getCurrentWordPosition() + "/" + _quiz.getQuizSize());
    }
    
    @FXML
    void quitEarly() {
    	alert.setTitle("In A Hurry?");
    	alert.setHeaderText("Are you sure you want to leave early?");
    	alert.setContentText("You won't get any experience if you leave now.");
    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK) {
    		try {
        		FXMLLoader loader = new FXMLLoader();
        		loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
        		BorderPane levelSelectPane = (BorderPane)loader.load();
        		
        		BorderPane root = MainApp.getRoot();
        		root.setCenter(levelSelectPane);
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
    	} else {
    		
    	}
    }

}

