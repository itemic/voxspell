package voxspell.gui;

import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import voxspell.model.GameType;
import voxspell.model.LevelModel;
import voxspell.util.MediaHandler;

/**
 * Controller class for the level select screen
 * @author terran
 *
 */
public class LevelSelectMenuController {

	@FXML
	private ComboBox<String> levelSelectComboBox;

	@FXML
	private Button statsBtn;

	@FXML
	private Button playBtn;

	@FXML
	private Label noLevelsLabel;

	@FXML
	private Button backBtn;

	private ObservableList<String> _ob;
	private ArrayList<String> _levels = new ArrayList<>();
	private ArrayList<LevelModel> _lvModel = new ArrayList<>();

	/**
	 * Button action to start a new Quiz
	 * @param event
	 */
	@FXML
	void goAndPlay(ActionEvent event) {
		LevelModel selectedLevel = _lvModel.get(levelSelectComboBox.getSelectionModel().getSelectedIndex());
		try {
			if (!MainApp.instance().getUser().getDisplaySoundtrack().equals("None")) {
				// if there is a soundtrack selected, start playing it
				MediaHandler.play(MainApp.instance().getUser().getCurrentSoundtrack());
			}
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("GameMenu.fxml"));
			BorderPane gamePane = (BorderPane) loader.load();
			GameMenuController controller = loader.<GameMenuController>getController();
			controller.initGame(selectedLevel);

			BorderPane root = MainApp.getRoot();
			root.setCenter(gamePane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Button action to go back to the menu
	 * @param event
	 */
	@FXML
	void returnToMenu(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
			BorderPane back = (BorderPane) loader.load();

			BorderPane root = MainApp.getRoot();
			root.setCenter(back);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Code to initialize several things when this menu is displayed, by
	 * populating the boxes
	 */
	void fromMenu() {

		for (LevelModel level : MainApp.instance().getUser().getGame().getLevels()) {
			if (level.canPlayLevel() || MainApp.instance().getUser().getGameType().equals(GameType.FREEPLAY)) {
				_levels.add(level.toString());
				_lvModel.add(level); //add level if in free play or unlocked in challenge
			}
		}
		if (_levels.isEmpty()) { //prompt the user to buy levels if there are none available
			playBtn.setVisible(false);
			playBtn.setManaged(false);
			levelSelectComboBox.setVisible(false);
			levelSelectComboBox.setManaged(false);
		} else { //populate the combobox
			noLevelsLabel.setVisible(false);
			noLevelsLabel.setManaged(false);
			_ob = FXCollections.observableArrayList(_levels);
			levelSelectComboBox.getItems().addAll(_ob);
			levelSelectComboBox.getSelectionModel().select(0);
		}
	}

}
