package se206_voxspell;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import se206_model.QuizModel;

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
    void backToMenu(ActionEvent event) {
    	try {
    		MainApp.instance().save();
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
    	xpLabel.setText("+" + quiz.xpEarnedThisQuiz() + " XP");
    	scoreLabel.setText(quiz.getCorrect() + "/" + quiz.getQuizSize());
    }

    @FXML
    void showReward(ActionEvent event) {

    }

}

