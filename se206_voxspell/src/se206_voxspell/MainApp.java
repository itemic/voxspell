package se206_voxspell;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {
	private static BorderPane _root = new BorderPane();
	private static MainApp _instance;
	private Stage _primaryStage;
//	private GameModel _game;
	private UserModel _user;
	private StatusHUDController _hud;
	
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

	
	public void start(Stage primaryStage) throws Exception {
		_instance = this;
		_user = new UserModel("tkro003");
		this._primaryStage = primaryStage;
		this._primaryStage.setTitle("VOXSpell β");
		setLayout();
	}
	
	public static BorderPane getRoot() {
		return _root;
	}
	
	public void setLayout() {
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
