package se206_voxspell;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import se206_model.GameType;
import se206_model.QuizModel;
import se206_util.MediaHandler;

public class LevelCompleteMenuController {

    @FXML
    private Label scoreLabel;

    @FXML
    private Label xpLabel;

    @FXML
    private Label rewardLabel;

    @FXML
    private Button rewardBtn;

    @FXML
    private Button menuBtn;
    
    @FXML
    private Label xpTitleLabel;
    

    @FXML
    void backToMenu(ActionEvent event) {
    	try {
    		MainApp.instance().save();
    		MediaHandler.stop();
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
    		BorderPane levelSelectPane = (BorderPane)loader.load();
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(levelSelectPane);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    
    public void init(QuizModel quiz) {
    	if (MainApp.instance().getUser().getGameType().equals(GameType.FREEPLAY)) {
    		//no experience in this mode
    		xpTitleLabel.setManaged(false);
    		xpLabel.setManaged(false);
    	}
    	
    	xpLabel.setText("β$" + quiz.getCurrencyGain());
    	scoreLabel.setText(quiz.getCorrect() + "/" + quiz.getQuizSize());
    }

    @FXML
    void showReward(ActionEvent event) {

    }

}

