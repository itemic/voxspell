package voxspell.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import voxspell.model.GameType;
import voxspell.model.UserModel;
import voxspell.model.WordModel;
import voxspell.util.FileHandler;
import voxspell.util.Save;

public class MainApp extends Application {
	private static BorderPane _root = new BorderPane();
	private static MainApp _instance;
	private Stage _primaryStage;
	private UserModel _user;
	private StatusHUDController _hud;
	private Save _s;
	private MediaPlayer _media;
	
	
	public MediaPlayer getMediaPlayer() {
		return _media;
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
		this._primaryStage.setTitle("VOXSpell 1.0");
		profileSelect();
		displayLayout();
	}
	
	public void changeWindowTitle(String str) {
		this._primaryStage.setTitle(str);
	}
	public static BorderPane getRoot() {
		return _root;
	}
	
	public Stage getStage() {
		return _primaryStage;
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
//			loader.setLocation(MainApp.class.getResource("StatusHUD.fxml"));
//			BorderPane status = (BorderPane)loader.load();
			_root.setCenter(menu);
//			_root.setTop(status);
//    		_hud = loader.<StatusHUDController>getController();
    		if (getUser().getGameType().equals(GameType.CHALLENGE)) {
    			this._primaryStage.setTitle("VOXSpell 1.0 - " + getUser().toString() + " (Challenge)");
    		} else {
    			this._primaryStage.setTitle("VOXSpell 1.0 - " + getUser().toString() + " (Free Play)");
    		}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    private static double xOffset = 0;
    private static double yOffset = 0;
    public void testCode() {
    	//http://stackoverflow.com/questions/18173956/how-to-drag-undecorated-window //offset too
    	
		_root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	xOffset = _primaryStage.getX() - event.getScreenX();
                yOffset = _primaryStage.getY() - event.getScreenY();
            }
        });
        _root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	_primaryStage.setX(event.getScreenX() + xOffset);
                _primaryStage.setY(event.getScreenY() + yOffset);
            }
        });
	}
	public void displayLayout() {
		Scene scene = new Scene(_root, 800, 600);
		scene.getStylesheets().add(getClass().getResource("voxstyle.css").toExternalForm());
		scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Righteous");
		scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto:400,700");
		testCode();
		_primaryStage.setScene(scene);
		_primaryStage.initStyle(StageStyle.UNDECORATED);
		_primaryStage.setResizable(false);
		_primaryStage.sizeToScene(); // prevents border from setResizable
		_primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}