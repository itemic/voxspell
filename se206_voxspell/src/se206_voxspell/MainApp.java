package se206_voxspell;

import java.io.FileNotFoundException;
import java.io.IOException;

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

public class MainApp extends Application {
	private static BorderPane _root = new BorderPane();
	private static MainApp _instance;
	private Stage _primaryStage;
//	private GameModel _game;
	private UserModel _user;
	private StatusHUDController _hud;
	private SaveGame _save;
	
//	public GameModel getGame() {
//		return _game;
//	}
	
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
	
	public void saveUser() {
		_save.save();
	}
	
	
	
	public void start(Stage primaryStage) throws Exception {
		_instance = this;
		this._primaryStage = primaryStage;
		this._primaryStage.setTitle("VOXSpell β");
		setLayout();
	}
	
	public static BorderPane getRoot() {
		return _root;
	}
	
	public void setLayout() {
		try {
			
			if (loadUser() && _user != null) {
			} else {
				_user = new UserModel("tkro003");
				_save = new SaveGame(_user);
			}
			
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
			Scene scene = new Scene(_root, 800, 600);
			scene.getStylesheets().add(getClass().getResource("voxstyle.css").toExternalForm());
			_primaryStage.setScene(scene);
			_primaryStage.initStyle(StageStyle.UNIFIED);
			_primaryStage.setResizable(false);
			_primaryStage.sizeToScene(); // prevents border from setResizable
			_primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
