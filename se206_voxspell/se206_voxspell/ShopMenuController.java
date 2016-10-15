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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import se206_model.LevelModel;

public class ShopMenuController implements Initializable {

	private final int MUSIC_PRICE = 100;
	private final int LEVEL_PRICE = 25;
	
    @FXML
    private Button backBtn;

    @FXML
    private Label costLabel;
    
    @FXML
    private Button buyBtn;
    
    @FXML
    private Button soundtrackBtn;
   
    
    @FXML
    private ComboBox<String> shopTypeCombo;

    @FXML
    private ListView<Object> salesListView;
    
    private Alert noFunds = new Alert(AlertType.INFORMATION);
    
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
    		for (String song: MainApp.instance().getUser().getMusicList()) {
				salesListView.getItems().add(song);
    			}
    		}
    	}

    void showError() {
    	noFunds.setTitle("Insufficient Funds");
    	noFunds.setHeaderText("Not enough money!");
    	noFunds.setContentText("Earn more credits and come back!");
    }
    @FXML
    void buyItem(ActionEvent evt) {
    	int currentCurrency = MainApp.instance().getUser().getCurrency();
    	//TODO: DISABLE BUTTONS IF NOTHING TO BUY
    	String selected = shopTypeCombo.getSelectionModel().getSelectedItem();
    	if (selected == null) {
    		//nada
    	}
    	else if (selected.equals("Levels")) {
    		LevelModel selectedLevel = (LevelModel)salesListView.getSelectionModel().getSelectedItem();
        	if ( currentCurrency >= LEVEL_PRICE) {
        		MainApp.instance().getUser().gainCurrency(-LEVEL_PRICE); //delete moneys
        		selectedLevel.toggleCanPlay();
        		MainApp.instance().getHUD().update();
        		salesListView.getItems().remove(selectedLevel);
        		updateList();
        	} else {
        		//not enough money :(
        		//let the user know!
        	}
    	} else if (selected.equals("Soundtracks")) {
    		String selectedTrack = (String)salesListView.getSelectionModel().getSelectedItem();
    		int index = MainApp.instance().getUser().getMusicList().indexOf(selectedTrack);
    		if (MainApp.instance().getUser().getCanPlay(index)) {
    			// user already has it
    		} else {
    			// user hasn't unlocked it (button should be buy)
    			if (currentCurrency >= MUSIC_PRICE) {
    				MainApp.instance().getUser().gainCurrency(-MUSIC_PRICE);
    				MainApp.instance().getUser().setCanPlay(index);
    				costLabel.setText("Owned");
    				buyBtn.setVisible(false);
    				buyBtn.setManaged(false);
    				soundtrackBtn.setVisible(true);
    				soundtrackBtn.setManaged(true);
    				if (!selectedTrack.equals(MainApp.instance().getUser().getDisplaySoundtrack())) {
    					soundtrackBtn.setDisable(false);
    				}
    			} else {
    				//not enough money
    			}
    		}
    	}
    }
    
    @FXML
    void setSoundtrack() {
		String sound = (String)salesListView.getSelectionModel().getSelectedItem();
		MainApp.instance().getUser().setCurrentSoundtrack(sound);
		soundtrackBtn.setDisable(true);
//		updateList();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		buyBtn.setVisible(false);
//		buyBtn.setManaged(false);
		soundtrackBtn.setVisible(false);
//		soundtrackBtn.setManaged(false);
		//ref http://stackoverflow.com/questions/12459086/how-to-perform-an-action-by-selecting-an-item-from-listview-in-javafx-2
		salesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
		    	String selected = shopTypeCombo.getSelectionModel().getSelectedItem();
				soundtrackBtn.setVisible(false);
				soundtrackBtn.setManaged(false);
		    	if (selected.equals("Levels")) {
			    	LevelModel lv = (LevelModel)salesListView.getSelectionModel().getSelectedItem();
			    	if (lv != null) {
			    		costLabel.setText("Cost: B$" + LEVEL_PRICE);
						buyBtn.setVisible(true);
						buyBtn.setManaged(true);
						soundtrackBtn.setVisible(false);
						soundtrackBtn.setManaged(false);

			    	} else {
			    		costLabel.setText("");
						buyBtn.setVisible(false);
//						buyBtn.setManaged(false);
						soundtrackBtn.setVisible(false);
//						soundtrackBtn.setManaged(false);

			    	}
		    	} else if (selected.equals("Soundtracks")) {
		    		String sound = (String)salesListView.getSelectionModel().getSelectedItem();
		    		if (sound != null) {
		    			costLabel.setText("Cost: B$" + MUSIC_PRICE);
			    		int index = MainApp.instance().getUser().getMusicList().indexOf(sound);

		    			if (MainApp.instance().getUser().getCanPlay(index)) {
			    			//if user owns, hide BUY button show set or unset
		    				costLabel.setText("Owned");
			    			buyBtn.setVisible(false);
			    			buyBtn.setManaged(false);
			    			soundtrackBtn.setVisible(true);
			    			soundtrackBtn.setManaged(true);
			    			
			    			if (sound.equals(MainApp.instance().getUser().getDisplaySoundtrack())) {
			    				soundtrackBtn.setDisable(true);
			    			} else {
			    				soundtrackBtn.setDisable(false);
			    			}
			    			
			    		} else {
			    			//if user doesn't own it, show BUY dont show Set button
			    			buyBtn.setVisible(true);
			    			buyBtn.setManaged(true);
			    			soundtrackBtn.setVisible(false);
			    			soundtrackBtn.setManaged(false);
			    		}
		    			
		    		} else {
		    			costLabel.setText("");
		    			buyBtn.setVisible(false);
//		    			buyBtn.setManaged(false);
		    			soundtrackBtn.setVisible(false);
//		    			soundtrackBtn.setManaged(false);
		    		}

		    		
		    	}
				
			}
			
		});
	}
}
