package se206_voxspell;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import se206_model.UserModel;
import se206_util.MediaHandler;

public class StatusHUDController implements Initializable{


    @FXML
    private Label levelLabel;
    
    
    @FXML
    private Label usernameLabel;
	private MediaPlayer _mp = MainApp.instance().getMediaPlayer();

    public void update() {
    	UserModel user = MainApp.instance().getUser();
    	levelLabel.setText("Credits: " + user.getCurrency());
    	usernameLabel.setText(user.toString());
    }

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		update();
	}
}
