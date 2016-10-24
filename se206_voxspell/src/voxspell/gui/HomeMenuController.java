package voxspell.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import voxspell.model.GameType;
import voxspell.model.LevelModel;
import voxspell.util.MediaHandler;

/**
 * Controller class for the MainMenu
 * @author terran
 *
 */
public class HomeMenuController implements Initializable {

    @FXML
    private Button playBtn;

    @FXML
    private Button statsBtn;

    @FXML
    private Button settingsBtn;
    
    @FXML
    private Button switchUserBtn;
    
    @FXML
    private Button editorBtn;

    @FXML
    private Button shopBtn;
    
    
    /**
     * Button action for going back to the user selection screen
     * @param event
     */
    @FXML
    void backToUserSelection(ActionEvent event) { //switch users
    	try {
    		FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("UserSelectionMenu.fxml"));
			BorderPane profiles = (BorderPane) loader.load();
			UserSelectionMenuController controller = loader.<UserSelectionMenuController>getController();
    		controller.initProfile();
			MainApp.getRoot().setCenter(profiles);
			MainApp.instance().changeWindowTitle("VOXSpell 1.0");
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    }
    
    /**
     * Button action to start a game to show the level selection menu
     * @param event
     */
    @FXML
    void startGame(ActionEvent event) { //start a game, show the level selection menu
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
    
    /**
     * Button action to load the statistics screen
     * @param event
     */
    @FXML
    void goToStats(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("StatisticsMenu.fxml"));
    		BorderPane stats = (BorderPane)loader.load();
    		StatisticsMenuController controller = loader.<StatisticsMenuController>getController();
    		controller.initStats(); //code to initialize statistics
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(stats);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Button action to load the settings screen
     * @param event
     */
    @FXML
    void goToSettings(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("SettingsMenu.fxml"));
    		BorderPane settings = (BorderPane)loader.load();
    		SettingsMenuController controller = loader.<SettingsMenuController>getController();
    		controller.initSettings(); //code to initialize settings
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(settings);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Button action to load the editor screen
     * @param event
     */
    @FXML
    void goToEditor(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("LevelEditor.fxml"));
    		BorderPane editor = (BorderPane)loader.load();
    		LevelEditorController controller = loader.<LevelEditorController>getController();
    		controller.initEditor(); //code to initialize level editor 
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(editor);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Button action to load the store
     * @param event
     */
    @FXML
    void goToShop(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("ShopMenu.fxml"));
    		BorderPane shop = (BorderPane)loader.load();
    		ShopMenuController controller = loader.<ShopMenuController>getController();
    		controller.initShop(); //code to initialize the shop
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(shop);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Method that automatically runs when this screen is initialized
     * Detects which game mode the user is in and disables the other button
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String user = MainApp.instance().getUser().toString();
		if (MainApp.instance().getUser().getGameType().equals(GameType.CHALLENGE)) {
			// no editor in Challenge mode
			editorBtn.setManaged(false);
			MainApp.instance().changeWindowTitle("VOXSpell 1.0 - " + user + " (Challenge)");

		} else {
			// no shop in Free Play mode
			shopBtn.setManaged(false);
			MainApp.instance().changeWindowTitle("VOXSpell 1.0 - " + user + " (Free Play)");

		}
		
		//Disables the "Play" if there are no levels available
		boolean canPlay = false;
		for (LevelModel level: MainApp.instance().getUser().getGame().getLevels()) {
			if (level.canPlayLevel() || MainApp.instance().getUser().getGameType().equals(GameType.FREEPLAY)) {
				canPlay = true;
				break;
			}
		}
		
		if (!canPlay) {
			playBtn.setDisable(true);
			playBtn.setText("No Levels (Add some or visit VOXStore)");
			shopBtn.requestFocus();
		} else {
			playBtn.setDisable(false);
			playBtn.setText("New Game");
			playBtn.requestFocus();
		}
		

		
		MainApp.instance().save(); //always autosave at home menu
		MediaHandler.stop(); //disable all playing music
	}

}
