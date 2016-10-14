package se206_voxspell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import se206_model.LevelModel;
import se206_model.WordModel;

public class LevelEditorController {

    @FXML
    private ComboBox<LevelModel> levelCombo;

    @FXML
    private ListView<WordModel> wordListView;
    
    @FXML
    private Button backBtn;
    
    @FXML
    private Button addWordBtn;

    @FXML
    private Button removeWordBtn;

    @FXML
    private Button addLevelBtn;

    @FXML
    private Button removeLevelBtn;

    @FXML
    private Button importWordListBtn;

    //dialogs and alerts from http://code.makery.ch/blog/javafx-dialogs-official/
    private Alert atLeastOneWarning = new Alert(AlertType.WARNING);
    private TextInputDialog addWordDialog = new TextInputDialog("");

	private FileChooser chooser = new FileChooser();
    
    private ObservableList<LevelModel> _ob;
    private ArrayList<LevelModel> _lvModel = new ArrayList<>();
    
    void fromMenu() {
    	wordListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	for (LevelModel level: MainApp.instance().getUser().getGame().getLevels()) {
    		_lvModel.add(level);
    	}
    	
    	_ob = FXCollections.observableArrayList(_lvModel);
    	levelCombo.getItems().addAll(_ob);
    	levelCombo.getSelectionModel().select(0);
    	updateList();
    }
    
    @FXML
    void updateList() {
    	wordListView.getItems().clear();
    	LevelModel selectedLevel = levelCombo.getSelectionModel().getSelectedItem();
//// UPDATE
    	for (LevelModel level: MainApp.instance().getUser().getGame().getLevels()) {
    		_lvModel.add(level);
    	}
    	
    	_ob = FXCollections.observableArrayList(_lvModel);
    	for (LevelModel l: _ob) {
    		if (!levelCombo.getItems().contains(l)) {
    			levelCombo.getItems().add(l);
    		}
    	}
    	
//// UPDATE
    	for (WordModel w: selectedLevel.getWords()){
    		wordListView.getItems().add(w);
    	}
    	wordListView.scrollTo(0);
    }
    
    @FXML
    void backToMenu(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
    		BorderPane levelSelectPane = (BorderPane)loader.load();
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(levelSelectPane);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void deleteWord() {
    	LevelModel selectedLevel = levelCombo.getSelectionModel().getSelectedItem();
		WordModel w = wordListView.getSelectionModel().getSelectedItem();
		if (w == null) {
			//nothing selected; do nothing
		} else {
			if(selectedLevel.getWords().size() == 1) {
	    		//at least 1 word needed
	    		atLeastOneWarning.setTitle("Can't Delete");
	    		atLeastOneWarning.setHeaderText("Cannot delete the word " + w.toString() + ".");
	    		atLeastOneWarning.setContentText("You need to have at least one word in a level.");
	    		atLeastOneWarning.showAndWait();
	    		
	    	} else {

	    		selectedLevel.removeWord(w);
	    		wordListView.getItems().remove(w);
	    		updateList();
	    	}
		}
    
	
    }
    
    @FXML
    void addWord() {
    	LevelModel selectedLevel = levelCombo.getSelectionModel().getSelectedItem();
    	addWordDialog.getEditor().clear();
    	addWordDialog.setTitle("Add New Word");
    	addWordDialog.setHeaderText("Add a word to " + selectedLevel.getName());
    	addWordDialog.setContentText("Word:");
    	
    	Optional<String> result = addWordDialog.showAndWait();
    	if (result.isPresent() && !result.get().equals("")) {
    		selectedLevel.addWord(result.get());
    		updateList();
    	}
    }
    
    @FXML
    void importList() {
    	chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
				 new ExtensionFilter("All Files", "*"));
    	File file = chooser.showOpenDialog(new Stage());
    	if (file != null) {
        	try {
        	String customFilePath = file.getAbsolutePath();
				MainApp.instance().getUser().getGame().addImportedLists(customFilePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	updateList();
    }

}
