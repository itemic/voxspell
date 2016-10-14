package se206_voxspell;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import se206_model.LevelModel;

public class ShopMenuController implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private Label costLabel;
    
    @FXML
    private Button buyBtn;
    
    @FXML
    private ComboBox<String> shopTypeCombo;

    @FXML
    private ListView<Object> salesListView;
    
    private ObservableList<String> _ob;
    private ArrayList<String> _selections = new ArrayList<>();

    
    @FXML
    void goBack(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
    		BorderPane back = (BorderPane)loader.load();
    		
    		BorderPane root = MainApp.getRoot();
    		root.setCenter(back);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    void fromMenu() {
    	_selections.add("Levels");
    	_selections.add("Soundtracks");
    	_ob = FXCollections.observableArrayList(_selections);
    	
    	shopTypeCombo.getItems().addAll(_ob);
    	shopTypeCombo.getSelectionModel().select(0);
    	updateList();
    }

    @FXML
    void updateList() {
    	salesListView.getItems().clear();
    	String selected = shopTypeCombo.getSelectionModel().getSelectedItem();
    	if (selected.equals("Levels")) {
    		for (LevelModel l: MainApp.instance().getUser().getGame().getLevels()) {
    			if (!l.canPlayLevel()) {
    				salesListView.getItems().add(l);
    			}
    		}
    	} else if (selected.equals("Soundtracks")) {
    		
    	}
    }
    
    @FXML
    void buyItem(ActionEvent evt) {
    	//TODO: DISABLE BUTTONS IF NOTHING TO BUY
    	String selected = shopTypeCombo.getSelectionModel().getSelectedItem();
    	if (selected == null) {
    		//nada
    	}
    	else if (selected.equals("Levels")) {
    		LevelModel selectedLevel = (LevelModel)salesListView.getSelectionModel().getSelectedItem();
        	if (MainApp.instance().getUser().getCurrency() >= selectedLevel.getLevelCost()) {
        		MainApp.instance().getUser().gainCurrency(-selectedLevel.getLevelCost()); //delete moneys
        		selectedLevel.toggleCanPlay();
        		MainApp.instance().getHUD().update();
        		salesListView.getItems().remove(selectedLevel);
        		updateList();
        	} else {
        		//not enough money :(
        		//let the user know!
        	}
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		salesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
		    	String selected = shopTypeCombo.getSelectionModel().getSelectedItem();
				LevelModel lv = (LevelModel)salesListView.getSelectionModel().getSelectedItem();

				if (selected.equals("Levels") && lv != null) {
					costLabel.setText("Cost: B$" + lv.getLevelCost());
				}
				
				if (lv == null) {
					costLabel.setText("");
				}
				
				
			}
			
		});
	}
}
