package voxspell.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import voxspell.model.LevelModel;
import voxspell.model.QuizModel;
import voxspell.util.MediaHandler;
import voxspell.util.TextToSpeech;

public class GameMenuController implements Initializable {

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
    
    private ArrayList<Node> disable = new ArrayList<>();

    @FXML
    void replayPressed(ActionEvent event) {
    	TextToSpeech.access().speak(_quiz.getCurrentWord(), disable);
    }

    @FXML
    void submitPressed(ActionEvent event) {
    	String guess = inputField.getText();
    	if (guess.trim() != "") {
    		boolean isSpelledCorrectly = _quiz.checkWord(guess);
        	if (isSpelledCorrectly) {
        		TextToSpeech.access().speak("You got it right.", disable);
        	} else {
        		TextToSpeech.access().speak("Incorrect.", disable);
        	}
        	update();
        	inputField.clear();
        	boolean levelHasMore = _quiz.loadNext();
        	MainApp.instance().save(); //save on every click?
        	if (!levelHasMore) {
        		try {
            		TextToSpeech.access().speak("Round over.", disable);
        			int quizXP = _quiz.getCurrencyGain();
        			MainApp.instance().getUser().gainCurrency(quizXP);
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
//        		System.out.println("Spell " + _quiz.getCurrentWord());
        		TextToSpeech.access().speak("Spell " + _quiz.getCurrentWord(), disable);
        	}
        	inputField.requestFocus();
    	}
    	

    	//TEMP CODE TO TEST DUMMY
    	
    }

    void loadGame(LevelModel level) {
    	_quiz = new QuizModel(level);
    	TextToSpeech.access().speak("Round starting. Spell " + _quiz.getCurrentWord(), disable);
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
    	alert.setContentText("You won't get any credits if you leave now.");
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		disable.add(inputField);
		disable.add(replayBtn);
		disable.add(submitBtn);
		disable.add(leaveQuizBtn);
		
	}

}

