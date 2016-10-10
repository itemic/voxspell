package se206_voxspell;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import se206_model.UserModel;
import se206_util.MediaHandler;

public class StatusHUDController implements Initializable{

    @FXML
    private ProgressBar levelProgressBar;

    @FXML
    private Label levelLabel;
    
    @FXML
    private Tooltip xpToolTip;
    
    @FXML
    private Label usernameLabel;
	private MediaPlayer _mp = MainApp.instance().getMediaPlayer();

    public void update() {
    	UserModel user = MainApp.instance().getUser();
    	levelProgressBar.setProgress(user.levelProgress());
    	levelLabel.setText("Level: " + user.getLevel());
    	usernameLabel.setText(user.toString());
    	xpToolTip.setText(user.getXP() + "/" + user.getNextXP());
    }

    public void update(boolean didLevelUp) {
    	this.update();
    	if (didLevelUp) {
    		MediaHandler.play("resources/Cool Intro.mp3");
    	}
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		update();
	}
}
