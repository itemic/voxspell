package voxspell.gui;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import voxspell.model.LevelModel;

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

	private ObservableList<String> _ob;
	private ArrayList<String> _selections = new ArrayList<>();

	@FXML
	private ImageView shopImage;

	@FXML
	void goBack(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("HomeMenu.fxml"));
			BorderPane back = (BorderPane) loader.load();

			BorderPane root = MainApp.getRoot();
			root.setCenter(back);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void initShop() {
		// set up shop with the two purchasable categories
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
			// populates levels if that is selected
			for (LevelModel l : MainApp.instance().getUser().getGame().getLevels()) {
				if (!l.canPlayLevel()) {
					salesListView.getItems().add(l);
				}
			}
		} else if (selected.equals("Soundtracks")) {
			// populate songs
			for (String song : MainApp.instance().getUser().getMusicList()) {
				salesListView.getItems().add(song);
			}
		}

		costLabel.setText("" + MainApp.instance().getUser().getCurrency());

	}

	@FXML
	void buyItem(ActionEvent evt) {
		int currentCurrency = MainApp.instance().getUser().getCurrency();
		String selected = shopTypeCombo.getSelectionModel().getSelectedItem();
		if (selected == null) {

		} else if (selected.equals("Levels")) {
			LevelModel selectedLevel = (LevelModel) salesListView.getSelectionModel().getSelectedItem();
			if (currentCurrency >= LEVEL_PRICE) {
				MainApp.instance().getUser().gainCurrency(-LEVEL_PRICE); // spend the money needed
				selectedLevel.toggleCanPlay();
				salesListView.getItems().remove(selectedLevel);
				//if user buys level, remove that from the list
				updateList();
			} 
		} else if (selected.equals("Soundtracks")) {
			String selectedTrack = (String) salesListView.getSelectionModel().getSelectedItem();
			int index = MainApp.instance().getUser().getMusicList().indexOf(selectedTrack);
			if (MainApp.instance().getUser().getCanPlay(index)) {
				// user already has it
			} else {
				// user hasn't unlocked it (button should be buy)
				if (currentCurrency >= MUSIC_PRICE) {
					MainApp.instance().getUser().gainCurrency(-MUSIC_PRICE);
					MainApp.instance().getUser().setCanPlay(index);
					costLabel.setText("Owned"); //keep song in list
					buyBtn.setVisible(false); 
					buyBtn.setManaged(false); // hide buy button (it's purchased already)
					soundtrackBtn.setVisible(true);
					soundtrackBtn.setManaged(true); // show set soundtrack button
					if (!selectedTrack.equals(MainApp.instance().getUser().getDisplaySoundtrack())) {
						// if the selected sountrack is the current soundtrack, disable the set button
						soundtrackBtn.setDisable(false);
					}
				} else {
				}
			}
		}
	}

	@FXML
	void setSoundtrack() {
		String sound = (String) salesListView.getSelectionModel().getSelectedItem();
		MainApp.instance().getUser().setCurrentSoundtrack(sound);
		soundtrackBtn.setDisable(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image coin = new Image("resources/voxcoin.png");
		shopImage.setImage(coin);
		buyBtn.setVisible(false);
		soundtrackBtn.setVisible(false);
		costLabel.setText("" + MainApp.instance().getUser().getCurrency());
		// reference:
		// http://stackoverflow.com/questions/12459086/how-to-perform-an-action-by-selecting-an-item-from-listview-in-javafx-2
		
		salesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
				// Listener for changing the selected object
				String selected = shopTypeCombo.getSelectionModel().getSelectedItem();
				soundtrackBtn.setVisible(false);
				soundtrackBtn.setManaged(false);
				if (selected.equals("Levels")) {
					LevelModel lv = (LevelModel) salesListView.getSelectionModel().getSelectedItem();
					if (lv != null) {
						buyBtn.setText("Buy (β$" + LEVEL_PRICE + ")");
						buyBtn.setVisible(true);
						buyBtn.setManaged(true);
						soundtrackBtn.setVisible(false);
						soundtrackBtn.setManaged(false);
						int currency = MainApp.instance().getUser().getCurrency();
						if (currency < LEVEL_PRICE) {
							//disable buy button if user doesn't have enough money/credits
							buyBtn.setDisable(true);
						} else {
							buyBtn.setDisable(false);
						}

					} else {
						// don't show anything if nothing is selected
						costLabel.setText("" + MainApp.instance().getUser().getCurrency());
						buyBtn.setVisible(false);
						soundtrackBtn.setVisible(false);

					}
				} else if (selected.equals("Soundtracks")) {
					//if the user selects a new soundtrack entry
					String sound = (String) salesListView.getSelectionModel().getSelectedItem();
					if (sound != null) {
						buyBtn.setText("Buy (β$" + MUSIC_PRICE + ")");
						int index = MainApp.instance().getUser().getMusicList().indexOf(sound);

						if (MainApp.instance().getUser().getCanPlay(index)) {
							// if user owns, hide BUY button show set or unset
							costLabel.setText("Owned");

							buyBtn.setVisible(false);
							buyBtn.setManaged(false);
							soundtrackBtn.setVisible(true);
							soundtrackBtn.setManaged(true);

							int currency = MainApp.instance().getUser().getCurrency();
							if (currency < MUSIC_PRICE) {
								//disable buy button if user doesnt have funds
								buyBtn.setDisable(true);
							} else {
								buyBtn.setDisable(false);
							}

							if (sound.equals(MainApp.instance().getUser().getDisplaySoundtrack())) {
								soundtrackBtn.setDisable(true);
							} else {
								soundtrackBtn.setDisable(false);
							}

						} else {
							// if user doesn't own it, show BUY dont show SET
							buyBtn.setVisible(true);
							buyBtn.setManaged(true);
							soundtrackBtn.setVisible(false);
							soundtrackBtn.setManaged(false);
							costLabel.setText("" + MainApp.instance().getUser().getCurrency());
						}

					} else {
						costLabel.setText("" + MainApp.instance().getUser().getCurrency());
						buyBtn.setVisible(false);
						soundtrackBtn.setVisible(false);
					}

				}

			}

		});
	}
}
