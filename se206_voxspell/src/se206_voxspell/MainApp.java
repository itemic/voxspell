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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se206_model.UserModel;
import se206_util.SaveGame;
import se206_util.SaveHelper;

public class MainApp extends Application {
	private static BorderPane _root = new BorderPane();
	private static MainApp _instance;
	private Stage _primaryStage;
	private UserModel _user;
	private StatusHUDController _hud;
	private SaveGame _save;
	private ArrayList<File> _profiles = SaveHelper.getInstance().findFiles();

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

	public boolean loadUser() {
		_save = new SaveGame();
		try {
			_user = _save.load();
			return true;
		} catch (FileNotFoundException e) {
			//resetUser();
			return false;
		}
	}
	
	public boolean loadUser(SaveGame save) {
		_save = save;
		try {
			_user = _save.load();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	public void saveUser() {
		_save.save();
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
	
	public void setSave(SaveGame sg) {
		_save = sg;
	}
	
	public void setLayout() {
		try {
			
			if (loadUser() && _user != null) {
				System.out.println("bheoahge");
			} else {
				_user = new UserModel("tkro003");
				_save = new SaveGame(_user);
			}
			System.out.print("called");
			saveUser();
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
