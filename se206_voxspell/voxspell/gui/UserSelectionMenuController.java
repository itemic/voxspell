package voxspell.gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import voxspell.model.GameType;
import voxspell.model.UserModel;
import voxspell.util.FileHandler;
import voxspell.util.MediaHandler;
import voxspell.util.Save;

public class UserSelectionMenuController implements Initializable {

    private ObservableList<String> _ob;
    private ArrayList<String> _filenames = new ArrayList<>();
    private ArrayList<File> _files = new ArrayList<>();
	private FileChooser chooser = new FileChooser();
	private String customFilePath;
    
    @FXML
    private TextField userTextField;

    @FXML
    private RadioButton defaultRadio;

    @FXML
    private ToggleGroup wordlist;

    @FXML
    private RadioButton customRadio;

    @FXML
    private Button addUserPlayBtn;

    @FXML
    private ComboBox<String> loadProfileComboBox;

    @FXML
    private Button loadUserPlayBtn;
    
    @FXML
    private Button loadCustomBtn;

    @FXML
    private RadioButton newUserRadio;

    @FXML
    private ToggleGroup userGroup;

    @FXML
    private RadioButton existingUserRadio;

    @FXML
    private VBox newUserVbox;
    
    @FXML
    private VBox existingUserVbox;
    
    @FXML
    private Label customLabel;
    
    @FXML
    private RadioButton freePlayRadio;
    
    @FXML
    private RadioButton challengeRadio;
    
    @FXML
    private ToggleGroup playMode;
    
    Alert alert = new Alert(AlertType.ERROR);


    void setup() {
    	MediaHandler.stop();
    	_files.clear();
    	_filenames.clear();
    	for (File f: FileHandler.findProfiles()) {
    		_files.add(f);
    		_filenames.add(f.getName());
    	}
    	if (_files.size() == 0) {
    		//no files so myeh
    		loadUserPlayBtn.setVisible(false);
    	} else {
    	_ob = FXCollections.observableArrayList(_filenames);
    	loadProfileComboBox.getItems().addAll(_ob);
    	loadProfileComboBox.getSelectionModel().select(0);
    	}
    }
    @FXML
    void showCustom() {
    	loadCustomBtn.setVisible(true);
    	customLabel.setVisible(true);
    }
    @FXML
    void hideCustom() {
    	loadCustomBtn.setVisible(false);
    	customLabel.setText("No file selected...");
    	customFilePath = null;
    	customLabel.setVisible(false);
    }
    
    @FXML
    void load() {
    	String filename = Save.DIRECTORY + loadProfileComboBox.getSelectionModel().getSelectedItem();
    	Save s = new Save(filename);
    	MainApp.instance().load(s);
    	MainApp.instance().startApp();
    }
    
    //TODO further file validation
    
    @FXML
    void add() throws IOException {
    	String filename = Save.DIRECTORY + userTextField.getText().trim() + Save.EXTENSION;
		RadioButton rbWordlist = (RadioButton)wordlist.getSelectedToggle();
		RadioButton rbMode = (RadioButton)playMode.getSelectedToggle();
		//Does the file exist already? (We don't want to overwrite)
    	boolean fileExists =  (new File(filename).exists());
    	
    	//no file selected in custom
    	boolean noCustomFile = rbWordlist.getText().equals("Custom Wordlist") && (customFilePath == null);
    	
    	//Did the user specify a username?
    	boolean fileNameEmpty = userTextField.getText().trim().isEmpty();
    	
    	if (fileExists || noCustomFile || fileNameEmpty) {
    		if (fileExists) {
    			alert.setTitle("User exists");
    	    	alert.setHeaderText("This user exists already.");
    	    	alert.setContentText("Choose a new name.");
    	    	userTextField.requestFocus();
    		} else if (fileNameEmpty) {
    			alert.setTitle("No username.");
    	    	alert.setHeaderText("No username.");
    	    	alert.setContentText("Choose a username for yourself.");
    	    	userTextField.requestFocus();
    		} else {
    			alert.setTitle("No wordlist chosen");
    	    	alert.setHeaderText("VOXSpell can't find a wordlist.");
    	    	alert.setContentText("Choose a wordlist or use the default.");
    		}
    		alert.showAndWait();
    	} else {
    		Save s;
    		if (rbWordlist.getText().equals("Default Wordlist")) {
    			if (rbMode.getText().equals("Challenge Mode")) {
                	s = new Save(new UserModel(userTextField.getText(), GameType.CHALLENGE));
    			} else {
                	s = new Save(new UserModel(userTextField.getText(), GameType.FREEPLAY));
    			}
    			//DEFAULT

    		} else {
    			//CUSTOM
    			if (rbMode.getText().equals("Challenge Mode")) {
    				s = new Save(new UserModel(userTextField.getText(), customFilePath, GameType.CHALLENGE));	
    			} else {
    				s = new Save(new UserModel(userTextField.getText(), customFilePath, GameType.FREEPLAY));
    			}
            	

    		}
    		MainApp.instance().save(s);
        	MainApp.instance().load(s);
        	MainApp.instance().startApp();
    	}    	
    	
    }
    

    
    @FXML
    void filePick() {
    	File file = chooser.showOpenDialog(new Stage());
    	if (file != null) {
        	customFilePath = file.getAbsolutePath();
        	String name = file.getName();
        	customLabel.setText(name);
    	}

    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		setup();
		MainApp.instance().changeWindowTitle("VOXSpell 1.0");
		hideCustom();
		showNewUser();
		//http://docs.oracle.com/javase/8/javafx/api/javafx/stage/FileChooser.html
		chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
											 new ExtensionFilter("All Files", "*"));
		
	}
    
	
   @FXML
    void showExistingUser() {
	   existingUserVbox.setVisible(true);
	   newUserVbox.setVisible(false);
	   existingUserVbox.setManaged(true);
	   newUserVbox.setManaged(false);
	   
	   addUserPlayBtn.setVisible(false);
	   loadUserPlayBtn.setVisible(true);
	   addUserPlayBtn.setManaged(false);
	   loadUserPlayBtn.setManaged(true);
    }

    @FXML
    void showNewUser() {
		existingUserVbox.setVisible(false);
		newUserVbox.setVisible(true);
		existingUserVbox.setManaged(false);
		newUserVbox.setManaged(true);
		
		addUserPlayBtn.setVisible(true);
		loadUserPlayBtn.setVisible(false);
		addUserPlayBtn.setManaged(true);
		loadUserPlayBtn.setManaged(false);
    }

    

}
