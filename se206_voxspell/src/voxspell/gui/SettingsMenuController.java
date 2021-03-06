package voxspell.gui;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import voxspell.model.GameType;
import voxspell.util.MediaHandler;
import voxspell.util.Save;
import voxspell.util.TextToSpeech;

/**
 * Controller class for Settings
 * @author terran
 *
 */
public class SettingsMenuController implements Initializable {

	private ObservableList<String> obVoices = FXCollections.observableArrayList(TextToSpeech.access().voices());
	private ObservableList<String> obSounds;

	@FXML
	private ComboBox<String> voiceComboBox;

	@FXML
	private Button aboutBtn;

	@FXML
	private Button previewBtn;

	@FXML
	private Slider soundtrackVolumeBar;

	@FXML
	private Button previewTrackBtn;
	@FXML
	private Button resetBtn;

	@FXML
	private ComboBox<String> soundtrackComboBox;

	@FXML
	private Button backBtn;

	private Alert alert = new Alert(AlertType.CONFIRMATION);

	private ArrayList<Node> allControls = new ArrayList<>();

	/**
	 * Button action to go back to menu
	 * @param event
	 */
	@FXML
	void backToMenu(ActionEvent event) {
		MainApp.instance().save(); // save settings
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

	/**
	 * Button action to preview the TTS voice
	 * @param event
	 */
	@FXML
	void previewVoice(ActionEvent event) { // previews the TTS voice
		TextToSpeech.access().speak("Hi, my name is " + TextToSpeech.access().selectedVoice(), allControls);
	}

	/**
	 * Button action to show the About screen
	 */
	@FXML
	void showAbout() { // opens the about screen
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("HelpScreen.fxml"));
			BorderPane back = (BorderPane) loader.load();

			BorderPane root = MainApp.getRoot();
			root.setCenter(back);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Button action to start the reset game action
	 * @param event
	 */
	@FXML
	void resetGame(ActionEvent event) {
		// confirms that the user wants to delete user
		alert.setTitle("Delete user");
		alert.setHeaderText("Are you sure you want to delete user?");
		alert.setContentText("The user " + MainApp.instance().getUser().toString()
				+ " will be deleted, along with all its statistics. There's no going back.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			//Try and delete the user if they confirm
			try {
				Files.deleteIfExists(
						Paths.get(Save.DIRECTORY + MainApp.instance().getUser().toString() + Save.EXTENSION));
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("UserSelectionMenu.fxml"));
				BorderPane profiles = (BorderPane) loader.load();
				UserSelectionMenuController controller = loader.<UserSelectionMenuController>getController();
				controller.initProfile(); // go back to user selection
				MainApp.getRoot().setCenter(profiles);
				MainApp.getRoot().setTop(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Code to populate soundtracks
	 */
	public void initSettings() {
		ArrayList<String> availMusic = new ArrayList<>();
		availMusic.add("None"); // "no soundtrack"
		// adds all the available soundtracks to the drop down
		for (int i = 0; i < MainApp.instance().getUser().getMusicList().size(); i++) {
			if (MainApp.instance().getUser().getCanPlay(i)
					|| MainApp.instance().getUser().getGameType().equals(GameType.FREEPLAY)) {
				String s = MainApp.instance().getUser().getMusicList().get(i);
				availMusic.add(s);
			}
		}
		obSounds = FXCollections.observableArrayList(availMusic);
		soundtrackComboBox.getItems().addAll(obSounds);
		soundtrackComboBox.getSelectionModel().select(MainApp.instance().getUser().getDisplaySoundtrack());
		voiceComboBox.getItems().addAll(obVoices);
		voiceComboBox.getSelectionModel().select(TextToSpeech.access().selectedVoiceNum());
	}

	/**
	 * Changes the current default soundtrack
	 */
	public void changeTrack() {
		MediaHandler.stop();
		MainApp.instance().getUser().setCurrentSoundtrack(soundtrackComboBox.getSelectionModel().getSelectedItem());
	}

	/**
	 * Changes the TTS voice
	 */
	public void changeVoice() { 
		TextToSpeech.access().chooseVoice(voiceComboBox.getSelectionModel().getSelectedIndex());
	}

	/**
	 * Start playing the current soundtrack as a preview
	 */
	@FXML
	void previewTrack() {
		MediaHandler.stop();
		MediaHandler.play(MainApp.instance().getUser().getCurrentSoundtrack());
	}

	/**
	 * Change the volume of the soundtrack (with slider)
	 */
	@FXML
	void setVolume() {
		double volValue = soundtrackVolumeBar.getValue();
		MediaHandler.setVolume(volValue);
	}

	/**
	 * Set up the buttons that should be disabled when TTS is active
	 * as well as the listener so that the volume is changed when the slider is moved
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// add all the controls to be disabled when TTS is active
		allControls.add(aboutBtn);
		allControls.add(previewBtn);
		allControls.add(previewTrackBtn);
		allControls.add(voiceComboBox);
		allControls.add(resetBtn);
		allControls.add(soundtrackVolumeBar);
		allControls.add(soundtrackComboBox);
		allControls.add(backBtn);
		soundtrackVolumeBar.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				MediaHandler.setVolume(newValue.doubleValue()); //changing slider value should change volume
			}
		});

	}

}
