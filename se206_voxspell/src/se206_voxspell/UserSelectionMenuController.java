package se206_voxspell;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import se206_model.UserModel;
import se206_util.FileHandler;
import se206_util.Save;

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
    private ChoiceBox<String> loadProfileComboBox;

    @FXML
    private Button loadUserPlayBtn;
    
    @FXML
    private Button loadCustomBtn;

    @FXML
    private Label customLabel;

    void setup() {
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
    	String filename = Save.DIRECTORY + userTextField.getText() + Save.EXTENSION;
		RadioButton rb = (RadioButton)wordlist.getSelectedToggle();
		
		//Does the file exist already? (We don't want to overwrite)
    	boolean fileExists =  (new File(filename).exists());
    	
    	//no file selected in custom
    	boolean noCustomFile = rb.getText().equals("Custom Wordlist") && (customFilePath == null);
    	
    	//Did the user specify a username?
    	boolean fileNameEmpty = userTextField.getText().isEmpty();
    	
    	if (fileExists || noCustomFile || fileNameEmpty) {
    		System.out.println("ERROR: add alert");
    	} else {
    		Save s;
    		if (rb.getText().equals("Default Wordlist")) {
    			//DEFAULT
            	s = new Save(new UserModel(userTextField.getText()));

    		} else {
    			//CUSTOM
            	s = new Save(new UserModel(userTextField.getText(), customFilePath));

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
		hideCustom();
		//http://docs.oracle.com/javase/8/javafx/api/javafx/stage/FileChooser.html
		chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
											 new ExtensionFilter("All Files", "*"));
		
	}
    
    

}
