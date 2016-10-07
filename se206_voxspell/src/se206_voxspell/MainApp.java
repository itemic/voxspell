package se206_voxspell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se206_model.UserModel;
import se206_util.Save;
import se206_util.FileHandler;

public class MainApp extends Application {
	private static BorderPane _root = new BorderPane();
	private static MainApp _instance;
	private Stage _primaryStage;
	private UserModel _user;
	private StatusHUDController _hud;
	private ArrayList<File> _profiles = FileHandler.getInstance().findProfiles();
	private Save _s;
	private MediaPlayer _media;
	
	public MediaPlayer getMediaPlayer() {
		return _media;
	}
	
	public ArrayList<File> getProfiles() {
		return _profiles;
	}
	
	
	
	
	public StatusHUDController getHUD() {
		return _hud;
	}
	public UserModel getUser() {
		return _user;
	}
	
	public static MainApp instance() {
		return _instance;
	}
	
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
	
	
	public void start(Stage primaryStage) throws Exception {
		_instance = this;
		this._primaryStage = primaryStage;
		this._primaryStage.setTitle("VOXSpell β");
		profileSelect();
		displayLayout();
	}
	
	public static BorderPane getRoot() {
		return _root;
	}
	
	public void profileSelect() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("UserSelectionMenu.fxml"));
			BorderPane profiles = (BorderPane) loader.load();
			UserSelectionMenuController controller = loader.<UserSelectionMenuController>getController();
    		controller.setup();
			_root.setCenter(profiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void startApp() {
		try {
			FXMLLoader loader = new FXMLLoader();			
			loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
			BorderPane menu = (BorderPane) loader.load();
			loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("StatusHUD.fxml"));
			BorderPane status = (BorderPane)loader.load();
			_root.setCenter(menu);
			_root.setTop(status);
    		_hud = loader.<StatusHUDController>getController();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void displayLayout() {
		Scene scene = new Scene(_root, 800, 600);
		scene.getStylesheets().add(getClass().getResource("voxstyle.css").toExternalForm());
		_primaryStage.setScene(scene);
		_primaryStage.initStyle(StageStyle.UNIFIED);
		_primaryStage.setResizable(false);
		_primaryStage.sizeToScene(); // prevents border from setResizable
		_primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
