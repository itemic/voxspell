package se206_voxspell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import se206_model.UserModel;
import se206_util.Save;

public class UserSelectionMenuController {

    private ObservableList<String> _ob;
    private ArrayList<String> _filenames = new ArrayList<>();
    private ArrayList<File> _files = new ArrayList<>();
    
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

    void setup() {
    	for (File f: MainApp.instance().getProfiles()) {
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
    void load() {
    	String filename = Save.DIRECTORY + loadProfileComboBox.getSelectionModel().getSelectedItem();
    	Save s = new Save(filename);
    	MainApp.instance().load(s);
    	MainApp.instance().startApp();
    }
    
    @FXML
    void add() throws IOException {
//    	String filename = Save.DIRECTORY + userTextField.getText() + Save.EXTENSION;
    	RadioButton rb = (RadioButton)wordlist.getSelectedToggle();
    	System.out.println("TOGGLE: " + rb.getText());
    	Save s;
    	if (rb.getText().equals("Default Wordlist")) {
        	s = new Save(new UserModel(userTextField.getText()));

    	} else {
    		FileChooser chooser = new FileChooser();
    	    File file = chooser.showOpenDialog(new Stage());
    	    String name = file.getAbsolutePath();
        	s = new Save(new UserModel(userTextField.getText(), name));
    	}

    	MainApp.instance().save(s);
    	MainApp.instance().load(s);
    	MainApp.instance().startApp();
    }
    
    

}
