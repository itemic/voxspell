package se206_voxspell;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class HomeMenuController {

    @FXML
    private Button playBtn;

    @FXML
    private Button statsBtn;

    @FXML
    private Button settingsBtn;
    
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

    }

    @FXML
    void goToSettings(ActionEvent event) {

    }

}
