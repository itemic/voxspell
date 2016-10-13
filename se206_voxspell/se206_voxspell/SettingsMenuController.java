package se206_voxspell;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import se206_util.Save;
import se206_util.TextToSpeech;

public class SettingsMenuController implements Initializable {
	
	private ObservableList<String> obVoices = FXCollections.observableArrayList(TextToSpeech.access().voices());
	private ObservableList<String> obSounds;
	
    @FXML
    private ComboBox<String> voiceComboBox;

    @FXML
    private Button previewBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private ComboBox<String> soundtrackComboBox;

    @FXML
    private Button backBtn;
    
    private Alert alert = new Alert(AlertType.CONFIRMATION);

    @FXML
    void backToMenu(ActionEvent event) {
    	MainApp.instance().save();
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
    		BorderPane levelSelectPane = (BorderPane)loader.load();
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(levelSelectPane);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void previewVoice(ActionEvent event) {
    	TextToSpeech.access().speak("Hi, my name is " + TextToSpeech.access().selectedVoice());
    }

    @FXML
    void resetGame(ActionEvent event) {
    	alert.setTitle("Delete user");
    	alert.setHeaderText("Are you sure you want to delete user?");
    	alert.setContentText("The user " + MainApp.instance().getUser().toString() + " will be deleted, along with all its statistics. There's no going back.");
    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK) {
    		try {
    		Files.deleteIfExists(Paths.get(Save.DIRECTORY + MainApp.instance().getUser().toString() + Save.EXTENSION));
    		FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("UserSelectionMenu.fxml"));
			BorderPane profiles = (BorderPane) loader.load();
			UserSelectionMenuController controller = loader.<UserSelectionMenuController>getController();
    		controller.setup();
			MainApp.getRoot().setCenter(profiles);
			MainApp.getRoot().setTop(null);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	} else {
    	}
    }
    
    public void setUpSettings() {
    	ArrayList<String> availMusic = new ArrayList<>();
    	availMusic.add("None");
    	for (int i = 0; i < MainApp.instance().getUser().getMusicList().size(); i++) {
    		if (MainApp.instance().getUser().getCanPlay(i)) {
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
    public void changeTrack() {
    	MainApp.instance().getUser().setCurrentSoundtrack(soundtrackComboBox.getSelectionModel().getSelectedItem());
    }
    public void changeVoice() {
    	TextToSpeech.access().chooseVoice(voiceComboBox.getSelectionModel().getSelectedIndex());
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
