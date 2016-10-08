package se206_voxspell;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import se206_model.LevelModel;
import se206_model.StatsModel;
import se206_model.WordModel;

public class StatisticsMenuController {
	
	private ObservableList<String> _ob;
    private ArrayList<String> _levels = new ArrayList<>();
    private ArrayList<LevelModel> _lvModel = new ArrayList<>();

    @FXML
    private Button backBtn;

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
    void goBack(ActionEvent event) {
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
    
    public void fromMenu() {
    	for (LevelModel level: MainApp.instance().getUser().getGame().getLevels()) {
    		_levels.add(level.getName());
    		_lvModel.add(level);
    	}
    	
    	_ob = FXCollections.observableArrayList(_levels);
    	levelComboBox.getItems().addAll(_ob);
    	levelComboBox.getSelectionModel().select(0);
    	changeView();
    }
    @FXML
    void changeView() {
    	LevelModel selectedLevel = _lvModel.get(levelComboBox.getSelectionModel().getSelectedIndex());

		ObservableList<StatsModel> s = FXCollections.observableArrayList();

		for (WordModel w: selectedLevel.getWords()) {
			s.add(new StatsModel(w));
		}
	
		
		wordsTable.setItems(s);
		wordColumn.setCellValueFactory(cellData -> cellData.getValue().wordProperty());
		correctColumn.setCellValueFactory(cellData -> cellData.getValue().correctProperty());
		attemptsColumn.setCellValueFactory(cellData -> cellData.getValue().attemptsProperty());
		
		wordsTable.scrollTo(0);
		
		FilteredList<StatsModel> filter = new FilteredList<>(s, p -> true);
		//CODE REFERENCE: http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filter.setPredicate(word -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				String wordFilter = newValue.toLowerCase();
				if (word.getWord().toLowerCase().contains(wordFilter)) {
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
