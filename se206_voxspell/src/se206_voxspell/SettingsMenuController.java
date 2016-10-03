package se206_voxspell;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import se206_util.TextToSpeech;

public class SettingsMenuController {
	
	private ObservableList<String> ob = FXCollections.observableArrayList(TextToSpeech.access().voices());
	
    @FXML
    private ComboBox<String> voiceComboBox;

    @FXML
    private Button previewBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button backBtn;

    @FXML
    void backToMenu(ActionEvent event) {
    	MainApp.instance().saveUser();
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
    	System.out.println("RESET!"); //tb implemented
    }
    
    public void setUpSettings() {
    	voiceComboBox.getItems().addAll(ob);
    	voiceComboBox.getSelectionModel().select(TextToSpeech.access().selectedVoiceNum());
    }
    
    public void changeVoice() {
    	TextToSpeech.access().chooseVoice(voiceComboBox.getSelectionModel().getSelectedIndex());
    }

}
