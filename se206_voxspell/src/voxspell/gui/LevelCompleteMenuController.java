package voxspell.gui;

import java.io.File;
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
import voxspell.model.LevelModel;
import voxspell.model.QuizModel;
import voxspell.util.MediaHandler;

/**
 * Controller class to show the Level Complete screen
 * @author terran
 *
 */
public class LevelCompleteMenuController {

    @FXML
    private Label scoreLabel;

    @FXML
    private Label xpLabel;

    @FXML
    private Label rewardLabel;


    @FXML
    private Button menuBtn;
    
    @FXML
    private Button replayLevelBtn;
    
    @FXML
    private Button newGameBtn;

    @FXML
    private ImageView completeImage;
    
    @FXML
    private ImageView gainImage;
    
    @FXML
    private ImageView scoreImage;
    
    @FXML
    private VBox creditVbox;
    
    private LevelModel currentLevel;

    /**
     * Button action to go back to menu
     * @param event
     */
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
    
    /**
     * Button action to start a new game by going to level select
     */
    @FXML
    void playNewGame() {
    	try { //same code as HomeMenu new game (same functionality too)
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("LevelSelectMenu.fxml"));
    		BorderPane levelSelectPane = (BorderPane)loader.load();
    		LevelSelectMenuController controller = loader.<LevelSelectMenuController>getController();
    		controller.fromMenu(); //code to initialize level select

    		BorderPane root = MainApp.getRoot();
    		root.setCenter(levelSelectPane);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Button action to replay the level
     */
    @FXML
    void replayLevel() {
		try {
			if (!MainApp.instance().getUser().getDisplaySoundtrack().equals("None")) {
				// if there is a soundtrack selected, start playing it
				MediaHandler.play(MainApp.instance().getUser().getCurrentSoundtrack());
			}
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("GameMenu.fxml"));
			BorderPane gamePane = (BorderPane) loader.load();
			GameMenuController controller = loader.<GameMenuController>getController();
			controller.initGame(currentLevel);

			BorderPane root = MainApp.getRoot();
			root.setCenter(gamePane);
		} catch (IOException e) {
			e.printStackTrace();
		}    	
    }
    
    /**
     * Code that sets up several parts of the screen
     * by disabling the VOXCredit gain if in FreePlay, and showing the 
     * score image
     * @param quiz
     */
    public void initComplete(QuizModel quiz) {
    	currentLevel = quiz.getLevel();
    	if (MainApp.instance().getUser().getGameType().equals(GameType.FREEPLAY)) {
    		//no experience in this mode so hide the credits gained
    		creditVbox.setManaged(false);
    		creditVbox.setVisible(false);
    	}
    	
    	xpLabel.setText("β$" + quiz.getCurrencyGain());
    	scoreLabel.setText(quiz.getCorrect() + "/" + quiz.getQuizSize());

    	Image image = new Image(new File("resources/levelcomplete.png").toURI().toString());
    	Image plus = new Image(new File("resources/voxcoingain.png").toURI().toString());
    	Image score;
    	//Shows a different icon based on the user's performance
    	double quizPercentage = (double)quiz.getCorrect() / (double)quiz.getQuizSize();
    	if (quizPercentage >= 0.8) {
    		score = new Image(new File("resources/goodscore.png").toURI().toString());
    	} else if (quizPercentage >= 0.6) {
    		score = new Image(new File("resources/decentscore.png").toURI().toString());
    	} else {
    		score = new Image(new File("resources/badscore.png").toURI().toString());
    	}
    	completeImage.setImage(image);
    	gainImage.setImage(plus);
    	scoreImage.setImage(score);
    }


}

