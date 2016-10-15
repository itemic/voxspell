package se206_voxspell;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import se206_model.LevelModel;
import se206_util.MediaHandler;

public class LevelSelectMenuController {

    @FXML
    private ComboBox<String> levelSelectComboBox;

    @FXML
    private Button statsBtn;

    @FXML
    private Button playBtn;

    @FXML
    private Label noLevelsLabel;
    
    @FXML
    private Button backBtn;
    
    private ObservableList<String> _ob;
    private ArrayList<String> _levels = new ArrayList<>();
    private ArrayList<LevelModel> _lvModel = new ArrayList<>();

    @FXML
    void goAndPlay(ActionEvent event) {
    	LevelModel selectedLevel = _lvModel.get(levelSelectComboBox.getSelectionModel().getSelectedIndex());
    	try {
    		if (!MainApp.instance().getUser().getDisplaySoundtrack().equals("None")) {
    			MediaHandler.play(MainApp.instance().getUser().getCurrentSoundtrack());
    		}
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("GameMenu.fxml"));
    		BorderPane gamePane = (BorderPane)loader.load();
    		GameMenuController controller = loader.<GameMenuController>getController();
    		controller.loadGame(selectedLevel);
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(gamePane);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void returnToMenu(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
    		BorderPane back = (BorderPane)loader.load();
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(back);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void showLevelStats(ActionEvent event) {

    }

    @FXML
    void updateLevel(ActionEvent event) {

    }
    
    void fromMenu() {
    	
    	for (LevelModel level: MainApp.instance().getUser().getGame().getLevels()) {
    		if (level.canPlayLevel() ){
    			addToComboBox(level);
    		}
    	}
    	if (_levels.isEmpty()) {
    		playBtn.setVisible(false);
    		playBtn.setManaged(false);
    		levelSelectComboBox.setVisible(false);
    		levelSelectComboBox.setManaged(false);
    	} else {
    		noLevelsLabel.setVisible(false);
    		noLevelsLabel.setManaged(false);
    	_ob = FXCollections.observableArrayList(_levels);
    	levelSelectComboBox.getItems().addAll(_ob);
    	levelSelectComboBox.getSelectionModel().select(0);
    	}
    }

    private void addToComboBox(LevelModel level) {
    	//helper method for fromMenu
		_levels.add(level.getName());
		_lvModel.add(level);
    }


}
