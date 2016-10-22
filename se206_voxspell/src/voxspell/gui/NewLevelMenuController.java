package voxspell.gui;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class NewLevelMenuController {

	@FXML
	private Button cancelBtn;

	@FXML
	private Button addLevelButton;

	@FXML
	private TextField levelNameField;

	@FXML
	private TextArea levelWordsArea;

	private Alert noName = new Alert(AlertType.WARNING);
	private Alert noWords = new Alert(AlertType.WARNING);

	@FXML // grabs all the text and makes a level out of it
	void addLevelConfirm(ActionEvent event) {

		String levelName = levelNameField.getText();
		// http://stackoverflow.com/questions/14632071/textarea-is-it-possible-to-get-the-number-of-lines
		String allText = levelWordsArea.getText();
		String[] words = allText.split(System.getProperty("line.separator"));
		ArrayList<String> wordlist = new ArrayList<>();
		for (String word: words) {
			if (!word.trim().equals("")) { //don't want empty words
				wordlist.add(word.trim());
			}
		}
		if (levelName.equals("") || allText.trim().isEmpty()) {
			if (levelName.equals("")) {
				// no level name
				noName.setTitle("Level Name Empty");
				noName.setHeaderText("Your level name is empty.");
				noName.setContentText("Add a name to your level.");
				noName.showAndWait();
			} else {
				// the user didn't provide any words
				noWords.setTitle("Word List Empty");
				noWords.setHeaderText("There are no words in this level.");
				noWords.setContentText("Add some words to this level.");
				noWords.showAndWait();
			}
		} else {
			MainApp.instance().getUser().getGame().addSingleLevel(levelName, wordlist);
			backToEditor();
		}
	}

	@FXML
	void cancel(ActionEvent event) {
		backToEditor();
	}

	void backToEditor() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("LevelEditor.fxml"));
			BorderPane editor = (BorderPane) loader.load();
			LevelEditorController controller = loader.<LevelEditorController>getController();
			controller.initEditor(); // code to initialize

			BorderPane root = MainApp.getRoot();
			root.setCenter(editor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
