package se206_voxspell;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import se206_model.UserModel;

public class StatusHUDController implements Initializable{

    @FXML
    private ProgressBar levelProgressBar;

    @FXML
    private Label levelLabel;
    
    @FXML
    private Tooltip xpToolTip;
    
    @FXML
    private Label usernameLabel;

    public void update() {
    	UserModel user = MainApp.instance().getUser();
    	levelProgressBar.setProgress(user.levelProgress());
    	levelLabel.setText("Level: " + user.getLevel());
    	usernameLabel.setText(user.toString());
    	xpToolTip.setText(user.getXP() + "/" + user.getNextXP());
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		update();
	}
}
