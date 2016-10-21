package voxspell.gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import voxspell.model.GameType;
import voxspell.model.QuizModel;
import voxspell.util.MediaHandler;

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
    private ImageView completeImage;
    
    @FXML
    private ImageView gainImage;
    
    @FXML
    private ImageView scoreImage;
    
    @FXML
    private VBox creditVbox;

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
    
    
    public void initQuiz(QuizModel quiz) {
    	if (MainApp.instance().getUser().getGameType().equals(GameType.FREEPLAY)) {
    		//no experience in this mode so hide the credits gained
    		creditVbox.setManaged(false);
    	}
    	
    	xpLabel.setText("β$" + quiz.getCurrencyGain());
    	scoreLabel.setText(quiz.getCorrect() + "/" + quiz.getQuizSize());
    	
    	Image image = new Image("resources/levelcomplete.png");
    	Image plus = new Image("resources/voxcoingain.png");
    	Image score;
    	//Shows a different icon based on the user's performance
    	double quizPercentage = (double)quiz.getCorrect() / (double)quiz.getQuizSize();
    	if (quizPercentage >= 0.8) {
    		score = new Image("resources/goodscore.png");
    	} else if (quizPercentage >= 0.6) {
    		score = new Image("resources/decentscore.png");
    	} else {
    		score = new Image("resources/badscore.png");
    	}
    	completeImage.setImage(image);
    	gainImage.setImage(plus);
    	scoreImage.setImage(score);
    }


}

