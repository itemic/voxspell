package voxspell.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import voxspell.util.MediaHandler;

/**
 * Controller class for the About page
 * @author terran
 *
 */
public class HelpScreenController implements Initializable {

    @FXML
    private Button backBtn;

    /**
     * Button action for the user to go back to Settings
     * @param event
     */
    @FXML
    void goBack(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("SettingsMenu.fxml"));
    		BorderPane settings = (BorderPane)loader.load();
    		SettingsMenuController controller = loader.<SettingsMenuController>getController();
    		controller.initSettings(); //code to initialize settings once we return to it
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(settings);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    /**
     * Ensures that whatever music was playing stops once you enter this screen.
     */
	@Override 
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		MediaHandler.stop();
		
	}

}
