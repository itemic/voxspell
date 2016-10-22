package voxspell.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import voxspell.model.GameType;
import voxspell.model.UserModel;
import voxspell.util.Save;

public class MainApp extends Application {
	private static BorderPane _root = new BorderPane();
	private static MainApp _instance;
	private Stage _primaryStage;
	private UserModel _user;
	private Save _s;
	
	// Reference to this application for the other classes to use
	public static MainApp instance() {
		return _instance;
	}
	
	//////////// FILE HANDLING (SAVE/LOAD) ////////////
	public void save() {
		_s.save();
	}
	
	public void save(Save s) {
		_s = s;
		save();
	}
	
	
	public boolean load(Save s){
		_s = s;
		try {
			_user = _s.load();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Code to set up the actual game after the player loads a user
	 */
	public void startApp() {
		try {
			FXMLLoader loader = new FXMLLoader();			
			loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
			BorderPane menu = (BorderPane) loader.load();
			loader = new FXMLLoader();
			_root.setCenter(menu);
    		if (getUser().getGameType().equals(GameType.CHALLENGE)) { //sets title bar based on game mode
    			this._primaryStage.setTitle("VOXSpell 1.0 - " + getUser().toString() + " (Challenge)");
    		} else {
    			this._primaryStage.setTitle("VOXSpell 1.0 - " + getUser().toString() + " (Free Play)");
    		}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets up the whole application to the profile selection screen
	 */
	public void start(Stage primaryStage) throws Exception {
		_instance = this;
		this._primaryStage = primaryStage;
		this._primaryStage.setTitle("VOXSpell 1.0");
		profileSelect();
		displayLayout();
	}
	
	/**
	 * Allows other classes to change the title bar text
	 * @param str The string to set it to
	 */
	public void changeWindowTitle(String str) {
		this._primaryStage.setTitle(str);
	}

	/**
	 * Code to set up the controller for the profile selection screen.
	 */
	public void profileSelect() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("UserSelectionMenu.fxml"));
			BorderPane profiles = (BorderPane) loader.load();
			UserSelectionMenuController controller = loader.<UserSelectionMenuController>getController();
    		controller.initProfile();
			_root.setCenter(profiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Sets up the stylesheets and the stage of the application
     */
	public void displayLayout() {
		Scene scene = new Scene(_root, 800, 600);
		scene.getStylesheets().add(getClass().getResource("voxstyle.css").toExternalForm());
		scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Righteous");
		scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto:400,700");
		_primaryStage.setScene(scene);
		_primaryStage.initStyle(StageStyle.DECORATED);
		_primaryStage.setResizable(false);
		_primaryStage.sizeToScene(); // prevents border from setResizable
		_primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	////////// GETTER METHODS //////////
	public static BorderPane getRoot() {
		return _root;
	}

	public UserModel getUser() {
		return _user;
	}
	
	public Stage getStage() {
		return _primaryStage;
	}
}
