package voxspell.gui;

import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import voxspell.model.LevelModel;
import voxspell.model.StatsModel;
import voxspell.model.WordModel;

/**
 * Controller class for Statistics
 * @author terran
 *
 */
public class StatisticsMenuController {
	
	private ObservableList<String> _ob;
    private ArrayList<String> _levels = new ArrayList<>();
    private ArrayList<LevelModel> _lvModel = new ArrayList<>();

    @FXML
    private Button backBtn;
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label levelAccuracyLabel;

    @FXML
    private ComboBox<String> levelComboBox;
    
    @FXML
    private TextField searchField;

    @FXML
    private TableView<StatsModel> wordsTable;

    @FXML
    private TableColumn<StatsModel, String> wordColumn;
    
    @FXML
    private TableColumn<StatsModel, String> correctColumn;
    
    @FXML
    private TableColumn<StatsModel, String> attemptsColumn;
    
    @FXML
    private TableColumn<StatsModel, String> accuracyColumn;
    
    @FXML
    private TableColumn<StatsModel, String> lastPracticedColumn;
    
    
    
    /**
     * Button action to go back
     * @param event
     */
    @FXML
    void goBack(ActionEvent event) { // go back to main menu
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
    
    /**
     * Populates the stats table with the first level
     */
    public void initStats() {
    	// set up the title and populate the stats table
    	titleLabel.setText(MainApp.instance().getUser().toString().toUpperCase()+"'S STATS");
    	for (LevelModel level: MainApp.instance().getUser().getGame().getLevels()) {
    		_levels.add(level.toString());
    		_lvModel.add(level);
    	}
    	
    	_ob = FXCollections.observableArrayList(_levels);
    	levelComboBox.getItems().addAll(_ob);
    	levelComboBox.getSelectionModel().select(0);
    	changeView();
    }
    
    /**
     * Action when the user chooses a different level from the dropdown
     * Repopulate the table
     */
    @FXML
    void changeView() {
    	LevelModel selectedLevel = _lvModel.get(levelComboBox.getSelectionModel().getSelectedIndex());
    	levelAccuracyLabel.setText("Level Accuracy: " + selectedLevel.levelAccuracy());
		ObservableList<StatsModel> s = FXCollections.observableArrayList();

		for (WordModel w: selectedLevel.getWords()) {
			s.add(new StatsModel(w));
		}
	
		//sets up the columns
		wordsTable.setItems(s);
		wordColumn.setCellValueFactory(cellData -> cellData.getValue().wordProperty());
		accuracyColumn.setCellValueFactory(cellData -> cellData.getValue().accuracyProperty());
		correctColumn.setCellValueFactory(cellData -> cellData.getValue().correctProperty());
		attemptsColumn.setCellValueFactory(cellData -> cellData.getValue().attemptsProperty());
		lastPracticedColumn.setCellValueFactory(cellData -> cellData.getValue().lastTriedProperty());

		
		wordsTable.scrollTo(0);
		
		//following code used for the search function
		FilteredList<StatsModel> filter = new FilteredList<>(s, p -> true);
		//CODE REFERENCE: http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filter.setPredicate(word -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
					//Show all words if the text box is empty
				}
				
				String wordFilter = newValue.toLowerCase();
				if (word.getWord().toLowerCase().contains(wordFilter)) { // no case sensitivity
					return true;
				}
				return false;
			});
		});
		
		SortedList<StatsModel> sorted = new SortedList<>(filter);
		sorted.comparatorProperty().bind(wordsTable.comparatorProperty());
		wordsTable.setItems(sorted);
    }

}
