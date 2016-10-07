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
import javafx.scene.layout.BorderPane;
import se206_model.LevelModel;

public class LevelSelectMenuController {

    @FXML
    private ComboBox<String> levelSelectComboBox;

    @FXML
    private Button statsBtn;

    @FXML
    private Button playBtn;

    @FXML
    private Button backBtn;
    
    private ObservableList<String> _ob;
    private ArrayList<String> _levels = new ArrayList<>();
    private ArrayList<LevelModel> _lvModel = new ArrayList<>();

    @FXML
    void goAndPlay(ActionEvent event) {
    	LevelModel selectedLevel = _lvModel.get(levelSelectComboBox.getSelectionModel().getSelectedIndex());
    	try {
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
    		BorderPane levelSelectPane = (BorderPane)loader.load();
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(levelSelectPane);
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
    	boolean startingLevels = true;
    	for (LevelModel level: MainApp.instance().getUser().getGame().getLevels()) {
    		//to ensure that there is at least a level to start with:
    		if (startingLevels) {
    			_levels.add(level.getName());
        		_lvModel.add(level);
        		startingLevels = false;
    		}
    		else if (MainApp.instance().getUser().getLevel() >= level.getLevelRequirement()){
        		_levels.add(level.getName());
        		_lvModel.add(level);
    		}
    	}
    	_ob = FXCollections.observableArrayList(_levels);
    	levelSelectComboBox.getItems().addAll(_ob);
    	levelSelectComboBox.getSelectionModel().select(0);
    	
    }



}
