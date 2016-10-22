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
	private Label promptLabel; // Spell what you hear...

	@FXML
	private TextField inputField;

	@FXML
	private Button replayBtn;

	@FXML
	private Button leaveQuizBtn;

	@FXML
	private Button submitBtn;

	private Alert alert = new Alert(AlertType.CONFIRMATION);

	private ArrayList<Node> disable = new ArrayList<>(); // all the buttons that
															// should be stopped
															// during Festival

	@FXML //when the user presses the replay button
	void replayPressed(ActionEvent event) {
		TextToSpeech.access().speak(_quiz.getCurrentWord(), disable);
	}

	@FXML //when the user checks their answer
	void submitPressed(ActionEvent event) throws InterruptedException {
		String guess = inputField.getText();
		if (!guess.trim().isEmpty()) { //disable empty lines
			boolean isSpelledCorrectly = _quiz.checkWord(guess);
			if (isSpelledCorrectly) {
				TextToSpeech.access().speak("You got it right.", disable);
			} else {
				TextToSpeech.access().speak("Incorrect.", disable);
			}
			update();
			inputField.clear();
			boolean levelHasMore = _quiz.loadNext();
			MainApp.instance().save(); // save on every click
			
			//if it is the last word, end the round
			if (!levelHasMore) {
				try {
					TextToSpeech.access().speak("Round over.", disable);
					//update the user's credit amount
					int quizCurrencyGain = _quiz.getCurrencyGain();
					MainApp.instance().getUser().gainCurrency(quizCurrencyGain);
					MediaHandler.stop(); //stop soundtrack
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainApp.class.getResource("LevelCompleteMenu.fxml"));
					BorderPane levelSelectPane = (BorderPane) loader.load();
					LevelCompleteMenuController controller = loader.<LevelCompleteMenuController>getController();
					controller.initComplete(_quiz);
					BorderPane root = MainApp.getRoot();
					root.setCenter(levelSelectPane);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				TextToSpeech.access().speak("Spell " + _quiz.getCurrentWord(), disable);
			}
			inputField.requestFocus();
		}

	}

	//Sets up a quiz with the selected level (and start speaking)
	void initGame(LevelModel level) {
		_quiz = new QuizModel(level);
		TextToSpeech.access().speak("Round starting. Spell " + _quiz.getCurrentWord(), disable);
		update();
	}

	// Update all the relevant labels
	void update() {
		levelLabel.setText(_quiz.getLevel().toString());
		levelAccuracyLabel.setText(_quiz.quizAccuracy());
		allTimeLabel.setText(_quiz.getLevel().levelAccuracy());
		progressLabel.setText(_quiz.getCurrentWordPosition() + "/" + _quiz.getQuizSize());
	}

	@FXML //code to stop the game when the user quits
	void quitEarly() {
		alert.setTitle("In A Hurry?");
		alert.setHeaderText("Are you sure you want to leave early?");
		alert.setContentText("You won't get any VOXCoins if you leave now.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
				BorderPane levelSelectPane = (BorderPane) loader.load();

				BorderPane root = MainApp.getRoot();
				root.setCenter(levelSelectPane);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// setup the controls that should be disabled during Festival TTS
		disable.add(inputField);
		disable.add(replayBtn);
		disable.add(submitBtn);
		disable.add(leaveQuizBtn);

	}

}
