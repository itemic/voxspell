package se206_voxspell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import se206_util.MediaHandler;
import se206_util.Save;

public class HomeMenuController {

    @FXML
    private Button playBtn;

    @FXML
    private Button statsBtn;

    @FXML
    private Button settingsBtn;
    
    @FXML
    private Button switchUserBtn;

    @FXML
    void backToUserSelection(ActionEvent event) {
    	try {
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
    }
    
    @FXML
    void startGame(ActionEvent event) {
    	try {
    		
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("LevelSelectMenu.fxml"));
    		BorderPane levelSelectPane = (BorderPane)loader.load();
    		LevelSelectMenuController controller = loader.<LevelSelectMenuController>getController();
    		controller.fromMenu(); //code to initialize level select
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(levelSelectPane);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void goToStats(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("StatisticsMenu.fxml"));
    		BorderPane stats = (BorderPane)loader.load();
    		StatisticsMenuController controller = loader.<StatisticsMenuController>getController();
    		controller.fromMenu(); //code to initialize 
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(stats);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void goToSettings(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("SettingsMenu.fxml"));
    		BorderPane settings = (BorderPane)loader.load();
    		SettingsMenuController controller = loader.<SettingsMenuController>getController();
    		controller.setUpSettings(); //code to initialize 
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(settings);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

}
